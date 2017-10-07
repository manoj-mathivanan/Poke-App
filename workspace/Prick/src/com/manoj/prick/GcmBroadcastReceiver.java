package com.manoj.prick;

import org.json.JSONObject;

import android.app.Activity;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.content.WakefulBroadcastReceiver;


public class GcmBroadcastReceiver extends WakefulBroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        // Explicitly specify that GcmIntentService will handle the intent.
    	try{
    		String message = "";
        ComponentName comp = new ComponentName(context.getPackageName(),
                GCMIntentService.class.getName());
        // Start the service, keeping the device awake while it is launching.
        startWakefulService(context, (intent.setComponent(comp)));
        setResultCode(Activity.RESULT_OK);
        String number = intent.getStringExtra("message");
       
        JSONObject poke = new JSONObject(number);
        DataBaseHandler db = new DataBaseHandler(context);
        if(globals.contactsmain == null)
        	globals.contactsmain = db.getAllContacts();
        if(number.contains("messager"))
        {
        	String messager = poke.getString("messager");
        	String msg = poke.getString("message");
        	
        	
        	int i=globals.getposition(poke.getString("messager"));
        	if(i==-1)
        	{
        		LoadContacts lc = new LoadContacts();
        		Contact cnt = new Contact(lc.getContactName(poke.getString("messager")),poke.getString("messager"));
        		globals.contactsmain.add(cnt);
        		db.addContact(cnt);
        	}
        	i=globals.getposition(poke.getString("messager"));
        	globals.contactsmain.get(i).setLastTime(System.currentTimeMillis());
        	if(globals.contactsmain.get(i).getNew_message().equals("yes"))
        	{
        		msg = globals.contactsmain.get(i).getMessage() + "\n" + msg; 
        	}
            globals.contactsmain.get(i).setMessage(msg);
            globals.contactsmain.get(i).setNew_message("yes");
            db.updateMessage(messager, msg);
        	db.updateNewMessage(messager, "yes");
            message="";
            message =  "Message:\n"+globals.contactsmain.get(i).getContactName() + " - " + msg;
            Intent intent2 = new Intent("update-view");
      	  // You can also include some extra data.
            
      	  intent2.putExtra("type", "message");
      	  LocalBroadcastManager.getInstance(context).sendBroadcast(intent2);
        }
        else if (number.contains("poker"))
        {
        	int i=globals.getposition(poke.getString("poker"));
        	if(i==-1)
        	{
        		LoadContacts lc = new LoadContacts();
        		Contact cnt = new Contact(lc.getContactName(poke.getString("poker")),poke.getString("poker"));
        		globals.contactsmain.add(cnt);
        		db.addContact(cnt);
        	}
        Contact newpokes = db.updateRecPokes(poke.getString("poker"), Integer.parseInt(poke.getString("sent")));
        db.updateSentPoke(poke.getString("poker"), Integer.parseInt(poke.getString("recv")));
        int flag =0;
        for(i=0;i<globals.newpokescontact.size();i++)
        {
        	if(globals.newpokescontact.get(i).getContactNumber().equalsIgnoreCase(poke.getString("poker")))
        	{
        		globals.newpokescontact.get(i).setNewpokes(globals.newpokescontact.get(i).getNewpokes()+Integer.parseInt(newpokes.getContactNumber()));
        		flag=1;
        	}
        }
        if(flag==0)
        {
        	globals.newpokescontact.add(new Contact(newpokes.getContactName(), poke.getString("poker"), 0, 0, System.currentTimeMillis(), Integer.parseInt(newpokes.getContactNumber())));
        }
        db.updateNewPoken(poke.getString("poker"), Integer.parseInt(newpokes.getContactNumber()));
        
        i=globals.getposition(poke.getString("poker"));
        globals.contactsmain.get(i).setSentPoke( Integer.parseInt(poke.getString("recv")));
        globals.contactsmain.get(i).setRecvPoke(globals.contactsmain.get(i).getRecvPoke()+Integer.parseInt(newpokes.getContactNumber()));
        globals.contactsmain.get(i).setLastTime(System.currentTimeMillis());
		globals.contactsmain.get(i).setNewpokes(globals.contactsmain.get(i).getNewpokes()+Integer.parseInt(newpokes.getContactNumber()));
        
        message ="";
        flag=0;
        for(i=0;i<globals.newpokescontact.size();i++)
        {
        	if(flag==0)
        	{
        		flag=1;
        		message = message + globals.newpokescontact.get(i).getNewpokes() + " new pricks from " + globals.newpokescontact.get(i).getContactName();
        	}
        	else
        		message = message + "\n" + globals.newpokescontact.get(i).getNewpokes() + " new pricks from " + globals.newpokescontact.get(i).getContactName();
        }
        Intent intent2 = new Intent("update-view");
        //intent2.addFlags(Intent.FLAG_INCLUDE_STOPPED_PACKAGES);
  	  // You can also include some extra data.
  	  intent2.putExtra("type", "poke");
  	  LocalBroadcastManager.getInstance(context).sendBroadcast(intent2);
        }
        else if(number.contains("update"))
        {
        	//read from shared preferences
        }
        if(globals.minimized == 1)
        {
		NotificationManager mNotificationManager = (NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE);
        PendingIntent contentIntent = PendingIntent.getActivity(context, 0,new Intent(context, poke_grid.class),0);
        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(context)
        .setSmallIcon(R.drawable.poke)
        .setContentTitle("Poke")
        .setStyle(new NotificationCompat.BigTextStyle()
        .bigText(message))
        .setContentText(message);
        mBuilder.setAutoCancel(true);
        mBuilder.setContentIntent(contentIntent);
        mNotificationManager.notify(1, mBuilder.build());
        Uri notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
    	Ringtone r = RingtoneManager.getRingtone(context, notification);
    	r.play();
        }
        //MediaPlayer mp = MediaPlayer.create(this, R.raw.ouch2);
        //mp.start();
    	
    	
       
    	}
    	catch(Exception e)
    	{
    		e.printStackTrace();
    	}
    }
}
