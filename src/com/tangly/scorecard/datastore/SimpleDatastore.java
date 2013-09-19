package com.tangly.scorecard.datastore;

import java.util.Collection;
import java.util.Map;

import com.tangly.scorecard.storage.*;
import java.util.*;

public abstract class SimpleDatastore implements Datastore 
{
	private Map<Long, Storable> simpleMap;

	public SimpleDatastore()
	{
		simpleMap = new HashMap<Long, Storable>();
	}

	@Override
	public void set(Collection<? extends Storable> c)
	{
		c.clear();
		this.add(c);
	}

	public void add(Collection<? extends Storable> c)
	{
		for (Storable s : c)
		{
			this.add(s);
		}
	}

	public void add(Storable s) { simpleMap.put(s.getId(), s); }

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
		return simpleMap.get(id);
	}
	
	public <T extends Storable> Collection<Storable> getAll(Class<? extends Storable> type)
	{
		return simpleMap.values();
	}
}
