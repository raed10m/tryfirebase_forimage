package com.assadh.tryfirebase_forimage;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;

public class activity_login extends AppCompatActivity {
    public  static  final String log="My_Application";
    boolean sinin=true;
    EditText username,useremail,userpassword;
    TextView txView;
    ProgressBar progressLoding;
    Button login;
    Uri uriimg;
    ImageView imgprofile;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        username=findViewById(R.id.UserName);
        useremail=findViewById(R.id.UserEmail);
        userpassword=findViewById(R.id.password);
        txView=findViewById(R.id.textView2);
        login=findViewById(R.id.login);
        progressLoding=findViewById(R.id.loading);
//        utils.mAuth= FirebaseAuth.getInstance();
        imgprofile=findViewById(R.id.imageprofile);
        imgprofile.setOnClickListener(view -> {
            Intent img=new Intent();
            img.setType("image/*");
            img.setAction(Intent.ACTION_GET_CONTENT);
            startActivityForResult(Intent.createChooser(img,"choose image"),200);
        });

        setsignin();

        login.setOnClickListener(Vew->{

            if(sinin)
                onLogin();
            else {onsignup();
//                if (uriimg!=null)onsignup();
//                else
//                    Toast.makeText(this,"plese check image profile",Toast.LENGTH_LONG).show();
            }
        });

        txView.setOnClickListener(view -> {
            if(sinin){
                setsignup();
            }
            else setsignin();
            sinin=!sinin;
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==200){
            uriimg=data.getData();
            try {
               Bitmap b = MediaStore.Images.Media.getBitmap(getContentResolver(),uriimg);
                imgprofile.setImageBitmap(b);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void setsignin() {
        username.setEnabled(false);
        username.setVisibility(View.GONE);
        txView.setText("Creat new Account?Sign Up");
    }
    private  void  setsignup(){
        username.setEnabled(true);
        username.setVisibility(View.VISIBLE);
        txView.setText("old Account?Sign in");
    }

    void uploadeimage(){
        StorageReference imgref= FirebaseStorage.getInstance().getReference("imageProfile/"+username.getText().toString()+System.currentTimeMillis()+".jpg");
        if(uriimg!=null){

            UploadTask uploadTask=imgref.putFile(uriimg);

            Task<Uri> urltask=uploadTask.continueWithTask(task ->{

                if(!task.isSuccessful()){
                    throw  task.getException();
//                    uploadeimage();
                }

                return imgref.getDownloadUrl();
            }).addOnCompleteListener(task -> {
                if(task.isSuccessful()) {

                    Toast.makeText(this,"success image",Toast.LENGTH_LONG).show();
                    utils.uriImginStorage = task.getResult().toString();

                }
            });
        }
    }

    private void updatedatauser(){
        String name=username.getText().toString();
//        uploadeimage();
        UserProfileChangeRequest userrequest=new UserProfileChangeRequest
                .Builder().setDisplayName(name).build();
        utils.user.updateProfile(userrequest).addOnCompleteListener(task1 -> {
if (task1.isSuccessful()){
            Toast.makeText(this, "successfull", Toast.LENGTH_SHORT).show();
           }
        });
    }


    private void onsignup() {
        String name=username.getText().toString();
        String email=useremail.getText().toString();
        String pass=userpassword.getText().toString();

        if(name.isEmpty()&&email.isEmpty()&&pass.isEmpty()){

        }
        else{
                        progressLoding.setVisibility(View.VISIBLE);
           utils.mAuth.createUserWithEmailAndPassword(email,pass).addOnSuccessListener(authResult -> {
                utils.user=utils.mAuth.getCurrentUser();
                if(utils.user!=null){
                   updatedatauser();
                    Intent intent=new Intent();
                    setResult(RESULT_OK);
                    finish();
                }
                else
                    Toast.makeText(this,"error in sinup",Toast.LENGTH_SHORT).show();

            }).addOnFailureListener(e -> {
                Toast.makeText(this,"error create"+e.getMessage(),Toast.LENGTH_SHORT).show();
            });

        }
    }
    private  void  onLogin(){
        String email=useremail.getText().toString();
        String pass=userpassword.getText().toString();
        utils.mAuth.signInWithEmailAndPassword(email,pass).addOnCompleteListener(task -> {
            if(task.isSuccessful()){
                utils.user=utils.mAuth.getCurrentUser();
                Toast.makeText(this,"successfull"+utils.user.getDisplayName(),Toast.LENGTH_LONG).show();
                Intent intent=new Intent();
                setResult(RESULT_OK);
                finish();

            }else{
                Toast.makeText(this,"error in login ",Toast.LENGTH_LONG).show();
            }
        });
    }
}