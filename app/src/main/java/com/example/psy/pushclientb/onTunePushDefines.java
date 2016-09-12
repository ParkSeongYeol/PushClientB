package com.example.psy.pushclientb;

import android.content.Intent;
import android.text.TextUtils;
import android.util.Log;

public class onTunePushDefines {
	static String sPushMessage = "undefined";
	static boolean sPopupFlag = true;
	static int sPopoupTime = 5000;
	static boolean sMainActivityOn = false;
	static boolean sMainActivityDestroied = true;
	static int sTempCount = 1;
	static String sPakageName = "com.example.psy.pushclientb";
	static String sMainActivityClassName = "com.example.psy.pushclientb.MainActivity";
	
	static boolean isPopup()
	{
		return true;
	}

}
