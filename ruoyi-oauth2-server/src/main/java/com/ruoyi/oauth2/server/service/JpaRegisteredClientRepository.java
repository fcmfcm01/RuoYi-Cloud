package com.ruoyi.oauth2.server.service;

import com.ruoyi.oauth2.server.entity.OAuth2RegisteredClientEntity;
import com.ruoyi.oauth2.server.repository.OAuth2RegisteredClientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.ClientAuthenticationMethod;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClientRepository;
import org.springframework.security.oauth2.server.authorization.settings.ClientSettings;
import org.springframework.security.oauth2.server.authorization.settings.TokenSettings;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.Arrays;
import java.util.Optional;

/**
 * 基于JPA的RegisteredClientRepository实现
 * 
 * @author ruoyi
 */
@Service
public class JpaRegisteredClientRepository implements RegisteredClientRepository
{
    @Autowired
    private OAuth2RegisteredClientRepository clientRepository;

    @Autowired
    private OAuth2ClientRegistrationService clientRegistrationService;

    @Override
    public void save(RegisteredClient registeredClient)
    {
        OAuth2RegisteredClientEntity entity = toEntity(registeredClient);
        clientRepository.save(entity);
    }

    @Override
    public RegisteredClient findById(String id)
    {
        Optional<OAuth2RegisteredClientEntity> entity = clientRepository.findById(id);
        return entity.map(this::toRegisteredClient).orElse(null);
    }

    @Override
    public RegisteredClient findByClientId(String clientId)
    {
        Optional<OAuth2RegisteredClientEntity> entity = clientRepository.findByClientId(clientId);
        return entity.map(this::toRegisteredClient).orElse(null);
    }

    /**
     * 转换为RegisteredClient
     */
    private RegisteredClient toRegisteredClient(OAuth2RegisteredClientEntity entity)
    {
        // 检查客户端状态
        if (entity.getStatus() != 1)
        {
            return null; // 禁用的客户端返回null
        }

        return clientRegistrationService.toRegisteredClient(entity);
    }

    /**
     * 转换为实体
     */
    private OAuth2RegisteredClientEntity toEntity(RegisteredClient registeredClient)
    {
        OAuth2RegisteredClientEntity entity = new OAuth2RegisteredClientEntity();
        entity.setId(registeredClient.getId());
        entity.setClientId(registeredClient.getClientId());
        entity.setClientSecret(registeredClient.getClientSecret());
        entity.setClientName(registeredClient.getClientName());
        entity.setClientIdIssuedAt(registeredClient.getClientIdIssuedAt());
        entity.setClientSecretExpiresAt(registeredClient.getClientSecretExpiresAt());

        // 设置认证方法
        entity.setClientAuthenticationMethods(
                registeredClient.getClientAuthenticationMethods().stream()
                        .map(ClientAuthenticationMethod::getValue)
                        .reduce((a, b) -> a + "," + b)
                        .orElse("")
        );

        // 设置授权类型
        entity.setAuthorizationGrantTypes(
                registeredClient.getAuthorizationGrantTypes().stream()
                        .map(AuthorizationGrantType::getValue)
                        .reduce((a, b) -> a + "," + b)
                        .orElse("")
        );

        // 设置重定向URI
        if (!registeredClient.getRedirectUris().isEmpty())
        {
            entity.setRedirectUris(String.join(",", registeredClient.getRedirectUris()));
        }

        // 设置登出重定向URI
        if (!registeredClient.getPostLogoutRedirectUris().isEmpty())
        {
            entity.setPostLogoutRedirectUris(String.join(",", registeredClient.getPostLogoutRedirectUris()));
        }

        // 设置授权范围
        entity.setScopes(String.join(",", registeredClient.getScopes()));

        // 设置客户端设置
        entity.setClientSettings(registeredClient.getClientSettings().getSettings().toString());

        // 设置令牌设置
        entity.setTokenSettings(registeredClient.getTokenSettings().getSettings().toString());

        return entity;
    }
}