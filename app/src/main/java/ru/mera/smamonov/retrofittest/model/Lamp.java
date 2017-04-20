package ru.mera.smamonov.retrofittest.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

public class Lamp extends GenericDevice {
    @SerializedName("switched")
    @Expose
    private Boolean switched;

    public Boolean getSwitched() {
        return switched;
    }

    public void setSwitched(Boolean switched) {
        this.switched = switched;
    }

    transient static int index = 0;
    transient static List<Lamp> m_list = new LinkedList();

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

    public static Lamp generate() {
        Lamp result = new Lamp();

        result.setName("Lamp#" + Integer.toString(index));
        result.setUuid(UUID.randomUUID().toString());
        result.setSwitched(index % 2 == 0 ?
                new Boolean(true) :
                new Boolean(false));

        index++;
        m_list.add(result);
        return result;
    }

    public static List<Lamp> getList() {
        return m_list;
    }
}