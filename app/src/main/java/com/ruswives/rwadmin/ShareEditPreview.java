package com.ruswives.rwadmin;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.ruswives.rwadmin.database.Prefs;
import com.ruswives.rwadmin.model.CoverImageData;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class ShareEditPreview extends AppCompatActivity {

    private static final int WRITE_EXTRNAL_PERMISSION_CODE = 201; //code to get write_External_storage permission
    RecyclerView recyclerView; // recyleview object
    ImageView imageView; // a preview imageview object
    Button back,save; //back and savebutton object
    List<CoverImageData> imageDataList; //the cover list object
    Bitmap result,original; //result have updated bitmap and original have a original image bitmap

    private static Uri uri; //a image uri object

    private String mUid = "";
    private Prefs mPrefs;

    private static boolean isShare;//true if share intent call


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_share_edit_preview);
        recyclerView=findViewById(R.id.share_edit_recyclerview);
        imageView=findViewById(R.id.share_edit_imgview1);
        back=findViewById(R.id.share_edit_btn_back);
        save=findViewById(R.id.share_edit_btn_nxt);

        mPrefs = new Prefs(this);
        //get image uri data from intent
        uri= (Uri) getIntent().getExtras().get("Bitmap");

        mUid = getIntent().getStringExtra(Consts.UID);
        //set imageview to original image
        Picasso.with(this).load(uri).into(imageView);

        //get bitmap from uri object
        try{
            original= MediaStore.Images.Media.getBitmap(getContentResolver(),uri);
        }catch (Exception e){
            Toast.makeText(this,"Some wrong happen..!",Toast.LENGTH_SHORT).show();
            finish();
        }

        setCoverList(); //create coverlist object

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPrefs.setValue(mUid, Consts.CONFIRMED);
                setUri();
                CustomDilog customDilog=new CustomDilog(ShareEditPreview.this,getIntent().getExtras().getString("text"));
                customDilog.show();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        setTitle("EditImage");
        recyclerView.setLayoutManager(new LinearLayoutManager(ShareEditPreview.this,RecyclerView.HORIZONTAL,false));
        ImagePreviewRecycleAdpter recycleAdpter=new ImagePreviewRecycleAdpter(imageDataList
                , new ImagePreviewRecylerItemListner() {
            @Override
            public void onItemClick(Bitmap bitmap) {
                result=getResultBitmap(original,bitmap);
                imageView.setImageBitmap(result);
            }
        });
        recyclerView.setAdapter(recycleAdpter);

//        result=MainActivity.getBitmap(imageView.getDrawable());
    }

    //get over cover image bitmap
    private Bitmap getResultBitmap(Bitmap original,Bitmap Cover){
        Log.d("details","original Height:-"+original.getWidth()+" original Width:-"+original.getWidth());
        Log.d("details","cover Height:-"+Cover.getWidth()+" cover Width:-"+Cover.getWidth());
        Bitmap bitmap=Bitmap.createBitmap(original.getWidth(),original.getHeight(),original.getConfig());
        Canvas canvas=new Canvas(bitmap);
        canvas.drawBitmap(original,new Matrix(),null);
        Cover=Bitmap.createScaledBitmap(Cover,original.getWidth(),original.getHeight(),false);
        canvas.drawBitmap(Cover,new Matrix(),null);
        Log.d("details","bitmap Height:-"+bitmap.getWidth()+" result Width:-"+bitmap.getWidth());
        return bitmap;
    }

    //to call a share intent
    public static void Share(Context context){
        if (uri!=null){
            Intent intent=new Intent();
            intent.setAction(Intent.ACTION_SEND);
            intent.putExtra(Intent.EXTRA_STREAM,uri);
            intent.setType("image/*");
            context.startActivity(Intent.createChooser(intent,"Share On Social Media"));
        }
        else{
            Toast.makeText(context,"Image must not null",Toast.LENGTH_LONG).show();
        }
        isShare=true; //to finish activity after share intent called
    }

    //to check permission of Write storage
    private boolean checkPermission(){
        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.M){
            return ContextCompat.checkSelfPermission(ShareEditPreview.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED;
        }
        return true;
    }

    //to request for a permission of write storage
    private void requestPermission(){
        if (Build.VERSION.SDK_INT>=Build.VERSION_CODES.M){
            if (ActivityCompat.shouldShowRequestPermissionRationale(ShareEditPreview.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)){
                Toast.makeText(getApplicationContext(),"You must Give Permission to share image",Toast.LENGTH_LONG).show();
            }else{
                ActivityCompat.requestPermissions(ShareEditPreview.this,new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},WRITE_EXTRNAL_PERMISSION_CODE);
            }
        }
    }

    //to set uri data from result bitmap object
    void setUri(){
        if (!checkPermission()){
            requestPermission();
        }
        String Path= MediaStore.Images.Media.insertImage(getContentResolver(),
                result, "Image Description", null);
        uri= Uri.parse(Path);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode==WRITE_EXTRNAL_PERMISSION_CODE){
            if (grantResults.length>0 && grantResults[0]==PackageManager.PERMISSION_GRANTED){
                Log.d("Permsiion","Permission granted");
                setUri();
            }
            else{
                Log.d("Permsiion","Permission not  granted");

            }
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (isShare) finish();
    }

    void setCoverList(){
        imageDataList=new ArrayList<>();
        imageDataList.add(new CoverImageData(original,"Original"));
        imageDataList.add(new CoverImageData(BitmapFactory.decodeResource(getResources(),R.drawable.male_grey)
                ,"cover_1"));
        imageDataList.add(new CoverImageData(BitmapFactory.decodeResource(getResources(),R.drawable.male_ping)
                ,"cover_2"));
        imageDataList.add(new CoverImageData(BitmapFactory.decodeResource(getResources(),R.drawable.male_white)
                ,"cover_3"));
        imageDataList.add(new CoverImageData(BitmapFactory.decodeResource(getResources(),R.drawable.female_blue)
                ,"cover_4"));
        imageDataList.add(new CoverImageData(BitmapFactory.decodeResource(getResources(),R.drawable.female_grey)
                ,"cover_5"));
    }
}
