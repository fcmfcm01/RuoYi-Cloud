package com.ruoyi.oauth2.server.dto;

import javax.validation.constraints.NotBlank;
import java.util.Set;

/**
 * 第三方OAuth服务器配置请求DTO
 * 
 * @author ruoyi
 */
public class ThirdPartyServerRequest
{
    @NotBlank(message = "服务器名称不能为空")
    private String serverName;

    @NotBlank(message = "服务器代码不能为空")
    private String serverCode;

    @NotBlank(message = "授权URI不能为空")
    private String authorizationUri;

    @NotBlank(message = "令牌URI不能为空")
    private String tokenUri;

    private String userInfoUri;

    private String jwkSetUri;

    private String issuerUri;

    @NotBlank(message = "客户端ID不能为空")
    private String clientId;

    @NotBlank(message = "客户端密钥不能为空")
    private String clientSecret;

    private Set<String> scopes;

    private String userNameAttribute;

    private String clientName;

    private String redirectUriTemplate;

    private String providerType;

    private String description;

    private Integer sortOrder = 0;

    // 构造函数
    public ThirdPartyServerRequest() {}

    // Getter和Setter方法
    public String getServerName() {
        return serverName;
    }

    public void setServerName(String serverName) {
        this.serverName = serverName;
    }

    public String getServerCode() {
        return serverCode;
    }

    public void setServerCode(String serverCode) {
        this.serverCode = serverCode;
    }

    public String getAuthorizationUri() {
        return authorizationUri;
    }

    public void setAuthorizationUri(String authorizationUri) {
        this.authorizationUri = authorizationUri;
    }

    public String getTokenUri() {
        return tokenUri;
    }

    public void setTokenUri(String tokenUri) {
        this.tokenUri = tokenUri;
    }

    public String getUserInfoUri() {
        return userInfoUri;
    }

    public void setUserInfoUri(String userInfoUri) {
        this.userInfoUri = userInfoUri;
    }

    public String getJwkSetUri() {
        return jwkSetUri;
    }

    public void setJwkSetUri(String jwkSetUri) {
        this.jwkSetUri = jwkSetUri;
    }

    public String getIssuerUri() {
        return issuerUri;
    }

    public void setIssuerUri(String issuerUri) {
        this.issuerUri = issuerUri;
    }

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public String getClientSecret() {
        return clientSecret;
    }

    public void setClientSecret(String clientSecret) {
        this.clientSecret = clientSecret;
    }

    public Set<String> getScopes() {
        return scopes;
    }

    public void setScopes(Set<String> scopes) {
        this.scopes = scopes;
    }

    public String getUserNameAttribute() {
        return userNameAttribute;
    }

    public void setUserNameAttribute(String userNameAttribute) {
        this.userNameAttribute = userNameAttribute;
    }

    public String getClientName() {
        return clientName;
    }

    public void setClientName(String clientName) {
        this.clientName = clientName;
    }

    public String getRedirectUriTemplate() {
        return redirectUriTemplate;
    }

    public void setRedirectUriTemplate(String redirectUriTemplate) {
        this.redirectUriTemplate = redirectUriTemplate;
    }

    public String getProviderType() {
        return providerType;
    }

    public void setProviderType(String providerType) {
        this.providerType = providerType;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getSortOrder() {
        return sortOrder;
    }

    public void setSortOrder(Integer sortOrder) {
        this.sortOrder = sortOrder;
    }
}