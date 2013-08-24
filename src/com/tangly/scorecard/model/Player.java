package com.tangly.scorecard.model;


import java.util.*;
import com.tangly.scorecard.storage.*;

public class Player extends DefaultStorable// implements SchemaDefinable
{
	private String name;
	private int score;

	/**
	 * Default cstor
	 */
	public Player()
	{
		this("Default", 0);
	}

	public Player(String name,
				  int score)
	{
		this.name = name;
		this.score = score;
	}

	public String toString()
	{
		String retVal = "ID:" + this.getId() + this.getName();
		retVal += " Score:" + this.getScore();
		return retVal;
	}

	public String getName() { return this.name; }
	public void setName(String name) { this.name = name; }
	public int getScore() { return this.score; }
	public void setScore(int score) { this.score = score; }

//	public Player(Parcel in)
//	{
//		this(in.readString(), in.readInt());
//	}
		
// this is used to regenerate the object
//	public static final Parcelable.Creator<Player> CREATOR 
//		= new Parcelable.Creator<Player>()
//		{
//		public Player createFromParcel(Parcel in)
//		{
//			return MainActivity.createPlayer(in);
//		}
//
//		public Player[] newArray(int size)
//		{
//			return new Player[size];
//		}
//	};
//
//	@Override
//	public void writeToParcel(Parcel dest, int flags)
//	{
//		dest.writeString(this.name);
//		dest.writeInt(this.score);
//	}
}

