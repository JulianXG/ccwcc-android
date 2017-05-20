package com.kalyter.ccwcc.model;

import java.util.Date;

public class Flag {
    private Integer id;

    private Integer userId;

    private Integer birdId;

    private String code;

    private String flagColorCombination;

    private String lu;

    private String ru;

    private String ld;

    private String rd;

    private Date discoveredTime;

    private String note;

    private Boolean isDeleted;

    private Date createTime;

    private Date updateTime;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Integer getBirdId() {
        return birdId;
    }

    public void setBirdId(Integer birdId) {
        this.birdId = birdId;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code == null ? null : code.trim();
    }

    public String getFlagColorCombination() {
        return flagColorCombination;
    }

    public void setFlagColorCombination(String flagColorCombination) {
        this.flagColorCombination = flagColorCombination == null ? null : flagColorCombination.trim();
    }

    public String getLu() {
        return lu;
    }

    public void setLu(String lu) {
        this.lu = lu == null ? null : lu.trim();
    }

    public String getRu() {
        return ru;
    }

    public void setRu(String ru) {
        this.ru = ru == null ? null : ru.trim();
    }

    public String getLd() {
        return ld;
    }

    public void setLd(String ld) {
        this.ld = ld == null ? null : ld.trim();
    }

    public String getRd() {
        return rd;
    }

    public void setRd(String rd) {
        this.rd = rd == null ? null : rd.trim();
    }

    public Date getDiscoveredTime() {
        return discoveredTime;
    }

    public void setDiscoveredTime(Date discoveredTime) {
        this.discoveredTime = discoveredTime;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note == null ? null : note.trim();
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