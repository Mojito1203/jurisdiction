package com.situ.jurisdiction;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@SpringBootTest
class JurisdictionApplicationTests {

    @Test
    void contextLoads() {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        String pwd = encoder.encode("123456");

        //System.out.println(encoder.matches("123456", pwd));
        //$2a$10$3.alrlNJ/KkeWSdpP0IbLOJawTFjbFeKIOuJcgojzhhavh3bBhwuu
        //$2a$10$WhG11eV0NJu6kwo19E104.1p0Jc4i0PKHwgOArbDoui/ezBub0Y62
        //$2a$10$Wuq9DLRnH3n/w20upZ.GnepLn.HopxaJdzUFQZhLQu7sDWM28PT/6
        System.out.println(pwd);
    }

}
