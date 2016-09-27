package com.example.psy.pushclientb;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import java.io.IOException;

import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.google.android.gms.common.ConnectionResult;
//import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.GooglePlayServicesUtil;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.SystemClock;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.telephony.TelephonyManager;

import com.example.psy.pushclientb.onTunePushDefines;

public class MainActivity extends AppCompatActivity {

    // member variables
    private final static int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;
    private static final String SENDER_ID = "145076158855";
    //private static final String SENDER_ID = "ontunepush";


    private GoogleCloudMessaging mGcm;
    private String mRegId = "";
    private TextView mTextStatus;
    private EditText mEditText;
    private TextView mtvPhoneNumber;


    class WebClient extends WebViewClient {
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return true;
        }
    }

    WebView webview;

    @Override
    protected void onResume() {
        super.onResume();

        onTunePushDefines.sMainActivityOn  = true;
        onTunePushDefines.sMainActivityDestroied = false;

        setSamsungBadgeCount(0);
        storeBadgeCount("");
    }

    @Override
    protected void onStop() {
        super.onPause();

        onTunePushDefines.sMainActivityOn  = false;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        onTunePushDefines.sMainActivityDestroied = true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


//		<style name="AppTheme.CustomTheme">
//		<item name = "android:windowIsTranslucent">true</item>
//	</style>

        storeRegistrationId("");

        mTextStatus = (TextView) findViewById(R.id.tvPushContent);
        mEditText = (EditText)findViewById(R.id.edPhoneNumber);
        mtvPhoneNumber = (TextView)findViewById(R.id.tvPhoneNumber);

        String phoneNumber = this.getPhoneNumber();
        Button btnPhoneNumber = (Button)findViewById(R.id.btnPhoneNumber);
        mtvPhoneNumber.setText("");

        btnPhoneNumber.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View V){
//				Intent popupIntent = new Intent(getBaseContext(), PopupActivity.class)
//						.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//
//                MainActivity.this.startActivity(popupIntent);

//				setSamsungBadgeCount(onTunePushDefines.sTempCount);
                onTunePushDefines.sTempCount++;



                final LinearLayout linear = (LinearLayout)View.inflate(MainActivity.this, R.layout.phonenum, null);

                EditText edPhoneNumber = (EditText)linear.findViewById(R.id.edPhoneNumber);
                edPhoneNumber.setText(getPhoneNumber());

                new AlertDialog.Builder(MainActivity.this)
                        .setTitle("전화번호를 입력해 주십시요")
                        .setView(linear)
                        .setCancelable(false)
                        .setPositiveButton("확인", new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                // TODO Auto-generated method stub
                                EditText edPhoneNumber = (EditText)linear.findViewById(R.id.edPhoneNumber);
                                String phoneNumber = edPhoneNumber.getText().toString();

                                if (phoneNumber == "")
                                    return;

                                storePhoneNumber(phoneNumber);

                                // 1. Device Token 등록
                                // google play service가 사용가능한가
                                if (checkPlayServices())
                                {
                                    mGcm = GoogleCloudMessaging.getInstance(MainActivity.this);
                                    mRegId = getRegistrationId();

                                    if (TextUtils.isEmpty(mRegId))
                                        registerInBackground();
                                }
                                else
                                {
                                    Log.i("MainActivity.java | onCreate", "|No valid Google Play Services APK found.|");
                                    mTextStatus.append("\n No valid Google Play Services APK found.\n");
                                }

                                // display received msg
                                String msg = getIntent().getStringExtra("msg");
                                if (!TextUtils.isEmpty(msg))
                                    mTextStatus.append("\n" + msg + "\n");

                                // 2. Web View
                                webview = (WebView)findViewById(R.id.webView1);
                                webview.setWebViewClient(new WebClient()); //
                                WebSettings set = webview.getSettings();
                                set.setJavaScriptEnabled(true);
                                set.setBuiltInZoomControls(true);
                                //webview.loadUrl("http://www.google.com");

                            }
                        })

                        .setNegativeButton("취소", new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                // TODO Auto-generated method stub
                                //mtvPhoneNumber.setText("bbb");
                            }
                        })
                        .show();

            }
        });



        if (phoneNumber != "")
        {
            // 1. Device Token 등록
            // google play service가 사용가능한가
            if (checkPlayServices())
            {
                mGcm = GoogleCloudMessaging.getInstance(this);
                mRegId = getRegistrationId();

                if (TextUtils.isEmpty(mRegId))
                    registerInBackground();
            }
            else
            {
                Log.i("MainActivity.java | onCreate", "|No valid Google Play Services APK found.|");
                mTextStatus.append("\n No valid Google Play Services APK found.\n");
            }

            // display received msg
            String msg = getIntent().getStringExtra("msg");
            if (!TextUtils.isEmpty(msg))
                mTextStatus.append("\n" + msg + "\n");

            // 2. Web View
            webview = (WebView)findViewById(R.id.webView1);
            webview.setWebViewClient(new WebClient()); //
            WebSettings set = webview.getSettings();
            set.setJavaScriptEnabled(true);
            set.setBuiltInZoomControls(true);
            //webview.loadUrl("http://www.google.com");

        }

