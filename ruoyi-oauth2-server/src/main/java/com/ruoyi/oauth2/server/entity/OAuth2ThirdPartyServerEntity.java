package com.ruoyi.oauth2.server.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

/**
 * 第三方OAuth2.0服务器配置实体
 * 
 * @author ruoyi
 */
@Entity
@Table(name = "oauth2_third_party_server")
public class OAuth2ThirdPartyServerEntity
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Column(name = "server_name", length = 100, nullable = false)
    private String serverName;

    @NotBlank
    @Column(name = "server_code", length = 50, unique = true, nullable = false)
    private String serverCode;

    @NotBlank
    @Column(name = "authorization_uri", length = 500, nullable = false)
    private String authorizationUri;

    @NotBlank
    @Column(name = "token_uri", length = 500, nullable = false)
    private String tokenUri;

    @Column(name = "user_info_uri", length = 500)
    private String userInfoUri;

    @Column(name = "jwk_set_uri", length = 500)
    private String jwkSetUri;

    @Column(name = "issuer_uri", length = 500)
    private String issuerUri;

    @NotBlank
    @Column(name = "client_id", length = 200, nullable = false)
    private String clientId;

    @NotBlank
    @Column(name = "client_secret", length = 500, nullable = false)
    private String clientSecret;

    @Column(name = "scopes", length = 1000)
    private String scopes;

    @Column(name = "user_name_attribute", length = 100)
    private String userNameAttribute;

    @Column(name = "client_name", length = 200)
    private String clientName;

    @Column(name = "redirect_uri_template", length = 500)
    private String redirectUriTemplate;

    @Column(name = "provider_type", length = 50)
    private String providerType; // github, google, wechat, dingtalk, etc.

    @Column(name = "description", length = 500)
    private String description;

    @NotNull
    @Column(name = "status")
    private Integer status = 1; // 1-启用, 0-禁用

    @Column(name = "sort_order")
    private Integer sortOrder = 0;

    @Column(name = "created_by", length = 64)
    private String createdBy;

    @Column(name = "updated_by", length = 64)
    private String updatedBy;

    @CreationTimestamp
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Column(name = "create_time")
    private LocalDateTime createTime;

    @UpdateTimestamp
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Column(name = "update_time")
    private LocalDateTime updateTime;

    // 构造函数
    public OAuth2ThirdPartyServerEntity() {}

    public OAuth2ThirdPartyServerEntity(String serverName, String serverCode) {
        this.serverName = serverName;
        this.serverCode = serverCode;
    }

    // Getter和Setter方法
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

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

    public String getScopes() {
        return scopes;
    }

    public void setScopes(String scopes) {
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

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Integer getSortOrder() {
        return sortOrder;
    }

    public void setSortOrder(Integer sortOrder) {
        this.sortOrder = sortOrder;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public String getUpdatedBy() {
        return updatedBy;
    }

    public void setUpdatedBy(String updatedBy) {
        this.updatedBy = updatedBy;
    }

    public LocalDateTime getCreateTime() {
        return createTime;
    }

    public void setCreateTime(LocalDateTime createTime) {
        this.createTime = createTime;
    }

    public LocalDateTime getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(LocalDateTime updateTime) {
        this.updateTime = updateTime;
    }
}