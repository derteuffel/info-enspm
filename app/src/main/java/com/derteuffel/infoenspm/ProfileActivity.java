package com.derteuffel.infoenspm;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.gms.tasks.OnCanceledListener;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.net.URI;
import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

import static android.Manifest.permission.READ_EXTERNAL_STORAGE;

public class ProfileActivity extends AppCompatActivity {

    private Toolbar _profile_tlb;
    private CircleImageView profileImage;
    private EditText profileName;
    private EditText profileFirstName;
    private EditText profilePhone;
    private EditText profileFiliere;
    private EditText profileQuartier;
    private Uri mainImageUri=null;
    private ProgressBar profileProgress;
    private FloatingActionButton profileValid;

    private String user_id;


    private FirebaseAuth mAuth;
    private StorageReference storageReference;
    private FirebaseFirestore firebaseFirestore;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);


        _profile_tlb=findViewById(R.id.profile_tlb);
        profileImage=findViewById(R.id.profile_image_setup);
        profileName=findViewById(R.id.user_profile_name);
        profileFirstName=findViewById(R.id.user_profile_first_name);
        profilePhone=findViewById(R.id.user_profile_phone);
        profileFiliere=findViewById(R.id.user_profile_filiere);
        profileQuartier=findViewById(R.id.user_profile_quartier);
        profileProgress=findViewById(R.id.profile_pgb);
        profileValid=findViewById(R.id.fat_profile_submit);


        mAuth=FirebaseAuth.getInstance();
        storageReference= FirebaseStorage.getInstance().getReference();
        firebaseFirestore=FirebaseFirestore.getInstance();
        user_id= mAuth.getCurrentUser().getUid();

profileProgress.setVisibility(View.VISIBLE);
// in this part we retrieve data in firebaseFirestore and show it as default
        firebaseFirestore.collection("Users").document(user_id).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {

                if (task.isSuccessful()){

                if (task.getResult().exists()){
                    String name=task.getResult().getString("name");
                    String firstName=task.getResult().getString("firstName");
                    String phone= task.getResult().getString("phone");
                    String filiere=task.getResult().getString("filiere");
                    String quartier= task.getResult().getString("quartier");
                    String image= task.getResult().getString("image");

                    Log.e("image", image);

                    mainImageUri = Uri.parse(image);

                    profileName.setText(name);
                    profileFirstName.setText(firstName);
                    profilePhone.setText(phone);
                    profileFiliere.setText(filiere);
                    profileQuartier.setText(quartier);

                    RequestOptions placeHolderRequest= new RequestOptions();
                    placeHolderRequest.placeholder(R.drawable.ic_launcher_background);

                    Glide.with(ProfileActivity.this).setDefaultRequestOptions(placeHolderRequest).load(image).into(profileImage);

                }

                }else {
                    String error=task.getException().getMessage();
                    Toast.makeText(ProfileActivity.this, "Firestore:"+ error, Toast.LENGTH_LONG).show();
                }

                profileProgress.setVisibility(View.INVISIBLE);
            }
        });

        profileImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN){
                    if (ContextCompat.checkSelfPermission(ProfileActivity.this, READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
                        Toast.makeText(ProfileActivity.this,"Permission Denied", Toast.LENGTH_LONG).show();
                        ActivityCompat.requestPermissions(ProfileActivity.this, new String[]{READ_EXTERNAL_STORAGE},1);
                    }else {
                        Toast.makeText(ProfileActivity.this, "you already have permission", Toast.LENGTH_LONG).show();
                        bringImagePicker();
                    }
                }else {
                    bringImagePicker();
                }
            }
        });


        setSupportActionBar(_profile_tlb);
        getSupportActionBar().setTitle("Mise à jour du profile");


