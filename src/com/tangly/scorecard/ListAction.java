package com.tangly.scorecard;

public enum ListAction
{
    GAMES ("Games"),
    PLAYERS ("Players"),
    DICE ("Dice")
    // ABOUT("About")
    ;

    private final String action;

    ListAction(String action)
    {
        this.action = action;
    }

    public String toString()
    {
        return this.action;
    }

    public static String[] toArray()
    {
        String[] retVal = new String[ListAction.values().length];
        for (int i = 0; i < ListAction.values().length; i++)
        {
            retVal[i] = ListAction.values()[i].toString();
        }
        return retVal;
    }
}
