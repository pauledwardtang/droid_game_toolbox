package com.tangly.scorecard.storage;

import com.tangly.scorecard.datastore.*;

/**
 * Interface for storing and deleting
 */
public interface Storable
{
	/**
	 * @return Gets the ID for the Storable
	 */
	public long getId();

	/**
	 * @param id The ID to set
	 */
	public void setId(long id);
	
	/**
	 * Saves the storable
	 * @param d A datastore instance to use for saving the Storable
	 */
	public void save(Datastore d);
	
	/**
	 * Deletes the storable
	 * @param d A Datastore instance to use for deleting the Storable
	 */
	public void delete(Datastore d);
	
	/**
	 * @return if this Storable is valid
	 */
	public boolean isValid();
}

