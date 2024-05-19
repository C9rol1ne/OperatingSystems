/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ur_os.system;

import ur_os.memory.MemoryOperation;
import ur_os.process.ProcessMemoryManager;

/**
 *
 * @author super
 */
public class MemoryUnit {
    
    ProcessMemoryManager pmm;
    CPU cpu;
    
    

    public MemoryUnit(CPU cpu) {
        this.cpu = cpu;
    }
    
    public MemoryUnit(CPU cpu, ProcessMemoryManager pmm) {
        this.cpu = cpu;
        this.pmm = pmm;
    }
    
    public void setPMM(ProcessMemoryManager pmm){
        this.pmm = pmm;
    }
    
    public void executeMemoryOperation(MemoryOperation mop){
        if(mop.getType() == ur_os.memory.MemoryOperationType.STORE){
            int i=0;
        }
        int logAdd = mop.getLogicalAddress();
        int phyAdd = getPhysicalAddress(logAdd);
        switch(mop.getType()){
            case LOAD:
                cpu.load(phyAdd);
                break;
                
            case STORE:
                cpu.store(phyAdd,mop.getContent());
                break;
        }
    }
    
    public int getPhysicalAddress(int logicalAddress){
        return pmm.getPhysicalAddress(logicalAddress);
    }
    
    
}
