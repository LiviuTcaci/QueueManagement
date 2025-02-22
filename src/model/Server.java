package model;

import util.SimulationClock;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicInteger;

public class Server implements Runnable {
    private BlockingQueue<Client> tasks = new LinkedBlockingQueue<>();
    private AtomicInteger waitingPeriod = new AtomicInteger(0);
    private volatile boolean isActive = false;

    public synchronized void addTask(Client client) {
        tasks.offer(client);
        waitingPeriod.addAndGet(client.getServiceTime());
        notifyAll();  // Notify potentially waiting threads that a new task has been added
    }
    public void stopServer() {
        isActive = true;  // Set isRunning to false to stop the server thread
        synchronized (this) {
            notifyAll();  // Ensure the server wakes up if it's waiting
        }
    }

    public synchronized String queueStatus() {
        return tasks.isEmpty() ? "closed" : "open";
    }

    @Override
    public void run() {
        while (!isActive || !tasks.isEmpty()) {
            try {
                Client client = null;
                synchronized (this) {
                    while (tasks.isEmpty() && !isActive) {
                        wait();
                    }
                    if (!tasks.isEmpty()) {
                        client = tasks.poll();
                    }
                }
                if (client != null) {
                    client.setStartTime(SimulationClock.getCurrentTime());
                    Thread.sleep(client.getServiceTime() * 1000);
                    tasks.remove(client); // Remove the client from the queue after its service time is done
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }

    public int getWaitingPeriod() {
        synchronized (this) {
            return waitingPeriod.get();
        }
    }

    public BlockingQueue<Client> getTasks() {
        synchronized (this) {
            return tasks;
        }
    }
    public synchronized Client getCurrentClient() {
        return tasks.peek();  // Returns the client at the front of the queue or null if empty
    }
    public synchronized boolean isActive() {
        return !tasks.isEmpty();  // Check if the server is actively processing a task
    }

    public int getQueueSize() {
        return tasks.size();
    }
}