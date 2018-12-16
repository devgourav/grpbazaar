package com.devworkxlabs.grpbazaarapp;

import android.app.Activity;
import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.util.Log;
import android.view.View;
import android.webkit.WebResourceRequest;
import android.webkit.WebResourceResponse;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.getbase.floatingactionbutton.FloatingActionsMenu;

import java.io.IOException;
import java.io.InputStream;

import androidx.annotation.Nullable;

class CustomWebViewClient extends WebViewClient {

    private static final String LOG_TAG = "CustomWebViewClient";
    private String ShowOrHideWebViewInitialUse = "show";
    Context context;

    public CustomWebViewClient(Context context) {
        this.context = context;
    }

    @Nullable
    @Override
    public WebResourceResponse shouldInterceptRequest(WebView view, WebResourceRequest request) {
        String resourceUrl = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            resourceUrl = request.getUrl().toString();
            String fileExtension = getFileExt(resourceUrl);
            AssetManager assetManager = context.getAssets();
            try {
                String[] list = assetManager.list("/assets");
                for (String s : list) {
                    Log.d(LOG_TAG, s);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            String resourceName = "fikri-rasyid-786614-unsplash-1-1-1-1024x602.jpg";
            if (fileExtension.endsWith("jpg")) {
                if (resourceUrl.contains(resourceName)) {
                    String mimeType = "image/jpeg";
                    String encoding = "UTF-8";
                    try {
                        InputStream inputStream = assetManager.open(resourceName);
                        return new WebResourceResponse(mimeType, encoding, inputStream);
                    } catch (IOException e) {
                        e.printStackTrace();
                        return super.shouldInterceptRequest(view, request);
                    }


                }
            }
        }


        return super.shouldInterceptRequest(view, request);
    }

    @Override
    public void onPageStarted(WebView webview, String url, Bitmap favicon) {
        if (ShowOrHideWebViewInitialUse.equals("show")) {
            webview.setVisibility(webview.INVISIBLE);
        }
    }

    @Override
    public void onPageFinished(WebView view, String url) {
         ProgressBar spinner;
         ImageView splashImage;
        final FloatingActionsMenu menuMultipleActions;


        spinner = ((Activity)context).findViewById(R.id.progressBarLoad);
        splashImage = ((Activity)context).findViewById(R.id.imageViewLoad);
        menuMultipleActions = ((Activity)context).findViewById(R.id.fab_menu);


        ShowOrHideWebViewInitialUse = "hide";
        spinner.setVisibility(View.GONE);
        splashImage.setVisibility(View.GONE);
        menuMultipleActions.setVisibility(View.VISIBLE);


        view.setVisibility(view.VISIBLE);
        view.setFocusable(true);
        view.setFocusableInTouchMode(true);
        view.getSettings().setJavaScriptEnabled(true);
        view.getSettings().setCacheMode(WebSettings.LOAD_DEFAULT);
        view.getSettings().setDomStorageEnabled(true);
        view.getSettings().setDatabaseEnabled(true);
        view.getSettings().setAppCacheEnabled(true);
        view.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
        super.onPageFinished(view, url);
    }

    public String getFileExt(String fileName) {
        return fileName.substring(fileName.lastIndexOf(".") + 1, fileName.length());
    }

}


