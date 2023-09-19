package com.situ.jurisdiction.security;

import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.situ.jurisdiction.entity.SysUser;
import com.situ.jurisdiction.service.SysUserService;
import com.situ.jurisdiction.util.JwtUtils;
import com.situ.jurisdiction.util.R;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;

@Component
public class LoginSuccessHandler implements AuthenticationSuccessHandler {
    @Autowired
    private SysUserService sysUserService;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        response.setContentType("application/json;charset=utf-8");
        //获取当前登录用户的用户名
        String username = authentication.getName();
        ServletOutputStream out = response.getOutputStream();
        HashMap<String, Object> map = new HashMap<>();
        SysUser sysUser = sysUserService.getSysUser(username);
        map.put("id", sysUser.getId());
        map.put("username", username);
        //生成JWT
        String jwt = JwtUtils.generateJwt(map);
        R result = R.ok("登录成功", jwt);
        out.write(JSONUtil.toJsonStr(result).getBytes("UTF-8"));
        out.flush();
        out.close();
    }
}