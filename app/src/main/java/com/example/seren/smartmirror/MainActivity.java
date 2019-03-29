package com.example.seren.smartmirror;


import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    public static  String user_mail ;
    public static  String user_name;
    private int Gallery_intent = 2;
    private EditText input_name, input_surname, input_email, input_password;
    private ImageButton input_image;
    //private ListView list_data;
    //private ProgressBar circular_progress;

    private FirebaseDatabase nFirebaseDatabase;
    private DatabaseReference nDatabaseReference;
    private StorageReference imagePath;

    private List<User> list_users = new ArrayList<>();
    //private User selectedUser; //hold user when we select item in listview

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Add toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Smart Mirror");
        setSupportActionBar(toolbar);

        //Control
        //comes from activity_main
        //circular_progress= (ProgressBar)findViewById(R.id.circular_progress);
        input_name = (EditText) findViewById(R.id.name);
        input_surname = (EditText) findViewById(R.id.Surname);
        input_email = (EditText) findViewById(R.id.email);
        input_password = (EditText) findViewById(R.id.password);
        input_image = (ImageButton) findViewById(R.id.imageButton);
       /* list_data= (ListView)findViewById(R.id.list_data);
        list_data.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                User user = (User)parent.getItemAtPosition(position);
                selectedUser = user;
                input_name.setText(user.getName());
                input_surname.setText(user.getSurname());
                input_email.setText(user.getEmail());
                input_password.setText((user.getPassword()));
            }
        });*/

        //Firebase
        initFirebase();
        //addEventFirebaseListener();

    }

 /*   private void addEventFirebaseListener() {
        //Progressing
        //circular_progress.setVisibility(View.VISIBLE);
        //list_data.setVisibility(View.INVISIBLE);

        nDatabaseReference.child("users").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (list_users.size() > 0)
                    list_users.clear();
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    User user = postSnapshot.getValue(User.class);
                    list_users.add(user);
                }
                //ListViewAdapter adapter = new ListViewAdapter(MainActivity.this, list_users);
                //list_data.setAdapter(adapter);

                //circular_progress.setVisibility(View.INVISIBLE);
                //list_data.setVisibility(View.VISIBLE);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }*/

    private void initFirebase() {
        FirebaseApp.initializeApp(this);
        nFirebaseDatabase = FirebaseDatabase.getInstance();
        nDatabaseReference = nFirebaseDatabase.getReference();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.menu_add) {
            createUser();
        }
       /* else if(item.getItemId() == R.id.menu_save){
            User user = new User(selectedUser.getUid(), input_name.getText().toString(), input_email.getText().toString(), input_toDo.getText().toString());
            updateUser(user);
        }*/
       /* else if(item.getItemId() == R.id.menu_remove){
            deleteUser(selectedUser);
        }*/
        return true;
    }

    private void deleteUser(User selectedUser) {
        nDatabaseReference.child("users").child(selectedUser.getEmail()).removeValue();
        clearEditText();
    }
/*
    private void updateUser(User user) {
        nDatabaseReference.child("users").child(user.getUid()).child("name").setValue(user.getName());
        nDatabaseReference.child("users").child(user.getUid()).child("surname").setValue(user.getSurname());
        nDatabaseReference.child("users").child(user.getUid()).child("email").setValue(user.getEmail());
        nDatabaseReference.child("users").child(user.getUid()).child("password").setValue(user.getPassword());
        clearEditText();

    }*/

    private void createUser() {
        User user = new User(input_name.getText().toString(), input_surname.getText().toString(), input_email.getText().toString(), input_password.getText().toString(), imagePath.toString());
        nDatabaseReference.child("users").child(user.getEmail()).setValue(user);
        clearEditText();
    }

    public void createUser(View view) {
        User user = new User(input_name.getText().toString(), input_surname.getText().toString(),
                input_email.getText().toString(), input_password.getText().toString(), imagePath.toString());
        nDatabaseReference.child("users").child(user.getEmail()).setValue(user);
        clearEditText();

        Intent intent = new Intent(this, ToDoActivity.class);
        intent.putExtra(user_mail, user.getEmail());
        //intent.putExtra(user_name, user.getName());
        startActivity(intent);

    }

    private void clearEditText() {
        input_name.setText("");
        input_surname.setText("");
        input_email.setText("");
        input_password.setText("");
    }

    public void btnImage(View view) {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent, Gallery_intent);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Gallery_intent && resultCode == RESULT_OK) {
            Uri uri = data.getData();
            input_image.setImageURI(uri);
            imagePath = FirebaseStorage.getInstance().getReference().child("users").child(uri.getLastPathSegment());
            imagePath.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {

                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    Toast.makeText(MainActivity.this, "Uploaded...", Toast.LENGTH_SHORT);
                }
            })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(MainActivity.this, "Not Uploaded...", Toast.LENGTH_SHORT);
                        }
                    });
        }
    }
}
