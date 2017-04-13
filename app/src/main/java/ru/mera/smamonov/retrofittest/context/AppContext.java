package ru.mera.smamonov.retrofittest.context;

import android.app.Application;
import android.content.Context;

import ru.mera.smamonov.retrofittest.controller.IotManager;

/**
 * Created by sergeym on 13.04.2017.
 */

public class AppContext extends Application {

    private static Context m_context;
    private static IotManager m_iotmanager;

    public void onCreate() {
        super.onCreate();
        AppContext.m_context = getApplicationContext();
        AppContext.m_iotmanager = new IotManager();
    }

    public static Context getAppContext() {
        return AppContext.m_context;
    }

    public static IotManager getIotManager() {
        return AppContext.m_iotmanager;
    }

}
