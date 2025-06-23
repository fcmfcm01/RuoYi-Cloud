-- ----------------------------
-- Casbin策略规则表
-- ----------------------------
DROP TABLE IF EXISTS `casbin_rule`;
CREATE TABLE `casbin_rule` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `ptype` varchar(100) NOT NULL COMMENT '策略类型',
  `v0` varchar(100) DEFAULT NULL COMMENT '规则值0',
  `v1` varchar(100) DEFAULT NULL COMMENT '规则值1',
  `v2` varchar(100) DEFAULT NULL COMMENT '规则值2',
  `v3` varchar(100) DEFAULT NULL COMMENT '规则值3',
  `v4` varchar(100) DEFAULT NULL COMMENT '规则值4',
  `v5` varchar(100) DEFAULT NULL COMMENT '规则值5',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  KEY `idx_ptype` (`ptype`),
  KEY `idx_v0` (`v0`),
  KEY `idx_v1` (`v1`),
  KEY `idx_v2` (`v2`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='Casbin策略规则表';

-- ----------------------------
-- 初始化Casbin策略数据
-- ----------------------------
INSERT INTO `casbin_rule` (`ptype`, `v0`, `v1`, `v2`) VALUES
('p', 'admin', 'system:user', 'read'),
('p', 'admin', 'system:user', 'write'),
('p', 'admin', 'system:role', 'read'),
('p', 'admin', 'system:role', 'write'),
('p', 'admin', 'system:menu', 'read'),
('p', 'admin', 'system:menu', 'write'),
('p', 'admin', 'system:dept', 'read'),
('p', 'admin', 'system:dept', 'write'),
('p', 'admin', 'system:post', 'read'),
('p', 'admin', 'system:post', 'write'),
('p', 'admin', 'system:dict', 'read'),
('p', 'admin', 'system:dict', 'write'),
('p', 'admin', 'system:config', 'read'),
('p', 'admin', 'system:config', 'write'),
('p', 'admin', 'system:notice', 'read'),
('p', 'admin', 'system:notice', 'write'),
('p', 'admin', 'system:log', 'read'),
('p', 'admin', 'monitor:online', 'read'),
('p', 'admin', 'monitor:job', 'read'),
('p', 'admin', 'monitor:job', 'write'),
('p', 'admin', 'monitor:druid', 'read'),
('p', 'admin', 'monitor:server', 'read'),
('p', 'admin', 'monitor:cache', 'read'),
('p', 'admin', 'tool:build', 'read'),
('p', 'admin', 'tool:gen', 'read'),
('p', 'admin', 'tool:gen', 'write'),
('p', 'admin', 'tool:swagger', 'read'),
('p', 'user', 'system:user', 'read'),
('p', 'user', 'system:profile', 'read'),
('p', 'user', 'system:profile', 'write'),
('g', 'admin', 'admin_role'),
('g', 'user', 'user_role');

-- ----------------------------
-- OAuth2.0客户端注册表
-- ----------------------------
DROP TABLE IF EXISTS `oauth2_registered_client`;
CREATE TABLE `oauth2_registered_client` (
  `id` varchar(100) NOT NULL COMMENT '客户端ID',
  `client_id` varchar(100) NOT NULL COMMENT '客户端标识',
  `client_id_issued_at` timestamp DEFAULT CURRENT_TIMESTAMP COMMENT '客户端ID签发时间',
  `client_secret` varchar(200) DEFAULT NULL COMMENT '客户端密钥',
  `client_secret_expires_at` timestamp NULL DEFAULT NULL COMMENT '客户端密钥过期时间',
  `client_name` varchar(200) NOT NULL COMMENT '客户端名称',
  `client_authentication_methods` varchar(1000) NOT NULL COMMENT '客户端认证方法',
  `authorization_grant_types` varchar(1000) NOT NULL COMMENT '授权类型',
  `redirect_uris` varchar(1000) DEFAULT NULL COMMENT '重定向URI',
  `post_logout_redirect_uris` varchar(1000) DEFAULT NULL COMMENT '登出重定向URI',
  `scopes` varchar(1000) NOT NULL COMMENT '授权范围',
  `client_settings` varchar(2000) NOT NULL COMMENT '客户端设置',
  `token_settings` varchar(2000) NOT NULL COMMENT '令牌设置',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_client_id` (`client_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='OAuth2.0客户端注册表';

-- ----------------------------
-- OAuth2.0授权表
-- ----------------------------
DROP TABLE IF EXISTS `oauth2_authorization`;
CREATE TABLE `oauth2_authorization` (
  `id` varchar(100) NOT NULL COMMENT '授权ID',
  `registered_client_id` varchar(100) NOT NULL COMMENT '注册客户端ID',
  `principal_name` varchar(200) NOT NULL COMMENT '主体名称',
  `authorization_grant_type` varchar(100) NOT NULL COMMENT '授权类型',
  `authorized_scopes` varchar(1000) DEFAULT NULL COMMENT '授权范围',
  `attributes` text COMMENT '属性',
  `state` varchar(500) DEFAULT NULL COMMENT '状态',
  `authorization_code_value` text COMMENT '授权码值',
  `authorization_code_issued_at` timestamp NULL DEFAULT NULL COMMENT '授权码签发时间',
  `authorization_code_expires_at` timestamp NULL DEFAULT NULL COMMENT '授权码过期时间',
  `authorization_code_metadata` text COMMENT '授权码元数据',
  `access_token_value` text COMMENT '访问令牌值',
  `access_token_issued_at` timestamp NULL DEFAULT NULL COMMENT '访问令牌签发时间',
  `access_token_expires_at` timestamp NULL DEFAULT NULL COMMENT '访问令牌过期时间',
  `access_token_metadata` text COMMENT '访问令牌元数据',
  `access_token_type` varchar(100) DEFAULT NULL COMMENT '访问令牌类型',
  `access_token_scopes` varchar(1000) DEFAULT NULL COMMENT '访问令牌范围',
  `oidc_id_token_value` text COMMENT 'OIDC ID令牌值',
  `oidc_id_token_issued_at` timestamp NULL DEFAULT NULL COMMENT 'OIDC ID令牌签发时间',
  `oidc_id_token_expires_at` timestamp NULL DEFAULT NULL COMMENT 'OIDC ID令牌过期时间',
  `oidc_id_token_metadata` text COMMENT 'OIDC ID令牌元数据',
  `refresh_token_value` text COMMENT '刷新令牌值',
  `refresh_token_issued_at` timestamp NULL DEFAULT NULL COMMENT '刷新令牌签发时间',
  `refresh_token_expires_at` timestamp NULL DEFAULT NULL COMMENT '刷新令牌过期时间',
  `refresh_token_metadata` text COMMENT '刷新令牌元数据',
  `user_code_value` text COMMENT '用户码值',
  `user_code_issued_at` timestamp NULL DEFAULT NULL COMMENT '用户码签发时间',
  `user_code_expires_at` timestamp NULL DEFAULT NULL COMMENT '用户码过期时间',
  `user_code_metadata` text COMMENT '用户码元数据',
  `device_code_value` text COMMENT '设备码值',
  `device_code_issued_at` timestamp NULL DEFAULT NULL COMMENT '设备码签发时间',
  `device_code_expires_at` timestamp NULL DEFAULT NULL COMMENT '设备码过期时间',
  `device_code_metadata` text COMMENT '设备码元数据',
  PRIMARY KEY (`id`),
  KEY `idx_registered_client_id` (`registered_client_id`),
  KEY `idx_principal_name` (`principal_name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='OAuth2.0授权表';

-- ----------------------------
-- OAuth2.0授权同意表
-- ----------------------------
DROP TABLE IF EXISTS `oauth2_authorization_consent`;
CREATE TABLE `oauth2_authorization_consent` (
  `registered_client_id` varchar(100) NOT NULL COMMENT '注册客户端ID',
  `principal_name` varchar(200) NOT NULL COMMENT '主体名称',
  `authorities` varchar(1000) NOT NULL COMMENT '权限',
  PRIMARY KEY (`registered_client_id`, `principal_name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='OAuth2.0授权同意表';