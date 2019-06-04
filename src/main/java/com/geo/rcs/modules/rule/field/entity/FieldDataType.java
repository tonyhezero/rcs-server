package com.geo.rcs.modules.rule.field.entity;

/**
 * @Project : rcs
 * @Package Name : com.geo.rcs.modules.rule.field.entity
 * @Description : TODD
 * @Author guoyujie
 * @email guoyujie@geotmt.com
 * @Creation Date : 2018年04月03日 上午11:02
 */
public class FieldDataType {
    //类型编号
    private Integer id;
    //类型名称
    private String typeName;
    //父级编号
    private Integer parentId;
    //接口名称
    private String name ;

    private Long uniqueCode;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

    public Integer getParentId() {
        return parentId;
    }

    public void setParentId(Integer parentId) {
        this.parentId = parentId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    public Long getUniqueCode() {
        return uniqueCode;
    }

    public void setUniqueCode(Long uniqueCode) {
        this.uniqueCode = uniqueCode;
    }
}
