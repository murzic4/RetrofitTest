package ru.mera.smamonov.retrofittest.com.tilgin.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Lamp {
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("uuid")
    @Expose
    private Integer uuid;
    @SerializedName("switched")
    @Expose
    private Boolean switched;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getUuid() {
        return uuid;
    }

    public void setUuid(Integer uuid) {
        this.uuid = uuid;
    }

    public Boolean getSwitched() {
        return switched;
    }

    public void setSwitched(Boolean switched) {
        this.switched = switched;
    }

}