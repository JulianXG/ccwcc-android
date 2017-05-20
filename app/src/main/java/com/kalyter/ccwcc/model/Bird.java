package com.kalyter.ccwcc.model;

import java.util.Date;

public class Bird {
    private Integer id;

    private String code;

    private String nameZh;

    private String nameEn;

    private String nameLt;

    private Integer protectClass;

    private String nation;

    private String order;

    private String family;

    private Integer birdCount;

    private String genus;

    private String category;

    private Integer onePercentStandard;

    private String threatened;

    private Boolean isWaterBird;

    private Boolean isWetlandDependBird;

    private String zhPinyin;

    private Boolean isAlive;

    private Boolean isDeleted;

    private Date createTime;

    private Date updateTime;

    private Date recordTime;

    public Date getRecordTime() {
        return recordTime;
    }

    public void setRecordTime(Date recordTime) {
        this.recordTime = recordTime;
    }

    public Integer getBirdCount() {
        return birdCount;
    }

    public void setBirdCount(Integer birdCount) {
        this.birdCount = birdCount;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code == null ? null : code.trim();
    }

    public String getNameZh() {
        return nameZh;
    }

    public void setNameZh(String nameZh) {
        this.nameZh = nameZh == null ? null : nameZh.trim();
    }

    public String getNameEn() {
        return nameEn;
    }

    public void setNameEn(String nameEn) {
        this.nameEn = nameEn == null ? null : nameEn.trim();
    }

    public String getNameLt() {
        return nameLt;
    }

    public void setNameLt(String nameLt) {
        this.nameLt = nameLt == null ? null : nameLt.trim();
    }

    public Integer getProtectClass() {
        return protectClass;
    }

    public void setProtectClass(Integer protectClass) {
        this.protectClass = protectClass;
    }

    public String getNation() {
        return nation;
    }

    public void setNation(String nation) {
        this.nation = nation == null ? null : nation.trim();
    }

    public String getOrder() {
        return order;
    }

    public void setOrder(String order) {
        this.order = order == null ? null : order.trim();
    }

    public String getFamily() {
        return family;
    }

    public void setFamily(String family) {
        this.family = family == null ? null : family.trim();
    }

    public String getGenus() {
        return genus;
    }

    public void setGenus(String genus) {
        this.genus = genus == null ? null : genus.trim();
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category == null ? null : category.trim();
    }

    public Integer getOnePercentStandard() {
        return onePercentStandard;
    }

    public void setOnePercentStandard(Integer onePercentStandard) {
        this.onePercentStandard = onePercentStandard;
    }

    public String getThreatened() {
        return threatened;
    }

    public void setThreatened(String threatened) {
        this.threatened = threatened == null ? null : threatened.trim();
    }

    public Boolean getIsWaterBird() {
        return isWaterBird;
    }

    public void setIsWaterBird(Boolean isWaterBird) {
        this.isWaterBird = isWaterBird;
    }

    public Boolean getIsWetlandDependBird() {
        return isWetlandDependBird;
    }

    public void setIsWetlandDependBird(Boolean isWetlandDependBird) {
        this.isWetlandDependBird = isWetlandDependBird;
    }

    public String getZhPinyin() {
        return zhPinyin;
    }

    public void setZhPinyin(String zhPinyin) {
        this.zhPinyin = zhPinyin == null ? null : zhPinyin.trim();
    }

    public Boolean getIsAlive() {
        return isAlive;
    }

    public void setIsAlive(Boolean isAlive) {
        this.isAlive = isAlive;
    }

    public Boolean getIsDeleted() {
        return isDeleted;
    }

    public void setIsDeleted(Boolean isDeleted) {
        this.isDeleted = isDeleted;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }
}