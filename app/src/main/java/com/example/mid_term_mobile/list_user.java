package com.example.mid_term_mobile;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class list_user extends AppCompatActivity {
    private ImageButton back;
    private RecyclerView listUsers;
    private User_Adapter mUser;
    private ArrayList<User> mData = new ArrayList<>();

    private FirebaseDatabase database;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_user_view);

        initView();


        mUser = new User_Adapter(this, mData);
        listUsers.setAdapter(mUser);
        listUsers.setLayoutManager(new LinearLayoutManager(this));



        initData();

        back_home();
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
        DatabaseReference usersRef = database.getReference("user");

        usersRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot snapshot1 : snapshot.getChildren()){

                    String ID = snapshot1.getKey();
                    String name = snapshot1.child("name").getValue(String.class);
                    String email = snapshot1.child("mail").getValue(String.class);
                    String role = snapshot1.child("role").getValue(String.class);
                    String status = snapshot1.child("status").getValue(String.class);
                    String avatar = snapshot1.child("avatar").getValue(String.class);
                    String phone = snapshot1.child("phone").getValue(String.class);
                    String pass = snapshot1.child("pass").getValue(String.class);
                    int age = snapshot1.child("age").getValue(int.class);

                    mData.add(new User(avatar,name, email, pass,role, status, phone, ID, age));
                    mUser.notifyDataSetChanged();
                }
            }


            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }

    private void initView() {
        listUsers = findViewById(R.id.listUser_recycler);
        back = findViewById(R.id.listUser_back);
    }




}
