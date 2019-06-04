package com.geo.rcs.modules.rule.scene.entity;

import com.geo.rcs.common.validator.group.AddGroup;
import com.geo.rcs.common.validator.group.UpdateGroup;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;

import java.util.Date;

/**
 * @Project : rcs
 * @Package Name : com.geo.rcs.modules.rule.scene.entity
 * @Description : TODD
 * @Author guoyujie
 * @email guoyujie@geotmt.com
 * @Creation Date : 2018年01月19日 下午8:43
 */
public class BusinessType {

    private Long id;

    /**
     * 业务类型名称
     */
    @NotBlank(message = "业务类型名称不能为空", groups = {AddGroup.class, UpdateGroup.class})
    @Length(max = 50, message = "业务类型名称长度不能超过50位",groups = {AddGroup.class, UpdateGroup.class})
    private String typeName;
    /**
     * 描述
     */
    private String describ;
    /**
     * 添加人
     */
    private String addUser;
    /**
     * 添加时间
     */
    private Date addTime;

    /**
     * 关联客户编号
     */
    private Long uniqueCode;

    private Integer pageSize;

    private Integer pageNo;

    public BusinessType(){}
    public BusinessType(Long id, String typeName, String describ,  String addUser, Date addTime, Long uniqueCode, Integer pageSize, Integer pageNo) {
        this.id = id;
        this.typeName = typeName;
        this.describ = describ;
        this.addUser = addUser;
        this.addTime = addTime;
        this.uniqueCode = uniqueCode;
        this.pageSize = pageSize;
        this.pageNo = pageNo;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

    public String getDescrib() {
        return describ;
    }

    public void setDescrib(String describ) {
        this.describ = describ;
    }

    public String getAddUser() {
        return addUser;
    }

    public void setAddUser(String addUser) {
        this.addUser = addUser;
    }

    public Date getAddTime() {
        return addTime;
    }

    public void setAddTime(Date addTime) {
        this.addTime = addTime;
    }

    public Long getUniqueCode() {
        return uniqueCode;
    }

    public void setUniqueCode(Long uniqueCode) {
        this.uniqueCode = uniqueCode;
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }

    public Integer getPageNo() {
        return pageNo;
    }

    public void setPageNo(Integer pageNo) {
        this.pageNo = pageNo;
    }
}
