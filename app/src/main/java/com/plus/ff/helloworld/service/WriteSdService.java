package com.plus.ff.helloworld.service;

import android.app.DownloadManager;
import android.app.IntentService;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Environment;
import android.webkit.MimeTypeMap;
import android.widget.Toast;

import com.plus.ff.helloworld.R;
import com.plus.ff.helloworld.activity.MainActivity;

/**
 * Created by Plus on 2016/11/28.
 * who's trying to love coding.
 */
public class WriteSdService extends IntentService{

    public WriteSdService()
    {
        super("WriteSdServices!");
    }
    private boolean isMounted()
    {
        return Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED);
    }
    @Override
    protected void onHandleIntent(Intent intent)
    {
        if(isMounted())
        {
            try
            {
                String serviceString = Context.DOWNLOAD_SERVICE;
                DownloadManager downloadManager;
                downloadManager = (DownloadManager) getSystemService(serviceString);

                Uri uri = Uri.parse(constant.UPDATE_URL);
                DownloadManager.Request request = new DownloadManager.Request(uri);

                request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_MOBILE | DownloadManager.Request.NETWORK_WIFI);
                request.setAllowedOverRoaming(true);
                setNotification(request);
                setDownloadFilePath(request);

                request.allowScanningByMediaScanner();
                request.setVisibleInDownloadsUi(true);

                MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
                request.setMimeType(mimeTypeMap.getMimeTypeFromExtension(constant.UPDATE_URL));
                long mreference = downloadManager.enqueue(request);

                IntentFilter intentFilter = new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE);
                BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
                    @Override
                    public void onReceive(Context context, Intent intent) {
                            long reference = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID , -1);
                            Toast.makeText(WriteSdService.this,"文件下载完成" , Toast.LENGTH_SHORT).show();
                    }
                };
                registerReceiver(broadcastReceiver,intentFilter);


            }
            catch (Exception ex)
            {
                ex.printStackTrace();
            }
        }
        else
        {
            Toast.makeText(WriteSdService.this,"SD卡错误",Toast.LENGTH_SHORT);

        }
    }

    private void setNotification(DownloadManager.Request request)
    {
        request.setTitle("文件下载");
        request.setDescription("简单地介绍一下");
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
    }

    private void setDownloadFilePath(DownloadManager.Request request)
    {
        request.setDestinationInExternalFilesDir(this,Environment.DIRECTORY_DOWNLOADS,"");
    }




}
