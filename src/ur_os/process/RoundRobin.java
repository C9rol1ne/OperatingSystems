/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ur_os.process;

import ur_os.system.InterruptType;
import ur_os.system.OS;

/**
 *
 * @author prestamour
 */
public class RoundRobin extends Scheduler {

    int q;
    int cont;

    RoundRobin(OS os) {
        super(os);
        q = 5;
        cont = 0;
    }

    RoundRobin(OS os, int q) {
        this(os);
        this.q = q;
    }

    void resetCounter() {
        cont = 0;
    }

    @Override
    public void getNext(boolean cpuEmpty) {
        if (cpuEmpty) {
            // Case 1
            // When No processes in queue and CPU empty, do nothing.
            if (processes.isEmpty()) {
                // Do Nothing
                cont = 0;
            } else {
                // Case 2
                // If there are processes waiting on the queue but the CPU is empty
                // The process that was there left the CPU to IO or to Termination
                // Return the next process in the queue, like FCFS
                Process p = processes.get(0);
                processes.remove();
                cont = 0; // Reset the counter
                os.interrupt(InterruptType.SCHEDULER_RQ_TO_CPU, p);
            }
        } else {
            // Case 3
            // If the quantums are done, the process will be removed from CPU.
            // If there are no processes in queue,
            cont++;
            if (cont == q) {
                // The program on the CPU will be sent to the RQ, an the counter will be reset
                if (processes.isEmpty()) {
                    os.interrupt(InterruptType.SCHEDULER_CPU_TO_RQ, null);
                    if (!processes.isEmpty()) { // If the process returned to the RQ, then it must return to the CPU. If
                                                // it is a MFQ, then the process may or may not return to this queue
                        Process p = processes.get(0);
                        processes.remove();
                        cont = 0; // Reset the counter
                        os.interrupt(InterruptType.SCHEDULER_RQ_TO_CPU, p);
                    }
                } else {
                    Process p = processes.get(0);
                    processes.remove();
                    os.interrupt(InterruptType.SCHEDULER_CPU_TO_RQ, p);
                }
                cont = 0;
            }

        }

    }

    @Override
    public void newProcess(boolean cpuEmpty) {
    } // Non-preemtive in this event

    @Override
    public void IOReturningProcess(boolean cpuEmpty) {
    } // Non-preemtive in this event

}
