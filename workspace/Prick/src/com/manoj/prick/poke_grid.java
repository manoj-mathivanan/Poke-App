package com.manoj.prick;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.concurrent.atomic.AtomicInteger;

import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicHttpResponse;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.gcm.GoogleCloudMessaging;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlarmManager;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.StrictMode;
import android.os.SystemClock;
import android.preference.PreferenceManager;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.PopupMenu.OnMenuItemClickListener;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class poke_grid extends Activity {
	NotificationManager mNotificationManager;
	//ArrayList<Contact> contacts;
	adapter adapter;
	DataBaseHandler db ;
	ListView dataGrid;
	//MediaPlayer mp;
	int rflag=0;
	static SharedPreferences mPrefs;
	final String GCM = "GCM";
	IntentFilter gcmFilter;
	//private InterstitialAd interstitial;
	//PublisherAdView bannerad = new BannerAd();
	int aflag=0;
	Activity activity;
	ProgressDialog progress;
	static TextView level;
	HttpClient client;// = new DefaultHttpClient();
	CountDownTimer countDownTimer;
	CountDownTimer refreshcountDownTimer;
	Uri notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
	static TextProgressBar pbpokes;
	static TextView pokestonextlevel;
	ImageView refimage;
	//TextProgressBar pbcontacts;
	//ProgressBar levelbar;
	RelativeLayout viewGroup;
	GoogleCloudMessaging gcm;
    SharedPreferences prefs;
    SharedPreferences.Editor editor;
    Context context;
    String regid;
    AdView mAdView;
    AdRequest adRequest;
    int sendpokeflag =0;
	public static final String EXTRA_MESSAGE = "message";
    public static final String PROPERTY_REG_ID = "registration_id";
    private static final String PROPERTY_APP_VERSION = "appVersion1";
    private final static int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;
    AtomicInteger msgId = new AtomicInteger();
	public void onCreate(Bundle savedInstanceState) {
	super.onCreate(savedInstanceState);
	getWindow().requestFeature(Window.FEATURE_ACTION_BAR);

    if (Build.VERSION.SDK_INT < 16) {
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
    }
    else{
    	final View decorView = getWindow().getDecorView();
    	
    	int uiOptions = View.SYSTEM_UI_FLAG_FULLSCREEN;
    	decorView.setSystemUiVisibility(uiOptions);

    }
    setContentView(R.layout.poke_list);
    StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();

    StrictMode.setThreadPolicy(policy); 
    /*
    moPubView = (MoPubView) findViewById(R.id.adview);
    moPubView.setAdUnitId("4c74edaf436d459eb943a6e8c808b4a9");
    moPubView.loadAd();
    moPubView.setBannerAdListener(new BannerAdListener() {
		
		@Override
		public void onBannerLoaded(MoPubView arg0) {
			// TODO Auto-generated method stub
			
		}
		
		@Override
		public void onBannerFailed(MoPubView arg0, MoPubErrorCode arg1) {
			// TODO Auto-generated method stub
			
		}
		
		@Override
		public void onBannerExpanded(MoPubView arg0) {
			// TODO Auto-generated method stub
			
		}
		
		@Override
		public void onBannerCollapsed(MoPubView arg0) {
			// TODO Auto-generated method stub
			
		}
		
		@Override
		public void onBannerClicked(MoPubView arg0) {
			// TODO Auto-generated method stub
			
		}
	});*/
    ActionBar bar = getActionBar();
    bar.hide();
    context = getApplicationContext();
    globals.minimized =0;
    bar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#78A61F")));
    level  = (TextView)findViewById(R.id.level);
    
    level.setOnClickListener(new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			
	
			 LayoutInflater layoutInflater 
		     = (LayoutInflater)getBaseContext()
		      .getSystemService(LAYOUT_INFLATER_SERVICE);  
			   View layout = layoutInflater.inflate(R.layout.popuplevel, null);
			 final PopupWindow popup = new PopupWindow(
		               layout, 
		               LayoutParams.WRAP_CONTENT,  
		                     LayoutParams.WRAP_CONTENT); 
			   // Creating the PopupWindow

			   //popup.setContentView(layout);
			   //popup.setWidth(WindowManager.LayoutParams.WRAP_CONTENT);
			   //popup.setHeight(WindowManager.LayoutParams.WRAP_CONTENT);
			   popup.setFocusable(true);
			 
			

			   popup.setBackgroundDrawable(new BitmapDrawable());

			   popup.showAtLocation(layout, Gravity.CENTER, 0,0);
			 
			   // Getting a reference to Close button, and close the popup when clicked.
			   Button close = (Button) layout.findViewById(R.id.btnclose);
			   close.setOnClickListener(new OnClickListener() {
			 
			     @Override
			     public void onClick(View v) {
			       popup.dismiss();
			     }
			   });
			
		}
	});
    LocalBroadcastManager.getInstance(this).registerReceiver(mMessageReceiver,
    	      new IntentFilter("update-view"));
    
    mPrefs = PreferenceManager.getDefaultSharedPreferences(this);
    editor = mPrefs.edit();
    pokestonextlevel = (TextView)findViewById(R.id.pbpokescount);
    refimage = (ImageView)findViewById(R.id.refview01);
    //ProgressWheel pw = (ProgressWheel) findViewById(R.id.progressBar1);
    //pw.setProgress(40);
   
    
    if (checkPlayServices()) {
        gcm = GoogleCloudMessaging.getInstance(this);
        regid = getRegistrationId(context);

        if (regid.isEmpty()) {
            registerInBackground();
        }
    } 
    
    
    
    
    gcmFilter = new IntentFilter();
	gcmFilter.addAction("POKE_REC");
	activity = this;
	client = new DefaultHttpClient();
    db = new DataBaseHandler(getApplicationContext());
    globals.r = RingtoneManager.getRingtone(getApplicationContext(), notification);   
    globals.contactsmain = db.getAllContacts();
