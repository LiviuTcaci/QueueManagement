package util;

public class SimulationClock {
    private static volatile int currentTime = 0;

    public static synchronized void incrementTime() {
        currentTime++;
    }

    public static int getCurrentTime() {
        return currentTime;
    }
}
