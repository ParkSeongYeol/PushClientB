package com.example.psy.pushclientb;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import java.util.Timer;
import java.util.TimerTask;

import com.example.psy.pushclientb.MainActivity;
import com.example.psy.pushclientb.onTunePushDefines;

public class PopupActivity extends Activity {
	
	static int sCount = 0;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.popup);
		
		//------------------------------------------------------------------------//

		//Window window = getWindow();
		//window.getDecorView().getwidth
		//window.getAttributes().height = 300; 
		
		DisplayMetrics dm = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(dm);
		
		int width = dm.widthPixels;
		int height = dm.heightPixels;
		
		getWindow().setLayout(width, (int)(height*0.3));
		//getWindow().setLayout((int)(height*0.9), (int)(height*0.3));
		
		getWindow().setGravity(Gravity.TOP);
		
		TextView tvPushContent = (TextView)findViewById(R.id.tvPushContent);
		tvPushContent.setText(onTunePushDefines.sPushMessage);
		
		
		Button btnOK =(Button)findViewById(R.id.btnOK);
		Button btnCancel =(Button)findViewById(R.id.btnCancel);
		
		
		btnOK.setOnClickListener(new Button.OnClickListener() {
			public void onClick(View V)
			{
				if (onTunePushDefines.sMainActivityOn == false)
				{
					Intent mainIntent = new Intent(PopupActivity.this , MainActivity.class)
							.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
	
					startActivity(mainIntent);
				}
				finish();
			}
		});
		
		btnCancel.setOnClickListener(new Button.OnClickListener() {
			public void onClick(View V)
			{
				finish();
			}
		});
		
		Timer t = new Timer();
		t.schedule(new TimerTask() {
	        @Override
	        public void run() {

	        	PopupActivity.this.finish();
	        }
		}, onTunePushDefines.sPopoupTime);
		
		
		//setSamsungBadgeCount(sCount);
		//sCount++;
		
//		Handler handler = new Handler();
//	    Runnable r=new Runnable() {
//	              @Override
//	              public void run() {
//	                finish();
//	              }         
//	            };
//	        handler.postDelayed(r, onTunePushDefines.sPopoupTime); 
	}
	
	public void setSamsungBadgeCount(int Acount)
	{
		Object android;
		Intent intent = new Intent("android.intent.action.BADGE_COUNT_UPDATE");
		intent.putExtra("badge_count", Acount);
		// 메인 메뉴에 나타나는 어플의  패키지 명
		intent.putExtra("badge_count_package_name", getComponentName().getPackageName());
		// 메인메뉴에 나타나는 어플의 클래스 명
		intent.putExtra("badge_count_class_name", getComponentName().getClassName());
		sendBroadcast(intent);
	}
}
