package Behavioural_Patterns.State_Pattern;


// State Interface
interface OrderState {

    void next(OrderContext context);

    void cancel(OrderContext context);

    String getStateName();
}

// Context
class OrderContext {

    private OrderState currentState;

    public OrderContext() {
        currentState = new OrderPlacedState();
    }

    public void setState(OrderState state) {
        currentState = state;
    }

    public void next() {
        currentState.next(this);
    }

    public void cancel() {
        currentState.cancel(this);
    }

    public String getState() {
        return currentState.getStateName();
    }
}

// Concrete State - Order Placed
class OrderPlacedState implements OrderState {

    @Override
    public void next(OrderContext context) {
        System.out.println("Order is being prepared.");
        context.setState(new PreparingState());
    }

    @Override
    public void cancel(OrderContext context) {
        System.out.println("Order has been cancelled.");
        context.setState(new CancelledState());
    }

    @Override
    public String getStateName() {
        return "Order Placed";
    }
}

// Concrete State - Preparing
class PreparingState implements OrderState {

    @Override
    public void next(OrderContext context) {
        System.out.println("Order is out for delivery.");
        context.setState(new OutForDeliveryState());
    }

    @Override
    public void cancel(OrderContext context) {
        System.out.println("Order has been cancelled.");
        context.setState(new CancelledState());
    }

    @Override
    public String getStateName() {
        return "Preparing";
    }
}

// Concrete State - Out For Delivery
class OutForDeliveryState implements OrderState {

    @Override
    public void next(OrderContext context) {
        System.out.println("Order delivered successfully.");
        context.setState(new DeliveredState());
    }

    @Override
    public void cancel(OrderContext context) {
        System.out.println("Cannot cancel. Order is out for delivery.");
    }

    @Override
    public String getStateName() {
        return "Out For Delivery";
    }
}

// Concrete State - Delivered
class DeliveredState implements OrderState {

    @Override
    public void next(OrderContext context) {
        System.out.println("Order is already delivered.");
    }

    @Override
    public void cancel(OrderContext context) {
        System.out.println("Delivered order cannot be cancelled.");
    }

    @Override
    public String getStateName() {
        return "Delivered";
    }
}

// Concrete State - Cancelled
class CancelledState implements OrderState {

    @Override
    public void next(OrderContext context) {
        System.out.println("Cancelled order cannot move to next state.");
    }

    @Override
    public void cancel(OrderContext context) {
        System.out.println("Order is already cancelled.");
    }

    @Override
    public String getStateName() {
        return "Cancelled";
    }
}

// Driver
public class Main {

    public static void main(String[] args) {

        OrderContext order = new OrderContext();

        System.out.println("Current State: " + order.getState());

        order.next();
        System.out.println("Current State: " + order.getState());

        order.next();
        System.out.println("Current State: " + order.getState());

        order.next();
        System.out.println("Current State: " + order.getState());

        order.cancel();
    }
}