package com.ruoyi.common.oauth2.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.core.OAuth2AccessToken;
import org.springframework.stereotype.Service;

/**
 * OAuth2.0客户端服务
 * 
 * @author ruoyi
 */
@Service
public class OAuth2ClientService
{
    @Autowired
    private ClientRegistrationRepository clientRegistrationRepository;

    @Autowired
    private OAuth2AuthorizedClientService authorizedClientService;

    /**
     * 获取客户端注册信息
     * 
     * @param registrationId 注册ID
     * @return 客户端注册信息
     */
    public ClientRegistration getClientRegistration(String registrationId)
    {
        return clientRegistrationRepository.findByRegistrationId(registrationId);
    }

    /**
     * 获取授权客户端
     * 
     * @param registrationId 注册ID
     * @param principalName 主体名称
     * @return 授权客户端
     */
    public OAuth2AuthorizedClient getAuthorizedClient(String registrationId, String principalName)
    {
        return authorizedClientService.loadAuthorizedClient(registrationId, principalName);
    }

    /**
     * 获取访问令牌
     * 
     * @param registrationId 注册ID
     * @param principalName 主体名称
     * @return 访问令牌
     */
    public OAuth2AccessToken getAccessToken(String registrationId, String principalName)
    {
        OAuth2AuthorizedClient authorizedClient = getAuthorizedClient(registrationId, principalName);
        return authorizedClient != null ? authorizedClient.getAccessToken() : null;
    }

    /**
     * 检查访问令牌是否有效
     * 
     * @param registrationId 注册ID
     * @param principalName 主体名称
     * @return 是否有效
     */
    public boolean isTokenValid(String registrationId, String principalName)
    {
        OAuth2AccessToken accessToken = getAccessToken(registrationId, principalName);
        if (accessToken == null)
        {
            return false;
        }
        
        // 检查令牌是否过期
        return accessToken.getExpiresAt() == null || 
               accessToken.getExpiresAt().isAfter(java.time.Instant.now());
    }

    /**
     * 移除授权客户端
     * 
     * @param registrationId 注册ID
     * @param principalName 主体名称
     */
    public void removeAuthorizedClient(String registrationId, String principalName)
    {
        authorizedClientService.removeAuthorizedClient(registrationId, principalName);
    }
}