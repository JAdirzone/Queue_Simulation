import Simulation.Simulation;


import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;


public class main {
    public static void main(String[] args) throws IOException {
        Simulation sim = new Simulation(8, 350, 3600, 20,
                400, 2.1, -1, .9);
        //add print statements here
        sim.run();
        //System.out.println("Wait times: " + sim.getWaitTimes());
        System.out.println("Average Wait Time: " + sim.getAverageWaitTime());
        System.out.println("Time Cashier Waiting " + sim.getTimeCashierWaiting());
        System.out.println("Percent Time Cashier Waiting " + sim.getPercentTimeCashierWaiting());
        System.out.println("Time Customer Waiting " + sim.getTimeSomeoneWaiting());
        System.out.println("Percent Time Customer Waiting " + sim.getPercentTimeCustomWaiting());
        System.out.println("Cumulative Customer Wait " + sim.getCumulativeWaitTime());
        System.out.println("Percent Time Backed Up " + sim.getPercentTimeBackedUp()); //TODO check
        System.out.println("Number of Customers Finished " + sim.getNumCustomFinished());

        ArrayList<Integer> arraytemp = new ArrayList<>();
        //System.getProperty("user.name");
        //PrintWriter writer = new PrintWriter("results.txt", "abc");



        //try {
            for (int i = 0; i < 500; i++) {
                arraytemp.add(sim.generateServiceTime());
                //writer.write(sim.generateServiceTime());
            }
            System.out.println(arraytemp);
            //}finally {
           // writer.close();
        //}

        //System.out.println(3.0 / 2);




    }
}
