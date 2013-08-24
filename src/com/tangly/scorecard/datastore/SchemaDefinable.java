package com.tangly.scorecard.datastore;

/**
 *
 */
import java.util.*;

public interface SchemaDefinable
{
	/**
	 * @return the create statement
	 */
	public List<String> getCreateStatements();
	
	/**
	 *@ return the delete statement
	 */
	public List<String> getDeleteStatements();
}
