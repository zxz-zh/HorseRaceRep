/*
Menu always remains
Main panel displays instance of a horse
Output area always remains

 */

import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;


public class DisplayRace extends JFrame
{
    private Race thisRace;
    private ArrayList<Horse> Horses = new ArrayList<>();
    private Track thisTrack;
    private History history;
    private JTextArea outputArea;
    private JPanel raceTrack;

    public DisplayRace ()
    {
        setTitle("Horse Race Simulator ");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); //closes program if you press 'X'
        setLayout(new BorderLayout());

        // Initialize with default track first
        thisTrack = new Track("Straight", "Dry", 100, 3);  // Default values
        raceTrack = new JPanel();  // Initialize raceTrack panel
        thisRace = null;
        history = null;

        createMenuBar();
        createControlPanel();
        createRaceTrack();
        createOutputArea();
        createStatsArea();

        pack(); //resizes window
        setVisible(true); //displays window
    }

    public static void main(String[] args)
    {
        SwingUtilities.invokeLater(() -> new DisplayRace());
    }

    //horizontal container holding menu items
    public void createMenuBar ()
    {
        JMenuBar menu = new JMenuBar();
        JMenuItem exitItem = new JMenuItem("Exit");

        //terminate upon click
        exitItem.addActionListener(e -> System.exit(0));

        menu.add(exitItem);
        setJMenuBar(menu);
    }


    public void createControlPanel ()
    {
        JPanel controls = new JPanel(new GridLayout(1, 3));

        //Section 1 - addHorse
        JPanel horsePanel = new JPanel(new BorderLayout());
        horsePanel.setBorder(BorderFactory.createTitledBorder("Horse"));

        JButton horseButton = new JButton("Add +");
        horseButton.addActionListener(e -> showAddHorseDialogue());
        horsePanel.add(horseButton, BorderLayout.CENTER);

        //Section 2 - trackConditions
        JPanel trackPanel = new JPanel(new BorderLayout());
        trackPanel.setBorder(BorderFactory.createTitledBorder("Track"));

        JButton trackButton = new JButton("Customise");
        trackButton.addActionListener(e -> showTrackDialogue());
        trackPanel.add(trackButton, BorderLayout.CENTER);

        //Section 3 - race
        JPanel racePanel = new JPanel(new BorderLayout());
        racePanel.setBorder(BorderFactory.createTitledBorder("Race"));

        JButton startButton = new JButton("Start");
        startButton.addActionListener(e ->
        {
            startNewRace();
        });

        racePanel.add(startButton, BorderLayout.CENTER);

        controls.add(horsePanel);
        controls.add(trackPanel);
        controls.add(racePanel);

        //attach controls to top (header)
        add(controls, BorderLayout.NORTH);

    }

    public void createStatsArea ()
    {
        JTabbedPane statistics = new JTabbedPane();
        JPanel horseStats = new JPanel(new GridLayout(0, 1));
        JPanel raceStats = new JPanel(new GridLayout(0, 1));

        //create dropdown menu of horses
        JComboBox <String> horseSelection = new JComboBox<>();
        if (thisRace != null)
        {
            for (Horse h: thisRace.getHorses())
            {
                if (h != null)
                {
                    horseSelection.addItem(h.getName());
                }
            }
        }

        horseStats.add(new JLabel("Horse: "));
        horseStats.add(horseSelection);
        JLabel speed = new JLabel("Average Speed: ");
        horseStats.add(speed);

        //calculate and output average speed
        if (thisRace != null)
        {
            JButton averageButton = new JButton("Calculate");

            averageButton.addActionListener(e ->
            {
                Horse thisHorse = (Horse) horseSelection.getSelectedItem();
                if (thisHorse != null)
                {
                    double average = thisHorse.getDistanceTravelled() / (thisTrack.getLength() * 0.1);
                    speed.setText("Average Speed: " + average);
                }
            });

            horseStats.add(averageButton);
        }

        //calculate and output records
        raceStats.add(new JLabel("Best Track Record: "));
        history.getBest();
        statistics.addTab("Horse Statistics ", horseStats);
        statistics.addTab("Race Statistics ", raceStats);

        add(statistics, BorderLayout.EAST);
    }

    public void createRaceTrack ()
    {
        if (raceTrack == null) {
            raceTrack = new JPanel();
        }
        raceTrack.removeAll();

        if (thisTrack == null) {
            return;  // Safety check
        }

        raceTrack.setLayout(new GridLayout(thisTrack.getLanes(), 1));

        for (int i = 0; i < thisTrack.getLanes(); i++)
        {
            JPanel lane = new JPanel(new BorderLayout());
            if (thisTrack.getCondition().equals("muddy"))
            {
                lane.setBackground(Color.getHSBColor(139, 69, 19));
            }
            else if (thisTrack.getCondition().equals("icy"))
            {
                lane.setBackground(Color.getHSBColor(220, 240, 255));
            }
            else
            {
                lane.setBackground(Color.getHSBColor(144, 238, 144));
            }
            JLabel laneLabel = new JLabel("Lane " + (i + 1), SwingConstants.CENTER);
            lane.add(laneLabel, BorderLayout.CENTER);
            raceTrack.add(lane);
        }

        //attach track to middle of screen
        add(raceTrack, BorderLayout.CENTER);

        Border trackBorder;
        if (thisTrack.getShape().equalsIgnoreCase("Oval"))
        {
            trackBorder = BorderFactory.createCompoundBorder
            (
                BorderFactory.createLineBorder(Color.BLUE, 3),
                BorderFactory.createEmptyBorder(50, 50, 50, 50)
            );
        }
        else if (thisTrack.getShape().equalsIgnoreCase("Figure-Eight"))
        {
            trackBorder = BorderFactory.createCompoundBorder
            (
                    BorderFactory.createMatteBorder(30, 5, 30, 5, Color.BLUE),
                    BorderFactory.createMatteBorder(5, 30, 5, 30, Color.BLUE)
            );
        }
        else if (thisTrack.getShape().equalsIgnoreCase("Zigzag"))
        {
            trackBorder = BorderFactory.createCompoundBorder
            (
                    BorderFactory.createMatteBorder(30, 5, 30, 5, Color.BLUE),
                    BorderFactory.createMatteBorder(20, 20, 20, 20, Color.BLUE)
            );
        }
        else
        {
            // Straight
            trackBorder = BorderFactory.createMatteBorder(0, 0, 3, 0, Color.BLUE);
        }

        raceTrack.setBorder(trackBorder);
        raceTrack.revalidate();
        raceTrack.repaint();
        return;
    }

    //create output-only text area
    public void createOutputArea ()
    {
        outputArea = new JTextArea(5, 20);
        outputArea.setEditable(false);
        JScrollPane scrollBar = new JScrollPane(outputArea);

        //attack output to bottom (footer)
        add(scrollBar, BorderLayout.SOUTH);
    }

    public void showTrackDialogue ()
    {
        JDialog dialog = new JDialog (this, "Customise Track", true);
        dialog.setLayout(new GridLayout(5, 2));

        JSpinner lengthField = new JSpinner(new SpinnerNumberModel(100, 50, 150, 10));
        JComboBox<String> conditionField = new JComboBox<>(new String[]{"Dry", "Muddy", "Icy"});
        JComboBox<String> shapeField = new JComboBox<>(new String[]{"Oval", "Figure-Eight", "Straight", "Zigzag"});
        JSpinner lanesField = new JSpinner(new SpinnerNumberModel(3, 2, 6, 1));
        JButton setButton = new JButton("Set Track");

        dialog.add(new JLabel("Length: "));
        dialog.add(lengthField);
        dialog.add(new JLabel("Condition: "));
        dialog.add(conditionField);
        dialog.add(new JLabel("Shape: "));
        dialog.add(shapeField);
        dialog.add(new JLabel("Lanes: "));
        dialog.add(lanesField);
        dialog.add(new JLabel(" "));
        dialog.add(setButton);

        setButton.addActionListener(e ->
        {
            // Get input values
            String shape = (String) shapeField.getSelectedItem();
            String condition = (String) conditionField.getSelectedItem();
            int length = (int) lengthField.getValue();
            int lanes = (int) lanesField.getValue();

            thisTrack = new Track(shape, condition, length, lanes);

            // Print success
            outputArea.append("Set Track: Shape - " + shape + ", Terrain - " + condition + " \n");
            dialog.dispose(); // Close window
        });

        dialog.pack();
        dialog.setLocationRelativeTo(this);  // Center over main window
        dialog.setVisible(true);
    }

    public void showAddHorseDialogue ()
    {
        //must complete/cancel(modal) box attached to main panel
        JDialog dialog = new JDialog (this, "Add New Horse", true);
        dialog.setLayout(new GridLayout(7, 2));

        JTextField nameField = new JTextField();
        JTextField symbolField = new JTextField(1);
        JSlider confidenceField = new JSlider(0, 100, 50);
        JComboBox<String> breedField = new JComboBox<>(new String[]{"Thoroughbred", "Arabian", "Quarter Horse"});
        JComboBox<String> colorField = new JComboBox<>(new String[]{"Brown", "Black", "White", "Grey", "Ruby"});
        JComboBox<String> equipmentField = new JComboBox<>(new String[]{"Standard", "Lightweight", "Heavy"});
        JButton addButton = new JButton("Add Horse");

        dialog.add(new JLabel("Name: "));
        dialog.add(nameField);
        dialog.add(new JLabel("Symbol: "));
        dialog.add(symbolField);
        dialog.add(new JLabel("Confidence: "));
        dialog.add(confidenceField);
        dialog.add(new JLabel("Breed: "));
        dialog.add(breedField);
        dialog.add(new JLabel("Coat Color:"));
        dialog.add(colorField);
        dialog.add(new JLabel("Equipment:"));
        dialog.add(equipmentField);
        dialog.add(new JLabel(" "));
        dialog.add(addButton);

        addButton.addActionListener(e ->
        {
            // Get input values
            String name = nameField.getText();
            char symbol = symbolField.getText().isEmpty() ? 'H' : symbolField.getText().charAt(0);
            double confidence = confidenceField.getValue() / 100.0; // Convert to decimal
            String breed = (String) breedField.getSelectedItem();
            String color = (String) colorField.getSelectedItem();
            String equipment = (String) equipmentField.getSelectedItem();

            // Create new horse
            Horse horse = new Horse(symbol, name, confidence, breed, color, equipment);
            Horses.add(horse);

            // Print success
            outputArea.append("Added Horse: " + name + "\n");
            dialog.dispose(); // Close window
        });

        dialog.pack();
        dialog.setLocationRelativeTo(this);  // Center over main window
        dialog.setVisible(true);
    }

    public void startNewRace ()
    {
        if (Horses.size() < 2)
        {
            JOptionPane.showMessageDialog(this, "Please select at least two horses to start a race.");
            return;
        }
        else if (Horses.size() > thisTrack.getLanes())
        {
            JOptionPane.showMessageDialog(this, "Maximum lanes exceeded.");
            return;
        }
        if (thisTrack == null)
        {
            JOptionPane.showMessageDialog(this, "Please select a track to start a race.");
            return;
        }

        createRaceTrack();
        thisRace = new Race (thisTrack);

        thisTrack.setWeatherEffects(Horses);
        thisTrack.setShapeEffects(Horses);

        //add horses to track/lanes
        for (int i = 0; i < Horses.size(); i++)
        {
            Horse h = Horses.get(i);
            try
            {
                thisRace.addHorse(h, i + 1);
            }
            catch (IllegalArgumentException e)
            {
                JOptionPane.showMessageDialog(this, e.getMessage());
                return;
            }
        }

        outputArea.append("Starting race...  " + Horses.size() + " Horses On Track\n");
        outputArea.append("Track Length: " + thisTrack.getLength() + "m; Condition: " + thisTrack.getCondition() + "\n");
        updateRaceDisplay();
    }

    public void updateRaceDisplay ()
    {
        raceTrack.removeAll();
        raceTrack.setLayout(new GridLayout(thisTrack.getLanes(), 1));

        for (int i = 0; i < thisRace.getHorses().size(); i++)
        {
            Horse h = thisRace.getHorses().get(i);
            JPanel lane =(JPanel) raceTrack.getComponent(i);

            if (h != null)
            {
                JProgressBar progress = (JProgressBar) lane.getComponent(1);
                progress.setValue(h.getDistanceTravelled());
                progress.setString(h.getName().toUpperCase() + " (Confidence: " + h.getSymbol() + ") ");
                progress.setStringPainted(true);

                JLabel symbol = new JLabel(String.valueOf(h.getSymbol()));
                positionHorse(symbol, h.getDistanceTravelled());

                //clear previous
                lane.remove(2);
                lane.add(symbol);

                if (h.hasFallen()) {
                    progress.setForeground(Color.RED);
                    progress.setString(progress.getString() + " [FALLEN]");
                }

                lane.add(progress, BorderLayout.CENTER);
            }

                //refresh screen
                raceTrack.revalidate();
                raceTrack.repaint();
        }


        //create a timer that is triggered every 100ms
        new Timer(100, new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                if (thisRace.isFinished())
                {
                    ((Timer) e.getSource()).stop();
                    outputRaceResults();
                    return;
                }
                for (Horse h: Horses)
                {
                    if (h != null && ! h.hasFallen())
                    {
                        thisRace.moveHorse(h);
                    }
                }
                updateRaceDisplay();
            }
        }).start();
    }

    public void positionHorse (JLabel symbol, int distance)
    {
        int x = 0;
        int y = 0;
        if (thisTrack.getShape().equalsIgnoreCase("oval"))
        {
            int trackLength = thisTrack.getLength();
            double progress = (double)distance / trackLength;

            // Oval path approximation
            x = (int)(100 + 80 * Math.cos(progress * 2 * Math.PI));
            y = (int)(100 + 50 * Math.sin(progress * 2 * Math.PI));

        }
        else if (thisTrack.getShape().equalsIgnoreCase("figure-eight"))
        {
            int trackLength = thisTrack.getLength();
            double progress = (double)distance / trackLength;

            x = (int)(100 + 100 * Math.sin(progress * 4 * Math.PI));
            y = (int)(100 + 50 * Math.sin(progress * 2 * Math.PI));
        }
        else if (thisTrack.getShape().equalsIgnoreCase("zigzag"))
        {
            int segmentLength = thisTrack.getLength() / 4;
            int segment = distance / segmentLength;
            int posInSegment = distance % segmentLength;

            x = 50 + posInSegment;
            y = 50 + (segment % 2 == 0 ? 0 : 30);
        }
        else
        {
            x = 50 + distance;
            y = 50;
        }

        symbol.setLocation(x,y);
    }

    public void outputRaceResults()
    {
        int fallenNo = 0;

        for (Horse h : thisRace.getHorses())
        {
            if (h != null && h.hasFallen())
            {
                ++fallenNo;
            }
            if (h != null && thisRace.raceWonBy(h))
            {
                thisRace.setWinners(h.getName().toUpperCase());
            }
        }

         history = thisRace.recordResults();

        if (history != null)
        {
            outputArea.append("Race Statistics: \n");
            outputArea.append("Winner: " + history.getHorse().getName() + "\n");
            outputArea.append("Winning Time: " + history.getTime() + " ns\n");
            outputArea.append("Track Condition: " + history.getTrack().getCondition() + "\n");

        }

        if (thisRace.getWinners().size() == 1)
        {
            outputArea.append("And the winner is... " + thisRace.getWinners().get(0) + "!\n");
        }
        else if (!thisRace.getWinners().isEmpty())
        {
            outputArea.append ("It's a tie between: " + String.join(", ", thisRace.getWinners()) + "!\n");
        }
        else if (fallenNo == thisRace.getHorses().size())
        {
            outputArea.append ("All horses fell! There is no winner.\n");
        }

        if (!History.getTimes().isEmpty())
        {
            History bestHistory = Collections.min(History.getTimes(),
                    Comparator.comparingLong(History::getTime));
            outputArea.append("\nHistorical Best:\n");
            outputArea.append("Horse: " + bestHistory.getHorse().getName() + "\n");
            outputArea.append("Time: " + bestHistory.getTime() + " ns\n");
            outputArea.append("Date: " + bestHistory.getDate() + "\n");
        }

        outputArea.append("----------------------------\n");
    }
}
