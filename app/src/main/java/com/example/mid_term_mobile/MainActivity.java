package com.example.mid_term_mobile;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                // Tạo Intent để chuyển từ MainActivity sang LoginActivity
                Intent intent = new Intent(MainActivity.this, login.class);

                // Bắt đầu LoginActivity
                startActivityForResult(intent, 50000);

                // Kết thúc MainActivity
                finish();
            }
        }, 2000); // 2000 milliseconds = 2 giây
    }
}