import Simulation.Simulation;

/**
 * Created by Jay on 5/6/2017.
 */
public class main {
    public static void main(String[] args) {
        Simulation sim = new Simulation(4, 60, 3600, 60,
                300, 150, 30); //I chose these numbers random must change.
        //add print statements here
        sim.run();
        System.out.println("Wait times: " + sim.getWaitTimes());
        //System.out.println("Average Wait Time: " + sim.getAverageWaitTime());
        System.out.println("Time Cashier Waiting " + sim.getTimeCashierWaiting());
        System.out.println("Time Customer Waiting " + sim.getTimeSomeoneWaiting());
        System.out.println("Cumulative Customer Wait " + sim.getCumulativeWaitTime());
        System.out.println("Percent Time Backed Up " + sim.getPercentTimeBackedUp());
    }
}
