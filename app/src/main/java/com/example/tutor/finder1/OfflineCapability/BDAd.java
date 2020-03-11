package com.example.tutor.finder1.OfflineCapability;

import android.app.Application;

import com.google.firebase.database.FirebaseDatabase;

/**
 * Created by Rana on 25-02-18.
 */

public class BDAd extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        FirebaseDatabase.getInstance().setPersistenceEnabled(true);
    }
}
