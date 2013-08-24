package com.tangly.scorecard.model;

import com.tangly.scorecard.datastore.*;
import com.tangly.scorecard.storage.*;
import java.util.*;

public class GameSession extends DefaultStorable
{
	private String name;
	private List<Player> players;

	public GameSession(String name,
					   List<Player> players)
	{
		this.name = name;
		this.players = players;
	}

	public GameSession(String name)
	{
		this(name, new ArrayList<Player>());
	}

	public GameSession()
	{
		this("", new ArrayList<Player>());
	}

	public String toString()
	{
		String retVal = "ID:" + this.getId();
		retVal += " Name:" + this.getName();
		for (Player player : this.getPlayers())
		{
			retVal += "\n" + player.toString();
		}
		return retVal;
	}


	public void setName(String name) { this.name = name; }
	public String getName() { return this.name; }
	public List<Player> getPlayers() { return this.players; }
	public void setPlayers(List<Player> players) { this.players = players; }

	@Override
	public void save(Datastore d)
	{
		for (Player p : this.getPlayers())
		{
			p.save(d);
		}
	}
	
//	public GameSession(Parcel in)
//	{
//		this.name = in.readString();
//		this.players = new ArrayList<Player>();
//		for (Player player : (Player[]) in.readParcelableArray(this.getClass().getClassLoader()))
//		{
//			players.add(player);
//		}
//	}
		
// this is used to regenerate the object
//	public static final Parcelable.Creator<GameSession> CREATOR 
//	= new Parcelable.Creator<GameSession>()
//	{
//		public GameSession createFromParcel(Parcel in)
//		{
//			return MainActivity.createGameSession(in);
//		}
//
//		public GameSession[] newArray(int size)
//		{
//			return new GameSession[size];
//		}
//	};
//		
//	@Override
//	public void writeToParcel(Parcel dest, int flags)
//	{
//		dest.writeString(this.name);
//		
//		Player[] playersArr = this.players.toArray(null);
//		dest.writeParcelableArray(playersArr, 0);
////		dest.writeArray(this.players);
//	}

//protected class DbStoreStrategy extends SimpleDatabaseStoreStrategy
//{
//	@Override
//	public void commit(Storable s)
//	{
//		// TODO: Implement this method
//	}
//
//	@Override
//	public Storable retrieve(int id)
//	{
//		// uhhh maybe this shouldnt be in the STORE interface
//		// TODO: Implement this method
//		return null;
//	}
//}
}

