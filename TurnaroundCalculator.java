package Schedulers;
import java.io.*;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
public class TurnaroundCalculator {
    public static void main(String[] args) throws IOException {
        int counter = 1;
        while (counter <= 5) {
            String path = "C://Users/talle/Desktop/OS Study/Scheduleres/input5.txt";
            BufferedReader reader = new BufferedReader(new FileReader(path));
            List<Process> processList = new ArrayList<>();
            // Read the first line
            int numRows = Integer.parseInt(reader.readLine());
            // Read the remaining lines
            int[][] processes = new int[numRows][2];
            for (int i = 0; i < numRows; i++) {
                String s = reader.readLine();
                String[] c = s.split(",");
                processes[i][0] = Integer.parseInt(c[0]);
                processes[i][1] = Integer.parseInt(c[1]);
                processList.add(new Process(i, processes[i][0], processes[i][1]));
            }

            // Calculate the average turnaround time for each scheduling strategy
            switch (counter) {
                case 1 -> {
                    double FCFS = calculateFCFS(processes);
                    System.out.println("FCFS: mean turnaround = " + FCFS);
                }
                case 2 -> {
                    double LCFSNonPreemptive = calculateLCFSNonPreemptive(processes);
                    System.out.println("LCFS (NP): mean turnaround = " + LCFSNonPreemptive);
                }
                case 3 -> {
                    double LCFSPreemptive = calculateLCFSPreemptiveMeanTurnaround(processList);
                    System.out.println("LCFS (P): mean turnaround = " + LCFSPreemptive);
                }
                case 4 -> {
                    double RR = calculateRR(processList, 2);
                    System.out.println("RR: mean turnaround = " + RR);
                }
                case 5 -> {
                    double SJFPreemptive = calculateSJFPreemptive(processList);
                    System.out.println("SJF: mean turnaround = " + SJFPreemptive);
                }
            }
            counter++;

        }
    }
    // Calculates the average turnaround time using the First Come First Serve (FCFS) scheduling strategy
    public static double calculateFCFS(int[][] processes) {
        int numProcesses = processes.length;
        int[] turnaroundTimes = new int[numProcesses];
        // Calculate the turnaround time for each process
        int currentTime = 0;
        while (currentTime < Integer.MAX_VALUE) {
            // Find the process with the lowest arrival time
            int minArrivalTime = Integer.MAX_VALUE;
            int minArrivalTimeIndex = -1;
            for (int i = 0; i < numProcesses; i++) {
                if (processes[i][0] < minArrivalTime && processes[i][1] > 0) {
                    minArrivalTime = processes[i][0];
                    minArrivalTimeIndex = i;
                }
            }
            // If no process was found, break the loop
            if (minArrivalTimeIndex == -1) {
                break;
            }
            // Retrieve the arrival time and computation time for the process with the lowest arrival time
            int arrivalTime = processes[minArrivalTimeIndex][0];
            int computationTime = processes[minArrivalTimeIndex][1];
            // Wait for the process to arrive if necessary
            if (currentTime < arrivalTime) {
                currentTime = arrivalTime;
            }
            // Calculate the turnaround time for the process
            turnaroundTimes[minArrivalTimeIndex] = currentTime + computationTime - arrivalTime;
            // Update the current time and the remaining computation time for the process
            currentTime += computationTime;
            processes[minArrivalTimeIndex][1] = 0;
        }
        // Calculate the mean turnaround time
        double meanTurnaround = 0;
        for (int turnaroundTime : turnaroundTimes) {
            meanTurnaround += turnaroundTime;
        }
        meanTurnaround /= numProcesses;
        return meanTurnaround;
    }
    // Calculates the average turnaround time using the Last Come First Serve (LCFS) scheduling strategy with non-preemptive scheduling
    public static double calculateLCFSNonPreemptive(int[][] processes) {
        int numProcesses = processes.length;
        int[] turnaroundTimes = new int[numProcesses];
        // Calculate the turnaround time for each process
        int currentTime = 0;
        // Find the process with the lowest arrival time
        int minArrivalTime = Integer.MAX_VALUE;
        int minArrivalTimeIndex = -1;
        for (int i = 0; i < numProcesses; i++) {
            if (processes[i][0] < minArrivalTime && processes[i][1] > 0) {
                minArrivalTime = processes[i][0];
                minArrivalTimeIndex = i;
            }
        }
        int arrivalTime = processes[minArrivalTimeIndex][0];
        int computationTime = processes[minArrivalTimeIndex][1];
        // Wait for the process to arrive if necessary
        if (currentTime < arrivalTime) {
            currentTime = arrivalTime;
        }
        // Calculate the turnaround time for the process
        turnaroundTimes[minArrivalTimeIndex] = currentTime + computationTime - arrivalTime;
        // Update the current time and the remaining computation time for the process
        currentTime += computationTime;
        processes[minArrivalTimeIndex][1] = 0;

        while (currentTime < Integer.MAX_VALUE) {
            // Find the process with the higest arrival time
            int maxArrivalTime = Integer.MIN_VALUE;
            int maxArrivalTimeIndex = -1;
            for (int i = 0; i < numProcesses; i++) {
                if (processes[i][0] > maxArrivalTime && processes[i][1] > 0) {
                    maxArrivalTime = processes[i][0];
                    maxArrivalTimeIndex = i;
                }
            }
            // If no process was found, break the loop
            if (maxArrivalTimeIndex == -1) {
                break;
            }
            // Retrieve the arrival time and computation time for the process with the highest arrival time
            int arrivalTime1 = processes[maxArrivalTimeIndex][0];
            int computationTime1 = processes[maxArrivalTimeIndex][1];
            // Wait for the process to arrive if necessary
            while (currentTime < arrivalTime1) {
                /*arrivalTime1 = processes[maxArrivalTimeIndex - 1][0];
                computationTime1 = processes[maxArrivalTimeIndex - 1][1];*/
                currentTime++;
            }
            // Calculate the turnaround time for the process
            turnaroundTimes[maxArrivalTimeIndex] = currentTime + computationTime1 - arrivalTime1;
            // Update the current time and the remaining computation time for the process
            currentTime += computationTime1;
            processes[maxArrivalTimeIndex][1] = 0;
        }
        // Calculate the mean turnaround time
        double meanTurnaround = 0;
        for (int turnaroundTime : turnaroundTimes) {
            meanTurnaround += turnaroundTime;
        }
        meanTurnaround /= numProcesses;
        return meanTurnaround;
    }
    /*public static double calculateLCFSNonPreemptive(List<Process> processList) {
        int numProcesses = processList.size();
        int[] turnaroundTimes = new int[numProcesses];
        // Sort the processes by arrival time
        int currentTime = 0;
        processList.sort(new Comparator<Process>() {
            @Override
            public int compare(Process p1, Process p2) {
                return Integer.compare(p1.arrivalTime, p2.arrivalTime);
            }
        });
        int completed = 0;

        for(Process q : processList){
            if (q.getRemaining() == 0)
                completed++;
        }

        ArrayList<Process> readyQueue = new ArrayList<>();

        // Add processes to ready queue as they arrive
        for (Process p : processList) {
            if (p.getArrivalTime() == currentTime) {
                readyQueue.add(p);
            }
        }
        // Execute processes until all are completed
        while (completed < numProcesses) {
            // Check if ready queue is empty
            if (readyQueue.isEmpty()) {
                currentTime++;
            }
            // Add processes to ready queue as they arrive
            do {
                for (Process newProc : processList) {
                    if (newProc.getArrivalTime() <= currentTime && newProc.getRemaining() > 0 && !readyQueue.contains(newProc)) {
                        readyQueue.add(newProc);
                    }
                }
                if (readyQueue.isEmpty()) {
                    currentTime++;
                }
            }while(readyQueue.isEmpty());

            // Sort ready queue in descending order of arrival time
            readyQueue.sort((b, a) -> a.getArrivalTime() - b.getArrivalTime());
            // Get next process from ready queue
            Process p = readyQueue.get(0);
            currentTime += p.getRemaining();
            p.setRemaining(0);
            // If process has completed, remove it from the ready queue
            if (p.getRemaining() == 0) {
                completed++;
                readyQueue.remove(p);
                turnaroundTimes[p.getIndex()] = currentTime - p.getArrivalTime();
            }
        }
        // Calculate the mean turnaround time
        double meanTurnaround = 0;
        for (int turnaroundTime : turnaroundTimes) {
            meanTurnaround += turnaroundTime;
        }
        meanTurnaround /= numProcesses;
        return meanTurnaround;

    }*/


