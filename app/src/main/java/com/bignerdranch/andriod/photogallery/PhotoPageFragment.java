package com.bignerdranch.andriod.photogallery;

import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

public class PhotoPageFragment extends VisibleFragment {

    //key for bundle Uri
    private static final String ARG_URI = "photo_page_uri";

    private Uri mUri;
    private WebView mWebView;
    private ProgressBar mProgressBar;


    //create a singleton with the Bundle data(Uri)
    public static PhotoPageFragment newInstance(Uri uri){
        Bundle args = new Bundle();
        args.putParcelable(ARG_URI, uri);

        PhotoPageFragment fragment = new PhotoPageFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //get data from intent sent from onclick within PhotoGalleryFragment
        mUri = getArguments().getParcelable(ARG_URI);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        //get view and inflate it inside the container: PhotoPageActivity
        //set WebView settings to true and get WebView object
        //load the Uri from mUri
        View v = inflater.inflate(R.layout.fragment_photo_page, container, false);

        //setup ProgressBar for WebChromeClient
        //WebChromeClient is an event interface for reacting to events
        //to change chrome around the browser.. alerts, favicons, and of
        //course updates for loading progress

        mProgressBar = v.findViewById(R.id.progress_bar);
        mProgressBar.setMax(100); // WebChromeClient reports in range of 0-99

        mWebView = (WebView) v.findViewById(R.id.web_view);
        mWebView.getSettings().setJavaScriptEnabled(true);

        //method to setup progressbar with WebChromeClient
        mWebView.setWebChromeClient(new WebChromeClient(){
            //set up methods to show progress and display title updates

            public void onProgressChanged(WebView webView, int newProgress){
                if ( newProgress ==  100){
                    mProgressBar.setVisibility(View.GONE);
                }else {
                    mProgressBar.setVisibility(View.VISIBLE);
                    mProgressBar.setProgress(newProgress);
                }
            }

            public void onReceiveTitle(WebView webView, String title){
                AppCompatActivity activity = (AppCompatActivity) getActivity();
                activity.getSupportActionBar().setSubtitle(title);
            }

        });

        //WebViewClient tells the webView to load the page
        //vs the webView asking the activity manager what to do
        mWebView.setWebViewClient(new WebViewClient());
        mWebView.loadUrl(mUri.toString());

        return v;
    }
}
