/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ur_os.process;

/**
 *
 * @author super
 */
public abstract class ProcessMemoryManager {
    
    ProcessMemoryManagerType type;
    int process_size;

    public ProcessMemoryManager() {
        this.type = ProcessMemoryManagerType.CONTIGUOUS;
        process_size = 100;
    }
    
    public ProcessMemoryManager(ProcessMemoryManagerType type) {
        this.type = type;
        process_size = 100;
    }
    
    public ProcessMemoryManager(ProcessMemoryManagerType type, int process_size) {
        this.type = type;
        this.process_size = process_size;
    }
    
    public ProcessMemoryManager(ProcessMemoryManager pmm) {
        this.type = pmm.type;
        this.process_size = pmm.process_size;
    }
    
    
    public ProcessMemoryManagerType getType(){
        return type;
    }
 
    public int getSize(){
        return process_size;
    }
    
    public abstract int getPhysicalAddress(int logicalAddress);
    
    
}
