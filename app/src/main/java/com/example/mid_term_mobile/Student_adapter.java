package com.example.mid_term_mobile;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class Student_adapter extends RecyclerView.Adapter<Student_adapter.ViewHolder> {

    private Context context;
    private ArrayList<Student> data;
    private Uri link_avatar;
    private String role;


    public Student_adapter(Context context, ArrayList<Student> students){
        this.context = context;
        this.data = students;
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.student_layout_row, parent, false);
        return new ViewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        Student student = data.get(position);

        if(data == null){
            return;
        }

        Picasso.get().load(student.getAvatar()).into(holder.avatar);
        holder.name.setText(data.get(position).getName());
        holder.st_class.setText(data.get(position).getSt_class());
        holder.email.setText(data.get(position).getEmail());




        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(role.equals("employee")){
                    Toast.makeText(context,"Your role doesn't had this function", Toast.LENGTH_SHORT).show();
                }else{
                    Intent intent = new Intent(context, edit_student_layout.class);
                    intent.putExtra("email", data.get(position).getEmail());
                    // Start the activity
                    context.startActivity(intent);
                }
            }
        });



        holder.remove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Student student = data.get(position);
                if(student.getEmail() != null){
                    FirebaseDatabase database = FirebaseDatabase.getInstance();
                    DatabaseReference databaseReference = database.getReference("student");

                    databaseReference.child(student.getStudentID()).removeValue();

                    data.remove(position);

                    notifyItemRemoved(position);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        if(data != null){
            return  data.size();
        }
        return 0;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        CircleImageView avatar;
        TextView name, st_class, email;
        ImageButton remove;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            avatar = itemView.findViewById(R.id.layoutStudent_avatar);
            name = itemView.findViewById(R.id.layoutStudent_name);
            st_class = itemView.findViewById(R.id.layoutStudent_class);
            email = itemView.findViewById(R.id.layoutStudent_email);
            remove = itemView.findViewById(R.id.layoutStudent_remove);
        }
    }
}
