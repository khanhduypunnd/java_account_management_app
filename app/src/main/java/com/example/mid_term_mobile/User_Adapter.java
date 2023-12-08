package com.example.mid_term_mobile;

import static androidx.core.content.ContextCompat.startActivity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class User_Adapter extends RecyclerView.Adapter<User_Adapter.ViewHolder> {

    private Context context;
    private ArrayList<User> data;
    private Uri link_avatar;

    private String UserID;

    /*public User_Adapter(List<User> data){
        this.data = data;
    }*/

    public User_Adapter(Context context, ArrayList<User> users){
        this.context = context;
        this.data = users;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.user_layout_row, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        User user = data.get(position);

        if(data == null){
            return;
        }

        Picasso.get().load(user.getAvatar()).into(holder.avatar);
        holder.name.setText(data.get(position).getName());
        holder.status.setText(data.get(position).getStatus());
        holder.role.setText(data.get(position).getRole());
        //holder.email.setText(data.get(position).getMail());
        //holder.pass.setText(data.get(position).getPass());
        //holder.phone.setText(data.get(position).getPhone());
        //holder.age.setText(String.valueOf(data.get(position).getAge()));


        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, edit_user_layout.class);
                intent.putExtra("email", data.get(position).getMail());
                // Start the activity
                context.startActivity(intent);
            }
        });

        holder.remove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                User user = data.get(position);
                if(user.getMail() != null){
                    FirebaseDatabase database = FirebaseDatabase.getInstance();
                    DatabaseReference databaseReference = database.getReference("user");

                    databaseReference.child(user.getUserID()).removeValue();

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


    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView avatar;
        TextView name, status, role, email, pass, phone, age;

        ImageButton remove;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            avatar = itemView.findViewById(R.id.layoutUser_avatar);
            name = itemView.findViewById(R.id.layoutUser_name);
            status = itemView.findViewById(R.id.layoutUser_status);
            role = itemView.findViewById(R.id.layoutUser_role);
            remove = itemView.findViewById(R.id.editUser_remove);
            //email = itemView.findViewById(R.id.layoutUser_email);
            //pass = itemView.findViewById(R.id.layoutUser_pass);
            //phone = itemView.findViewById(R.id.layoutUser_phone);
            // = itemView.findViewById(R.id.layoutUser_age);
        }
    }

}
