package com.situ.jurisdiction;

import com.situ.jurisdiction.service.SysRoleService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.List;
import java.util.Map;

@SpringBootTest
class JurisdictionApplicationTests {

    @Autowired
    private SysRoleService sysRoleService;

    @Test
    void contextLoads() {
        List<Map<String, Integer>> roleMenuCount = sysRoleService.getRoleMenuCount();
        roleMenuCount.stream().forEach(System.out::println);
    }

}
