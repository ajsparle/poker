/**
 * (C) COPYRIGHT 2018 Andrew Parle
 */
package poker;

/**
 * @author Andrew
 * 
 * Stores information about how a hand is to be evaluated, up to but NOT including highest card comparison
 */
public class Evaluation implements Comparable<Evaluation>
{
    // note - royal flush is just a straight flush, Ace high, as A2345 is not a straight.

    enum Kind { HIGH_CARD, ONE_PAIR, TWO_PAIRS, THREE_OF_A_KIND, STRAIGHT,
                FLUSH, FULL_HOUSE, FOUR_OF_A_KIND, STRAIGHT_FLUSH }
    
    /**
     * This function performs the logic of comparing hands, not including 'last ditch' comparison by decreasing card number
     */
    @Override
    public int
    compareTo(Evaluation other)
    {
        if (m_kind != other.m_kind)
        {
            return m_kind.compareTo(other.m_kind);
        }
        
        if (m_value != other.m_value)
        {
            return m_value.compareTo(other.m_value);
        }
        
        if (m_value2 != null && other.m_value2 != null)
        {
            return m_value2.compareTo(other.m_value2);
        }
        
        return 0;
    }
    
    private Kind    m_kind;
    private Number  m_value;
    private Number  m_value2;
    
    private
    Evaluation(Kind    kind,
               Number  value,
               Number  value2)  // second value card - lower of two pairs, pair in full house
    {
        m_kind   = kind;
        m_value  = value;
        m_value2 = value2;
    }
    
    @Override
    public String
    toString()
    {
        StringBuilder  buf = new StringBuilder();
        
        buf.append(m_kind).append(' ').append(m_value);
        
        if (m_value2 != null)
        {
            buf.append('/').append(m_value2);
        }
        return buf.toString();
    }
    
    /*
     * Public construction functions
     */
    public static Evaluation
    evalHighCard(Number high)
    {
        return new Evaluation(Kind.HIGH_CARD, high, null);
    }
    
    public static Evaluation
    evalOnePair(Number value)
    {
        return new Evaluation(Kind.ONE_PAIR, value, null);
    }
    
    public static Evaluation
    evalTwoPair(Number pairHigh, Number pairLow)
    {
        return new Evaluation(Kind.TWO_PAIRS, pairHigh, pairLow);
    }
    
    public static Evaluation
    evalThreeOfAKind(Number value)
    {
        return new Evaluation(Kind.THREE_OF_A_KIND, value, null);
    }
    
    public static Evaluation
    evalStraight(Number high)
    {
        return new Evaluation(Kind.STRAIGHT, high, null);
    }
    
    public static Evaluation
    evalFlush(Number high)
    {
        return new Evaluation(Kind.FLUSH, high, null);
    }
    
    public static Evaluation
    evalFullHouse(Number threes, Number pair)
    {
        return new Evaluation(Kind.FULL_HOUSE, threes, pair);
    }
    
    public static Evaluation
    evalFourOfAKind(Number value)
    {
        return new Evaluation(Kind.HIGH_CARD, value, null);
    }
    
    public static Evaluation
    evalStraightFlush(Number high)
    {
        return new Evaluation(Kind.HIGH_CARD, high, null);
    }
}
