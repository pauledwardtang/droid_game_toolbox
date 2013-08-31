package com.tangly.scorecard.model;

import com.tangly.framework.*;
import com.tangly.scorecard.storage.*;
import java.util.*;

/**
 * Class for custom dice
 */
public class Dice extends DefaultStorable
{
	private static final int DEFAULT_NUM_SIDES = 6;
	private static final String DEFAULT_NAME = "D6";
	Map<Integer, String> dieResultMap;

	/**
	 * Default cstor - creates a D6 by default
	 */
	public Dice()
	{
		this(DEFAULT_NUM_SIDES, DEFAULT_NAME);
	}

	public Dice(int numSides, String name)
	{
		this.name = name;
		this.dieResultMap = new HashMap<Integer, String>();
		for (int i = 0; i < numSides; i++)
		{
			this.dieResultMap.put(i, String.valueOf(i));
		}
	}

	public int getNumSides() { return this.dieResultMap.size(); }

	public String getResult(int i)
	{
		if (i <= DEFAULT_NUM_SIDES)
			return this.dieResultMap.get(i);
		else
			return "";
	}

	// TODO figure out a better way to this. This is a little too revealing
	public Map<Integer,String> getResultMap() { return this.dieResultMap; }

	/**
	 * Convenience class for creating Dice "piecewise"
	 */
	public static class DiceBuilder implements Builder<Dice>
	{
		private Dice dice;

		/**
		 * Creates a DiceBuilder with no die sides
		 */
		protected DiceBuilder()
		{
			// Create a new empty dice
			dice = new Dice(0, "");
		}

		/**
		 * Factory method for getting a dicebuilder
		 */
		public static DiceBuilder getBuilder()
		{
			return new DiceBuilder();
		}

		public DiceBuilder getBuilder(Dice dice)
		{
			DiceBuilder builder = new DiceBuilder();

			// Populate the builder with the given dice's information
			for (int i = 0; i < dice.getNumSides(); i++)
			{
				builder.add(i, dice.getResult(i));
			}
			return builder;
		}

		public Dice build()
		{
			return this.dice;
		}

		public DiceBuilder add(Integer sideNumber, String resultText)
		{
			this.dice.getResultMap().put(sideNumber, resultText);
			return this;
		}
		
		public DiceBuilder setName(String name)
		{
			this.dice.setDisplayName(name);
			return this;
		}
	}
}