    // Calculates the average turnaround time using the Last Come First Serve (LCFS) scheduling strategy with preemptive scheduling
    public static double calculateLCFSPreemptiveMeanTurnaround(List<Process> processList) {
        int numProcesses = processList.size();
        int[] turnaroundTimes = new int[numProcesses];
        // Sort the processes by arrival time
        int currentTime = 0;
        processList.sort(new Comparator<Process>() {
            @Override
            public int compare(Process p1, Process p2) {
                return Integer.compare(p1.arrivalTime, p2.arrivalTime);
            }
        });
        int completed = 0;

        for(Process q : processList){
            if (q.getRemaining() == 0)
                completed++;
        }

        ArrayList<Process> readyQueue = new ArrayList<>();

        // Add processes to ready queue as they arrive
        for (Process p : processList) {
            if (p.getArrivalTime() == currentTime) {
                readyQueue.add(p);
            }
        }
        // Execute processes until all are completed
        while (completed < numProcesses) {
            // Check if ready queue is empty
            if (readyQueue.isEmpty()) {
                currentTime++;
            }
            // Add processes to ready queue as they arrive
            do {
                for (Process newProc : processList) {
                    if (newProc.getArrivalTime() <= currentTime && newProc.getRemaining() > 0 && !readyQueue.contains(newProc)) {
                        readyQueue.add(newProc);
                    }
                }
                if (readyQueue.isEmpty()) {
                    currentTime++;
                }
            }while(readyQueue.isEmpty());

            // Sort ready queue in descending order of arrival time
            readyQueue.sort((b, a) -> a.getArrivalTime() - b.getArrivalTime());
            // Get next process from ready queue
            Process p = readyQueue.get(0);
            currentTime++;
            p.setRemaining(p.getRemaining()-1);
            // If process has completed, remove it from the ready queue
            if (p.getRemaining() == 0) {
                completed++;
                readyQueue.remove(p);
                turnaroundTimes[p.getIndex()] = currentTime - p.getArrivalTime();
            }
        }
        // Calculate the mean turnaround time
        double meanTurnaround = 0;
        for (int turnaroundTime : turnaroundTimes) {
            meanTurnaround += turnaroundTime;
        }
        meanTurnaround /= numProcesses;
        return meanTurnaround;

    }
    public static double calculateRR(List<Process> processList, int time) {
        int numProcesses = processList.size();
        int[] turnaroundTimes = new int[numProcesses];
        // Sort the processes by arrival time
        processList.sort(new Comparator<Process>() {
            @Override
            public int compare(Process p1, Process p2) {
                return Integer.compare(p1.arrivalTime, p2.arrivalTime);
            }
        });
        // Calculate the turnaround time for each process
        int currentTime = 0;
        while (!processList.isEmpty()) {
            Process p = processList.remove(0);
            if (currentTime == 0) {
                if (p.getLength() > time) {
                    p.updateLength(time);
                    currentTime += p.getArrivalTime() + (time + p.getArrivalTime()) % 2;
                    processList.add(p);
                } else {
                    currentTime += p.getArrivalTime() + p.getLength();
                    turnaroundTimes[p.getIndex()] = currentTime - p.getArrivalTime();
                }
            } else {
                if (p.getArrivalTime() <= currentTime) {
                    if (p.getLength() > time) {
                        p.updateLength(time);
                        currentTime += time;
                        processList.add(p);
                    } else {
                        currentTime += p.getLength();
                        turnaroundTimes[p.getIndex()] = currentTime - p.getArrivalTime();
                    }
                } else {
                    processList.add(p);
                    currentTime++;
                }
            }

        }
        // Calculate the mean turnaround time
        double meanTurnaround = 0;
        for (int turnaroundTime : turnaroundTimes) {
            meanTurnaround += turnaroundTime;
        }
        meanTurnaround /= numProcesses;
        return meanTurnaround;
    }
    // Calculates the average turnaround time using the Shortest Job First (SJF) scheduling strategy with preemptive scheduling
    public static double calculateSJFPreemptive(List<Process> processList) {
        int numProcesses = processList.size();
        int[] turnaroundTimes = new int[numProcesses];
        int n = processList.size();
        // Sort the processes by arrival time
        processList.sort(new Comparator<Process>() {
            @Override
            public int compare(Process p1, Process p2) {
                return Integer.compare(p1.arrivalTime, p2.arrivalTime);
            }
        });
        // Set of processes that are ready to execute
        ArrayList<Process> readyQueue = new ArrayList<>();
        int currentTime = 0;
        int completed = 0;
        for(Process q : processList){
            if (q.getRemaining() == 0)
                completed++;
        }

        // Add processes to ready queue as they arrive
        for (Process p : processList) {
            if (p.getArrivalTime() == currentTime) {
                readyQueue.add(p);
            }
        }
        // Execute processes until all are completed
        while (completed < n) {
            // Check if ready queue is empty
            if (readyQueue.isEmpty()) {
                currentTime++;
            }
            // Add processes to ready queue as they arrive
            do {
                for (Process newProc : processList) {
                    if (newProc.getArrivalTime() <= currentTime && newProc.getRemaining() > 0 && !readyQueue.contains(newProc)) {
                        readyQueue.add(newProc);
                    }
                    if (readyQueue.isEmpty()) {
                        currentTime++;
                    }
                }
            }while(readyQueue.isEmpty());
            // Sort ready queue in ascending order of remaining time
            readyQueue.sort((a, b) -> a.getRemaining() - b.getRemaining());
            // Get next process from ready queue
            Process p = readyQueue.get(0);
            currentTime++;
            p.setRemaining(p.getRemaining()-1);
            // If process has completed, remove it from the ready queue
            if (p.getRemaining() == 0) {
                completed++;
                readyQueue.remove(p);
                turnaroundTimes[p.getIndex()] = currentTime - p.getArrivalTime();
            }
        }

        // Calculate average waiting time and average turn around time
        int total_tat = 0;
        for (int i = 0; i < n; i++) {
            total_tat += turnaroundTimes[i];
        }
        return (float) total_tat / (float)n;
    }
}









