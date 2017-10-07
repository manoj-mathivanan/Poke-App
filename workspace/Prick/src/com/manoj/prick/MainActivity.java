package com.manoj.prick;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicHttpResponse;
import org.apache.http.util.EntityUtils;

import android.support.v7.app.ActionBarActivity;
import android.support.v4.app.Fragment;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.StrictMode;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View.OnClickListener;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.os.Build;
import android.preference.PreferenceManager;
import android.provider.ContactsContract;

public class MainActivity extends ActionBarActivity {

	private boolean api_key = false;
	private boolean call_key = false;
	Context context;
	ArrayList<Contact> final_contacts;
	String api_key_value;
	String call_key_value;
	String globalnumber;
	TextView statusText;
	EditText userName;
	EditText userPhone;
	Button signUp;
	ProgressBar progressStatus;
	CounterClass timer;
	ProgressDialog progress;
	Registration reg;
	int regflag=0;
	int callflag=0;
	
	public void check_registered()
	{
		/*timer.cancel();
		//display loading screen
		progress = new ProgressDialog(this);
        progress.setTitle("Loading");
        progress.setMessage("Authentication Success.. Populating your contacts for first time...");
        progress.show();
		//load the contacts
        Contacts_task contacts_task2 = new Contacts_task();
        contacts_task2.execute(new String[] {  });
        
        */
        try{
		if(((api_key==true)&&(call_key==true)))
		{
			if(api_key_value.equalsIgnoreCase(call_key_value))
         	{
				timer.cancel();
		   		statusText.setText("Authentication Success..");
			onboard task = new onboard();
            task.execute(new String[] {});
         	}
	         else
	         {
	        	 timer.cancel();
	        	 statusText.setText("Authentication Unsuccessful. Check your phone number and please try again.");
	        	 progressStatus.setVisibility(4);
	        	 signUp.setEnabled(true);
	         }
		}
        }catch(Exception e)
        {
        	e.printStackTrace();
        }
			
	}
	SharedPreferences mPrefs;
    final String welcomeScreenShownPref = "welcomeScreenShown";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        setContentView(R.layout.activity_main);
        ActionBar bar = getActionBar();
        bar.hide();
        bar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#78A61F")));
        context = getApplicationContext();
        progress = new ProgressDialog(getApplicationContext());
        
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction().add(R.id.container, new PlaceholderFragment()).commit();
        }
            mPrefs = PreferenceManager.getDefaultSharedPreferences(this);
     
            
