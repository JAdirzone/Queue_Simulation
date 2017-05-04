package Simulation;

import Line.Line;
import Line.Customer;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Jay on 5/3/2017.
 */
public class Simulation {
    private int totalTime;
    private int currentSecond;
    private List<Event> eventList;
    private List<Line> lines;

    private int timeSomeoneWaiting;
    private int timeCashierWaiting;
    private int cumulativeWaitTime;
    private int timeBackedUp;

    private List<Integer> waitTimes;

    public Simulation(int numLines, int numArrivals, int latestPossArrival){
        //this.totalTime = totalTime;
        this.currentSecond = 0;
        this.eventList = new ArrayList<>();
        this.lines = new ArrayList<>();

        this.timeSomeoneWaiting = 0;
        this.cumulativeWaitTime = 0;
        this.timeBackedUp = 0;

        this.waitTimes = new ArrayList<>();

        ArrayList<Integer> arrivalTimes = generateArrivalTimes(numArrivals, latestPossArrival);
        sort(arrivalTimes);
        for(int arrivalTime : arrivalTimes){
            eventList.add(new Event(arrivalTime, 0));
        }
        for(int i = 0; i < numLines; i++){
            lines.add(new Line());
        }

    }

    public boolean finished(){
        return eventList.isEmpty();
    }

    public void processNextEvent(){
        Event nextEvent = eventList.get(0);
        //Record analysis here
        for(Line line : lines){
            if(line.isCustWaiting()) {
                timeSomeoneWaiting += nextEvent.getTime() - currentSecond;
            }
        }
        for(Line line : lines){
            if(line.isCashierWaiting()){
                timeCashierWaiting += nextEvent.getTime() - currentSecond;
            }
        }
        for(Line line : lines){
            if(line.size() >= 2){
                cumulativeWaitTime += (nextEvent.getTime() - currentSecond) * (line.size() - 1);
            }
        }
        for(Line line : lines){
            if(line.isBackedUp()){
                timeBackedUp += nextEvent.getTime() - currentSecond;
            }
        }


        currentSecond = nextEvent.getTime();
        if(nextEvent.getType() == 0){//event is an arrival
            int targetLine = pickLine();
            lines.get(targetLine).putInLine(new Customer(currentSecond, generateServiceTime()));
            if(lines.get(targetLine).size() == 1){//The line was empty.
                newFront(targetLine);
            }
        }else{//event is a customer being finished
            Customer finishedCustomer = lines.get(nextEvent.getType() - 1).poll();
            waitTimes.add(currentSecond - finishedCustomer.getEnterTime() - finishedCustomer.getServiceTime());
            if(!lines.get(nextEvent.getType() - 1).isCashierWaiting()){//There was someone waiting
                newFront(nextEvent.getType());
            }
        }
        eventList.remove(0);
    }

    /**
     * Adds an event to the eventlist for the current front of a line being finished.
     * Should be used when a new person becomes the front of a line.
     * Notice the lineNum starts at 1.
     * @param lineNum
     */
    private void newFront(int lineNum){
        insertEvent(new Event(lines.get(lineNum - 1).peek().getServiceTime() + currentSecond, lineNum));
    }

    private ArrayList<Integer> generateArrivalTimes(int numArrivals, int latestPossArrival){
        ArrayList<Integer> result = new ArrayList<Integer>(numArrivals);
        for(int i = 0; i < numArrivals; i++){
            generateArrivalTime(latestPossArrival);
        }
        return result;
    }

    //TODO
    private int generateArrivalTime(int latestPossArrival){
        return 1;
    }

    //TODO
    private int generateServiceTime(){
        return 50;
    }

    //TODO
    private int pickLine(){
        return 1;
    }

    //TODO
    private void sort(ArrayList<Integer> list){

    }

    //TODO
    private void insertEvent(Event event){

    }

    public int getTimeSomeoneWaiting(){
        return timeSomeoneWaiting;
    }

    public int getTimeCashierWaiting() {
        return timeCashierWaiting;
    }

    public int getTimeBackedUp() {
        return timeBackedUp;
    }

    public int getPercentCustWaiting(){
        return timeSomeoneWaiting / (totalTime * lines.size());
    }

    public int getCashierWaiting(){
        return timeCashierWaiting / (totalTime * lines.size());
    }

    public int getCumulativeWaitTime(){
        return cumulativeWaitTime;
    }

    public int getPercentTimeBackedUp(){
        return timeBackedUp / (totalTime + lines.size());
    }

    public int getAverageWaitTime(){
        int result = 0;
        for(int waitTime : waitTimes){
            result += waitTime;
        }
        return result / waitTimes.size();
    }

}
