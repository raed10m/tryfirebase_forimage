package com.assadh.tryfirebase_forimage;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    FloatingActionButton btn_add;
    ListView list;
    listresource listadapter;
    ArrayList<Postdata> arrayList;
    ProgressBar br;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        br=findViewById(R.id.progressBar3);
        list=findViewById(R.id.list_post);
        arrayList=new ArrayList<>();
        listadapter=new listresource(this,arrayList);
        list.setAdapter(listadapter);
        btn_add=findViewById(R.id.floatingActionButton2);
        btn_add.setOnClickListener(view -> {

            Intent intent=new Intent(getBaseContext(),Activity_get_image.class);
            intent.putExtra("fromcamera","1");
            startActivity(intent);
            //startActivityForResult(intent,102);

        });



        utils.mAuth= FirebaseAuth.getInstance();

    }

    @Override
    protected void onStart() {
        super.onStart();
        utils.user=utils.mAuth.getCurrentUser();
        if(utils.user==null) {
            Intent intent = new Intent(getBaseContext(), activity_login.class);
            startActivityForResult(intent,105);
        }
        else {
            task_back task = new task_back();
            task.execute("sd");
        }
    }
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==105){
            try {

                task_back task = new task_back();
                task.execute("sd");
            }catch (Exception e){
                Toast.makeText(this,""+e.getMessage(),Toast.LENGTH_LONG).show();
            }
        }
    }
    class task_back extends AsyncTask<String , Map<String ,Object>,Void> {
        boolean empty=true;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            arrayList.clear();
           listadapter.notifyDataSetChanged();
            // Toast.makeText(getBaseContext(),"go",Toast.LENGTH_LONG).show();
            br.setVisibility(View.VISIBLE);

        }

        @Override
        protected void onPostExecute(Void unused) {
            super.onPostExecute(unused);
            if (!empty)
            Toast.makeText(getBaseContext(),"nothing post",Toast.LENGTH_LONG).show();
            br.setVisibility(View.GONE);
            utils.user=utils.mAuth.getCurrentUser();

        }

        @Override
        protected void onProgressUpdate(Map<String, Object>... values) {
            super.onProgressUpdate(values);

            try {
                Postdata data = new Postdata(values[0].get("username").toString(), values[0].get("uri").toString(), values[0].get("description").toString(), (Timestamp) values[0].get("date"));
                arrayList.add(data);
                listadapter.notifyDataSetChanged();

            }catch (Exception e)
            {
                Toast.makeText(getParent(),""+e.getMessage(),Toast.LENGTH_LONG).show();
            }
        }

        @Override
        protected Void doInBackground(String... strings) {
            utils.FireStor= FirebaseFirestore.getInstance();
            utils.FireStor.collection("Post").get().addOnCompleteListener(task -> {
                if(task.isSuccessful()) {
                    if (!task.getResult().isEmpty()){
                        for (QueryDocumentSnapshot doc : task.getResult()) {
                            publishProgress(doc.getData());
                        }
                }
                    else empty=false;

                }
                else {
                    Map<String, Object> f = new HashMap<>();
                    f.put("e", task.getException());
                    publishProgress(f);
                }

            });
            return null;

        }
    }
}