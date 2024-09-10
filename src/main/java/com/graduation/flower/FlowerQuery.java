package com.graduation.flower;

import java.util.List;

public class FlowerQuery {

    // 查询页码
    private Integer current = 1;

    //id
    private Integer id;

    private List<Integer> idList;

    //鲜花名称
    private String name;

    //鲜花类型
    private Integer typeId;

    //鲜花图片
    private String imgUrl;

    //状态
    private String status;

    //库存量
    private Integer storeNum;

    private boolean onlyCollect;

    public Integer getCurrent() {
        return current;
    }

    public void setCurrent(Integer current) {
        this.current = current;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getTypeId() {
        return typeId;
    }

    public void setTypeId(Integer typeId) {
        this.typeId = typeId;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Integer getStoreNum() {
        return storeNum;
    }

    public void setStoreNum(Integer storeNum) {
        this.storeNum = storeNum;
    }

    public boolean isOnlyCollect() {
        return onlyCollect;
    }

    public void setOnlyCollect(boolean onlyCollect) {
        this.onlyCollect = onlyCollect;
    }

    public List<Integer> getIdList() {
        return idList;
    }

    public void setIdList(List<Integer> idList) {
        this.idList = idList;
    }
}
