package com.situ.jurisdiction.entity;

import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * @TableName sys_user
 */
@Data
public class SysUser implements Serializable {
    private Long id;

    private String username;

    private String password;

    private String avatar;

    private String email;

    private String city;

    private Date created;

    private Date updated;

    private Date lastLogin;

    private Integer statu;

    private static final long serialVersionUID = 1L;
}