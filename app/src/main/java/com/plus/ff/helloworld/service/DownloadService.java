package com.plus.ff.helloworld.service;

import android.app.IntentService;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.support.v4.app.NotificationCompat;

import com.plus.ff.helloworld.R;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;


/**
 * An {@link IntentService} subclass for handling asynchronous task requests in
 * a service on a separate handler thread.
 * <p/>
 * TODO: Customize class - update intent actions and extra parameters.
 */
public class DownloadService extends IntentService {

    private NotificationManager mNotifyManager;
    private NotificationCompat.Builder mBuilder;


    public DownloadService() {
        super("DownloadService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        mNotifyManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        mBuilder = new NotificationCompat.Builder(this);

        String appName = getString(getApplicationInfo().labelRes);
        int icon = getApplicationInfo().icon;
        String serverFilePath = intent.getStringExtra("url");
        mBuilder.setContentTitle(appName).setSmallIcon(icon);
        try {
            if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) {
                URL serverURL = new URL(serverFilePath);
                HttpURLConnection connect = (HttpURLConnection) serverURL.openConnection();
                BufferedInputStream bis = new BufferedInputStream(connect.getInputStream());
                File apkfile = new File(constant.SAVE_PATH);
                BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(apkfile));

                int fileLength = connect.getContentLength();
                int downLength = 0;
                int oldprogress = 0;
                int n;
                byte[] buffer = new byte[1024];
                while ((n = bis.read(buffer, 0, buffer.length)) != -1) {
                    bos.write(buffer, 0, n);
                    downLength += n;
                    int progress = (int) (((float) downLength / fileLength) * 100);
                    if(oldprogress != progress) {
                        UpdateProgress(progress);
                    }
                    oldprogress = progress;
                }
                bis.close();
                bos.close();
                connect.disconnect();

                downloadApkFile(apkfile);
                mNotifyManager.cancel(0);
            }

        } catch (Exception ex) {
            System.out.println("Error in downloadApkFile ! " + ex.toString());
            ex.printStackTrace();
        }

    }
    private void UpdateProgress(int progress)
    {
        //"正在下载:" + progress + "%"
        mBuilder.setContentText(this.getString(R.string.android_auto_update_download_progress, progress)).setProgress(100, progress, false);
        PendingIntent pendingintent = PendingIntent.getActivity(this, 0, new Intent(), PendingIntent.FLAG_CANCEL_CURRENT);
        mBuilder.setContentIntent(pendingintent);
        mNotifyManager.notify(0, mBuilder.build());
    }

    private void downloadApkFile(File apkFile)
    {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        try {
            String[] command = {"chmod", "777", apkFile.toString()};
            ProcessBuilder builder = new ProcessBuilder(command);
            builder.start();
        } catch (IOException ignored) {
        }
        String path = constant.SAVE_PATH;
        intent.setDataAndType(Uri.fromFile(new File(path)), "application/vnd.android.package-archive");

        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

}
