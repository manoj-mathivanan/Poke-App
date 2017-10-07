package com.manoj.oye;

public class Level {

	public static String getlevel(int pokes)
	{
		String l = "";
		if(pokes>=55000)
			l = "L6";
		if(pokes<55000)
			l = "L5";
		if(pokes<30000)
			l = "L4";
		if(pokes<14000)
			l = "L3";
		if(pokes<5000)
			l = "L2";
		if(pokes<1000)
			l = "L1";
		return l;
	}
	
	public static int getnextlevel(int pokes)
	{
		int l=0;
		if(pokes>55000)
			l=9999;
		if(pokes<55000)
			l=55000;
		if(pokes<30000)
			l=30000;
		if(pokes<14000)
			l=14000;
		if(pokes<5000)
			l=5000;
		if(pokes<1000)
			l=1000;
		
		return l;
	}
	
	public static int getpokesmultiply(int pokes)
	{
		int l = 0;
		String L = getlevel(pokes);
		l = Integer.parseInt(L.substring(1));
		return l;
	}
	
}
