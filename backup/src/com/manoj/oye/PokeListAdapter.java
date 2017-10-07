package com.manoj.oye;

import java.net.URL;
import java.util.List;


import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.view.View.OnClickListener;

public class PokeListAdapter extends ArrayAdapter<Contact> {
	
	private class sendPoke extends AsyncTask<String, Integer, String> {
		
	     protected String doInBackground(String... urls) {
	    	 
			return "success";
	     }
	     protected void onPostExecute(String result) {
	    	 adapter.notifyDataSetChanged();
	     }
	 }

  private final List<Contact> list;
  private final Activity context;
  private final PokeListAdapter adapter;

  public PokeListAdapter(Activity context, List<Contact> list) {
    super(context, R.layout.row, list);
    this.context = context;
    this.list = list;
    this.adapter = this;
  }

  static class ViewHolder {
	  ImageView smiley;
	  TextView name;
	  ImageView upImage;
	  ImageView downImage;
	  TextView upScore;
	  TextView downScore;
	  Button poke;
  }

  @Override
  public Contact getItem(int position) {
    return list.get(position);
  }
  
  @Override
  public View getView(int position, View convertView, ViewGroup parent) {
    View view = null;
    
    if (convertView == null) {
    final ViewHolder viewHolder = new ViewHolder();
      LayoutInflater inflator = context.getLayoutInflater();
      view = inflator.inflate(R.layout.row, null);

      viewHolder.smiley = (ImageView)view.findViewById(R.id.contactImage);
      viewHolder.upImage = (ImageView)view.findViewById(R.id.upImage);
      viewHolder.downImage= (ImageView)view.findViewById(R.id.downImage);
      viewHolder.name = (TextView)view.findViewById(R.id.contactName);
      viewHolder.upScore = (TextView)view.findViewById(R.id.upScore);
      viewHolder.downScore = (TextView)view.findViewById(R.id.downScore);
      viewHolder.poke = (Button)view.findViewById(R.id.pokeButton);
      viewHolder.poke.setOnClickListener(new OnClickListener() { 
      	@Override public void onClick(View v) { 
    		Integer upScore = Integer.parseInt(viewHolder.upScore.getText().toString());
    		Integer downScore = Integer.parseInt(viewHolder.downScore.getText().toString());
    		upScore++;
    		viewHolder.upScore.setText(upScore.toString());
    		if(upScore>downScore)
    		{
    			viewHolder.upImage.setImageBitmap(BitmapFactory.decodeResource(context.getResources(),R.drawable.winning_up));
    			viewHolder.downImage.setImageBitmap(BitmapFactory.decodeResource(context.getResources(),R.drawable.normal_down));
    			viewHolder.smiley.setImageBitmap(BitmapFactory.decodeResource(context.getResources(),R.drawable.happy));
    			if(upScore-downScore>10)
    			{
    				viewHolder.smiley.setImageBitmap(BitmapFactory.decodeResource(context.getResources(),R.drawable.very_happy));
    			}
    		}
    		else if(upScore<downScore)
    		{
    			viewHolder.smiley.setImageBitmap(BitmapFactory.decodeResource(context.getResources(),R.drawable.happy));
    			viewHolder.upImage.setImageBitmap(BitmapFactory.decodeResource(context.getResources(),R.drawable.normal_up));
    			viewHolder.downImage.setImageBitmap(BitmapFactory.decodeResource(context.getResources(),R.drawable.winning_down));
    			if(downScore-upScore>10)
    			{
    				viewHolder.smiley.setImageBitmap(BitmapFactory.decodeResource(context.getResources(),R.drawable.very_sad));
    			}
    		}
    		else if(upScore==downScore)
    		{
    			viewHolder.smiley.setImageBitmap(BitmapFactory.decodeResource(context.getResources(),R.drawable.no_reaction));
    			viewHolder.upImage.setImageBitmap(BitmapFactory.decodeResource(context.getResources(),R.drawable.normal_up));
    			viewHolder.downImage.setImageBitmap(BitmapFactory.decodeResource(context.getResources(),R.drawable.normal_down));
    		}
    		//sendPoke sendpoke = new sendPoke();
    		
    		//sendpoke.execute(new String[] { viewHolder.toString() });
    		} 
    	}); 
      view.setTag(viewHolder);
      viewHolder.name.setTag(list.get(position));
    } else {
      view = convertView;
      ((ViewHolder) view.getTag()).name.setTag(list.get(position));
    }
    ViewHolder holder = (ViewHolder) view.getTag();
    holder.name.setText(list.get(position).getContactName());
    holder.smiley.setImageBitmap(BitmapFactory.decodeResource(context.getResources(),R.drawable.no_reaction));
    holder.upImage.setImageBitmap(BitmapFactory.decodeResource(context.getResources(),R.drawable.normal_up));
    holder.downImage.setImageBitmap(BitmapFactory.decodeResource(context.getResources(),R.drawable.normal_down));
    holder.upScore.setText(Integer.toString(list.get(position).getSentPoke()));
    holder.downScore.setText(Integer.toString(list.get(position).getRecvPoke()));
    return view;
  }
} 