import java.util.Scanner;
import java.util.concurrent.TimeUnit;
import java.lang.Math;
import javax.swing.Timer;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

/**
 * A three-horse race, each horse running in its own lane
 * for a given distance
 * 
 * @author McRaceface
 * @version 1.0
 */
public class Race
{
    private int raceLength;
    private List <Horse> Horses;
    private int horseNo;

    /**
     * Constructor for objects of class Race
     * Initially there are no horses in the lanes
     * 
     * @param distance the length of the racetrack (in metres/yards...)
     */
    public Race(int distance)
    {
        // initialise instance variables
        raceLength = distance;
        Horses = new ArrayList<>();
    }

    public int getHorseNo ()
    {
        return horseNo;
    }

    public void incrementHorseNo()
    {
        horseNo++;
    }

    //checks if symbol is unique. If not, asks for new unique symbol
    public char getUniqueSymbol(Horse newHorse)
    {
        char symbol = newHorse.getSymbol();

        boolean duplicate = true;
        int counter = 0;

        while (duplicate)
        {
            duplicate = false;

            // Check against existing horses
            while (counter < Horses.size() && !duplicate)
            {
                Horse h = Horses.get(counter);
                if (h != null && h.getSymbol() == symbol)
                {
                    duplicate = true;
                }
                counter++;
            }

            if (duplicate)
            {
                Scanner scanner = new Scanner(System.in);
                System.out.println("Symbol '" + symbol + "' is already taken. Enter new symbol: ");
                symbol = scanner.nextLine().charAt(0);
            }
        }
        return symbol;
    }
    
    /**
     * Adds a horse to the race in a given lane
     * 
     * @param theHorse the horse to be added to the race
     * @param laneNumber the lane that the horse will be added to
     */
    public void addHorse(Horse theHorse, int laneNumber)
    {
        //ensure symbols are unique
        char uniqueSymbol = getUniqueSymbol(theHorse);
        if (uniqueSymbol != theHorse.getSymbol())
        {
            theHorse.setSymbol(uniqueSymbol);
        }

        while (laneNumber < 1)
        {
            Scanner scanner = new Scanner(System.in);
            System.out.println("Please enter a valid lane number: ");
            try
            {
                laneNumber = Integer.parseInt(scanner.nextLine());
            }
            catch (NumberFormatException e)
            {
                laneNumber = -1;
            }
            if (Horses.get(laneNumber -1) != null)
            {
                laneNumber = -1;
            }
        }

        //ensure list size is sufficient
        while (Horses.size() < laneNumber)
        {
            Horses.add(null);
        }

        Horses.set(laneNumber -1, theHorse);
        incrementHorseNo();
    }
    
