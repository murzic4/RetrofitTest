package ru.mera.smamonov.retrofittest.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

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

    /*
    public interface DeviceListener {
        void onDelete();

        void onUpdate();
    }


    public void addListener(Object observer,
                            DeviceListener listener) {
        if (listener != null && observer != null) {
            Log.e("GenericDevice", "addListener " +
                    observer +
                    " UUID:" +
                    getUuid());
            m_listener = listener;
        } else {
            throw new NullPointerException();
        }
    }

    */
    /*
    public void addListener(Object observer,
                            DeviceListener listener) {
        if (listener != null && observer != null) {
            Log.e("GenericDevice", "addListener " +
                    observer +
                    " UUID:" +
                    getUuid());
            m_listeners.put(observer, listener);
        } else {
            throw new NullPointerException();
        }
    }

    public void Update() {
        if (m_listeners != null) {
            for (Hashtable.Entry entity : m_listeners.entrySet()) {
                DeviceListener listener = (DeviceListener) entity.getValue();

                if (listener != null) {
                    listener.onUpdate();
                }
            }
        }
    }

    public void Delete() {
        if (m_listeners != null) {
            for (Hashtable.Entry entity : m_listeners.entrySet()) {
                DeviceListener listener = (DeviceListener) entity.getValue();

                if (listener != null) {
                    listener.onDelete();
                }
            }
        }
    }

    transient private Hashtable<Object, DeviceListener> m_listeners = new Hashtable<>();
    */
/*
    public void Update() {
        if (m_listener != null) {
            m_listener.onUpdate();
        }
    }

    public void Delete() {
        if (m_listener != null) {
            m_listener.onDelete();
        }
    }

    transient private DeviceListener m_listener = null;
    */
}
