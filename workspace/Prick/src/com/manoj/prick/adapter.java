package com.manoj.prick;

import java.net.URL;
import java.util.List;

import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicHttpResponse;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.drawable.BitmapDrawable;
import android.os.AsyncTask;
import android.os.Build;
import android.os.CountDownTimer;
import android.preference.PreferenceManager;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnKeyListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.webkit.WebView.FindListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;
import android.widget.Toast;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;

public class adapter extends ArrayAdapter<Contact> {
	
int aflag=0;
static ImageView imvpress;
static MyCountDownTimer countDownTimer2;
  private final List<Contact> list;
  private final Activity context;
  private final adapter adapter;
  SharedPreferences.Editor editor;
  DataBaseHandler db ;
  private final PopupWindow popup; 
  private String sendmessage;
  CountDownTimer countDownTimer;
  HttpClient client;
  SharedPreferences mPrefs;
  public adapter(Activity context, List<Contact> list) {
    super(context, R.layout.row, list);
    this.context = context;
    this.list = list;
    this.adapter = this;
    this.popup = new PopupWindow(context);
    this.sendmessage = "";
    countDownTimer2 = new MyCountDownTimer(1000, 100);
    mPrefs = PreferenceManager.getDefaultSharedPreferences(context);
    editor = mPrefs.edit();
    countDownTimer = new MyCountDownTimer2(1000, 200);
    db = new DataBaseHandler(context);
    client = new DefaultHttpClient();
  }

  public void update()
  {
	  adapter.notifyDataSetChanged();
  }
  static class ViewHolder {
	  TextView name;
	  ImageView upImage;
	  ImageView downImage;
	  TextView status;
	  TextView recv;
	  TextView sent;
	  TextView diff;
	  ImageView newmsg;
	  ImageView upImage2;
	  RelativeLayout rl;
	  
  }

  @Override
  public Contact getItem(int position) {
    return list.get(position);
  }
  
  @SuppressLint("NewApi") @Override
  public View getView(final int position, View convertView, ViewGroup parent) {
    View view = null;
    
    if (convertView == null) {
    final ViewHolder viewHolder = new ViewHolder();
      LayoutInflater inflator = context.getLayoutInflater();
      view = inflator.inflate(R.layout.row, null);
      viewHolder.newmsg = (ImageView)view.findViewById(R.id.imageView1);
      viewHolder.upImage = (ImageView)view.findViewById(R.id.up);
      viewHolder.upImage2 = (ImageView)view.findViewById(R.id.up2);
      viewHolder.downImage= (ImageView)view.findViewById(R.id.down);
      viewHolder.name = (TextView)view.findViewById(R.id.name);
      viewHolder.status = (TextView)view.findViewById(R.id.newpokes);
      viewHolder.recv = (TextView)view.findViewById(R.id.recv);
      viewHolder.sent = (TextView)view.findViewById(R.id.sent);
      viewHolder.diff = (TextView)view.findViewById(R.id.diff);
      viewHolder.rl = (RelativeLayout)view.findViewById(R.id.rowlayout);
      view.setTag(viewHolder);
      viewHolder.name.setTag(list.get(position));
    } else {
      view = convertView;
      ((ViewHolder) view.getTag()).name.setTag(list.get(position));
    }
    
    final ViewHolder holder = (ViewHolder) view.getTag();
    Integer upScore = list.get(position).getSentPoke();
	Integer downScore = list.get(position).getRecvPoke();
    holder.diff.setText(String.valueOf(upScore-downScore));
    holder.recv.setText("Received: "+downScore);
    holder.sent.setText("Sent: "+upScore);
    if(list.get(position).getNew_message().compareToIgnoreCase("no")==0)
    	holder.newmsg.setImageBitmap(BitmapFactory.decodeResource(context.getResources(),R.drawable.no_mesage));
    else
    	holder.newmsg.setImageBitmap(BitmapFactory.decodeResource(context.getResources(),R.drawable.new_mesage));
    if(list.get(position).getNewpokes()==0)
    {
    	holder.status.setText("");
    }
	else
	{
		int c = list.get(position).getNewpokes();
			holder.status.setText(c+" new pricks");
	}
	if(upScore>downScore)
	{
		holder.rl.setBackground(context.getResources().getDrawable(R.drawable.layout_bg));
		holder.upImage.setImageBitmap(BitmapFactory.decodeResource(context.getResources(),R.drawable.color_up));
		holder.downImage.setImageBitmap(BitmapFactory.decodeResource(context.getResources(),R.drawable.normal_down));
	}
	else if(upScore<downScore)
	{
		holder.rl.setBackground(context.getResources().getDrawable(R.drawable.layout_br));
		holder.upImage.setImageBitmap(BitmapFactory.decodeResource(context.getResources(),R.drawable.normal_up));
		holder.downImage.setImageBitmap(BitmapFactory.decodeResource(context.getResources(),R.drawable.color_down));
	}
	else if(upScore==downScore)
	{
		holder.rl.setBackground(context.getResources().getDrawable(R.drawable.layout_bg));
		holder.upImage.setImageBitmap(BitmapFactory.decodeResource(context.getResources(),R.drawable.normal_up));
		holder.downImage.setImageBitmap(BitmapFactory.decodeResource(context.getResources(),R.drawable.normal_down));
	}
	if(list.get(position).getContactName().length()>22)
		holder.name.setText(list.get(position).getContactName().substring(0, 22)+"..");
	else
		holder.name.setText(list.get(position).getContactName());
	
	
	holder.rl.setOnClickListener(new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			imvpress = holder.upImage2;
			aflag=0;
			countDownTimer2.cancel();
	        countDownTimer2.start();
	        int total_pokes = mPrefs.getInt("pokes", 0);
	        int k = Level.getpokesmultiply(total_pokes); 
	        if(globals.contactsmain.get(position).getSentPoke()-globals.contactsmain.get(position).getRecvPoke()+k<=999)
			{
			globals.newpokescontact.clear();
			
			
			if(!globals.contactsmain.get(position).getContactNumber().equals(mPrefs.getString("mynumber", ""))){
			editor.putInt("pokes",total_pokes+1*k);
			editor.commit();
			total_pokes = total_pokes + 1*k;
			poke_grid.updateuilevel();
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
				Toast.makeText(context, "You have pricked too much. Message your buddy to prick back.", Toast.LENGTH_SHORT).show();
				globals.r.play();
			}
	        
			//sendnewpokes.add(new Contact(globals.contactsmain.get(position).getContactNumber(), "",0,));
	        
		}
	});
	
