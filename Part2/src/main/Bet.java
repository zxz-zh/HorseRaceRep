import java.util.ArrayList;

public class Bet {
    private final ArrayList<Horse> order;
    private final String bet;
    private double amount;
    private double payout;


    public Bet (Horse h, String bet, double amount)
    {
        //win
        this.amount = amount;
        this.bet = bet;
        this.order = new ArrayList<Horse>();
        this.order.add(h);
        //place
        if (this.bet.equals("Place"))
        {
            this.order.add(h);
            this.payout = 2.0;
        }
        //show
        else if (this.bet.equals("Show"))
        {
            this.order.add(h);
            this.order.add(h);
            this.payout = 1.7;
        }
        else
        {
            this.payout = 2.5;
        }
    }

    //exacta
    public Bet (Horse first, Horse second, double amount)
    {
        this.amount = amount;
        this.bet = "exacta";
        this.order = new ArrayList<>();
        this.order.add(first);
        this.order.add(second);
        this.payout = 3.4;
    }

    //trifecta
    public Bet (Horse first, Horse second, Horse third, double amount)
    {
        this.amount = amount;
        this.bet = "trifecta";
        this.order = new ArrayList<>();
        this.order.add(first);
        this.order.add(second);
        this.order.add(third);
        this.payout = 4.5;
    }

    public ArrayList<Horse> getOrder()
    {
        return this.order;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public double getPayout() {
        return payout;
    }

    public String getBet ()
    {
        return this.bet;
    }

    public void setPayout(double payout) {
        this.payout = payout;
    }

    public void applyOdds ()
    {
        double oddsConfidence = 1.0;
        double oddsFall = 1.0;
        double oddsDistance = 1.0;

        for (Horse h : this.order)
        {
            if (h.getModConfidence() > oddsConfidence)
            {
                oddsConfidence = h.getModConfidence();
            }
            if (h.getModFall() > oddsFall)
            {
                oddsFall = h.getModFall();
            }
            if (h.getModDistance() < oddsDistance)
            {
                oddsDistance = h.getModDistance();
            }

        }
        double modOdds =  oddsConfidence* oddsFall * (2-oddsDistance);
        payout *= modOdds;
    }

    public double Payout ()
    {
        return amount * payout;
    }
}