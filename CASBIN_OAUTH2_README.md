# RuoYi-Cloud Casbin & OAuth2.0 集成指南

## 概述

本项目已成功集成了Casbin权限管理框架和OAuth2.0授权体系，提供了更加灵活和强大的权限控制能力。

## 主要功能

### 1. Casbin权限管理
- **基于RBAC模型的权限控制**
- **支持复杂的权限策略定义**
- **数据库和文件两种适配器支持**
- **动态权限策略管理**
- **与原有权限系统兼容**

### 2. OAuth2.0授权体系
- **标准OAuth2.0授权服务器**
- **支持授权码、客户端凭证等多种授权模式**
- **JWT令牌支持**
- **第三方应用集成**
- **自授权和外部OAuth2.0服务器支持**
- **客户端动态注册和管理**
- **JPA数据持久化**
- **第三方OAuth服务器配置管理**

## 项目结构

```
RuoYi-Cloud/
├── ruoyi-common/
│   ├── ruoyi-common-casbin/          # Casbin权限管理模块
│   │   ├── config/                   # Casbin配置
│   │   ├── service/                  # Casbin服务
│   │   ├── annotation/               # 权限注解
│   │   ├── aspect/                   # AOP切面
│   │   ├── auth/                     # 权限逻辑
│   │   └── adapter/                  # 数据库适配器
│   └── ruoyi-common-oauth2/          # OAuth2.0客户端模块
│       ├── config/                   # OAuth2.0配置
│       └── service/                  # OAuth2.0服务
├── ruoyi-oauth2-server/              # OAuth2.0授权服务器
│   ├── config/                       # 授权服务器配置
│   └── controller/                   # OAuth2.0控制器
└── sql/
    └── casbin_rule.sql               # Casbin数据库表结构
```

## 快速开始

### 1. 数据库初始化

执行SQL脚本创建Casbin相关表：

```sql
-- 执行 sql/casbin_rule.sql 文件
source sql/casbin_rule.sql;
```

### 2. 启动OAuth2.0授权服务器

```bash
# 启动OAuth2.0授权服务器（端口9000）
cd ruoyi-oauth2-server
mvn spring-boot:run
```

### 3. 配置Casbin

在应用配置文件中添加Casbin配置：

```yaml
# Casbin配置
casbin:
  model: casbin/rbac_model.conf
  adapter:
    type: database  # 使用数据库适配器
  enable-auto-save: true
  enable-log: true
```

### 4. 使用Casbin权限控制

#### 4.1 注解方式

```java
@RestController
public class UserController {
    
    @CasbinEnforce(subject = "#{authentication.name}", 
                   object = "system:user", 
                   action = "read")
    @GetMapping("/users")
    public List<User> getUsers() {
        // 业务逻辑
    }
    
    @CasbinEnforce(subject = "#{authentication.name}", 
                   object = "system:user", 
                   action = "write")
    @PostMapping("/users")
    public User createUser(@RequestBody User user) {
        // 业务逻辑
    }
}
```

#### 4.2 编程方式

```java
@Service
public class UserService {
    
    public void deleteUser(Long userId) {
        // 检查权限
        AuthUtil.checkEnforce(
            SecurityUtils.getUsername(), 
            "system:user", 
            "delete"
        );
        
        // 业务逻辑
        userRepository.deleteById(userId);
    }
}
```

### 5. OAuth2.0授权流程

#### 5.1 获取授权码

```
GET http://localhost:9000/oauth2/authorize?
    response_type=code&
    client_id=ruoyi-client&
    redirect_uri=http://127.0.0.1:8080/authorized&
    scope=read write&
    state=xyz
```

#### 5.2 获取访问令牌

```bash
curl -X POST http://localhost:9000/oauth2/token \
  -H "Content-Type: application/x-www-form-urlencoded" \
  -H "Authorization: Basic cnVveWktY2xpZW50OnJ1b3lpLXNlY3JldA==" \
  -d "grant_type=authorization_code&code=AUTHORIZATION_CODE&redirect_uri=http://127.0.0.1:8080/authorized"
```

#### 5.3 使用访问令牌

```bash
curl -H "Authorization: Bearer ACCESS_TOKEN" \
  http://localhost:8080/api/users
```

## API接口

### Casbin权限管理API

| 接口 | 方法 | 描述 |
|------|------|------|
| `/casbin/enforce` | GET | 检查权限 |
| `/casbin/policy` | POST | 添加权限策略 |
| `/casbin/policy` | DELETE | 删除权限策略 |
| `/casbin/role` | POST | 为用户添加角色 |
| `/casbin/role` | DELETE | 删除用户角色 |

### OAuth2.0 API

| 接口 | 方法 | 描述 |
|------|------|------|
| `/oauth2/authorize` | GET | 获取授权码 |
| `/oauth2/token` | POST | 获取访问令牌 |
| `/oauth2/user` | GET | 获取用户信息 |
| `/oauth2/token/status` | GET | 检查令牌状态 |
| `/oauth2/revoke` | DELETE | 撤销授权 |

### 客户端管理 API

