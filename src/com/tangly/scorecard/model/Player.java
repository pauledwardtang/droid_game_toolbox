package com.tangly.scorecard.model;


import java.util.*;
import com.tangly.scorecard.storage.*;

public class Player extends DefaultStorable// implements SchemaDefinable
{
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

	// Should be using "getDisplayName" now from Storable interface
	@Deprecated
	public String getName() { return this.getDisplayName(); }

	// Should be using "getDisplayName" now from Storable interface
	@Deprecated
	public void setName(String name) { this.setDisplayName(name); }
	public int getScore() { return this.score; }
	public void setScore(int score) { this.score = score; }
}

