package com.example.mid_term_mobile;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.content.FileProvider;

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

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.Console;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class home extends AppCompatActivity {

    private ImageView avatar;
    private TextView name;
    private AppCompatButton listUser, addUser, listStudent, addStudent, btnImport, btnExport, logout;

    private static final int PICK_IMAGE_REQUEST = 1;

    private Uri link_avatar;

    private FirebaseDatabase database;
    private StorageReference storage_avatar;

    private String key;
    private String role;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        initView();

        Intent intent = getIntent();
        key = intent.getStringExtra("key");

        name.setText(intent.getStringExtra("name"));
        role = intent.getStringExtra("role");


        getAvatar(intent.getStringExtra("email"), avatar);

        if(role.equals("admin")){
            listUser.setVisibility(View.VISIBLE);
            addUser.setVisibility(View.VISIBLE);
            addStudent.setVisibility(View.VISIBLE);
            btnImport.setVisibility(View.VISIBLE);
            btnExport.setVisibility(View.VISIBLE);

        } else if (role.equals("manager")) {
            addStudent.setVisibility(View.VISIBLE);
            btnImport.setVisibility(View.VISIBLE);
            btnExport.setVisibility(View.VISIBLE);
        }



        click_event();


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

    private void click_event() {
        listUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(home.this, list_user.class);
                startActivity(intent);
            }
        });

        addUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(home.this, user_profile.class);
                startActivity(intent);
            }
        });

        listStudent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(home.this, list_students_view.class);
                intent.putExtra("role", role);
                startActivity(intent);
            }
        });


        addStudent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(home.this, student_profile.class);
                startActivity(intent);
            }
        });


        btnImport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });


        btnExport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //write_file();
                export_file();
                Toast.makeText(home.this, "Success", Toast.LENGTH_SHORT).show();
            }


        });

    }

    private void export_file() {
        DatabaseReference databaseRef = FirebaseDatabase.getInstance().getReference("student");

        databaseRef.addListenerForSingleValueEvent(new ValueEventListener() {

            File file = new File(getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS), "exported_data.xlsx");

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // Step 2: Export data to Excel (XLSX) file
                try {
                    Workbook  workbook = new XSSFWorkbook();
                    Sheet sheet = workbook.createSheet("Data");

                    int rowNum = 0;

                    Row header = sheet.createRow(0);
                    header.createCell(0).setCellValue("name");
                    header.createCell(1).setCellValue("age");
                    header.createCell(2).setCellValue("st_class");
                    header.createCell(3).setCellValue("phone");
                    header.createCell(4).setCellValue("email");

                    //rowNum+=1;

                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        // Assuming each child represents a row in your Excel file
                        Row row = sheet.createRow(rowNum++);



                        String name = snapshot.child("name").getValue(String.class);
                        String age = snapshot.child("age").getValue(String.class);
                        String st_class = snapshot.child("st_class").getValue(String.class);
                        String phone = snapshot.child("phone").getValue(String.class);
                        String email = snapshot.child("email").getValue(String.class);

                        //Log.e("name",name);
                        //Log.e("age",age);
                        //Log.e("st_class",st_class);
                        //Log.e("phone",phone);
                        //Log.e("email",email);


                        row.createCell(0).setCellValue(name);
                        row.createCell(1).setCellValue(age);
                        row.createCell(2).setCellValue(st_class);
                        row.createCell(3).setCellValue(phone);
                        row.createCell(4).setCellValue(email);


                    }

                    // Step 3: Save the Excel file to device's storage
                    //File file = new File(getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS), "exported_data.xlsx");
                    FileOutputStream fileOut = new FileOutputStream(file);
                    workbook.write(fileOut);
                    fileOut.close();



                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Handle errors
            }
        });
    }

    private void write_file(){
        Intent intent = new Intent(Intent.ACTION_CREATE_DOCUMENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        intent.putExtra(Intent.EXTRA_TITLE, "list_export_students.xlsx");
        startActivityForResult(intent, 2);
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
        } /*else if (requestCode == 2 && resultCode == Activity.RESULT_OK && data != null && data.getData() != null) {
            Uri uri = data.getData();
            getStudents(new StudentListCallback() {
                @Override
                public void onCallback(List<Student> students) {
                    exportToExcel(uri, students);
                }
            });
        }*/


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