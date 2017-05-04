package Line;

/**
 * Created by Jay on 5/3/2017.
 */
public class Customer {
    private int enterTime;  //The second when this customer entered the line.
    private int serviceTime;//The number of seconds which this customer was at the register.

    public Customer(int enterTime, int serviceTime){
        this.enterTime = enterTime;
        this.serviceTime = serviceTime;
    }

    public int getEnterTime() {
        return enterTime;
    }

    public int getServiceTime() {
        return serviceTime;
    }
}
