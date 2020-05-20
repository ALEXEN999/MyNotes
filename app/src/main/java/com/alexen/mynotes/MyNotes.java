package com.alexen.mynotes;

import android.app.Application;

import com.alexen.mynotes.model.Migration;

import io.realm.Realm;
import io.realm.RealmConfiguration;

public class MyNotes extends Application {
    static Realm realm;
    @Override
    public void onCreate() {
        super.onCreate();
        Realm.init(this);
        RealmConfiguration config = new RealmConfiguration.Builder()
                .name("MyNotes.realm")
                .schemaVersion(1)
                .migration(new Migration())
                .build();
        Realm.setDefaultConfiguration(config);
        realm = Realm.getInstance(config);

    }
}
