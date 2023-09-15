package com.situ.jurisdiction.entity;

import java.io.Serializable;
import java.util.Date;
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

    private Integer ordernum;

    private Date created;

    private Date updated;

    private Integer statu;

    private static final long serialVersionUID = 1L;
}