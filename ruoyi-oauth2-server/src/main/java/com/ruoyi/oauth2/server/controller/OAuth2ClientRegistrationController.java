package com.ruoyi.oauth2.server.controller;

import com.ruoyi.oauth2.server.dto.ClientRegistrationRequest;
import com.ruoyi.oauth2.server.dto.ClientRegistrationResponse;
import com.ruoyi.oauth2.server.service.OAuth2ClientRegistrationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * OAuth2.0客户端注册管理控制器
 * 
 * @author ruoyi
 */
@RestController
@RequestMapping("/oauth2/clients")
@Validated
public class OAuth2ClientRegistrationController
{
    @Autowired
    private OAuth2ClientRegistrationService clientRegistrationService;

    /**
     * 注册新客户端
     */
    @PostMapping("/register")
    public ResponseEntity<Map<String, Object>> registerClient(@Valid @RequestBody ClientRegistrationRequest request)
    {
        try
        {
            ClientRegistrationResponse response = clientRegistrationService.registerClient(request);
            
            Map<String, Object> result = new HashMap<>();
            result.put("code", 200);
            result.put("message", "客户端注册成功");
            result.put("data", response);
            
            return ResponseEntity.ok(result);
        }
        catch (Exception e)
        {
            Map<String, Object> result = new HashMap<>();
            result.put("code", 500);
            result.put("message", "客户端注册失败: " + e.getMessage());
            
            return ResponseEntity.status(500).body(result);
        }
    }

    /**
     * 获取客户端详情
     */
    @GetMapping("/{clientId}")
    public ResponseEntity<Map<String, Object>> getClient(@PathVariable String clientId)
    {
        try
        {
            ClientRegistrationResponse response = clientRegistrationService.getClient(clientId);
            
            Map<String, Object> result = new HashMap<>();
            result.put("code", 200);
            result.put("message", "获取成功");
            result.put("data", response);
            
            return ResponseEntity.ok(result);
        }
        catch (Exception e)
        {
            Map<String, Object> result = new HashMap<>();
            result.put("code", 404);
            result.put("message", e.getMessage());
            
            return ResponseEntity.status(404).body(result);
        }
    }

    /**
     * 更新客户端
     */
    @PutMapping("/{clientId}")
    public ResponseEntity<Map<String, Object>> updateClient(
            @PathVariable String clientId,
            @Valid @RequestBody ClientRegistrationRequest request)
    {
        try
        {
            ClientRegistrationResponse response = clientRegistrationService.updateClient(clientId, request);
            
            Map<String, Object> result = new HashMap<>();
            result.put("code", 200);
            result.put("message", "客户端更新成功");
            result.put("data", response);
            
            return ResponseEntity.ok(result);
        }
        catch (Exception e)
        {
            Map<String, Object> result = new HashMap<>();
            result.put("code", 500);
            result.put("message", "客户端更新失败: " + e.getMessage());
            
            return ResponseEntity.status(500).body(result);
        }
    }

    /**
     * 重置客户端密钥
     */
    @PostMapping("/{clientId}/reset-secret")
    public ResponseEntity<Map<String, Object>> resetClientSecret(@PathVariable String clientId)
    {
        try
        {
            String newSecret = clientRegistrationService.resetClientSecret(clientId);
            
            Map<String, Object> result = new HashMap<>();
            result.put("code", 200);
            result.put("message", "客户端密钥重置成功");
            result.put("data", Map.of("clientSecret", newSecret));
            
            return ResponseEntity.ok(result);
        }
        catch (Exception e)
        {
            Map<String, Object> result = new HashMap<>();
            result.put("code", 500);
            result.put("message", "客户端密钥重置失败: " + e.getMessage());
            
            return ResponseEntity.status(500).body(result);
        }
    }

    /**
     * 启用/禁用客户端
     */
    @PostMapping("/{clientId}/toggle-status")
    public ResponseEntity<Map<String, Object>> toggleClientStatus(@PathVariable String clientId)
    {
        try
        {
            clientRegistrationService.toggleClientStatus(clientId);
            
            Map<String, Object> result = new HashMap<>();
            result.put("code", 200);
            result.put("message", "客户端状态切换成功");
            
            return ResponseEntity.ok(result);
        }
        catch (Exception e)
        {
            Map<String, Object> result = new HashMap<>();
            result.put("code", 500);
            result.put("message", "客户端状态切换失败: " + e.getMessage());
            
            return ResponseEntity.status(500).body(result);
        }
    }

