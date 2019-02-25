package com.jesulonimi.user.sqliteandmvvp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.Toast;

public class AddEditNoteActivity extends AppCompatActivity {
    private EditText title;
    private EditText description;
    private NumberPicker numberPicker;
    public static final String EXTRA_TITLE = " com.jesulonimi.user.sqliteandmvvp.TITLE";
    public static final String EXTRA_ID = " com.jesulonimi.user.sqliteandmvvp.ID";
    public static final String EXTRA_DESC = " com.jesulonimi.user.sqliteandmvvp.DESCRIPTION";
    public static final String EXTRA_PRIORITY = " com.jesulonimi.user.sqliteandmvvp.Priority";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_note);
        title = findViewById(R.id.edit_text_title);
        description = findViewById(R.id.edit_text_desc);
        numberPicker = findViewById(R.id.num_picker);

        numberPicker.setMaxValue(10);
        numberPicker.setMinValue(1);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_close);

        if (getIntent().hasExtra(EXTRA_ID)) {
            setTitle("Edit Note");
            title.setText(getIntent().getStringExtra(EXTRA_TITLE));
            description.setText(getIntent().getStringExtra(EXTRA_DESC));
            numberPicker.setValue(getIntent().getIntExtra(EXTRA_PRIORITY, 1));
        } else {
            setTitle("Add Note");
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.add_note_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.savenote:
                saveeNote();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void saveeNote() {
        String titleText = title.getText().toString();
        String desctext = description.getText().toString();
        int priority = numberPicker.getValue();
        if (titleText.trim().isEmpty() || desctext.trim().isEmpty()) {
            Toast.makeText(this, "Please insert a title and a description", Toast.LENGTH_SHORT).show();
            return;
        }
        Intent data = new Intent();
        data.putExtra(EXTRA_TITLE, titleText);
        data.putExtra(EXTRA_DESC, desctext);
        data.putExtra(EXTRA_PRIORITY, priority);
        int id = getIntent().getIntExtra(EXTRA_ID, -1);
        if (id != -1) {
            data.putExtra(EXTRA_ID, id);
        }
        setResult(RESULT_OK, data);
        finish();
    }
}
