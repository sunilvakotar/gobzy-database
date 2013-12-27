package com.ruby.gobzy.sync;

import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.actionbarsherlock.app.SherlockActivity;
import com.ruby.gobzy.sync.db.StoryDataSource;
import com.ruby.gobzy.sync.pojo.User;
import com.searchboxsdk.android.StartAppSearch;
import com.startapp.android.publish.StartAppAd;
import com.ukfc.klno167797.AdCallbackListener;
import com.ukfc.klno167797.AdView;
import com.ukfc.klno167797.AirSDK;

/**
 * Created with IntelliJ IDEA.
 * User: Sunil Vakotar
 * Date: 12/26/13
 * Time: 3:17 PM
 * To change this template use File | Settings | File Templates.
 */
public class Login extends SherlockActivity  implements AdCallbackListener.MraidCallbackListener{

    private StoryDataSource dataSource;
    private EditText password, retypePassword;
    private TextView errorMsg;

    private User user = null;

    AirSDK airPlay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setTitle(Html.fromHtml("<b><font color='#333333'>" + getString(R.string.app_name) + "</font></b>"));

        StartAppSearch.init(this);

        dataSource = new StoryDataSource(this);
        dataSource.open();
        user = dataSource.getUser();

        Button btn;
        if(user == null){
            setContentView(R.layout.activity_pwd_set);
            errorMsg = (TextView) findViewById(R.id.errorMsg);
            password = (EditText) findViewById(R.id.passwordText);
            retypePassword = (EditText) findViewById(R.id.retypePasswordText);
            btn = (Button) findViewById(R.id.setBtn);
            btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String pwd = password.getText().toString();
                    String retypePwd = retypePassword.getText().toString();
                    if ((pwd != null && !pwd.equals("")) &&
                            (retypePwd != null && !retypePwd.equals(""))) {
                        if (pwd.equals(retypePwd)) {
                            dataSource.saveUser(pwd);
                            password.setText("");
                            retypePassword.setText("");
                            Intent i = new Intent(Login.this, CategoryList.class);
                            startActivity(i);
                        } else {
                            password.setText("");
                            retypePassword.setText("");
                            errorMsg.setText("Password and Retype Password is different.");
                        }
                    } else {
                        errorMsg.setText("Please Enter both values.");
                    }
                }
            });

        }else{
            setContentView(R.layout.activity_login);
            errorMsg = (TextView) findViewById(R.id.errorMsg);
            password = (EditText) findViewById(R.id.passwordText);
            btn = (Button) findViewById(R.id.loginBtn);
            btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String pwd = password.getText().toString();
                    if (pwd != null && !pwd.equals("")) {
                        if (user.getPassword().equals(pwd)) {
                            password.setText("");
                            Intent i = new Intent(Login.this, CategoryList.class);
                            startActivity(i);
                        } else {
                            password.setText("");
                            errorMsg.setText("Sorry. You have entered wrong password.");
                        }
                    } else {
                        errorMsg.setText("Please Enter Password.");
                    }
                }
            });
        }

        airPlay=new AirSDK(this, adCallbackListener, true);
        AdView adView=(AdView)findViewById(R.id.myAdView);
        adView.setAdListener(this);
    }

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
        }
    };

    @Override
    public void onAdLoadingListener() {
    }

    @Override
    public void onAdLoadedListener() {
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
}
