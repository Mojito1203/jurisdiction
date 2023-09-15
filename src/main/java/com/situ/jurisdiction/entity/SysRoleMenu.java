package com.situ.jurisdiction.entity;

import java.io.Serializable;
import lombok.Data;

/**
 * @TableName sys_role_menu
 */
@Data
public class SysRoleMenu implements Serializable {
    private Long id;

    private Long roleId;

    private Long menuId;

    private static final long serialVersionUID = 1L;
}