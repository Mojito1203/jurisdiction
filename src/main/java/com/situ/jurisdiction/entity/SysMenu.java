package com.situ.jurisdiction.entity;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

/**
 * @TableName sys_menu
 */
@Data
public class SysMenu implements Serializable {
    @JsonFormat(shape =JsonFormat.Shape.STRING)
    @TableId(value = "id",type = IdType.AUTO)
    private Long id;

    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long parentId;

    private String name;

    private String path;

    private String perms;

    private String component;

    private Integer type;

    private String icon;

    private Integer orderNum;

    @JsonFormat(pattern="yyyy-MM-dd", timezone="GMT+8")
    @TableField(fill = FieldFill.INSERT)
    private Date created;

    @JsonFormat(pattern="yyyy-MM-dd", timezone="GMT+8")
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Date updated;

    private Integer status;

    @TableField(exist = false)
    private List<SysMenu> children;

    private static final long serialVersionUID = 1L;
}