package com.plus.ff.helloworld.service;

/**
 * Created by Plus on 2016/11/23.
 * who's trying to love coding.
 */
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Looper;
import android.view.Gravity;
import android.widget.Toast;

import com.plus.ff.helloworld.activity.MainActivity;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;


public class VersionUpdate{
    private static VersionUpdate vp = new VersionUpdate();
    private int version, Sversion;
    private static String versionName;
    private static String serverFilePath;
    private static String description;

    public int getVersion(Context context) {
        version = -1;
        try {
            version = context.getPackageManager().getPackageInfo("com.plus.ff.helloworld", 0).versionCode;
        } catch (Exception ex) {
            System.out.println("Error in getVersion ! " + ex);
            Toast toast = Toast.makeText(context.getApplicationContext(), "获取当前版本失败", Toast.LENGTH_LONG);
            toast.setGravity(Gravity.CENTER,0,0);
            toast.show();
        }
        return version;
    }

    public String getVersionName(Context context) {
        versionName = "";
        try {
            versionName = context.getPackageManager().getPackageInfo("com.plus.ff.helloworld", 0).versionName;
        } catch (Exception ex) {
            System.out.println("Error in getVersionName ! " + ex);
        }
        return versionName;
    }

    // TODO: 2016/11/23 use http connection
    public int getServerVersion(Context context) {
        HttpURLConnection connect = null;
        BufferedReader bufferedReader = null;
        InputStreamReader inputStreamReader = null;
        try {
            //get the json
            URL serverURL = new URL(constant.UPDATE_URL);
            connect = (HttpURLConnection) serverURL.openConnection();
            inputStreamReader = new InputStreamReader(connect.getInputStream());
            bufferedReader = new BufferedReader(inputStreamReader);
            String line;
            StringBuilder stringBuilder = new StringBuilder();
            while ((line = bufferedReader.readLine()) != null) {
                stringBuilder.append(line);
            }

            //analyze the json
            JSONObject jsonObject = new JSONObject(stringBuilder.toString());
            Sversion = jsonObject.getInt("versioncode");
            serverFilePath = jsonObject.optString("url");
            description = jsonObject.optString("description");
        } catch (Exception ex) {
            System.out.println("Error in getServerVersion ! " + ex);
            Toast toast = Toast.makeText(context.getApplicationContext(),"获取最新版本失败",Toast.LENGTH_LONG);
            toast.setGravity(Gravity.CENTER,0,0);
            toast.show();
        } finally {
            if (bufferedReader != null) {
                try {
                    bufferedReader.close();
                } catch (IOException ex) {

                }
            }
            if (inputStreamReader != null) {
                try {
                    inputStreamReader.close();
                } catch (IOException ex) {

                }
            }
            if (connect != null) {
                try {
                    connect.disconnect();
                } catch (Exception ex) {

                }
            }
        }
        return Sversion;
    }

    // TODO: 2016/11/23 use local files
    public int getServerVersion_host(Context context) {
        StringBuilder stringBuilder = new StringBuilder();
        Sversion = 0;
        try {
            //read from json.file
            InputStreamReader inputStreamReader = new InputStreamReader(context.getAssets().open("version.json"));
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                stringBuilder.append(line);
            }
            bufferedReader.close();
            inputStreamReader.close();

            //analyze the json file
            JSONObject jsonObject = new JSONObject(stringBuilder.toString());
            Sversion = jsonObject.getInt("versioncode");

        } catch (Exception ex) {
            System.out.println("Error in getServerVersion_host ! " + ex.toString());
            ex.printStackTrace();
        }
        return Sversion;
    }

    // TODO: 2016/11/24 compare the version
    public boolean versionCompare(final Context context) {
        final Context contextTemp = context;
        try {
            new Thread() {
                public void run() {
                    Looper.prepare();
                    int currentVersion = getVersion(contextTemp);
                    int latestVersion = getServerVersion(contextTemp);
                    if (currentVersion < latestVersion) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(contextTemp);
                        builder.setTitle("版本更新");
                        builder.setMessage("当前版本： " + currentVersion + "\n" + "最新版本：" + latestVersion + "\n" + "说明：" + description + "\n" + serverFilePath);
                        builder.setPositiveButton("更新", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int argc) {
                                //start downloading the apk
                                new Thread() {
                                    public void run() {
                                        Looper.prepare();
                                        gotoDownload(contextTemp,serverFilePath);
                                        Looper.loop();
                                    }
                                }.start();
                            }
                        });
                        builder.setNegativeButton("取消", null);
                        AlertDialog dialog = builder.create();
                        dialog.setCanceledOnTouchOutside(false);
                        dialog.show();
                    } else {
                        AlertDialog.Builder builder = new AlertDialog.Builder(contextTemp);
                        builder.setTitle("版本更新");
                        builder.setMessage("当前是最新版本，无须更新");
                        builder.setPositiveButton("确定", null);
                        builder.show();
                    }
                    Looper.loop();

                }
            }.start();
        } catch (Exception ex) {
            System.out.println("Error in versionCompare ! " + ex.toString());
            ex.printStackTrace();
        } finally {
            return false;
        }
    }

    private static void gotoDownload(Context context,String url)
    {
        Intent intent = new Intent(context.getApplicationContext(),DownloadService.class);
        intent.putExtra("url",url);
        context.startService(intent);
    }

}