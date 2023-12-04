package com.example.mid_term_mobile;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.TaskCompletionSource;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import org.checkerframework.checker.units.qual.A;

import java.io.FileInputStream;
import java.net.URL;
import java.time.Instant;

public class activity_main_system extends AppCompatActivity {

    private ImageView avatar;
    private TextView name;
    private AppCompatButton listUser, addUser, listStudent, addStudent, btnImport, btnExport, logout;

    private static final int PICK_IMAGE_REQUEST = 1;

    private Uri link_avatar;

    private FirebaseDatabase database;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_system);

        initView();

        Intent intent = getIntent();
        name.setText(intent.getStringExtra("name"));
        int role = intent.getIntExtra("role", 0);

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
                builder = new AlertDialog.Builder(activity_main_system.this);
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK && data != null && data.getData() != null){
            link_avatar = data.getData();


            avatar.setImageURI(link_avatar);
        }
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
}