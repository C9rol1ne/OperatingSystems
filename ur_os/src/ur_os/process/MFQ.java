/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ur_os.process;

import java.util.ArrayList;
import java.util.Arrays;

import ur_os.system.InterruptType;
import ur_os.system.OS;

/**
 *
 * @author prestamour
 */
public class MFQ extends Scheduler {

    int currentScheduler;

    private ArrayList<Scheduler> schedulers;

    MFQ(OS os) {
        super(os);
        currentScheduler = -1;
        schedulers = new ArrayList();
    }

    MFQ(OS os, Scheduler... s) { // Received multiple arrays
        this(os);
        schedulers.addAll(Arrays.asList(s));
        if (s.length > 0)
            currentScheduler = 0;
    }

    @Override
    public void addProcess(Process p) {
        if (p.getState() == ProcessState.NEW || p.getState() == ProcessState.IO) {
            p.setState(ProcessState.READY); // If the process comes from the CPU, just add it to the list
            schedulers.get(0).addProcess(p);
            p.setCurrentScheduler(0);
        } else if (p.getState() == ProcessState.CPU) {
            int tempcurrent = p.getCurrentScheduler();
            if (tempcurrent < schedulers.size() - 1) { // If the scheduler is not the last one, go to the next one
                tempcurrent++;
            }
            schedulers.get(tempcurrent).addProcess(p);
            p.setCurrentScheduler(tempcurrent);
            p.setState(ProcessState.READY);
        }

    }

    void defineCurrentScheduler() {
        int i = 0;
        boolean found = false;
        while (i < schedulers.size() && !found) {
            if (schedulers.get(i).isEmpty()) {
                i++;
            } else {
                found = true;
            }
        }
        if (found) {
            if (!schedulers.get(i).isEmpty()) {
                this.currentScheduler = i;
            } else
                System.out.println("Error in planner!");
        }
    }

    @Override
    public void getNext(boolean cpuEmpty) {

        if (!cpuEmpty) {
            Process tempp = os.getProcessInCPU();
            currentScheduler = os.getProcessInCPU().getCurrentScheduler();
            schedulers.get(currentScheduler).getNext(cpuEmpty);

            if (!os.isCPUEmpty() && tempp != os.getProcessInCPU()) {
                int temp = currentScheduler;
                defineCurrentScheduler();
                if (currentScheduler < temp) {
                    tempp = os.getProcessInCPU();
                    schedulers.get(tempp.getCurrentScheduler()).returnProcess(tempp);
                    os.removeProcessFromCPU();
                    schedulers.get(currentScheduler).getNext(cpuEmpty);
                }
            }
        }

        if (os.isCPUEmpty()) {
            defineCurrentScheduler();
            schedulers.get(currentScheduler).getNext(os.isCPUEmpty());
        }

    }

    @Override
    public void newProcess(boolean cpuEmpty) {
    } // Non-preemtive in this event

    @Override
    public void IOReturningProcess(boolean cpuEmpty) {
    } // Non-preemtive in this event

}