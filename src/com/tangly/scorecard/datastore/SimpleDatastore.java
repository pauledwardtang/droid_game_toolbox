package com.tangly.scorecard.datastore;

import java.util.Collection;
import java.util.Map;

import com.tangly.scorecard.model.Dice;
import com.tangly.scorecard.model.GameSession;
import com.tangly.scorecard.model.Player;
import com.tangly.scorecard.storage.*;

import java.util.*;

public abstract class SimpleDatastore implements Datastore 
{
	private Map<Class<? extends Storable>, Map<Long,Storable>> simpleMap;

	public SimpleDatastore()
	{
		simpleMap = new HashMap<Class<? extends Storable>, Map<Long,Storable>>();
	}

	@Override
	public void set(Collection<? extends Storable> c)
	{
		c.clear();
		this.add(c);
	}

	public void add(Collection<? extends Storable> c)
	{
		if (!c.isEmpty())
		{
			for (Storable s : c)
			{
				this.add(s);
			}
		}
	}

	public <T extends Storable> void add(T s)
	{
		// TODO infer class from a generic type argument
//		Class<? extends Storable> theClass = (Class<? extends Storable>) c.toArray()[0].getClass();
		Class<? extends Storable> theClass = s.getClass();

		// Lazy initialize
		if (!this.simpleMap.containsKey(theClass))
		{
			this.simpleMap.put(theClass, new HashMap<Long, Storable>());
		}

		simpleMap.get(theClass).put(s.getId(), s);
	}

	public boolean has(Storable s) 
	{
		return this.has(s.getId());
	}

	public boolean has(long id)
	{
		return simpleMap.containsKey(id);
		
	}
	public void remove(Storable s) { this.simpleMap.remove(s.getId()); }
	
	// TODO throws!
	public <T extends Storable> Storable get(long id, Class<? extends Storable> type)
	{
		return simpleMap.get(type).get(Long.valueOf(id));
	}
	
	public <T extends Storable> Collection<Storable> getAll(Class<? extends Storable> type)
	{
		return simpleMap.get(type).values();
	}
}
