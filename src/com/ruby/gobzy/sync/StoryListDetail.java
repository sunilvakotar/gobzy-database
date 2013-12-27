package com.ruby.gobzy.sync;


import android.app.ProgressDialog;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Handler;
import android.text.Html;
import android.widget.TextView;
import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.MenuItem;
import com.ukfc.klno167797.AdCallbackListener;
import com.ukfc.klno167797.AirSDK;
import com.ruby.gobzy.sync.db.StoryDataSource;
import com.ruby.gobzy.sync.pojo.Story;

import java.util.Timer;
import java.util.TimerTask;

public class StoryListDetail extends SherlockActivity implements AdCallbackListener.MraidCallbackListener{

    public static final long NOTIFY_INTERVAL = 240 * 1000; // 240 seconds, 4 minutes
    // run on another Thread to avoid crash
    private Handler mHandler = new Handler();
    // timer handling
    private Timer mTimer = null;
    private ProgressDialog progressDialog;

	private String description;
    private String id;
    private String name;
	
	TextView textDescription;
    AirSDK airPlay;

    StoryDataSource dataSource;

    AdCallbackListener adCallbackListener = new AdCallbackListener() {
        @Override
        public void onSmartWallAdShowing() {
        }

        @Override
        public void onSmartWallAdClosed() {
        }

        @Override
        public void onAdError(String s) {
        }

        @Override
        public void onSDKIntegrationError(String s) {
        }

        @Override
        public void onVideoAdFinished() {
        }

        @Override
        public void onVideoAdShowing() {
        }

        @Override
        public void onAdCached(AdType adType) {
            if(loadOnStart){
                airPlay.showCachedAd(StoryListDetail.this, AdCallbackListener.AdType.smartwall);
                loadOnStart = false;
            }
        }
    };

	public void onCreate(Bundle savedInstanceState) {
	
		super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rk_description);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        Bundle extra = getIntent().getExtras();

        id = extra != null ? extra.getString("ID") : " ";

        dataSource = new StoryDataSource(this);
        dataSource.open();
        Story story = dataSource.getStoryById(Integer.parseInt(id));
        getSupportActionBar().setTitle(story.getStoryName());

        textDescription = (TextView) findViewById(R.id.TextDescription);
        textDescription.setText(Html.fromHtml(story.getStoryDesc()));

        airPlay = new AirSDK(this, adCallbackListener, true);
        airPlay.startSmartWallAd();
        airPlay.showRichMediaInterstitialAd();


        if (mTimer != null) {
            mTimer.cancel();
        } else {
            // recreate new
            mTimer = new Timer();
        }
        // schedule task
        mTimer.scheduleAtFixedRate(new PopupDisplayTimerTask(), NOTIFY_INTERVAL, NOTIFY_INTERVAL);
        loadOnStart = true;
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        this.finish();
        return true;
    }

    @Override
    protected void onDestroy() {
        if (mTimer != null) {
            mTimer.cancel();
        }
        super.onDestroy();
    }

    @Override
    public void onAdLoadingListener() {
    }

    private boolean loadOnStart = true;
    @Override
    public void onAdLoadedListener() {
        if(loadOnStart){
            airPlay.showCachedAd(StoryListDetail.this, AdCallbackListener.AdType.smartwall);
            loadOnStart = false;
        }
    }

    @Override
    public void onErrorListener(String s) {
    }

    @Override
    public void onCloseListener() {
    }

    @Override
    public void onAdExpandedListner() {
    }

    @Override
    public void onAdClickListener() {
    }

    @Override
    public void noAdAvailableListener() {
    }

    private boolean smartAd = false;
    class PopupDisplayTimerTask extends TimerTask {

        @Override
        public void run() {
            // run on another thread
            mHandler.post(new Runnable() {

                @Override
                public void run() {
                    if(smartAd){
                        airPlay.showRichMediaInterstitialAd();
                        airPlay.showCachedAd(StoryListDetail.this, AdCallbackListener.AdType.smartwall);
                        smartAd = false;
                    }else {
                        airPlay.startSmartWallAd();
                        airPlay.showCachedAd(StoryListDetail.this, AdCallbackListener.AdType.interstitial);
                        smartAd = true;
                    }
                }

            });
        }

    }

}
