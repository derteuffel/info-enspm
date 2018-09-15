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

public class RegisterActivity extends AppCompatActivity {

    private TextView _login_link;
    private Button _reg_btn;
    private EditText _reg_pass;
    private EditText _confirm_reg_pass;
    private EditText _reg_email;
    private ProgressBar _reg_pgb;

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        _login_link=findViewById(R.id.login_link);
        _reg_btn=findViewById(R.id.reg_btn);
        _reg_pass=findViewById(R.id.reg_pass);
        _confirm_reg_pass=findViewById(R.id.confirm_reg_pass);
        _reg_email=findViewById(R.id.reg_email);
        _reg_pgb=findViewById(R.id.reg_pgb);

        mAuth=FirebaseAuth.getInstance();


        _login_link.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendToLogin();
            }
        });

        _reg_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email= _reg_email.getText().toString();
                String pass= _reg_pass.getText().toString();
                String confirm_pass= _confirm_reg_pass.getText().toString();

                if (!TextUtils.isEmpty(email) && !TextUtils.isEmpty(pass) & !TextUtils.isEmpty(confirm_pass)){

                    if (pass.equals(confirm_pass)){

                        _reg_pgb.setVisibility(View.VISIBLE);

                        mAuth.createUserWithEmailAndPassword(email,pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {

                                if (task.isSuccessful()){

                                    sendToMain();
                                }else {
                                    String errorMessage=task.getException().getMessage();
                                    Toast.makeText(RegisterActivity.this, "Error:"+errorMessage, Toast.LENGTH_LONG).show();
                                }

                                _reg_pgb.setVisibility(View.INVISIBLE);
                            }

                        });
                    }else {
                        Toast.makeText(RegisterActivity.this, "les champs Mot de passe et confirmer Mot de passe dovent etre identique", Toast.LENGTH_LONG).show();
                    }
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
        Intent mainIntent= new Intent(RegisterActivity.this, MainActivity.class);
        startActivity(mainIntent);
        finish();
    }

    private void sendToLogin() {
        Intent loginIntent= new Intent(RegisterActivity.this, LoginActivity.class);
        startActivity(loginIntent);
        finish();
    }
}
