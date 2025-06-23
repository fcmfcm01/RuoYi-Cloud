package com.ruoyi.oauth2.server.service;

import com.ruoyi.oauth2.server.dto.ClientRegistrationRequest;
import com.ruoyi.oauth2.server.dto.ClientRegistrationResponse;
import com.ruoyi.oauth2.server.entity.OAuth2RegisteredClientEntity;
import com.ruoyi.oauth2.server.repository.OAuth2RegisteredClientRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.ClientAuthenticationMethod;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;
import org.springframework.security.oauth2.server.authorization.settings.ClientSettings;
import org.springframework.security.oauth2.server.authorization.settings.TokenSettings;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.time.Duration;
import java.time.Instant;
import java.util.*;
import java.util.stream.Collectors;

/**
 * OAuth2.0客户端注册服务
 * 
 * @author ruoyi
 */
@Service
@Transactional
public class OAuth2ClientRegistrationService
{
    @Autowired
    private OAuth2RegisteredClientRepository clientRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    /**
     * 注册新客户端
     */
    public ClientRegistrationResponse registerClient(ClientRegistrationRequest request)
    {
        // 生成客户端ID和密钥
        String clientId = generateClientId();
        String clientSecret = generateClientSecret();
        String encodedSecret = passwordEncoder.encode(clientSecret);

        // 创建实体
        OAuth2RegisteredClientEntity entity = new OAuth2RegisteredClientEntity();
        entity.setId(UUID.randomUUID().toString());
        entity.setClientId(clientId);
        entity.setClientSecret(encodedSecret);
        entity.setClientName(request.getClientName());
        entity.setClientIdIssuedAt(Instant.now());
        entity.setDescription(request.getDescription());

        // 设置认证方法
        entity.setClientAuthenticationMethods(String.join(",", request.getClientAuthenticationMethods()));

        // 设置授权类型
        entity.setAuthorizationGrantTypes(String.join(",", request.getAuthorizationGrantTypes()));

        // 设置重定向URI
        if (request.getRedirectUris() != null && !request.getRedirectUris().isEmpty())
        {
            entity.setRedirectUris(String.join(",", request.getRedirectUris()));
        }

        // 设置登出重定向URI
        if (request.getPostLogoutRedirectUris() != null && !request.getPostLogoutRedirectUris().isEmpty())
        {
            entity.setPostLogoutRedirectUris(String.join(",", request.getPostLogoutRedirectUris()));
        }

        // 设置授权范围
        entity.setScopes(String.join(",", request.getScopes()));

        // 设置客户端设置
        ClientSettings clientSettings = ClientSettings.builder()
                .requireAuthorizationConsent(request.getRequireAuthorizationConsent())
                .requireProofKey(request.getRequireProofKey())
                .build();
        entity.setClientSettings(clientSettings.getSettings().toString());

        // 设置令牌设置
        TokenSettings tokenSettings = TokenSettings.builder()
                .accessTokenTimeToLive(Duration.ofSeconds(request.getAccessTokenTimeToLive()))
                .refreshTokenTimeToLive(Duration.ofSeconds(request.getRefreshTokenTimeToLive()))
                .reuseRefreshTokens(request.getReuseRefreshTokens())
                .build();
        entity.setTokenSettings(tokenSettings.getSettings().toString());

        // 保存到数据库
        entity = clientRepository.save(entity);

        // 转换为响应DTO
        ClientRegistrationResponse response = convertToResponse(entity);
        response.setClientSecret(clientSecret); // 返回明文密钥（仅此一次）

        return response;
    }

    /**
     * 获取客户端详情
     */
    public ClientRegistrationResponse getClient(String clientId)
    {
        OAuth2RegisteredClientEntity entity = clientRepository.findByClientId(clientId)
                .orElseThrow(() -> new RuntimeException("客户端不存在: " + clientId));
        
        ClientRegistrationResponse response = convertToResponse(entity);
        response.setClientSecret("***"); // 隐藏密钥
        return response;
    }

