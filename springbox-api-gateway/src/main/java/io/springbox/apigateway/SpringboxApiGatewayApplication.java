package io.springbox.apigateway;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.oauth2.client.EnableOAuth2Sso;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.cloud.netflix.zuul.EnableZuulProxy;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.oauth2.client.OAuth2ClientContext;
import org.springframework.security.oauth2.client.OAuth2RestTemplate;
import org.springframework.security.oauth2.client.resource.OAuth2ProtectedResourceDetails;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.web.client.RestTemplate;

@SpringBootApplication
@EnableDiscoveryClient
@EnableZuulProxy
@EnableOAuth2Sso
public class SpringboxApiGatewayApplication  extends WebSecurityConfigurerAdapter {

    public static void main(String[] args) {
        SpringApplication.run(SpringboxApiGatewayApplication.class, args);
    }
    
    @LoadBalanced
    @Bean
    @Qualifier("loadBalancedRestTemplate")
    public RestTemplate loadBalancedRestTemplate(RestTemplateBuilder builder){
    	return builder.build();
    }
    
    @LoadBalanced
    @Bean
    @Primary
    public OAuth2RestTemplate OAuth2RestTemplate(OAuth2ProtectedResourceDetails resource, OAuth2ClientContext clientContext) {
        return new OAuth2RestTemplate(resource, clientContext);
    }


	@Override
	public void configure(HttpSecurity http) throws Exception {
		http.logout().and().antMatcher("/**").authorizeRequests()
				.antMatchers("/index.html", "/**/*.html", "/home.html", "/", "/login", "/beans", "/hystrix.stream").permitAll()
				.antMatchers(HttpMethod.OPTIONS).permitAll()
				.antMatchers(HttpMethod.GET, "/recommendations/**","/reviews/**","/people/**","/movie/**","/catalog/**","/likes/**").permitAll()
				.anyRequest().authenticated().and()
				.csrf()
                  .csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse());
    }
}
