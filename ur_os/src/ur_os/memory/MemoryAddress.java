/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ur_os.memory;

/**
 *
 * @author super
 */
public class MemoryAddress {
    
    int division;
    int offset;

    public MemoryAddress() {
        this(0,0);
    }
    
    public MemoryAddress(int division, int offset) {
        this.division = division;
        this.offset = offset;
    }

    public int getDivision() {
        return division;
    }

    public void setDivision(int division) {
        this.division = division;
    }

    public int getOffset() {
        return offset;
    }

    public void setOffset(int offset) {
        this.offset = offset;
    }
    
    public int getAddress(){
        return this.division+this.offset;
    }
    
}
