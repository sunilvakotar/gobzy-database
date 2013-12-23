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
    //ImageView closeButton;
    //RelativeLayout adsLayout;
    AirSDK airPlay;

    StoryDataSource dataSource;

    AdCallbackListener adCallbackListener = new AdCallbackListener() {
        @Override
        public void onSmartWallAdShowing() {
            //Toast.makeText(StoryListDetail.this, "onAdCached", Toast.LENGTH_SHORT).show();
            //To change body of implemented methods use File | Settings | File Templates.
        }

        @Override
        public void onSmartWallAdClosed() {
            //Toast.makeText(StoryListDetail.this, "onSmartWallAdClosed", Toast.LENGTH_SHORT).show();
            //To change body of implemented methods use File | Settings | File Templates.
        }

        @Override
        public void onAdError(String s) {
            //Toast.makeText(StoryListDetail.this, "onAdError:"+s, Toast.LENGTH_SHORT).show();
            //To change body of implemented methods use File | Settings | File Templates.
        }

        @Override
        public void onSDKIntegrationError(String s) {
            //Toast.makeText(StoryListDetail.this, "onSDKIntegrationError", Toast.LENGTH_SHORT).show();
            //To change body of implemented methods use File | Settings | File Templates.
        }

        @Override
        public void onVideoAdFinished() {
            //Toast.makeText(StoryListDetail.this, "onVideoAdFinished", Toast.LENGTH_SHORT).show();
            //To change body of implemented methods use File | Settings | File Templates.
        }

        @Override
        public void onVideoAdShowing() {
            //Toast.makeText(StoryListDetail.this, "onVideoAdShowing", Toast.LENGTH_SHORT).show();
            //To change body of implemented methods use File | Settings | File Templates.
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
        // Look up the AdView as a resource and load a request.
        //new RkDescription().execute(new Object());

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
        //airPlay.showCachedAd(StoryListDetail.this, AdCallbackListener.AdType.smartwall);
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
        //Toast.makeText(StoryListDetail.this, "onAdLoadingListener", Toast.LENGTH_SHORT).show();
        //To change body of implemented methods use File | Settings | File Templates.
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
        //Toast.makeText(StoryListDetail.this, "onErrorListener:"+s, Toast.LENGTH_SHORT).show();
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void onCloseListener() {
        //Toast.makeText(StoryListDetail.this, "onCloseListener", Toast.LENGTH_SHORT).show();
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void onAdExpandedListner() {
        //Toast.makeText(StoryListDetail.this, "onAdExpandedListner", Toast.LENGTH_SHORT).show();
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void onAdClickListener() {
        //Toast.makeText(StoryListDetail.this, "onAdClickListener", Toast.LENGTH_SHORT).show();
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void noAdAvailableListener() {
        //Toast.makeText(StoryListDetail.this, "noAdAvailableListener", Toast.LENGTH_SHORT).show();
        //To change body of implemented methods use File | Settings | File Templates.
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

    /*class RkDescription extends AsyncTask<Object, Void, String> {
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = ProgressDialog.show(StoryListDetail.this, "",
                    "Loading....", true, false);
        }

        protected String doInBackground(Object... parametros) {

            String result = null;
            try {
                String envelop = String.format(SoapWebServiceInfo.STORY_ENVELOPE, id);
                result = SoapWebServiceUtility.callWebService(envelop, SoapWebServiceInfo.STORY_SOAP_ACTION, SoapWebServiceInfo.STORY_RESULT_TAG);
                if(result != null){
                    JSONObject resJsonObj = new JSONObject(result);
                    description = convertJsonToString(resJsonObj);
                }
            } catch (Exception e) {
                progressDialog.dismiss();
            }

            return result;
        }

        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            if (description != null) {
                textDescription.setText(Html.fromHtml(description));
            }
            progressDialog.dismiss();
        }
    }

    private String convertJsonToString(JSONObject jsonObject)
            throws JSONException {
        int total = (Integer) jsonObject.get("Total");
        JSONArray detailArray;
        String desc = "";
        for (int i = 0; i < total; i++) {
            detailArray = (JSONArray) jsonObject.get("Record" + i);
            desc =  (String) detailArray.get(0);
        }
        return desc;
    }*/

}
