import java.util.Scanner;

/**
 * Write a description of class Horse here.
 * A horse class, which can create horse objects
 *  to compete in the race.
 * @author Xinzhu Zhao
 * @version 1.0
 */
public class Horse
{
    //Fields of class Horse
    private String name;
    private char symbol;
    private int distance;
    private Boolean fallen = false;
    private double confidence;

    //Constructor of class Horse
    /**
     * Constructor for objects of class Horse
     */
    public Horse(char horseSymbol, String horseName, double horseConfidence)
    {
        while (horseSymbol == ' ')
        {
            Scanner scanner = new Scanner(System.in);
            System.out.print("Cannot enter white space. Please re-enter: ");
            horseSymbol = scanner.next().charAt(0);
        }
        this.symbol = horseSymbol;
        this.name = horseName;
        this.confidence = horseConfidence;
    }
    
    
    
    //Other methods of class Horse
    public void fall()
    {
        this.fallen = true;
        if (getConfidence() > 0)
        {
            setConfidence(getConfidence()-0.1);
        }
    }
    
    public double getConfidence()
    {
        return this.confidence;
    }
    
    public int getDistanceTravelled()
    {
        return this.distance;
    }
    
    public String getName()
    {
        return this.name;
    }
    
    public char getSymbol()
    {
        return this.symbol;
    }
    
    public void goBackToStart()
    {
        this.distance = 0;
        this.fallen = false;
    }
    public boolean hasFallen()
    {
        return this.fallen;
    }

    public void moveForward()
    {
        this.distance ++;
    }

    public void setConfidence(double newConfidence)
    {
        if (this.confidence == 0 && newConfidence < 0 || this.confidence == 1 && newConfidence > 1)
        {
            return; //if confidence is at max/min, it will not change on attempts to +/- further
        }

        while (newConfidence < 0 || newConfidence > 1)
        {
            Scanner scanner = new Scanner (System.in);
            System.out.println("Invalid confidence. Please re-enter: ");
            newConfidence = Double.parseDouble(scanner.nextLine());
        }
        this.confidence = newConfidence;
    }
    
    public void setSymbol(char newSymbol)
    {
        this.symbol = newSymbol;
    }
    
}
