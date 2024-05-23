/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ur_os.memory.freememorymagament;

import java.util.ArrayList;
import java.util.LinkedList;

import ur_os.process.Process;

/**
 *
 * @author super
 */
public class FirstFitMemorySlotManager extends FreeMemorySlotManager {

    @Override
    public MemorySlot getSlot(int size) {
        LinkedList<MemorySlot> freeMemorySlots = this.getFreeMemorySlotsList();

        for (MemorySlot slot : freeMemorySlots) {
            if (slot.getSize() >= size) { // found hole big enough
                // Create a new MemorySlot with the requested size
                MemorySlot allocatedSlot = new MemorySlot(slot.getBase(), size);

                if (slot.getSize() == size) { // perfect fit case
                    freeMemorySlots.remove(slot); // so there's no slot in freeMemorySlots with size 0
                } else {
                    // Adjust the original slot
                    slot.setBase(slot.getBase() + size);
                    slot.setSize(slot.getSize() - size);
                }
                return allocatedSlot;
            }
        }
        throw new IllegalArgumentException("Not enough memory to satisfy request");
    }

}