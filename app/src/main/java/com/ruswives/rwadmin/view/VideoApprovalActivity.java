package com.ruswives.rwadmin.view;

import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.MediaController;
import android.widget.ProgressBar;
import android.widget.Toast;
import android.widget.VideoView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;
import com.google.firebase.storage.FirebaseStorage;
import com.ruswives.rwadmin.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class VideoApprovalActivity extends AppCompatActivity {
    VideoView mVideoView;
    ProgressBar mProgress_bar;
    Button mUploadButton, mDeclineButton;

    List<String> videoIDList = new ArrayList<>();
    private CollectionReference users = FirebaseFirestore.getInstance().collection("Users");
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_approval);

        mVideoView = findViewById(R.id.videoView_video_approval);

        mProgress_bar = findViewById(R.id.progress_bar);

        mUploadButton = findViewById(R.id.uploadButton_video_approval);
        mUploadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uploadButtonAction();
            }
        });
        mDeclineButton = findViewById(R.id.declineButton_video_approval);
        mDeclineButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                declineButtonAction();
            }
        });

        retrieveVideos();
    }

    /**
     * This function retrieve all the new videos from database.
     */
    private void retrieveVideos() {
        FirebaseDatabase.getInstance().getReference().child("videos").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    videoIDList.add(ds.getKey());
                }
                populateVideoView();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    /**
     * This function reproduces the first video of the list in the videoView
     */
    private void populateVideoView() {
        if (!videoIDList.isEmpty()) {
            mProgress_bar.setAlpha(1f);
            FirebaseDatabase.getInstance().getReference().child("videos").child(videoIDList.get(0)).child("videoURL").addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    Uri videoURL = Uri.parse(String.valueOf(dataSnapshot.getValue()));

                    MediaController mediaController = new MediaController(VideoApprovalActivity.this);
                    mVideoView.setMediaController(mediaController);
                    mVideoView.setVideoURI(videoURL);

                    mVideoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                        @Override
                        public void onPrepared(MediaPlayer mp) {
                            mp.setLooping(true);
                            mProgress_bar.setAlpha(0f);
                            mVideoView.start();
                        }
                    });
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        } else {
            Toast.makeText(getApplicationContext(), "There are no videos", Toast.LENGTH_LONG).show();
        }
    }

    /**
     * This function declines the video.
     * It delete the video from Firebase Storage and the reference in database and also in user document in firestore.
     */
    private void declineButtonAction() {
        if (!videoIDList.isEmpty()) {
            mVideoView.pause();
            mProgress_bar.setAlpha(1f);
            //remove video from firebase storage
            FirebaseStorage.getInstance().getReference().child("/videos/" + videoIDList.get(0)).delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    //set video value default in specified user document

                    FirebaseDatabase.getInstance().getReference().child("videos").child(videoIDList.get(0)).getRef().addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            HashMap<String, String> userVideoInfo = new HashMap<>(); //a map which store in firestore user db
                            String uid=null; //a user id
                            for (DataSnapshot ds:snapshot.getChildren()){
                                if (ds.getKey().equals("userID")){
                                    uid= (String) ds.getValue();
                                    break;
                                }
                            }
                            userVideoInfo.put("video","default");
                            if (uid!=null) {
                                Log.d("videoDecline","video set to default for uid--->"+uid);
                                setVideoUrlInUserDb(uid, userVideoInfo);
                            }
                            else {
                                Log.e("videoDecline","Something wrong uid is null-->>");
                            }
                            FirebaseDatabase.getInstance().getReference().child("videos").child(videoIDList.get(0)).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    mProgress_bar.setAlpha(0f);
                                    videoIDList.remove(videoIDList.get(0));
                                    populateVideoView();
                                }
                            });
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                            Log.d("videoDecline","oncancelled");
                            Log.e("videoDecline","error while gating info of video error Message-->>"+error.getMessage());
                        }
                    });

                    //remove videoInfo from realtime

                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(getApplicationContext(), e.getLocalizedMessage(), Toast.LENGTH_LONG).show();
                }
            });
        } else {
            Toast.makeText(getApplicationContext(), "There are no videos", Toast.LENGTH_LONG).show();
        }
    }

    /**
     * This function accepts the video.
     * It moves the reference of the video to a new database reference so it doesn't pop up anymore in admin app.
     */
    private void uploadButtonAction() {
        if (!videoIDList.isEmpty()) {
            mProgress_bar.setAlpha(1f);

            FirebaseDatabase.getInstance().getReference().child("videos").child(videoIDList.get(0)).getRef().addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    HashMap<String, String> videoInfo=new HashMap<>(); //a map which store in realtime db
                    HashMap<String, String> userVideoInfo = new HashMap<>(); //a map which store in firestore user db
                    String uid=null; //a user id

                    //store all value in maps object
                    for (DataSnapshot snapshot1:snapshot.getChildren()){
                        if (snapshot1.getKey().equals("userID")){
                            uid= (String) snapshot1.getValue(); //store a uid
                        }else if (snapshot1.getKey().equals("videoID")){
                            videoInfo.put("videoID", (String) snapshot1.getValue()); //store videoId in a map
                        }else if (snapshot1.getKey().equals("videoURL")){
                            //store a videoUrl in a both map object
                            videoInfo.put("videoURL", (String) snapshot1.getValue());
                            userVideoInfo.put("video", (String) snapshot1.getValue());
                        }
                    }

                    //check for uid value
                    if (uid!=null){
                        //if not null then update url in a user db
                        setVideoUrlInUserDb(uid,userVideoInfo);
                    }else {
                        //if null then return
                        Toast.makeText(getApplicationContext(),"uid is null Please try again..!",Toast.LENGTH_SHORT).show();
                        return;
                    }
                    //set a videoInfo in a videoAccepted db
                    setVideoAccept(videoInfo);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Log.e("onUpload","onVideoItemListener error:----"+error.getMessage());
                }
            });
        } else {
            Toast.makeText(getApplicationContext(), "There are no videos", Toast.LENGTH_LONG).show();
        }
    }

    /***
     * update a videoUrl in a specified user document
     * @param uid a user id of a user
     * @param video a map object which has a videoUrl
     */
    private void setVideoUrlInUserDb(final String uid, final Map<String, String> video){

        users.document(uid).set(video,SetOptions.merge()).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Log.d("videoUpload","videoURL updated ina user db for uid->"+uid);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                e.printStackTrace();
                Log.e("videoUpload","error while update videoURl in user db");
                setVideoUrlInUserDb(uid,video);
            }
        });

    }

    /***
     * store a accepted video info in realtime references
     * @param videoInfo a map object with video Information
     */
    private void setVideoAccept(Map<String, String> videoInfo){
        FirebaseDatabase.getInstance().getReference().child("videosAccepted").child(videoIDList.get(0)).setValue(videoInfo).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                FirebaseDatabase.getInstance().getReference().child("videos").child(videoIDList.get(0)).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        videoIDList.remove(videoIDList.get(0));
                        mProgress_bar.setAlpha(0f);
                        Toast.makeText(getApplicationContext(), "Video Accepted", Toast.LENGTH_LONG).show();
                        populateVideoView();
                    }
                });
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                mProgress_bar.setAlpha(0f);
                e.printStackTrace();
                Log.e("videoUpload","error while set videoAccept in realtime");
                Toast.makeText(getApplicationContext(), e.getLocalizedMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mVideoView.isPlaying())
            mVideoView.stopPlayback();
    }

}