package com.derteuffel.infoenspm;

import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.miguelcatalan.materialsearchview.MaterialSearchView;
import com.theartofdev.edmodo.cropper.CropImage;

import de.hdodenhof.circleimageview.CircleImageView;

public class MainActivity extends AppCompatActivity {

    private FrameLayout _main_frame;
    private Toolbar _main_btm_nav;
    private CircleImageView mainImage;
    private TextView userName;
    private TextView userFiliere;
    private Uri mainImageUri=null;

    private MaterialSearchView searchView;

    private HomeFragment homeFragment;
    private NotificationFragment notificationFragment;

    private FirebaseAuth mAuth;
    private FirebaseFirestore firebaseFirestore;
    private String user_id;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        _main_btm_nav=findViewById(R.id.main_btm_nav);
        _main_frame=findViewById(R.id.main_frame);

        mainImage=findViewById(R.id.connected_user_image);
        userName=findViewById(R.id.connected_user_name);
        userFiliere=findViewById(R.id.connected_filiere);

        homeFragment=new HomeFragment();
        notificationFragment =new NotificationFragment();

        setSupportActionBar(_main_btm_nav);
        getSupportActionBar().setTitle("Actualités!!!");

        searchView=findViewById(R.id.search_nav);

        mAuth=FirebaseAuth.getInstance();
        firebaseFirestore=FirebaseFirestore.getInstance();
        user_id= mAuth.getCurrentUser().getUid();



        firebaseFirestore.collection("Users").document(user_id).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {

                if (task.isSuccessful()){

                    if (task.getResult().exists()){
                        String name=task.getResult().getString("name");
                        String filiere=task.getResult().getString("filiere");
                        String image= task.getResult().getString("image");

                        mainImageUri = Uri.parse(image);

                        userName.setText(name);
                        userFiliere.setText(filiere);

                        RequestOptions placeHolderRequest= new RequestOptions();
                        placeHolderRequest.placeholder(R.drawable.ic_launcher_background);
                        Glide.with(MainActivity.this).applyDefaultRequestOptions(placeHolderRequest).load(image).into(mainImage);
                    }
                }else {
                    String error=task.getException().getMessage();
                    Toast.makeText(MainActivity.this, "Firestore:"+ error, Toast.LENGTH_LONG).show();
                }
            }
        });

        setFragment(homeFragment);
        _main_btm_nav.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch(item.getItemId()){
                    case R.id.search_menu:
                        searchView.setMenuItem(item);
                        return true;
                    case R.id.refresh_menu:
                        Toast.makeText(MainActivity.this, "refresh item are Clicked", Toast.LENGTH_LONG).show();
                        return true;
                    case R.id.logout_menu:
                        Toast.makeText(MainActivity.this, "you will be logged out!!!", Toast.LENGTH_LONG).show();
                        logout();
                        return true;
                    case R.id.notification_menu:
                        setFragment(notificationFragment);
                        return true;
                    case R.id.profil_menu:
                        sendToProfile();
                        return true;

                        default:
                            return false;
                }
            }
        });
    }

    private void logout() {
        mAuth.signOut();
        sendToLogin();
    }

    private void sendToLogin() {
        Intent loginIntent= new Intent(MainActivity.this, LoginActivity.class);
        startActivity(loginIntent);
        finish();
    }

    private void sendToProfile() {
        Intent profileIntent= new Intent(MainActivity.this, ProfileActivity.class);
        startActivity(profileIntent);
        finish();
    }


    public boolean onCreateOptionsMenu (Menu menu){
        getMenuInflater().inflate(R.menu.main_menu, menu);

        return true;
    }



    private void setFragment(Fragment fragment) {
        FragmentTransaction fragmentTransaction= getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.main_frame, fragment);
        fragmentTransaction.commit();

    }

    @Override
    protected void onStart() {

        super.onStart();

        FirebaseUser currentUser =mAuth.getCurrentUser();


        if (currentUser==null){
            sendToStart();
        }


    }

    private void sendToStart() {
        Intent startIntent= new Intent(MainActivity.this, StartActivity.class);
        startActivity(startIntent);
        finish();
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){

        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode== CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE){
            CropImage.ActivityResult result= CropImage.getActivityResult(data);
            if (resultCode==RESULT_OK){
                mainImageUri= result.getUri();
                mainImage.setImageURI(mainImageUri);
                // isChanged= true;

            }else if (resultCode==CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE){
                Exception error= result.getError();
            }
        }
    }


}
