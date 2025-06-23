package com.ruoyi.oauth2.server;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import com.ruoyi.common.security.annotation.EnableCustomConfig;
import com.ruoyi.common.security.annotation.EnableRyFeignClients;

/**
 * OAuth2.0授权服务器
 * 
 * @author ruoyi
 */
@EnableCustomConfig
@EnableRyFeignClients
@EnableDiscoveryClient
@SpringBootApplication
public class RuoYiOAuth2ServerApplication
{
    public static void main(String[] args)
    {
        SpringApplication.run(RuoYiOAuth2ServerApplication.class, args);
        System.out.println("(♥◠‿◠)ﾉﾞ  OAuth2.0授权服务器启动成功   ლ(´ڡ`ლ)ﾞ  ");
    }
}