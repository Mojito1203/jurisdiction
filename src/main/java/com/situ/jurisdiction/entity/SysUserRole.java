package com.situ.jurisdiction.entity;

import java.io.Serializable;
import lombok.Data;

/**
 * @TableName sys_user_role
 */
@Data
public class SysUserRole implements Serializable {
    private Long id;

    private Long userId;

    private Long roleId;

    private static final long serialVersionUID = 1L;
}