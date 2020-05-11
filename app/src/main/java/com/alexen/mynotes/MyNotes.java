package com.alexen.mynotes;

import android.app.Application;

import io.realm.Realm;
import io.realm.RealmConfiguration;

public class MyNotes extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        Realm.init(this);
        RealmConfiguration config = new RealmConfiguration.Builder()
                .name("MyNotes.realm")
                .schemaVersion(0)
                .build();
        Realm.setDefaultConfiguration(config);
    }
}
