package com.askntake.admin.askntake;


import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.WakefulBroadcastReceiver;
import com.askntake.badger.ShortcutBadger;

public class OwnerGcmBroadcastReceiver extends WakefulBroadcastReceiver {
	@Override
	public void onReceive(Context context, Intent intent) {

		// Explicitly specify that GcmIntentService will handle the intent.
		ComponentName comp = new ComponentName(context.getPackageName(),
				GcmIntentService.class.getName());
		// Start the service, keeping the device awake while it is launching.
		startWakefulService(context, (intent.setComponent(comp)));
		setResultCode(Activity.RESULT_OK);
		
		Bundle extras = intent.getExtras();
		
		if(extras.getString("unreadcount")!=null)
		{
			if(!extras.getString("unreadcount").equalsIgnoreCase("0"));
			{
				ShortcutBadger.applyCount(context, Integer.parseInt(extras.getString("unreadcount")));
			}
		}
		Intent i = new Intent("CHAT_MESSAGE_RECEIVED_IN_HISTORY");
		i.putExtra("message", extras.getString("message"));
		i.putExtra("productId", extras.getString("productId"));
		i.putExtra("messageFrom", extras.getString("messageFrom"));
		i.putExtra("productImage", extras.getString("productImage"));
		i.putExtra("productName", extras.getString("productName"));
		i.putExtra("unreadcount", extras.getString("unreadcount"));
		context.sendBroadcast(i);
		
		
		
		
	}
}