//    AdView mAdView = (AdView) findViewById(R.id.adView);
//    AdRequest adRequest = new AdRequest.Builder().build();
//    mAdView.loadAd(adRequest);
    //levelbar = (ProgressBar)findViewById(R.id.progressBar1);
    //levelbar.setMax(100);
    //levelbar.setProgress(70);
    //globals.mp = MediaPlayer.create(this, R.raw.ouch);
    pbpokes = (TextProgressBar) findViewById(R.id.pbpokes);
    //pbcontacts = (TextProgressBar) findViewById(R.id.pbcontacts);
    pbpokes.setMax(100);
	pbpokes.setProgress(20);
	countDownTimer = new MyCountDownTimer(1000, 200);
	pbpokes.setText("Pokes needed to reach next Level");
	pbpokes.setProgressDrawable(getResources().getDrawable(R.drawable.green_progress));
	globals.newpokescontact.clear();
	
	//pbcontacts.setMax(100);
	//pbcontacts.setProgress(70);
	//pbcontacts.setText("Buddies needed to reach next Level");
	//pbcontacts.setProgressDrawable(getResources().getDrawable(R.drawable.green_progress));
    adapter = new adapter(this,globals.contactsmain);
    dataGrid = (ListView) findViewById(R.id.ctlist);
	dataGrid.setAdapter(adapter);
	//setStatusBarColor(findViewById(R.id.statusBarBackground),getResources().getColor(android.R.color.white));
	dataGrid.setOnItemClickListener(new OnItemClickListener()
	{
		@Override
		public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
			
			if(globals.contactsmain.get(position).getSentPoke()-globals.contactsmain.get(position).getRecvPoke()<=998)
			{
			globals.newpokescontact.clear();
			int total_pokes = mPrefs.getInt("pokes", 0);
			int k = Level.getpokesmultiply(total_pokes);
			if(!globals.contactsmain.get(position).getContactNumber().equals(mPrefs.getString("mynumber", ""))){
			editor.putInt("pokes",total_pokes+1*k);
			editor.commit();
			total_pokes = total_pokes + 1*k;
			updateuilevel();
			}
			
			db.updateSentPoke(globals.contactsmain.get(position).getContactNumber(),globals.contactsmain.get(position).getSentPoke()+1*k);
			globals.contactsmain.get(position).setSentPoke(globals.contactsmain.get(position).getSentPoke()+1*k);
			globals.contactsmain.get(position).setLastTime(System.currentTimeMillis());
			db.updateNewPoke0(globals.contactsmain.get(position).getContactNumber());
			globals.contactsmain.get(position).setNewpokes(0);
			adapter.notifyDataSetChanged();
		    //dataGrid.invalidateViews();
			
			//dataGrid.setAdapter(adapter);
			//globals.mp.stop();
			//adapter.notifyDataSetChanged();
			//globals.mp.start();
			//send_poke sendpoke = new send_poke();
			//sendpoke.execute(new String[] {mPrefs.getString("mynumber", "null") ,globals.contactsmain.get(position).getContactNumber()});
			int flag =0;
	        for(int i=0;i<globals.sendnewpokes.size();i++)
	        {
	        	if(globals.sendnewpokes.get(i).getContactNumber().equalsIgnoreCase(globals.contactsmain.get(position).getContactNumber()))
	        	{
	        		globals.sendnewpokes.get(i).setNewpokes(globals.sendnewpokes.get(i).getNewpokes()+1*k);
	        		flag=1;
	        	}
	        }
	        if(flag==0)
	        {
	        	globals.sendnewpokes.add(new Contact("", globals.contactsmain.get(position).getContactNumber(), 0, 0, 0, 1*k));
	        }
	        countDownTimer.cancel();
	        countDownTimer.start();
			}
			else
			{
				Toast.makeText(getApplicationContext(), "You have poked too much. Message your buddy to poke back.", Toast.LENGTH_SHORT).show();
				globals.r.play();
			}
	        
			//sendnewpokes.add(new Contact(globals.contactsmain.get(position).getContactNumber(), "",0,));
			
		}
		
	});

	
}
	
	class LastPoke implements Comparator<Contact>{
		 
	    @Override
	    public int compare(Contact e1, Contact e2) {
	        if(e1.getLastTime() < e2.getLastTime()){
	            return 1;
	        } else if(e1.getLastTime() == e2.getLastTime()){
	            return 1;
	        }else
	        {
	        	return -1;
	        }
	    }
	}
	
	private BroadcastReceiver mMessageReceiver = new BroadcastReceiver() {
		  @Override
		  public void onReceive(Context context, Intent intent) {
		    // Get extra data included in the Intent
			  //System.out.println("recieved");
			  //dataGrid.invalidateViews();
			    //adapter = new adapter(activity,globals.contactsmain);
			 
			  Collections.sort(globals.contactsmain,new LastPoke());
				adapter.notifyDataSetChanged();
				//dataGrid.setAdapter(adapter);
		  }
		};

	
		public class MyRefreshCountDownTimer extends CountDownTimer {
			  public MyRefreshCountDownTimer(long startTime, long interval) {
			   super(startTime, interval);
			  }

			  @Override
			  public void onFinish() {
				  refimage.setImageBitmap(BitmapFactory.decodeResource(context.getResources(),R.drawable.options));
			  }

			  @Override
			  public void onTick(long millisUntilFinished) {
				  if(rflag==0)
				  {
					  rflag=1;
					  refimage.setImageBitmap(BitmapFactory.decodeResource(context.getResources(),R.drawable.refresh));
				  }
				  else
				  {
					  refimage.setImageBitmap(BitmapFactory.decodeResource(context.getResources(),R.drawable.refresh2));
					  rflag=0;
				  }
			  }
			 }

	 public class MyCountDownTimer extends CountDownTimer {
	  public MyCountDownTimer(long startTime, long interval) {
	   super(startTime, interval);
	  }

	  @Override
	  public void onFinish() {
		  send_poke sendpoke = new send_poke();
			sendpoke.execute(new String[] {mPrefs.getString("mynumber", "null") ,""});
	  }

	  @Override
	  public void onTick(long millisUntilFinished) {
		  
	  }
	 }
	 
	 protected void onDestroy() {
		 globals.minimized=1;
		    super.onDestroy(); 
		}
	 
	@Override
	public void onResume() {
		  

		super.onResume();
		globals.minimized = 0;
//		adRequest = new AdRequest.Builder().build();
//	    mAdView.loadAd(adRequest);
		NotificationManager mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
		mNotificationManager.cancelAll();
		LocalBroadcastManager.getInstance(this).registerReceiver(mMessageReceiver,
	    	      new IntentFilter("update-view"));
	    activity = this;
	    updateuilevel();
	    globals.newpokescontact.clear();
	    gcmFilter = new IntentFilter();
		gcmFilter.addAction("POKE_REC");
		
		if (checkPlayServices()) {
	        gcm = GoogleCloudMessaging.getInstance(this);
	        regid = getRegistrationId(context);

	        if (regid.isEmpty()) {
	            registerInBackground();
	        }
	    } 
		
		//globals.contactsmain = db.getAllContacts();
		Collections.sort(globals.contactsmain,new LastPoke());
		dataGrid.invalidateViews();
	    adapter = new adapter(this,globals.contactsmain);
		dataGrid.setAdapter(adapter);
		Handler handler = new Handler();
		handler.postDelayed(new Runnable() {
		      public void run() {
		    	  getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                          WindowManager.LayoutParams.FLAG_FULLSCREEN);
		      }
		   }, 100); 
	}
	
	@Override
	public void onBackPressed() {
		globals.minimized=1;
		moveTaskToBack(true);
		LocalBroadcastManager.getInstance(this).unregisterReceiver(mMessageReceiver);
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	private void registerInBackground() {
	    new AsyncTask() {
	      	@Override
			protected Object doInBackground(Object... params) {
				 String msg = "";
		            try {
		                if (gcm == null) {
		                    gcm = GoogleCloudMessaging.getInstance(context);
		                }
		                regid = gcm.register(globals.senderID);
		                
		                try{
		                	String URL = globals.serverIP+"sendRegId";
		            		HttpPost reg_user =new HttpPost(URL);
		            		BasicHttpResponse reg_user_resp; 
		            		HttpClient client = new DefaultHttpClient();
		            		String body = "{\"mobile_number\":\""+ mPrefs.getString("mynumber", "null")+"\",\"reg\":\""+regid+"\"}";
		            		StringEntity entity = new StringEntity(body,"UTF-8");
		            		reg_user.setEntity(entity);
		            		reg_user.setHeader("Authorization", "Basic cG9rZXJhcHA6cHJpY2s=");
		            		reg_user_resp = (BasicHttpResponse) client.execute(reg_user);
		            		reg_user_resp.getEntity().getContent().close();
		            		int resp_code = reg_user_resp.getStatusLine().getStatusCode();
		                	}
		                	catch(Exception e)
		                	{
		                		e.printStackTrace();
		                	}

		                storeRegistrationId(context, regid);
		            } catch (Exception ex) {
		                msg = "Error :" + ex.getMessage();
		            }
		            return msg;
			}
	    }.execute(null, null, null);
	   
	}
	
	private void storeRegistrationId(Context context, String regId) {
	    final SharedPreferences prefs = getGCMPreferences(context);
	    int appVersion = getAppVersion(context);
	    SharedPreferences.Editor editor = prefs.edit();
	    editor.putString(PROPERTY_REG_ID, regId);
	    editor.putInt(PROPERTY_APP_VERSION, appVersion);
	    editor.commit();
	}
	
	
	private String getRegistrationId(Context context) {
	    final SharedPreferences prefs = getGCMPreferences(context);
	    String registrationId = prefs.getString(PROPERTY_REG_ID, "");
	    if (registrationId.isEmpty()) {
	        return "";
	    }
	 
	    int registeredVersion = prefs.getInt(PROPERTY_APP_VERSION, Integer.MIN_VALUE);
	    int currentVersion = getAppVersion(context);
	    if (registeredVersion != currentVersion) {
	        return "";
	    }
	    return registrationId;
	}
	
	private SharedPreferences getGCMPreferences(Context context) {
	    return getSharedPreferences(poke_grid.class.getSimpleName(),
	            Context.MODE_PRIVATE);
	}
	
	private static int getAppVersion(Context context) {
	    try {
	        PackageInfo packageInfo = context.getPackageManager()
	                .getPackageInfo(context.getPackageName(), 0);
	        return packageInfo.versionCode;
	    } catch (NameNotFoundException e) {
	        // should never happen
	        throw new RuntimeException("Could not get package name: " + e);
	    }
	}

	
	
	
	private class send_poke extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... numbers) {
        	String fromnumber = numbers[0];
        	globals.r.play();
        	//String tonumber = numbers[1];
        	try{
        	String URL = globals.serverIP+"sendPoke";
			HttpPost reg_user =new HttpPost(URL);
			BasicHttpResponse reg_user_resp; 
			String body = "{\"poke\":[";
			 for(int i=0;i<globals.sendnewpokes.size();i++)
		        {
				 body=body+"{\"mobile_number1\":\""+fromnumber+"\",\"mobile_number2\":\""+globals.sendnewpokes.get(i).getContactNumber()+"\",\"pokes\":\""+globals.sendnewpokes.get(i).getNewpokes()+"\"},";
		        }
			 
			//String body = "{\"mobile_number1\":\""+ fromnumber+"\",\"mobile_number2\":\""+tonumber+"\"}";
			 body = body.substring(0, body.length()-1);
			 body=body+"]}";
			StringEntity entity = new StringEntity(body,"UTF-8");
			reg_user.setEntity(entity);
			reg_user.setHeader("Authorization", "Basic cG9rZXJhcHA6cHJpY2s=");
			reg_user_resp = (BasicHttpResponse) client.execute(reg_user);
			int resp_code = reg_user_resp.getStatusLine().getStatusCode();
			reg_user_resp.getEntity().getContent().close();
			
			if(resp_code==200)
			{
				globals.sendnewpokes.clear();
				return "Success";
			}
			else
			{
				return "fail";
			}
        	}
        	catch(Exception e)
        	{
        		e.printStackTrace();
        		return "fail";
        	}
          
        }

        @Override
        protected void onPostExecute(String result) {
        	if(result.compareToIgnoreCase("Success")!=0)
        	{
        		 for(int j=0;j<globals.sendnewpokes.size();j++)
 		        {
        		editor.putInt("pokes", mPrefs.getInt("pokes", 0)-1);
        		db.pokeFail(globals.sendnewpokes.get(j).getContactNumber());
        		int i=globals.getposition(globals.sendnewpokes.get(j).getContactNumber());
        		globals.contactsmain.get(i).setSentPoke(globals.contactsmain.get(i).getSentPoke()-1);
 		        }
        		 globals.sendnewpokes.clear();
    			adapter.notifyDataSetChanged();
    			editor.commit();
    			updateuilevel();
    			Toast.makeText(getApplicationContext(), "Check Internet Connection", Toast.LENGTH_LONG).show();
    			//dataGrid.invalidateViews();
    		    //adapter = new ContactGridAdapter(activity,globals.contactsmain);
    			//dataGrid.setAdapter(adapter);
        	}
        }
      }
	
	 public static void updateuilevel()
	    {
		 int pokes = mPrefs.getInt("pokes",0);
	    	int levelvalue = Level.getnextlevel(pokes);
	    	String clevel = Level.getlevel(pokes);
	    	level.setText(clevel.toString());
	    	if(levelvalue==9999)
	    	{
	    		pbpokes.setText("You are the guru of pricks");
	    		pbpokes.setMax(100);
	    		pbpokes.setProgress(100);
	    		pokestonextlevel.setText("-");
	    	}
	    	else
	    	{
	    	pbpokes.setText("Pricks to next level");
	    	pokestonextlevel.setText(String.valueOf(levelvalue-pokes));
	    	
	    	pbpokes.setMax(levelvalue);
	    	pbpokes.setProgress(pokes);
	    	pbpokes.refreshDrawableState();
	    	}
	    }
	 
	private class refresh_contacts extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... numbers) {
        	try 
        	{
        	
        	DataBaseHandler db2  = new DataBaseHandler(getApplicationContext());
        	ArrayList<Contact> time_cnt =  globals.contactsmain;
        	LoadContacts contactsl = new LoadContacts();
            ArrayList<String> array_contacts = contactsl.get_all_contacts(getApplicationContext().getContentResolver());
            String json_contacts = contactsl.build_contacts(array_contacts);
            ArrayList<Contact> array_recv_contacts = contactsl.send_contacts(json_contacts,PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).getString("mynumber","0"));
            globals.Contacts = contactsl.get_details(array_recv_contacts,getApplicationContext());
            //progress.dismiss();
            
            int total_pokes = 0;
            for(int i=0;i<time_cnt.size();i++)
            {
            	db2.updateTime(time_cnt.get(i).getContactNumber(), time_cnt.get(i).getLastTime());
            	if(!time_cnt.get(i).getContactNumber().equals(mPrefs.getString("mynumber", "")))
            		total_pokes=total_pokes+time_cnt.get(i).getSentPoke();
            	
            }
            globals.contactsmain = null;
            globals.contactsmain = db2.getAllContacts();
            editor.putInt("pokes",total_pokes);
            editor.commit();
            
			
          return "Success";
        	}
        	catch(Exception e)
        	{
        		refreshcountDownTimer.cancel();
        		return "Fail";
        	}
        }

        @Override
        protected void onPostExecute(String result) {
        	dataGrid.invalidateViews();
        	DataBaseHandler db2  = new DataBaseHandler(getApplicationContext());
        	globals.contactsmain = db2.getAllContacts();
		    adapter = new adapter(activity,globals.contactsmain);
			adapter.notifyDataSetChanged();
			dataGrid.setAdapter(adapter);
			updateuilevel();
			refreshcountDownTimer.cancel();
			refimage.setImageBitmap(BitmapFactory.decodeResource(context.getResources(),R.drawable.options));
        }
      }
	
	@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
	
	public void refresh(View v)
	{
		PopupMenu popup = new PopupMenu(getApplication(), v);
	    MenuInflater inflater = popup.getMenuInflater();
	    inflater.inflate(R.menu.main, popup.getMenu());
	    popup.setOnMenuItemClickListener(new OnMenuItemClickListener() {
			
			@Override
			public boolean onMenuItemClick(MenuItem item) {
				return onOptionsItemSelected(item);
			}
		});
	    popup.show();
	}

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
        	
        	progress = new ProgressDialog(this);
    		progress.setTitle("Loading");
    		progress.setMessage("Populating your contacts...");
    		//progress.show();
    		refreshcountDownTimer = new MyRefreshCountDownTimer(999000, 300);
    		refreshcountDownTimer.start();
    		refresh_contacts refresh_contacts1 = new refresh_contacts();
    		refresh_contacts1.execute(new String[] {});
        	
            return true;
        }
        if (id == R.id.action_settings2) {
        	
        	String shareBody = "I am currently in Level "+ Level.getpokesmultiply(mPrefs.getInt("pokes", 0))+" with "+mPrefs.getInt("pokes", 0) + " pokes.";
            Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
                sharingIntent.setType("text/plain");
                sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Poke Level");
                sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBody);
                startActivity(Intent.createChooser(sharingIntent, "Share your Level"));
            return true;
        }
        if (id == R.id.action_settings3) {
        	
        	String shareBody = "Use the poke app to buzz me. Download from ";
            Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
                sharingIntent.setType("text/plain");
                sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, shareBody);
                sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, "http://www.google.com");
                startActivity(Intent.createChooser(sharingIntent, "Share The App"));
            return true;
        }
        
        return super.onOptionsItemSelected(item);
    }
	
    private boolean checkPlayServices() {
        int resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);
        if (resultCode != ConnectionResult.SUCCESS) {
            if (GooglePlayServicesUtil.isUserRecoverableError(resultCode)) {
                GooglePlayServicesUtil.getErrorDialog(resultCode, this,
                		PLAY_SERVICES_RESOLUTION_REQUEST).show();
            } else {
            	Toast.makeText(getApplicationContext(), "install google play", Toast.LENGTH_SHORT).show();
                finish();
            }
            return false;
        }
        return true;
    }
    

	
}
