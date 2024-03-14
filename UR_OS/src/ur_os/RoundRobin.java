/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ur_os;

/**
 *
 * @author prestamour
 */
public class RoundRobin extends Scheduler {

    int quantum;
    int executedCyclesInBurst;

    RoundRobin(OS os) {
        super(os);
        quantum = 4; // default q
        executedCyclesInBurst = 0;
    }

    RoundRobin(OS os, int q) {
        this(os);
        this.quantum = q;
    }

    @Override

    public void getNext(boolean cpuEmpty) {
        if (processes.isEmpty()) {
            if (cpuEmpty) {
                return;
            }

            executedCyclesInBurst++;
            if (executedCyclesInBurst >= quantum) {
                executedCyclesInBurst = 0;
            }

            return;
        }

        if (cpuEmpty) {
            Process first = processes.remove(0);
            os.interrupt(InterruptType.SCHEDULER_RQ_TO_CPU, first);
            executedCyclesInBurst = 1;
        } else {
            // cpu busy and RQ has processes
            if (executedCyclesInBurst < quantum) {
                executedCyclesInBurst++;
            } else {
                os.interrupt(InterruptType.SCHEDULER_CPU_TO_RQ, processes.remove(0));
                executedCyclesInBurst = 1;
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
