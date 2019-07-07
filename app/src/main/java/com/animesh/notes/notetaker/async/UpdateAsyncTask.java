package com.animesh.notes.notetaker.async;

import android.os.AsyncTask;

import com.animesh.notes.notetaker.models.Note;
import com.animesh.notes.notetaker.persistance.NoteDao;

public class UpdateAsyncTask extends AsyncTask<Note, Void, Void> {

    private NoteDao mNoteDao;

    public UpdateAsyncTask(NoteDao dao) {
        mNoteDao = dao;
    }

    @Override
    protected Void doInBackground(Note... notes) {
        mNoteDao.update(notes);
        return null;
    }
}
