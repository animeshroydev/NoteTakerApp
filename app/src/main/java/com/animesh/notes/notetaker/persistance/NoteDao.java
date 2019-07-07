package com.animesh.notes.notetaker.persistance;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.animesh.notes.notetaker.models.Note;

import java.util.List;

@Dao
public interface NoteDao {

    @Insert
    long[] insertNote(Note... notes);

    @Query("SELECT * FROM notes")
    LiveData<List<Note>> getNotes();

    @Query("SELECT * FROM notes WHERE title LIKE :title")
    List<Note> getNoteWithCustomQuery(String title);

    @Delete
    int delete(Note... notes);

    @Update
    int update(Note... notes);

}
