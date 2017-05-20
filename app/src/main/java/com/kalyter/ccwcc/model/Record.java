package com.kalyter.ccwcc.model;

import java.util.Date;
import java.util.List;

public class Record {
    private List<BirdRecord> birds;

    private Integer id;

    private Integer birdId;

    private Integer userId;

    private Integer birdCount;

    private Integer checkpointId;

    private String detail;

    private String weather;

    private Integer visitTimes;

    private Date recordTime;

    private Double latitude;

    private Double longitude;

    private Boolean isDeleted;

    private Date createTime;

    private Date updateTime;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getBirdId() {
        return birdId;
    }

    public void setBirdId(Integer birdId) {
        this.birdId = birdId;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Integer getBirdCount() {
        return birdCount;
    }

    public void setBirdCount(Integer birdCount) {
        this.birdCount = birdCount;
    }

    public Integer getCheckpointId() {
        return checkpointId;
    }

    public void setCheckpointId(Integer checkpointId) {
        this.checkpointId = checkpointId;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail == null ? null : detail.trim();
    }

    public String getWeather() {
        return weather;
    }

    public void setWeather(String weather) {
        this.weather = weather == null ? null : weather.trim();
    }

    public Integer getVisitTimes() {
        return visitTimes;
    }

    public void setVisitTimes(Integer visitTimes) {
        this.visitTimes = visitTimes;
    }

    public Date getRecordTime() {
        return recordTime;
    }

    public void setRecordTime(Date recordTime) {
        this.recordTime = recordTime;
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

    public List<BirdRecord> getBirds() {
        return birds;
    }

    public void setBirds(List<BirdRecord> birds) {
        this.birds = birds;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }
}