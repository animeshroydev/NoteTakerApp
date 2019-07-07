package com.animesh.notes.notetaker;

import android.app.Activity;
import android.os.PersistableBundle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.animesh.notes.notetaker.models.Note;
import com.animesh.notes.notetaker.persistance.NoteRepository;
import com.animesh.notes.notetaker.utils.Utility;

public class NoteActivity extends AppCompatActivity implements View.OnTouchListener, GestureDetector.OnGestureListener,
        GestureDetector.OnDoubleTapListener, View.OnClickListener, TextWatcher {

    private static final String TAG = "NoteActivity";

    private EditText mEditTitle;
    private EditText mEditText;
    private TextView mViewTitle;
    private NoteRepository mNoteRepository;

    private boolean mIsNewNote;
    private Note mInitialNote;
    private GestureDetector mGestureDetector;

    private RelativeLayout mCheckContainer, mBackArrowContainer;
    private ImageButton mCheck, mBackArrow;

    private static final int EDIT_MODE_ENABLED = 1;
    private static final int EDIT_MODE_DISABLED = 0;
    private int mMode;
    private Note mFinalNote;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note);

        mEditText = findViewById(R.id.note_text);
        mEditTitle = findViewById(R.id.note_edit_title);
        mViewTitle = findViewById(R.id.note_text_title);
        mCheckContainer = findViewById(R.id.check_container);
        mBackArrowContainer = findViewById(R.id.back_arrow_container);
        mCheck = findViewById(R.id.toolbar_check);
        mBackArrow = findViewById(R.id.toolbar_back_arrow);
        mNoteRepository = new NoteRepository(this);

        if (getIncomingIntent()) {
            // Edit mode
            setNewNoteProperties();
            enableEditMode();
        }
        else {
            // view mode
            setNoteProperties();
            disableInteraction();
        }

        setListener();

    }

    private void updateNote() {
        mNoteRepository.updateNote(mFinalNote);
    }

    private void enableEditMode() {
        mBackArrowContainer.setVisibility(View.GONE);
        mCheckContainer.setVisibility(View.VISIBLE);

        mViewTitle.setVisibility(View.GONE);
        mEditTitle.setVisibility(View.VISIBLE);

        mMode = EDIT_MODE_ENABLED;
        enableInteraction();
    }

    private void disableEditMode() {
        mBackArrowContainer.setVisibility(View.VISIBLE);
        mCheckContainer.setVisibility(View.GONE);

        mViewTitle.setVisibility(View.VISIBLE);
        mEditTitle.setVisibility(View.GONE);

        mMode = EDIT_MODE_DISABLED;
        disableInteraction();

        String temp = mEditText.getText().toString();
        temp = temp.replace("\n", "");
        temp = temp.replace(" ", "");
        if (temp.length() > 0) {
            mFinalNote.setTitle(mEditTitle.getText().toString());
            mFinalNote.setContent(mEditText.getText().toString());
            String timestamp = Utility.getCurrentTimestamp();
            mFinalNote.setTimestamp(timestamp);

            if (!mFinalNote.getContent().equals(mInitialNote.getContent())
                 || !mFinalNote.getTitle().equals(mInitialNote.getTitle())) {
                saveChanges();
            }

        }
        hideKeyboard();
    }

    private void setListener() {
        mEditText.setOnTouchListener(this);
        mGestureDetector = new GestureDetector(this, this);
        mViewTitle.setOnClickListener(this);
        mCheck.setOnClickListener(this);
        mBackArrow.setOnClickListener(this);
        mEditTitle.addTextChangedListener(this);
    }


    public boolean getIncomingIntent() {
        if (getIntent().hasExtra("selected_note")) {
            mInitialNote = getIntent().getParcelableExtra("selected_note");

            mFinalNote = new Note();
            mFinalNote.setTitle(mInitialNote.getTitle());
            mFinalNote.setContent(mInitialNote.getContent());
            mFinalNote.setTimestamp(mInitialNote.getTimestamp());
            mFinalNote.setId(mInitialNote.getId());
            mMode = EDIT_MODE_DISABLED;
            mIsNewNote = false;
            return false;
        }
        mMode = EDIT_MODE_ENABLED;
        mIsNewNote = true;
        return true;
    }

    private void saveChanges() {
        if (mIsNewNote) {
            saveNewNotes();
        } else {
            updateNote();
        }
    }

    private void saveNewNotes() {
        mNoteRepository.insertNoteTask(mFinalNote);
    }

    private void hideKeyboard() {
        InputMethodManager imm = (InputMethodManager) this.getSystemService(Activity.INPUT_METHOD_SERVICE);
        View view = this.getCurrentFocus();
        if (view == null) {
            view = new View(this);
        }
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    private void setNoteProperties() {
        mViewTitle.setText(mInitialNote.getTitle());
        mEditTitle.setText(mInitialNote.getTitle());
        mEditText.setText(mInitialNote.getContent());
    }

    private void setNewNoteProperties() {
        mViewTitle.setText("Note Title");
        mViewTitle.setText("Note Title");

        mInitialNote = new Note();
        mFinalNote = new Note();
        mInitialNote.setTitle("Note Title");
        mFinalNote.setTitle("Note Title");

    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        return mGestureDetector.onTouchEvent(event);
    }

    @Override
    public boolean onDown(MotionEvent e) {
        return false;
    }

    @Override
    public void onShowPress(MotionEvent e) {

    }

    @Override
    public boolean onSingleTapUp(MotionEvent e) {
        return false;
    }

    @Override
    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
        return false;
    }

    @Override
    public void onLongPress(MotionEvent e) {

    }

    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
        return false;
    }

    @Override
    public boolean onSingleTapConfirmed(MotionEvent e) {
        return false;
    }

    @Override
    public boolean onDoubleTap(MotionEvent e) {
        enableEditMode();
        Log.d(TAG, "onDoubleTap: double tapped");
        return false;
    }

    @Override
    public boolean onDoubleTapEvent(MotionEvent e) {
        return false;
    }

    @Override
    public void onClick(View view) {
      switch(view.getId()) {
          case R.id.toolbar_check: {
              disableEditMode();
              break;
          }
          case R.id.note_text_title: {
              enableEditMode();
              mEditTitle.requestFocus();
              mEditTitle.setSelection(mEditTitle.length());
              break;
          }
          case R.id.toolbar_back_arrow: {
              finish();
              break;
          }
      }
    }

    @Override
    public void onBackPressed() {
        if (mMode == EDIT_MODE_ENABLED) {
            onClick(mCheck);
        } else {
            super.onBackPressed();
        }
    }

    private void disableInteraction() {
        mEditText.setKeyListener(null);
        mEditText.setFocusable(false);
        mEditText.setFocusableInTouchMode(false);
        mEditText.setCursorVisible(false);
        mEditText.clearFocus();
    }

    private void enableInteraction() {
        mEditText.setKeyListener(new EditText(this).getKeyListener());
        mEditText.setFocusable(true);
        mEditText.setFocusableInTouchMode(true);
        mEditText.setCursorVisible(true);
        mEditText.requestFocus();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt("mode", mMode);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        mMode = savedInstanceState.getInt("mode");
        if (mMode == EDIT_MODE_ENABLED) {
            enableEditMode();
        }
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
         mViewTitle.setText(s.toString());
    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {

    }
}
