/*
 * Decompiled with CFR 0.152.
 */
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.Collections;

public class Race
{
    private Track raceTrack;
    private ArrayList<Horse> Horses;
    private int horseNo;
    private Boolean finished;
    private long startTime;
    private long endTime;
    private List<String> winners;

    public Race(Track thisTrack)
    {
        raceTrack = thisTrack;
        this.Horses = new ArrayList<Horse>();
    }

    public int getHorseNo()
    {
        return this.horseNo;
    }

    public List<Horse> getHorses ()
    {
        return Horses;
    }

    public void incrementHorseNo()
    {
        ++this.horseNo;
    }

    public Boolean isFinished ()
    {
        return this.finished;
    }

    public long getStartTime ()
    {
        return this.startTime;
    }

    public long getEndTime ()
    {
        return this.endTime;
    }

    public void setStartTime ()
    {
        this.startTime = System.nanoTime();
    }

    public void setEndTime ()
    {
        this.endTime = System.nanoTime();
    }

    public List<String> getWinners()
    {
        return this.winners;
    }

    public void setWinners (String winner)
    {
        this.winners.add(winner);
    }

    public void addHorse(Horse theHorse, int laneNumber)
    {
        if (laneNumber < 1)
        {
            throw new IllegalArgumentException("Lane number cannot be less than 1. ");
        }
        else if (this.Horses.get(laneNumber - 1) != null)
        {
            throw new IllegalArgumentException("Lane number " + laneNumber + " is already in the race. ");
        }

        while (this.Horses.size() < laneNumber)
        {
            this.Horses.add(null);
        }

        this.Horses.set(laneNumber - 1, theHorse);
        this.incrementHorseNo();
    }

    public void startRace()
    {
        if (this.getHorseNo() < 2)
        {
            System.out.println("Insufficient number of horses. Must have at least 2 horses to start.");
            return;
        }

        this.finished = false;
        this.winners = new ArrayList<>();
        for (Horse h : this.Horses)
        {
            if (h != null)
            {
                h.goBackToStart();
            }
        }
        this.raceTrack.setShapeEffects(this.Horses);
        this.raceTrack.setWeatherEffects(this.Horses);

        setStartTime();
        while (!this.finished)
        {
            int fallenNo = 0;
            for (Horse h : this.Horses)
            {
                if (h == null) continue;
                this.moveHorse(h);
            }
            this.printRace();
            for (Horse h : this.Horses)
            {
                if (h != null && h.hasFallen())
                {
                    ++fallenNo;
                }
            }
            if (fallenNo == this.getHorseNo())
            {
                this.finished = true;
            }
            for (Horse h : this.Horses)
            {
                if (h == null || !this.raceWonBy(h)) continue;
                this.finished = true;
                h.setConfidence(h.getConfidence() + 0.1);
                setWinners(h.getName().toUpperCase());
            }

            if (getWinners().size() == 1)
            {
                System.out.println("And the winner is... " + (String)getWinners().get(0) + "! ");
            }
            else if (!getWinners().isEmpty())
            {
                System.out.println("It's a tie between: " + String.join((CharSequence)", ", getWinners()) + "! ");
            }
            else if (this.finished)
            {
                System.out.println("All horses fell! There is no winner. ");
            }
            try
            {
                TimeUnit.MILLISECONDS.sleep(100);
            }
            catch (Exception exception) {}
        }
        setEndTime();
        sortHorses();
        recordResults();
    }

    public void moveHorse(Horse h) {
        if (h != null && ! h.hasFallen())
        {
            //probability of moving
            if (Math.random() < h.getConfidence() * h.getModConfidence())
            {
                h.setDistance((int)Math.round(h.getModDistance()));
            }

            //probability of falling
            if (Math.random() < 0.1 * (1- h.getConfidence()*h.getModConfidence()) * h.getModFall())
            {
                h.fall();
            }
        }
    }

    public boolean raceWonBy(Horse theHorse)
    {
        return theHorse.getDistanceTravelled() >= this.raceTrack.getLength();
    }

    private void printRace()
    {
        System.out.print("\u001b[H\u001b[2J");
        this.multiplePrint('=', this.raceTrack.getLength() + 3);
        System.out.println();
        for (Horse h : this.Horses)
        {
            if (h == null)
            {
                this.printEmptyLane();
            }
            else
            {
                this.printLane(h);
            }
            System.out.println();
        }
        this.multiplePrint('=', this.raceTrack.getLength() + 3);
        System.out.println();
    }

    private void printEmptyLane() {
        System.out.print('|');
        this.multiplePrint(' ', this.raceTrack.getLength() + 1);
        System.out.print('|');
    }

    private void printLane(Horse theHorse) {
        int spacesBefore = theHorse.getDistanceTravelled();
        int spacesAfter = this.raceTrack.getLength() - theHorse.getDistanceTravelled();
        System.out.print('|');
        this.multiplePrint(' ', spacesBefore);
        if (theHorse.hasFallen())
        {
            System.out.print('\u274c');
        }
        else
        {
            System.out.print(theHorse.getSymbol());
        }
        this.multiplePrint(' ', spacesAfter);
        System.out.print('|');
        System.out.print(theHorse.getName().toUpperCase() + " (Current confidence " + theHorse.getConfidence() + ") ");
    }

    private void multiplePrint(char aChar, int times)
    {
        for (int i = 0; i < times; ++i)
        {
            System.out.print(aChar);
        }
    }

    public void sortHorses()
    {
        Horses.removeIf(horse -> horse == null);
        Horses.sort(Comparator.comparingInt(Horse::getDistanceTravelled).reversed());
    }

    public History recordResults ()
    {
        History won = null;
        for (int i = 0; i < getHorseNo(); i++)
        {
            if (Horses.get(i) != null)
            {
                long time = getEndTime() - getStartTime();
                History history = new History
                (
                        this,
                        raceTrack,
                        Horses.get(i),
                        LocalDateTime.now(),
                        i+1,
                        time
                );

                if (i == 0 || raceWonBy(Horses.get(i)))
                {
                    won = history;
                }
            }
        }
        return won;
    }
}
