package Line;

import java.util.*;
import java.util.LinkedList;
//import java.util.Queue;


public class Line {
    private static final int BACKUP = 5;
    private LinkedList<Customer> lineQueue;

    public Line(){
        this.lineQueue = new LinkedList<Customer>();
    }

    public Customer poll(){
        return lineQueue.poll();
    }

    public Customer peek(){
        return lineQueue.peek();
    }

    public void putInLine(Customer customer){
        lineQueue.add(customer);
    }

    public int size(){
        return lineQueue.size();
    }

    public int FirstCustSerTime(){
        return lineQueue.peek().getServiceTime();
    }

    public boolean isCustWaiting(){
        return lineQueue.size() >= 2;
    }

    public boolean isCashierWaiting(){
        return lineQueue.size() == 0;
    }

    public boolean isBackedUp(){
        return lineQueue.size() >= BACKUP;
    }
}
