package com.situ.jurisdiction.filter;


import cn.hutool.core.util.StrUtil;
import cn.hutool.jwt.JWTException;
import com.auth0.jwt.exceptions.AlgorithmMismatchException;
import com.auth0.jwt.exceptions.SignatureVerificationException;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.situ.jurisdiction.service.SysUserService;
import com.situ.jurisdiction.util.JwtUtils;
import com.situ.jurisdiction.util.RedisUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Map;


public class JwtAuthenticationFilter extends BasicAuthenticationFilter {

    @Autowired
    private RedisUtil redisUtil;
    @Autowired
    private SysUserService sysUserService;

    public JwtAuthenticationFilter(AuthenticationManager authenticationManager) {
        super(authenticationManager);
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {
        String token = request.getHeader("token");
        if(StrUtil.isBlankOrUndefined(token)){
            chain.doFilter(request,response);
            return;
        }
        Map<String, Object> map = null;

        try {
            //解析JWT，如果出现问题会抛出异常
            map = JwtUtils.parseJwtToMap(token);
        } catch (SignatureVerificationException e) {
            throw new JWTException("无效签名");
        } catch (TokenExpiredException e) {
            throw new JWTException("令牌超时");
        } catch (AlgorithmMismatchException e) {
            throw new JWTException("算法不匹配");
        } catch (Exception e) {
            throw new JWTException("令牌无效");
        }

        Object getId = map.get("id");
        long id=Long.parseLong(getId.toString());

        //设置登录用户信息
        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(map.get("username"), null, getUserAuthority(id));
        SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);

        //放行
        chain.doFilter(request, response);

    }

    //根据用户ID获取用户权限
    public List<GrantedAuthority> getUserAuthority(Long userId) {
        // 通过内置的工具类，把权限字符串封装成GrantedAuthority列表
        return  AuthorityUtils.commaSeparatedStringToAuthorityList(sysUserService.getUserAuthorityInfo(userId));
    }
}