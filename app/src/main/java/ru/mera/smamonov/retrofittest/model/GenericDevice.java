package ru.mera.smamonov.retrofittest.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by sergeym on 13.04.2017.
 */

public abstract class GenericDevice {
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("uuid")
    @Expose
    private String uuid;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public interface DeviceListener {
        void onDelete();

        void onUpdate();
    }

    public void addListener(DeviceListener listener) {
        m_listeners.add(listener);
    }

    public void Update() {
        if (m_listeners != null) {
            for (DeviceListener listener : m_listeners) {
                listener.onUpdate();
            }
        }
    }

    public void Delete() {
        if (m_listeners != null) {
            for (DeviceListener listener : m_listeners) {
                listener.onDelete();
            }
        }
    }

    transient private List<DeviceListener> m_listeners = new LinkedList();
}
