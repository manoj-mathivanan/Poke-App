package com.manoj.oye;

import java.util.ArrayList;

import android.graphics.Point;
import android.media.MediaPlayer;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.widget.ImageView;

public class globals {
public static ArrayList<Contact> Contacts;
//public static ArrayList<String> names;
//public static ArrayList<String> numbers;
public static ArrayList<Contact> temp_contacts;
static ArrayList<Contact> contactsmain;
public static String senderID = "312227851237";
public static String serverIP = "http://enigmatic-river-5419.herokuapp.com/service/";
public static ArrayList<Contact> newpokescontact = new ArrayList<Contact>();
static Ringtone r;
public static ArrayList<Contact> sendnewpokes = new ArrayList<Contact>();
public static int getposition(String number)
{
	int i=0;
	int flag = 0;
	for(i=0;i<globals.contactsmain.size();i++)
	{
		if(number.compareToIgnoreCase(contactsmain.get(i).getContactNumber())==0)
		{
			flag=1;
			break;
		}
	}
	if(flag==1)
		return i;
	else
		return -1;
}
}
