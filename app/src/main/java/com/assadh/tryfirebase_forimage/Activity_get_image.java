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
import android.widget.Toast;

import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;
import java.util.Date;

public class Activity_get_image extends AppCompatActivity {
    ImageView im;
    Bitmap b= null;
    Uri uriimg;
    String uriImginStorage;
    ProgressBar progressBar;
    EditText description;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_image);
        utils.FireStor= FirebaseFirestore.getInstance();
        Button btn=findViewById(R.id.button);
        im=findViewById(R.id.imageView2);
        progressBar=findViewById(R.id.progressBar);
        FloatingActionButton btn_addimg=findViewById(R.id.floatingActionButton2);
        description=findViewById(R.id.description);

        btn_addimg.setOnClickListener( view -> {
//           // Intent img=new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//            GetGallary();
//           // startActivityForResult(img,101);

            Intent intent=getIntent();
            String check=intent.getStringExtra("fromcamera");
//            if(check=="0")Getcamera();
//            else GetGallary();
            GetGallary();
        });


        btn.setOnClickListener(view -> {

            Uploadeimg();
            // uploadImageData();
        });
    }
    private void Getcamera(){
        Intent img=new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(img,104);
    }
    private void GetGallary(){
        Intent img=new Intent();
        img.setType("image/*");
        img.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(img,"choose image"),119);
    }
    private  void Uploadeimg(){
        StorageReference imgref= FirebaseStorage.getInstance().getReference("images/"+System.currentTimeMillis()+".jpg");
        if(uriimg!=null){

            UploadTask uploadTask=imgref.putFile(uriimg);
            progressBar.setVisibility(View.VISIBLE);
            Task<Uri> urltask=uploadTask.continueWithTask(task ->{

                if(!task.isSuccessful()) throw  task.getException();
                return imgref.getDownloadUrl();
            }).addOnCompleteListener(task -> {
                if(task.isSuccessful()) {
                    Toast.makeText(this,"success image",Toast.LENGTH_LONG).show();
                    uriImginStorage = task.getResult().toString();
                    uploadImageData();
                }
            });
        }

    }

    void uploadImageData(){
        String txt=description.getText().toString();
        if(utils.user!=null&&!uriImginStorage.isEmpty()&&!txt.isEmpty()) {
            Toast.makeText(this,"run",Toast.LENGTH_LONG).show();
//            utils.user= FirebaseAuth.getInstance().getCurrentUser();
            Postdata pd=new Postdata(utils.user. getDisplayName(),uriImginStorage,txt,new Timestamp(new Date()));
            utils.FireStor=FirebaseFirestore.getInstance();
            utils.FireStor.collection("Post").add(pd).addOnSuccessListener(documentReference -> {
                progressBar.setVisibility(View.GONE);
                Toast.makeText(this,"sucssesfull",Toast.LENGTH_LONG).show();

            }).addOnFailureListener(e -> {
                Toast.makeText(this,"error in image data",Toast.LENGTH_LONG).show();
        uploadImageData();
            });

//            Postdata post=new Postdata(utils.user.getDisplayName(),"#",txt,new Timestamp(new Date()),b);
            Intent intent=new Intent();
            try {
                finish();
            }catch (Exception e){
                Toast.makeText(this,""+e.getMessage(),Toast.LENGTH_LONG).show();
            }
        }


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode==119){
            uriimg=data.getData();
            try {
                b = MediaStore.Images.Media.getBitmap(getContentResolver(),uriimg);
                im.setImageBitmap(b);
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
        else if(requestCode==104){
            try {
                uriimg = data.getData();
                b = (Bitmap) data.getExtras().get("data");
            }catch (Exception e){
                Toast.makeText(this,""+e.getMessage(),Toast.LENGTH_LONG).show();
                finish();
            }
        }
    }
}