//           //to be deleted
//            {
//            SharedPreferences.Editor editor = mPrefs.edit();
//			editor.putBoolean(welcomeScreenShownPref, true);
//			editor.putInt("pokes",2500);
//			editor.putString("mynumber", "9916726325");
//			editor.commit();
//            }
            
			Boolean welcomeScreenShown = mPrefs.getBoolean(welcomeScreenShownPref, false);
            if(welcomeScreenShown){
            	Intent goToNextActivity = new Intent(MainActivity.this,poke_grid.class);
    			startActivity(goToNextActivity);   
             }
        else
        {
        	
        statusText = (TextView)findViewById(R.id.textView3);
        userName = (EditText)findViewById(R.id.editText1);
        userPhone = (EditText)findViewById(R.id.editText2);
        signUp = (Button)findViewById(R.id.button1);
        progressStatus = (ProgressBar)findViewById(R.id.progressBar1);
        
        
        TelephonyManager telephonyManager = (TelephonyManager)getSystemService(Context.TELEPHONY_SERVICE);        
        PhoneStateListener callStateListener = new PhoneStateListener() {
             public void onCallStateChanged(int state, String incomingNumber) 
             {
                     
                     
                     if(state==TelephonyManager.CALL_STATE_RINGING)
                     {
                    	call_key_value=incomingNumber.substring(incomingNumber.length()-4);;
                    	call_key = true;
                    	callflag=1;
                    	//telephonyService.endCall();
                    	check_registered();      
                     }
             }
        };
        telephonyManager.listen(callStateListener,PhoneStateListener.LISTEN_CALL_STATE);
        
        
        
        timer = new CounterClass(60000,1000); 
        signUp.setOnClickListener(new OnClickListener() { 
        	@Override public void onClick(View v) { 
        		statusText.setVisibility(0);
        		
        		if(userName.getText().toString().length()<=10)
        		{
        			statusText.setText("Please enter your mobile number with country code");
        		}
        		else
        		{
        			progressStatus.setVisibility(0);
            		progressStatus.setMax(60);
            		regflag=0;
            		callflag=0;
            		call_key=false;
            		api_key=false;
            		reg = new Registration();
            		timer.start(); 
            		globalnumber = userName.getText().toString();
//            		onboard task = new onboard();
//                    task.execute(new String[] {});

                signUp.setEnabled(false);
        		}
        		} 
        	}); 
        }
    }

    private class onboard extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... numbers) {
        	try{
        	
     
			String URL = globals.serverIP+"insertPerson";
			HttpPost reg_user =new HttpPost(URL);
			BasicHttpResponse reg_user_resp; 
			HttpClient client = new DefaultHttpClient();
			String body = "{\"mobile_number\":\""+ userName.getText().toString().substring(userName.getText().toString().length()-10)+"\",\"name\":\""+userPhone.getText().toString()+"\",\"type\":\""+"android"+"\"}";
			StringEntity entity = new StringEntity(body,"UTF-8");
			reg_user.setEntity(entity);
			reg_user.setHeader("Authorization", "Basic cG9rZXJhcHA6cHJpY2s=");
			reg_user_resp = (BasicHttpResponse) client.execute(reg_user);
			int resp_code = reg_user_resp.getStatusLine().getStatusCode();
			if(resp_code==201)
			{
				return "Success";
			}
			else{
				return "failure";
			}
         	
        	}
        	catch(Exception e){
        		e.printStackTrace();
        		return "failure";
        	}
        }

        @Override
        protected void onPostExecute(String result) {
        	if(result.compareToIgnoreCase("Success")==0)
        	{
        	timer.cancel();
			//display loading screen
			
			progress.setTitle("Loading");
			progress.setMessage("Authentication Success.. Populating your contacts...");
			statusText.setText("Registration Success.. Populating your contacts...");
			//progress.show();
			SharedPreferences.Editor editor = mPrefs.edit();
			editor.putBoolean(welcomeScreenShownPref, true);
			editor.putInt("pokes", 0);
			//editor.putInt("total_pokes", 0);
			editor.putString("mynumber", userName.getText().toString().substring(userName.getText().toString().length()-10));
			editor.commit();
			//load the contacts
			Contacts_task contacts_task = new Contacts_task();
			contacts_task.execute(new String[] {  });
        	}
        	else{
        		timer.cancel();
           	 statusText.setText("Authentication Success but Registration Failed. Poke server couldnt be reached.");
           	 progressStatus.setVisibility(4);
           	 signUp.setEnabled(true);
        	}
        }
      }
    
    private class Registration_task extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... numbers) {
        	if(numbers[0].equals("api"))
        	{
        		try {
					Thread.sleep(6000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			return reg.register_number(globalnumber);
        	}
        	else
        	{
        		reg.initiatecall(numbers[0]);
        		return "call";
        	}
        }

        @Override
        protected void onPostExecute(String result) {
        	if(!result.equals("call"))
        	{
        	api_key_value = result.substring(result.length()-4);
        	api_key=true;
        	check_registered();
        	}
        }
      }
    

    private class Contacts_task extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... numbers) {
          LoadContacts contacts = new LoadContacts();
          ArrayList<String> array_contacts = contacts.get_all_contacts(context.getContentResolver());
          String json_contacts = contacts.build_contacts(array_contacts);
          ArrayList<Contact> array_recv_contacts = contacts.send_contacts(json_contacts,PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).getString("mynumber","0"));
          globals.Contacts = contacts.get_details(array_recv_contacts,context); 
          int total_pokes = 0;
          for(int i=0;i<globals.Contacts.size();i++)
        	  total_pokes = total_pokes + globals.Contacts.get(i).getSentPoke();
          SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
  	    	SharedPreferences.Editor editor = prefs.edit();
          editor.putInt("pokes", total_pokes);
          editor.commit();
          return "Success";
        }

        @Override
        protected void onPostExecute(String result) {
        	progress.dismiss();
        	Intent goToNextActivity = new Intent(MainActivity.this,poke_grid.class);
			startActivity(goToNextActivity);
        }
      }
      
    public class CounterClass extends CountDownTimer { 
    	public CounterClass(long millisInFuture, long countDownInterval) { 
    		super(millisInFuture, countDownInterval); 
    		} 
    	@Override public void onFinish() { 
    		check_registered();
    		statusText.setText("Authentication Unsuccessful. Check your phone number, network connectivity and please try again."); 
    		signUp.setEnabled(true);
    		progressStatus.setVisibility(4);
    		} 

    	@SuppressLint({ "NewApi", "DefaultLocale" }) @Override public void onTick(long millisUntilFinished) {
    		String hms = Long.toString(millisUntilFinished/1000);
    		int pr = Integer.parseInt(hms);
    		statusText.setText(pr + " seconds remaining"); 
    		progressStatus.setProgress(pr);
    		if(regflag==0)
    		{
    			regflag=1;
    			Registration_task task = new Registration_task();
             task.execute(new String[] { globalnumber });
            
    		}
    		if(callflag==1)
    		{
    			
    			Registration_task task = new Registration_task();
              task.execute(new String[] { "api" });
              callflag=0;
    		}
    		} 
    	} 


    

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {

        public PlaceholderFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_main, container, false);
            return rootView;
        }
    }

}
