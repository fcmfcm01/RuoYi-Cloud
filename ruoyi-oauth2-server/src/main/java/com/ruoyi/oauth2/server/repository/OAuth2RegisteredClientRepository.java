package com.ruoyi.oauth2.server.repository;

import com.ruoyi.oauth2.server.entity.OAuth2RegisteredClientEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * OAuth2.0注册客户端Repository
 * 
 * @author ruoyi
 */
@Repository
public interface OAuth2RegisteredClientRepository extends JpaRepository<OAuth2RegisteredClientEntity, String>
{
    /**
     * 根据客户端ID查找
     */
    Optional<OAuth2RegisteredClientEntity> findByClientId(String clientId);

    /**
     * 检查客户端ID是否存在
     */
    boolean existsByClientId(String clientId);

    /**
     * 根据状态查找客户端
     */
    List<OAuth2RegisteredClientEntity> findByStatus(Integer status);

    /**
     * 根据客户端名称模糊查询
     */
    @Query("SELECT c FROM OAuth2RegisteredClientEntity c WHERE c.clientName LIKE %:clientName%")
    Page<OAuth2RegisteredClientEntity> findByClientNameContaining(@Param("clientName") String clientName, Pageable pageable);

    /**
     * 根据创建者查找
     */
    List<OAuth2RegisteredClientEntity> findByCreatedBy(String createdBy);

    /**
     * 查找启用的客户端
     */
    @Query("SELECT c FROM OAuth2RegisteredClientEntity c WHERE c.status = 1 ORDER BY c.createTime DESC")
    List<OAuth2RegisteredClientEntity> findEnabledClients();

    /**
     * 根据多个条件查询
     */
    @Query("SELECT c FROM OAuth2RegisteredClientEntity c WHERE " +
           "(:clientName IS NULL OR c.clientName LIKE %:clientName%) AND " +
           "(:status IS NULL OR c.status = :status) AND " +
           "(:createdBy IS NULL OR c.createdBy = :createdBy)")
    Page<OAuth2RegisteredClientEntity> findByConditions(
            @Param("clientName") String clientName,
            @Param("status") Integer status,
            @Param("createdBy") String createdBy,
            Pageable pageable);

    /**
     * 统计客户端数量
     */
    @Query("SELECT COUNT(c) FROM OAuth2RegisteredClientEntity c WHERE c.status = :status")
    long countByStatus(@Param("status") Integer status);
}