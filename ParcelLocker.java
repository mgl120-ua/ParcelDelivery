import java.time.LocalDateTime;
import java.util.*;

public class ParcelLocker {
    private static int nextId = 0;
    private int id;
    private String address;
    private int capacity;
    private Map<Parcel, ParcelSlot> parcelSlotMap;
    private List<Event> history;
    private Queue<Parcel> plannedParcels;

    public ParcelLocker(String address, int capacity) {
        this.id = nextId++;
        this.address = address;
        this.capacity = capacity;
        this.parcelSlotMap = new LinkedHashMap<>(capacity); // LinkedHashMap to keep the insertion order
        initializeSlots(capacity / 3, ParcelSize.SMALL);
        initializeSlots(capacity / 3, ParcelSize.MEDIUM);
        initializeSlots(capacity / 3, ParcelSize.LARGE);

        this.history = new ArrayList<>();
        this.plannedParcels = new LinkedList<>();
    }

    private void initializeSlots(int capacity, ParcelSize type) {
        for (int i = 0; i < capacity; i++) {
            ParcelSlot slot = new ParcelSlot(type);
            parcelSlotMap.put(null, slot);
        }
    }

    public void setAddress(String newAddress) {
        boolean change = true;

        if (change) {
            // Check if there are any Parcels stored in the ParcelLocker
            for (Parcel parcel : parcelSlotMap.keySet()) {
                if (parcel != null) {
                    // Check if there is any Parcel in transit to this ParcelLocker
                    for (Event event : parcel.getTransitHistory()) {
                        if (event.getLocation() == this) {
                            change = false;
                            break;
                        }
                    }
                }
            }
        }

        if (change) {
            this.address = newAddress;
            System.out.println("Locker address changed successfully.");
        } else {
            System.out.println("Cannot change locker address: parcels are stored or in transit.");
        }

    }

    // If the parcel is deposited return true
    public boolean depositParcel(Parcel parcel) {
        boolean deposited = false;

        // Firts: Find Available Slot (con el tamaño del paquete mas pequeño posible)
        for (Parcel it : parcelSlotMap.keySet()) { // Iterates parcels
            ParcelSlot slot = parcelSlotMap.get(it);
            if (!slot.isOccupied()) {
                parcelSlotMap.put(parcel, slot);// Second: Insert the parcel in this slot
                slot.setOccupied(true);
                parcel.markDelivered(this);// Third: mark the parcel as deposited in this locker
                history.add(new Event("arrival", LocalDateTime.now(), this, parcel));
                deposited = true;

                Parcel plannedParcel = plannedParcels.peek();
                if (plannedParcel.equals(parcel))
                    plannedParcels.poll(); // Remove the planned parcel from the queue

                break;
            }
        }

        return deposited;
    }

    // Método para recoger una parcela del locker
    public boolean collectParcel(Parcel parcel) {
        boolean collected = false;
        // Firts: Find Available Slot (con el tamaño del paquete mas pequeño posible)
        for (Parcel it : parcelSlotMap.keySet()) { // Iterates parcels
            if (it.equals(parcel)) {
                parcelSlotMap.remove(parcel);// Second: Pulls the parcel out of the slot
                parcelSlotMap.get(it).setOccupied(false);// = slot
                parcel.markPickupTime(this);// Third: mark parcel as pickup from this locker
                history.add(new Event("departure", LocalDateTime.now(), this, parcel));
                collected = true;
                break;
            }
        }

        return collected;
    }

    // Método para actualizar el historial a los paquetes de los ultimos 7 dias
    public List<Event> getHistory() {
        LocalDateTime sevenDaysAgo = LocalDateTime.now().minusDays(7);

        Iterator<Event> iterator = history.iterator();
        while (iterator.hasNext()) { // iterates all events
            Event event = iterator.next();
            if (event.getTimestamp().isBefore(sevenDaysAgo)) {
                iterator.remove(); // removes from the history the events with a date older than the last 7 days
            }
        }

        return history; // returns updated history
    }

    public void registerPlannedParcel(Parcel parcel) {
        plannedParcels.add(parcel);
        System.out.println("Planned parcel registered successfully.");
    }

    // The locker can determine its expected occupancy
    public int calculateExpectedOccupancy(LocalDateTime time) {
        int expectedOccupancy = 0;

        // Calculate the expected occupancy of the planned parcels
        for (Parcel plannedParcel : plannedParcels) {
            LocalDateTime estimatedDeliveryTime = plannedParcel.getDeliveredTime()
                    .plusDays(plannedParcel.getEstimatedDeliveryTime());
            if (estimatedDeliveryTime.isAfter(time)) {
                expectedOccupancy++;
            }
        }

        // Calculate the expected occupancy of the deposited parcels
        for (Parcel parcel : parcelSlotMap.keySet()) {
            if (parcelSlotMap.get(parcel).isOccupied() && parcel.getDeliveredTime().isBefore(time)) {
                expectedOccupancy++;
            }
        }

        return expectedOccupancy;
    }

    // The locker can determine availability at an arbitrary time
    public boolean isAvailable(LocalDateTime time) {
        int expectedOccupancy = calculateExpectedOccupancy(time);
        return (capacity - expectedOccupancy) > 0;
    }

    public void userPanel() {
        Scanner scanner = new Scanner(System.in);

        System.out.println("USER PANEL\nChoose a number:\n");
        System.out.println("0. Exit\n 1. Sending a parcel\n 2. Collecting a parcel");

        int op = scanner.nextInt();
        ;
        switch (op) {
            case 1:
                userSendingParcel();
                break;
            case 2:
                userCollectingParcel();
                break;
            default:
                break;
        }
    }

    public void userSendingParcel() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Please enter the parcel id:");
        String parcelIdentifier = scanner.nextLine();

        // how to find the package through the identifier?
        // not to create a new package
        Parcel newParcel = new Parcel(parcelIdentifier, "Recipient", 1, ParcelSize.MEDIUM);

        // Determine and indicate a slot to store the parcel
        if (depositParcel(newParcel)) {
            System.out.println("Parcel " + parcelIdentifier + " stored successfully.");
        }
        userPanel(); // After completing the action, return to the user panel

    }

    public void userCollectingParcel() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Please enter the parcel id:");
        int parcelIdentifier = scanner.nextInt();

        // Search for the parcel in the slot map
        for (Parcel parcel : parcelSlotMap.keySet()) {
            if (parcel != null && parcel.getId() == parcelIdentifier) {
                ParcelSlot slot = parcelSlotMap.get(parcel);

                System.out.println("Parcel " + parcelIdentifier + " is stored in slot " + slot.getId());
                System.out.println("Slot " + slot.getId() + " opened. Parcel collected.");

                // Mark the package as picked up from the locker
                collectParcel(parcel);
                return;
            }
        }
    }

}
