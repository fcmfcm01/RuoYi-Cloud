package com.ruoyi.auth.controller;

import com.ruoyi.common.core.domain.R;
import com.ruoyi.common.oauth2.service.OAuth2ClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.annotation.RegisteredOAuth2AuthorizedClient;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

/**
 * OAuth2.0登录控制器
 * 
 * @author ruoyi
 */
@RestController
@RequestMapping("/oauth2")
public class OAuth2LoginController
{
    @Autowired
    private OAuth2ClientService oauth2ClientService;

    /**
     * OAuth2.0登录成功回调
     */
    @GetMapping("/login/success")
    public R<Map<String, Object>> loginSuccess(
            @RegisteredOAuth2AuthorizedClient("ruoyi") OAuth2AuthorizedClient authorizedClient,
            @AuthenticationPrincipal OAuth2User oauth2User)
    {
        Map<String, Object> result = new HashMap<>();
        result.put("clientName", authorizedClient.getClientRegistration().getClientName());
        result.put("userName", oauth2User.getName());
        result.put("userAttributes", oauth2User.getAttributes());
        result.put("authorities", oauth2User.getAuthorities());
        
        return R.ok(result);
    }

    /**
     * 获取OAuth2.0用户信息
     */
    @GetMapping("/user")
    public R<Map<String, Object>> getUser(@AuthenticationPrincipal OAuth2User oauth2User)
    {
        if (oauth2User == null)
        {
            return R.fail("用户未登录");
        }
        
        Map<String, Object> userInfo = new HashMap<>();
        userInfo.put("name", oauth2User.getName());
        userInfo.put("attributes", oauth2User.getAttributes());
        userInfo.put("authorities", oauth2User.getAuthorities());
        
        return R.ok(userInfo);
    }

    /**
     * 检查OAuth2.0令牌状态
     */
    @GetMapping("/token/status")
    public R<Map<String, Object>> getTokenStatus(
            @RequestParam String registrationId,
            @RequestParam String principalName)
    {
        Map<String, Object> status = new HashMap<>();
        
        boolean isValid = oauth2ClientService.isTokenValid(registrationId, principalName);
        status.put("valid", isValid);
        
        if (isValid)
        {
            var accessToken = oauth2ClientService.getAccessToken(registrationId, principalName);
            status.put("tokenType", accessToken.getTokenType().getValue());
            status.put("expiresAt", accessToken.getExpiresAt());
            status.put("scopes", accessToken.getScopes());
        }
        
        return R.ok(status);
    }

    /**
     * 撤销OAuth2.0授权
     */
    @DeleteMapping("/revoke")
    public R<String> revokeAuthorization(
            @RequestParam String registrationId,
            @RequestParam String principalName)
    {
        oauth2ClientService.removeAuthorizedClient(registrationId, principalName);
        return R.ok("授权已撤销");
    }

    /**
     * 获取OAuth2.0授权URL
     */
    @GetMapping("/authorize/url")
    public R<String> getAuthorizationUrl(@RequestParam String registrationId)
    {
        var clientRegistration = oauth2ClientService.getClientRegistration(registrationId);
        if (clientRegistration == null)
        {
            return R.fail("未找到客户端注册信息");
        }
        
        String authorizationUrl = clientRegistration.getProviderDetails().getAuthorizationUri() +
                "?response_type=code" +
                "&client_id=" + clientRegistration.getClientId() +
                "&redirect_uri=" + clientRegistration.getRedirectUri() +
                "&scope=" + String.join(" ", clientRegistration.getScopes());
        
        return R.ok(authorizationUrl);
    }
}