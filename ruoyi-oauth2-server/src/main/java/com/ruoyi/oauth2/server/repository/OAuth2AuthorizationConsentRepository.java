package com.ruoyi.oauth2.server.repository;

import com.ruoyi.oauth2.server.entity.OAuth2AuthorizationConsentEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * OAuth2.0授权同意Repository
 * 
 * @author ruoyi
 */
@Repository
public interface OAuth2AuthorizationConsentRepository extends JpaRepository<OAuth2AuthorizationConsentEntity, OAuth2AuthorizationConsentEntity.AuthorizationConsentId>
{
    /**
     * 根据客户端ID和主体名称查找
     */
    Optional<OAuth2AuthorizationConsentEntity> findByRegisteredClientIdAndPrincipalName(String registeredClientId, String principalName);

    /**
     * 根据客户端ID查找所有同意记录
     */
    List<OAuth2AuthorizationConsentEntity> findByRegisteredClientId(String registeredClientId);

    /**
     * 根据主体名称查找所有同意记录
     */
    List<OAuth2AuthorizationConsentEntity> findByPrincipalName(String principalName);

    /**
     * 检查是否存在同意记录
     */
    boolean existsByRegisteredClientIdAndPrincipalName(String registeredClientId, String principalName);

    /**
     * 分页查询同意记录
     */
    @Query("SELECT c FROM OAuth2AuthorizationConsentEntity c WHERE " +
           "(:registeredClientId IS NULL OR c.registeredClientId = :registeredClientId) AND " +
           "(:principalName IS NULL OR c.principalName LIKE %:principalName%)")
    Page<OAuth2AuthorizationConsentEntity> findByConditions(
            @Param("registeredClientId") String registeredClientId,
            @Param("principalName") String principalName,
            Pageable pageable);

    /**
     * 统计同意记录数量
     */
    @Query("SELECT COUNT(c) FROM OAuth2AuthorizationConsentEntity c WHERE c.registeredClientId = :registeredClientId")
    long countByRegisteredClientId(@Param("registeredClientId") String registeredClientId);

    /**
     * 根据权限查找同意记录
     */
    @Query("SELECT c FROM OAuth2AuthorizationConsentEntity c WHERE c.authorities LIKE %:authority%")
    List<OAuth2AuthorizationConsentEntity> findByAuthoritiesContaining(@Param("authority") String authority);

    /**
     * 删除客户端的所有同意记录
     */
    void deleteByRegisteredClientId(String registeredClientId);

    /**
     * 删除用户的所有同意记录
     */
    void deleteByPrincipalName(String principalName);
}