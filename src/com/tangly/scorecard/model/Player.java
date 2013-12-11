package com.tangly.scorecard.model;

import com.tangly.scorecard.storage.*;

public class Player extends DefaultStorable implements Comparable<Player>
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
        return this.getDisplayName();
    }

    public int getScore()
    {
        return this.score;
    }

    public void setScore(int score)
    {
        this.score = score;
    }

    /**
     * Compares two Players. Note that ID is NOT checked.
     * 
     * @param another
     * @return
     */
    @Override
    public int compareTo(Player another)
    {
        return this.getDisplayName().compareTo(another.getDisplayName());
    }
}
