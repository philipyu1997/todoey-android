package com.yuphilip.controller;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.yuphilip.R;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Objects;

public class MainActivity extends AppCompatActivity {

    //region Properties

    private final static int EDIT_REQUEST_CODE = 20; // A numeric code to identify the edit activity
    public final static String ITEM_TEXT = "itemText"; // keys used for passing data between activities
    public final static String ITEM_POSITION = "itemPosition";

    private ArrayList<String> items;
    private ArrayAdapter<String> itemsAdapter;
    private ListView lvItems;

    //endregion

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        readItems();
        itemsAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, items);
        lvItems = findViewById(R.id.lvItems);
        lvItems.setAdapter(itemsAdapter);

        setupListViewListener();

    }

    public void onAddItem(View view) {

        EditText etNewItem = findViewById(R.id.etNewItem);
        String itemText = etNewItem.getText().toString();

        itemsAdapter.add(itemText);

        etNewItem.setText("");

        writeItems();

        Toast.makeText(getApplicationContext(), "Item added to list", Toast.LENGTH_SHORT).show();

    }

    private void setupListViewListener() {

        Log.i("MainActivity", "Setting up listener on list view");

        lvItems.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                Log.i("MainActivity", "Item removed from list " + i);
                items.remove(i);
                itemsAdapter.notifyDataSetChanged();
                writeItems();

                return true;
            }
        });

        // Setup item listener for edit (regular click)
        lvItems.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                // create the new activity
                Intent intent = new Intent(MainActivity.this, EditItemActivity.class);
                // pass te data being edited
                intent.putExtra(ITEM_TEXT, items.get(i));
                intent.putExtra(ITEM_POSITION, i);
                // display the activity
                startActivityForResult(intent, EDIT_REQUEST_CODE);
            }
        });

    }

    // Handle results from edit activity
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

        super.onActivityResult(requestCode, resultCode, data);

        // If the edit activity completed ok
        if (resultCode == RESULT_OK && requestCode == EDIT_REQUEST_CODE) {
            // Extract updated item text from result intent extras
            String updatedItem = null;

            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT) {
                updatedItem = Objects.requireNonNull(Objects.requireNonNull(data).getExtras()).getString(ITEM_TEXT);
            }

            // Extract original position of edited item
            int position = 0;

            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT) {
                position = Objects.requireNonNull(data.getExtras()).getInt(ITEM_POSITION);
            }

            // Update the model with the new item text at the edited position
            items.set(position, updatedItem);

            // Notify the adapter that the model changed
            itemsAdapter.notifyDataSetChanged();

            // Persist the changed model
            writeItems();

            // Notify the user the operation completed ok
            Toast.makeText(this, "Item updated successfully", Toast.LENGTH_SHORT).show();

        }

    }

    private File getDataFile() {
        return new File(getFilesDir(), "todo.txt");
    }

    private void readItems() {

        try {
            items = new ArrayList<>(FileUtils.readLines(getDataFile(), Charset.defaultCharset()));
        } catch (Exception e) {
            Log.e("MainActivity", "Error reading file", e);
            items = new ArrayList<>();
        }

    }

    private void writeItems() {

        try {
            FileUtils.writeLines(getDataFile(), items);
        } catch (IOException e) {
            Log.e("MainActivity", "Error writing file", e);
        }

    }

}
