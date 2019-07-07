package com.animesh.notes.notetaker.async;

import android.os.AsyncTask;

import com.animesh.notes.notetaker.models.Note;
import com.animesh.notes.notetaker.persistance.NoteDao;

public class insertAsyncTask extends AsyncTask<Note, Void, Void> {

    private NoteDao mNoteDao;

    public insertAsyncTask(NoteDao dao) {
        mNoteDao = dao;
    }

    @Override
    protected Void doInBackground(Note... notes) {
        mNoteDao.insertNote(notes);
        return null;
    }
}
