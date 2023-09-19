package com.situ.jurisdiction.entity;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;

/**
 * @TableName sys_menu
 */
@Data
public class SysMenu implements Serializable {
    private Long id;

    private Long parentId;

    private String name;

    private String path;

    private String perms;

    private String component;

    private Integer type;

    private String icon;

    private Integer orderNum;

    private Date created;

    private Date updated;

    private Integer status;

    @TableField(exist = false)
    private List<SysMenu> children;

    private static final long serialVersionUID = 1L;
}