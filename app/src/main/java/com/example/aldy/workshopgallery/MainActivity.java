package com.example.aldy.workshopgallery;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.example.aldy.workshopgallery.Adapter.GalleryAdapter;
import com.example.aldy.workshopgallery.Model.GalleryModel;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    FloatingActionButton floatingActionButton;
    List<GalleryModel> listModel = new ArrayList<>();
    GalleryAdapter adapter;

    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference ref = database.getReference();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = findViewById(R.id.recyclerView);
        floatingActionButton = findViewById(R.id.fab);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        layoutManager.setReverseLayout(true); //membalikkan data dari bawah
        layoutManager.setStackFromEnd(true); //buat nabrakin ke atas
        recyclerView.setLayoutManager(layoutManager);

        ref.child("gallery").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                //waktu masuk disarankan untuk hapus dulu data awal terus diperbaharui
                listModel.clear();

                for (DataSnapshot data : dataSnapshot.getChildren()){
                    String nama = data.child("nama").getValue().toString();
                    String caption = data.child("caption").getValue().toString();
                    String foto = data.child("foto").getValue().toString();
                    listModel.add(new GalleryModel(nama, caption, foto));
                }

                adapter = new GalleryAdapter(listModel, getApplicationContext());
                recyclerView.setAdapter(adapter);
                adapter.notifyDataSetChanged(); //perubahan biar tau
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, Main2Activity.class);
                startActivity(intent);
            }
        });
    }
}
