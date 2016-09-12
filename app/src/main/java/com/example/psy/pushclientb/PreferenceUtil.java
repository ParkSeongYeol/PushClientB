package com.example.psy.pushclientb;

import android.content.Context;

public class PreferenceUtil extends BasePreferenceUtil {
	  private static PreferenceUtil _instance = null;
	  
	  private static final String PROPERTY_REG_ID = "registration_id";
	  private static final String PROPERTY_APP_VERSION = "appVersion";
	  private static final String PROPERTY_PHONE_NUMBER = "phone_number";
	  private static final String PROPERTY_BADGE_COUNT = "badge_number";
	  
	  
	  public static synchronized PreferenceUtil instance(Context $context)
	  {
	    if (_instance == null)
	      _instance = new PreferenceUtil($context);
	    return _instance;
	  }
	  
	  
	  protected PreferenceUtil(Context $context)
	  {
	    super($context);
	  }
	  
	  
	  public void putRedId(String $regId)
	  {
	    put(PROPERTY_REG_ID, $regId);
	  }
	  
	  public String regId()
	  {
	    return get(PROPERTY_REG_ID);
	  }
	 
	  public void putPhoneNumber(String $phoneNumber)
	  {
	    put(PROPERTY_PHONE_NUMBER, $phoneNumber);
	  }
	  
	  public String phoneNumber()
	  {
	    return get(PROPERTY_PHONE_NUMBER);
	  }
	  
	  public void putBadgeCount(String $BadgeCountStr)
	  {
	    put(PROPERTY_BADGE_COUNT, $BadgeCountStr);
	  }
	  
	  public String badgeCount()
	  {
	    return get(PROPERTY_BADGE_COUNT);
	  }
	  
	  public void putAppVersion(int $appVersion)
	  {
	    put(PROPERTY_APP_VERSION, $appVersion);
	  }
	  
	  
	  public int appVersion()
	  {
	    return get(PROPERTY_APP_VERSION, Integer.MIN_VALUE);
	  }
}
