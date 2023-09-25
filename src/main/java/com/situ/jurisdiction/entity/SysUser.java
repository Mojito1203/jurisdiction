package com.situ.jurisdiction.entity;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

/**
 * @TableName sys_user
 */
@Data
public class SysUser implements Serializable {
    @JsonFormat(shape =JsonFormat.Shape.STRING )
    private Long id;

    private String username;

    private String password;

    private String avatar;

    private String email;

    private String city;

    @JsonFormat(pattern="yyyy-MM-dd", timezone="GMT+8")
    @TableField(fill = FieldFill.INSERT)
    private Date created;

    @JsonFormat(pattern="yyyy-MM-dd", timezone="GMT+8")
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Date updated;

    @JsonFormat(pattern="yyyy-MM-dd", timezone="GMT+8")
    private Date lastLogin;

    private Integer statu;

    @TableField(exist = false)
    private List<SysRole> roles;

    private static final long serialVersionUID = 1L;
}