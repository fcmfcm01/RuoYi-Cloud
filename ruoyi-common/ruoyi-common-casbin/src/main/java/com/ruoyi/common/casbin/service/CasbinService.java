package com.ruoyi.common.casbin.service;

import org.casbin.jcasbin.main.Enforcer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Casbin权限服务
 * 
 * @author ruoyi
 */
@Service
public class CasbinService
{
    @Autowired
    private Enforcer enforcer;

    /**
     * 检查权限
     * 
     * @param subject 主体（用户）
     * @param object 对象（资源）
     * @param action 动作（操作）
     * @return 是否有权限
     */
    public boolean enforce(String subject, String object, String action)
    {
        return enforcer.enforce(subject, object, action);
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
        return enforcer.addPolicy(subject, object, action);
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
        return enforcer.removePolicy(subject, object, action);
    }

    /**
     * 添加角色继承关系
     * 
     * @param user 用户
     * @param role 角色
     * @return 是否添加成功
     */
    public boolean addRoleForUser(String user, String role)
    {
        return enforcer.addRoleForUser(user, role);
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
        return enforcer.deleteRoleForUser(user, role);
    }

    /**
     * 删除用户的所有角色
     * 
     * @param user 用户
     * @return 是否删除成功
     */
    public boolean deleteRolesForUser(String user)
    {
        return enforcer.deleteRolesForUser(user);
    }

    /**
     * 获取用户的所有角色
     * 
     * @param user 用户
     * @return 角色列表
     */
    public List<String> getRolesForUser(String user)
    {
        return enforcer.getRolesForUser(user);
    }

    /**
     * 获取拥有指定角色的所有用户
     * 
     * @param role 角色
     * @return 用户列表
     */
    public List<String> getUsersForRole(String role)
    {
        return enforcer.getUsersForRole(role);
    }

    /**
     * 检查用户是否拥有指定角色
     * 
     * @param user 用户
     * @param role 角色
     * @return 是否拥有角色
     */
    public boolean hasRoleForUser(String user, String role)
    {
        return enforcer.hasRoleForUser(user, role);
    }

    /**
     * 获取所有策略
     * 
     * @return 策略列表
     */
    public List<List<String>> getPolicy()
    {
        return enforcer.getPolicy();
    }

    /**
     * 获取所有角色继承关系
     * 
     * @return 角色继承关系列表
     */
    public List<List<String>> getGroupingPolicy()
    {
        return enforcer.getGroupingPolicy();
    }

    /**
     * 保存策略到存储
     */
    public void savePolicy()
    {
        enforcer.savePolicy();
    }

    /**
     * 从存储加载策略
     */
    public void loadPolicy()
    {
        enforcer.loadPolicy();
    }
}