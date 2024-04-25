import java.util.ArrayList;
import java.util.List;

//A dedicated place controlled by the system (or a courier), to temporally store the parcels while they are in transit. 
public class IntermediateStore {
    private List<Parcel> storedParcels;

    public IntermediateStore() {
        this.storedParcels = new ArrayList<>();
    }

    public void storeParcel(Parcel parcel) {
        storedParcels.add(parcel);
        System.out.println("Parcel " + parcel.getId() + " temporarily stored in intermediate store.");
    }
}
