package scheduler;

import model.Client;
import model.Server;
import model.Task;

import java.util.List;

public class ConcreteStrategyTime implements Strategy {
    @Override
    public void addTask(List<Server> servers, Client client) {
        servers.stream()
                .min((s1, s2) -> Integer.compare(s1.getWaitingPeriod(), s2.getWaitingPeriod()))
                .ifPresent(server -> server.addTask(client));
    }
}
