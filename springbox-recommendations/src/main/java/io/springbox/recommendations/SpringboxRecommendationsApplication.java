package io.springbox.recommendations;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;

@SpringBootApplication
@EnableDiscoveryClient
@EntityScan("io.springbox.recommendations.domain")
@EnableGlobalMethodSecurity(prePostEnabled = true)
@EnableResourceServer
public class SpringboxRecommendationsApplication extends ResourceServerConfigurerAdapter{

    public static void main(String[] args) {
        SpringApplication.run(SpringboxRecommendationsApplication.class, args);
    }

    @Override
    public void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()

                .antMatchers(HttpMethod.POST, "/recommendations/**").authenticated()
                .antMatchers(HttpMethod.POST,"/people/**").authenticated()
                .antMatchers(HttpMethod.POST,"/movie/**").authenticated()
                .antMatchers(HttpMethod.GET,"/does/**/").authenticated()

                .and().authorizeRequests()
                .anyRequest().permitAll();


    }
}
