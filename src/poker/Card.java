/**
 * (C) COPYRIGHT 2018 Andrew Parle
 */
package poker;

import java.util.ArrayList;
import java.util.StringTokenizer;

/**
 * @author Andrew
 * 
 * Immutable object representing a card.
 */
public class Card
    implements Comparable<Card>
{
    // these may be public because they are final
    public final Suite   m_suite;
    public final Number  m_number;
    
    public
    Card(Suite suite, Number number)
    {
        m_suite = suite;
        m_number = number;
    }
    
    public String
    toString()
    {
        return "" + m_number.m_code + m_suite.m_code;
    }

    /**
     * This function is used for sorting cards into a hand, not for evaluation.
     * It sorts first by number, then by suite, so all cards with the same number are consecutive.
     */
    @Override
    public int
    compareTo(Card card)
    {
        int  diff = m_number.compareTo(card.m_number);
        if (diff != 0)
        {
            return diff;
        }
        
        diff = m_suite.compareTo(card.m_suite);
        if (diff != 0)
        {
            return diff;
        }

        return 0;
    }

    /**
     * Compares ignoring suite - used for hand evaluations (high card comparisons)
     * @param card
     * @return
     */
    public int
    compareNumber(Card card)
    {
        return m_number.compareTo(card.m_number);
    }
    
    /*
     * Parsing functions
     */
    /**
     * Accepts a string containing white-space separated character pairs representing cards
     * @param s  Input string.
     * @return  List of cards found - non-null but may be empty
     * @throws PokerException if invalid token is found
     */
    public static ArrayList<Card>
    parseString(String  s)
        throws PokerException
    {
        ArrayList<Card>  result = new ArrayList<Card>();
        
        StringTokenizer  tokens = new StringTokenizer(s);
        while (tokens.hasMoreTokens())
        {
            String  token = tokens.nextToken();
            if (token.length() == 0)
            {
                continue;
            }
            
            if (token.length() != 2)
            {
                throw new PokerException("Invalid token: " + token);
            }
            
            Number  number = Number.parse(token.charAt(0));
            Suite   suite  = Suite.parse(token.charAt(1));
            
            result.add(new Card(suite, number));
        }
        return result;
    }
}
