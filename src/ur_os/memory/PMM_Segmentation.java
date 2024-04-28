/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ur_os.memory;

import java.util.ArrayList;

import ur_os.process.ProcessMemoryManager;
import ur_os.process.ProcessMemoryManagerType;

/**
 *
 * @author super
 */
public class PMM_Segmentation extends ProcessMemoryManager{
    
    SegmentTable st;

    public PMM_Segmentation(int processSize) {
        super(ProcessMemoryManagerType.SEGMENTATION,processSize);
        st = new SegmentTable(processSize);
    }

    public PMM_Segmentation(SegmentTable st) {
        this.st = st;
    }
    
    public PMM_Segmentation(PMM_Segmentation pmm) {
        super(pmm);
        if(pmm.getType() == this.getType()){
            this.st = new SegmentTable(pmm.getSt());
        }else{
            System.out.println("Error - Wrong PMM parameter");
        }
    }

    public SegmentTable getSt() {
        return st;
    }
    
    public void addSegment(int base, int limit){
        st.addSegment(base, limit);
    }
    
    public SegmentTableEntry getSegment(int i){
        return st.getSegment(i);
    }
    
    public MemoryAddress getSegmentMemoryAddressFromLocalAddress(int locAdd){
        
        return st.getSegmentMemoryAddressFromLocalAddress(locAdd);
    }
    
    public MemoryAddress getPhysicalMemoryAddressFromLogicalMemoryAddress(MemoryAddress m){
        
        return st.getPhysicalMemoryAddressFromLogicalMemoryAddress(m);
    }

    
   @Override
    public int getPhysicalAddress(int logicalAddress){
        
        return -1;
    }
    
     @Override
    public String toString(){
        return st.toString();
    }
    
}
