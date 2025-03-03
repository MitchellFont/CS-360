package com.example.eventtrackerfinalproject;
// Mitchell Fontaine
// 08/14/2024
// CS-360

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class Main extends AppCompatActivity {

    private int SMS_PERMISSION_CODE = 1;
    EditText username, password;
    Button signB, registerB, requestB;

    LoginDatabase DB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        username = (EditText) findViewById(R.id.username_one);
        password = (EditText) findViewById(R.id.password_one);
        signB = (Button) findViewById(R.id.sign_in_button_one);
        registerB = (Button) findViewById(R.id.register_button);
        requestB = (Button) findViewById(R.id.request_button);
        DB = new LoginDatabase(this);

        createNotificationChannel();    //channel for notifications

        // Log in
        signB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String U = username.getText().toString();
                String P = password.getText().toString();

                //Makes sure all required fields are filled
                if (U.equals("") || P.equals("")) {
                    Toast.makeText(Main.this, "Make sure you filled in all the fields", Toast.LENGTH_SHORT).show();
                }
                //This will look to see if the username and password already exist and to see if it is a match in the database
                else {
                    Boolean checkPass = DB.checkUsernamePassword(U, P);
                    if (checkPass == true) {
                        Toast.makeText(Main.this, "Successfully Logged In", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(getApplicationContext(), Home.class);
                        startActivity(intent);

                    } else {
                        Toast.makeText(Main.this, "Invalid credentials", Toast.LENGTH_SHORT).show();

                    }
                }
            }
        });

        //This is for the user to be able to register and have their data stored in the database
        registerB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), Register.class);
                startActivity(intent);
            }
        });

    }

    private void createNotificationChannel() {
        CharSequence name = "Reminder";
        String desc = "For Reminder";
        int imp = NotificationManager.IMPORTANCE_DEFAULT;
        NotificationChannel ch = new NotificationChannel("notifyText", name, imp);
        ch.setDescription(desc);

        NotificationManager notificationManager = getSystemService(NotificationManager.class);
        notificationManager.createNotificationChannel(ch);
    }

}