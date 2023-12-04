package com.example.mid_term_mobile;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

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

public class home extends AppCompatActivity {

    private ImageView avatar;
    private TextView name;
    private AppCompatButton listUser, addUser, listStudent, addStudent, btnImport, btnExport, logout;

    private static final int PICK_IMAGE_REQUEST = 1;

    private Uri link_avatar;

    private FirebaseDatabase database;
    private StorageReference storage_avatar;

    private String key;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        initView();

        Intent intent = getIntent();
        key = intent.getStringExtra("key");

        name.setText(intent.getStringExtra("name"));
        int role = intent.getIntExtra("role", 0);


        getAvatar(intent.getStringExtra("email"), avatar);

        if(role == 0){
            listUser.setVisibility(View.VISIBLE);
            addUser.setVisibility(View.VISIBLE);
            addStudent.setVisibility(View.VISIBLE);
            btnImport.setVisibility(View.VISIBLE);
            btnExport.setVisibility(View.VISIBLE);
        } else if (role == 1) {
            addStudent.setVisibility(View.VISIBLE);
            btnImport.setVisibility(View.VISIBLE);
            btnExport.setVisibility(View.VISIBLE);
        }



        avatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder;
                builder = new AlertDialog.Builder(home.this);
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

        logout_function(logout);

    }

    private void logout_function(AppCompatButton logout) {
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(home.this, login.class);

                startActivity(intent);

                finish();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK && data != null && data.getData() != null){
            link_avatar = data.getData();

            //upload avatar to firebase
            upload_firebase(link_avatar);

            avatar.setImageURI(link_avatar);
        }
    }

    private void upload_firebase(Uri linkAvatar) {
        StorageReference storageRef = FirebaseStorage.getInstance().getReference();
        StorageReference avatarRef = storageRef.child("avatars/" + key);

        FirebaseDatabase database1 = FirebaseDatabase.getInstance();

        DatabaseReference usersRef = database1.getReference("user");


        avatarRef.putFile(link_avatar).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                avatarRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        String url = uri.toString();
                        usersRef.child(key + "/avatar").setValue(url);
                        Toast.makeText(home.this, "Update successfully", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        });



    }


    private void initView() {
        avatar = findViewById(R.id.img_profile);
        name = findViewById(R.id.txt_name_home);
        listUser = findViewById(R.id.home_listUsers);
        addUser = findViewById(R.id.home_addnewUser);
        listStudent = findViewById(R.id.home_ListStudents);
        addStudent = findViewById(R.id.home_add_student);
        btnImport = findViewById(R.id.home_import);
        btnExport = findViewById(R.id.home_export);
        logout = findViewById(R.id.home_logout);


        listUser.setVisibility(View.GONE);
        addUser.setVisibility(View.GONE);
        addStudent.setVisibility(View.GONE);
        btnImport.setVisibility(View.GONE);
        btnExport.setVisibility(View.GONE);
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
                            user.setAvatar("https://firebasestorage.googleapis.com/v0/b/mid-term-mobile.appspot.com/o/avatars%2FUS3?alt=media&token=6dedf4b5-8362-4ad3-bf40-9e69e8cf1cbe");
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