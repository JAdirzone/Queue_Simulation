package Simulation;

import Line.Line;
import Line.Customer;

import java.util.ArrayList;
import java.util.List;

import java.util.Random;

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

    private Random rand;

    private double serveMin;
    private double serveMax;
    private double serveSkew;
    private double serveBias;

    private double cashierMod;

    public Simulation(int numLines, int numArrivals, int latestPossArrival,
                      int serveMin, int serveMax, double serveSkew, double serveBias, double cashierMod){
        this.totalTime = totalTime;
        this.currentSecond = 0;
        this.eventList = new ArrayList<>();
        this.lines = new ArrayList<>();
        this.rand = new Random();

        this.timeSomeoneWaiting = 0;
        this.cumulativeWaitTime = 0;
        this.timeBackedUp = 0;

        this.serveMin = (double)serveMin;
        this.serveMax = (double)serveMax;
        this.serveSkew = (double)serveSkew;
        this.serveBias = (double)serveBias;

        this.cashierMod = cashierMod;

        this.waitTimes = new ArrayList<>();

        ArrayList<Integer> arrivalTimes = generateArrivalTimes(numArrivals, latestPossArrival);
        sort(arrivalTimes);
        for(int arrivalTime : arrivalTimes){
            eventList.add(new Event(arrivalTime, 0));
        }
        eventList.add(new Event(latestPossArrival, -1));//Terminating event
        for(int i = 0; i < numLines; i++){
            lines.add(new Line());
        }

    }

    //public boolean finished(){
    //    return eventList.isEmpty();
    //}

    public boolean processNextEvent(){
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
        if(nextEvent.getType() == -1){//event is the end of the simulation.
            return false;
        }
        if(nextEvent.getType() == 0){//event is an arrival
            int targetLine = pickLine();
            lines.get(targetLine - 1).putInLine(new Customer(currentSecond, (int)(generateServiceTime() * cashierMod)));
            if(lines.get(targetLine - 1).size() == 1){//The line was empty.
                newFront(targetLine);
            }
        }else{//event is a customer being finished
            //System.out.println(lines.get(nextEvent.getType() - 1));
            Customer finishedCustomer = lines.get(nextEvent.getType() - 1).poll();
            //System.out.println(finishedCustomer.getServiceTime());
            waitTimes.add(currentSecond - finishedCustomer.getEnterTime() - finishedCustomer.getServiceTime());
            if(!lines.get(nextEvent.getType() - 1).isCashierWaiting()){//There was someone waiting
                newFront(nextEvent.getType());
            }
        }
        eventList.remove(0); //This is the problem. When a customer is add to this position than remove immediately
        return true;
    }

    /**
     * Adds an event to the eventlist for the current front of a line being finished.
     * Should be used when a new person becomes the front of a line.
     * Notice the lineNum starts at 1.
     * @param lineNum
     */
    private void newFront(int lineNum){
        //insertEvent(new Event(lines.get(lineNum - 1).peek().getServiceTime() + currentSecond, lineNum));
        Line tarLine = lines.get(lineNum - 1);
        Customer headCust = tarLine.peek();
        int tarTime = headCust.getServiceTime() + currentSecond; //This may have been the fix!
        Event event = new Event(tarTime, lineNum);
        insertEvent(event);
    }

    private ArrayList<Integer> generateArrivalTimes(int numArrivals, int latestPossArrival){
        ArrayList<Integer> result = new ArrayList<Integer>(numArrivals);
        for(int i = 0; i < numArrivals; i++){
            result.add(generateArrivalTime(latestPossArrival));
        }
        return result;
    }

    //Generating a number within 1 and latestPossArrival
    private int generateArrivalTime(int latestPossArrival){
        int n = rand.nextInt(latestPossArrival);
        return n;
    }

    //Generating service time
    public int generateServiceTime(){ //Made public to aid debugging.
        double range = serveMax - serveMin;
        double mid = serveMin + range / 2.0;
        double unit = rand.nextGaussian();
        double biasFactor = Math.exp(serveBias);
        double retval = mid +(range*(biasFactor/(biasFactor+Math.exp(-unit/serveSkew))-0.5));
        return (int) retval;
    }

    // Checking which line has lowest number of people
    private int pickLine(){
        int minPerson = lines.get(0).size(); // get number of people in  line 0.
        int lineNumber = 0;

        for(int i = 0; i < lines.size(); i++){  // start from line 1 (the second line).
            int numPerson = lines.get(i).size();
            if(numPerson < minPerson)
            {
                minPerson = numPerson;
                lineNumber = i;
            }
        }

        return lineNumber + 1; //The lines aren't number by their array index. Line 1 is lines[0].
    }

    // Sorting the list in order from low to high
    private void sort(ArrayList<Integer> list){

        /**
        public static int[] doInsertionSort(int[] input){
            int temp;
            for (int i = 1; i < input.length; i++) {
                for(int j = i ; j > 0 ; j--){
                    if(input[j] < input[j-1]){
                        temp = input[j];
                        input[j] = input[j-1];
                        input[j-1] = temp;
                    }
                }
            }
            return input;
        **/

        int temp;
        for (int i = 1; i < list.size(); i++) {
            for(int j = i ; j > 0 ; j--){
                if(list.get(j) < list.get(j-1)){
                    temp = list.get(j);
                    list.set(j, list.get(j-1));
                    list.set(j-1, temp);
                }
            }
        }
        //return list;

    }

    private void insertEvent(Event event){
        int insInd = getInsertionIndex(event.getTime()); //For debugging only.
        eventList.add(insInd, event);
    }

    private int getInsertionIndex(int eventTime){
        //System.out.println(getInsertionIndex(eventTime, 0, eventList.size() - 1));
        return getInsertionIndex(eventTime, 0, eventList.size() - 1);
    }

    //TODO Fix
    private int getInsertionIndex(int eventTime, int startIndex, int endIndex){
        //base cases
        if(startIndex == endIndex) {//The size of the part being looked at is one
            if(eventTime <= eventList.get(startIndex).getTime()){
                //System.out.println(startIndex);
                return startIndex;
            }
            //System.out.println(startIndex + 1);
            return startIndex + 1;
        }
        int halfSize = (endIndex - startIndex + 1) / 2;
        int secondCheck = startIndex + halfSize;
        int firstCheck = secondCheck - 1;
        if(eventTime >= eventList.get(firstCheck).getTime() && eventTime <= eventList.get(secondCheck).getTime()){
            //System.out.println(secondCheck);
            return secondCheck;
        }
        //recursive calls here
        if(eventTime < eventList.get(firstCheck).getTime()) {
            return getInsertionIndex(eventTime, startIndex, firstCheck);
        }
        return getInsertionIndex(eventTime, secondCheck, endIndex);
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

    public double getAverageWaitTime(){
        int result = 0;
        for(int waitTime : waitTimes){
            result += waitTime;
            System.out.println("TEST " + result);
            System.out.println("Test2 " + waitTimes.size());
        }
        return (double)result / waitTimes.size();
    }

    public List<Integer> getWaitTimes(){
        return waitTimes;
    }

    public void run(){
        while(processNextEvent()){
            //nothing needs to go here.
        }
    }
}
