package com.ruoyi.oauth2.server.controller;

import com.ruoyi.oauth2.server.dto.ThirdPartyServerRequest;
import com.ruoyi.oauth2.server.entity.OAuth2ThirdPartyServerEntity;
import com.ruoyi.oauth2.server.service.OAuth2ThirdPartyServerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 第三方OAuth2.0服务器管理控制器
 * 
 * @author ruoyi
 */
@RestController
@RequestMapping("/oauth2/third-party-servers")
@Validated
public class OAuth2ThirdPartyServerController
{
    @Autowired
    private OAuth2ThirdPartyServerService thirdPartyServerService;

    /**
     * 添加第三方OAuth服务器
     */
    @PostMapping
    public ResponseEntity<Map<String, Object>> addThirdPartyServer(@Valid @RequestBody ThirdPartyServerRequest request)
    {
        try
        {
            OAuth2ThirdPartyServerEntity entity = thirdPartyServerService.addThirdPartyServer(request);
            
            Map<String, Object> result = new HashMap<>();
            result.put("code", 200);
            result.put("message", "第三方OAuth服务器添加成功");
            result.put("data", entity);
            
            return ResponseEntity.ok(result);
        }
        catch (Exception e)
        {
            Map<String, Object> result = new HashMap<>();
            result.put("code", 500);
            result.put("message", "第三方OAuth服务器添加失败: " + e.getMessage());
            
            return ResponseEntity.status(500).body(result);
        }
    }

    /**
     * 更新第三方OAuth服务器
     */
    @PutMapping("/{id}")
    public ResponseEntity<Map<String, Object>> updateThirdPartyServer(
            @PathVariable Long id,
            @Valid @RequestBody ThirdPartyServerRequest request)
    {
        try
        {
            OAuth2ThirdPartyServerEntity entity = thirdPartyServerService.updateThirdPartyServer(id, request);
            
            Map<String, Object> result = new HashMap<>();
            result.put("code", 200);
            result.put("message", "第三方OAuth服务器更新成功");
            result.put("data", entity);
            
            return ResponseEntity.ok(result);
        }
        catch (Exception e)
        {
            Map<String, Object> result = new HashMap<>();
            result.put("code", 500);
            result.put("message", "第三方OAuth服务器更新失败: " + e.getMessage());
            
            return ResponseEntity.status(500).body(result);
        }
    }

    /**
     * 获取第三方OAuth服务器详情
     */
    @GetMapping("/{id}")
    public ResponseEntity<Map<String, Object>> getThirdPartyServer(@PathVariable Long id)
    {
        try
        {
            OAuth2ThirdPartyServerEntity entity = thirdPartyServerService.getThirdPartyServer(id);
            
            Map<String, Object> result = new HashMap<>();
            result.put("code", 200);
            result.put("message", "获取成功");
            result.put("data", entity);
            
            return ResponseEntity.ok(result);
        }
        catch (Exception e)
        {
            Map<String, Object> result = new HashMap<>();
            result.put("code", 404);
            result.put("message", e.getMessage());
            
            return ResponseEntity.status(404).body(result);
        }
    }

    /**
     * 根据服务器代码获取
     */
    @GetMapping("/by-code/{serverCode}")
    public ResponseEntity<Map<String, Object>> getByServerCode(@PathVariable String serverCode)
    {
        try
        {
            OAuth2ThirdPartyServerEntity entity = thirdPartyServerService.getByServerCode(serverCode);
            
            Map<String, Object> result = new HashMap<>();
            result.put("code", 200);
            result.put("message", "获取成功");
            result.put("data", entity);
            
            return ResponseEntity.ok(result);
        }
        catch (Exception e)
        {
            Map<String, Object> result = new HashMap<>();
            result.put("code", 404);
            result.put("message", e.getMessage());
            
            return ResponseEntity.status(404).body(result);
        }
    }

