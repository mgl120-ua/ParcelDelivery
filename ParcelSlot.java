public class ParcelSlot {
    private static int nextId = 0;
    private int id;
    private ParcelSize type; // (SMALL, MEDIUM, LARGE)
    private boolean occupied;

    public ParcelSlot(ParcelSize type) {
        this.id = nextId++;
        this.type = type;
        this.occupied = false;
    }

    public boolean isOccupied() {
        return occupied;
    }

    public void setOccupied(boolean occupied) {
        this.occupied = occupied;
    }

    public int getId() {
        return id;
    }

    public ParcelSize getParcelSize() {
        return type;
    }

}
