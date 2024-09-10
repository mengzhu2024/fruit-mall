package com.graduation.flower;

public class FlowerModel {

    //id
    private Integer id;

    //鲜花名称
    private String name;

    //鲜花类型
    private Integer typeId;

    //鲜花类型
    private FlowerTypeModel type;

    //鲜花图片
    private String imgUrl;

    //状态
    private String status;

    //库存量
    private Integer storeNum;

    //售价
    private String price;

    //描述
    private String description;

    //创建时间
    private String createTime;

    //是否收藏
    private boolean ifCollect;

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

    public FlowerTypeModel getType() {
        return type;
    }

    public void setType(FlowerTypeModel type) {
        this.type = type;
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

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public boolean isIfCollect() {
        return ifCollect;
    }

    public void setIfCollect(boolean ifCollect) {
        this.ifCollect = ifCollect;
    }
}
