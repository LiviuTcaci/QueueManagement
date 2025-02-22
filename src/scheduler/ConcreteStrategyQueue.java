package scheduler;

import model.Client;
import model.Server;
import model.Task;

import java.util.List;

public class ConcreteStrategyQueue implements Strategy {
    @Override
    public void addTask(List<Server> servers, Client client) {
        servers.stream()
                .min((s1, s2) -> Integer.compare(s1.getTasks().size(), s2.getTasks().size()))
                .ifPresent(server -> server.addTask(client));
    }
}