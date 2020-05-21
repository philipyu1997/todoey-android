package com.yuphilip.controller;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.yuphilip.R;

import java.util.Objects;

import static com.yuphilip.controller.MainActivity.ITEM_POSITION;
import static com.yuphilip.controller.MainActivity.ITEM_TEXT;

public class EditItemActivity extends AppCompatActivity {

    //region Properties

    private EditText etItemText; // Track edit text
    private int position; // Position of edited item in list

    //endregion

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_edit_item);

        // Resolve edit text from layout
        etItemText = findViewById(R.id.etItemText);
        // Set edit text value from intent extra
        etItemText.setText(getIntent().getStringExtra(ITEM_TEXT));

        // Update position from intent extra
        position = getIntent().getIntExtra(ITEM_POSITION, 0);
        // Update the title bar of the activity

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Objects.requireNonNull(getSupportActionBar()).setTitle("Edit Item");
        }

    }

    //region Button Action Section

    // Handler for save button
    public void onSaveItem(View view) {

        // Prepare new intent for result
        Intent i = new Intent();

        // Pass updated item text as extra
        i.putExtra(ITEM_TEXT, etItemText.getText().toString());
        // Pass original position as extra
        i.putExtra(ITEM_POSITION, position);

        // Set the intent as a result of the activity
        setResult(RESULT_OK, i);
        // Close the activity and redirect to main

        finish();

    }

    //endregion

}
