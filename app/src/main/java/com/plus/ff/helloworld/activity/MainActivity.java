package com.plus.ff.helloworld.activity;

import android.Manifest;
import android.app.Activity;
import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.plus.ff.helloworld.R;
import com.plus.ff.helloworld.service.*;

import java.io.File;

public class MainActivity extends Activity implements View.OnClickListener{

    private TextView Version;
    public static String versionname,serverVersionName;
    public static int versioncode,serverVersionCode;
    private VersionUpdate vp = new VersionUpdate();

    private ProgressBar proBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // TODO: 2016/11/23  test for version check & update
        Version = (TextView)findViewById(R.id.version);
        proBar = (ProgressBar)findViewById(R.id.progressBar_id);
        Context con = this;
        versioncode = vp.getVersion(con);
        versionname = vp.getVersionName(con);

        Version.setText("VersionCode : " + versioncode + "\n" + "VersionName : " + versionname);


        findViewById(R.id.button_popWindow).setOnClickListener(this);
        findViewById(R.id.button_send).setOnClickListener(this);
        findViewById(R.id.button_update).setOnClickListener(this);
        findViewById(R.id.button_download).setOnClickListener(this);

    }
    public final static String EXTRA_MESSAGE = "com.plus.ff.helloworld.MESSAGE";
    @Override
    public void onClick(View view)
    {
        switch (view.getId())
        {
            case R.id.button_send:
                // when user click the send button
                // TODO: 2016/11/15 activity transfer
                Intent intent = new Intent(this,DisplayMessageActivity.class);
                EditText editText = (EditText) findViewById(R.id.edit_message);
                String message = editText.getText().toString();
                intent.putExtra(EXTRA_MESSAGE,message);
                startActivity(intent);
                break;
            case R.id.button_popWindow:
                //test for pop a window
                Intent intent1 = new Intent(this,PopupSettingActivity.class);
                startActivity(intent1);
                break;
            case R.id.button_update:
                vp.versionCompare(this);
                break;
            case R.id.button_download:
                Intent intent2 = new Intent(this,WriteSdService.class);
                startService(intent2);
                break;
            default:
                break;
        }
    }


}
