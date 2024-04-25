import java.util.*;

// Dedicated place controlled by the system, in which the uncollected parcels are safely storedpublic 
class ExternalStorage {
    private List<Parcel> storedParcels;

    public ExternalStorage() {
        this.storedParcels = new ArrayList<>();
    }

    public void storeParcel(Parcel parcel) {
        storedParcels.add(parcel);
        System.out.println("Parcel " + parcel.getId() + " safely stored in external storage.");
    }
}