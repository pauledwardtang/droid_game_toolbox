package com.tangly.scorecard.datastore;

import com.tangly.scorecard.storage.*;

/**
 * Simple interface for datastore implementations
 * TODO make this generic i.e., <? extends Storable>
 * TODO add javadoc
 */
import java.util.*;

public interface Datastore
{
	public void set(Collection<? extends Storable> c);

	public void add(Collection<? extends Storable> c);
	public void add(Storable s);

	public <T extends Storable> Storable get(long id, Class type);
	public <T extends Storable> Collection<Storable> getAll(Class type);

	public long store(Storable s);
	public void delete(Storable s);
}

