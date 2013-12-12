package com.ruby.rkandro;

import java.util.ArrayList;
import java.util.List;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.text.Html;
import android.util.Log;
import android.widget.Toast;
import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.lmsa.cqkv143768.AdCallbackListener;
import com.lmsa.cqkv143768.AdView;
import com.lmsa.cqkv143768.AirPlay;
import com.ruby.rkandro.adapter.CategoryAdapter;
import com.ruby.rkandro.db.StoryDataSource;
import com.ruby.rkandro.pojo.Category;
import com.ruby.rkandro.pojo.Story;
import com.searchboxsdk.android.StartAppSearch;
import com.startapp.android.publish.StartAppAd;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.ruby.rkandro.soap.SoapWebServiceInfo;
import com.ruby.rkandro.soap.SoapWebServiceUtility;


public class CategoryList extends SherlockActivity implements AdCallbackListener.MraidCallbackListener {

    private ListView lv;
    private ProgressDialog progressDialog;
    final String PREFS_NAME = "GobzyPrefsFile";

    private List<Category> categoryList = new ArrayList<Category>();
    CategoryAdapter categoryAdapter;

    ConnectionDetector cd;

    private StartAppAd startAppAd = new StartAppAd(this);
    AirPlay airPlay;

    StoryDataSource dataSource;
    SharedPreferences settings;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rk_list);
        getSupportActionBar().setTitle(Html.fromHtml("<b><font color='#333333'>"+getString(R.string.app_name)+"</font></b>"));

        settings = getSharedPreferences(PREFS_NAME, 0);

        StartAppSearch.init(this);
        startAppAd.loadAd();

        dataSource = new StoryDataSource(this);
        dataSource.open();

        lv = (ListView) findViewById(R.id.lviRkList);
        lv.setOnItemClickListener(new OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {

                Category category = (Category) parent.getItemAtPosition(position);
                Intent i = new Intent(CategoryList.this, StoryList.class);
                Bundle bundle = new Bundle();
                bundle.putString("ID", category.getId().toString());
                bundle.putString("name", category.getCategoryName());
                i.putExtras(bundle);
                startActivity(i);
            }
        });

        if (settings.getBoolean("my_first_time", true)) {
            //the app is being launched for first time, do something
            Log.d("Comments", "First time :: Preparing Database.");

            // first time task
            // creating connection detector class instance
            cd = new ConnectionDetector(getApplicationContext());
            // get Internet status
            boolean isInternetPresent = cd.isConnectingToInternet();

            if (isInternetPresent) {
                // Internet Connection is Present
                new RkDetail().execute(new Object());

                airPlay=new AirPlay(this, adCallbackListener, true);
                AdView adView=(AdView)findViewById(R.id.myAdView);
                adView.setAdListener(this);
            } else {
                // Internet connection is not present
                // Ask user to connect to Internet
                showAlertDialog(CategoryList.this, "No Internet Connection",
                        "You don't have internet connection.");
            }

            // record the fact that the app has been started at least once
            settings.edit().putBoolean("my_first_time", false).commit();
        }else{
            List<Category> categories = dataSource.getCategories();

            categoryAdapter = new CategoryAdapter(CategoryList.this, categories);
            lv.setAdapter(categoryAdapter);
            categoryAdapter.notifyDataSetChanged();
        }


        airPlay=new AirPlay(this, adCallbackListener, true);
        AdView adView=(AdView)findViewById(R.id.myAdView);
        adView.setAdListener(this);
        AppRater.app_launched(CategoryList.this);
    }

    @Override
    public void onResume(){
        super.onResume();
        startAppAd.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        startAppAd.onPause();
    }

    @Override
    public void onBackPressed() {
        startAppAd.onBackPressed();
        this.finish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        menu.add("refresh").setIcon(R.drawable.ic_refresh_inverse).
                setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        String title = (String) item.getTitle();
        if (title.equalsIgnoreCase("refresh")) {
            new RkDetail().execute(new Object());
        }
        return true;
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }

    private List<Category> convertJsonToCategoryList(JSONObject jsonObject)
            throws JSONException {
        List<Category> details = new ArrayList<Category>();
        int total = (Integer) jsonObject.get("Total");
        JSONArray detailArray;
        Category category;

        for (int i = 0; i < total; i++) {
            detailArray = (JSONArray) jsonObject.get("Record" + i);
            category = new Category();
            category.setId((Integer) detailArray.get(0));
            category.setCategoryName((String) detailArray.get(1));
            details.add(category);
        }
        return details;
    }

    private List<Story> convertJsonToStoryList(JSONObject jsonObject)
            throws JSONException {
        List<Story> details = new ArrayList<Story>();
        int total = (Integer) jsonObject.get("Total");
        JSONArray detailArray;
        Story story;

        for (int i = 0; i < total; i++) {
            detailArray = (JSONArray) jsonObject.get("Record" + i);
            story = new Story();
            story.setId((Integer) detailArray.get(0));
            story.setStoryName((String) detailArray.get(1));
            story.setStoryDesc((String) detailArray.get(2));
            story.setStoryCategoryId((Integer) detailArray.get(3));
            details.add(story);
        }
        return details;
    }

    @Override
    public void onAdLoadingListener() {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void onAdLoadedListener() {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void onErrorListener(String s) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void onCloseListener() {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void onAdExpandedListner() {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void onAdClickListener() {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void noAdAvailableListener() {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    class RkDetail extends AsyncTask<Object, Void, String> {
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = ProgressDialog.show(CategoryList.this, "",
                    "Updating....", true, false);
        }

        protected String doInBackground(Object... parametros) {

            String result = null;
            try {
                String envelop = String.format(SoapWebServiceInfo.CATEGORY_LIST_ENVELOPE);
                result = SoapWebServiceUtility.callWebService(envelop, SoapWebServiceInfo.CATEGORY_LIST_SOAP_ACTION, SoapWebServiceInfo.CATEGORY_LIST_RESULT_TAG);
                if(result != null){
                    JSONObject resJsonObj = new JSONObject(result);
                    categoryList = convertJsonToCategoryList(resJsonObj);
                    dataSource.addAllCategory(categoryList);
                    if(categoryList.size() > 0){
                        List<Story> storyList;
                        for (Category category : categoryList) {
                            envelop = String.format(SoapWebServiceInfo.STORY_ENVELOPE, category.getId());
                            result = SoapWebServiceUtility.callWebService(envelop, SoapWebServiceInfo.STORY_SOAP_ACTION, SoapWebServiceInfo.STORY_RESULT_TAG);
                            if (result != null) {
                                resJsonObj = new JSONObject(result);
                                storyList = convertJsonToStoryList(resJsonObj);
                                dataSource.addStories(storyList);
                            }
                        }
                    }
                }
                settings.edit().putBoolean("my_first_time", false).commit();
            } catch (Exception e) {
                progressDialog.dismiss();
            }

            return result;
        }

        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            if (categoryList.size() > 0) {
                categoryAdapter = new CategoryAdapter(CategoryList.this, categoryList);
                lv.setAdapter(categoryAdapter);
                categoryAdapter.notifyDataSetChanged();
            }

            progressDialog.dismiss();
        }
    }


    public void showAlertDialog(Context context, String title, String message) {
        AlertDialog alertDialog = new AlertDialog.Builder(context).create();

        // Setting Dialog Title
        alertDialog.setTitle(title);

        // Setting Dialog Message
        alertDialog.setMessage(message);

        // Setting alert dialog icon
        alertDialog.setIcon(R.drawable.fail);

        // Setting OK Button
        alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                finish();
            }
        });

        // Showing Alert Message
        alertDialog.show();
    }

    AdCallbackListener adCallbackListener = new AdCallbackListener() {
        @Override
        public void onSmartWallAdShowing() {
            Toast.makeText(CategoryList.this, "onAdCached", Toast.LENGTH_SHORT).show();
            //To change body of implemented methods use File | Settings | File Templates.
        }

        @Override
        public void onSmartWallAdClosed() {
            Toast.makeText(CategoryList.this, "onSmartWallAdClosed", Toast.LENGTH_SHORT).show();
            //To change body of implemented methods use File | Settings | File Templates.
        }

        @Override
        public void onAdError(String s) {
            Toast.makeText(CategoryList.this, "onAdError"+s, Toast.LENGTH_SHORT).show();
            //To change body of implemented methods use File | Settings | File Templates.
        }

        @Override
        public void onSDKIntegrationError(String s) {
            Toast.makeText(CategoryList.this, "onSDKIntegrationError", Toast.LENGTH_SHORT).show();
            //To change body of implemented methods use File | Settings | File Templates.
        }

        @Override
        public void onVideoAdFinished() {
            Toast.makeText(CategoryList.this, "onVideoAdFinished", Toast.LENGTH_SHORT).show();
            //To change body of implemented methods use File | Settings | File Templates.
        }

        @Override
        public void onVideoAdShowing() {
            Toast.makeText(CategoryList.this, "onVideoAdShowing", Toast.LENGTH_SHORT).show();
            //To change body of implemented methods use File | Settings | File Templates.
        }

        @Override
        public void onAdCached(AdType adType) {
            Toast.makeText(CategoryList.this, "onAdCached", Toast.LENGTH_SHORT).show();
            airPlay.showCachedAd(CategoryList.this, AdType.interstitial);
        }
    };
}
