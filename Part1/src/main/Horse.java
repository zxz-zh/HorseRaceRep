
/*
 * Decompiled with CFR 0.152.
 */
import java.util.Scanner;

public class Horse {
    private String name;
    private char symbol;
    private int distance;
    private Boolean fallen = false;
    private double confidence;

    public Horse(char horseSymbol, String horseName, double horseConfidence) {
        while (horseSymbol == ' ') {
            Scanner scanner = new Scanner(System.in);
            System.out.print("Cannot enter white space. Please re-enter: ");
            horseSymbol = scanner.next().charAt(0);
        }
        this.symbol = horseSymbol;
        this.name = horseName;
        this.confidence = horseConfidence;
    }

    public void fall() {
        this.fallen = true;
        if (this.getConfidence() > 0.0) {
            this.setConfidence(this.getConfidence() - 0.1);
        }
    }

    public double getConfidence() {
        return this.confidence;
    }

    public int getDistanceTravelled() {
        return this.distance;
    }

    public String getName() {
        return this.name;
    }

    public char getSymbol() {
        return this.symbol;
    }

    public void goBackToStart() {
        this.distance = 0;
        this.fallen = false;
    }

    public boolean hasFallen() {
        return this.fallen;
    }

    public void moveForward() {
        ++this.distance;
    }

    public void setConfidence(double newConfidence) {
        if (this.confidence == 0.0 && newConfidence < 0.0 || this.confidence == 1.0 && newConfidence > 1.0) {
            return;
        }
        while (newConfidence < 0.0 || newConfidence > 1.0) {
            Scanner scanner = new Scanner(System.in);
            System.out.println("Invalid confidence. Please re-enter: ");
            newConfidence = Double.parseDouble(scanner.nextLine());
        }
        this.confidence = newConfidence;
    }

    public void setSymbol(char newSymbol) {
        this.symbol = newSymbol;
    }
}
