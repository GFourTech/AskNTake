package com.askntake.admin.askntake;


import android.app.IntentService;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.google.android.gms.gcm.GoogleCloudMessaging;

import AppUtils.AppConstants;
import AppUtils.DataKeyValues;

public class GcmIntentService extends IntentService {

	public static final int NOTIFICATION_ID = 1;
	private NotificationManager mNotificationManager;
	NotificationCompat.Builder builder;
	public static final String TAG = "GcmIntentService";
//	SharedPreferences prefs =null;
	public GcmIntentService() {
		super("GcmIntentService");
	}

	@Override
	protected void onHandleIntent(Intent intent) {

		Bundle extras = intent.getExtras();
		GoogleCloudMessaging gcm = GoogleCloudMessaging.getInstance(this);
		// The getMessageType() intent parameter must be the intent you received
		// in your BroadcastReceiver.
		String messageType = gcm.getMessageType(intent);

		if (!extras.isEmpty()) { // has effect of unparcelling Bundle
			/*
			 * Filter messages based on message type. Since it is likely that
			 * GCM will be extended in the future with new message types, just
			 * ignore any message types you're not interested in, or that you
			 * don't recognize.
			 */
			
			
			
			if (GoogleCloudMessaging.MESSAGE_TYPE_SEND_ERROR
					.equals(messageType)) {
				sendNotification(extras.getString("messageFrom"),extras.toString(),extras.getString("productId"),extras.getString("messageFromFname"),extras.getString("productImage"),extras.getString("productName"));
			} else if (GoogleCloudMessaging.MESSAGE_TYPE_DELETED
					.equals(messageType)) {
				sendNotification(extras.getString("messageFrom"),extras.toString(),extras.getString("registrationIDs"),extras.getString("messageFromFname"),extras.getString("productImage"),extras.getString("productName"));
				// If it's a regular GCM message, do some work.
			} else if (GoogleCloudMessaging.MESSAGE_TYPE_MESSAGE
					.equals(messageType)) {
				sendNotification(extras.getString("messageFrom"),extras.getString("message"),extras.getString("productId"),extras.getString("messageFromFname"),extras.getString("productImage"),extras.getString("productName"));
				Log.i(TAG, "Received: " + extras.toString());
			}
		}
		// Release the wake lock provided by the WakefulBroadcastReceiver.
		GcmBroadcastReceiver.completeWakefulIntent(intent);


	}

	// Put the message into a notification and post it.
	// This is just one simple example of what you might choose to do with
	// a GCM message.
	private void sendNotification(String messageFrom,String msg,String productId,String fname,String product_image,String product_name) {

	
		
		mNotificationManager = (NotificationManager) this
				.getSystemService(Context.NOTIFICATION_SERVICE);

		Intent intent=new Intent(this, MessagesActivity.class);
		
		SharedPreferences fb_data_pref = AppConstants.preferencesData(getApplicationContext());
		String UserId_Main=fb_data_pref.getString(DataKeyValues.USER_USERID, null);

		String	userFname=fb_data_pref.getString("fb_username", null);
		intent.putExtra("chattingFrom",userFname);
		intent.putExtra("chattingToName", fname);
		intent.putExtra("userIdFrom",UserId_Main);
		intent.putExtra("userIdTo", messageFrom);
		intent.putExtra("itemId",productId);
		intent.putExtra("itemName",product_name);
		intent.putExtra("requestFrom", "description_scr");
		intent.putExtra("itemImage",product_image);
		
		
		//set badget for specified divices
		
		
		
		
		
		//userFname=fb_data_pref.getString("fb_username", null);
		/*intent.putExtra("chattingFrom",userFname);
		intent.putExtra("chattingToName", owner_firstname);
		intent.putExtra("userIdFrom",UserId_Main);
		intent.putExtra("userIdTo", ownerId);
		intent.putExtra("itemId",itemId);
		intent.putExtra("itemName",item_name);
		intent.putExtra("requestFrom", "description_scr");
		intent.putExtra("itemImage",AppUtils.IMG_BASE_URL+itemImages[0]);*/
		
		
		PendingIntent contentIntent = PendingIntent.getActivity(this, 0,intent, PendingIntent.FLAG_UPDATE_CURRENT);

		Uri soundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

		NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(
				this).setSmallIcon(R.mipmap.luncher_img2)
				.setContentTitle("Ask-n-Take")
				.setStyle(new NotificationCompat.BigTextStyle().bigText(msg))
				.setContentText(msg)
				.setSound(soundUri)
				.setLights(Color.BLUE, 500, 500)
				.setAutoCancel(true);

		mBuilder.setContentIntent(contentIntent);
		
	
		mNotificationManager.notify(NOTIFICATION_ID, mBuilder.build());

		
		
	}
	
	
}