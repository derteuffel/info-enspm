package com.derteuffel.infoenspm;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;


import de.hdodenhof.circleimageview.CircleImageView;

public class DetailPostActivity extends AppCompatActivity {


    private CircleImageView _user_post_image;
    private TextView _date_public;
    private TextView _user_post_name;
    private TextView _post_title_details;
    private TextView _post_desc_detail;


    private FirebaseAuth mAuth;
   // private FirebaseFirestore firebaseFirestore;

    private String current_user_id;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_post);

        _user_post_image=findViewById(R.id.user_post_image);
        _date_public=findViewById(R.id.date_public);
        _user_post_name=findViewById(R.id.user_post_name);
        _post_title_details=findViewById(R.id.post_title_details);
        _post_desc_detail=findViewById(R.id.post_desc_detail);

    }
}
