package com.animesh.notes.notetaker;

import android.arch.lifecycle.Observer;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.text.Html;
import android.util.Log;
import android.view.View;

import com.animesh.notes.notetaker.adapters.NotesRecyclerViewAdapter;
import com.animesh.notes.notetaker.models.Note;
import com.animesh.notes.notetaker.persistance.NoteRepository;
import com.animesh.notes.notetaker.utils.SpacingItemDecorator;

import java.util.ArrayList;
import java.util.List;

public class NoteListActivity extends AppCompatActivity implements NotesRecyclerViewAdapter.OnNoteListener,
        FloatingActionButton.OnClickListener {

    private static final String TAG = "NoteListActivity";

    private RecyclerView mRecyclerView;
    private ArrayList<Note> mNotes = new ArrayList<>();
    private NotesRecyclerViewAdapter mNotesRecyclerViewAdapter;

    private NoteRepository mNoteRepository;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note_list);

        findViewById(R.id.fab).setOnClickListener(this);
        mNoteRepository = new NoteRepository(this);

        mRecyclerView = findViewById(R.id.recyclerView);
        initRecyclerView();
        retrieveNote();

        setSupportActionBar((Toolbar) findViewById(R.id.notes_toolbar));

        setTitle(Html.fromHtml("<font color='#ffffff'>Note Taker</font>")); // set the title in toolbar

    }

    private void retrieveNote() {
        mNoteRepository.retrieveNotesTask().observe(this, new Observer<List<Note>>() {
            @Override
            public void onChanged(@Nullable List<Note> notes) {
                if (mNotes.size() > 0) {
                    mNotes.clear();
                }
                if (notes != null) {
                    mNotes.addAll(notes);
                }
                mNotesRecyclerViewAdapter.notifyDataSetChanged();
            }
        });
    }

    private void insertFakeData() {
        for (int i = 0; i<500; i++) {
            Note note = new Note();
            note.setTitle("title $ "+ i);
            note.setContent("content $" + i);
            note.setTimestamp("Jan 2019");
            mNotes.add(note);
        }
        mNotesRecyclerViewAdapter.notifyDataSetChanged();
    }

    private void initRecyclerView() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(linearLayoutManager);
        SpacingItemDecorator itemDecorator = new SpacingItemDecorator(10);
        mRecyclerView.addItemDecoration(itemDecorator);
        new ItemTouchHelper(itemTouchHelperCallback).attachToRecyclerView(mRecyclerView);
        mNotesRecyclerViewAdapter = new NotesRecyclerViewAdapter(mNotes, this);
        mRecyclerView.setAdapter(mNotesRecyclerViewAdapter);
    }


    @Override
    public void onNoteClick(int position) {
        Log.d(TAG, "onNoteClick: clicked " + position);

        Intent intent = new Intent(this, NoteActivity.class);
        intent.putExtra("selected_note", mNotes.get(position));
        startActivity(intent);
    }

    @Override
    public void onClick(View v) {
       Intent intent = new Intent(this, NoteActivity.class);
       startActivity(intent);
    }

    private void deleteNote(Note note) {
        mNotes.remove(note);
        mNotesRecyclerViewAdapter.notifyDataSetChanged();
        mNoteRepository.deleteNote(note);
    }

    private ItemTouchHelper.SimpleCallback itemTouchHelperCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.RIGHT) {
        @Override
        public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder viewHolder1) {
            return false;
        }

        @Override
        public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
            deleteNote(mNotes.get(viewHolder.getAdapterPosition()));
        }
    };
}
