package model;

public class Client extends Task {
    private int startTime; // Time when service starts for this client

    public Client(int id, int arrivalTime, int serviceTime) {
        super(id, arrivalTime, serviceTime);
        this.startTime = -1; // Initialize with -1 indicating that service hasn't started yet
    }

    public int getStartTime() {
        return startTime;
    }

    public void setStartTime(int startTime) {
        this.startTime = startTime;
    }
}
