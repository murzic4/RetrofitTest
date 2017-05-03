package ru.mera.smamonov.retrofittest.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

public class Lamp extends GenericDevice {
    transient static int index = 0;
    transient static List<Lamp> m_list = new LinkedList();
    @SerializedName("switched")
    @Expose
    private Boolean switched;
    @SerializedName("dimming")
    @Expose
    private Integer dimming;

    public static Lamp generate() {
        Lamp result = new Lamp();

        result.setName("Lamp#" + Integer.toString(index));
        result.setUuid(UUID.randomUUID().toString());
        result.setSwitched(index % 2 == 0 ?
                Boolean.valueOf(true) :
                Boolean.valueOf(false));

        index++;
        m_list.add(result);
        return result;
    }

    public static List<Lamp> getList() {
        return m_list;
    }

    public Boolean getSwitched() {
        return switched;
    }

    public void setSwitched(Boolean switched) {
        this.switched = switched;
    }

    public Integer getDimming() {
        return dimming;
    }

    public void setDimming(Integer dimming) {
        this.dimming = dimming;
    }

    @Override
    public boolean equals(Object obj) {
        boolean result = false;

        if (obj instanceof Lamp) {
            Lamp lamp = (Lamp) obj;

            if (lamp != null &&
                    lamp.getUuid().equals(getUuid())) {
                result = true;
            }
        }

        return result;
    }
}