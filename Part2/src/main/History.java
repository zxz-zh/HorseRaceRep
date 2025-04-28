import java.lang.reflect.Field;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;

public class History
{
    private Race race;
    private final Track track;
    private final Horse horse;
    private final LocalDateTime date;
    private final int position;
    private final double time;
    private double speed;
    private final boolean fell;

    private ArrayList <Track> tracks = new ArrayList <>();
    private ArrayList <Horse> horses = new ArrayList <>();
    private static ArrayList <History> times = new ArrayList <>();

    public History (Race r, Track t, Horse h, LocalDateTime Date, int Position, long Time)
    {
        race = r;
        date = Date;
        position = Position;
        speed = h.getDistanceTravelled()/(Time/60.0);
        time = Time/1_000_000_000.0;
        speed = h.getDistanceTravelled()/(t.getLength() * 0.1);
        fell = h.hasFallen();

        int getIndex = isAt(tracks, t, "shape", "condition", "length", "lanes");
        if (getIndex == -1)
        {
            tracks.add(t);
            track = tracks.get(tracks.size()-1);
        }
        else
        {
            track = tracks.get(getIndex);
        }

        getIndex = isAt(horses, h, "name", "symbol", "distance", "fallen", "confidence", "breed", "coatColor", "equipment");
        if (getIndex == -1)
        {
            horses.add(h);
            horse = horses.get(horses.size()-1);
        }
        else
        {
            horse = horses.get(getIndex);
        }

        times.add(this);


    }

    public <T> int isAt (ArrayList<T> checkAgainst, T find, String ... fieldNames)
    {
        boolean found = false;
        int count = 0;
        while (!found && count < checkAgainst.size())
        {
            try
            {
                found = true;
                for (String fieldName : fieldNames)
                {
                    Field field = checkAgainst.get(count).getClass().getDeclaredField(fieldName);
                    field.setAccessible(true);
                    if (!field.get(checkAgainst.get(count)).equals(field.get(find)))
                    {
                        found = false;
                        break;
                    }
                }
            }
            catch (Exception e)
            {
                System.err.println("Warning: Field comparison failed");
            }

            if (found)
            {
                return count;
            }
            else
            {
                count++;
            }
        }
        return -1;
    }

    public double getTime ()
    {
        return time;
    }

    public Horse getHorse ()
    {
        return horse;
    }

    public Track getTrack ()
    {
        return track;
    }

    public LocalDateTime getDate ()
    {
        return date;
    }

    public static ArrayList <History> getTimes()
    {
        return times;
    }

    public String getBest ()
    {
        if (times != null)
        {
            times.sort(Comparator.comparingDouble(History :: getTime));
            return (times.get(0).getHorse().getName() + "\n Time: " + times.get(0).getTime() +
                    "\n Track Condition: " + times.get(0).getTrack().getCondition() +
                    "\n Track Shape: " + times.get(0).getTrack().getShape() +
                    "\n Track Length: " + times.get(0).getTrack().getLength() +
                    "\n Horse Breed: " + times.get(0).getHorse().getBreed() +
                    "\n Coat Color: " + times.get(0).getHorse().getCoatColor() +
                    "\n Equipment: " + times.get(0).getHorse().getEquipment());
        }
        return "No Races Yet";
    }

    public double getSpeed() {
        return speed;
    }

    public Race getRace() {
        return race;
    }

    public boolean isFell() {
        return fell;
    }
}