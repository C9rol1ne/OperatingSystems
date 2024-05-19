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
public class SJF_NP extends Scheduler{

    
    SJF_NP(OS os){
        super(os);
    }
    
   
    @Override
    public void getNext(boolean cpuEmpty) {
       if(!processes.isEmpty() && cpuEmpty)
        {   
            
            int min_r = 9999;
            int temp_r;
            Process p = null;
            
            for (Process process : processes) {
                if(process.isCurrentBurstCPU()){
                    temp_r = process.getRemainingTimeInCurrentBurst();
                    if(temp_r < min_r){
                        min_r = temp_r;
                        p = process;
                    }else if(temp_r == min_r){//If there is a tie
                        if(p.getPid() < process.getPid()){ //Priority to larger PID
                            min_r = temp_r;
                            p = process;
                        }
                    }
                }
            }
            
            processes.remove(p);
            os.interrupt(InterruptType.SCHEDULER_RQ_TO_CPU, p);
        }
        
    }
    
    @Override
    public void newProcess(boolean cpuEmpty) {} //Non-preemtive

    @Override
    public void IOReturningProcess(boolean cpuEmpty) {} //Non-preemtive
    
}
