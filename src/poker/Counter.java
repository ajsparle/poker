/**
 * (C) COPYRIGHT 2018 Andrew Parle
 */
package poker;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

/**
 * @author Andrew
 * 
 * Main program for counting winning hands.<br>
 * It can be run either as a filter, accepting hands from standard input, or 
 * taking a filename as the only command line argument.
 */
public class Counter
{
    private  int  m_player1;
    private  int  m_player2;
    private  int  m_tied;
    private  int  m_errors;
    
    public
    Counter()
    {
        // default initialization okay
    }

    public void
    process(InputStream is)
        throws IOException
    {
        BufferedReader  buffered = new BufferedReader(new InputStreamReader(is));
        
        String  line = null;
        int     num  = 0;
        while ((line = buffered.readLine()) != null)
        {
            num++;
            try
            {
                processHands(line.trim());
            }
            catch (PokerException ex)
            {
                System.err.println("Line " + num + ": "+ ex.getMessage());
                m_errors++;
            }
        }
    }

    /*
     * Process a single line which must either be empty (which is ignored) or contains exactly 10 cards.
     * A line with only white space has length 0 since it has been trimmed.
     */
    private void
    processHands(String line)
        throws PokerException
    {
        // ignore empty lines or those with only white space
        if (line.length() == 0)
        {
            return;
        }

        ArrayList<Card> cards = Card.parseString(line);
        
        if (cards.size() != 10)
        {
            throw new PokerException("Hands with " + cards.size() + " cards");
        }
        
        Hand hand1 = new Hand(cards.subList(0, 5));
        Hand hand2 = new Hand(cards.subList(5, 10));
        
        int comp = hand1.compareTo(hand2);
        if (comp == 0)
        {
            m_tied++;
        }
        else if (comp < 0)
        {
            m_player2++;
        }
        else
        {
            m_player1++;
        }
    }
    
    public int
    getPlayer1()
    {
        return m_player1;
    }
    
    public int
    getPlayer2()
    {
        return m_player2;
    }
    
    public int
    getErrors()
    {
        return m_errors;
    }
    
    public int
    getTies()
    {
        return m_tied;
    }

    public static void
    main(String... args)
    {
        System.out.println("Running Counter\n");
        try
        {
            Counter  counter = new Counter();
            
            if (args.length > 0)
            {
                try (FileInputStream fis = new FileInputStream(args[0]))
                {
                    counter.process(fis);
                }
            }
            else
            {
                counter.process(System.in);
            }
            
            System.out.println("Player 1: " + counter.m_player1);
            System.out.println("Player 2: " + counter.m_player2);
            if (counter.m_tied > 0)
            {
                System.out.println("Tied    : " + counter.m_tied);
            }
            if (counter.m_errors > 0)
            {
                System.out.println("Errors  : " + counter.m_errors);
            }
            
            System.out.println("\nFINISHED");
        }
        catch (Exception ex)
        {
            System.err.println("\nERROR: " + ex);
        }
    }
}
