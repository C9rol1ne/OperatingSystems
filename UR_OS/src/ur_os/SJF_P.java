/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ur_os;

import java.util.Comparator;
import java.util.PriorityQueue;

/**
 *
 * @author prestamour
 */
public class SJF_P extends Scheduler {

    SJF_P(OS os) {
        super(os);
    }

    @Override
    // When a NEW process enters the queue, process in CPU, if any, is extracted to
    // compete with the rest
    public void newProcess(boolean cpuEmpty) {
    }

    @Override
    public void IOReturningProcess(boolean cpuEmpty) {// When a process returns from IO and enters the queue, process in
                                                      // CPU, if any, is extracted to compete with the rest
    }

    private Process findShortestRemainingProcess() {
        if (processes.isEmpty()) {
            return null;
        }

        Process shortestRBT = processes.get(0);
        for (Process p : processes) {
            if (p.getRemainingTimeInCurrentBurst() < shortestRBT.getRemainingTimeInCurrentBurst()) {
                shortestRBT = p;
            }
        }

        return shortestRBT;
    }

    @Override
    // In case of a draw FIFO is applyed
    public void getNext(boolean cpuEmpty) {
        if (!processes.isEmpty()) { // if there are processes in the RQ
            if (cpuEmpty) { // CPU is empty
                Process shortestRBTprocess = findShortestRemainingProcess();

                if (shortestRBTprocess != null) {
                    processes.remove(shortestRBTprocess);
                    os.interrupt(InterruptType.SCHEDULER_RQ_TO_CPU, shortestRBTprocess);
                }

            } else { // CPU busy
                int currentRemainingTimeOfCpuProcess = os.cpu.getProcess().getRemainingTimeInCurrentBurst();

                Process shortestProcessInRq = findShortestRemainingProcess();

                if (currentRemainingTimeOfCpuProcess > shortestProcessInRq.getRemainingTimeInCurrentBurst()) {
                    processes.remove(shortestProcessInRq);
                    os.interrupt(InterruptType.SCHEDULER_CPU_TO_RQ, shortestProcessInRq);
                }
            }

        }

    }

}
