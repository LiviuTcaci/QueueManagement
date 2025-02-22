package scheduler;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.NoSuchElementException;

import model.Client;
import model.Server;

public class Scheduler {
    private final List<Server> servers;
    private final List<Thread> serverThreads = new ArrayList<>();  // To keep track of server threads
    private Strategy strategy;

    public Scheduler(int maxNoServers) {
        servers = new ArrayList<>(maxNoServers);
        for (int i = 0; i < maxNoServers; i++) {
            Server server = new Server();
            Thread thread = new Thread(server);
            thread.start();
            servers.add(server);
            serverThreads.add(thread);  // Store the thread
        }
    }

    public synchronized void changeStrategy(SelectionPolicy policy) {
        switch (policy) {
            case SHORTEST_QUEUE:
                this.strategy = new ConcreteStrategyQueue();
                break;
            case SHORTEST_TIME:
                this.strategy = new ConcreteStrategyTime();
                break;
            default:
                throw new IllegalArgumentException("Unknown strategy");
        }
    }

    public void dispatchTask(Client client) {
        // Find the server with the shortest queue
        Server shortestQueueServer = servers.stream()
                .min(Comparator.comparingInt(Server::getQueueSize))
                .orElseThrow(NoSuchElementException::new);

        // Add the client to the server's queue
        shortestQueueServer.addTask(client);
    }

    public List<Server> getServers() {
        return servers;
    }

    public List<Thread> getServerThreads() {
        return serverThreads;  // Getter for server threads
    }

    public void stopServers() {
        servers.forEach(Server::stopServer);  // Stop each server
        serverThreads.forEach(thread -> {
            try {
                thread.join();  // Wait for each thread to terminate
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        });
    }
}