/**
 * (C) COPYRIGHT 2018 Andrew Parle
 */
package poker;


/**
 * @author Andrew
 * Enumeration of the different possible suites
 */
public enum Suite
{
    DIAMONDS('D'), HEARTS('H'), SPADES('S'), CLUBS('C');
    
    public final Character  m_code;
    
    Suite(char  code)
    {
        m_code = code;
    }
    
    /**
     * Parses a character representing a suite. Allows case insensitive comparison or a change of coding.
     * @param code      Character representing a suite
     * @return matching Suite enum object
     * @throws PokerException if no match found
     */
    public static Suite
    parse(char  code)
        throws PokerException 
    {
        for (Suite s : Suite.values())
        {
            if (s.m_code.equals(code))
            {
                return s;
            }
        }
        throw new PokerException("Invalid Suite: " + code);
    }
}
