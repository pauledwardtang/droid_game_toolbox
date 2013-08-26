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

	/**
	 * @see Storable.getDisplayName()
	 */
	public String getDisplayName() { return this.getName(); }
}