    /**
     * 启用/禁用第三方OAuth服务器
     */
    @PostMapping("/{id}/toggle-status")
    public ResponseEntity<Map<String, Object>> toggleServerStatus(@PathVariable Long id)
    {
        try
        {
            thirdPartyServerService.toggleServerStatus(id);
            
            Map<String, Object> result = new HashMap<>();
            result.put("code", 200);
            result.put("message", "第三方OAuth服务器状态切换成功");
            
            return ResponseEntity.ok(result);
        }
        catch (Exception e)
        {
            Map<String, Object> result = new HashMap<>();
            result.put("code", 500);
            result.put("message", "第三方OAuth服务器状态切换失败: " + e.getMessage());
            
            return ResponseEntity.status(500).body(result);
        }
    }

    /**
     * 删除第三方OAuth服务器
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, Object>> deleteThirdPartyServer(@PathVariable Long id)
    {
        try
        {
            thirdPartyServerService.deleteThirdPartyServer(id);
            
            Map<String, Object> result = new HashMap<>();
            result.put("code", 200);
            result.put("message", "第三方OAuth服务器删除成功");
            
            return ResponseEntity.ok(result);
        }
        catch (Exception e)
        {
            Map<String, Object> result = new HashMap<>();
            result.put("code", 500);
            result.put("message", "第三方OAuth服务器删除失败: " + e.getMessage());
            
            return ResponseEntity.status(500).body(result);
        }
    }

    /**
     * 分页查询第三方OAuth服务器
     */
    @GetMapping
    public ResponseEntity<Map<String, Object>> getThirdPartyServers(
            @RequestParam(required = false) String serverName,
            @RequestParam(required = false) String providerType,
            @RequestParam(required = false) Integer status,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "sortOrder") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDir)
    {
        try
        {
            Sort sort = Sort.by(Sort.Direction.fromString(sortDir), sortBy);
            Pageable pageable = PageRequest.of(page, size, sort);
            
            Page<OAuth2ThirdPartyServerEntity> servers = thirdPartyServerService.getThirdPartyServers(serverName, providerType, status, pageable);
            
            Map<String, Object> result = new HashMap<>();
            result.put("code", 200);
            result.put("message", "查询成功");
            result.put("data", servers.getContent());
            result.put("total", servers.getTotalElements());
            result.put("totalPages", servers.getTotalPages());
            result.put("currentPage", servers.getNumber());
            result.put("size", servers.getSize());
            
            return ResponseEntity.ok(result);
        }
        catch (Exception e)
        {
            Map<String, Object> result = new HashMap<>();
            result.put("code", 500);
            result.put("message", "查询失败: " + e.getMessage());
            
            return ResponseEntity.status(500).body(result);
        }
    }

    /**
     * 获取启用的第三方OAuth服务器列表
     */
    @GetMapping("/enabled")
    public ResponseEntity<Map<String, Object>> getEnabledThirdPartyServers()
    {
        try
        {
            List<OAuth2ThirdPartyServerEntity> servers = thirdPartyServerService.getEnabledThirdPartyServers();
            
            Map<String, Object> result = new HashMap<>();
            result.put("code", 200);
            result.put("message", "查询成功");
            result.put("data", servers);
            
            return ResponseEntity.ok(result);
        }
        catch (Exception e)
        {
            Map<String, Object> result = new HashMap<>();
            result.put("code", 500);
            result.put("message", "查询失败: " + e.getMessage());
            
            return ResponseEntity.status(500).body(result);
        }
    }

    /**
     * 根据提供商类型获取服务器
     */
    @GetMapping("/by-provider/{providerType}")
    public ResponseEntity<Map<String, Object>> getByProviderType(@PathVariable String providerType)
    {
        try
        {
            List<OAuth2ThirdPartyServerEntity> servers = thirdPartyServerService.getByProviderType(providerType);
            
            Map<String, Object> result = new HashMap<>();
            result.put("code", 200);
            result.put("message", "查询成功");
            result.put("data", servers);
            
            return ResponseEntity.ok(result);
        }
        catch (Exception e)
        {
            Map<String, Object> result = new HashMap<>();
            result.put("code", 500);
            result.put("message", "查询失败: " + e.getMessage());
            
            return ResponseEntity.status(500).body(result);
        }
    }

    /**
     * 测试第三方OAuth服务器连接
     */
    @PostMapping("/{id}/test-connection")
    public ResponseEntity<Map<String, Object>> testConnection(@PathVariable Long id)
    {
        try
        {
            boolean isConnected = thirdPartyServerService.testThirdPartyServerConnection(id);
            
            Map<String, Object> result = new HashMap<>();
            result.put("code", 200);
            result.put("message", isConnected ? "连接测试成功" : "连接测试失败");
            result.put("data", Map.of("connected", isConnected));
            
            return ResponseEntity.ok(result);
        }
        catch (Exception e)
        {
            Map<String, Object> result = new HashMap<>();
            result.put("code", 500);
            result.put("message", "连接测试失败: " + e.getMessage());
            
            return ResponseEntity.status(500).body(result);
        }
    }

    /**
     * 获取提供商类型统计
     */
    @GetMapping("/provider-statistics")
    public ResponseEntity<Map<String, Object>> getProviderStatistics()
    {
        try
        {
            List<Object[]> statistics = thirdPartyServerService.getProviderTypeStatistics();
            
            Map<String, Object> result = new HashMap<>();
            result.put("code", 200);
            result.put("message", "获取成功");
            result.put("data", statistics);
            
            return ResponseEntity.ok(result);
        }
        catch (Exception e)
        {
            Map<String, Object> result = new HashMap<>();
            result.put("code", 500);
            result.put("message", "获取失败: " + e.getMessage());
            
            return ResponseEntity.status(500).body(result);
        }
    }

    /**
     * 批量导入第三方OAuth服务器配置
     */
    @PostMapping("/batch-import")
    public ResponseEntity<Map<String, Object>> batchImport(@Valid @RequestBody List<ThirdPartyServerRequest> requests)
    {
        try
        {
            thirdPartyServerService.batchImportThirdPartyServers(requests);
            
            Map<String, Object> result = new HashMap<>();
            result.put("code", 200);
            result.put("message", "批量导入完成");
            
            return ResponseEntity.ok(result);
        }
        catch (Exception e)
        {
            Map<String, Object> result = new HashMap<>();
            result.put("code", 500);
            result.put("message", "批量导入失败: " + e.getMessage());
            
            return ResponseEntity.status(500).body(result);
        }
    }

    /**
     * 获取第三方OAuth服务器配置示例
     */
    @GetMapping("/config-example")
    public ResponseEntity<Map<String, Object>> getConfigExample()
    {
        Map<String, Object> example = new HashMap<>();
        example.put("serverName", "GitHub OAuth");
        example.put("serverCode", "github");
        example.put("authorizationUri", "https://github.com/login/oauth/authorize");
        example.put("tokenUri", "https://github.com/login/oauth/access_token");
        example.put("userInfoUri", "https://api.github.com/user");
        example.put("clientId", "your-github-client-id");
        example.put("clientSecret", "your-github-client-secret");
        example.put("scopes", List.of("user:email", "read:user"));
        example.put("userNameAttribute", "login");
        example.put("clientName", "GitHub");
        example.put("redirectUriTemplate", "{baseUrl}/login/oauth2/code/{registrationId}");
        example.put("providerType", "github");
        example.put("description", "GitHub OAuth2.0第三方登录");
        example.put("sortOrder", 1);
        
        Map<String, Object> result = new HashMap<>();
        result.put("code", 200);
        result.put("message", "获取成功");
        result.put("data", example);
        
        return ResponseEntity.ok(result);
    }
}