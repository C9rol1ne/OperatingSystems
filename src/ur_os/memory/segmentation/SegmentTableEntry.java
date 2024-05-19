/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ur_os.memory.segmentation;

import ur_os.memory.freememorymagament.MemorySlot;

/**
 *
 * @author super
 */
public class SegmentTableEntry {
    
    int base;
    int limit;
    boolean dirty;
    MemorySlot m;

    public SegmentTableEntry(int base, int limit) {
        this.base = base;
        this.limit = limit;
        dirty = false;
    }
    
    public SegmentTableEntry(MemorySlot m) {
        this.base = m.getBase();
        this.limit = m.getSize();
        dirty = false;
    }
    
    public void setSegment(int base, int limit){
        this.base = base;
        this.limit = limit;
    }
    
    public void setMemorySlot(MemorySlot m){
        setSegment(m.getBase(), m.getSize());
        this.m = m;
    }
    
    public MemorySlot getMemorySlot(){
        return m;
    }
    
    public void markDirty(){
        dirty = true;
    }

    public int getBase() {
        return base;
    }

    public void setBase(int base) {
        this.base = base;
    }

    public int getLimit() {
        return limit;
    }

    public void setLimit(int limit) {
        this.limit = limit;
    }
    
    public boolean isDirty(){
        return dirty;
    }
    
    @Override
    public String toString(){
        return "Base: "+base+" Limit: "+limit+" Dirty: "+dirty;
    }
    
    
}
