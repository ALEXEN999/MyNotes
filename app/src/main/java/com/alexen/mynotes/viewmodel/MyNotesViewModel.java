package com.alexen.mynotes.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

import com.alexen.mynotes.model.Note;

import io.realm.Realm;
import io.realm.RealmResults;

public class MyNotesViewModel extends AndroidViewModel {

    private Realm realm;
    public boolean isUserEdit;
    public int userToEditId;

    public MyNotesViewModel(@NonNull Application application) {
        super(application);
        realm = Realm.getDefaultInstance();
    }


    public RealmResults<Note> obtenerNotasDetalle(){
        return realm.where(Note.class).findAll();
    }

    public Note obtenerNotasDetallePorId(int id){
        return realm.where(Note.class).equalTo("id", id).findAll().first();
    }

    public RealmResults<Note> obtenerNotasDetallePorNombre(String filtro){
        return realm.where(Note.class).contains("title", filtro).findAll();
    }



    public void insertarNota(final Note note){
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                Number maxId = obtenerNotasDetalle().max("id");
                int nextId = (maxId == null) ? 1 : maxId.intValue() + 1;

                Note noteTmp = realm.createObject(Note.class, nextId);

                noteTmp.setTitle(note.getTitle());
                noteTmp.setContent(note.getContent());

                realm.insertOrUpdate(noteTmp);
            }
        });
    }

    public void actualizarNota(final int id, final Note note){
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                Note editarNota = obtenerNotasDetallePorId(id);
                editarNota.setTitle(note.getTitle());
                editarNota.setContent(note.getContent());
            }
        });
    }

    public void eliminarNota(final int id){
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                RealmResults<Note> result = realm.where(Note.class).equalTo("id",id).findAll();
                result.deleteAllFromRealm();

            }
        });
    }

    public void editarNota(int userToEditId) {
        isUserEdit = true;
        this.userToEditId = userToEditId;
    }
}
