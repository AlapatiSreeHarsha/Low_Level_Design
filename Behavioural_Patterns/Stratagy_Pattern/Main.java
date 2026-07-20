package Behavioural_Patterns.Stratagy_Pattern;

// Strategy Interface
interface MatchingStrategy {
    void match(String location);
}

// Concrete Strategy 1
class NearestDriverStrategy implements MatchingStrategy {

    @Override
    public void match(String location) {
        System.out.println("Matching nearest driver at " + location);
    }
}

// Concrete Strategy 2
class SurgePriorityStrategy implements MatchingStrategy {

    @Override
    public void match(String location) {
        System.out.println("Matching high-priority driver during surge at " + location);
    }
}

// Concrete Strategy 3
class AirportQueueStrategy implements MatchingStrategy {

    @Override
    public void match(String location) {
        System.out.println("Matching driver from airport queue at " + location);
    }
}

// Context
class MatchingService {
    private MatchingStrategy strategy;

    public MatchingService(MatchingStrategy strategy) {
        this.strategy = strategy;
    }

    public void setStrategy(MatchingStrategy strategy) {
        this.strategy = strategy;
    }

    public void matchRide(String location) {
        strategy.match(location);
    }
}

// Driver
public class Main {
    public static void main(String[] args) {

        MatchingService service =
                new MatchingService(new NearestDriverStrategy());

        service.matchRide("Hyderabad");

        service.setStrategy(new SurgePriorityStrategy());
        service.matchRide("Bangalore");

        service.setStrategy(new AirportQueueStrategy());
        service.matchRide("Delhi Airport");
    }
}