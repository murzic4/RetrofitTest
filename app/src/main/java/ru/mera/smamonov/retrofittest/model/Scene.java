package ru.mera.smamonov.retrofittest.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import java.util.UUID;

/**
 * Created by sergeym on 07.04.2017.
 */

public class Scene extends GenericDevice {
    @SerializedName("devices")
    @Expose
    private List<Lamp> devices;

    @SerializedName("activated")
    @Expose
    private boolean activated;

    public List<Lamp> getDevices() {
        return devices;
    }

    public void setDevices(List<Lamp> devices) {
        this.devices = devices;
    }

    public boolean getActivated() {
        return activated;
    }

    public void setActivated(boolean activated) {
        this.activated = activated;
    }

    static int index = 0;

    public Scene() {
        this.devices = new LinkedList<>();
    }

    public static Scene generate() {
        Scene result = new Scene();

        result.setName("Scene#" + Integer.toString(index));
        result.setUuid(UUID.randomUUID().toString());
        result.setActivated(index % 2 == 0 ?
                new Boolean(true) :
                new Boolean(false));

        result.devices = new LinkedList<>();
        Random random = new Random();

        for (int device_index = 0;
             device_index < random.nextInt(5);
             device_index++) {
            result.devices.add(Lamp.generate());
        }

        index++;
        return result;
    }
}
