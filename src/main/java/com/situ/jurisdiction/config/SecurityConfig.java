package com.situ.jurisdiction.config;

import com.situ.jurisdiction.filter.CaptchaFilter;
import com.situ.jurisdiction.security.LoginFailureHandler;
import com.situ.jurisdiction.security.LoginSuccessHandler;
import com.situ.jurisdiction.service.impl.UserDetailsServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableGlobalMethodSecurity(prePostEnabled = true)
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    @Autowired
    private LoginFailureHandler loginFailureHandler;

    @Autowired
    private CaptchaFilter captchaFilter;

    @Autowired
    private LoginSuccessHandler loginSuccessHandler;

    //白名单
    public static final String[] URL_WHITELIST = {
            "/code",
            "/login",
            "/logout",
    };

    @Bean
    BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Autowired
    UserDetailsServiceImpl userDetailsService;
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService);
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.cors().and().csrf().disable()
                .formLogin()
                .failureHandler(loginFailureHandler) //配置登录失败处理器
                .successHandler(loginSuccessHandler)
                .and()
                .authorizeRequests()
                .antMatchers(URL_WHITELIST).permitAll() //白名单放行
                .anyRequest().authenticated() //其他地址必须认证
                .and()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS) //不会创建 session
                .and()
                //登录验证码校验过滤器放在UsernamePasswordAuthenticationFilter之前
                .addFilterBefore(captchaFilter, UsernamePasswordAuthenticationFilter.class);
    }
}