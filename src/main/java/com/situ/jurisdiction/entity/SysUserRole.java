package com.situ.jurisdiction.entity;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

/**
 * @TableName sys_user_role
 */
@Data
public class SysUserRole implements Serializable {

    @JsonFormat(shape =JsonFormat.Shape.STRING )
    private Long id;

    private Long userId;

    private Long roleId;

    private static final long serialVersionUID = 1L;
}