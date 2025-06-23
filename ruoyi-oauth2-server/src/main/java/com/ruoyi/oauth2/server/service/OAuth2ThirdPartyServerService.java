package com.ruoyi.oauth2.server.service;

import com.ruoyi.oauth2.server.dto.ThirdPartyServerRequest;
import com.ruoyi.oauth2.server.entity.OAuth2ThirdPartyServerEntity;
import com.ruoyi.oauth2.server.repository.OAuth2ThirdPartyServerRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.ClientAuthenticationMethod;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 第三方OAuth2.0服务器管理服务
 * 
 * @author ruoyi
 */
@Service
@Transactional
public class OAuth2ThirdPartyServerService
{
    @Autowired
    private OAuth2ThirdPartyServerRepository serverRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    /**
     * 添加第三方OAuth服务器
     */
    public OAuth2ThirdPartyServerEntity addThirdPartyServer(ThirdPartyServerRequest request)
    {
        // 检查服务器代码是否已存在
        if (serverRepository.existsByServerCode(request.getServerCode()))
        {
            throw new RuntimeException("服务器代码已存在: " + request.getServerCode());
        }

        OAuth2ThirdPartyServerEntity entity = new OAuth2ThirdPartyServerEntity();
        BeanUtils.copyProperties(request, entity);

        // 加密客户端密钥
        entity.setClientSecret(passwordEncoder.encode(request.getClientSecret()));

        // 设置授权范围
        if (request.getScopes() != null && !request.getScopes().isEmpty())
        {
            entity.setScopes(String.join(",", request.getScopes()));
        }

        return serverRepository.save(entity);
    }

    /**
     * 更新第三方OAuth服务器
     */
    public OAuth2ThirdPartyServerEntity updateThirdPartyServer(Long id, ThirdPartyServerRequest request)
    {
        OAuth2ThirdPartyServerEntity entity = serverRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("第三方OAuth服务器不存在: " + id));

        // 检查服务器代码是否与其他记录冲突
        if (!entity.getServerCode().equals(request.getServerCode()) && 
            serverRepository.existsByServerCode(request.getServerCode()))
        {
            throw new RuntimeException("服务器代码已存在: " + request.getServerCode());
        }

        // 更新基本信息
        entity.setServerName(request.getServerName());
        entity.setServerCode(request.getServerCode());
        entity.setAuthorizationUri(request.getAuthorizationUri());
        entity.setTokenUri(request.getTokenUri());
        entity.setUserInfoUri(request.getUserInfoUri());
        entity.setJwkSetUri(request.getJwkSetUri());
        entity.setIssuerUri(request.getIssuerUri());
        entity.setClientId(request.getClientId());
        entity.setUserNameAttribute(request.getUserNameAttribute());
        entity.setClientName(request.getClientName());
        entity.setRedirectUriTemplate(request.getRedirectUriTemplate());
        entity.setProviderType(request.getProviderType());
        entity.setDescription(request.getDescription());
        entity.setSortOrder(request.getSortOrder());

        // 如果提供了新密钥，则更新
        if (StringUtils.hasText(request.getClientSecret()))
        {
            entity.setClientSecret(passwordEncoder.encode(request.getClientSecret()));
        }

        // 更新授权范围
        if (request.getScopes() != null)
        {
            entity.setScopes(String.join(",", request.getScopes()));
        }

