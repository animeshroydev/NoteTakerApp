package com.animesh.notes.notetaker.persistance;

import android.arch.lifecycle.LiveData;
import android.content.Context;

import com.animesh.notes.notetaker.async.DeleteAsyncTask;
import com.animesh.notes.notetaker.async.UpdateAsyncTask;
import com.animesh.notes.notetaker.async.insertAsyncTask;
import com.animesh.notes.notetaker.models.Note;

import java.util.List;

public class NoteRepository {

    private NoteDatabase mNoteDatabase;

    public NoteRepository(Context context) {
        mNoteDatabase = NoteDatabase.getInstance(context);
    }

    public void insertNoteTask(Note note) {
      new insertAsyncTask(mNoteDatabase.getNoteDao()).execute(note);
    }

    public void updateNote(Note note) {
      new UpdateAsyncTask(mNoteDatabase.getNoteDao()).execute(note);
    }

    public LiveData<List<Note>> retrieveNotesTask() {

        return mNoteDatabase.getNoteDao().getNotes();
    }

    public void deleteNote(Note note) {
      new DeleteAsyncTask(mNoteDatabase.getNoteDao()).execute(note);
    }

}
