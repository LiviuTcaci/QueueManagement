package main;

import model.Client;
import model.Server;

import scheduler.Scheduler;
import scheduler.SelectionPolicy;
import util.RandomGenerator;
import logger.EventLogger;
import util.SimulationClock;

import java.util.ArrayList;
import java.util.List;

public class SimulationManager implements Runnable {
    private int timeLimit = 30;  // Simulation time in seconds
    private Scheduler scheduler;
    private List<Client> generatedTasks;
    private int numberOfClients = 3;

    public SimulationManager() {
        scheduler = new Scheduler(2);  // Number of servers
        scheduler.changeStrategy(SelectionPolicy.SHORTEST_TIME);
        generatedTasks = new ArrayList<>();
        generateNRandomTasks();
    }

    private void generateNRandomTasks() {
        for (int i = 1; i <= numberOfClients; i++) {
            int arrivalTime = RandomGenerator.getRandomInteger(1, 4);
            int serviceTime = RandomGenerator.getRandomInteger(2, 4);
            generatedTasks.add(new Client(i, arrivalTime, serviceTime));
            EventLogger.logClientArrival(i, arrivalTime);
        }
        generatedTasks.sort((c1, c2) -> Integer.compare(c1.getArrivalTime(), c2.getArrivalTime()));
    }

    private void shutdownServers() {
        scheduler.stopServers();  // Stop servers and wait for threads to finish
    }



    @Override
    public void run() {
        int index = 0;
        while (SimulationClock.getCurrentTime() <= timeLimit) {
            EventLogger.log("Time " + SimulationClock.getCurrentTime());  // Log the current simulation time
            logWaitingClients(index);

            while (index < generatedTasks.size() && generatedTasks.get(index).getArrivalTime() <= SimulationClock.getCurrentTime()) {
                Client client = generatedTasks.get(index);
                scheduler.dispatchTask(client);
                EventLogger.logServiceStart(client.getId(), "Dispatched to Queue");
                index++;
            }

            logQueueStatus();  // Log status after potential task dispatch

            try {
                Thread.sleep(1000); // Simulating each second of simulation
                SimulationClock.incrementTime();  // Increment simulation time
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
        calculateAndLogMetrics();
        shutdownServers();  // Ensure all threads are terminated properly
    }

    private void logWaitingClients(int index) {
        StringBuilder waitingClients = new StringBuilder("Waiting clients: ");
        for (int i = index; i < generatedTasks.size(); i++) {
            Client client = generatedTasks.get(i);
            if (client.getArrivalTime() > SimulationClock.getCurrentTime()) {
                waitingClients.append(String.format("(%d,%d,%d); ", client.getId(), client.getArrivalTime(), client.getServiceTime()));
            }
        }
        EventLogger.log(waitingClients.toString());
    }

    private void logQueueStatus() {
        StringBuilder queueStatus = new StringBuilder("Time " + SimulationClock.getCurrentTime() + "\n");
        List<Server> servers = scheduler.getServers();
        for (int i = 0; i < servers.size(); i++) {
            Server server = servers.get(i);
            queueStatus.append("Queue ").append(i + 1).append(": ");
            Client client = server.getCurrentClient();
            if (client != null) {
                queueStatus.append("(").append(client.getId()).append(",").append(client.getArrivalTime())
                        .append(",").append(client.getServiceTime()).append("); ");
            } else {
                queueStatus.append("closed; ");
            }
        }
        EventLogger.log(queueStatus.toString());
    }



    private void calculateAndLogMetrics() {
        double totalWaitingTime = 0;
        double totalServiceTime = 0;
        int maxWaitingTime = 0;

        for (Client client : generatedTasks) {
            int waitingTime = client.getStartTime() - client.getArrivalTime();
            totalWaitingTime += waitingTime;
            totalServiceTime += client.getServiceTime();
            if (waitingTime > maxWaitingTime) {
                maxWaitingTime = waitingTime;
            }
        }

        double averageWaitingTime = totalWaitingTime / generatedTasks.size();
        double averageServiceTime = totalServiceTime / generatedTasks.size();

        EventLogger.log("Simulation ends.");
        EventLogger.log("Average Waiting Time: " + averageWaitingTime + " seconds");
        EventLogger.log("Average Service Time: " + averageServiceTime + " seconds");
        EventLogger.log("Maximum Waiting Time: " + maxWaitingTime + " seconds");
    }


    public static void main(String[] args) {
        SimulationManager simManager = new SimulationManager();
        Thread t = new Thread(simManager);
        t.start();
        try {
            t.join();  // Ensures the main thread waits for the simulation to complete
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

}
