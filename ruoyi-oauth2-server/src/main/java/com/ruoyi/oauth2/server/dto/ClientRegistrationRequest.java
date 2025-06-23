package com.ruoyi.oauth2.server.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import java.util.Set;

/**
 * 客户端注册请求DTO
 * 
 * @author ruoyi
 */
public class ClientRegistrationRequest
{
    @NotBlank(message = "客户端名称不能为空")
    private String clientName;

    @NotEmpty(message = "客户端认证方法不能为空")
    private Set<String> clientAuthenticationMethods;

    @NotEmpty(message = "授权类型不能为空")
    private Set<String> authorizationGrantTypes;

    private Set<String> redirectUris;

    private Set<String> postLogoutRedirectUris;

    @NotEmpty(message = "授权范围不能为空")
    private Set<String> scopes;

    private String description;

    private Boolean requireAuthorizationConsent = true;

    private Boolean requireProofKey = false;

    private Integer accessTokenTimeToLive = 3600; // 秒

    private Integer refreshTokenTimeToLive = 7200; // 秒

    private Boolean reuseRefreshTokens = true;

    // 构造函数
    public ClientRegistrationRequest() {}

    // Getter和Setter方法
    public String getClientName() {
        return clientName;
    }

    public void setClientName(String clientName) {
        this.clientName = clientName;
    }

    public Set<String> getClientAuthenticationMethods() {
        return clientAuthenticationMethods;
    }

    public void setClientAuthenticationMethods(Set<String> clientAuthenticationMethods) {
        this.clientAuthenticationMethods = clientAuthenticationMethods;
    }

    public Set<String> getAuthorizationGrantTypes() {
        return authorizationGrantTypes;
    }

    public void setAuthorizationGrantTypes(Set<String> authorizationGrantTypes) {
        this.authorizationGrantTypes = authorizationGrantTypes;
    }

    public Set<String> getRedirectUris() {
        return redirectUris;
    }

    public void setRedirectUris(Set<String> redirectUris) {
        this.redirectUris = redirectUris;
    }

    public Set<String> getPostLogoutRedirectUris() {
        return postLogoutRedirectUris;
    }

    public void setPostLogoutRedirectUris(Set<String> postLogoutRedirectUris) {
        this.postLogoutRedirectUris = postLogoutRedirectUris;
    }

    public Set<String> getScopes() {
        return scopes;
    }

    public void setScopes(Set<String> scopes) {
        this.scopes = scopes;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Boolean getRequireAuthorizationConsent() {
        return requireAuthorizationConsent;
    }

    public void setRequireAuthorizationConsent(Boolean requireAuthorizationConsent) {
        this.requireAuthorizationConsent = requireAuthorizationConsent;
    }

    public Boolean getRequireProofKey() {
        return requireProofKey;
    }

    public void setRequireProofKey(Boolean requireProofKey) {
        this.requireProofKey = requireProofKey;
    }

    public Integer getAccessTokenTimeToLive() {
        return accessTokenTimeToLive;
    }

    public void setAccessTokenTimeToLive(Integer accessTokenTimeToLive) {
        this.accessTokenTimeToLive = accessTokenTimeToLive;
    }

    public Integer getRefreshTokenTimeToLive() {
        return refreshTokenTimeToLive;
    }

    public void setRefreshTokenTimeToLive(Integer refreshTokenTimeToLive) {
        this.refreshTokenTimeToLive = refreshTokenTimeToLive;
    }

    public Boolean getReuseRefreshTokens() {
        return reuseRefreshTokens;
    }

    public void setReuseRefreshTokens(Boolean reuseRefreshTokens) {
        this.reuseRefreshTokens = reuseRefreshTokens;
    }
}