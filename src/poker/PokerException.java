/**
 * (C) COPYRIGHT 2018 Andrew Parle
 */
package poker;

/**
 * @author Andrew
 * 
 * Simple checkable exception for Poker errors
 */
public class PokerException extends Exception
{
    public
    PokerException(String message)
    {
        super(message);
    }
    
    public String
    toString()
    {
        return "Poker Exception: " + getMessage();
    }
}
