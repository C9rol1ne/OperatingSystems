/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ur_os.memory.freememorymagament;

/**
 *
 * @author super
 */
public class MemorySlot{
    
    int base;
    int size;
    
    public MemorySlot(){
        base = 0;
        size = 0;
    }

    public MemorySlot(int base, int size) {
        this.base = base;
        this.size = size;
    }
    
    public MemorySlot(MemorySlot m) {
        this.base = m.base;
        this.size = m.size;
    }

    public int getBase() {
        return base;
    }

    public void setBase(int base) {
        this.base = base;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public int getEnd() {
        return base + size - 1;
    }

    public int getRemainder(int request){
        return size - request;
    }
    
    public boolean canContain(int request){
        return size >= request;
    }
    
    public MemorySlot assignMemory(int request){
        MemorySlot m = null;
        if(canContain(request)){
            m = new MemorySlot(base, request);
            base = base + request;
            size = size - request;
        }
        return m;
    }
    
    public void addSlot(MemorySlot m){
        size = size + m.getSize();
    }
    
    public boolean inNull(){
        return size == 0;
    }
    
    @Override
    public String toString(){
        return "Base: "+base+" Size: "+size;
    }
    
    @Override
    public boolean equals(Object o){
    
        if(o instanceof MemorySlot){
            MemorySlot m = (MemorySlot)o;
            return base == m.base && size == m.size;
        }
        
        return false;
    }
    
}
