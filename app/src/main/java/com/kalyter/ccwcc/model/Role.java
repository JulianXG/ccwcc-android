package com.kalyter.ccwcc.model;

/**
 * Created by Kalyter on 2017-5-14 0014.
 */

public enum Role {
    USER(1, "ROLE_ROOT", "超级管理员"),
    ADMIN(2, "ROLE_ADMIN", "管理员"),
    ROOT(3, "ROLE_USER", "普通用户");

    private int id;
    private String name;
    private String description;

    Role(int id, String name, String description) {
        this.id = id;
        this.name = name;
        this.description = description;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public static Role getRoleById(Integer id) {
        for (Role role : Role.values()) {
            if (role.id == id) {
                return role;
            }
        }
        return null;
    }
}
