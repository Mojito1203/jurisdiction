package com.situ.jurisdiction.entity;

import java.io.Serializable;
import java.util.Date;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

/**
 * @TableName sys_role
 */
@Data
public class SysRole implements Serializable {
    @JsonFormat(shape =JsonFormat.Shape.STRING )
    private Long id;

    private String name;

    private String code;

    private String remark;

    @JsonFormat(pattern="yyyy-MM-dd", timezone="GMT+8")
    @TableField(fill = FieldFill.INSERT)
    private Date created;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    @JsonFormat(pattern="yyyy-MM-dd", timezone="GMT+8")
    private Date updated;

    private Integer statu;

    private static final long serialVersionUID = 1L;
}