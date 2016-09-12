package com.example.psy.pushclientb;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class StartReceiver extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {
		// TODO Auto-generated method stub

		//수신받은 방송(Broadcast)의 Action을 얻어오기
		//메니페스트 파일안에 이 Receiver에게 적용된 필터(Filter)의 Action만 받아오게 되어 있음.
		String action= intent.getAction();
		
		//수신된 action값이 시스템의 '부팅 완료'가 맞는지 확인..
		if (action.equals("android.intent.action.BOOT_COMPLETED"))
		{
		 //맞다면...MainActivity 실행을 위한 Intent 생성..
		 Intent i= new Intent(context, MainActivity.class);   
		 //위에 만들어진 Intent에 의해 실행되는 Activity는 
		 //액티비티 스택에서 새로운 Task로 Activity를 실행하도록 하라는 설정.
		 i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		 
		 //Intent에 설정된 Component(여기서는 MainActivity)를 실행
		 context.startActivity(i);
		}
	}
}
