/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ur_os;

import java.util.ArrayList;
import java.util.LinkedList;

/**
 *
 * @author prestamour
 */
public abstract class Scheduler {

    OS os;
    protected LinkedList<Process> processes;
    Scheduler MFQParent; // This is the parent for scheduling queues 

    public Scheduler(OS os) {
        this.os = os;
        processes = new LinkedList();
    }

    public Scheduler getMFQParent() {
        return MFQParent;
    }

    public void setMFQParent(Scheduler mFQParent) {
        MFQParent = mFQParent;
    }

    public void getNext() {
        getNext(false);
    }

    public abstract void getNext(boolean cpuEmpty);

    public abstract void newProcess(boolean cpuEmpty); // Implement for Preemtive schedulers

    public abstract void IOReturningProcess(boolean cpuEmpty); // Implement for Preemtive schedulers

    public void addProcess(Process p) {

        if (p.getState() == ProcessState.NEW) {
            newProcess(os.isCPUEmpty()); // If the process is NEW, let the scheduler defines what it will do to update
                                         // the queue to select the next
        } else if (p.getState() == ProcessState.IO) {
            IOReturningProcess(os.isCPUEmpty()); // If the process is returning from IO, let the scheduler defines what
                                                 // it will do to update the queue to select the next
        }

        p.setState(ProcessState.READY); // If the process comes from the CPU, just add it to the list
        processes.add(p);
        // priorityqueue.add(p)
    }

    public void update() {
        getNext(os.isCPUEmpty());
    }

    public Process getNextProcess(){
        return null;
    }

    public boolean isMFQQueueDowngraded(){ // If process have to go to a lower priority queue in MFQ
        return false;
    }

    public Process removeProcess(Process p) {
        Process temp = p;
        processes.remove(p);
        return temp;
    }

    public String toString() {
        StringBuffer sb = new StringBuffer();

        for (Process process : processes) {
            sb.append(process);
            sb.append("\n");
        }

        return sb.toString();
    }

    public double calculateTurnaroundTime(Process p) {
        return p.getTime_finished() - p.getTime_init();
    }

}
