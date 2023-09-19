package com.situ.jurisdiction.controller;

import cn.hutool.captcha.LineCaptcha;
import com.situ.jurisdiction.util.R;
import com.situ.jurisdiction.util.RedisUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.UUID;

@RestController
public class AuthController {
    @Autowired
    RedisUtil redisUtil;

    @GetMapping("/code")
    public R getCode() {
        LineCaptcha lineCaptcha = new LineCaptcha(120, 38, 5, 10);
        String key = UUID.randomUUID().toString().substring(0, 5).replace("-", "");
        redisUtil.set(key, lineCaptcha.getCode(), 120);
        HashMap<String, Object> map = new HashMap<>();
        map.put("key", key);
        map.put("codeSrc", lineCaptcha.getImageBase64Data());
        return R.ok(map);
    }
}
