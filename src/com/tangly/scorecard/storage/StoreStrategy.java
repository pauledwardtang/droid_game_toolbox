package com.tangly.scorecard.storage;

// interface for... uhh i forgot looking back at the code :)
// TODO add javadoc
public interface StoreStrategy
{
    /**
     * @param s
     *            Storable to commit
     */
    public void commit(Storable s);

    /**
     * @param s
     *            Storable to retrieve
     */
    public Storable retrieve(int id);
}
