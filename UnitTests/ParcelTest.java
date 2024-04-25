import static org.junit.Assert.*;
import org.junit.*;

import java.time.LocalDateTime;

public class ParcelTest {

    @Test
    public void testParcelCreation() {
        Parcel parcel = new Parcel("Sender", "Recipient", 3, ParcelSize.SMALL);
        assertNotNull(parcel);
        assertEquals("Sender", parcel.getSender());
        assertEquals("Recipient", parcel.getRecipient());
        assertEquals(3, parcel.getEstimatedDeliveryTime());
        assertEquals(ParcelSize.SMALL, parcel.getParcelSize());
    }

    @Test
    public void testAddEvent() {
        Parcel parcel = new Parcel("Sender", "Recipient", 3, ParcelSize.SMALL);
        LocalDateTime timestamp = LocalDateTime.now();
        parcel.addEvent("departure", timestamp, new ParcelLocker("Locker A"));
        assertEquals(1, parcel.getTransitHistory().size());
    }

    @Test
    public void testMarkDelivered() {
        Parcel parcel = new Parcel("Sender", "Recipient", 3, ParcelSize.SMALL);
        parcel.markDelivered(new ParcelLocker("Locker A"));
        assertTrue(parcel.isDelivered());
        assertNotNull(parcel.getDeliveredTime());
    }

    @Test
    public void testMarkPickupTime() {
        Parcel parcel = new Parcel("Sender", "Recipient", 3, ParcelSize.SMALL);
        parcel.markDelivered(new ParcelLocker("Locker A"));
        LocalDateTime pickupTimestamp = LocalDateTime.now().plusMinutes(30);
        parcel.markPickupTime(new ParcelLocker("Locker B"));
        assertNotNull(parcel.getpickupTime());
        assertTrue(parcel.getActualDeliveryTime() >= 0);
    }

    @Test
    public void testInsurance() {
        Parcel parcel = new Parcel("Sender", "Recipient", 3, ParcelSize.SMALL);
        assertFalse(parcel.hasInsurance());
        parcel.setInsurance();
        assertTrue(parcel.hasInsurance());
    }

}
