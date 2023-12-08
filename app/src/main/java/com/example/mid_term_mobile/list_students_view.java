package com.example.mid_term_mobile;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class list_students_view extends AppCompatActivity {
    private ImageButton back;
    private RecyclerView listStudents;
    private Student_adapter mStudents;
    private ArrayList<Student> mData = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_students_view);

        initView();


        mStudents = new Student_adapter(this, mData);
        listStudents.setAdapter(mStudents);
        listStudents.setLayoutManager(new LinearLayoutManager(this));


        initData();

        //back_home();
    }

    private void back_home() {
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void initData() {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference usersRef = database.getReference("student");

        usersRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot snapshot1 : snapshot.getChildren()){

                    String ID = snapshot1.getKey();
                    String name = snapshot1.child("name").getValue(String.class);
                    String email = snapshot1.child("email").getValue(String.class);
                    String avatar = snapshot1.child("avatar").getValue(String.class);
                    String phone = snapshot1.child("phone").getValue(String.class);
                    String st_class = snapshot1.child("st_class").getValue(String.class);
                    String age = snapshot1.child("age").getValue(String.class);

                    mData.add(new Student( ID,age,avatar, st_class, email, name, phone));
                    mStudents.notifyDataSetChanged();
                }
            }


            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void initView() {
        listStudents = findViewById(R.id.listStudent_recycler);
        back = findViewById(R.id.listUser_back);
    }
}