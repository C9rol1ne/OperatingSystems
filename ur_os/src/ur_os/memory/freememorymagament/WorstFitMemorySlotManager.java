/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ur_os.memory.freememorymagament;

import java.util.Collections;
import java.util.LinkedList;

/**
 *
 * @author super
 */
public class WorstFitMemorySlotManager extends FreeMemorySlotManager{
    
    @Override
    public MemorySlot getSlot(int size) {
        MemorySlot m = null, largestSlot;
        LinkedList<MemorySlot> freeMemorySlots = this.getFreeMemorySlotsList();

        if (freeMemorySlots.size() == 0){
            throw new IllegalArgumentException("Not enough memory to satisfy request");
        } else {
            Collections.sort(freeMemorySlots);
            largestSlot = freeMemorySlots.get(freeMemorySlots.size()-1);
            if (largestSlot.getSize() >= size){
                // Create a new MemorySlot with the requested size
                m = new MemorySlot(largestSlot.getBase(), size);
                if (largestSlot.getSize() == size) { // perfect fit case
                    freeMemorySlots.remove(largestSlot); // so there's no slot in freeMemorySlots with size 0
                } else {
                    // Adjust the original slot
                    largestSlot.setBase(largestSlot.getBase() + size);
                    largestSlot.setSize(largestSlot.getSize() - size);
                }
                return m;

            } else {
                throw new IllegalArgumentException("Not enough memory to satisfy request");
            }
        }        
    }    
}