// here we put data in firebase storage when you click the valid button
        profileValid.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                        final String user_name=profileName.getText().toString();
                        final String user_first_name=profileFirstName.getText().toString();
                        final String user_phone=profilePhone.getText().toString();
                        final String user_filiere=profileFiliere.getText().toString();
                        final String user_quartier= profileQuartier.getText().toString();

                        if (!TextUtils.isEmpty(user_name) && !TextUtils.isEmpty(user_filiere) && mainImageUri!= null){

                            //get the user connected id

                             user_id=mAuth.getCurrentUser().getUid();

                            profileProgress.setVisibility(View.VISIBLE);

                            //set the folder where the images will be saved

                            StorageReference image_path=storageReference.child("profile_images").child(user_id+".jpg");



                           /* image_path.putFile(mainImageUri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {

                                    if (task.isSuccessful()){

                                        Uri download_uri=task.getResult().getUploadSessionUri();

                                        Map<String, String> userMap= new HashMap<>();
                                        userMap.put("name",user_name);
                                        userMap.put("firstName",user_first_name);
                                        userMap.put("phone", user_phone);
                                        userMap.put("filiere", user_filiere);
                                        userMap.put("quartier", user_quartier);
                                        userMap.put("image", download_uri.toString());

                                        firebaseFirestore.collection("Users").document(user_id).set(userMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if (task.isSuccessful()){

                                                    Toast.makeText(ProfileActivity.this, "your profile are updated succesffuly", Toast.LENGTH_LONG).show();
                                                    sendToMain();
                                                }else {
                                                    String error=task.getException().getMessage();
                                                    Toast.makeText(ProfileActivity.this, "Firestore:"+ error, Toast.LENGTH_LONG).show();
                                                }
                                            }
                                        });
                                    }else {

                                        String error=task.getException().getMessage();
                                        Toast.makeText(ProfileActivity.this, "Error:"+error, Toast.LENGTH_LONG).show();
                                    }

                                    profileProgress.setVisibility(View.INVISIBLE);
                                }
                            });*/



                           image_path.putFile(mainImageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                               @Override
                               public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                   Task<Uri> urlTask=taskSnapshot.getStorage().getDownloadUrl();

                                   while (!urlTask.isSuccessful());
                                   Uri download_uri=urlTask.getResult();


                                   Map<String, String> userMap= new HashMap<>();
                                   userMap.put("name",user_name);
                                   userMap.put("firstName",user_first_name);
                                   userMap.put("phone", user_phone);
                                   userMap.put("filiere", user_filiere);
                                   userMap.put("quartier", user_quartier);
                                   userMap.put("image", download_uri.toString());

                                   firebaseFirestore.collection("Users").document(user_id).set(userMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                                       @Override
                                       public void onComplete(@NonNull Task<Void> task) {
                                           if (task.isSuccessful()){

                                               Toast.makeText(ProfileActivity.this, "your profile are updated succesffuly", Toast.LENGTH_LONG).show();
                                               sendToMain();
                                           }else {
                                               String error=task.getException().getMessage();
                                               Toast.makeText(ProfileActivity.this, "Firestore:"+ error, Toast.LENGTH_LONG).show();
                                           }
                                       }
                                   });
                                   profileProgress.setVisibility(View.INVISIBLE);
                               }
                           });
                        }
                        Toast.makeText(ProfileActivity.this, "your will save this profile, are you sure to wan it??", Toast.LENGTH_LONG).show();


            }
        });
    }


    private void sendToMain() {
        Intent mainIntent= new Intent(ProfileActivity.this, MainActivity.class);
        startActivity(mainIntent);
        finish();
    }

    private void bringImagePicker() {
        CropImage.activity()
                .setGuidelines(CropImageView.Guidelines.ON)
                .setAspectRatio(1,1)
                .start(ProfileActivity.this);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){

        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode==CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE){
            CropImage.ActivityResult result= CropImage.getActivityResult(data);
            if (resultCode==RESULT_OK){
                mainImageUri= result.getUri();
                profileImage.setImageURI(mainImageUri);
               // isChanged= true;

            }else if (resultCode==CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE){
                Exception error= result.getError();
            }
        }
    }


}
