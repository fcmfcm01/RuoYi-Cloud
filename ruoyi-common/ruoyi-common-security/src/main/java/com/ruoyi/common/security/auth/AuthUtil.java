package com.ruoyi.common.security.auth;

import com.ruoyi.common.security.annotation.RequiresPermissions;
import com.ruoyi.common.security.annotation.RequiresRoles;
import com.ruoyi.system.api.model.LoginUser;

/**
 * Token 权限验证工具类
 * 
 * @author ruoyi
 */
public class AuthUtil
{
    /**
     * 底层的 AuthLogic 对象
     * 优先使用CasbinAuthLogic，如果不可用则使用默认的AuthLogic
     */
    public static AuthLogic authLogic = createAuthLogic();

    /**
     * 创建AuthLogic实例
     */
    private static AuthLogic createAuthLogic()
    {
        try
        {
            // 尝试创建CasbinAuthLogic
            Class<?> casbinAuthLogicClass = Class.forName("com.ruoyi.common.casbin.auth.CasbinAuthLogic");
            return (AuthLogic) casbinAuthLogicClass.newInstance();
        }
        catch (Exception e)
        {
            // 如果Casbin模块不可用，使用默认的AuthLogic
            return new AuthLogic();
        }
    }

    /**
     * 会话注销
     */
    public static void logout()
    {
        authLogic.logout();
    }

    /**
     * 会话注销，根据指定Token
     * 
     * @param token 指定token
     */
    public static void logoutByToken(String token)
    {
        authLogic.logoutByToken(token);
    }

    /**
     * 检验当前会话是否已经登录，如未登录，则抛出异常
     */
    public static void checkLogin()
    {
        authLogic.checkLogin();
    }

    /**
     * 获取当前登录用户信息
     * 
     * @param token 指定token
     * @return 用户信息
     */
    public static LoginUser getLoginUser(String token)
    {
        return authLogic.getLoginUser(token);
    }

    /**
     * 验证当前用户有效期
     * 
     * @param loginUser 用户信息
     */
    public static void verifyLoginUserExpire(LoginUser loginUser)
    {
        authLogic.verifyLoginUserExpire(loginUser);
    }

    /**
     * 当前账号是否含有指定角色标识, 返回true或false
     * 
     * @param role 角色标识
     * @return 是否含有指定角色标识
     */
    public static boolean hasRole(String role)
    {
        return authLogic.hasRole(role);
    }

    /**
     * 当前账号是否含有指定角色标识, 如果验证未通过，则抛出异常: NotRoleException
     * 
     * @param role 角色标识
     */
    public static void checkRole(String role)
    {
        authLogic.checkRole(role);
    }

    /**
     * 根据注解传入参数鉴权, 如果验证未通过，则抛出异常: NotRoleException
     * 
     * @param requiresRoles 角色权限注解
     */
    public static void checkRole(RequiresRoles requiresRoles)
    {
        authLogic.checkRole(requiresRoles);
    }

    /**
     * 当前账号是否含有指定角色标识 [指定多个，必须全部验证通过]
     * 
     * @param roles 角色标识数组
     */
    public static void checkRoleAnd(String... roles)
    {
        authLogic.checkRoleAnd(roles);
    }

    /**
     * 当前账号是否含有指定角色标识 [指定多个，只要其一验证通过即可]
     * 
     * @param roles 角色标识数组
     */
    public static void checkRoleOr(String... roles)
    {
        authLogic.checkRoleOr(roles);
    }

    /**
     * 当前账号是否含有指定权限, 返回true或false
     * 
     * @param permission 权限码
     * @return 是否含有指定权限
     */
    public static boolean hasPermi(String permission)
    {
        return authLogic.hasPermi(permission);
    }

    /**
     * 当前账号是否含有指定权限, 如果验证未通过，则抛出异常: NotPermissionException
     * 
     * @param permission 权限码
     */
    public static void checkPermi(String permission)
    {
        authLogic.checkPermi(permission);
    }

    /**
     * 根据注解传入参数鉴权, 如果验证未通过，则抛出异常: NotPermissionException
     * 
     * @param requiresPermissions 权限注解
     */
    public static void checkPermi(RequiresPermissions requiresPermissions)
    {
        authLogic.checkPermi(requiresPermissions);
    }

