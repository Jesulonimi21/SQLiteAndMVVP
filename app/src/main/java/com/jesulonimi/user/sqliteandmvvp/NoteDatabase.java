package com.jesulonimi.user.sqliteandmvvp;

import android.arch.persistence.db.SupportSQLiteDatabase;
import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;
import android.os.AsyncTask;
import android.support.annotation.NonNull;

@Database(entities = Note.class,version = 1)
public abstract class NoteDatabase extends RoomDatabase {
    public static NoteDatabase instance;

    public abstract NoteDao noteDao();

    public static synchronized NoteDatabase getInstance(Context context){
        if(instance==null){

            instance= Room.databaseBuilder(context.getApplicationContext(),
                    NoteDatabase.class,"note_database").addCallback(callback)
                    .fallbackToDestructiveMigration().build();
        }
        return instance;
    }

    public static RoomDatabase.Callback callback=new RoomDatabase.Callback(){
        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase db) {
            super.onCreate(db);
            new populateDbAsyncTask(instance).execute();
        }
    };
    public static class populateDbAsyncTask  extends AsyncTask<Void,Void,Void>{
        NoteDao noteDao;
        public populateDbAsyncTask(NoteDatabase noteDatabase) {
            noteDao=noteDatabase.noteDao();

        }        @Override
        protected Void doInBackground(Void... voids) {
            noteDao.insert(new Note("title","Description",2));
            noteDao.insert(new Note("title1","Description1",3));
            noteDao.insert(new Note("title2","Description2",4));
            return null;
        }
    }
}
