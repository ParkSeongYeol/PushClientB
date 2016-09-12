package com.example.psy.pushclientb;

import android.content.Context;
import android.widget.Toast;

public class DisplayToast implements Runnable {
	private final Context mContext;
    String mText;

    public DisplayToast(Context mContext, String text){
        this.mContext = mContext;
        mText = text;
    }
	
	@Override
	public void run() {
		// TODO Auto-generated method stub
		Toast.makeText(mContext, mText, Toast.LENGTH_SHORT).show();
	}
}
