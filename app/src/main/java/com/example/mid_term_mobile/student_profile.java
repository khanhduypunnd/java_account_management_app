package com.example.mid_term_mobile;

import static android.widget.Toast.LENGTH_SHORT;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.content.ContextCompat;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.Calendar;

import de.hdodenhof.circleimageview.CircleImageView;

public class student_profile extends AppCompatActivity {

    private CircleImageView avatar;
    private EditText st_name, st_class, st_email, st_phone, st_dob;

    private ImageButton back;
    private AppCompatButton save;

    private String key;

    private static final int PICK_IMAGE_REQUEST = 1;
    private Uri link_avatar;

    private FirebaseDatabase database;
    private StorageReference storage_avatar;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_profile);

        initUI();


        avatar.setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.avatar_default));


        st_dob.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showCalendar();
            }
        });
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name1, phone1, email1, class1, age1;

                name1 = st_name.getText().toString();
                class1 = st_class.getText().toString();
                email1 = st_email.getText().toString();
                phone1 = st_phone.getText().toString();
                age1 = st_dob.getText().toString();

                //manager1 = String.valueOf(manager.getText().toString());
                //employee1 = String.valueOf(employee.getText().toString());
                //normal1 = String.valueOf(normal.getText().toString());
                //locked1 = String.valueOf(locked.getText().toString());


                if(name1.isEmpty()){
                    Toast.makeText(student_profile.this, "Please enter Name", LENGTH_SHORT).show();
                    st_name.requestFocus();
                } else if (class1.isEmpty()) {
                    Toast.makeText(student_profile.this, "Please enter Class", LENGTH_SHORT).show();
                    st_class.requestFocus();
                } else if (email1.isEmpty()) {
                    Toast.makeText(student_profile.this, "Please enter Email", LENGTH_SHORT).show();
                    st_email.requestFocus();
                } else if (phone1.isEmpty()) {
                    Toast.makeText(student_profile.this, "Please enter Phone number", LENGTH_SHORT).show();
                    st_phone.requestFocus();
                }else if (age1.isEmpty()) {
                    Toast.makeText(student_profile.this, "Please enter Day of birth", LENGTH_SHORT).show();
                    st_dob.requestFocus();
                }
                else{
                    putStudenttodatabase();
                }
            }
        });


        back_to_home();

    }

    private void back_to_home() {
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }


    private void putStudenttodatabase(){
        StorageReference storageRef = FirebaseStorage.getInstance().getReference();
        StorageReference avatarRef = storageRef.child("avatars/avatar_default.png");

        FirebaseDatabase database1 = FirebaseDatabase.getInstance();

        DatabaseReference studentsRef = database1.getReference("student");

        String name1, phone1, email1, class1, age1;

        name1 = st_name.getText().toString();
        phone1 = st_phone.getText().toString();
        email1 = st_email.getText().toString();
        class1 = st_class.getText().toString();
        age1 = st_dob.getText().toString();


        avatarRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                String url = uri.toString();

                Student student = new Student(age1, url,class1, email1, name1, phone1);

                String studentID = studentsRef.push().getKey();

                studentsRef.child(studentID).setValue(student);


                //avatar.clearFocus();
                st_name.setText("");
                st_email.setText("");
                st_class.setText("");
                st_phone.setText("");
                st_dob.setText("");
                avatar.setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.avatar_default));


                Toast.makeText(student_profile.this, "Add successfully", Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(student_profile.this, "Add failure", Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void initUI() {
        avatar = findViewById(R.id.addStudent_avatar);
        st_name = findViewById(R.id.addStudent_name);
        st_class = findViewById(R.id.addStudent_class);
        st_email = findViewById(R.id.addStudent_email);
        st_phone = findViewById(R.id.addStudent_phone);
        st_dob = findViewById(R.id.addStudent_age);
        back = findViewById(R.id.addStudent_back);
        save = findViewById(R.id.addStudent_save);
    }


    private void showCalendar() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());

        int day = calendar.get(Calendar.DAY_OF_MONTH);
        int month = calendar.get(Calendar.MONTH);
        int year = calendar.get(Calendar.YEAR);


        DatePickerDialog dialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                String date = dayOfMonth + "/" + (month + 1 ) + "/" + year;
                st_dob.setText(date);
            }
        }, year, month, day);
        dialog.show();
    }

}