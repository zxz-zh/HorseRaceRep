
/*
 * Decompiled with CFR 0.152.
 */
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;

public class Race {
    private int raceLength;
    private List<Horse> Horses;
    private int horseNo;

    public Race(int distance) {
        this.raceLength = distance;
        this.Horses = new ArrayList<Horse>();
    }

    public int getHorseNo() {
        return this.horseNo;
    }

    public void incrementHorseNo() {
        ++this.horseNo;
    }

    public void addHorse(Horse theHorse, int laneNumber) {
        while (laneNumber < 1) {
            Scanner scanner = new Scanner(System.in);
            System.out.println("Please enter a valid lane number: ");
            try {
                laneNumber = Integer.parseInt(scanner.nextLine());
            }
            catch (NumberFormatException e) {
                laneNumber = -1;
            }
            if (this.Horses.get(laneNumber - 1) == null) continue;
            laneNumber = -1;
        }
        while (this.Horses.size() < laneNumber) {
            this.Horses.add(null);
        }
        this.Horses.set(laneNumber - 1, theHorse);
        this.incrementHorseNo();
    }

    public void startRace() {
        if (this.getHorseNo() < 2) {
            System.out.println("Insufficient number of horses. Must have at least 2 horses to start.");
            return;
        }
        boolean finished = false;
        ArrayList<String> winners = new ArrayList<String>();
        for (Horse h : this.Horses) {
            if (h == null) continue;
            h.goBackToStart();
        }
        while (!finished) {
            int fallenNo = 0;
            for (Horse h : this.Horses) {
                if (h == null) continue;
                this.moveHorse(h);
            }
            this.printRace();
            for (Horse h : this.Horses) {
                if (h == null || !h.hasFallen()) continue;
                ++fallenNo;
            }
            if (fallenNo == this.getHorseNo()) {
                finished = true;
            }
            for (Horse h : this.Horses) {
                if (h == null || !this.raceWonBy(h)) continue;
                finished = true;
                h.setConfidence(h.getConfidence() + 0.1);
                winners.add(h.getName().toUpperCase());
            }
            if (winners.size() == 1) {
                System.out.println("And the winner is... " + (String)winners.get(0) + "! ");
            } else if (!winners.isEmpty()) {
                System.out.println("It's a tie between: " + String.join((CharSequence)", ", winners) + "! ");
            } else if (finished) {
                System.out.println("All horses fell! There is no winner. ");
            }
            try {
                TimeUnit.MILLISECONDS.sleep(100L);
            }
            catch (Exception exception) {}
        }
    }

    private void moveHorse(Horse theHorse) {
        if (theHorse == null || theHorse.hasFallen()) {
            return;
        }
        if (Math.random() < theHorse.getConfidence()) {
            theHorse.moveForward();
        }
        if (Math.random() < 0.1 * theHorse.getConfidence() * theHorse.getConfidence()) {
            theHorse.fall();
        }
    }

    private boolean raceWonBy(Horse theHorse) {
        return theHorse.getDistanceTravelled() == this.raceLength;
    }

    private void printRace() {
        System.out.print("\u001b[H\u001b[2J");
        this.multiplePrint('=', this.raceLength + 3);
        System.out.println();
        for (Horse h : this.Horses) {
            if (h == null) {
                this.printEmptyLane();
                System.out.println();
                continue;
            }
            this.printLane(h);
            System.out.println();
        }
        this.multiplePrint('=', this.raceLength + 3);
        System.out.println();
    }

    private void printEmptyLane() {
        System.out.print('|');
        this.multiplePrint(' ', this.raceLength + 1);
        System.out.print('|');
    }

    private void printLane(Horse theHorse) {
        int spacesBefore = theHorse.getDistanceTravelled();
        int spacesAfter = this.raceLength - theHorse.getDistanceTravelled();
        System.out.print('|');
        this.multiplePrint(' ', spacesBefore);
        if (theHorse.hasFallen()) {
            System.out.print('\u274c');
        } else {
            System.out.print(theHorse.getSymbol());
        }
        this.multiplePrint(' ', spacesAfter);
        System.out.print('|');
        System.out.print(theHorse.getName().toUpperCase() + " (Current confidence " + theHorse.getConfidence() + ") ");
    }

    private void multiplePrint(char aChar, int times) {
        for (int i = 0; i < times; ++i) {
            System.out.print(aChar);
        }
    }
}
