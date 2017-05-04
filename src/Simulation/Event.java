package Simulation;

/**
 * Created by Jay on 5/3/2017.
 */
public class Event {
    private int time;   //The time this event will happen.

    private int type;   //The type of event this is.
                        //0 represents an arrival.
                        //Any number greater than 0 represents a customer being finished.
                        //The number corresponds to the line that has a finished customer.

    public Event(int time, int type){
        this.time = time;
        this.type = type;
    }

    public boolean isArrival(){
        return type == 0;
    }

    public int getTime() {
        return time;
    }

    public int getType() {
        return type;
    }
}
