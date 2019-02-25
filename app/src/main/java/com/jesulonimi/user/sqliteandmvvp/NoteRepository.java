package com.jesulonimi.user.sqliteandmvvp;

import android.app.Application;
import android.arch.lifecycle.LiveData;
import android.os.AsyncTask;

import java.util.List;

public class NoteRepository {
    private NoteDao noteDao;
    private LiveData<List<Note>> allNotes;

    public NoteRepository(Application application){
        NoteDatabase noteDatabase=NoteDatabase.getInstance(application);
        noteDao=noteDatabase.noteDao();
        allNotes=noteDao.getAllNotes();
    }
    public void insert(Note note){
        new InsertNoteAsyncTask(noteDao).execute(note);
    }
    public void delete(Note note){
        new DeleteNoteAsyncTask(noteDao).execute(note);
    }
    public void update(Note note){
        new UpdateNoteAsyncTask(noteDao).execute(note);
    }
    public void deleteAllNotes(){
        new DeleteAllNotesAsyncTask(noteDao).execute();
    }
    public LiveData<List<Note>> getAllNotes(){
        return  allNotes;
    }

    private class InsertNoteAsyncTask extends AsyncTask<Note,Void,Void>{
        NoteDao noteDao;
    public InsertNoteAsyncTask(NoteDao noteDao){
    this.noteDao=noteDao;
    }
        @Override
        protected Void doInBackground(Note... notes) {
        noteDao.insert(notes[0]);
            return null;
        }
    }
    private class DeleteNoteAsyncTask extends AsyncTask<Note,Void,Void>{
        NoteDao noteDao;
        public DeleteNoteAsyncTask(NoteDao noteDao){
            this.noteDao=noteDao;
        }
        @Override
        protected Void doInBackground(Note... notes) {
            noteDao.delete(notes[0]);
            return null;
        }
    }

    private class UpdateNoteAsyncTask extends AsyncTask<Note,Void,Void>{
        NoteDao noteDao;
        public UpdateNoteAsyncTask(NoteDao noteDao){
            this.noteDao=noteDao;
        }
        @Override
        protected Void doInBackground(Note... notes) {
            noteDao.update(notes[0]);
            return null;
        }
    }

    private class DeleteAllNotesAsyncTask extends AsyncTask<Note,Void,Void>{
        NoteDao noteDao;
        public DeleteAllNotesAsyncTask(NoteDao noteDao){
            this.noteDao=noteDao;
        }
        @Override
        protected Void doInBackground(Note... notes) {
            noteDao.deleteAllNotes();
            return null;
        }
    }
}