| 接口 | 方法 | 描述 |
|------|------|------|
| `/oauth2/clients/register` | POST | 注册新客户端 |
| `/oauth2/clients/{clientId}` | GET | 获取客户端详情 |
| `/oauth2/clients/{clientId}` | PUT | 更新客户端 |
| `/oauth2/clients/{clientId}` | DELETE | 删除客户端 |
| `/oauth2/clients/{clientId}/reset-secret` | POST | 重置客户端密钥 |
| `/oauth2/clients/{clientId}/toggle-status` | POST | 启用/禁用客户端 |
| `/oauth2/clients` | GET | 分页查询客户端 |
| `/oauth2/clients/enabled` | GET | 获取启用的客户端 |

### 第三方OAuth服务器管理 API

| 接口 | 方法 | 描述 |
|------|------|------|
| `/oauth2/third-party-servers` | POST | 添加第三方OAuth服务器 |
| `/oauth2/third-party-servers/{id}` | GET | 获取服务器详情 |
| `/oauth2/third-party-servers/{id}` | PUT | 更新服务器配置 |
| `/oauth2/third-party-servers/{id}` | DELETE | 删除服务器 |
| `/oauth2/third-party-servers/{id}/toggle-status` | POST | 启用/禁用服务器 |
| `/oauth2/third-party-servers` | GET | 分页查询服务器 |
| `/oauth2/third-party-servers/enabled` | GET | 获取启用的服务器 |
| `/oauth2/third-party-servers/{id}/test-connection` | POST | 测试服务器连接 |

## 权限模型

### RBAC模型配置

```ini
[request_definition]
r = sub, obj, act

[policy_definition]
p = sub, obj, act

[role_definition]
g = _, _

[policy_effect]
e = some(where (p.eft == allow))

[matchers]
m = g(r.sub, p.sub) && keyMatch2(r.obj, p.obj) && regexMatch(r.act, p.act)
```

### 权限策略示例

```csv
p, admin, system:user, read
p, admin, system:user, write
p, admin, system:role, read
p, admin, system:role, write
p, user, system:profile, read
p, user, system:profile, write
g, admin, admin_role
g, user, user_role
```

## 配置说明

### Casbin配置项

| 配置项 | 默认值 | 描述 |
|--------|--------|------|
| `casbin.model` | `casbin/rbac_model.conf` | 权限模型文件路径 |
| `casbin.policy` | `casbin_policy.csv` | 策略文件路径 |
| `casbin.adapter.type` | `database` | 适配器类型 |
| `casbin.enable-auto-save` | `true` | 是否自动保存 |
| `casbin.enable-log` | `false` | 是否启用日志 |

### OAuth2.0配置项

| 配置项 | 描述 |
|--------|------|
| `spring.security.oauth2.authorizationserver.issuer` | 授权服务器地址 |
| `spring.security.oauth2.authorizationserver.client` | 客户端配置 |

## 最佳实践

### 1. 权限设计原则

- **最小权限原则**：用户只获得完成工作所需的最小权限
- **职责分离**：不同角色承担不同的职责
- **权限继承**：通过角色继承简化权限管理

### 2. 安全建议

- **定期审计权限**：定期检查和清理不必要的权限
- **使用HTTPS**：在生产环境中使用HTTPS
- **令牌过期时间**：设置合适的令牌过期时间
- **密钥管理**：妥善保管客户端密钥

### 3. 性能优化

- **权限缓存**：对频繁访问的权限进行缓存
- **批量操作**：使用批量API减少数据库访问
- **索引优化**：为权限表创建合适的索引

## 故障排除

### 常见问题

1. **Casbin策略不生效**
   - 检查模型文件配置
   - 确认策略格式正确
   - 验证数据库连接

2. **OAuth2.0授权失败**
   - 检查客户端ID和密钥
   - 确认重定向URI配置
   - 验证授权服务器状态

3. **权限检查异常**
   - 检查用户认证状态
   - 确认权限策略存在
   - 验证SpEL表达式语法

### 日志配置

```yaml
logging:
  level:
    org.springframework.security: DEBUG
    org.casbin: DEBUG
    com.ruoyi: DEBUG
```

## 扩展开发

### 自定义权限检查器

```java
@Component
public class CustomPermissionChecker {
    
    @Autowired
    private CasbinService casbinService;
    
    public boolean hasCustomPermission(String user, String resource) {
        // 自定义权限逻辑
        return casbinService.enforce(user, resource, "custom");
    }
}
```

### 自定义OAuth2.0客户端

```java
@Configuration
public class CustomOAuth2ClientConfig {
    
    @Bean
    public ClientRegistrationRepository customClientRegistrationRepository() {
        // 自定义客户端注册
        return new InMemoryClientRegistrationRepository(
            customClientRegistration()
        );
    }
}
```

## 版本信息

- **Casbin版本**: 1.41.2
- **Spring Security OAuth2**: 2.5.2
- **Spring Boot**: 2.7.x
- **RuoYi-Cloud**: 3.6.6

## 联系支持

如有问题或建议，请通过以下方式联系：

- 提交Issue到项目仓库
- 发送邮件到开发团队
- 查看官方文档和示例

---

**注意**: 在生产环境中部署前，请确保完成安全配置和性能测试。