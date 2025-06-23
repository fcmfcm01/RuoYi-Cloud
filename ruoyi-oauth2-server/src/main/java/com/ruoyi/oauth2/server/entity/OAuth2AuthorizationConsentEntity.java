package com.ruoyi.oauth2.server.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * OAuth2.0授权同意实体
 * 
 * @author ruoyi
 */
@Entity
@Table(name = "oauth2_authorization_consent")
@IdClass(OAuth2AuthorizationConsentEntity.AuthorizationConsentId.class)
public class OAuth2AuthorizationConsentEntity
{
    @Id
    @Column(name = "registered_client_id", length = 100)
    private String registeredClientId;

    @Id
    @Column(name = "principal_name", length = 200)
    private String principalName;

    @NotBlank
    @Column(name = "authorities", length = 1000, nullable = false)
    private String authorities;

    @CreationTimestamp
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Column(name = "create_time")
    private LocalDateTime createTime;

    @UpdateTimestamp
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Column(name = "update_time")
    private LocalDateTime updateTime;

    // 构造函数
    public OAuth2AuthorizationConsentEntity() {}

    public OAuth2AuthorizationConsentEntity(String registeredClientId, String principalName, String authorities) {
        this.registeredClientId = registeredClientId;
        this.principalName = principalName;
        this.authorities = authorities;
    }

    // 复合主键类
    public static class AuthorizationConsentId implements Serializable {
        private String registeredClientId;
        private String principalName;

        public AuthorizationConsentId() {}

        public AuthorizationConsentId(String registeredClientId, String principalName) {
            this.registeredClientId = registeredClientId;
            this.principalName = principalName;
        }

        public String getRegisteredClientId() {
            return registeredClientId;
        }

        public void setRegisteredClientId(String registeredClientId) {
            this.registeredClientId = registeredClientId;
        }

        public String getPrincipalName() {
            return principalName;
        }

        public void setPrincipalName(String principalName) {
            this.principalName = principalName;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            AuthorizationConsentId that = (AuthorizationConsentId) o;

            if (!registeredClientId.equals(that.registeredClientId)) return false;
            return principalName.equals(that.principalName);
        }

        @Override
        public int hashCode() {
            int result = registeredClientId.hashCode();
            result = 31 * result + principalName.hashCode();
            return result;
        }
    }

    // Getter和Setter方法
    public String getRegisteredClientId() {
        return registeredClientId;
    }

    public void setRegisteredClientId(String registeredClientId) {
        this.registeredClientId = registeredClientId;
    }

    public String getPrincipalName() {
        return principalName;
    }

    public void setPrincipalName(String principalName) {
        this.principalName = principalName;
    }

    public String getAuthorities() {
        return authorities;
    }

    public void setAuthorities(String authorities) {
        this.authorities = authorities;
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