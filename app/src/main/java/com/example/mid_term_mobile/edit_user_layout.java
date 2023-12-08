package com.example.mid_term_mobile;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.content.ContextCompat;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class edit_user_layout extends AppCompatActivity {

    private CircleImageView avatar;

    private TextView name, email;
    private EditText  phone, pass;
    private RadioButton normal, locked;
    private RadioGroup role;
    private AppCompatButton save;
    static final int PICK_IMAGE_REQUEST = 1;
    private Uri link_avatar;

    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private StorageReference storage_avatar;

    private String key;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_user_layout);


        initUI();
        setAvatar();
        //remove_user();


        Intent intent = getIntent();
        key = intent.getStringExtra("email");

        getAvatar(key, avatar);

        fillInfor(key);

        avatar.setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.avatar_default));

        //setAvatar(avatar);

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                DatabaseReference usersRef = database.getReference("user");

                String  phone1, pass1;
                String normal1, locked1;


                phone1 = phone.getText().toString();
                pass1 = pass.getText().toString();

                String status = "normal";

                if(locked.isChecked() == true){
                    status = "locked";
                }


                String finalStatus = status;

                //Query query = usersRef.orderByChild("mail").equalTo(key);

                usersRef.orderByChild("mail").equalTo(key).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if(snapshot.exists()){
                            for(DataSnapshot snap : snapshot.getChildren()){
                                User user = snap.getValue(User.class);

                                String id = user.getUserID();
                                    /*Map<String, Object> updates = new HashMap<>();
                                    updates.put("phone", phone1);
                                    updates.put("pass", pass1);
                                    updates.put("status", finalStatus);
                                    // Update the specific fields in Firebase
                                    usersRef.child(user.getMail()).updateChildren(updates);*/

                                //snap.child("id").getRef().setValue(new User(user.getAvatar(), user.getName(), user.getMail(),pass1,user.getRole(), finalStatus, phone1 ,user.getUserID(), user.getAge() ));

                                snap.child("pass").getRef().setValue(pass1);
                                snap.child("phone").getRef().setValue(phone1);
                                snap.child("status").getRef().setValue(finalStatus);

                                Toast.makeText(edit_user_layout.this, "Success", Toast.LENGTH_SHORT).show();

                                finish();

                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }
        });

    }

    private void change_Infor() {

        StorageReference storageRef = FirebaseStorage.getInstance().getReference();
        StorageReference avatarRef = storageRef.child("avatars/" + key);


    }

    private void fillInfor(String key) {
        DatabaseReference usersRef = database.getReference("user");
        Query query = usersRef.orderByChild("mail").equalTo(key);

        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    for(DataSnapshot snap : snapshot.getChildren()){
                        User user = snap.getValue(User.class);
                        if(user.getMail().toString().equals(key)){
                            name.setText(user.getName());
                            phone.setText(user.getPhone());
                            email.setText(user.getMail());
                            pass.setText(user.getPass());
                            if(user.getStatus().equals("normal")){
                                normal.setChecked(true);
                            } else if (user.getStatus().equals("locked")) {
                                locked.setChecked(true);
                            }
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        link_avatar = data.getData();

        avatar.setImageURI(link_avatar);
    }

    private void remove_user() {
        DatabaseReference usersRef = database.getReference("user");
        Query query = usersRef.orderByChild("mail").equalTo(key);
        Query query1 = usersRef.orderByChild("phone");


        /*remove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(edit_user_layout.this, list_user.class);
                startActivity(intent);
                }
            });*/

    }

    private void setAvatar() {
        avatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder;
                builder = new AlertDialog.Builder(edit_user_layout.this);
                builder.setTitle("Picture");

                final CharSequence[] items = {"Gallery"};
                builder.setItems(items, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        switch (i) {
                            case 0:
                                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                                startActivityForResult(intent, PICK_IMAGE_REQUEST);
                                break;
                        }
                    }
                });

                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });
    }

    private void initUI() {
        avatar = findViewById(R.id.editUser_avatar);
        name = findViewById(R.id.editUser_name);
        phone = findViewById(R.id.editUser_phone);
        email = findViewById(R.id.editUser_email);
        pass = findViewById(R.id.editUser_pass);
        normal = findViewById(R.id.editUser_normal);
        locked = findViewById(R.id.editUser_locked);
        role = findViewById(R.id.editUser_role);
        save = findViewById(R.id.editUser_save);
    }

    private void getAvatar(String email, ImageView avatar) {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference usersRef = database.getReference("user");
        Query query = usersRef.orderByChild("mail").equalTo(email);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    for (DataSnapshot userSnapshot : dataSnapshot.getChildren()) {
                        User user = userSnapshot.getValue(User.class);
                        if(user.getAvatar().isEmpty()){
                            user.setAvatar("https://firebasestorage.googleapis.com/v0/b/mid-term-mobile.appspot.com/o/avatars%2Fnull?alt=media&token=c1762122-4618-429e-8f10-b8004b33aaa5");
                        }
                        Picasso.get()
                                .load(user.getAvatar())
                                .into(avatar);
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }



}