    /**
     * 更新客户端
     */
    public ClientRegistrationResponse updateClient(String clientId, ClientRegistrationRequest request)
    {
        OAuth2RegisteredClientEntity entity = clientRepository.findByClientId(clientId)
                .orElseThrow(() -> new RuntimeException("客户端不存在: " + clientId));

        // 更新基本信息
        entity.setClientName(request.getClientName());
        entity.setDescription(request.getDescription());

        // 更新认证方法
        entity.setClientAuthenticationMethods(String.join(",", request.getClientAuthenticationMethods()));

        // 更新授权类型
        entity.setAuthorizationGrantTypes(String.join(",", request.getAuthorizationGrantTypes()));

        // 更新重定向URI
        if (request.getRedirectUris() != null)
        {
            entity.setRedirectUris(String.join(",", request.getRedirectUris()));
        }

        // 更新登出重定向URI
        if (request.getPostLogoutRedirectUris() != null)
        {
            entity.setPostLogoutRedirectUris(String.join(",", request.getPostLogoutRedirectUris()));
        }

        // 更新授权范围
        entity.setScopes(String.join(",", request.getScopes()));

        // 更新客户端设置
        ClientSettings clientSettings = ClientSettings.builder()
                .requireAuthorizationConsent(request.getRequireAuthorizationConsent())
                .requireProofKey(request.getRequireProofKey())
                .build();
        entity.setClientSettings(clientSettings.getSettings().toString());

        // 更新令牌设置
        TokenSettings tokenSettings = TokenSettings.builder()
                .accessTokenTimeToLive(Duration.ofSeconds(request.getAccessTokenTimeToLive()))
                .refreshTokenTimeToLive(Duration.ofSeconds(request.getRefreshTokenTimeToLive()))
                .reuseRefreshTokens(request.getReuseRefreshTokens())
                .build();
        entity.setTokenSettings(tokenSettings.getSettings().toString());

        // 保存更新
        entity = clientRepository.save(entity);

        ClientRegistrationResponse response = convertToResponse(entity);
        response.setClientSecret("***"); // 隐藏密钥
        return response;
    }

    /**
     * 重置客户端密钥
     */
    public String resetClientSecret(String clientId)
    {
        OAuth2RegisteredClientEntity entity = clientRepository.findByClientId(clientId)
                .orElseThrow(() -> new RuntimeException("客户端不存在: " + clientId));

        String newSecret = generateClientSecret();
        String encodedSecret = passwordEncoder.encode(newSecret);
        
        entity.setClientSecret(encodedSecret);
        clientRepository.save(entity);

        return newSecret;
    }

    /**
     * 启用/禁用客户端
     */
    public void toggleClientStatus(String clientId)
    {
        OAuth2RegisteredClientEntity entity = clientRepository.findByClientId(clientId)
                .orElseThrow(() -> new RuntimeException("客户端不存在: " + clientId));

        entity.setStatus(entity.getStatus() == 1 ? 0 : 1);
        clientRepository.save(entity);
    }

    /**
     * 删除客户端
     */
    public void deleteClient(String clientId)
    {
        OAuth2RegisteredClientEntity entity = clientRepository.findByClientId(clientId)
                .orElseThrow(() -> new RuntimeException("客户端不存在: " + clientId));

        clientRepository.delete(entity);
    }

    /**
     * 分页查询客户端
     */
    public Page<ClientRegistrationResponse> getClients(String clientName, Integer status, String createdBy, Pageable pageable)
    {
        Page<OAuth2RegisteredClientEntity> entities = clientRepository.findByConditions(clientName, status, createdBy, pageable);
        
        return entities.map(entity -> {
            ClientRegistrationResponse response = convertToResponse(entity);
            response.setClientSecret("***"); // 隐藏密钥
            return response;
        });
    }

    /**
     * 获取启用的客户端列表
     */
    public List<ClientRegistrationResponse> getEnabledClients()
    {
        List<OAuth2RegisteredClientEntity> entities = clientRepository.findEnabledClients();
        
        return entities.stream()
                .map(entity -> {
                    ClientRegistrationResponse response = convertToResponse(entity);
                    response.setClientSecret("***"); // 隐藏密钥
                    return response;
                })
                .collect(Collectors.toList());
    }

