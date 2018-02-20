/**
 * (C) COPYRIGHT 2018 Andrew Parle
 */
package poker;

import java.util.Arrays;
import java.util.List;

/**
 * @author Andrew
 *
 * Represents a hand of five cards. Immutable as the input array is copied.
 * Cards are sorted on construction to simplify evaluation.
 */
public class Hand
    implements Comparable<Hand>
{
    public static final int Size = 5;
    
    private  Card[]      m_cards;
    private  Evaluation  m_evaluation;
    
    /**
     * Constructor from a list.
     * @param cards must contain exactly 5 cards
     * @throws PokerException
     */
    public
    Hand(List<Card> cards)
        throws PokerException
    {
        if (cards.size() != Size)
        {
            throw new PokerException("Hand with illegal card count " + cards.size());
        }
        
        m_cards = new Card[Size];
        cards.toArray(m_cards);
        Arrays.sort(m_cards);
        
        m_evaluation = makeEvaluation();
    }
    
    /**
     * Constructor from an array
     * @param cards must contain exactly 5 elements
     * @throws PokerException
     */
    public
    Hand(Card...cards)
        throws PokerException
    {
        if (cards.length != Size)
        {
            throw new PokerException("Hand with illegal card count " + cards.length);
        }
        
        m_cards = new Card[Size];
        System.arraycopy(cards, 0, m_cards, 0, Size);
        Arrays.sort(m_cards);
        
        m_evaluation = makeEvaluation();
    }
    
    @Override
    public String
    toString()
    {
        StringBuilder  buf = new StringBuilder();
        
        for (Card c : m_cards)
        {
            buf.append(c).append(' ');
        }
        buf.append(m_evaluation);
        return  buf.toString();
    }
    
    private Evaluation
    makeEvaluation()
    {
        // determine pattern of cards based on cards with the same number to generate an easily understandable key
        // uses the fact that the hand is sorted so that cards with the same number are consecutive 
        
        StringBuilder  buf = new StringBuilder();
        {
            Number  last  = m_cards[0].m_number;
            int     count = 0;
            
            for (Card c : m_cards)
            {
                if (last == c.m_number)
                {
                    count++;
                }
                else
                {
                    buf.append(count);
                    last = c.m_number;
                    count = 1;
                }
            }
            
            buf.append(count);
        }
        
        String  key = buf.toString();
        
        // one pair - first card in pair in positions 0-3
        if ("2111".equals(key))
        {
            return Evaluation.evalOnePair(m_cards[0].m_number);
        }
        
        if ("1211".equals(key))
        {
            return Evaluation.evalOnePair(m_cards[1].m_number);
        }
        
        if ("1121".equals(key))
        {
            return Evaluation.evalOnePair(m_cards[2].m_number);
        }
        
        if ("1112".equals(key))
        {
            return Evaluation.evalOnePair(m_cards[3].m_number);
        }
        
        // two pairs - note the higher pair appears later. Odd card in position 4, 2, 0
        if ("221".equals(key))
        {
            return Evaluation.evalTwoPair(m_cards[2].m_number, m_cards[0].m_number);
        }
        
        if ("212".equals(key))
        {
            return Evaluation.evalTwoPair(m_cards[3].m_number, m_cards[0].m_number);
        }
        
        if ("122".equals(key))
        {
            return Evaluation.evalTwoPair(m_cards[3].m_number, m_cards[1].m_number);
        }
        
        // three of a kind - first card can appear in positions 0-2
        if ("311".equals(key))
        {
            return Evaluation.evalThreeOfAKind(m_cards[0].m_number);
        }
        
        if ("131".equals(key))
        {
            return Evaluation.evalThreeOfAKind(m_cards[1].m_number);
        }
        
        if ("113".equals(key))
        {
            return Evaluation.evalThreeOfAKind(m_cards[2].m_number);
        }
        
        // full house - three of a kind can appear in positions 0 and 2, pair in 3 and 0 respectively
        if ("32".equals(key))
        {
            return Evaluation.evalFullHouse(m_cards[0].m_number, m_cards[3].m_number);
        }
        
        if ("23".equals(key))
        {
            return Evaluation.evalFullHouse(m_cards[2].m_number, m_cards[0].m_number);
        }
        
        // four of a kind - can appear in positions 0 and 1 only.
        if ("41".equals(key))
        {
            return Evaluation.evalFourOfAKind(m_cards[0].m_number);
        }

        if ("14".equals(key))
        {
            return Evaluation.evalFourOfAKind(m_cards[1].m_number);
        }
        
        // only other valid key is 11111 - all different numbers.
        // Assertion because this is a programming error if wrong!
        assert "11111".equals(key);
        
        Number  low      = m_cards[0].m_number;
        Number  high     = m_cards[Size-1].m_number;
        
        boolean straight = low.ordinal() + 4 == high.ordinal();
        
        // note that if we needed to allow the Ace to have value 1 to make a straight, would change line above
        // to allow low == Number.TWO && m_cards[3] == Number.FIVE && high == Number.ACE as well.
        // this would also require a tweak of the Evaluation object and other small changes.

        boolean flush    = true;
        {
            Suite   suite = m_cards[0].m_suite;
            
            for (int i = 1; i < Size && flush; ++i)
            {
                Card c = m_cards[i];
                if (c.m_suite != suite)
                {
                    flush = false;
                }
            }
        }
        
        if (straight && flush)
        {
            return Evaluation.evalStraightFlush(high);
        }
        
        if (flush)
        {
            return Evaluation.evalFlush(high);
        }
        
        if (straight)
        {
            return Evaluation.evalStraight(high);
        }
        
        // nothing
        return Evaluation.evalHighCard(high);
    }

    /**
     * Compare two hands to see which one is highest. I use the Comparable interface here because maybe at some
     * point we need to sort more than two hands.
     */
    @Override
    public int
    compareTo(Hand other)
    {
        // perform straight evaluation comparison
        int  comp = m_evaluation.compareTo(other.m_evaluation);
        if (comp != 0)
        {
            return comp;
        }
        
        // compare highest cards in descending order
        
        for (int i = Size-1; i >= 0; --i)
        {
            int diff = m_cards[i].compareNumber(other.m_cards[i]);
            if (diff != 0)
            {
                return diff;
            }
        }
        
        // Tie - basically the hands are identical differing only in suites
        return 0;
    }
    
    /*
     * Unit test code based on the examples provided in the problem specification.
     */
    public static void
    main(String... args)
    {
        System.out.println("Testing Hand");
        try
        {
            test("4H 4C 6S 7S KD", "2C 3S 9S 9D TD", true);
            test("5D 8C 9S JS AC", "2C 5C 7D 8S QH", false);
            test("2D 9C AS AH AC", "3D 6D 7D TD QD", true);
            test("4D 6S 9H QH QC", "3D 6D 7H QD QS", false);
            test("2H 2D 4C 4D 4S", "3C 3D 3S 9S 9D", false);
            
            System.out.println("\nTest PASSED");
        }
        catch (PokerException ex)
        {
            System.err.println("\nERROR: " + ex);
        }
    }
    
    private static void
    test(String s1, String s2, boolean expect)
        throws PokerException
    {
        System.out.println("\nTest " + s1 + " || " + s2 + " => " + expect);
        
        Hand  hand1 = new Hand(Card.parseString(s1));
        System.out.println("   Hand1 " + hand1);
        
        Hand  hand2 = new Hand(Card.parseString(s2));
        System.out.println("   Hand2 " + hand2);
        
        compare(hand1, hand2, expect);
        compare(hand2, hand1, !expect);
    }
    
    private static boolean compare(Hand hand1, Hand hand2, boolean expect)
        throws PokerException
    {
        int comp = hand1.compareTo(hand2);
        
        if (comp == 0)
        {
            throw new PokerException("Unexpected tie: ");
        }
        
        StringBuilder  buf = new StringBuilder("    ");
        boolean  second = comp < 0;
        
        if (second)
        {
            buf.append(hand2);
        }
        else
        {
            buf.append(hand1);
        }
        
        buf.append(" Wins ").append((second == expect ? "OK":"ERROR"));
        System.out.println(buf);
        
        if (second != expect)
        {
            throw new PokerException("Wrong one won!");
        }

        return second;
    }
}
