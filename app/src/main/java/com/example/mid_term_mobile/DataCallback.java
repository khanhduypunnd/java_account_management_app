package com.example.mid_term_mobile;

import com.google.firebase.database.DatabaseError;

public interface DataCallback{
    void onSuccess(User user);
    void onWrongPassword();
    void onUserNotFound();
    void onError(DatabaseError error);
}