    /**
     * 当前账号是否含有指定权限 [指定多个，必须全部验证通过]
     * 
     * @param permissions 权限码数组
     */
    public static void checkPermiAnd(String... permissions)
    {
        authLogic.checkPermiAnd(permissions);
    }

    /**
     * 当前账号是否含有指定权限 [指定多个，只要其一验证通过即可]
     * 
     * @param permissions 权限码数组
     */
    public static void checkPermiOr(String... permissions)
    {
        authLogic.checkPermiOr(permissions);
    }

    // ==================== Casbin 扩展方法 ====================

    /**
     * 使用Casbin验证权限
     * 
     * @param subject 主体（用户）
     * @param object 对象（资源）
     * @param action 动作（操作）
     * @return 是否有权限
     */
    public static boolean enforce(String subject, String object, String action)
    {
        try
        {
            if (authLogic.getClass().getName().contains("CasbinAuthLogic"))
            {
                return (Boolean) authLogic.getClass()
                        .getMethod("enforce", String.class, String.class, String.class)
                        .invoke(authLogic, subject, object, action);
            }
        }
        catch (Exception e)
        {
            // 如果调用失败，返回false
        }
        return false;
    }

    /**
     * 检查Casbin权限，如果验证未通过，则抛出异常
     * 
     * @param subject 主体（用户）
     * @param object 对象（资源）
     * @param action 动作（操作）
     */
    public static void checkEnforce(String subject, String object, String action)
    {
        try
        {
            if (authLogic.getClass().getName().contains("CasbinAuthLogic"))
            {
                authLogic.getClass()
                        .getMethod("checkEnforce", String.class, String.class, String.class)
                        .invoke(authLogic, subject, object, action);
            }
        }
        catch (Exception e)
        {
            // 如果调用失败，抛出权限不足异常
            throw new RuntimeException("权限验证失败: " + subject + " -> " + object + ":" + action);
        }
    }

    /**
     * 为用户添加角色
     * 
     * @param user 用户
     * @param role 角色
     * @return 是否添加成功
     */
    public static boolean addRoleForUser(String user, String role)
    {
        try
        {
            if (authLogic.getClass().getName().contains("CasbinAuthLogic"))
            {
                return (Boolean) authLogic.getClass()
                        .getMethod("addRoleForUser", String.class, String.class)
                        .invoke(authLogic, user, role);
            }
        }
        catch (Exception e)
        {
            // 如果调用失败，返回false
        }
        return false;
    }

    /**
     * 删除用户角色
     * 
     * @param user 用户
     * @param role 角色
     * @return 是否删除成功
     */
    public static boolean deleteRoleForUser(String user, String role)
    {
        try
        {
            if (authLogic.getClass().getName().contains("CasbinAuthLogic"))
            {
                return (Boolean) authLogic.getClass()
                        .getMethod("deleteRoleForUser", String.class, String.class)
                        .invoke(authLogic, user, role);
            }
        }
        catch (Exception e)
        {
            // 如果调用失败，返回false
        }
        return false;
    }

    /**
     * 添加权限策略
     * 
     * @param subject 主体
     * @param object 对象
     * @param action 动作
     * @return 是否添加成功
     */
    public static boolean addPolicy(String subject, String object, String action)
    {
        try
        {
            if (authLogic.getClass().getName().contains("CasbinAuthLogic"))
            {
                return (Boolean) authLogic.getClass()
                        .getMethod("addPolicy", String.class, String.class, String.class)
                        .invoke(authLogic, subject, object, action);
            }
        }
        catch (Exception e)
        {
            // 如果调用失败，返回false
        }
        return false;
    }

    /**
     * 删除权限策略
     * 
     * @param subject 主体
     * @param object 对象
     * @param action 动作
     * @return 是否删除成功
     */
    public static boolean removePolicy(String subject, String object, String action)
    {
        try
        {
            if (authLogic.getClass().getName().contains("CasbinAuthLogic"))
            {
                return (Boolean) authLogic.getClass()
                        .getMethod("removePolicy", String.class, String.class, String.class)
                        .invoke(authLogic, subject, object, action);
            }
        }
        catch (Exception e)
        {
            // 如果调用失败，返回false
        }
        return false;
    }
}