        return serverRepository.save(entity);
    }

    /**
     * 获取第三方OAuth服务器详情
     */
    public OAuth2ThirdPartyServerEntity getThirdPartyServer(Long id)
    {
        return serverRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("第三方OAuth服务器不存在: " + id));
    }

    /**
     * 根据服务器代码获取
     */
    public OAuth2ThirdPartyServerEntity getByServerCode(String serverCode)
    {
        return serverRepository.findByServerCode(serverCode)
                .orElseThrow(() -> new RuntimeException("第三方OAuth服务器不存在: " + serverCode));
    }

    /**
     * 启用/禁用第三方OAuth服务器
     */
    public void toggleServerStatus(Long id)
    {
        OAuth2ThirdPartyServerEntity entity = serverRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("第三方OAuth服务器不存在: " + id));

        entity.setStatus(entity.getStatus() == 1 ? 0 : 1);
        serverRepository.save(entity);
    }

    /**
     * 删除第三方OAuth服务器
     */
    public void deleteThirdPartyServer(Long id)
    {
        if (!serverRepository.existsById(id))
        {
            throw new RuntimeException("第三方OAuth服务器不存在: " + id);
        }
        serverRepository.deleteById(id);
    }

    /**
     * 分页查询第三方OAuth服务器
     */
    public Page<OAuth2ThirdPartyServerEntity> getThirdPartyServers(String serverName, String providerType, Integer status, Pageable pageable)
    {
        return serverRepository.findByConditions(serverName, providerType, status, pageable);
    }

    /**
     * 获取启用的第三方OAuth服务器列表
     */
    public List<OAuth2ThirdPartyServerEntity> getEnabledThirdPartyServers()
    {
        return serverRepository.findEnabledServersOrderBySortOrder();
    }

    /**
     * 根据提供商类型获取服务器
     */
    public List<OAuth2ThirdPartyServerEntity> getByProviderType(String providerType)
    {
        return serverRepository.findByProviderType(providerType);
    }

    /**
     * 转换为Spring Security的ClientRegistration
     */
    public ClientRegistration toClientRegistration(OAuth2ThirdPartyServerEntity entity)
    {
        ClientRegistration.Builder builder = ClientRegistration.withRegistrationId(entity.getServerCode())
                .clientId(entity.getClientId())
                .clientSecret(entity.getClientSecret())
                .clientAuthenticationMethod(ClientAuthenticationMethod.CLIENT_SECRET_BASIC)
                .authorizationGrantType(AuthorizationGrantType.AUTHORIZATION_CODE)
                .authorizationUri(entity.getAuthorizationUri())
                .tokenUri(entity.getTokenUri());

        // 设置用户信息URI
        if (StringUtils.hasText(entity.getUserInfoUri()))
        {
            builder.userInfoUri(entity.getUserInfoUri());
        }

        // 设置JWK Set URI
        if (StringUtils.hasText(entity.getJwkSetUri()))
        {
            builder.jwkSetUri(entity.getJwkSetUri());
        }

        // 设置Issuer URI
        if (StringUtils.hasText(entity.getIssuerUri()))
        {
            builder.issuerUri(entity.getIssuerUri());
        }

        // 设置用户名属性
        if (StringUtils.hasText(entity.getUserNameAttribute()))
        {
            builder.userNameAttributeName(entity.getUserNameAttribute());
        }
        else
        {
            // 默认用户名属性
            builder.userNameAttributeName("sub");
        }

        // 设置客户端名称
        if (StringUtils.hasText(entity.getClientName()))
        {
            builder.clientName(entity.getClientName());
        }
        else
        {
            builder.clientName(entity.getServerName());
        }

        // 设置重定向URI模板
        if (StringUtils.hasText(entity.getRedirectUriTemplate()))
        {
            builder.redirectUri(entity.getRedirectUriTemplate());
        }
        else
        {
            builder.redirectUri("{baseUrl}/login/oauth2/code/{registrationId}");
        }

        // 设置授权范围
        if (StringUtils.hasText(entity.getScopes()))
        {
            String[] scopes = entity.getScopes().split(",");
            for (String scope : scopes)
            {
                builder.scope(scope.trim());
            }
        }
        else
        {
            // 默认范围
            builder.scope("openid", "profile", "email");
        }

        return builder.build();
    }

    /**
     * 获取提供商类型统计
     */
    public List<Object[]> getProviderTypeStatistics()
    {
        return serverRepository.countByProviderType();
    }

    /**
     * 批量导入第三方OAuth服务器配置
     */
    public void batchImportThirdPartyServers(List<ThirdPartyServerRequest> requests)
    {
        for (ThirdPartyServerRequest request : requests)
        {
            try
            {
                if (!serverRepository.existsByServerCode(request.getServerCode()))
                {
                    addThirdPartyServer(request);
                }
            }
            catch (Exception e)
            {
                // 记录错误但继续处理其他记录
                System.err.println("导入第三方OAuth服务器失败: " + request.getServerCode() + ", 错误: " + e.getMessage());
            }
        }
    }

    /**
     * 测试第三方OAuth服务器连接
     */
    public boolean testThirdPartyServerConnection(Long id)
    {
        OAuth2ThirdPartyServerEntity entity = getThirdPartyServer(id);
        
        try
        {
            // 这里可以实现实际的连接测试逻辑
            // 例如：发送HTTP请求到授权URI检查响应
            // 暂时返回true，实际实现时可以添加具体的测试逻辑
            return true;
        }
        catch (Exception e)
        {
            return false;
        }
    }
}