//        while (true)
//        {
//        	if (mRegId != "")
//        		break;
//
//        	SystemClock.sleep(50);
//        }
//
//		TelephonyManager telManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
//		String phoneNumber = telManager.getLine1Number();
//        //String webviewURL = String.format("http://ontune.iptime.org:8090/message/usermessage.dll/message?userid=%s&token=%s&os=%s", phoneNumber, mRegId, "android");
//        String webviewURL = String.format("http://parkseongyeol1.iptime.org:9090/psyServlet/psyServlet?op=P&pnonenum=%s&regid=%s&os=android", phoneNumber, mRegId);
//
//        webview.loadUrl(webviewURL);
    }


    @Override
    protected void onNewIntent(Intent intent)
    {
        super.onNewIntent(intent);

        // display received msg
        String msg = intent.getStringExtra("msg");
        Log.i("MainActivity.java | onNewIntent", "|" + msg + "|");
        if (!TextUtils.isEmpty(msg))
            mTextStatus.append("\n" + msg + "\n");
    }

    // google play service가 사용가능한가
    private boolean checkPlayServices()
    {
        /*
        GoogleApiAvailability googleAPI = GoogleApiAvailability.getInstance();
        int result = googleAPI.isGooglePlayServicesAvailable(this);
        if(result != ConnectionResult.SUCCESS) {
            if(googleAPI.isUserResolvableError(result)) {
                googleAPI.getErrorDialog(this, result,
                        PLAY_SERVICES_RESOLUTION_REQUEST).show();
            }

            return false;
        }

        return true;
        */

        return true;
    }

    // registration  id를 가져온다.
    private String getRegistrationId()
    {
        String registrationId = PreferenceUtil.instance(getApplicationContext()).regId();
        if (TextUtils.isEmpty(registrationId))
        {
            Log.i("MainActivity.java | getRegistrationId", "|Registration not found.|");
            mTextStatus.append("\n Registration not found.\n");
            return "";
        }
        int registeredVersion = PreferenceUtil.instance(getApplicationContext()).appVersion();
        int currentVersion = getAppVersion();
        if (registeredVersion != currentVersion)
        {
            Log.i("MainActivity.java | getRegistrationId", "|App version changed.|");
            mTextStatus.append("\n App version changed.\n");
            return "";
        }
        return registrationId;
    }

    // phone number를 가져온다.
    public String getPhoneNumber()
    {
        String phoneNumber = PreferenceUtil.instance(getApplicationContext()).phoneNumber();
        if (TextUtils.isEmpty(phoneNumber))
        {
            Log.i("MainActivity.java | phoneNumber", "|phoneNumber not found.|");
            mTextStatus.append("\n phoneNumber not found.\n");
            return "";
        }
        int registeredVersion = PreferenceUtil.instance(getApplicationContext()).appVersion();
        int currentVersion = getAppVersion();
        if (registeredVersion != currentVersion)
        {
            Log.i("MainActivity.java | phoneNumber", "|App version changed.|");
            mTextStatus.append("\n App version changed.\n");
            return "";
        }
        return phoneNumber;
    }


    // app version을 가져온다. 뭐에 쓰는건지는 모르겠다.
    private int getAppVersion()
    {
        try
        {
            PackageInfo packageInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
            return packageInfo.versionCode;
        }
        catch (NameNotFoundException e)
        {
            // should never happen
            throw new RuntimeException("Could not get package name: " + e);
        }
    }

    // gcm 서버에 접속해서 registration id를 발급받는다.
    private void registerInBackground()
    {
        new AsyncTask<Void, Void, String>()
        {
            @Override
            protected String doInBackground(Void... params)
            {
                String msg = "";
                try
                {
                    if (mGcm == null)
                    {
                        mGcm = GoogleCloudMessaging.getInstance(getApplicationContext());
                    }
                    mRegId = mGcm.register(SENDER_ID);
                    msg = "Device registered, registration ID=" + mRegId;

                    // For this demo: we don't need to send it because the device
                    // will send upstream messages to a server that echo back the
                    // message using the 'from' address in the message.

                    // Persist the regID - no need to register again.
                    //storeRegistrationId(mRegId);
                }
                catch (IOException ex)
                {
                    msg = "Error :" + ex.getMessage();
                    // If there is an error, don't just keep trying to register.
                    // Require the user to click a button again, or perform
                    // exponential back-off.
                }

                return msg;
            }

            @Override
            protected void onPostExecute(String msg)
            {
                Log.i("MainActivity.java | onPostExecute", "|" + msg + "|");
                mTextStatus.append(msg);
                mEditText.setText(msg);
                mEditText.selectAll();

                // phone number
                //TelephonyManager telManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
                //String phoneNumber = telManager.getLine1Number();
                //phoneNumber = phoneNumber.replace('+', ' ');

                String phoneNumber = getPhoneNumber();

                if (phoneNumber == "")
                    return;

                mtvPhoneNumber.setText(phoneNumber);
                mTextStatus.append(phoneNumber);

                // set webview
                String webviewURL = String.format("http://parkseongyeol1.iptime.org:9090/psyServlet/psyServlet?op=R&phonenum=%s&regid=%s&os=android", phoneNumber, mRegId);
                webview.loadUrl(webviewURL);

            }
        }.execute(null, null, null);
    }

    // registraion id를 preference에 저장한다.
    private void storeRegistrationId(String regId)
    {
        int appVersion = getAppVersion();
        Log.i("MainActivity.java | storeRegistrationId", "|" + "Saving regId on app version " + appVersion + "|");
        PreferenceUtil.instance(getApplicationContext()).putRedId(regId);
        PreferenceUtil.instance(getApplicationContext()).putAppVersion(appVersion);
    }

    // registraion id를 preference에 저장한다.
    public void storePhoneNumber(String phoneNumber)
    {
        int appVersion = getAppVersion();
        Log.i("MainActivity.java | storePhoneNumber", "|" + "Saving phoneNumber on app version " + appVersion + "|");
        PreferenceUtil.instance(getApplicationContext()).putPhoneNumber(phoneNumber);
        PreferenceUtil.instance(getApplicationContext()).putAppVersion(appVersion);
    }

    // phone number를 가져온다.
    public String getBadgeCount()
    {
        String BadgeCount = PreferenceUtil.instance(getApplicationContext()).badgeCount();
        if (TextUtils.isEmpty(BadgeCount))
        {
            Log.i("GcmIntentService.java | BadgeCount", "|BadgeCount not found.|");
            //mTextStatus.append("\n phoneNumber not found.\n");
            return "0";
        }

//		int registeredVersion = PreferenceUtil.instance(getApplicationContext()).appVersion();
//		int currentVersion = getAppVersion();
//		if (registeredVersion != currentVersion)
//		{
//			 Log.i("MainActivity.java | phoneNumber", "|App version changed.|");
//			 mTextStatus.append("\n App version changed.\n");
//			 return "";
//		}
        return BadgeCount;
    }

    private void storeBadgeCount(String ABadgeCountStr)
    {
        //int appVersion = getAppVersion();
        //Log.i("MainActivity.java | storeRegistrationId", "|" + "Saving regId on app version " + appVersion + "|");
        PreferenceUtil.instance(getApplicationContext()).putBadgeCount(ABadgeCountStr);
        //PreferenceUtil.instance(getApplicationContext()).putAppVersion(appVersion);
    }

    public void setSamsungBadgeCount(int Acount)
    {
        Object android;
        Intent intent = new Intent("android.intent.action.BADGE_COUNT_UPDATE");
        intent.putExtra("badge_count", Acount);
        // 메인 메뉴에 나타나는 어플의  패키지 명

        String PakagName = onTunePushDefines.sPakageName;
        String ClassName = onTunePushDefines.sMainActivityClassName;
        intent.putExtra("badge_count_package_name", PakagName);
        // 메인메뉴에 나타나는 어플의 클래스 명
        intent.putExtra("badge_count_class_name", ClassName);
        sendBroadcast(intent);
    }
}
