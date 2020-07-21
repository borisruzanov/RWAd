package com.ruswives.rwadmin;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.firebase.ui.auth.AuthUI;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.ruswives.rwadmin.view.FemaleActivity;
import com.ruswives.rwadmin.view.MaleActivity;
import com.ruswives.rwadmin.view.VideoApprovalActivity;

import java.util.Arrays;
import java.util.List;

import static com.ruswives.rwadmin.Consts.RC_SIGN_IN;

public class MainActivity extends AppCompatActivity {

    private Button mFemaleButton;
    private Button mMaleButton;
    private Button mVideoButton;
    private Button mLogin;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        FirebaseApp.initializeApp(this);
        mFemaleButton = (Button) findViewById(R.id.main_soc_female);
        mMaleButton = (Button) findViewById(R.id.main_soc_male);
        mFemaleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent femaleIntent = new Intent(MainActivity.this, FemaleActivity.class);
                startActivity(femaleIntent);
            }
        });
        mMaleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent maleIntent = new Intent(MainActivity.this, MaleActivity.class);
                startActivity(maleIntent);
            }
        });
        mVideoButton=findViewById(R.id.main_video);
        mVideoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (FirebaseAuth.getInstance().getCurrentUser()!=null){
                    startActivity(new Intent(MainActivity.this, VideoApprovalActivity.class));
                }else{
                    Toast.makeText(getApplicationContext(),"Please first login",Toast.LENGTH_SHORT).show();
                }
            }
        });
        mLogin=findViewById(R.id.main_login);
        mLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callAuthWindow();
            }
        });

    }

    /**
     * Calling auth window to log in
     */
    public void callAuthWindow() {
        List<AuthUI.IdpConfig> providers = Arrays.asList(
                new AuthUI.IdpConfig.EmailBuilder().build(),
                new AuthUI.IdpConfig.GoogleBuilder().build());


        startActivityForResult(
                AuthUI.getInstance()
                        .createSignInIntentBuilder()
                        .setAvailableProviders(providers)
                        .build(),
                RC_SIGN_IN);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==RC_SIGN_IN){
            if (data!=null)
            {
                if (resultCode==RESULT_OK){
                    Toast.makeText(getApplicationContext(),"Login successful",Toast.LENGTH_LONG).show();
                }else{
                    Toast.makeText(getApplicationContext(),"Login failed,try again",Toast.LENGTH_LONG).show();
                    Log.w("Login","Login failed with result_code->>"+resultCode);
                }
            }else{
                Toast.makeText(getApplicationContext(),"Login failed,try again",Toast.LENGTH_LONG).show();
                Log.w("Login","Login failed data is null");
            }
        }
    }
}