    /**
     * 删除客户端
     */
    @DeleteMapping("/{clientId}")
    public ResponseEntity<Map<String, Object>> deleteClient(@PathVariable String clientId)
    {
        try
        {
            clientRegistrationService.deleteClient(clientId);
            
            Map<String, Object> result = new HashMap<>();
            result.put("code", 200);
            result.put("message", "客户端删除成功");
            
            return ResponseEntity.ok(result);
        }
        catch (Exception e)
        {
            Map<String, Object> result = new HashMap<>();
            result.put("code", 500);
            result.put("message", "客户端删除失败: " + e.getMessage());
            
            return ResponseEntity.status(500).body(result);
        }
    }

    /**
     * 分页查询客户端
     */
    @GetMapping
    public ResponseEntity<Map<String, Object>> getClients(
            @RequestParam(required = false) String clientName,
            @RequestParam(required = false) Integer status,
            @RequestParam(required = false) String createdBy,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "createTime") String sortBy,
            @RequestParam(defaultValue = "desc") String sortDir)
    {
        try
        {
            Sort sort = Sort.by(Sort.Direction.fromString(sortDir), sortBy);
            Pageable pageable = PageRequest.of(page, size, sort);
            
            Page<ClientRegistrationResponse> clients = clientRegistrationService.getClients(clientName, status, createdBy, pageable);
            
            Map<String, Object> result = new HashMap<>();
            result.put("code", 200);
            result.put("message", "查询成功");
            result.put("data", clients.getContent());
            result.put("total", clients.getTotalElements());
            result.put("totalPages", clients.getTotalPages());
            result.put("currentPage", clients.getNumber());
            result.put("size", clients.getSize());
            
            return ResponseEntity.ok(result);
        }
        catch (Exception e)
        {
            Map<String, Object> result = new HashMap<>();
            result.put("code", 500);
            result.put("message", "查询失败: " + e.getMessage());
            
            return ResponseEntity.status(500).body(result);
        }
    }

    /**
     * 获取启用的客户端列表
     */
    @GetMapping("/enabled")
    public ResponseEntity<Map<String, Object>> getEnabledClients()
    {
        try
        {
            List<ClientRegistrationResponse> clients = clientRegistrationService.getEnabledClients();
            
            Map<String, Object> result = new HashMap<>();
            result.put("code", 200);
            result.put("message", "查询成功");
            result.put("data", clients);
            
            return ResponseEntity.ok(result);
        }
        catch (Exception e)
        {
            Map<String, Object> result = new HashMap<>();
            result.put("code", 500);
            result.put("message", "查询失败: " + e.getMessage());
            
            return ResponseEntity.status(500).body(result);
        }
    }

    /**
     * 获取客户端配置示例
     */
    @GetMapping("/config-example")
    public ResponseEntity<Map<String, Object>> getConfigExample()
    {
        Map<String, Object> example = new HashMap<>();
        example.put("clientName", "示例应用");
        example.put("clientAuthenticationMethods", List.of("client_secret_basic", "client_secret_post"));
        example.put("authorizationGrantTypes", List.of("authorization_code", "refresh_token"));
        example.put("redirectUris", List.of("http://127.0.0.1:8080/login/oauth2/code/ruoyi"));
        example.put("postLogoutRedirectUris", List.of("http://127.0.0.1:8080/logged-out"));
        example.put("scopes", List.of("openid", "profile", "read", "write"));
        example.put("description", "这是一个示例OAuth2.0客户端应用");
        example.put("requireAuthorizationConsent", true);
        example.put("requireProofKey", false);
        example.put("accessTokenTimeToLive", 3600);
        example.put("refreshTokenTimeToLive", 7200);
        example.put("reuseRefreshTokens", true);
        
        Map<String, Object> result = new HashMap<>();
        result.put("code", 200);
        result.put("message", "获取成功");
        result.put("data", example);
        
        return ResponseEntity.ok(result);
    }
}