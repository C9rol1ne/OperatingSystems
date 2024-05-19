/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ur_os.process;

import ur_os.memory.MemoryOperation;
import ur_os.memory.MemoryOperationList;
import ur_os.memory.contiguous.PMM_Contiguous;
import static ur_os.process.ProcessMemoryManagerType.PAGING;

/**
 *
 * @author super
 */
public class Process implements Comparable{
    public static final int NUM_CPU_CYCLES = 3;
    public static final int MAX_CPU_CYCLES = 10;
    public static final int MAX_IO_CYCLES = 10;
    int pid;
    int time_init;
    int time_finished;
    ProcessBurstList pbl;
    ProcessState state;
    int currentScheduler;

    ProcessMemoryManager pmm;
    MemoryOperationList mol;
    
    public Process() {
        this(false);
    }
    
    public Process(boolean auto) {
        this(false, false, -1, 0, null);
    }
    
    public Process(int pid, int time_init) {
        this(false, false, pid, time_init, null);
    }
    
    public Process(int pid, int time_init, boolean autoproc) {
        this(autoproc, false, pid, time_init, null);
    }
    
    public Process(int pid, int time_init, ProcessMemoryManager pmm) {
        this(false, true, pid, time_init, pmm);
    }
    
    public Process(boolean autoProc, boolean autoMem, int pid, int time_init, ProcessMemoryManager pmm) {
        this.pid = pid;
        this.time_init = time_init;
        time_finished = -1;
        
        if(pmm == null)
            this.pmm = new PMM_Contiguous();
        else
            this.pmm = pmm;
        
        pbl = new ProcessBurstList();
        mol = new MemoryOperationList();
        
        if(autoProc){
            pbl.generateRandomBursts(NUM_CPU_CYCLES, MAX_CPU_CYCLES, MAX_IO_CYCLES);
            //pbl.generateSimpleBursts(); //Generates process with 3 bursts (CPU, IO, CPU) with 5 cycles each
        }
        
        if(autoMem){
            mol.generateSimpleMemoryOperations(pmm.getSize()); //Generate 10 random Memory Operations
        }
        
        state = ProcessState.NEW;
        currentScheduler = 0;
        
        
    }
    
    public Process(Process p) {
        this.pid = p.pid;
        this.time_init = p.time_init;
        this.pbl = new ProcessBurstList(p.getPBL());
        this.pmm = p.pmm;
        /*switch(SystemOS.PMM){
            case PAGING:
                this.pmm = new PMM_Paging((PMM_Paging)p.pmm);
                break;
                
            case CONTIGUOUS:
                this.pmm = new PMM_Contiguous((PMM_Contiguous)p.pmm);
                break;
        }*/
        
    }

    public ProcessMemoryManager getPMM() {
        return pmm;
    }
    
    public void setPMM(ProcessMemoryManager pmm){
        this.pmm = pmm;
        mol.generateSimpleMemoryOperations(pmm.getSize()); //Generate 10 random Memory Operations
    }

    public boolean advanceBurst(){
        return pbl.advanceBurst();
    }
    
    public boolean isFinished(){
        return pbl.isFinished();
    }

    public void setTime_finished(int time_finished) {
        this.time_finished = time_finished;
    }
    
    public void addBurst(ProcessBurst pb){
        pbl.addBurst(pb);
    }
    
    public void addMemoryOperation(MemoryOperation m){
        mol.add(m);
    }
    
    public MemoryOperation getNextMemoryOperation(){
        return mol.getNext();
    }
    
    public int getPid() {
        return pid;
    }

    public void setPid(int pid) {
        this.pid = pid;
    }

    public int getTime_init() {
        return time_init;
    }

    public void setTime_init(int time_init) {
        this.time_init = time_init;
    }
    
    public ProcessBurstList getPBL(){
        return pbl;
    }

    public ProcessState getState() {
        return state;
    }

    public int getTime_finished() {
        return time_finished;
    }

    public int getTotalExecutionTime(){
        return pbl.getTotalExecutionTime();
    }
    
    public void setState(ProcessState state) {
        this.state = state;
    }

    public int getSize() {
        return pmm.getSize();
    }
 
    public int getRemainingTimeInCurrentBurst(){
        return pbl.getRemainingTimeInCurrentBurst();
    }
    
    public boolean isCurrentBurstCPU(){
        return pbl.isCurrentBurstCPU();
    }

    public ProcessBurstList getPbl() {
        return pbl;
    }

    public void setPbl(ProcessBurstList pbl) {
        this.pbl = pbl;
    }

    public int getCurrentScheduler() {
        return currentScheduler;
    }

    public void setCurrentScheduler(int currentScheduler) {
        this.currentScheduler = currentScheduler;
    }
    
    
    public String toString(){
        StringBuilder sb = new StringBuilder();
        sb.append("PID: ");
        sb.append(pid);
        sb.append(" Size: ");
        sb.append(pmm.getSize());
        sb.append(" t_init: ");
        sb.append(time_init);
        sb.append("\nPMM:\n");
        sb.append(pmm.toString());
        sb.append("\nPBL:\n");
        sb.append(pbl.toString());
        
        return sb.toString();
    }
    
    

    @Override
    public int compareTo(Object o) {
        if(o instanceof Process){
            Process p = (Process)o;
            return this.getPid() - p.getPid();
        }
        
        return -1;
    }
    
    @Override
    public boolean equals(Object o){
    
        if(o instanceof Process){
            Process p = (Process)o;
            return this.getPid() == p.getPid();
        }
        
        return false;
        
    }
    
    
}
