import java.time.LocalDateTime;

public class PaymentSystem {
    private static final double BASE_COST = 5.0;
    // Additional costs
    private static final double INSURANCE_COST = 2.0;
    private static final double PRIORITY_COST = 3.0;
    private static final double PERSONAL_DELIVERY_COST = 4.0;
    private static final double EXTENSION_COST = 1.0;

    public static double calculateShippingCost(Parcel parcel) {
        double totalCost = BASE_COST;

        if (parcel.getParcelSize() == ParcelSize.LARGE) {
            totalCost += 2.0;
        }

        if (parcel.getParcelSize() == ParcelSize.MEDIUM) {
            totalCost += 1.0;
        }

        if (parcel.hasInsurance()) {
            totalCost += INSURANCE_COST;
        }

        if (parcel.hasPriority()) {
            totalCost += PRIORITY_COST;
        }

        if (parcel.hasPersonalDelivery()) {
            totalCost += PERSONAL_DELIVERY_COST;
        }

        if (movedExternalStorage(parcel.getpickupTime())) {
            // TODO moved to the external storage
        }

        if (isLateForPickup(parcel.getpickupTime())) {
            totalCost += EXTENSION_COST;
        }
        return totalCost;
    }

    // Method to verify if the locker collection deadline has been exceeded
    public static boolean movedExternalStorage(LocalDateTime pickupTime) {
        LocalDateTime deadline = pickupTime.plusHours(48);
        return LocalDateTime.now().isAfter(deadline);
    }

    public static boolean isLateForPickup(LocalDateTime pickupTime) {
        LocalDateTime deadline = pickupTime.plusHours(168);
        return LocalDateTime.now().isAfter(deadline);
    }
}
