package com.tangly.scorecard.model;

import com.tangly.scorecard.datastore.*;
import com.tangly.scorecard.storage.*;
import java.util.*;

public class GameSession extends DefaultStorable
{
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
}

