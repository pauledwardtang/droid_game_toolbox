package com.tangly.scorecard.storage;


import com.tangly.scorecard.datastore.*;

/**
 * Default Storable implementation.
 */
public abstract class DefaultStorable implements Storable
{
	private long id = DatastoreDefs.INVALID_ID;

	/**
	 * @see Storable.getId
	 */
	public long getId() { return this.id; }

	/**
	 * @see Storable.setId
	 */
	public void setId(long id) { this.id = id; }
	
	/**
	 * @see Storable.save
	 */
	public void save(Datastore d) { d.store(this); }

	/**
	 * @see Storable.delete
	 */
	public void delete(Datastore d)
	{
		this.setId(DatastoreDefs.INVALID_ID);
		d.delete(this);
	}

	// TODO Use this so some manager can figure out how to invalidate
	// references and such?
	/**
	 * @see Storable.isValid
	 */
	public boolean isValid() { return id == DatastoreDefs.INVALID_ID; }
}

