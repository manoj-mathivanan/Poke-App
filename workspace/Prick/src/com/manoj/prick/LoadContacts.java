package com.manoj.prick;

import java.util.ArrayList;
import java.util.Iterator;

import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicHttpResponse;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.provider.ContactsContract;

public class LoadContacts {

	public ArrayList<String> get_all_contacts(ContentResolver contentResolver)
	{
		ArrayList<String> alContacts = new ArrayList<String>();
		//globals.names = new ArrayList<String>();
		//globals.numbers = new ArrayList<String>();
		globals.temp_contacts = new ArrayList<Contact>();
		String contactNumber;
		String contactName;
		String num;
		
		
		
        Cursor cur = contentResolver.query(ContactsContract.Contacts.CONTENT_URI,
                null, null, null, null);
        if (cur.getCount() > 0) {
            while (cur.moveToNext()) {
                  String id = cur.getString(cur.getColumnIndex(ContactsContract.Contacts._ID));
                  contactName = cur.getString(cur.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
                  if (Integer.parseInt(cur.getString(
                        cur.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER))) > 0) {
                     Cursor pCur = contentResolver.query(
                               ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                               null,
                               ContactsContract.CommonDataKinds.Phone.CONTACT_ID +" = ?",
                               new String[]{id}, null);
                     while (pCur.moveToNext()) {
                         contactNumber = pCur.getString(pCur.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
     					num = remove_spaces(contactNumber);
     					int flag=0;
     					for(int i=0;i<alContacts.size();i++)
     					{
     						if(alContacts.get(i).compareToIgnoreCase(num)==0)
     						{
     							flag=1;
     						}
     					}
     					if(flag==0)
     					{
     					alContacts.add(num);
     					globals.temp_contacts.add(new Contact(contactName,num));
     					}

                     }
                    pCur.close();
                }
            }
        }
        return alContacts;
	}
	
	public ArrayList<Contact> get_details(ArrayList<Contact> contacts, Context context)
	{
		DataBaseHandler db = new DataBaseHandler(context);
		db.droptables();
		ArrayList<Contact> alContacts = new ArrayList<Contact>();
		 Iterator<Contact> iterator = contacts.iterator();
		 Long time = System.currentTimeMillis();
		 while (iterator.hasNext()) {
			 Contact c = iterator.next();
			 String num = c.getContactNumber();
             Contact cnt = new Contact(getContactName(num),num,c.getRecvPoke(),c.getSentPoke(),time,0);
             alContacts.add(cnt);
             db.addContact(cnt);
		 }
	
		return alContacts;
	}
	
	public String getContactName(String number) {

	    String name = null;
	    int i=0;
	    Iterator<Contact> iterator = globals.temp_contacts.iterator();
		 while (iterator.hasNext()) {
			 if(globals.temp_contacts.get(i).getContactNumber().compareToIgnoreCase(number)==0)
			 {
				 name=globals.temp_contacts.get(i).getContactName();
				 globals.temp_contacts.get(i).setContactNumber("0");
				 break;
			 }
			 i++;
		 }
	    return name;
	}

	public String build_contacts(ArrayList<String> array_contacts) {
		 Iterator<String> iterator = array_contacts.iterator();
		 String json_body="{\"mobile_number\": [";
		 while (iterator.hasNext()) {
			 json_body=json_body+"\""+iterator.next()+"\",";
		 }
		return json_body.substring(0,json_body.length()-1)+"]}";
	}
	
	public String remove_spaces(String num)
	{
		String final_num="";
		for(int i=0;i<num.length();i++)
		{
			switch(num.charAt(i))
			{
			case '0':case '1':case '2':case '3':case '4':case '5':case '6':case '7':case '8':case '9':
				final_num=final_num+num.charAt(i);
			}
		}
		if(final_num.length()>10)
			return final_num.substring(final_num.length()-10);
		else
			return final_num;
	}

	public ArrayList<Contact> send_contacts(String json_contacts,String number) {
		ArrayList<Contact> alContacts = new ArrayList<Contact>();
		String pokers;
		try{
		String URL = globals.serverIP+"getThePokers";
		HttpPost pokers_post =new HttpPost(URL);
		BasicHttpResponse pokers_resp; 
		HttpClient client = new DefaultHttpClient();
		StringEntity entity = new StringEntity(json_contacts,"UTF-8");
		pokers_post.setEntity(entity);
		pokers_post.setHeader("mobilenumber",number);
		pokers_post.setHeader("Authorization", "Basic cG9rZXJhcHA6cHJpY2s=");
		pokers_resp = (BasicHttpResponse) client.execute(pokers_post);
		int resp_code = pokers_resp.getStatusLine().getStatusCode();
		if(resp_code==201)
		{
			pokers = EntityUtils.toString(pokers_resp.getEntity());
			JSONObject mainObject2 = new JSONObject(pokers);
			JSONArray pokers_list1 = mainObject2.getJSONArray("count");
			for(int i =0;i<pokers_list1.length();i++)
			{
			JSONObject poker = pokers_list1.getJSONObject(i);
			Contact cnt = new Contact("",poker.getString("mobile"),poker.getInt("count_receive"),poker.getInt("count_sent"),System.currentTimeMillis());
			alContacts.add(cnt);
			}
		}
		}
		catch(Exception e){
			e.printStackTrace();
		}
		return alContacts;
	}



}