	holder.newmsg.setOnClickListener(new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			
					
				   RelativeLayout viewGroup = (RelativeLayout) context.findViewById(R.id.trelativeLayout1);
				   LayoutInflater layoutInflater = (LayoutInflater) context
				     .getSystemService(context.LAYOUT_INFLATER_SERVICE);
				   final View layout = layoutInflater.inflate(R.layout.popup, viewGroup);
				 
				   // Creating the PopupWindow

				   popup.setContentView(layout);
				   popup.setWidth(WindowManager.LayoutParams.WRAP_CONTENT);
				   popup.setHeight(WindowManager.LayoutParams.WRAP_CONTENT);
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
				   

				   TextView mesg = (TextView)layout.findViewById(R.id.recvdtext);
				   if(list.get(position).getMessage().length()==0)
					   mesg.setText("No new message");
				   else
					   mesg.setText(list.get(position).getMessage());
				   
				   final EditText sendmesg = (EditText)layout.findViewById(R.id.editText1);
				   sendmessage = sendmesg.getText().toString();
				   sendmesg.setOnKeyListener(new OnKeyListener() {
					
					@Override
					public boolean onKey(View v, int keyCode, KeyEvent event) {
						// TODO Auto-generated method stub
						if ((event.getAction() == KeyEvent.ACTION_DOWN) &&
					            (keyCode == KeyEvent.KEYCODE_ENTER)) {
					          // Perform action on key press
							 popup.dismiss();
							 send_message sendmes = new send_message();
								sendmes.execute(new String[] {list.get(position).getContactNumber() ,sendmesg.getText().toString()});
					          return true;
					        }
					        return false;
					}
				});
				   Button send = (Button) layout.findViewById(R.id.btnsend);
				   send.setOnClickListener(new OnClickListener() {
				 
				     @Override
				     public void onClick(View v) {
				    	 popup.dismiss();
						 send_message sendmes = new send_message();
							sendmes.execute(new String[] {list.get(position).getContactNumber() ,sendmesg.getText().toString()});
				     }
				   });
				   DataBaseHandler db = new DataBaseHandler(context);
					db.updateNewMessage(list.get(position).getContactNumber(), "no");
					holder.newmsg.setImageBitmap(BitmapFactory.decodeResource(context.getResources(),R.drawable.no_mesage));
					int i=globals.getposition(list.get(position).getContactNumber());
		            globals.contactsmain.get(i).setNew_message("no");
			
		}
	});
	
    return view;
  }
  
	 public class MyCountDownTimer extends CountDownTimer {
		  public MyCountDownTimer(long startTime, long interval) {
		   super(startTime, interval);
		  }

		  @Override
		  public void onFinish() {
			 
		  }

		  @Override
		  public void onTick(long millisUntilFinished) {
			  if(aflag==0)
			  {
				  aflag=1;
				  imvpress.setImageBitmap(BitmapFactory.decodeResource(context.getResources(),R.drawable.color_up));
			  }
			  else
			  {
				  imvpress.setImageBitmap(BitmapFactory.decodeResource(context.getResources(),R.drawable.normal_up));
			  }
		  }
		 }
  
	 public class MyCountDownTimer2 extends CountDownTimer {
		  public MyCountDownTimer2(long startTime, long interval) {
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
				if(globals.sendnewpokes.size()==0)
					return "Success";
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
					Toast.makeText(context, resp_code, Toast.LENGTH_SHORT);
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
	    			poke_grid.updateuilevel();
	    			Toast.makeText(context, "Check Internet Connection", Toast.LENGTH_SHORT).show();
	    			//dataGrid.invalidateViews();
	    		    //adapter = new ContactGridAdapter(activity,globals.contactsmain);
	    			//dataGrid.setAdapter(adapter);
	        	}
	        }
	      }
		
		
		 
	
	private class send_message extends AsyncTask<String, Void, String> {
      @Override
      protected String doInBackground(String... numbers) {
    	  String endmessage = numbers[1];
    	  if(endmessage.length()!=0)
			{
  		 try{
  			String mob2 = numbers[0];
  		 String URL = globals.serverIP+"sendMessage";
      		HttpPost reg_user =new HttpPost(URL);
      		BasicHttpResponse reg_user_resp; 
      		HttpClient client = new DefaultHttpClient();
      		SharedPreferences mPrefs;
      		mPrefs = PreferenceManager.getDefaultSharedPreferences(context);
      		String body = "{\"mobile_number1\":\""+ mPrefs.getString("mynumber", "null")+"\",\"mobile_number2\":\""+mob2+"\",\"message\":\""+endmessage+"\"}";
      		StringEntity entity = new StringEntity(body,"UTF-8");
      		reg_user.setEntity(entity);
      		reg_user.setHeader("Authorization", "Basic cG9rZXJhcHA6cHJpY2s=");
      		reg_user_resp = (BasicHttpResponse) client.execute(reg_user);
      		reg_user_resp.getEntity().getContent().close();
      		int resp_code = reg_user_resp.getStatusLine().getStatusCode();
      		if(resp_code==200)
      		{
      			//Toast.makeText(context.getApplicationContext(),"Message sent", Toast.LENGTH_SHORT).show();
	
  globals.r.play();

  return "sent";
      		}
      		else
      		{
      			//Toast.makeText(context.getApplicationContext(),"Message not sent. Check Internet Connection", Toast.LENGTH_LONG).show();
      			return "notsent";
      		}
  		 }
  		 catch(Exception e)
  		 {
  			 e.printStackTrace();
  		 }
			}
  	 else
  	 {
  		 //Toast.makeText(context.getApplicationContext(),"Enter some text", Toast.LENGTH_LONG).show();
  		 return "enter";
  	 }
		return sendmessage;
        
      }

      @Override
      protected void onPostExecute(String result) {
    	 
      	if(result.compareToIgnoreCase("sent")==0)
      	{
      		Toast.makeText(context.getApplicationContext(),"Message sent", Toast.LENGTH_SHORT).show();
      	  //popup.dismiss();
      	}
      	if(result.compareToIgnoreCase("notsent")==0)
      		Toast.makeText(context.getApplicationContext(),"Message not sent. Check Internet Connection", Toast.LENGTH_LONG).show();
      	if(result.compareToIgnoreCase("enter")==0)
      		Toast.makeText(context.getApplicationContext(),"Enter some text", Toast.LENGTH_LONG).show();
      }
    }
  
} 