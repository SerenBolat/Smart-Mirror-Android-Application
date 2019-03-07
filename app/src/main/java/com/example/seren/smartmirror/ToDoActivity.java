package com.example.seren.smartmirror;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

public class ToDoActivity extends AppCompatActivity
        implements DatePickerDialog.OnDateSetListener {

    private DatabaseReference databaseToDo;
    private EditText editNote;
    private RecyclerView.LayoutManager mLayoutManager;
    private RecyclerView recyclerView;
    private ToDoAdapter toDoAdapter;
    private FloatingActionButton floatingActionButton;
    List<ToDoList> toDoLists;
    boolean selectDate;
    String dateofNote;

    @Override
    protected void onStart() {
        super.onStart();
        databaseToDo.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                toDoLists.clear();
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    ToDoList toDo = postSnapshot.getValue(ToDoList.class);
                    toDoLists.add(toDo);
                }
                recyclerView.setHasFixedSize(true);
                mLayoutManager = new LinearLayoutManager(ToDoActivity.this);
                recyclerView.setLayoutManager(mLayoutManager);

                toDoAdapter = new ToDoAdapter(toDoLists,ToDoActivity.this);
                recyclerView.setAdapter(toDoAdapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_to_do);
        Intent intent = getIntent();

        databaseToDo = FirebaseDatabase.getInstance().getReference("ToDoList").child(intent.getStringExtra(MainActivity.user_mail));
        editNote = (EditText)findViewById(R.id.note);
        floatingActionButton = (FloatingActionButton)findViewById(R.id.addNote);
        toDoLists = new ArrayList<>();
        recyclerView = (RecyclerView)findViewById(R.id.list_data);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(editNote.getText().length()>0 && selectDate){

                    String id  = databaseToDo.push().getKey();
                    String situation = "ToDo";
                    ToDoList toDoList = new ToDoList(id, dateofNote,editNote.getText().toString(),situation);
                    databaseToDo.child(id).setValue(toDoList);
                    Toast.makeText(getApplicationContext(),"Note added",Toast.LENGTH_SHORT).show();
                    editNote.setText("");
                    selectDate=false;

                }else{

                    showAlertDialog();
                }
            }
        });
    }
    private void showAlertDialog() {

        AlertDialog.Builder builder = new AlertDialog.Builder(ToDoActivity.this);
        builder.setTitle("Error");
        builder.setMessage("Please add a note and pick a date");

        String positiveText = "OK.";
        builder.setPositiveButton(positiveText,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // positive button logic
                    }
                });

        AlertDialog dialog = builder.create();
        // display dialog
        dialog.show();
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int day) {
        Calendar cal = new GregorianCalendar(year, month, day);
        selectDate = setDate(cal);
    }

    private boolean setDate(final Calendar calendar) {
        final DateFormat dateFormat = DateFormat.getDateInstance(DateFormat.MEDIUM);

        dateofNote = dateFormat.format(calendar.getTime());
        //((TextView) findViewById(R.id.showDate))
                //.setText(dateFormat.format(calendar.getTime()));
        if(dateFormat.format((calendar.getTime()))!= null){
            return true;
        }
        else {
            return false;
        }
    }

    public void datePicker(View view){

        DatePickerFragment fragment = new DatePickerFragment();
        fragment.show(getSupportFragmentManager(), "datePicker");
    }
}
