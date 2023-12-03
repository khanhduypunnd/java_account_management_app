package com.example.mid_term_mobile;

import static android.widget.Toast.LENGTH_SHORT;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.ValueEventListener;

public class login extends AppCompatActivity {

    private EditText mail, pass;
    private AppCompatButton signin;
    private DataCallback loginCallback;

    public login(DataCallback loginCallback) {
        this.loginCallback = loginCallback;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mail = findViewById(R.id.login_email);
        pass = findViewById(R.id.login_password);
        signin = findViewById(R.id.btnSignIn);


        login loginActivity = new login(new DataCallback() {
            @Override
            public void onSuccess(User user) {
                // Xử lý khi đăng nhập thành công
                Toast.makeText(login.this, "Success", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(login.this, MainActivity.class);
                intent.putExtra("email", user.getMail());
                intent.putExtra("name", user.getName());
                intent.putExtra("role", user.getRole());
                startActivityForResult(intent, 100);
            }

            @Override
            public void onWrongPassword() {
                // Xử lý khi mật khẩu sai
                Toast.makeText(login.this, "Wrong password", Toast.LENGTH_SHORT).show();
                pass.setText("");
                pass.requestFocus();
            }

            @Override
            public void onUserNotFound() {
                // Xử lý khi người dùng không được tìm thấy
                Toast.makeText(login.this, "User not found", Toast.LENGTH_SHORT).show();
                mail.setText("");
                pass.setText("");
                mail.requestFocus();
            }

            @Override
            public void onError(DatabaseError error) {
                // Xử lý khi có lỗi xảy ra trong database
            }
        });



        signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email, password;
                email = mail.getText().toString();
                password = pass.getText().toString();

                if(email.isEmpty()){
                    Toast.makeText(login.this, "Please enter your email", LENGTH_SHORT).show();
                    mail.requestFocus();
                } else if (password.isEmpty()){
                    Toast.makeText(login.this, "Please enter your pass", LENGTH_SHORT).show();
                    pass.requestFocus();
                } else if (!email.contains("@")){
                    Toast.makeText(login.this, "Please enter a valid email", Toast.LENGTH_SHORT).show();
                    mail.requestFocus();
                }

                FirebaseDatabase database = FirebaseDatabase.getInstance();
                DatabaseReference usersRef = database.getReference("user");
                Query query = usersRef.orderByChild("email").equalTo(email);

                query.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if(snapshot.exists()){
                            for(DataSnapshot snap : snapshot.getChildren()){
                                User user = snap.getValue(User.class);
                                if(user.getPass().toString().equals(password)){
                                    loginCallback.onSuccess(user);
                                }
                                else{
                                    loginCallback.onWrongPassword();
                                }
                            }
                        }
                        else {
                            loginCallback.onUserNotFound();
                        };
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        loginCallback.onError(error);
                    }
                });

            }
        });
    }


}