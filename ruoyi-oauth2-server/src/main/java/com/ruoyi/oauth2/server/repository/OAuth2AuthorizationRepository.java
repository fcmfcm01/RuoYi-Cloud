package com.ruoyi.oauth2.server.repository;

import com.ruoyi.oauth2.server.entity.OAuth2AuthorizationEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

/**
 * OAuth2.0授权Repository
 * 
 * @author ruoyi
 */
@Repository
public interface OAuth2AuthorizationRepository extends JpaRepository<OAuth2AuthorizationEntity, String>
{
    /**
     * 根据状态值查找授权记录
     */
    Optional<OAuth2AuthorizationEntity> findByState(String state);

    /**
     * 根据授权码值查找
     */
    Optional<OAuth2AuthorizationEntity> findByAuthorizationCodeValue(String authorizationCode);

    /**
     * 根据访问令牌值查找
     */
    Optional<OAuth2AuthorizationEntity> findByAccessTokenValue(String accessToken);

    /**
     * 根据刷新令牌值查找
     */
    Optional<OAuth2AuthorizationEntity> findByRefreshTokenValue(String refreshToken);

    /**
     * 根据用户码值查找
     */
    Optional<OAuth2AuthorizationEntity> findByUserCodeValue(String userCode);

    /**
     * 根据设备码值查找
     */
    Optional<OAuth2AuthorizationEntity> findByDeviceCodeValue(String deviceCode);

    /**
     * 根据客户端ID查找授权记录
     */
    List<OAuth2AuthorizationEntity> findByRegisteredClientId(String registeredClientId);

    /**
     * 根据主体名称查找授权记录
     */
    List<OAuth2AuthorizationEntity> findByPrincipalName(String principalName);

    /**
     * 根据客户端ID和主体名称查找
     */
    List<OAuth2AuthorizationEntity> findByRegisteredClientIdAndPrincipalName(String registeredClientId, String principalName);

    /**
     * 分页查询授权记录
     */
    @Query("SELECT a FROM OAuth2AuthorizationEntity a WHERE " +
           "(:registeredClientId IS NULL OR a.registeredClientId = :registeredClientId) AND " +
           "(:principalName IS NULL OR a.principalName LIKE %:principalName%) AND " +
           "(:authorizationGrantType IS NULL OR a.authorizationGrantType = :authorizationGrantType)")
    Page<OAuth2AuthorizationEntity> findByConditions(
            @Param("registeredClientId") String registeredClientId,
            @Param("principalName") String principalName,
            @Param("authorizationGrantType") String authorizationGrantType,
            Pageable pageable);

    /**
     * 删除过期的授权码
     */
    @Modifying
    @Transactional
    @Query("DELETE FROM OAuth2AuthorizationEntity a WHERE a.authorizationCodeExpiresAt < :now")
    int deleteExpiredAuthorizationCodes(@Param("now") Instant now);

    /**
     * 删除过期的访问令牌
     */
    @Modifying
    @Transactional
    @Query("DELETE FROM OAuth2AuthorizationEntity a WHERE a.accessTokenExpiresAt < :now")
    int deleteExpiredAccessTokens(@Param("now") Instant now);

    /**
     * 删除过期的刷新令牌
     */
    @Modifying
    @Transactional
    @Query("DELETE FROM OAuth2AuthorizationEntity a WHERE a.refreshTokenExpiresAt < :now")
    int deleteExpiredRefreshTokens(@Param("now") Instant now);

    /**
     * 统计授权记录数量
     */
    @Query("SELECT COUNT(a) FROM OAuth2AuthorizationEntity a WHERE a.registeredClientId = :registeredClientId")
    long countByRegisteredClientId(@Param("registeredClientId") String registeredClientId);

    /**
     * 根据授权类型统计
     */
    @Query("SELECT a.authorizationGrantType, COUNT(a) FROM OAuth2AuthorizationEntity a GROUP BY a.authorizationGrantType")
    List<Object[]> countByAuthorizationGrantType();

    /**
     * 查找最近的授权记录
     */
    @Query("SELECT a FROM OAuth2AuthorizationEntity a WHERE a.principalName = :principalName ORDER BY a.createTime DESC")
    Page<OAuth2AuthorizationEntity> findRecentAuthorizationsByPrincipal(@Param("principalName") String principalName, Pageable pageable);
}