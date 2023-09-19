package com.situ.jurisdiction.filter;

import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.situ.jurisdiction.exception.CaptchaException;
import com.situ.jurisdiction.security.LoginFailureHandler;
import com.situ.jurisdiction.util.RedisUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class CaptchaFilter extends OncePerRequestFilter {
    private final String Login="/user/login";
    @Autowired
    private RedisUtil redisUtil;
    @Autowired
    private LoginFailureHandler loginFailureHandler;
    @Override
    public void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String url=request.getRequestURI();
        String method=request.getMethod();
        if(Login.equals(url) && "POST".equals(method)){
            try{
                validate(request);
            }catch (CaptchaException e){
                loginFailureHandler.onAuthenticationFailure(request,response,e);
            }
        }
        filterChain.doFilter(request,response);
    }

    public void validate(HttpServletRequest request){
        String key = request.getParameter("key");
        String code = request.getParameter("code");
        if(StringUtils.isBlank(key) || StringUtils.isBlank(code)) {
            throw new CaptchaException("验证码为空");
        }
        //获取redis中存放的验证码
        Object captchaRedis = redisUtil.get(key);
        //判断验证码是否正确
        if(!code.equals(captchaRedis)) {
            throw new CaptchaException("验证码错误");
        }
    }

}
