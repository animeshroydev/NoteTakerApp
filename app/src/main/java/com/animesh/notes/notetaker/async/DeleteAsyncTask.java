package com.animesh.notes.notetaker.async;

import android.os.AsyncTask;

import com.animesh.notes.notetaker.models.Note;
import com.animesh.notes.notetaker.persistance.NoteDao;

public class DeleteAsyncTask extends AsyncTask<Note, Void, Void> {

    private NoteDao mNoteDao;

    public DeleteAsyncTask(NoteDao dao) {
        mNoteDao = dao;
    }

    @Override
    protected Void doInBackground(Note... notes) {
        mNoteDao.delete(notes);
        return null;
    }
}
