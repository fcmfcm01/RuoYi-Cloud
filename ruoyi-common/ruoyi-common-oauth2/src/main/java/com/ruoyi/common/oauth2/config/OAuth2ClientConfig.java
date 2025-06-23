package com.ruoyi.common.oauth2.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.client.registration.InMemoryClientRegistrationRepository;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.ClientAuthenticationMethod;
import org.springframework.security.oauth2.core.oidc.IdTokenClaimNames;
import org.springframework.security.web.SecurityFilterChain;

/**
 * OAuth2.0客户端配置
 * 
 * @author ruoyi
 */
@Configuration
@EnableWebSecurity
public class OAuth2ClientConfig
{

    /**
     * OAuth2.0客户端注册信息
     */
    @Bean
    public ClientRegistrationRepository clientRegistrationRepository()
    {
        return new InMemoryClientRegistrationRepository(this.ruoyiClientRegistration());
    }

    /**
     * 若依OAuth2.0客户端注册信息
     */
    private ClientRegistration ruoyiClientRegistration()
    {
        return ClientRegistration.withRegistrationId("ruoyi")
                .clientId("ruoyi-client")
                .clientSecret("ruoyi-secret")
                .clientAuthenticationMethod(ClientAuthenticationMethod.CLIENT_SECRET_BASIC)
                .authorizationGrantType(AuthorizationGrantType.AUTHORIZATION_CODE)
                .redirectUri("{baseUrl}/login/oauth2/code/{registrationId}")
                .scope("openid", "profile", "read", "write")
                .authorizationUri("http://localhost:9000/oauth2/authorize")
                .tokenUri("http://localhost:9000/oauth2/token")
                .userInfoUri("http://localhost:9000/userinfo")
                .userNameAttributeName(IdTokenClaimNames.SUB)
                .jwkSetUri("http://localhost:9000/oauth2/jwks")
                .clientName("RuoYi OAuth2.0 Client")
                .build();
    }

    /**
     * OAuth2.0客户端安全配置
     */
    @Bean
    public SecurityFilterChain oauth2ClientFilterChain(HttpSecurity http) throws Exception
    {
        http
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers("/", "/login/**", "/oauth2/**").permitAll()
                        .anyRequest().authenticated()
                )
                .oauth2Login(oauth2Login -> oauth2Login
                        .loginPage("/login")
                        .defaultSuccessUrl("/dashboard", true)
                )
                .oauth2Client(oauth2Client -> {
                    // OAuth2.0客户端配置
                });

        return http.build();
    }
}