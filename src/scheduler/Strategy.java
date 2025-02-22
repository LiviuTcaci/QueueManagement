package scheduler;

import java.util.List;
import model.Server;
import model.Client; // Ensure this import is correct

public interface Strategy {
    void addTask(List<Server> servers, Client client);  // Signature must match usage
}