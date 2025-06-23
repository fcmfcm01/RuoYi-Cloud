package com.ruoyi.oauth2.server.repository;

import com.ruoyi.oauth2.server.entity.OAuth2ThirdPartyServerEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * 第三方OAuth2.0服务器Repository
 * 
 * @author ruoyi
 */
@Repository
public interface OAuth2ThirdPartyServerRepository extends JpaRepository<OAuth2ThirdPartyServerEntity, Long>
{
    /**
     * 根据服务器代码查找
     */
    Optional<OAuth2ThirdPartyServerEntity> findByServerCode(String serverCode);

    /**
     * 检查服务器代码是否存在
     */
    boolean existsByServerCode(String serverCode);

    /**
     * 根据状态查找服务器
     */
    List<OAuth2ThirdPartyServerEntity> findByStatus(Integer status);

    /**
     * 根据提供商类型查找
     */
    List<OAuth2ThirdPartyServerEntity> findByProviderType(String providerType);

    /**
     * 根据服务器名称模糊查询
     */
    @Query("SELECT s FROM OAuth2ThirdPartyServerEntity s WHERE s.serverName LIKE %:serverName%")
    Page<OAuth2ThirdPartyServerEntity> findByServerNameContaining(@Param("serverName") String serverName, Pageable pageable);

    /**
     * 查找启用的服务器，按排序顺序
     */
    @Query("SELECT s FROM OAuth2ThirdPartyServerEntity s WHERE s.status = 1 ORDER BY s.sortOrder ASC, s.createTime DESC")
    List<OAuth2ThirdPartyServerEntity> findEnabledServersOrderBySortOrder();

    /**
     * 根据多个条件查询
     */
    @Query("SELECT s FROM OAuth2ThirdPartyServerEntity s WHERE " +
           "(:serverName IS NULL OR s.serverName LIKE %:serverName%) AND " +
           "(:providerType IS NULL OR s.providerType = :providerType) AND " +
           "(:status IS NULL OR s.status = :status)")
    Page<OAuth2ThirdPartyServerEntity> findByConditions(
            @Param("serverName") String serverName,
            @Param("providerType") String providerType,
            @Param("status") Integer status,
            Pageable pageable);

    /**
     * 统计服务器数量
     */
    @Query("SELECT COUNT(s) FROM OAuth2ThirdPartyServerEntity s WHERE s.status = :status")
    long countByStatus(@Param("status") Integer status);

    /**
     * 根据提供商类型统计
     */
    @Query("SELECT s.providerType, COUNT(s) FROM OAuth2ThirdPartyServerEntity s WHERE s.status = 1 GROUP BY s.providerType")
    List<Object[]> countByProviderType();
}