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
  `description` varchar(500) DEFAULT NULL COMMENT '描述',
  `status` int(1) DEFAULT 1 COMMENT '状态(1-启用 0-禁用)',
  `created_by` varchar(64) DEFAULT NULL COMMENT '创建者',
  `updated_by` varchar(64) DEFAULT NULL COMMENT '更新者',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_client_id` (`client_id`),
  KEY `idx_status` (`status`),
  KEY `idx_create_time` (`create_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='OAuth2.0客户端注册表';

-- ----------------------------
-- 第三方OAuth2.0服务器配置表
-- ----------------------------
DROP TABLE IF EXISTS `oauth2_third_party_server`;
CREATE TABLE `oauth2_third_party_server` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `server_name` varchar(100) NOT NULL COMMENT '服务器名称',
  `server_code` varchar(50) NOT NULL COMMENT '服务器代码',
  `authorization_uri` varchar(500) NOT NULL COMMENT '授权URI',
  `token_uri` varchar(500) NOT NULL COMMENT '令牌URI',
  `user_info_uri` varchar(500) DEFAULT NULL COMMENT '用户信息URI',
  `jwk_set_uri` varchar(500) DEFAULT NULL COMMENT 'JWK Set URI',
  `issuer_uri` varchar(500) DEFAULT NULL COMMENT 'Issuer URI',
  `client_id` varchar(200) NOT NULL COMMENT '客户端ID',
  `client_secret` varchar(500) NOT NULL COMMENT '客户端密钥',
  `scopes` varchar(1000) DEFAULT NULL COMMENT '授权范围',
  `user_name_attribute` varchar(100) DEFAULT NULL COMMENT '用户名属性',
  `client_name` varchar(200) DEFAULT NULL COMMENT '客户端名称',
  `redirect_uri_template` varchar(500) DEFAULT NULL COMMENT '重定向URI模板',
  `provider_type` varchar(50) DEFAULT NULL COMMENT '提供商类型',
  `description` varchar(500) DEFAULT NULL COMMENT '描述',
  `status` int(1) DEFAULT 1 COMMENT '状态(1-启用 0-禁用)',
  `sort_order` int(11) DEFAULT 0 COMMENT '排序顺序',
  `created_by` varchar(64) DEFAULT NULL COMMENT '创建者',
  `updated_by` varchar(64) DEFAULT NULL COMMENT '更新者',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_server_code` (`server_code`),
  KEY `idx_provider_type` (`provider_type`),
  KEY `idx_status` (`status`),
  KEY `idx_sort_order` (`sort_order`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='第三方OAuth2.0服务器配置表';

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
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`registered_client_id`, `principal_name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='OAuth2.0授权同意表';

-- ----------------------------
-- 初始化OAuth2.0客户端数据
-- ----------------------------
INSERT INTO `oauth2_registered_client` (`id`, `client_id`, `client_name`, `client_secret`, `client_authentication_methods`, `authorization_grant_types`, `redirect_uris`, `post_logout_redirect_uris`, `scopes`, `client_settings`, `token_settings`, `description`, `status`) VALUES
('ruoyi-client-id', 'ruoyi-client', 'RuoYi默认客户端', '{bcrypt}$2a$10$MF7hYnWLeLT66gNccBgxaONZHbrSMjlUofkp0ta0MKlAFGTsn1Wr2', 'client_secret_basic,client_secret_post', 'authorization_code,refresh_token,client_credentials', 'http://127.0.0.1:8080/login/oauth2/code/ruoyi-client,http://127.0.0.1:8080/authorized', 'http://127.0.0.1:8080/logged-out', 'openid,profile,read,write', '{"@class":"java.util.Collections$UnmodifiableMap","settings.client.require-proof-key":false,"settings.client.require-authorization-consent":true}', '{"@class":"java.util.Collections$UnmodifiableMap","settings.token.reuse-refresh-tokens":true,"settings.token.id-token-signature-algorithm":["org.springframework.security.oauth2.jose.jws.SignatureAlgorithm","RS256"],"settings.token.access-token-time-to-live":["java.time.Duration",3600.000000000],"settings.token.access-token-format":{"@class":"org.springframework.security.oauth2.server.authorization.settings.OAuth2TokenFormat","value":"self-contained"},"settings.token.refresh-token-time-to-live":["java.time.Duration",7200.000000000],"settings.token.authorization-code-time-to-live":["java.time.Duration",300.000000000]}', 'RuoYi系统默认OAuth2.0客户端', 1);

-- ----------------------------
-- 初始化第三方OAuth服务器数据
-- ----------------------------
INSERT INTO `oauth2_third_party_server` (`server_name`, `server_code`, `authorization_uri`, `token_uri`, `user_info_uri`, `client_id`, `client_secret`, `scopes`, `user_name_attribute`, `client_name`, `redirect_uri_template`, `provider_type`, `description`, `status`, `sort_order`) VALUES
('GitHub OAuth', 'github', 'https://github.com/login/oauth/authorize', 'https://github.com/login/oauth/access_token', 'https://api.github.com/user', 'your-github-client-id', '{bcrypt}$2a$10$MF7hYnWLeLT66gNccBgxaONZHbrSMjlUofkp0ta0MKlAFGTsn1Wr2', 'user:email,read:user', 'login', 'GitHub', '{baseUrl}/login/oauth2/code/{registrationId}', 'github', 'GitHub第三方OAuth2.0登录', 0, 1),
('Google OAuth', 'google', 'https://accounts.google.com/o/oauth2/v2/auth', 'https://www.googleapis.com/oauth2/v4/token', 'https://www.googleapis.com/oauth2/v3/userinfo', 'your-google-client-id', '{bcrypt}$2a$10$MF7hYnWLeLT66gNccBgxaONZHbrSMjlUofkp0ta0MKlAFGTsn1Wr2', 'openid,profile,email', 'sub', 'Google', '{baseUrl}/login/oauth2/code/{registrationId}', 'google', 'Google第三方OAuth2.0登录', 0, 2),
('微信开放平台', 'wechat', 'https://open.weixin.qq.com/connect/qrconnect', 'https://api.weixin.qq.com/sns/oauth2/access_token', 'https://api.weixin.qq.com/sns/userinfo', 'your-wechat-appid', '{bcrypt}$2a$10$MF7hYnWLeLT66gNccBgxaONZHbrSMjlUofkp0ta0MKlAFGTsn1Wr2', 'snsapi_login', 'openid', '微信', '{baseUrl}/login/oauth2/code/{registrationId}', 'wechat', '微信开放平台第三方登录', 0, 3),
('钉钉开放平台', 'dingtalk', 'https://oapi.dingtalk.com/connect/oauth2/sns_authorize', 'https://oapi.dingtalk.com/sns/gettoken', 'https://oapi.dingtalk.com/sns/getuserinfo', 'your-dingtalk-appid', '{bcrypt}$2a$10$MF7hYnWLeLT66gNccBgxaONZHbrSMjlUofkp0ta0MKlAFGTsn1Wr2', 'snsapi_login', 'openid', '钉钉', '{baseUrl}/login/oauth2/code/{registrationId}', 'dingtalk', '钉钉开放平台第三方登录', 0, 4);