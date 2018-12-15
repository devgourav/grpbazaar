package com.devworkxlabs.grpbazaarapp;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import androidx.appcompat.app.AlertDialog;

public class NetworkCheck extends AsyncTask<Context, Void, Boolean> {
    private static final String LOG_TAG = "networkCheck" ;
    private static final Integer SETTINGS_REQUEST_CODE = 1 ;
    String latestVersionCode;
    String successCode;
    Context context;

    public NetworkCheck(Context context) {
        this.context = context;
    }

    @Override
    protected Boolean doInBackground(Context...contexts) {
        HttpURLConnection connection = null;
        BufferedReader reader = null;
        String detailsApiUrl = "https://devgourav.github.io/grpbazaar/api/details.json";
        if (isNetworkAvailable(contexts[0])) {

            try {
                URL url = new URL(detailsApiUrl);
                connection = (HttpURLConnection) url.openConnection();
                connection.connect();
                InputStream stream = connection.getInputStream();

                reader = new BufferedReader(new InputStreamReader(stream));

                StringBuffer buffer = new StringBuffer();
                String line = "";

                while ((line = reader.readLine()) != null) {
                    buffer.append(line+"\n");

                }

                Log.d(LOG_TAG,buffer.toString());

                JSONObject detailsObject = new JSONObject(buffer.toString());

                successCode = detailsObject.getJSONObject("codes").getString("success");
                latestVersionCode = detailsObject.getJSONObject("metaDetails").getString("version");

                Log.d(LOG_TAG,successCode);
                Log.d(LOG_TAG,latestVersionCode);

                return successCode.equals("200");
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else {
            Log.d(LOG_TAG, "No network available!");
        }
        return false;
    }

    @Override
    protected void onPostExecute(Boolean isActiveNetwork) {
        super.onPostExecute(isActiveNetwork);
        if(isActiveNetwork){
            Log.d(LOG_TAG,"Active Network Connection");
            String versionCode = "0.0";
            try {
                PackageInfo packageInfo = context.getPackageManager().getPackageInfo(context.getPackageName(),0);
                versionCode = Integer.toString(packageInfo.versionCode);
            } catch (PackageManager.NameNotFoundException e) {
                e.printStackTrace();
            }
            if(versionCode.equals(latestVersionCode)){
                Log.d(LOG_TAG,"Latest Version");
            }else{
                Log.d(LOG_TAG,"Old Version");
                createUpdateAppDialog();
            }
        }else{
            Log.d(LOG_TAG,"InActive Network Connection");
            createNoNetworkDialog();
        }
    }

    private boolean isNetworkAvailable(Context context) {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null;
    }

    private void createNoNetworkDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setMessage("GrpBazaar App requires a active internet connection to open.Please switch on your mobile data or WIFI.")
                .setTitle("No Internet Connection")
                .setIcon(R.drawable.ic_launcher_icon);
        builder.setPositiveButton("Settings", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                ((Activity)context).startActivityForResult(new Intent(android.provider.Settings.ACTION_SETTINGS), SETTINGS_REQUEST_CODE);
            }
        });
        builder.setNegativeButton("Close", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                ((Activity)context).finishAffinity();
            }
        });
        builder.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                ((Activity)context).finishAffinity();
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void createUpdateAppDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setMessage("You are using a old version of GRP Bazaar app.Please update to the latest version")
                .setTitle("Update required")
                .setIcon(R.drawable.ic_launcher_icon);
        builder.setPositiveButton("Update Now", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse("https://play.google.com/store/apps/details?id=com.devworkxlabs.grpbazaarapp&hl=en"));
                intent.setPackage("com.android.vending");
                context.startActivity(intent);
            }
        });
        builder.setNegativeButton("Close", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                ((Activity)context).finishAffinity();
            }
        });
        builder.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                ((Activity)context).finishAffinity();
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }
}