    /**
     * 转换为Spring Security的RegisteredClient
     */
    public RegisteredClient toRegisteredClient(OAuth2RegisteredClientEntity entity)
    {
        RegisteredClient.Builder builder = RegisteredClient.withId(entity.getId())
                .clientId(entity.getClientId())
                .clientSecret(entity.getClientSecret())
                .clientName(entity.getClientName())
                .clientIdIssuedAt(entity.getClientIdIssuedAt())
                .clientSecretExpiresAt(entity.getClientSecretExpiresAt());

        // 设置认证方法
        if (StringUtils.hasText(entity.getClientAuthenticationMethods()))
        {
            Arrays.stream(entity.getClientAuthenticationMethods().split(","))
                    .forEach(method -> builder.clientAuthenticationMethod(new ClientAuthenticationMethod(method.trim())));
        }

        // 设置授权类型
        if (StringUtils.hasText(entity.getAuthorizationGrantTypes()))
        {
            Arrays.stream(entity.getAuthorizationGrantTypes().split(","))
                    .forEach(grantType -> builder.authorizationGrantType(new AuthorizationGrantType(grantType.trim())));
        }

        // 设置重定向URI
        if (StringUtils.hasText(entity.getRedirectUris()))
        {
            Arrays.stream(entity.getRedirectUris().split(","))
                    .forEach(uri -> builder.redirectUri(uri.trim()));
        }

        // 设置登出重定向URI
        if (StringUtils.hasText(entity.getPostLogoutRedirectUris()))
        {
            Arrays.stream(entity.getPostLogoutRedirectUris().split(","))
                    .forEach(uri -> builder.postLogoutRedirectUri(uri.trim()));
        }

        // 设置授权范围
        if (StringUtils.hasText(entity.getScopes()))
        {
            Arrays.stream(entity.getScopes().split(","))
                    .forEach(scope -> builder.scope(scope.trim()));
        }

        return builder.build();
    }

    /**
     * 生成客户端ID
     */
    private String generateClientId()
    {
        return "client_" + System.currentTimeMillis() + "_" + (int)(Math.random() * 1000);
    }

    /**
     * 生成客户端密钥
     */
    private String generateClientSecret()
    {
        return UUID.randomUUID().toString().replace("-", "");
    }

    /**
     * 转换为响应DTO
     */
    private ClientRegistrationResponse convertToResponse(OAuth2RegisteredClientEntity entity)
    {
        ClientRegistrationResponse response = new ClientRegistrationResponse();
        BeanUtils.copyProperties(entity, response);

        // 转换集合字段
        if (StringUtils.hasText(entity.getClientAuthenticationMethods()))
        {
            response.setClientAuthenticationMethods(
                    Arrays.stream(entity.getClientAuthenticationMethods().split(","))
                            .map(String::trim)
                            .collect(Collectors.toSet())
            );
        }

        if (StringUtils.hasText(entity.getAuthorizationGrantTypes()))
        {
            response.setAuthorizationGrantTypes(
                    Arrays.stream(entity.getAuthorizationGrantTypes().split(","))
                            .map(String::trim)
                            .collect(Collectors.toSet())
            );
        }

        if (StringUtils.hasText(entity.getRedirectUris()))
        {
            response.setRedirectUris(
                    Arrays.stream(entity.getRedirectUris().split(","))
                            .map(String::trim)
                            .collect(Collectors.toSet())
            );
        }

        if (StringUtils.hasText(entity.getPostLogoutRedirectUris()))
        {
            response.setPostLogoutRedirectUris(
                    Arrays.stream(entity.getPostLogoutRedirectUris().split(","))
                            .map(String::trim)
                            .collect(Collectors.toSet())
            );
        }

        if (StringUtils.hasText(entity.getScopes()))
        {
            response.setScopes(
                    Arrays.stream(entity.getScopes().split(","))
                            .map(String::trim)
                            .collect(Collectors.toSet())
            );
        }

        return response;
    }
}