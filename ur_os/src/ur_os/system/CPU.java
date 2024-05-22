/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ur_os.system;

import ur_os.memory.MemoryOperation;
import ur_os.process.Process;
import ur_os.process.ProcessState;


/**
 *
 * @author super
 */
public class CPU {
    
    Process p;
    OS os;
    MemoryUnit mu;
    
    
    public CPU(){
        this(null);
    }
    
    public CPU(OS os){
        this.os = os;
        this.mu = new MemoryUnit(this);
    }
    
    public void setOS(OS os){
        this.os = os;
    }
    
    public void addProcess(Process p){
        this.p = p;
        p.setState(ProcessState.CPU);
        mu.setPMM(p.getPMM());
    }
    
    public Process getProcess(){
        return p;
    }
    
    public boolean isEmpty(){
        return p == null;
    }
    
    public void update(){
        if(!isEmpty())
            advanceBurst();
    }
    
    public void advanceBurst(){
        advanceMemoryOperation(); //Join CPU execution with Memory Operations... later they will be independent
        
        if(p.advanceBurst()){
            Process tempp = p;
            removeProcess();
            os.interrupt(InterruptType.CPU, tempp);
        }
    }
    
    public void advanceMemoryOperation(){
        MemoryOperation mop = p.getNextMemoryOperation();
        if(mop != null){
            System.out.println("Process "+p.getPid()+" is executing "+mop);
            mu.executeMemoryOperation(mop);
        }
    }
    
    public byte load(int physicalAddress) {
        return os.load(physicalAddress);
    }

    public void store(int physicalAddress, byte content) {
        os.store(physicalAddress, content);
    }
    
    public void removeProcess(){
        p = null;
    }
    
    public Process extractProcess(){
        Process temp = p;
        p = null;
        return temp;
    }
    
    
    public String toString(){
        if(!isEmpty())
            return "CPU: "+p.toString();
        else
            return "CPU: Empty";
    }
    
   
    
}
