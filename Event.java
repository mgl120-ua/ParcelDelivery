import java.time.LocalDateTime;

public final class Event { // Immutable and non-removable
    private final String type; // Arrival, departure, change of terms, etc
    private final LocalDateTime timestamp;
    private final ParcelLocker location; // DEBERIA SER UN LOCKER (sender locker o receiver locker)
    private final Parcel parcel;

    public Event(String type, LocalDateTime timestamp, ParcelLocker location, Parcel parcel) {
        this.type = type;
        this.timestamp = timestamp;
        this.location = location;
        this.parcel = parcel;
    }

    public String getEventType() {
        return type;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public ParcelLocker getLocation() {
        return location;
    }

    public Parcel getParcel() {
        return parcel;
    }
}