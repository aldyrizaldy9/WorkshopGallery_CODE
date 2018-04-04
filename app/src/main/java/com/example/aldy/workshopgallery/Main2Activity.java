package com.example.aldy.workshopgallery;

import android.graphics.Bitmap;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.vansuita.pickimage.bean.PickResult;
import com.vansuita.pickimage.bundle.PickSetup;
import com.vansuita.pickimage.dialog.PickImageDialog;
import com.vansuita.pickimage.listeners.IPickResult;

import java.io.ByteArrayOutputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

public class Main2Activity extends AppCompatActivity implements IPickResult {
    EditText mEtName, mEtCaption;
    ImageView mCamera;
    Bitmap mGallery;
    Uri mUri, dlUri;
    String mPath;
    String idString, name, caption, mKeys;
    Button mUpload;
    int id;

    private static final String ALLOWED_CHARACTERS = "0123456789qwertyuiopasdfghjklzxcvbnmABCDEFGHIJKLMNOPQRSTUVWXYZ";

    FirebaseDatabase mDatabase = FirebaseDatabase.getInstance();
    DatabaseReference mRef = mDatabase.getReference();

    FirebaseStorage mStorage = FirebaseStorage.getInstance();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        setTitle("Add Gallery");

        mEtName = findViewById(R.id.etName);
        mEtCaption = findViewById(R.id.etCaption);
        mCamera = findViewById(R.id.ivCamera);
        mUpload = findViewById(R.id.btnUpload);

        mCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PickImageDialog.build(new PickSetup()).show(Main2Activity.this);
            }
        });

        mUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mKeys = getRandomString(8);
                new Timer().schedule(new TimerTask() {
                    @Override
                    public void run() {
                        uploadGallery(mGallery, mKeys);
                    }
                }, 1000);

                getId();


            }
        });
    }

    @Override
    public void onPickResult(PickResult r) {
        if (r.getError() == null) {
            mGallery = r.getBitmap();
            mUri = r.getUri();
            mPath = r.getPath();
            mCamera.setImageBitmap(r.getBitmap());
            mCamera.setEnabled(false);
            Toast.makeText(this, r.getUri() + "  " + r.getPath(), Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(this, r.getError().getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    public void uploadGallery(Bitmap foto, String id) {
        StorageReference storageRef = mStorage.getReferenceFromUrl("gs://workshopgallery-ea2df.appspot.com");
        StorageReference mountainImagesRef = storageRef.child("gallery/" + id + ".jpg");
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        foto.compress(Bitmap.CompressFormat.JPEG, 20, baos);
        byte[] data = baos.toByteArray();
        UploadTask uploadTask = mountainImagesRef.putBytes(data);
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle unsuccessful uploads
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                // taskSnapshot.getMetadata() contains file metadata such as size, content-type, and download URL.
                Uri downloadUrl = taskSnapshot.getDownloadUrl();
                dlUri = downloadUrl;
                Log.v("downloadUrl-->", "" + downloadUrl);
            }
        });
    }

    public void uploadData(int id, String keys) {
        Map<String, Object> gallery = new HashMap<String, Object>();
        gallery.put("/gallery/" + id + "/nama", name);
        gallery.put("/gallery/" + id + "/caption", caption);
        gallery.put("/gallery/" + id + "/foto", keys + ".jpg");
        mRef.updateChildren(gallery);
    }

    public void getId() {
        mRef.child("gallery")
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        for (DataSnapshot data : dataSnapshot.getChildren()) {
                            idString = data.getKey();
                        }
                        id = Integer.parseInt(idString) + 1;
                        name = mEtName.getText().toString();
                        caption = mEtCaption.getText().toString();
                        uploadData(id, mKeys);
                        finish();
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

    }


    private static String getRandomString(final int sizeOfRandomString) {
        final Random random = new Random();
        final StringBuilder sb = new StringBuilder(sizeOfRandomString);
        for (int i = 0; i < sizeOfRandomString; ++i)
            sb.append(ALLOWED_CHARACTERS.charAt(random.nextInt(ALLOWED_CHARACTERS.length())));
        return sb.toString();
    }
}