    /**
     * Start the race
     * The horse are brought to the start and
     * then repeatedly moved forward until the 
     * race is finished
     */
    public void startRace()
    {
        if (getHorseNo() < 2)
        {
            System.out.println("Insufficient number of horses. Must have at least 2 horses to start.");
            return;
        }

        // Reset all horses
        for (Horse h : Horses)
        {
            if (h != null) h.goBackToStart();
        }

        List<String> winners = new ArrayList<>();
        boolean[] finished = {false}; // Array to allow modification in ActionListener

        // Create the timer
        Timer timer = new Timer(100, new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                if (finished[0])
                {
                    ((Timer)e.getSource()).stop();
                    return;
                }

                // Move horses
                for (Horse h : Horses)
                {
                    if (h != null)
                    {
                        moveHorse(h);
                    }
                }

                printRace();

                // Check for winners
                for (Horse h : Horses)
                {
                    if (h != null && raceWonBy(h))
                    {
                        finished[0] = true;
                        h.setConfidence(h.getConfidence() + 0.1);
                        winners.add(h.getName().toUpperCase());
                    }
                }

                // Check if all horses fell
                int fallenCount = 0;
                for (Horse h : Horses)
                {
                    if (h != null && h.hasFallen()) fallenCount++;
                }

                if (fallenCount == getHorseNo())
                {
                    finished[0] = true;
                }

                // Announce results if race finished
                if (finished[0])
                {
                    ((Timer)e.getSource()).stop();

                    //print winners or announce all horses fell
                    if (winners.size() == 1)
                    {
                        System.out.println("And the winner is... " + winners.get(0) + "!");
                    }
                    else if (! winners.isEmpty())
                    {
                        System.out.println("It's a tie between: " + String.join(", ", winners) + "!");
                    }
                    else if (finished[0])
                    {
                        System.out.println("All horses fell! There is no winner.");
                    }
                }
            }
        });

        timer.start();
        // Keep main thread alive for console app
        try {
            while (!finished[0]) {
                Thread.sleep(100);
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
    
    /**
     * Randomly make a horse move forward or fall depending
     * on its confidence rating
     * A fallen horse cannot move
     * 
     * @param theHorse the horse to be moved
     */
    private void moveHorse(Horse theHorse)
    {
        //return if no horse in lane or horse is fallen
        //if the horse has fallen it cannot move,
        //so only run if it has not fallen
        if (theHorse == null || theHorse.hasFallen())
        {
            return;
        }
        //the probability that the horse will move forward depends on the confidence;
        if (Math.random() < theHorse.getConfidence())
        {
            theHorse.moveForward();
        }

        //the probability that the horse will fall is very small (max is 0.1)
        //but will also will depends exponentially on confidence
        //so if you double the confidence, the probability that it will fall is *2
        if (Math.random() < (0.1*theHorse.getConfidence()*theHorse.getConfidence()))
        {
            theHorse.fall();
        }
    }
        
    /** 
     * Determines if a horse has won the race
     *
     * @param theHorse The horse we are testing
     * @return true if the horse has won, false otherwise.
     */
    private boolean raceWonBy(Horse theHorse)
    {
        //returns whether the horse has travelled the length of the race
        return (theHorse.getDistanceTravelled() == raceLength);
    }
    
    /***
     * Print the race on the terminal
     */
    private void printRace()
    {
        //System.out.print('\u000C');  //clear the terminal window
        System.out.print("\033[H\033[2J");
        
        multiplePrint('=',raceLength+3); //top edge of track
        System.out.println();

        for (Horse h: Horses)
        {
            if (h == null)
            {
                printEmptyLane();
                System.out.println();
            }
            else
            {
                printLane(h);
                System.out.println();
            }
        }

        
        multiplePrint('=',raceLength+3); //bottom edge of track
        System.out.println();    
    }
    
    /**
     * print a horse's lane during the race
     * for example
     * |           X                      |
     * to show how far the horse has run
     */

    private void printEmptyLane ()
    {
        //if no horse in lane, print empty lane
        System.out.print('|');
        multiplePrint(' ',raceLength +1); //+1 for what would have been horse's symbol
        System.out.print('|');
    }

    private void printLane(Horse theHorse)
    {
        //calculate how many spaces are needed before
        //and after the horse
        int spacesBefore = theHorse.getDistanceTravelled();
        int spacesAfter = raceLength - theHorse.getDistanceTravelled();
        
        //print a | for the beginning of the lane
        System.out.print('|');
        
        //print the spaces before the horse
        multiplePrint(' ',spacesBefore);
        
        //if the horse has fallen then print dead
        //else print the horse's symbol
        if(theHorse.hasFallen())
        {
            //System.out.print('\u2322');
            System.out.print('❌');
        }
        else
        {
            System.out.print(theHorse.getSymbol());
        }
        
        //print the spaces after the horse
        multiplePrint(' ',spacesAfter);
        
        //print the | for the end of the track
        System.out.print('|');

        //padding
        multiplePrint(' ',3);

        //print the horse's name & confidence
        System.out.print(theHorse.getName().toUpperCase() + " (Current confidence " + theHorse.getConfidence() + ") ");
    }
        
    
    /***
     * print a character a given number of times.
     * e.g. printmany('x',5) will print: xxxxx
     * 
     * @param aChar the character to Print
     */
    private void multiplePrint(char aChar, int times)
    {
        int i = 0;
        while (i < times)
        {
            System.out.print(aChar);
            i = i + 1;
        }
    }
}
