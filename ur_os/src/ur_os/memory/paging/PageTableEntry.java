/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ur_os.memory.paging;

/**
 *
 * @author super
 */
public class PageTableEntry implements Comparable{
    
    int frameId;
    boolean dirty;

    public PageTableEntry(int frameId) {
        this.frameId = frameId;
        dirty = false;
    }
    
    public void markDirty(){
        dirty = true;
    }
    
    public int getFrameId(){
        return frameId;
    }
    
    public void setFrameId(int frame){
        frameId = frame;
    }
    
    public boolean isDirty(){
        return dirty;
    }
    
    @Override
    public String toString(){
        return "Frame: "+frameId+" Dirty: "+dirty;
    }

    @Override
    public int compareTo(Object o) {
        if(o instanceof PageTableEntry){
            PageTableEntry p = (PageTableEntry)o;
            
            return this.frameId - p.frameId;
            
        }
            
        return -999;
    }
    
    
}
