package com.situ.jurisdiction.entity;

import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * @TableName sys_role
 */
@Data
public class SysRole implements Serializable {
    private Long id;

    private String name;

    private String code;

    private String remark;

    private Date created;

    private Date updated;

    private Integer statu;

    private static final long serialVersionUID = 1L;
}