/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ur_os.memory;

/**
 *
 * @author super
 */
public class MemoryOperation {
    MemoryOperationType type;
    int logicalAddress;
    byte content;

    public MemoryOperation() {
        type = MemoryOperationType.LOAD;
        logicalAddress = 0;
        content = 0;
    }
    
    public MemoryOperation(MemoryOperationType type, int logicalAddress, byte content) {
        this.type = type;
        this.logicalAddress = logicalAddress;
        this.content = content;
    }

    public MemoryOperationType getType() {
        return type;
    }

    public void setType(MemoryOperationType type) {
        this.type = type;
    }

    public int getLogicalAddress() {
        return logicalAddress;
    }

    public void setLogicalAddress(int logicalAddress) {
        this.logicalAddress = logicalAddress;
    }

    public byte getContent() {
        return content;
    }

    public void setContent(byte content) {
        this.content = content;
    }
    
    
    public String toString(){
        return type + "," + logicalAddress + ","+content;
    }
    
    
}
