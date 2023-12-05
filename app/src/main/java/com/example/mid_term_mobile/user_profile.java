package com.example.mid_term_mobile;


import static android.widget.Toast.LENGTH_SHORT;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.NumberPicker;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import de.hdodenhof.circleimageview.CircleImageView;

public class user_profile extends AppCompatActivity {
    private CircleImageView avatar;
    private ImageButton back;
    private EditText name, phone, email, pass;
    private NumberPicker age;
    private RadioButton manager, employee, normal, locked;
    private RadioGroup group1, group2;
    private AppCompatButton save;

    private static final int PICK_IMAGE_REQUEST = 1;
    private Uri link_avatar;

    private FirebaseDatabase database;
    private StorageReference storage_avatar;

    private String key;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);

        initUI();

        age.setMinValue(1);
        age.setMaxValue(150);
        age.setValue(1);

        avatar.setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.avatar_default));

        setAvatar(avatar);



        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name1, phone1, email1, pass1;
                String manager1, employee1;
                String normal1, locked1;
                int age1;

                name1 = name.getText().toString();
                phone1 = phone.getText().toString();
                email1 = email.getText().toString();
                pass1 = pass.getText().toString();
                age1 = age.getValue();
                //manager1 = String.valueOf(manager.getText().toString());
                //employee1 = String.valueOf(employee.getText().toString());
                //normal1 = String.valueOf(normal.getText().toString());
                //locked1 = String.valueOf(locked.getText().toString());



                if(name1.isEmpty()){
                    Toast.makeText(user_profile.this, "Please enter Name", LENGTH_SHORT).show();
                    name.requestFocus();
                } else if (phone1.isEmpty()) {
                    Toast.makeText(user_profile.this, "Please enter Phone number", LENGTH_SHORT).show();
                    phone.requestFocus();
                } else if (email1.isEmpty()) {
                    Toast.makeText(user_profile.this, "Please enter Email", LENGTH_SHORT).show();
                    email.requestFocus();
                } else if (pass1.isEmpty()) {
                    Toast.makeText(user_profile.this, "Please enter Pass", LENGTH_SHORT).show();
                    pass.requestFocus();
                } else if ((manager.isChecked() == false && employee.isChecked() == false) || (manager.isChecked() == true && employee.isChecked() == true)) {
                    Toast.makeText(user_profile.this, "Please select a role", LENGTH_SHORT).show();
                    manager.requestFocus();
                } else if ((normal.isChecked() == false && locked.isChecked() == false) || (normal.isChecked() == true && locked.isChecked() == true)) {
                    Toast.makeText(user_profile.this, "Please select a status of account", LENGTH_SHORT).show();
                    normal.requestFocus();
                }else{
                    putUsertodatabase();
                }


            }
        });


        back_to_home();
    }

    private void back_to_home() {
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(user_profile.this, home.class);
                startActivity(intent);
                finish();
            }
        });
    }

    private void putUsertodatabase(){
        StorageReference storageRef = FirebaseStorage.getInstance().getReference();
        StorageReference avatarRef = storageRef.child("avatars/" + key);

        FirebaseDatabase database1 = FirebaseDatabase.getInstance();

        DatabaseReference usersRef = database1.getReference("user");

        String name1, phone1, email1, pass1;
        String manager1, employee1;
        String normal1, locked1;
        int age1;

        name1 = name.getText().toString();
        phone1 = phone.getText().toString();
        email1 = email.getText().toString();
        pass1 = pass.getText().toString();
        age1 = age.getValue();

        String role = "employee";
        String status = "normal";

        if(manager.isChecked() == true){
            role = "manager";
        }
        if(locked.isChecked() == true){
            status = "locked";
        }



        String finalRole = role;
        String finalStatus = status;
        avatarRef.putFile(link_avatar).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                avatarRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        String url = uri.toString();



                        User user = new User(url, name1, email1, pass1, finalRole, finalStatus,age1, phone1);


                        //usersRef.child(key + "/avatar").setValue(url);


                        String userID = usersRef.push().getKey();

                        usersRef.child(userID).setValue(user);


                        avatar.clearFocus();
                        name.setText("");
                        email.setText("");
                        pass.setText("");
                        phone.setText("");
                        group1.clearCheck();
                        group2.clearCheck();
                        avatar.setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.avatar_default));


                        Toast.makeText(user_profile.this, "Add successfully", Toast.LENGTH_SHORT).show();

                    }
                });
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(user_profile.this, "Add failure", Toast.LENGTH_SHORT).show();
            }
        });

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        link_avatar = data.getData();

        //upload avatar to firebase
        //upload_firebase(link_avatar);

        avatar.setImageURI(link_avatar);
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
                        Toast.makeText(user_profile.this, "Update successfully", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        });
    }

    private void setAvatar(ImageView avatar) {

        avatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder;
                builder = new AlertDialog.Builder(user_profile.this);
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

        back = findViewById(R.id.userAdd_btnBack);
        avatar = findViewById(R.id.userAdd_avatar);
        name = findViewById(R.id.userAdd_name);
        phone = findViewById(R.id.userAdd_phone);
        email = findViewById(R.id.userAdd_email);
        pass = findViewById(R.id.userAdd_pass);
        age = findViewById(R.id.userAdd_age);
        manager = findViewById(R.id.userAdd_manager);
        employee = findViewById(R.id.userAdd_employee);
        normal = findViewById(R.id.userAdd_normal);
        locked = findViewById(R.id.userAdd_locked);
        save = findViewById(R.id.userAdd_save);
        group1 =findViewById(R.id.userAdd_radioGroup1);
        group2 =findViewById(R.id.userAdd_radioGroup2);
    }
}