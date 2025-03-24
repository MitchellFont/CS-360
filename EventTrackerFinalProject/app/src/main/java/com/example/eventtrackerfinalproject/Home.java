package com.example.eventtrackerfinalproject;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Home extends AppCompatActivity implements Custom.OnItemClickListener {

    private RecyclerView recyclerView;
    private FloatingActionButton addButton;
    private Custom adapter;
    private MainDatabase db;
    private List<Reminder> reminders;
    private Map<String, Reminder> reminderMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        recyclerView = findViewById(R.id.recyclerview);
        addButton = findViewById(R.id.add_new);

        // Initialize database and reminders list
        db = new MainDatabase(this);
        reminders = new ArrayList<>();
        reminderMap = new HashMap<>();

        // Load data from database
        loadData();

        // Set up RecyclerView
        setupRecyclerView();

        // Add new event
        addButton.setOnClickListener(v -> {
            Intent intent = new Intent(Home.this, Add.class);
            startActivityForResult(intent, Constants.REQUEST_CODE_ADD);
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Constants.REQUEST_CODE_ADD) {
            recreate(); // Refresh the activity
        }
    }

    private void loadData() {
        reminders.clear();
        reminderMap.clear();
        
        Cursor cursor = db.readAllData();
        if (cursor.getCount() == 0) {
            Toast.makeText(this, "No events found", Toast.LENGTH_SHORT).show();
        } else {
            while (cursor.moveToNext()) {
                Reminder reminder = new Reminder(
                        cursor.getString(0),
                        cursor.getString(1),
                        cursor.getString(2),
                        cursor.getString(3),
                        cursor.getString(4)
                );
                reminders.add(reminder);
                reminderMap.put(reminder.getId(), reminder);
            }
            sortReminders();
        }
    }

    private void sortReminders() {
        Collections.sort(reminders, new Comparator<Reminder>() {
            @Override
            public int compare(Reminder r1, Reminder r2) {
                String dateTime1 = r1.getDate() + " " + r1.getTime();
                String dateTime2 = r2.getDate() + " " + r2.getTime();
                return dateTime1.compareTo(dateTime2);
            }
        });
    }

    private void setupRecyclerView() {
        adapter = new Custom(this, this, reminders, this);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Constants.REQUEST_CODE_ADD || requestCode == Constants.REQUEST_CODE_UPDATE) {
            loadData();
            adapter.updateData(reminders);
        }
    }

    @Override
    public void onItemClick(int position) {
        // Handle item click if needed
    }

    @Override
    public void onEditClick(int position) {
        Reminder reminder = reminders.get(position);
        Intent intent = new Intent(this, Update.class);
        intent.putExtra(Constants.EXTRA_ID, reminder.getId());
        intent.putExtra(Constants.EXTRA_TITLE, reminder.getTitle());
        intent.putExtra(Constants.EXTRA_DESCRIPTION, reminder.getDescription());
        intent.putExtra(Constants.EXTRA_DATE, reminder.getDate());
        intent.putExtra(Constants.EXTRA_TIME, reminder.getTime());
        startActivityForResult(intent, Constants.REQUEST_CODE_UPDATE);
    }

    public Reminder getReminderById(String id) {
        return reminderMap.get(id);
    }
}
