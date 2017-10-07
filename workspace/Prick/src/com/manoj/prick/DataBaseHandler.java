package com.manoj.prick;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DataBaseHandler extends SQLiteOpenHelper {

private static final int DATABASE_VERSION = 1;

private static final String DATABASE_NAME = "pokedb";

private static final String TABLE_CONTACTS = "contacts";

private static final String NAME = "name";
private static final String NUMBER = "number";
private static final String RECV_POKE = "rpoke";
private static final String SENT_POKE = "spoke";
private static final String TIME = "time";
private static final String ID = "id";
private static final String NEW = "new";
private static final String MESSAGE = "message";
private static final String NEW_MESSAGE = "new_message";
public DataBaseHandler(Context context) {
super(context, DATABASE_NAME, null, DATABASE_VERSION);
}

@Override
public void onCreate(SQLiteDatabase db) {
String CREATE_CONTACTS_TABLE = "CREATE TABLE " + TABLE_CONTACTS + "("+ID+" INTEGER PRIMARY KEY,"+ NAME + " TEXT,"+ NUMBER + " TEXT," + RECV_POKE + " INTEGER," + SENT_POKE + " INTEGER," + TIME + " TEXT," + NEW + " INTEGER,"+ MESSAGE + " TEXT,"+ NEW_MESSAGE + " TEXT"+")";
db.execSQL(CREATE_CONTACTS_TABLE);
}

@Override
public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
db.execSQL("DROP TABLE IF EXISTS " + TABLE_CONTACTS);
onCreate(db);
}

public void addContact(Contact contact) {
SQLiteDatabase db = this.getWritableDatabase();
ContentValues values = new ContentValues();
values.put(NAME, contact.getContactName());
values.put(NUMBER, contact.getContactNumber());
values.put(RECV_POKE, contact.getRecvPoke());
values.put(SENT_POKE, contact.getSentPoke());
values.put(TIME, contact.getLastTime());
values.put(NEW, contact.getNewpokes());
values.put(MESSAGE, contact.getMessage());
values.put(NEW_MESSAGE, contact.getNew_message());
db.insert(TABLE_CONTACTS, null, values);
db.close();
}

public ArrayList<Contact> getAllContacts() {
ArrayList<Contact> contactList = new ArrayList<Contact>();
String selectQuery = "SELECT * FROM contacts order by time desc,name";
SQLiteDatabase db = this.getWritableDatabase();
Cursor cursor = db.rawQuery(selectQuery, null);
if (cursor.moveToFirst()) {
do {
Contact contact = new Contact(cursor.getString(1),cursor.getString(2),Integer.parseInt(cursor.getString(3)),Integer.parseInt(cursor.getString(4)),Long.parseLong(cursor.getString(5)),Integer.parseInt(cursor.getString(6)),cursor.getString(7),cursor.getString(8));
contactList.add(contact);
} while (cursor.moveToNext());
}
db.close();
return contactList;
}

public ArrayList<Contact> getAllContacts2() {
	ArrayList<Contact> contactList = new ArrayList<Contact>();
	String selectQuery = "SELECT * FROM contacts order by rpoke desc,name";
	SQLiteDatabase db = this.getWritableDatabase();
	Cursor cursor = db.rawQuery(selectQuery, null);
	if (cursor.moveToFirst()) {
	do {
	Contact contact = new Contact(cursor.getString(1),cursor.getString(2),Integer.parseInt(cursor.getString(3)),Integer.parseInt(cursor.getString(4)),Long.parseLong(cursor.getString(5)),Integer.parseInt(cursor.getString(6)),cursor.getString(7),cursor.getString(8));
	contactList.add(contact);
	} while (cursor.moveToNext());
	}
	db.close();
	return contactList;
  }

public int updateSentPoke(String number,int poke) {
//String selectQuery = "update contacts set spoke=spoke+1 and time = '"+System.currentTimeMillis()+"' where number ='"+number+"'";
SQLiteDatabase db = this.getWritableDatabase();
ContentValues values = new ContentValues();
values.put(SENT_POKE, poke);
values.put(TIME, System.currentTimeMillis());
int res =  db.update(TABLE_CONTACTS, values, NUMBER + " = ?",new String[] { number });
db.close();
return res;
}

public void droptables() {
	// TODO Auto-generated method stub
	//String selectQuery = "update contacts set spoke=spoke+1 and time = '"+System.currentTimeMillis()+"' where number ='"+number+"'";
	SQLiteDatabase db = this.getWritableDatabase();
	db.execSQL("DROP TABLE IF EXISTS " + TABLE_CONTACTS);
	onCreate(db);
	db.close();
}

public Contact updateRecPokes(String number,int n) {
	SQLiteDatabase db = this.getWritableDatabase();
	int poke=0;
	String name = null;
	String selectQuery = "SELECT rpoke,name FROM contacts where number = \""+number+"\"";
	Cursor cursor = db.rawQuery(selectQuery, null);
	if (cursor.moveToFirst()) {
	do {
	poke=cursor.getInt(0);
	name=cursor.getString(1);
	} while (cursor.moveToNext());
	}
	cursor.close();
	poke=n-poke;
	Contact cnt = new Contact(name, String.valueOf(poke));
	ContentValues values = new ContentValues();
	values.put(RECV_POKE, n);
	values.put(TIME, System.currentTimeMillis());
	int res =  db.update(TABLE_CONTACTS, values, NUMBER + " = ?",new String[] { number });
	db.close();
	return cnt;
}

