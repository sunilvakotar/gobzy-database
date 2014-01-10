package com.ruby.gobzy.sync;

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
import com.ukfc.klno167797.AdCallbackListener;
import com.ukfc.klno167797.AdView;
import com.ukfc.klno167797.AirSDK;
import com.ruby.gobzy.sync.adapter.CategoryAdapter;
import com.ruby.gobzy.sync.db.StoryDataSource;
import com.ruby.gobzy.sync.pojo.Category;
import com.ruby.gobzy.sync.pojo.Story;
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

import com.ruby.gobzy.sync.soap.SoapWebServiceInfo;
import com.ruby.gobzy.sync.soap.SoapWebServiceUtility;


public class CategoryList extends SherlockActivity implements AdCallbackListener.MraidCallbackListener {

    private ListView lv;
    private ProgressDialog progressDialog;
    final String PREFS_NAME = "GobzyPrefsFile";

    private List<Category> categoryList = new ArrayList<Category>();
    CategoryAdapter categoryAdapter;

    ConnectionDetector cd;

    private StartAppAd startAppAd = new StartAppAd(this);
    AirSDK airPlay;

    StoryDataSource dataSource;
    SharedPreferences settings;
    private boolean checkUpdate = false;

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

        // first time task
        // creating connection detector class instance
        cd = new ConnectionDetector(getApplicationContext());
        // get Internet status
        boolean isInternetPresent = cd.isConnectingToInternet();

        if (settings.getBoolean("my_first_time", true)) {
            //the app is being launched for first time, do something
            Log.d("Comments", "First time :: Preparing Database.");

            if (isInternetPresent) {
                // Internet Connection is Present
                new RkDetail().execute(new Object());

            } else {
                // Internet connection is not present
                // Ask user to connect to Internet
                showAlertDialog(CategoryList.this, "No Internet Connection",
                        "You don't have internet connection.");
            }

            // record the fact that the app has been started at least once
            settings.edit().putBoolean("my_first_time", false).commit();
        }else{
            if (isInternetPresent) {
                checkUpdate = true;
                // Internet Connection is Present
                new RkDetail().execute(new Object());
            }
            List<Category> categories = dataSource.getCategories();
            categoryAdapter = new CategoryAdapter(CategoryList.this, categories);
            lv.setAdapter(categoryAdapter);
            categoryAdapter.notifyDataSetChanged();
        }


        airPlay=new AirSDK(this, adCallbackListener, true);
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
        super.onBackPressed();
        startAppAd.onBackPressed();
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
            String date = (String) detailArray.get(4);
            story.setCreateDate(Long.parseLong(date.substring(date.indexOf('(')+1,date.indexOf(')'))));
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

    int total = 0;
    int dbCount = 0;
    class RkDetail extends AsyncTask<Object, Void, String> {
        protected void onPreExecute() {
            super.onPreExecute();
            if (!checkUpdate) {
                progressDialog = ProgressDialog.show(CategoryList.this, "",
                        "Updating....", true, false);
            }
        }

        protected String doInBackground(Object... parametros) {

            String result = null;
            try {
                if(checkUpdate){
                    String envelop = String.format(SoapWebServiceInfo.CHECK_UPDATE_ENVELOPE);
                    result = SoapWebServiceUtility.callWebService(envelop, SoapWebServiceInfo.CHECK_UPDATE_ACTION, SoapWebServiceInfo.CHECK_UPDATE_RESULT_TAG);
                    if(result != null){
                        JSONObject resJsonObj = new JSONObject(result);
                        total = (Integer) resJsonObj.get("Total");
                        dbCount = dataSource.getStoryCount();

                    }
                }else{
                    String envelop = String.format(SoapWebServiceInfo.CATEGORY_LIST_ENVELOPE);
                    result = SoapWebServiceUtility.callWebService(envelop, SoapWebServiceInfo.CATEGORY_LIST_SOAP_ACTION, SoapWebServiceInfo.CATEGORY_LIST_RESULT_TAG);
                    if(result != null){
                        JSONObject resJsonObj = new JSONObject(result);
                        categoryList = convertJsonToCategoryList(resJsonObj);
                        if(categoryList.size() > 0){
                            dataSource.addAllCategory(categoryList);
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
                }
            } catch (Exception e) {
                if (!checkUpdate) {
                    progressDialog.dismiss();
                }
            }

            return result;
        }

        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            if (checkUpdate) {
                if(total > dbCount){
                    showAlertDialog(CategoryList.this, "Update", "New Story Available on cloud. You can update stories by clicking Update button below.");
                }
                checkUpdate = false;
            } else {
                if (categoryList.size() > 0) {
                    categoryAdapter = new CategoryAdapter(CategoryList.this, categoryList);
                    lv.setAdapter(categoryAdapter);
                    categoryAdapter.notifyDataSetChanged();
                }
                progressDialog.dismiss();
            }


        }
    }

    private AlertDialog alertDialog;
    public void showAlertDialog(Context context, String title, String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        //alertDialog = new AlertDialog.Builder(context).create();

        // Setting Dialog Title
        builder.setTitle(title);

        // Setting Dialog Message
        builder.setMessage(message);


        // Setting alert dialog icon
        if(checkUpdate){
            builder.setPositiveButton("Update", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    new RkDetail().execute(new Object());
                }
            });
            builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
        }else {
            alertDialog.setIcon(R.drawable.fail);
            // Setting OK Button
            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
        }

        alertDialog = builder.create();
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
            //Toast.makeText(CategoryList.this, "onSmartWallAdClosed", Toast.LENGTH_SHORT).show();
            //To change body of implemented methods use File | Settings | File Templates.
        }

        @Override
        public void onAdError(String s) {
            //Toast.makeText(CategoryList.this, "onAdError"+s, Toast.LENGTH_SHORT).show();
            //To change body of implemented methods use File | Settings | File Templates.
        }

        @Override
        public void onSDKIntegrationError(String s) {
            //Toast.makeText(CategoryList.this, "onSDKIntegrationError", Toast.LENGTH_SHORT).show();
            //To change body of implemented methods use File | Settings | File Templates.
        }

        @Override
        public void onVideoAdFinished() {
            //Toast.makeText(CategoryList.this, "onVideoAdFinished", Toast.LENGTH_SHORT).show();
            //To change body of implemented methods use File | Settings | File Templates.
        }

        @Override
        public void onVideoAdShowing() {
            //Toast.makeText(CategoryList.this, "onVideoAdShowing", Toast.LENGTH_SHORT).show();
            //To change body of implemented methods use File | Settings | File Templates.
        }

        @Override
        public void onAdCached(AdType adType) {
            //Toast.makeText(CategoryList.this, "onAdCached", Toast.LENGTH_SHORT).show();
            airPlay.showCachedAd(CategoryList.this, AdType.interstitial);
        }
    };
}
