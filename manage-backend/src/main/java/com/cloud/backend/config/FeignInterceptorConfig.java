package com.cloud.backend.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.authentication.OAuth2AuthenticationDetails;

import feign.RequestInterceptor;
import feign.RequestTemplate;

/**
 * 使用feign client访问别的微服务时，将access_token放入参数或者header<br>
 * 任选其一即可，<br>
 * 如token为xxx<br>
 * 参数形式就是access_token=xxx<br>
 * header的话，是Authorization:Bearer xxx<br>
 * 我们默认放在header里
 * 
 * @author allen
 *
 */
@Configuration
public class FeignInterceptorConfig {

	@Bean
	public RequestInterceptor requestInterceptor() {
		RequestInterceptor requestInterceptor = new RequestInterceptor() {

			@Override
			public void apply(RequestTemplate template) {
				Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
				if (authentication != null) {
					if (authentication instanceof OAuth2Authentication) {
						OAuth2AuthenticationDetails details = (OAuth2AuthenticationDetails) authentication.getDetails();
						String access_token = details.getTokenValue();

						template.header("Authorization", OAuth2AccessToken.BEARER_TYPE + " " + access_token);
//						template.query(OAuth2AccessToken.ACCESS_TOKEN, access_token);
					}

				}
			}
		};

		return requestInterceptor;
	}
}
