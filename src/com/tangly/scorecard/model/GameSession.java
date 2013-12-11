package com.tangly.scorecard.model;

import com.tangly.scorecard.datastore.*;
import com.tangly.scorecard.storage.*;

import java.util.*;

import org.apache.commons.collections4.CollectionUtils;

public class GameSession extends DefaultStorable implements Comparable<GameSession>
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

    public void setName(String name)
    {
        this.name = name;
    }

    public String getName()
    {
        return this.name;
    }

    public List<Player> getPlayers()
    {
        return this.players;
    }

    public void setPlayers(List<Player> players)
    {
        this.players = players;
    }

    @Override
    public void save(Datastore d)
    {
        for (Player p : this.getPlayers())
        {
            p.save(d);
        }
    }

    /**
     * Compares two GameSession. Note that ID is NOT checked.
     * 
     * @param another
     * @return
     */
    @Override
    public int compareTo(GameSession another)
    {
        // Check display name
        if (this.getDisplayName().compareTo(another.getDisplayName()) != 0)
        {
            return this.getDisplayName().compareTo(another.getDisplayName());
        }
        // Check players
        // TODO this may not be sufficient
        else if (!CollectionUtils.isEqualCollection(this.getPlayers(), another.getPlayers()))
        {
            // If the collections are not the same, find which one is "bigger"
            if (this.getPlayers().size() != another.getPlayers().size())
            {
                return (this.getPlayers().size() > another.getPlayers().size()) ? 1 : -1;
            }
            else
            {
                // Ok, they're not the same. Don't really care about
                // lexographically
                // comparing each player at this point, they're different
                return 1;
            }
        }
        // Same
        return 0;
    }
}
