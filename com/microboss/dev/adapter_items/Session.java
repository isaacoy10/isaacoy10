package com.microboss.dev.adapter_items;

/*
 * This is demo code to accompany the Mobiletuts+ series:
 * Android SDK: Creating a Music Player
 * 
 * Sue Smith - February 2014
 */

import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;

public class Session implements Parcelable {

	private String title;
	private String info;
	private String date;
	private String URL;
	private byte[] art;



	public Session(String xtitle, String xinfo, String xdate, String xURL, byte[] xartUri){
		title=xtitle;
		info = xinfo;
		date = xdate;
		URL = xURL;
		art = xartUri;
	}

//	protected Session(Parcel in) {
//		title = in.readString();
//		info = in.readString();
//		date = in.readString();
//		URL = in.readString();
//		art = in.readParcelable(Bitmap.class.getClassLoader());
//	}
//
//	public static final Creator<Session> CREATOR = new Creator<Session>() {
//		@Override
//		public Session createFromParcel(Parcel in) {
//			return new Session(in);
//		}
//
//		@Override
//		public Session[] newArray(int size) {
//			return new Session[size];
//		}
//	};

	protected Session(Parcel in) {
		title = in.readString();
		info = in.readString();
		date = in.readString();
		URL = in.readString();
		art = in.createByteArray();
	}

	public static final Creator<Session> CREATOR = new Creator<Session>() {
		@Override
		public Session createFromParcel(Parcel in) {
			return new Session(in);
		}

		@Override
		public Session[] newArray(int size) {
			return new Session[size];
		}
	};

	public String getTitle(){return title;}
	public String getInfo(){return info;}
	public String getDate() {
		return date;
	}
	public String getStreamURL(){return URL;}
	public byte[] getTArt(){return art;}

	public void setTitle(String abc){
		title = abc;
	}
	public void setInfo(String abc){
		info = abc;
	}
	public void setDate(String date) {
		this.date = date;
	}
	public void setStreamURL(String abc){
		URL = abc;
	}
	public void setTArt(byte[] abc){
		art = abc;
	}



	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel parcel, int i) {
		parcel.writeString(title);
		parcel.writeString(info);
		parcel.writeString(date);
		parcel.writeString(URL);
		parcel.writeByteArray(art);
	}
}
