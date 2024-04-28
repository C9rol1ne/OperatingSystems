/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ur_os.system;

import ur_os.memory.PMM_Contiguous;
import ur_os.memory.Memory;
import ur_os.memory.FreeFramesList;
import ur_os.memory.PMM_Paging;
import ur_os.process.ProcessMemoryManager;
import ur_os.process.ProcessMemoryManagerType;
import ur_os.process.Process;
import ur_os.process.ReadyQueue;
import ur_os.process.ProcessState;
import java.util.LinkedList;
import java.util.Random;
import ur_os.memory.PMM_Segmentation;
import static ur_os.system.InterruptType.SCHEDULER_CPU_TO_RQ;


/**
 *
 * @author super
 */
public class OS {
    
    ReadyQueue rq;
    IOQueue ioq;
    private static int process_count = 0;
    SystemOS system;
    CPU cpu;
    Memory memory;
    FreeFramesList freeFrames;
    Random r;
    
    public OS(SystemOS system, CPU cpu, IOQueue ioq, Memory memory){
        rq = new ReadyQueue(this);
        this.ioq = ioq;
        this.system = system;
        this.cpu = cpu;
        this.memory = memory;
        freeFrames = new FreeFramesList(memory.getSize());
        r = new Random();
    }
    
    
    public void update(){
        rq.update();
    }
    
    public boolean isCPUEmpty(){
        return cpu.isEmpty();
    }
    
    public Process getProcessInCPU(){
        return cpu.getProcess();
    }
    
    public void interrupt(InterruptType t, Process p){
        
        switch(t){
        
            case CPU: //It is assumed that the process in CPU is done and it has been removed from the cpu
                if(p.isFinished()){//The process finished completely
                    p.setState(ProcessState.FINISHED);
                    p.setTime_finished(system.getTime());
                }else{
                    ioq.addProcess(p);
                }
            break;
            
            case IO: //It is assumed that the process in IO is done and it has been removed from the queue
                rq.addProcess(p);
            break;
            
            case SCHEDULER_CPU_TO_RQ:
                //When the scheduler is preemptive and will send the current process in CPU to the Ready Queue
                Process temp = cpu.extractProcess();
                rq.addProcess(temp);
                if(p != null){
                    cpu.addProcess(p);
                }
                
            break;
            
            
            case SCHEDULER_RQ_TO_CPU:
                //When the scheduler defined which process will go to CPU
                cpu.addProcess(p);
                
            break;
            
            
        }
        
    }
    
    public void removeProcessFromCPU(){
        cpu.removeProcess();
    }
    
    public void create_process(){
        Process p = new Process(process_count++, system.getTime());
        rq.addProcess(p);
        ProcessMemoryManager pmm;
        if(SystemOS.PMM == ProcessMemoryManagerType.PAGING){
            pmm = new PMM_Paging(r.nextInt(SystemOS.MAX_PROC_SIZE));
            assignFramesToProcess(p);
        }else{
            pmm = new PMM_Contiguous(p.getPid()*SystemOS.MAX_PROC_SIZE, r.nextInt(SystemOS.MAX_PROC_SIZE));
        }
        
    }
    
    public void create_process(Process p){
        p.setPid(process_count++);
        rq.addProcess(p);
        if(SystemOS.PMM == ProcessMemoryManagerType.PAGING){
            assignFramesToProcess(p);
        }else if(SystemOS.PMM == ProcessMemoryManagerType.SEGMENTATION){
            assignSegmentsToMemory(p);
        } 
    }
    
    public void assignSegmentsToMemory(Process p){
        PMM_Segmentation pmm = (PMM_Segmentation)p.getPMM();
        int base = p.getPid()*SystemOS.MAX_PROC_SIZE;
        int limit = 0;
        int ptSize = pmm.getSt().getSize();
        for (int i = 0; i < ptSize; i++) {
            limit = pmm.getSegment(i).getLimit();
            pmm.getSegment(i).setBase(base + limit);
            base += limit;
        }
    }
    
    public void assignFramesToProcess(Process p){
        PMM_Paging pmm = (PMM_Paging)p.getPMM();
        int ptSize = pmm.getPt().getSize();
        if(ptSize <= freeFrames.getSize()){
            for (int i = 0; i < ptSize; i++) {
                pmm.addFrameID(freeFrames.getFrame());
            }
        }else{
            System.out.println("Error - Process size larger than available memory");
        }
        
    }
    
    public void showProcesses(){
        System.out.println("Process list:");
        System.out.println(rq.toString());
    }
    
    public Memory getMemory(){
        return memory;
    }
    
    public byte load(int physicalAddress){
        byte b = memory.get(physicalAddress);
        System.out.println("The obtained data is: "+b);
        return b;
    }
    
    public void store(int physicalAddress, byte content){
        memory.set(physicalAddress, content);
        System.out.println("The data "+memory.get(physicalAddress)+" is stored in: "+physicalAddress);
    }
    
    public SimulationType getSimulationType() {
        return system.getSimulationType();
    }
    
}
