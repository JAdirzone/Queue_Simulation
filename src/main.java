import Simulation.Simulation;

import java.util.ArrayList;

/**
 * Created by Jay on 5/6/2017.
 */
public class main {
    public static void main(String[] args) {
        Simulation sim = new Simulation(4, 60, 3600, 10,
                100, 1, 1); //I chose these numbers random must change.
        //add print statements here
        sim.run();
        System.out.println("Wait times: " + sim.getWaitTimes());
        //System.out.println("Average Wait Time: " + sim.getAverageWaitTime());
        System.out.println("Time Cashier Waiting " + sim.getTimeCashierWaiting());
        System.out.println("Time Customer Waiting " + sim.getTimeSomeoneWaiting());
        System.out.println("Cumulative Customer Wait " + sim.getCumulativeWaitTime());
        System.out.println("Percent Time Backed Up " + sim.getPercentTimeBackedUp());

        ArrayList<Integer> testResults = new ArrayList<>();
        for(int i = 0; i < 100; i++){
            testResults.add(sim.generateServiceTime());
        }
        //Create file from testResults here.

    }
}
