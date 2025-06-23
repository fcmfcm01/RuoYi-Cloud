package com.ruoyi.common.casbin.auth;

import com.ruoyi.common.casbin.service.CasbinService;
import com.ruoyi.common.core.exception.auth.NotPermissionException;
import com.ruoyi.common.core.utils.SpringUtils;
import com.ruoyi.common.core.utils.StringUtils;
import com.ruoyi.common.security.auth.AuthLogic;
import com.ruoyi.common.security.utils.SecurityUtils;
import com.ruoyi.system.api.model.LoginUser;
import org.springframework.util.PatternMatchUtils;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

/**
 * 基于Casbin的权限验证逻辑
 * 
 * @author ruoyi
 */
public class CasbinAuthLogic extends AuthLogic
{
    /** 所有权限标识 */
    private static final String ALL_PERMISSION = "*:*:*";

    /** 管理员角色权限标识 */
    private static final String SUPER_ADMIN = "admin";

    private CasbinService casbinService = SpringUtils.getBean(CasbinService.class);

    /**
     * 验证用户是否具备某权限（使用Casbin）
     * 
     * @param permission 权限字符串
     * @return 用户是否具备某权限
     */
    @Override
    public boolean hasPermi(String permission)
    {
        try
        {
            String username = SecurityUtils.getUsername();
            if (StringUtils.isEmpty(username))
            {
                return false;
            }

            // 先检查传统权限
            if (hasPermi(getPermiList(), permission))
            {
                return true;
            }

            // 使用Casbin检查权限
            String[] parts = permission.split(":");
            if (parts.length >= 3)
            {
                String resource = parts[0] + ":" + parts[1];
                String action = parts[2];
                return casbinService.enforce(username, resource, action);
            }
            else
            {
                // 如果权限格式不标准，使用原始权限字符串作为资源
                return casbinService.enforce(username, permission, "access");
            }
        }
        catch (Exception e)
        {
            return false;
        }
    }

    /**
     * 验证用户是否具备某权限, 如果验证未通过，则抛出异常: NotPermissionException
     * 
     * @param permission 权限字符串
     */
    @Override
    public void checkPermi(String permission)
    {
        if (!hasPermi(permission))
        {
            throw new NotPermissionException(permission);
        }
    }

    /**
     * 使用Casbin验证权限
     * 
     * @param subject 主体（用户）
     * @param object 对象（资源）
     * @param action 动作（操作）
     * @return 是否有权限
     */
    public boolean enforce(String subject, String object, String action)
    {
        return casbinService.enforce(subject, object, action);
    }

    /**
     * 检查Casbin权限，如果验证未通过，则抛出异常
     * 
     * @param subject 主体（用户）
     * @param object 对象（资源）
     * @param action 动作（操作）
     */
    public void checkEnforce(String subject, String object, String action)
    {
        if (!enforce(subject, object, action))
        {
            throw new NotPermissionException("权限不足: " + subject + " -> " + object + ":" + action);
        }
    }

    /**
     * 为用户添加角色
     * 
     * @param user 用户
     * @param role 角色
     * @return 是否添加成功
     */
    public boolean addRoleForUser(String user, String role)
    {
        return casbinService.addRoleForUser(user, role);
    }

    /**
     * 删除用户角色
     * 
     * @param user 用户
     * @param role 角色
     * @return 是否删除成功
     */
    public boolean deleteRoleForUser(String user, String role)
    {
        return casbinService.deleteRoleForUser(user, role);
    }

    /**
     * 添加权限策略
     * 
     * @param subject 主体
     * @param object 对象
     * @param action 动作
     * @return 是否添加成功
     */
    public boolean addPolicy(String subject, String object, String action)
    {
        return casbinService.addPolicy(subject, object, action);
    }

    /**
     * 删除权限策略
     * 
     * @param subject 主体
     * @param object 对象
     * @param action 动作
     * @return 是否删除成功
     */
    public boolean removePolicy(String subject, String object, String action)
    {
        return casbinService.removePolicy(subject, object, action);
    }

    /**
     * 判断是否包含权限（兼容原有逻辑）
     * 
     * @param authorities 权限列表
     * @param permission 权限字符串
     * @return 用户是否具备某权限
     */
    @Override
    public boolean hasPermi(Collection<String> authorities, String permission)
    {
        return authorities.stream().filter(StringUtils::hasText)
                .anyMatch(x -> ALL_PERMISSION.equals(x) || PatternMatchUtils.simpleMatch(x, permission));
    }

    /**
     * 判断是否包含角色（兼容原有逻辑）
     * 
     * @param roles 角色列表
     * @param role 角色
     * @return 用户是否具备某角色权限
     */
    @Override
    public boolean hasRole(Collection<String> roles, String role)
    {
        return roles.stream().filter(StringUtils::hasText)
                .anyMatch(x -> SUPER_ADMIN.equals(x) || PatternMatchUtils.simpleMatch(x, role));
    }
}