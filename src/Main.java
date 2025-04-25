import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        // 创建三匹赛马
        Horse horse1 = new Horse('A', "闪电", 0.7);
        //Horse horse2 = new Horse('B', "风暴", 0.5);
        Horse horse3 = new Horse('C', "流星", 0.6);

        // 创建比赛（赛道长度30单位）
        Race race = new Race(30);

        // 把马匹加入赛道
        race.addHorse(horse1, 1);
        //race.addHorse(horse2, 2);
        race.addHorse(horse3, 3);

        // 开始比赛！
        race.startRace();

    }
}

//ERROR 1
//      If all horses fall, the race never ends.
//      SOLUTION: add extra condition - while (! ( finished && Lane1Horse.hasFallen() && Lane2Horse.hasFallen() && Lane3Horse.hasFallen() ) )

//ERROR 2
//      Name & Confidence of horse for each lane is not shown.
//      SOLUTION: In printLane - After the '|' for the end of the track, System.out.print(theHorse.getName.toUpperCase() + " (Current confidence " + theHorse.getConfidence() + ") ")

//ERROR 3
//      When horse falls, symbol is ⌢, not ❌
//      SOLUTION: Change symbol in printLane

//ERROR 4
//      Code to clear terminal doesn't work on all terminals (like IntelliJ!)
//      SOLUTION: In printLane -change System.out.print('\u000C') to print \033[H\033[2J (ANSI escape code has better compatibility)

//ERROR 5
//      No solution for draw in race
//      SOLUTION: In startRace,

//ERROR 6!!!!!!
//      TimeUnit.MILLISECONDS.sleep(100) blocks main thread & pauses entire program during this time
//      SOLUTION: In startRace - use javax.swing.Timer for GUIs

//ERROR 7!!!!!!
//      Horses 2 & 3 still get a move after horse 1 wins, and horse 3 still gets a move if Horse 2 wins (startRace loops sequentially)
//      SOLUTION: In startRace - break in if branch when horse 1 or 2 wins, or change format

//ERROR 8
//      NullPointerException - some lanes can have no horse
//      SOLUTION: Only allow non-null horse objects to be passed to moveHorse()

//IMPROVEMENT 1
//      Add all horses participating into a list, with the index/key being the horse's lane number
//      EXPLANATION: Dynamic horse number and lane number

//IMPROVEMENT 2
//      Validate all addHorse e.g. prevent invalid lane number
//      EXPLANATION: More robust program

//IMPROVEMENT 3
//      Ensure all horses have unique symbols
//      EXPLANATION: Easier to spot different horses on racetrack
