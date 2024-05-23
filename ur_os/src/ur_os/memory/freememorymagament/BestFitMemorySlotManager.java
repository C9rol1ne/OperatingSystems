/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ur_os.memory.freememorymagament;

import java.util.Collection;
import java.util.Collections;
import java.util.LinkedList;

/**
 *
 * @author super
 */
public class BestFitMemorySlotManager extends FreeMemorySlotManager{
    
    @Override
    public MemorySlot getSlot(int size) {
        MemorySlot m = null;
        LinkedList<MemorySlot> freeMemorySlots = this.getFreeMemorySlotsList();
        if (freeMemorySlots.size() == 0){
            throw new IllegalArgumentException("Not enough memory to satisfy request");
        } else {
            Collections.sort(freeMemorySlots);
            for (MemorySlot slot : freeMemorySlots) {
                if (slot.getSize() >= size){
                    // Create a new MemorySlot with the requested size
                    m = new MemorySlot(slot.getBase(), size);
                    if (slot.getSize() == size) { // perfect fit case
                        freeMemorySlots.remove(slot); // so there's no slot in freeMemorySlots with size 0
                    } else {
                        // Adjust the original slot
                        slot.setBase(slot.getBase() + size);
                        slot.setSize(slot.getSize() - size);
                    }
                    return m;
                }
            }
        }
        //m is the slot that the method will return to be assigned to the process.
        //The original selected slot needs to be updated with the amount of memory that was taken from it to be assigned to the process.
        //Example: if the selected slot had 190 bytes and the process requested 120 bytes, then m will be a slot of size 120 with a certain base,
        //and the original slot will have the 70 remaining bytes, with either the base or the size modified.
        //Note: Think of what will happen if the process request fits perfectly on a memory slot.        
        throw new IllegalArgumentException("Not enough memory to satisfy request");
    }
}
