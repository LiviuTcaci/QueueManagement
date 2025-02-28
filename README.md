# Queue Management System Simulator
A multi-threaded Java application that simulates a queue management system for optimizing client waiting times.

## Project Overview

This project implements a queue management system using threads and synchronization mechanisms to model real-world scenarios where multiple clients are serviced by different queues. The primary goal is to minimize client waiting time through efficient queue allocation strategies.

## System Architecture

The application follows an MVC architecture with these key components:

- **Model**: Client, Server, and Task classes representing the core data structures
- **Controller**: Scheduler and SimulationManager classes orchestrating the simulation
- **View**: EventLogger for output visualization

## Key Features

- Multi-threaded design with each server running on a dedicated thread
- Thread-safe operations using synchronization mechanisms
- Multiple queue selection strategies:
  - Shortest queue (least number of waiting clients)
  - Shortest waiting time (least total service time)
- Dynamic client generation with configurable parameters
- Real-time logging of simulation events
- Performance metrics calculation (average waiting time, service time)

## Design Patterns

- **Strategy Pattern**: Used to dynamically switch between queue selection algorithms
- **Singleton Pattern**: Applied to SimulationClock and EventLogger components

## How to Run

1. Clone the repository
2. Compile the Java files or import as a project in your IDE
3. Run the main method in `SimulationManager.java`

## Configuration

You can modify the following parameters in `SimulationManager.java`:

```java
private int timeLimit = 30;  // Simulation time in seconds
private int numberOfClients = 3;  // Number of clients to generate
// In constructor:
scheduler = new Scheduler(2);  // Number of servers/queues
scheduler.changeStrategy(SelectionPolicy.SHORTEST_TIME);  // Selection strategy
```

Client generation parameters (in `generateNRandomTasks` method):
```java
int arrivalTime = RandomGenerator.getRandomInteger(1, 4);  // Random arrival time between 1-4
int serviceTime = RandomGenerator.getRandomInteger(2, 4);  // Random service time between 2-4
```

## Output

The simulation outputs events to both console and an `events.txt` file, including:
- Client arrivals
- Service start times
- Queue status at each time step
- Final performance metrics

## Package Structure

- `logger`: Event logging functionality
- `main`: Main simulation control
- `model`: Core data structures (Client, Server, Task)
- `scheduler`: Queue selection strategies
- `util`: Utility classes for simulation

## Metrics and Analysis

The system collects and reports:
- Average waiting time
- Average service time
- Maximum waiting time

## Future Improvements

- GUI interface for better visualization
- More sophisticated scheduling algorithms
- Dynamic server count based on load
- Advanced statistical analysis of performance

## Requirements

- Java 8 or higher
- No external dependencies
