package com.ruoyi.system.controller.casbin;

import com.ruoyi.common.core.domain.R;
import com.ruoyi.common.core.web.controller.BaseController;
import com.ruoyi.common.security.annotation.RequiresPermissions;
import com.ruoyi.common.security.auth.AuthUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Casbin权限管理控制器
 * 
 * @author ruoyi
 */
@RestController
@RequestMapping("/casbin")
public class CasbinController extends BaseController
{
    /**
     * 检查权限
     */
    @GetMapping("/enforce")
    @RequiresPermissions("system:casbin:enforce")
    public R<Boolean> enforce(@RequestParam String subject, 
                             @RequestParam String object, 
                             @RequestParam String action)
    {
        boolean result = AuthUtil.enforce(subject, object, action);
        return R.ok(result);
    }

    /**
     * 添加权限策略
     */
    @PostMapping("/policy")
    @RequiresPermissions("system:casbin:policy:add")
    public R<Boolean> addPolicy(@RequestParam String subject, 
                               @RequestParam String object, 
                               @RequestParam String action)
    {
        boolean result = AuthUtil.addPolicy(subject, object, action);
        return R.ok(result);
    }

    /**
     * 删除权限策略
     */
    @DeleteMapping("/policy")
    @RequiresPermissions("system:casbin:policy:remove")
    public R<Boolean> removePolicy(@RequestParam String subject, 
                                  @RequestParam String object, 
                                  @RequestParam String action)
    {
        boolean result = AuthUtil.removePolicy(subject, object, action);
        return R.ok(result);
    }

    /**
     * 为用户添加角色
     */
    @PostMapping("/role")
    @RequiresPermissions("system:casbin:role:add")
    public R<Boolean> addRoleForUser(@RequestParam String user, 
                                    @RequestParam String role)
    {
        boolean result = AuthUtil.addRoleForUser(user, role);
        return R.ok(result);
    }

    /**
     * 删除用户角色
     */
    @DeleteMapping("/role")
    @RequiresPermissions("system:casbin:role:remove")
    public R<Boolean> deleteRoleForUser(@RequestParam String user, 
                                       @RequestParam String role)
    {
        boolean result = AuthUtil.deleteRoleForUser(user, role);
        return R.ok(result);
    }
}