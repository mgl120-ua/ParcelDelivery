import java.time.LocalDateTime;
import java.time.Duration;
import java.util.*;

public class Parcel {
    private static int nextId = 0;
    private int id;
    private final String sender; // cannot be changed upon the registration
    private final String recipient;// cannot be changed upon the registration
    private final List<Event> transitHistory; // it stores all events related to that parcel
    private ParcelSize parcelSize;

    private boolean delivered;
    private LocalDateTime deliveredTime;
    private LocalDateTime pickupTime;

    // expressed in working days from deposit in the sender locker
    private int estimatedDeliveryTime;
    private int actualDeliveryTime;
    private int guaranteedDeliveryTime;

    public boolean insurance;
    public boolean priority;
    public boolean personalDelivery;

    // TODO private ParcelLocker location;//Could be ParcelLoker, ExternalStore or
    // Inmediate store

    public Parcel(String sender, String recipient, int estimatedDeliveryTime, ParcelSize parcelSize) {
        this.id = nextId++;
        this.sender = sender;
        this.recipient = recipient;
        this.parcelSize = parcelSize;
        this.transitHistory = new ArrayList<>();
        this.estimatedDeliveryTime = estimatedDeliveryTime;
    }

    public void addEvent(String eventType, LocalDateTime timestamp, ParcelLocker location) {
        transitHistory.add(new Event(eventType, timestamp, location, this));
    }

    public void markDelivered(ParcelLocker location) {
        if (!delivered) {
            deliveredTime = LocalDateTime.now();// marks the delivered time
            transitHistory.add(new Event("arrival", LocalDateTime.now(), location, this));
            delivered = true;
        } else {
            System.out.println("Parcel has already been delivered");
        }
    }

    public void markPickupTime(ParcelLocker location) {
        if (delivered) {
            pickupTime = LocalDateTime.now(); // marks the pick-up time
            Duration transitDuration = Duration.between(pickupTime, deliveredTime);
            actualDeliveryTime = (int) transitDuration.toDays();
            transitHistory.add(new Event("departure", LocalDateTime.now(), location, this));
        } else {
            System.out.println("Parcel has not been delivered yet");
        }
    }

    // getters
    public int getId() {
        return id;
    }

    public String getSender() {
        return sender;
    }

    public String getRecipient() {
        return recipient;
    }

    public ParcelSize getParcelSize() {
        return parcelSize;
    }

    public List<Event> getTransitHistory() {
        return transitHistory;
    }

    public LocalDateTime getDeliveredTime() {
        return deliveredTime;
    }

    public LocalDateTime getpickupTime() {
        return pickupTime;
    }

    public int getEstimatedDeliveryTime() {
        return estimatedDeliveryTime;
    }

    public int getActualDeliveryTime() {
        return actualDeliveryTime;
    }

    public int getGuaranteedDeliveryTime() {
        return guaranteedDeliveryTime;
    }

    public boolean hasInsurance() {
        return insurance;
    }

    public boolean hasPriority() {
        return priority;
    }

    public boolean hasPersonalDelivery() {
        return personalDelivery;
    }

    // Setters

    public void setInsurance() {
        insurance = true;
    }

    public void setPriority() {
        priority = true;
    }

    public void setPersonalDelivery() {
        personalDelivery = true;
    }
}