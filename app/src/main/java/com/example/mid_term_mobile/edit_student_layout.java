package com.example.mid_term_mobile;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

public class edit_student_layout extends AppCompatActivity {

    private CircleImageView avatar;

    private TextView name, email, DOB, st_class;
    private EditText phone;
    private AppCompatButton save;
    static final int PICK_IMAGE_REQUEST = 1;
    private Uri link_avatar;

    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private StorageReference storage_avatar;

    private String key;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_student_layout);


        initUI();
        setAvatar();


        Intent intent = getIntent();
        key = intent.getStringExtra("email");

        //getAvatar(key, avatar);

        fillInfor(key);

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatabaseReference usersRef = database.getReference("student");

                String  phone1;


                phone1 = phone.getText().toString();


                usersRef.orderByChild("email").equalTo(key).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if(snapshot.exists()){
                            for(DataSnapshot snap : snapshot.getChildren()){
                                Student student = snap.getValue(Student.class);

                                String id = student.getStudentID();
                                snap.child("phone").getRef().setValue(phone1);


                                Toast.makeText(edit_student_layout.this, "Success", Toast.LENGTH_SHORT).show();

                                finish();

                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            };
        });
    }

    private void fillInfor(String key) {

        DatabaseReference usersRef = database.getReference("student");
        Query query = usersRef.orderByChild("email").equalTo(key);

        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    for(DataSnapshot snap : snapshot.getChildren()){
                        Student student = snap.getValue(Student.class);
                        if(student.getEmail().toString().equals(key)){
                            Picasso.get().load(student.getAvatar()).into(avatar);
                            name.setText(student.getName());
                            DOB.setText(student.getAge());
                            st_class.setText(student.getSt_class());
                            email.setText(student.getEmail());
                            phone.setText(student.getPhone());
                        }
                    }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void getAvatar(String key, CircleImageView avatar) {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference usersRef = database.getReference("student");
        Query query = usersRef.orderByChild("email").equalTo(key);
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

    private void setAvatar() {

    }

    private void initUI() {
        avatar = findViewById(R.id.editStudent_avatar);
        name = findViewById(R.id.editStudent_name);
        DOB = findViewById(R.id.editStudent_dob);
        st_class = findViewById(R.id.editStudent_class);
        email = findViewById(R.id.editStudent_email);
        phone = findViewById(R.id.editStudent_phone);
        save = findViewById(R.id.editStudent_save);
    }
}