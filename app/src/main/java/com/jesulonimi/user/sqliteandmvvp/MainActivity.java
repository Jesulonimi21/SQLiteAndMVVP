package com.jesulonimi.user.sqliteandmvvp;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import java.util.List;

import static com.jesulonimi.user.sqliteandmvvp.AddEditNoteActivity.EXTRA_DESC;
import static com.jesulonimi.user.sqliteandmvvp.AddEditNoteActivity.EXTRA_PRIORITY;
import static com.jesulonimi.user.sqliteandmvvp.AddEditNoteActivity.EXTRA_TITLE;

public class MainActivity extends AppCompatActivity {
NoteViewModel noteViewModel;
public static final int ADDNOTEREQUEST=1;
    public static final int UPDATENOTEREQUEST=2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        FloatingActionButton buttonAddNote=findViewById(R.id.button_add);
        noteViewModel=ViewModelProviders.of(this).get(NoteViewModel.class);
        RecyclerView recyclerView=findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);

        final NoteAdapter noteAdapter=new NoteAdapter();
        recyclerView.setAdapter(noteAdapter);

        noteViewModel.getAllNotes().observe(this, new Observer<List<Note>>() {
            @Override
            public void onChanged(@Nullable List<Note> notes) {
              noteAdapter.submitList(notes);
            }
        });

        buttonAddNote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(MainActivity.this,AddEditNoteActivity.class);
                startActivityForResult(intent,ADDNOTEREQUEST);
            }
        });

        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0,ItemTouchHelper.LEFT|ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
                noteViewModel.delete(noteAdapter.getNoteAt(viewHolder.getAdapterPosition()));
                Toast.makeText(MainActivity.this, "Item deleted ", Toast.LENGTH_SHORT).show();
            }
        }).attachToRecyclerView(recyclerView);
        noteAdapter.onRecyclerViewItemClickListener(new NoteAdapter.RecyclerViewItemClick() {
            @Override
            public void onRecyclerviewItemClick(Note note) {
                Intent i=new Intent(MainActivity.this,AddEditNoteActivity.class);
                i.putExtra(AddEditNoteActivity.EXTRA_TITLE,note.getTitle());
                i.putExtra(AddEditNoteActivity.EXTRA_PRIORITY,note.getPriority());
                i.putExtra(AddEditNoteActivity.EXTRA_DESC,note.getDescription());
                i.putExtra(AddEditNoteActivity.EXTRA_ID,note.getId());
                startActivityForResult(i,UPDATENOTEREQUEST);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==ADDNOTEREQUEST&&resultCode==RESULT_OK){
            String title=data.getStringExtra(EXTRA_TITLE);
            String description=data.getStringExtra(EXTRA_DESC);
            int priority=data.getIntExtra(EXTRA_PRIORITY,1);
            noteViewModel.insert(new Note(title,description,priority));
            Toast.makeText(this, "saved successful", Toast.LENGTH_SHORT).show();
        } else  if(requestCode==UPDATENOTEREQUEST&&resultCode==RESULT_OK){
            int id=data.getIntExtra(AddEditNoteActivity.EXTRA_ID,-1);
            if(id==-1){
                Toast.makeText(this, "Note cant be updated", Toast.LENGTH_SHORT).show();
                return;
            }
            String title=data.getStringExtra(EXTRA_TITLE);
            String description=data.getStringExtra(EXTRA_DESC);
            int priority=data.getIntExtra(EXTRA_PRIORITY,1);
            Note note=new Note(title,description,priority);
            note.setId(id);
            noteViewModel.update(note);
            Toast.makeText(this, "Note updated", Toast.LENGTH_SHORT).show();
        }  else{
            Toast.makeText(this, "Note not saved", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()){
            case R.id.delete_all_notes: noteViewModel.deleteAllNotes();
                Toast.makeText(this, "All notes deleted", Toast.LENGTH_SHORT).show();
            return  true;
            default:return super.onOptionsItemSelected(item);
        }
    }
}
