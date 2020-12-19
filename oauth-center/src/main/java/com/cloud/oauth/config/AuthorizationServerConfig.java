package com.cloud.oauth.config;

import com.cloud.model.user.LoginAppUser;
import com.cloud.oauth.service.impl.RandomAuthenticationKeyGenerator;
import com.cloud.oauth.service.impl.RedisAuthorizationCodeServices;
import com.cloud.oauth.service.impl.RedisClientDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.oauth2.common.DefaultOAuth2AccessToken;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.DefaultAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.DefaultUserAuthenticationConverter;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;
import org.springframework.security.oauth2.provider.token.store.redis.RedisTokenStore;

import java.util.HashMap;
import java.util.Map;

/**
 * 授权服务器配置
 *
 * @author allen zhangxuelun@cmsr.chinamobile.com
 */
@Configuration
@EnableAuthorizationServer
public class AuthorizationServerConfig extends AuthorizationServerConfigurerAdapter {

    /**
     * 认证管理器
     *
     * @see SecurityConfig 的authenticationManagerBean()
     */
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private RedisConnectionFactory redisConnectionFactory;
    /**
     * 使用jwt或者redis<br>
     * 默认redis
     */
    @Value("${access_token.store-jwt:false}")
    private boolean storeWithJwt;
    /**
     * 登陆后返回的json数据是否追加当前用户信息<br>
     * 默认false
     */
    @Value("${access_token.add-userinfo:false}")
    private boolean addUserInfo;
    @Autowired
    private RedisAuthorizationCodeServices redisAuthorizationCodeServices;
    @Autowired
    private RedisClientDetailsService redisClientDetailsService;

    /**
     * 令牌存储
     */
    @Bean
    public TokenStore tokenStore() {
        if (storeWithJwt) {
            return new JwtTokenStore(accessTokenConverter());
        }
        RedisTokenStore redisTokenStore = new RedisTokenStore(redisConnectionFactory);
        // 解决同一username每次登陆access_token都相同的问题
        redisTokenStore.setAuthenticationKeyGenerator(new RandomAuthenticationKeyGenerator());

        return redisTokenStore;
    }

    @Override
    public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
        endpoints.authenticationManager(this.authenticationManager);
        endpoints.tokenStore(tokenStore());
        // 授权码模式下，code存储
//		endpoints.authorizationCodeServices(new JdbcAuthorizationCodeServices(dataSource));
        endpoints.authorizationCodeServices(redisAuthorizationCodeServices);
        if (storeWithJwt) {
            endpoints.accessTokenConverter(accessTokenConverter());
        } else {
            // 2019.07.13 将当前用户信息追加到登陆后返回数据里
            endpoints.tokenEnhancer((accessToken, authentication) -> {
                addLoginUserInfo(accessToken, authentication);
                return accessToken;
            });
        }
    }

    /**
     * 将当前用户信息追加到登陆后返回的json数据里<br>
     * 通过参数access_token.add-userinfo控制<br>
     * 2019.07.13
     *
     * @param accessToken
     * @param authentication
     */
    private void addLoginUserInfo(OAuth2AccessToken accessToken, OAuth2Authentication authentication) {
        if (!addUserInfo) {
            return;
        }

        if (accessToken instanceof DefaultOAuth2AccessToken) {
            DefaultOAuth2AccessToken defaultOAuth2AccessToken = (DefaultOAuth2AccessToken) accessToken;

            Authentication userAuthentication = authentication.getUserAuthentication();
            Object principal = userAuthentication.getPrincipal();
            if (principal instanceof LoginAppUser) {
                LoginAppUser loginUser = (LoginAppUser) principal;

                Map<String, Object> map = new HashMap<>(defaultOAuth2AccessToken.getAdditionalInformation()); // 旧的附加参数
                map.put("loginUser", loginUser); // 追加当前登陆用户

                defaultOAuth2AccessToken.setAdditionalInformation(map);
            }
        }
    }

    @Override
    public void configure(AuthorizationServerSecurityConfigurer security) throws Exception {
        security.allowFormAuthenticationForClients(); // 允许表单形式的认证
    }

//    @Autowired
//    private BCryptPasswordEncoder bCryptPasswordEncoder;

    /**
     * 我们将client信息存储到oauth_client_details表里<br>
     * 并将数据缓存到redis
     *
     * @param clients
     * @throws Exception
     */
    @Override
    public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
//		clients.inMemory().withClient("system").secret(bCryptPasswordEncoder.encode("system"))
//				.authorizedGrantTypes("password", "authorization_code", "refresh_token").scopes("app")
//				.accessTokenValiditySeconds(3600);

//		clients.jdbc(dataSource);
        // 2019.06.06，这里优化一下，详细看下redisClientDetailsService这个实现类
        clients.withClientDetails(redisClientDetailsService);
        redisClientDetailsService.loadAllClientToCache();
    }

    @Autowired
    public UserDetailsService userDetailsService;
    /**
     * jwt签名key，可随意指定<br>
     * 如配置文件里不设置的话，冒号后面的是默认值
     */
    @Value("${access_token.jwt-signing-key:lucy_lun}")
    private String signingKey;

    /**
     * Jwt资源令牌转换器<br>
     * 参数access_token.store-jwt为true时用到
     *
     * @return accessTokenConverter
     */
    @Bean
    public JwtAccessTokenConverter accessTokenConverter() {
        JwtAccessTokenConverter jwtAccessTokenConverter = new JwtAccessTokenConverter() {
            @Override
            public OAuth2AccessToken enhance(OAuth2AccessToken accessToken, OAuth2Authentication authentication) {
                OAuth2AccessToken oAuth2AccessToken = super.enhance(accessToken, authentication);
                addLoginUserInfo(oAuth2AccessToken, authentication); // 2019.07.13 将当前用户信息追加到登陆后返回数据里
                return oAuth2AccessToken;
            }
        };
        DefaultAccessTokenConverter defaultAccessTokenConverter = (DefaultAccessTokenConverter) jwtAccessTokenConverter
                .getAccessTokenConverter();
        DefaultUserAuthenticationConverter userAuthenticationConverter = new DefaultUserAuthenticationConverter();
        userAuthenticationConverter.setUserDetailsService(userDetailsService);

        defaultAccessTokenConverter.setUserTokenConverter(userAuthenticationConverter);
        // 2019.06.29 这里务必设置一个，否则多台认证中心的话，一旦使用jwt方式，access_token将解析错误
        jwtAccessTokenConverter.setSigningKey(signingKey);

        return jwtAccessTokenConverter;
    }

}
