package logger;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

public class EventLogger {
    private static final String FILE_NAME = "events.txt";

    // Log with simulation time
    // Adjust this method in EventLogger
    public static void log(String message) {
        // Output to terminal and log to file without real-world timestamp
        System.out.println(message);
        try (PrintWriter out = new PrintWriter(new FileWriter(FILE_NAME, true))) {
            out.println(message);
        } catch (IOException e) {
            System.err.println("Error writing to log file: " + e.getMessage());
        }
    }


    public static void logClientArrival(int clientId, int arrivalTime) {
        log("Client " + clientId + " arrived at " + arrivalTime);
    }

    public static void logServiceStart(int clientId, String queueName) {
        log("Service for Client " + clientId + " started in " + queueName);
    }

}
