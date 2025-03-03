package com.example.eventtrackerfinalproject;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class Register extends AppCompatActivity {

    EditText username, password, pass2;
    Button register, signin;
    LoginDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        username = (EditText) findViewById(R.id.username);
        password = (EditText) findViewById(R.id.password);
        pass2 = (EditText) findViewById(R.id.repassword);

        register = (Button)  findViewById(R.id.sign_up_button);
        signin = (Button) findViewById(R.id.sign_in_button);

        db = new LoginDatabase(this);

        //this lets users register with what they put for username and password
        register.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){

                String user = username.getText().toString();
                String pass = password.getText().toString();
                String repass = pass2.getText().toString();

                //checking for data
                if(user.equals("") || (pass.equals("") || repass.equals("")))
                    Toast.makeText(Register.this, "Please check all fields", Toast.LENGTH_SHORT).show();
                else{
                    //makes sure passwords are the same
                    if(pass.equals(repass)){
                        //checks for username in the database
                        Boolean checkuser = db.checkUsername(user);
                        if(checkuser == false){
                            //puts the username and the password into the database
                            Boolean insert = db.insertData(user, pass);
                            //start the home
                            if(insert == true){
                                Toast.makeText(Register.this, "User registered successfully", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(getApplicationContext(), Home.class);
                                startActivity(intent);
                            }
                            else{
                                Toast.makeText(Register.this, "User already exists you can sign in", Toast.LENGTH_SHORT).show();
                            }
                        }
                        else{
                            Toast.makeText(Register.this, "User already exists you can sign in", Toast.LENGTH_SHORT).show();
                        }
                    }
                    else{
                        Toast.makeText(Register.this, "Passwords do not match", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        //takes you to log in
        signin.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                Intent intent = new Intent(getApplicationContext(), Main.class);
                startActivity(intent);

            }
        });
    }
}
