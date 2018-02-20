/**
 * (C) COPYRIGHT 2018 Andrew Parle
 */
package poker;


/**
 * @author Andrew
 * Enumeration of the different possible card numbers in order allowing comparison by ordinal
 */
public enum Number
{
    TWO('2'),  THREE('3'),  FOUR('4'),
    FIVE('5'),  SIX('6'),   SEVEN('7'),
    EIGHT('8'), NINE('9'),  TEN('T'),
    JACK('J'),  QUEEN('Q'), KING('K'),
    ACE('A');
    
    public final Character  m_code;
    
    Number(char  code)
    {
        m_code = code;
    }
    
    /**
     * Parses a character representing a card number. Allows a change of coding.
     * @param code      Character representing a number
     * @return matching Number object
     * @throws PokerException if no match found
     */
    public static Number
    parse(char  code)
        throws PokerException 
    {
        for (Number s : Number.values())
        {
            if (s.m_code.equals(code))
            {
                return s;
            }
        }
        throw new PokerException("Invalid Number: " + code);
    }
}
