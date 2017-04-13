package ru.mera.smamonov.retrofittest.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.UUID;

public class Lamp extends GenericDevice {
    /*
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("uuid")
    @Expose
    private String uuid;
    */
    @SerializedName("switched")
    @Expose
    private Boolean switched;

    /*
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
    */
    public Boolean getSwitched() {
        return switched;
    }

    public void setSwitched(Boolean switched) {
        this.switched = switched;
    }

    static int index = 0;

    public static Lamp generate() {
        Lamp result = new Lamp();

        result.setName("Lamp#" + Integer.toString(index));
        result.setUuid(UUID.randomUUID().toString());
        result.setSwitched(index % 2 == 0 ?
                new Boolean(true) :
                new Boolean(false));

        index++;
        return result;
    }
}