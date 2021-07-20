package com.microboss.dev.adapter_items;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;


import com.bumptech.glide.Glide;
import com.microboss.dev.MeetBoard;
import com.microboss.dev.R;
import com.microboss.dev.UpdateService;

import java.util.ArrayList;



/*
 * This is demo code to accompany the Mobiletuts+ series:
 * Android SDK: Creating a Music Player
 * 
 * Sue Smith - February 2014
 */

public class SessionAdapter extends BaseAdapter {

	//song list and layout
	private ArrayList<Session> sessions;
	private LayoutInflater sessionsInf;

	//constructor
	public SessionAdapter(Context c, ArrayList<Session> theSessions){
		sessions = theSessions;
		sessionsInf = LayoutInflater.from(c);
	}

	@Override
	public int getCount() {
		return sessions.size();
	}

	@Override
	public Object getItem(int arg0) {
		return null;
	}

	@Override
	public long getItemId(int arg0) {
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		//map to song layout
		LinearLayout sessionLay = (LinearLayout) sessionsInf.inflate(R.layout.topic_content, parent, false);
		//get title and artist views
		TextView sessionTitle = (TextView)sessionLay.findViewById(R.id.session_title);
		TextView sessionInfo = (TextView)sessionLay.findViewById(R.id.session_info);
		ImageView sessionImg = (ImageView) sessionLay.findViewById(R.id.session_image);



		final Session currSession = sessions.get(position);
		sessionTitle.setText(currSession.getTitle());
		sessionInfo.setText(currSession.getInfo());

		Glide.with(MeetBoard.activity.getApplicationContext()).load(currSession.getTArt()).into(sessionImg);
//		sessionImg.setImageURI(currSession.getTArt());

		//set position as tag
		sessionLay.setTag(position);

		return sessionLay;
	}


}
