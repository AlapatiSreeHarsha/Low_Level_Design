//Create small, specific interfaces instead of one big interface.

interface Rider{
    void bookRide();
    void cancelRide();
    void trackRide();
    void payForRide();
}

interface Driver{
    void acceptRide();
    void startRide();
    void endRide();
    void updateLocation();
}

class RiderApp implements Rider{
    public void bookRide(){
        System.out.println("Booking ride");
    }

    public void cancelRide(){
        System.out.println("Cancelling ride");
    }

    public void trackRide(){
        System.out.println("Tracking ride");
    }

    public void payForRide(){
        System.out.println("Paying for ride");
    }
}

class DriverApp implements Driver{
    public void acceptRide(){
        System.out.println("Ride accepted");
    }

    public void startRide(){
        System.out.println("Ride started");
    }

    public void endRide(){
        System.out.println("Ride ended");
    }

    public void updateLocation(){
        System.out.println("Location updated");
    }
}

public class ISP{
    public static void main(String[] args){
        Rider rider=new RiderApp();
        Driver driver=new DriverApp();

        rider.bookRide();
        rider.trackRide();

        driver.acceptRide();
        driver.startRide();
    }
}