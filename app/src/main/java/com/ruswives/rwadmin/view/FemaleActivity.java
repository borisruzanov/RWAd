package com.ruswives.rwadmin.view;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.Toast;

import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.ruswives.rwadmin.MainActivity;
import com.ruswives.rwadmin.R;
import com.ruswives.rwadmin.RecycleListItemListner;
import com.ruswives.rwadmin.ShareEditPreview;
import com.ruswives.rwadmin.ShareItemAdapter;
import com.ruswives.rwadmin.model.FsUser;

import java.util.ArrayList;
import java.util.List;

public class FemaleActivity extends AppCompatActivity implements RecycleListItemListner {

    //recylerview object
    RecyclerView recyclerView;
    //Fsuser list object
    List<FsUser> users;
    //to store image data in Uri object
    static Uri uri;
    //write external storage code
    final int WRITE_EXTRNAL_PERMISSION_CODE = 201;
    //to check from request is called
    private boolean onShare = false;
    //to store drawable object
    private Drawable drawable;

    ShareItemAdapter shareItemAdapter;



    DatabaseReference realtimeReference = FirebaseDatabase.getInstance().getReference();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_female);
        FirebaseApp.initializeApp(this);
        recyclerView = findViewById(R.id.main_recyclerview);
        if (!checkPermission()) requestPermission();
        users = new ArrayList<>();
        getData();
    }

    private void getData() {
        realtimeReference.child("SocialAccounts").child("Female").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    FsUser user = postSnapshot.getValue(FsUser.class);
                    users.add(new FsUser(user.getName(),
                            user.getAge(),
                            user.getCountry(),
                            user.getDrink_status(),
                            user.getImage(),
                            user.getStatus(),
                            user.getThumb_image(),
                            user.getUid(),
                            user.getGender(),
                            user.getRelationship_status(),
                            user.getBody_type(),
                            user.getEthnicity(),
                            user.getFaith(),
                            user.getSmoking_status(),
                            user.getNumber_of_kids(),
                            user.getWant_children_or_not(),
                            user.getHobby(),
                            user.getRating(),
                            user.getId_soc()));

//                    this.name = name;
//                    this.age = age;
//                    this.country = country;
//                    this.drink_status = drink_status;
//                    this.image = image;
//                    this.status = status;
//                    this.thumb_image = thumb_image;
//                    this.uid = uid;
//                    this.gender = gender;
//                    this.relationship_status = relationship_status;
//                    this.body_type = body_type;
//                    this.ethnicity = ethnicity;
//                    this.faith = faith;
//                    this.smoking_status = smoking_status;
//                    this.number_of_kids = number_of_kids;
//                    this.want_children_or_not = want_children_or_not;
//                    this.hobby = hobby;
//                    this.rating = rating;
//                    this.id_soc = id_soc;
                }
                Log.d("debug", "Size of list response " + users.size());
                shareItemAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }


    @Override
    protected void onResume() {
        super.onResume();

        //to set layout of drawable
        recyclerView.setLayoutManager(new LinearLayoutManager(this, RecyclerView.VERTICAL, false));
        //adpater object
        shareItemAdapter = new ShareItemAdapter(users, this, this);
        //set a adpter on recyclerview
        recyclerView.setAdapter(shareItemAdapter);
    }


    Uri getUri(Bitmap bitmap) {
        String Path = MediaStore.Images.Media.insertImage(getContentResolver(),
                bitmap, "Image Description" + Math.random(), null);
        return Uri.parse(Path);
    }

    public static Bitmap getBitmap(Drawable drawable) {
        return (((BitmapDrawable) drawable).getBitmap());
    }

    //to check permission of Write storage
    private boolean checkPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(FemaleActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                return true;
            } else {
                return false;
            }
        }
        return true;
    }

    //to request for a permission of write storage
    private void requestPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            ActivityCompat.requestPermissions(FemaleActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, WRITE_EXTRNAL_PERMISSION_CODE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == WRITE_EXTRNAL_PERMISSION_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Log.d("Permsiion", "Permission granted");
            } else {
                Log.d("Permsiion", "Permission not  granted");

            }
        }
    }

    @Override
    public void itemListner(FsUser fsUser, ShareItemAdapter.MyViewHolder viewHolder) {

        drawable = viewHolder.imageView.getDrawable();
        if (drawable == null) {
            Toast.makeText(this, "Please wait until image is loaded", Toast.LENGTH_SHORT).show();
            return;
        }
        if (!checkPermission()) {
            requestPermission();
            Toast.makeText(getApplicationContext(), "Please give write permission and select again", Toast.LENGTH_LONG).show();
        } else {
            //call a imagePreview activity
            Intent sharePreview = new Intent(FemaleActivity.this, ShareEditPreview.class);
            sharePreview.putExtra("Bitmap", getUri(FemaleActivity.getBitmap(drawable))); //put image data in uri
            sharePreview.putExtra("text", viewHolder.textView.getText()); //put text
            sharePreview.putExtra("uid", fsUser.getUid()); //put text
            startActivity(sharePreview);
        }
    }
}
