package com.derteuffel.infoenspm;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity {

           private TextView registerLink;
           private Button loginBtn;
           private EditText loginPassText;
           private EditText loginEmailText;
            private ProgressBar loginPgb;


            private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);


        registerLink=(TextView) findViewById(R.id.reg_link);
        loginBtn=(Button) findViewById(R.id.login_btn);
        loginPassText=(EditText) findViewById(R.id.loginPass);
        loginEmailText=(EditText) findViewById(R.id.loginEmail);
        loginPgb=(ProgressBar) findViewById(R.id.login_pgb);


        mAuth=FirebaseAuth.getInstance();


        registerLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendToRegister();
            }
        });

        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                 String email= loginEmailText.getText().toString();
                String password= loginPassText.getText().toString();

                if (!TextUtils.isEmpty(email) && !TextUtils.isEmpty(password)){

                    loginPgb.setVisibility(View.VISIBLE);

                    mAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()){

                            sendToMain();
                        }else {
                            String error= task.getException().getMessage();
                            Toast.makeText(LoginActivity.this, "Error:"+ error, Toast.LENGTH_LONG).show();
                        }

                            loginPgb.setVisibility(View.INVISIBLE);
                        }
                    });
                }
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();

        FirebaseUser currentUser= mAuth.getCurrentUser();

        if (currentUser!=null){
            sendToMain();
        }

    }

    private void sendToMain() {
        Intent mainItent= new Intent(LoginActivity.this, MainActivity.class);
        startActivity(mainItent);
        finish();
    }

    private void sendToRegister() {
        Intent registerItent= new Intent(LoginActivity.this, RegisterActivity.class);
        startActivity(registerItent);
        finish();
    }
}
