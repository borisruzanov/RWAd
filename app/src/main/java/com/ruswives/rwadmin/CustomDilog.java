package com.ruswives.rwadmin;

import android.app.Dialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;

public class CustomDilog extends Dialog {

    EditText editText;
    Button Share,Cancel;
    //to store edittext value
    String str;
    Context context;

    public CustomDilog(@NonNull Context context, String str) {
        super(context);
        this.str=str;
        this.context=context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_dilouage);
        editText=findViewById(R.id.dilouge_edittext);
        Share =findViewById(R.id.dilouge_btn_share);
        Cancel=findViewById(R.id.dilouge_btn_cancel);
        editText.setText(str);
        Cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        Share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String str=editText.getText().toString();
                ClipboardManager manager= (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData clipData=ClipData.newPlainText("copy-data",str);
                manager.setPrimaryClip(clipData);
                Toast.makeText(context,"Text Copied to clipboard",Toast.LENGTH_SHORT).show();
                ShareEditPreview.Share(context);
                dismiss();
            }
        });
    }


}
