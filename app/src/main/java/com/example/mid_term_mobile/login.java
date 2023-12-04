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

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class login extends AppCompatActivity {

    private EditText mail, pass;
    private AppCompatButton signin;

    private FirebaseDatabase database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mail = findViewById(R.id.login_email);
        pass = findViewById(R.id.login_password);
        signin = findViewById(R.id.btnSignIn);



        database = FirebaseDatabase.getInstance();


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
                }else{
                    connectToFirebase(email,password);
                }


                //Executor executor = Executors.newFixedThreadPool(3);

                /*executor.execute(new Runnable() {
                    @Override
                    public void run() {
                        connectToFirebase(email,password);
                    }
                });*/
                //executor.notifyAll();
            }
        });
    }

    private void connectToFirebase(String email,String password)
    {

        DatabaseReference usersRef = database.getReference("user");
        Query query = usersRef.orderByChild("mail").equalTo(email);



        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    for(DataSnapshot snap : snapshot.getChildren()){
                        User user = snap.getValue(User.class);
                        if(user.getPass().toString().equals(password)){
                            Toast.makeText(login.this, "Success", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(login.this, activity_main_system.class);
                            intent.putExtra("email", user.getMail());
                            intent.putExtra("name", user.getName());
                            intent.putExtra("role", user.getRole());
                            startActivity(intent);
                            //startActivityForResult(intent, 100);
                        }
                        else{
                            Toast.makeText(login.this, "Wrong password", Toast.LENGTH_SHORT).show();
                            pass.setText("");
                            pass.requestFocus();
                        }
                    }
                }
                else {
                    Toast.makeText(login.this, "User not found", Toast.LENGTH_SHORT).show();
                    mail.setText("");
                    pass.setText("");
                    mail.requestFocus();
                };
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

}