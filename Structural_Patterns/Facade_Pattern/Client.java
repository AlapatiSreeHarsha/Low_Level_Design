package Structural_Patterns.Facade_Pattern;
class Client {
    static class PaymentService {
        public void pay(String paymentType, double amount) {
            System.out.println("Payment of " + amount + " made using " + paymentType);
        }
    }

    static class SeatReservation {
        public void reserveSeat(String seatNumber) {
            System.out.println("Seat " + seatNumber + " reserved successfully.");
        }
    }

    static class NotificationService {
        public void sendNotification(String message) {
            System.out.println("Notification sent: " + message);
        }
    }

    static class LoyaltyPoints {
        public void addPoints(int points) {
            System.out.println(points + " loyalty points added to your account.");
        }
    }

    static class TicketBooking {
        public void bookTicket(String ticketId) {
            System.out.println("Ticket " + ticketId + " booked successfully.");
        }
    }

    static class MovieBookingFacade {
        private PaymentService paymentService;
        private SeatReservation seatReservation;
        private NotificationService notificationService;
        private LoyaltyPoints loyaltyPoints;
        private TicketBooking ticketBooking;

        public MovieBookingFacade() {
            paymentService = new PaymentService();
            seatReservation = new SeatReservation();
            notificationService = new NotificationService();
            loyaltyPoints = new LoyaltyPoints();
            ticketBooking = new TicketBooking();
        }

        public void bookMovieTicket(String paymentType, double amount, String seatNumber, String ticketId) {
            paymentService.pay(paymentType, amount);
            seatReservation.reserveSeat(seatNumber);
            ticketBooking.bookTicket(ticketId);
            loyaltyPoints.addPoints(10); // Adding 10 loyalty points for each booking
            notificationService.sendNotification("Your movie ticket has been booked successfully!");
        }
    }

    public static void main(String[] args) {
        MovieBookingFacade facade = new MovieBookingFacade();
        facade.bookMovieTicket("Credit Card", 15.99, "A1", "T001");
    }
}