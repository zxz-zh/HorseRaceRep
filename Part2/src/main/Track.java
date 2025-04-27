/*
Applies terrain (weather) and track shape effects
NEED A CREATE LANES METHOD (TAKE printRace/Lanes method from Race class
 */

import java.util.ArrayList;


public class Track
{
    private String shape;
    private String condition;
    private int length;
    private int lanes;
    
    public Track(String Shape, String terrain, int distance, int Lanes)
    {
        this.length = distance;
        this.condition = terrain;
        this.shape = Shape;
        this.lanes = Lanes;
    }

    public String getShape ()
    {
        return this.shape;
    }
    
    public String getCondition ()
    {
        return this.condition;
    }

    public int getLength ()
    {
        return this.length;
    }

    public int getLanes ()
    {
        return this.lanes;
    }

    public void setShapeEffects (ArrayList<Horse> Horses)
    {
        for (Horse h: Horses)
        {
            if (h != null)
            {
                if (getShape().equalsIgnoreCase ("zigzag"))
                {
                    h.setModFall (h.getModFall() * 1.4);
                    h.setModDistance (h.getModDistance() * 0.8);
                }
                else if (getShape().equalsIgnoreCase ("oval"))
                {
                    h.setModConfidence (h.getModConfidence() * 1.1);
                }
                else if (getShape().equalsIgnoreCase ("figure-eight"))
                {
                    h.setModFall (h.getModFall() * 1.3);
                }
            }
        }
    }
    
    public void setWeatherEffects (ArrayList<Horse> Horses)
    {
        for (Horse h : Horses)
        {
            if (h != null)
            {
                if (getCondition().equalsIgnoreCase("muddy"))
                {
                    h.setModDistance(h.getModDistance() * 0.8);
                    h.setModFall(h.getModFall() * 1.2);
                    h.setModConfidence(h.getModConfidence() * 0.9);
                }
                else if (getCondition().equalsIgnoreCase("icy"))
                {
                    h.setModDistance(h.getModDistance() * 0.9);
                    h.setModFall(h.getModFall() * 1.5);
                    h.setModConfidence(h.getModConfidence() * 0.7);
                }
                else if (getCondition().equalsIgnoreCase("dry"))
                {
                    h.setModDistance(h.getModDistance() * 1.2);
                    h.setModFall(h.getModFall() * 0.9);
                    h.setModConfidence(h.getModConfidence() * 1.2);
                }
            }
        }
    }

}