public int pokeFail(String number) {
	SQLiteDatabase db = this.getWritableDatabase();
	int poke=0;
	String selectQuery = "SELECT spoke FROM contacts where number = \""+number+"\"";
	Cursor cursor = db.rawQuery(selectQuery, null);
	if (cursor.moveToFirst()) {
	do {
	poke=cursor.getInt(0);
	} while (cursor.moveToNext());
	}
	cursor.close();
	poke--;
	ContentValues values = new ContentValues();
	values.put(SENT_POKE, poke);
	values.put(TIME, System.currentTimeMillis());
	int res =  db.update(TABLE_CONTACTS, values, NUMBER + " = ?",new String[] { number });
	db.close();
	return res;
}

public void updateNewPoken(String number2,int n) {
	// TODO Auto-generated method stub
	SQLiteDatabase db = this.getWritableDatabase();
	int poke=0;
	String selectQuery = "SELECT new FROM contacts where number = \""+number2+"\"";
	Cursor cursor = db.rawQuery(selectQuery, null);
	if (cursor.moveToFirst()) {
	do {
	poke=cursor.getInt(0);
	} while (cursor.moveToNext());
	}
	cursor.close();
	poke=poke+n;
	ContentValues values = new ContentValues();
	values.put(NEW, poke);
	values.put(TIME, System.currentTimeMillis());
	int res =  db.update(TABLE_CONTACTS, values, NUMBER + " = ?",new String[] { number2 });
	db.close();
}

public void updateNewPoke0(String number2) {
	// TODO Auto-generated method stub
	SQLiteDatabase db = this.getWritableDatabase();
	ContentValues values = new ContentValues();
	values.put(NEW, 0);
	values.put(TIME, System.currentTimeMillis());
	int res =  db.update(TABLE_CONTACTS, values, NUMBER + " = ?",new String[] { number2 });
	db.close();
}

public void updateMessage(String number2, String message) {
	// TODO Auto-generated method stub
	SQLiteDatabase db = this.getWritableDatabase();
	ContentValues values = new ContentValues();
	values.put(MESSAGE, message);
	values.put(TIME, System.currentTimeMillis());
	int res =  db.update(TABLE_CONTACTS, values, NUMBER + " = ?",new String[] { number2 });
	db.close();
}

public void updateNewMessage(String number2, String message) {
	// TODO Auto-generated method stub
	SQLiteDatabase db = this.getWritableDatabase();
	ContentValues values = new ContentValues();
	values.put(NEW_MESSAGE, message);
	values.put(TIME, System.currentTimeMillis());
	int res =  db.update(TABLE_CONTACTS, values, NUMBER + " = ?",new String[] { number2 });
	db.close();
}

public void updateTime(String number2, long time) {
	// TODO Auto-generated method stub
	SQLiteDatabase db = this.getWritableDatabase();
	ContentValues values = new ContentValues();
	values.put(TIME, time);
	int res =  db.update(TABLE_CONTACTS, values, NUMBER + " = ?",new String[] { number2 });
	db.close();
}

/*
Contact getContact(int id) {
SQLiteDatabase db = this.getReadableDatabase();
Cursor cursor = db.query(TABLE_CONTACTS, new String[] { NUMBER, NAME, RECV_POKE,SENT_POKE,TIME }, NUMBER + "=?",new String[] { String.valueOf(id) }, null, null, null, null);
if (cursor != null)
cursor.moveToFirst();

Contact contact = new Contact(Integer.parseInt(cursor.getString(0)),
cursor.getString(1), cursor.getBlob(1));

// return contact
return contact;

}

public List<Contact> getAllContacts() {
List<Contact> contactList = new ArrayList<Contact>();
// Select All Query
String selectQuery = "SELECT * FROM contacts ORDER BY name";

SQLiteDatabase db = this.getWritableDatabase();
Cursor cursor = db.rawQuery(selectQuery, null);
// looping through all rows and adding to list
if (cursor.moveToFirst()) {
do {
Contact contact = new Contact();
contact.setID(Integer.parseInt(cursor.getString(0)));
contact.setName(cursor.getString(1));
contact.setImage(cursor.getBlob(2));
// Adding contact to list
contactList.add(contact);
} while (cursor.moveToNext());
}
// close inserting data from database
db.close();
// return contact list
return contactList;

}

// Updating single contact
public int updateContact(Contact contact) {
SQLiteDatabase db = this.getWritableDatabase();

ContentValues values = new ContentValues();
values.put(KEY_NAME, contact.getName());
values.put(KEY_IMAGE, contact.getImage());

// updating row
return db.update(TABLE_CONTACTS, values, KEY_ID + " = ?",
new String[] { String.valueOf(contact.getID()) });

}

// Deleting single contact
public void deleteContact(Contact contact) {
SQLiteDatabase db = this.getWritableDatabase();
db.delete(TABLE_CONTACTS, KEY_ID + " = ?",
new String[] { String.valueOf(contact.getID()) });
db.close();
}

// Getting contacts Count
public int getContactsCount() {
String countQuery = "SELECT * FROM " + TABLE_CONTACTS;
SQLiteDatabase db = this.getReadableDatabase();
Cursor cursor = db.rawQuery(countQuery, null);
cursor.close();

// return count
return cursor.getCount();
}
*/
}

