package com.example.psy.pushclientb;

import com.google.android.gms.gcm.GoogleCloudMessaging;

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.PowerManager;
import android.os.PowerManager.WakeLock;
import android.support.v4.app.NotificationCompat;
import android.text.TextUtils;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;
import com.example.psy.pushclientb.DisplayToast;

import com.example.psy.pushclientb.onTunePushDefines;
import com.example.psy.pushclientb.PopupActivity;

public class GcmIntentService extends IntentService {
	public static final int NOTIFICATION_ID = 1;
	Handler mHandler;
	
	public GcmIntentService()
	{
		super("GcmIntentService");
		mHandler = new Handler();
	}	

	@Override
	protected void onHandleIntent(Intent intent) {
		// TODO Auto-generated method stub

		Bundle extras = intent.getExtras();
		GoogleCloudMessaging gcm = GoogleCloudMessaging.getInstance(this);
		// The getMessageType() intent parameter must be the intent you received
		// in your BroadcastReceiver.
		String messageType = gcm.getMessageType(intent);

		if (!extras.isEmpty())
		{ // has effect of unparcelling Bundle
			/*
			* Filter messages based on message type. Since it is likely that GCM
			* will be extended in the future with new message types, just ignore
			* any message types you're not interested in, or that you don't
			* recognize.
			*/
			
			
			if (GoogleCloudMessaging.MESSAGE_TYPE_SEND_ERROR.equals(messageType))
			{
				sendNotification("Send error: " + extras.toString());
			}
			else if (GoogleCloudMessaging.MESSAGE_TYPE_DELETED.equals(messageType))
			{
				sendNotification("Deleted messages on server: " + extras.toString());
			// If it's a regular GCM message, do some work.
			}
			else if (GoogleCloudMessaging.MESSAGE_TYPE_MESSAGE.equals(messageType))
			{
				String msg = intent.getStringExtra("msg");
				// Post notification of received message.
				//            sendNotification("Received: " + extras.toString());
				//sendNotification("Received: " + msg);
				//sendNotification("Received: " + onTunePushDefines.sPushMessage);
				sendNotification(onTunePushDefines.sPushMessage);
				Log.i("GcmIntentService.java | onHandleIntent", "Received: " + onTunePushDefines.sPushMessage);
			}
			
		}
		// Release the wake lock provided by the WakefulBroadcastReceiver.
		GcmBroadcastReceiver.completeWakefulIntent(intent);
		
		//mHandler.post(new DisplayToast(this, "onTuneClientA received: " + onTunePushDefines.sPushMessage));
		
		if (onTunePushDefines.sMainActivityOn == true)
		{
			setSamsungBadgeCount(0);
			storeBadgeCount("0");
		}
		else
		{
			String BadgeCountStr = getBadgeCount() ;
			int BadgeCount = Integer.parseInt(BadgeCountStr);
			BadgeCount++;
			
			setSamsungBadgeCount(BadgeCount);
			BadgeCountStr = String.valueOf(BadgeCount);
			
			storeBadgeCount(BadgeCountStr);			
		}
		
		//-----------  popup --------------
		if (onTunePushDefines.isPopup())
		{
			Intent popupIntent = new Intent(getBaseContext() , PopupActivity.class)
											.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			
			//Intent popupIntent = new Intent(this, PopupActivity.class)
			//								.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			
			this.startActivity(popupIntent);
		}
		
		
	}
	
	
	// phone number�� �����´�.
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
	
	
	
	// Put the message into a notification and post it.
	// This is just one simple example of what you might choose to do with
	// a GCM message.
	private void sendNotification(String msg)
	{
		PowerManager pm = (PowerManager) this.getSystemService(Context.POWER_SERVICE);

        WakeLock wl = pm.newWakeLock(PowerManager.SCREEN_DIM_WAKE_LOCK | PowerManager.ACQUIRE_CAUSES_WAKEUP, "TAG");
        wl.acquire(5000);
				
		NotificationManager mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

		Intent intent = new Intent(getApplicationContext(), MainActivity.class);
		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		intent.putExtra("msg", msg);

		PendingIntent contentIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

		NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this).setSmallIcon(R.mipmap.ic_launcher)
																				//.setContentTitle("GCM Notification")
																				.setContentTitle("onTuneClientB")
																				.setStyle(new NotificationCompat.BigTextStyle().bigText(msg))
																				.setContentText(msg)
																				.setAutoCancel(true)
																				.setVibrate(new long[] { 0, 500 });

//		mBuilder.setDefaults(Notification.DEFAULT_SOUND);
//		Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
//		mBuilder.setSound(alarmSound);
		
		mBuilder.setContentIntent(contentIntent);
		Notification notification = mBuilder.build();
//		notification.defaults |= Notification.DEFAULT_SOUND;
		
		//MediaPlayer mp= MediaPlayer.create(this, R.raw.sirens);
        //mp.start();
		
		mNotificationManager.notify(NOTIFICATION_ID, notification);
	}
	
	public void setSamsungBadgeCount(int Acount)
	{
		Object android;
		Intent intent = new Intent("android.intent.action.BADGE_COUNT_UPDATE");
		intent.putExtra("badge_count", Acount);
		// ���� �޴��� ��Ÿ���� ������  ��Ű�� ��
		
		String PakagName = onTunePushDefines.sPakageName; 
		String ClassName = onTunePushDefines.sMainActivityClassName;
		intent.putExtra("badge_count_package_name", PakagName);
		// ���θ޴��� ��Ÿ���� ������ Ŭ���� ��
		intent.putExtra("badge_count_class_name", ClassName);
		sendBroadcast(intent);
	}
}
