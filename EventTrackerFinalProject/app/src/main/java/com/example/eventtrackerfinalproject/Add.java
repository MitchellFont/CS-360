package com.example.eventtrackerfinalproject;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import java.text.ParseException;
import java.util.Calendar;

public class Add extends AppCompatActivity {
    private Button addButton, timeButton, dateButton, requestButton;
    private EditText titleInput, descriptionInput;
    private ReminderManager reminderManager;
    private PermissionManager permissionManager;
    private MainDatabase database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);

        // Initialize components
        initViews();
        reminderManager = new ReminderManager(this);
        permissionManager = new PermissionManager(this);
        database = new MainDatabase(this);

        // Set listeners
        setListeners();
    }

    private void initViews() {
        requestButton = findViewById(R.id.request_button);
        titleInput = findViewById(R.id.title_input);
        descriptionInput = findViewById(R.id.description_input);
        timeButton = findViewById(R.id.btnTime);
        dateButton = findViewById(R.id.btnDate);
        addButton = findViewById(R.id.add_new);
    }

    private void setListeners() {
        dateButton.setOnClickListener(view -> selectDate());
        timeButton.setOnClickListener(view -> selectTime());

        titleInput.addTextChangedListener(textWatcher);
        descriptionInput.addTextChangedListener(textWatcher);

        addButton.setOnClickListener(view -> addReminder());
        requestButton.setOnClickListener(view -> permissionManager.requestSmsPermission());
    }

    private void addReminder() {
        Reminder reminder = new Reminder(
                titleInput.getText().toString().trim(),
                dateButton.getText().toString().trim(),
                descriptionInput.getText().toString().trim(),
                timeButton.getText().toString().trim()
        );

        long notificationId = database.addReminder(reminder);

        if (permissionManager.hasSmsPermission()) {
            try {
                reminderManager.setAlarm(reminder);
            } catch (ParseException e) {
                Toast.makeText(this, "Failed to set alarm", Toast.LENGTH_SHORT).show();
                e.printStackTrace();
            }
        } else {
            Toast.makeText(this, "Permission required for this feature", Toast.LENGTH_SHORT).show();
        }

        finish();
    }

    private void selectDate() {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(
                this,
                (view, selectedYear, selectedMonth, selectedDay) -> dateButton.setText(String.format("%d-%d-%d", selectedDay, selectedMonth + 1, selectedYear)),
                year, month, day);
        datePickerDialog.show();
    }

    private void selectTime() {
        Calendar calendar = Calendar.getInstance();
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);

        TimePickerDialog timePickerDialog = new TimePickerDialog(
                this,
                (view, selectedHour, selectedMinute) -> {
                    String time = formatTime(selectedHour, selectedMinute);
                    timeButton.setText(time);
                },
                hour, minute, false);
        timePickerDialog.show();
    }

    private String formatTime(int hour, int minute) {
        String formattedMinute = (minute < 10) ? "0" + minute : String.valueOf(minute);
        String period = (hour < 12) ? "AM" : "PM";
        int formattedHour = (hour == 0 || hour == 12) ? 12 : hour % 12;
        return String.format("%d:%s %s", formattedHour, formattedMinute, period);
    }

    private final TextWatcher textWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            String title = titleInput.getText().toString().trim();
            String description = descriptionInput.getText().toString().trim();
            String date = dateButton.getText().toString().trim();
            String time = timeButton.getText().toString().trim();

            addButton.setEnabled(!title.isEmpty() && !description.isEmpty() && !date.isEmpty() && !time.isEmpty());
        }

        @Override
        public void afterTextChanged(Editable editable) {}
    };
}
