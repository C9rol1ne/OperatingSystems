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
        quantum = 3; // defult q
        executedCyclesInBurst = 0;
    }

    RoundRobin(OS os, int q) {
        this(os);
        this.quantum = q;
    }

    void resetCounter(){
        executedCyclesInBurst = 0;
    }
    
    @Override

    public void getNext(boolean cpuEmpty) {
        System.out.print("////////");
        System.out.print(processes.size());
        if (processes.isEmpty()) {
            if (cpuEmpty) { // CPU is not empty but there are no processes in the ready queue
                return;
            }

            executedCyclesInBurst++;
            if (MFQParent == null){
                if (executedCyclesInBurst >= quantum) {
                    executedCyclesInBurst = 0;
                }    
            }
            

            return;
        }

        if (cpuEmpty) {
            Process first = processes.remove(0);
            os.interrupt(InterruptType.SCHEDULER_RQ_TO_CPU, first);
            executedCyclesInBurst = 1;
        } else {
            // cpu busy
            // System.out.print("111111111111");
            if (executedCyclesInBurst < quantum) {
                // System.out.print("22222222");
                executedCyclesInBurst++;
            } else {                
                os.interrupt(InterruptType.SCHEDULER_CPU_TO_RQ, processes.remove(0));                            
                executedCyclesInBurst = 1;
            }
        }
        // System.out.print("\n");
        // System.out.print(executedCyclesInBurst);
    }    

    @Override
    public boolean isMFQQueueDowngraded(){
        System.out.print("------------------\n");
        System.out.print(executedCyclesInBurst);
        if (executedCyclesInBurst >= quantum){
            executedCyclesInBurst = 1;
            return true;
        } else {
            return false;
        }        
    }

    @Override
    public Process getNextProcess(){
        return processes.remove(0);
    }

    @Override
    public void newProcess(boolean cpuEmpty) {
    } // Non-preemtive in this event

    @Override
    public void IOReturningProcess(boolean cpuEmpty) {
    } // Non-preemtive in this event

}
