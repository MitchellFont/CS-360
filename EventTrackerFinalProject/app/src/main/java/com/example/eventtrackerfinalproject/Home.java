package com.example.eventtrackerfinalproject;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class Home extends AppCompatActivity {

    public static final int REQUEST = 1;
    RecyclerView recyclerV;
    FloatingActionButton addB;
    Custom Adaptor;
    MainDatabase db;
    ArrayList<String> e_id, e_title, e_description, e_date, e_time;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        // build a recycler view to display events from db
        recyclerV = findViewById(R.id.recyclerview);
        addB = findViewById(R.id.add_new);

        // action button to add event
        addB.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                Intent intent = new Intent(Home.this, Add.class);
                startActivityForResult(intent, REQUEST);       // deprecated use here

            }
        });

        //create arrays
        db = new MainDatabase(Home.this);
        e_id = new ArrayList<>();
        e_title = new ArrayList<>();
        e_description = new ArrayList<>();
        e_date = new ArrayList<>();
        e_time = new ArrayList<>();

        //data to fill arrays
        storeDataInArrays();

        //this will build the rows
        Adaptor = new Custom(Home.this, this, e_id, e_title,
                e_description, e_date, e_time);
        recyclerV.setAdapter(Adaptor);
        recyclerV.setLayoutManager(new LinearLayoutManager(Home.this));

    }

    @Override
    protected void onActivityResult(int request, int result, @Nullable Intent data){
        super.onActivityResult(request, result, data);
        if(request == 1){
            recreate();
        }
    }

    //creates rws for the recycler
    void storeDataInArrays(){
        Cursor c = db.readAllData();
        if(c.getCount() == 0){
            Toast.makeText(this, "No data", Toast.LENGTH_SHORT).show();
        }
        else{
            while (c.moveToNext()){
                e_id.add(c.getString(0));
                e_title.add(c.getString(1));
                e_description.add(c.getString(2));
                e_date.add(c.getString(3));
                e_time.add(c.getString(4));
            }
        }
    }
    void delete(){

    }
}