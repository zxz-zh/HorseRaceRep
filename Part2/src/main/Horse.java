/*
 * Decompiled with CFR 0.152.
 */

public class Horse {
    private String name;
    private char symbol;
    private int distance;
    private Boolean fallen = false;
    private double confidence;
    private String breed; // New field
    private String coatColor; // New field
    private String equipment;

    // Weather impact modifiers: 1 = normal
    private double modDistance = 1.0;
    private double modFall = 1.0;
    private double modConfidence = 1.0;


    public Horse(char horseSymbol, String horseName, double horseConfidence, String breed, String coatColor, String equipment)
    {
        if (horseSymbol == ' ')
        {
            throw new IllegalArgumentException("Horse symbol cannot be white space. ");
        }
        this.symbol = horseSymbol;
        this.name = horseName;
        this.confidence = horseConfidence;
        this.breed = breed;
        if (breed.equals("Thoroughbred"))
        {
            this.modDistance *= 1.2;
            this.modConfidence *= 0.9;
        }
        else if (breed.equals("Arabian"))
        {
            this.modFall *= 0.8;
        }
        else if (breed.equals ("Quarter Horse"))
        {
            this.modDistance *= 1.1;
            this.modConfidence *= 1.1;
        }
        this.coatColor = coatColor;
        this.equipment = equipment;
        if (equipment.equals("Lightweight"))
        {
            this.modDistance *= 1.2;
            this.modFall *= 1.1;
        }
        else if (equipment.equals("Heavy"))
        {
            this.modDistance *= 0.9;
            this.modFall *= 0.8;
        }
        this.fallen = false;
    }

    public void fall()
    {
        this.fallen = true;
        if (this.getConfidence() > 0.0)
        {
            this.setConfidence(this.getConfidence() - 0.1);
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

    public void setDistance(int distance)
    {
        this.distance += distance;
    }

    public String getName()
    {
        return this.name;
    }

    public char getSymbol()
    {
        return this.symbol;
    }

    public String getBreed()
    {
        return breed;
    }

    public String getCoatColor()
    {
        return coatColor;
    }

    public String getEquipment()
    {
        return equipment;
    }

    public double getModDistance()
    {
        return this.modDistance;
    }

    public double getModFall()
    {
        return this.modFall;
    }

    public double getModConfidence()
    {
        return this.modConfidence;
    }

    public void setModDistance (double modifier)
    {
        this.modDistance = modifier;
    }

    public void setModFall (double modifier)
    {
        this.modFall = modifier;
    }

    public void setModConfidence (double modifier)
    {
        this.modConfidence = modifier;
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

    public void setConfidence(double newConfidence)
    {
        if (this.confidence == 0.0 && newConfidence < 0.0 || this.confidence == 1.0 && newConfidence > 1.0)
        {
            return;
        }
        if (newConfidence < 0.0 || newConfidence > 1.0)
        {
            throw new IllegalArgumentException("Confidence must be between 0.0 and 1.0 ");
        }
        this.confidence = newConfidence;
    }

    public void setSymbol(char newSymbol)
    {
        this.symbol = newSymbol;
    }
}
