/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ur_os.memory.freememorymagament;

import ur_os.memory.segmentation.SegmentTableEntry;
import ur_os.memory.segmentation.PMM_Segmentation;
import ur_os.memory.contiguous.PMM_Contiguous;
import java.util.ArrayList;
import java.util.LinkedList;
import ur_os.process.Process;
import ur_os.process.ProcessMemoryManager;
import static ur_os.process.ProcessMemoryManagerType.CONTIGUOUS;
import static ur_os.process.ProcessMemoryManagerType.SEGMENTATION;

/**
 *
 * @author super
 */
public abstract class FreeMemorySlotManager extends FreeMemoryManager {

    LinkedList<MemorySlot> list;

    public FreeMemorySlotManager() {
        list = new LinkedList();
        list.add(new MemorySlot(0, ur_os.system.SystemOS.MEMORY_SIZE));
    }

    public abstract MemorySlot getSlot(int size);

    protected LinkedList<MemorySlot> getFreeMemorySlotsList() { // Added getter method for list

        return list;
    }

    public void fuseSlots() {
        int tam = list.size();
        for (int i = 0; i < tam - 1; i++) {
            if (list.get(i).getEnd() + 1 == list.get(i + 1).getBase()) {// If the slota are contiguous, fuse then
                list.get(i).addSlot(list.get(i + 1));
                list.remove(list.get(i + 1));
                tam--;
                i--;
            }
        }
        for (int i = 0; i < tam; i++) {// Check if the are slots with 0 size, and remove them
            if (list.get(i).getSize() == 0) {
                list.remove(i);
                tam--;
                i--;
            }
        }

    }

    private void returnMemorySlot(MemorySlot m) {

        int i = 0;
        // Find the slot with a higher base address than the one inserted
        // if (list.size() == 0){
        //     throw new IllegalArgumentException("Not enough memory to satisfy request");
        // }
        while (i < list.size() && list.get(i).getBase() < m.getBase()) {
            i++;
        }
        // If it is not the first one, then change the index to have the previous slot
        if (i > 0) {
            i--;
        }

        if (i == 0 && list.get(i).getBase() > m.getBase()) {// If the slot is the first one and the incoming slot is
                                                            // indeed the lower one
            list.addFirst(m);
        } else if (i == list.size() - 1) {// If the slot is the last one
            list.getLast().addSlot(m);
        } else {
            list.add(i + 1, m); // Insert the slot where it is supose to be
        }

        fuseSlots(); // Check all the slots and fuse them if possible

    }

    @Override
    public void reclaimMemory(Process p) {
        ProcessMemoryManager pmm = p.getPMM();
        switch (pmm.getType()) {
            case SEGMENTATION:
                PMM_Segmentation pmms = (PMM_Segmentation) p.getPMM();
                ArrayList<SegmentTableEntry> list = pmms.getSt().getTable();
                for (SegmentTableEntry ste : list) {
                    this.returnMemorySlot(ste.getMemorySlot());
                }

                break;
            default:
            case CONTIGUOUS:
                PMM_Contiguous pmmc = (PMM_Contiguous) p.getPMM();
                this.returnMemorySlot(pmmc.getMemorySlot());
                break;
        }
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (MemorySlot memorySlot : list) {
            sb.append(memorySlot.toString());
            sb.append("\n");
        }
        return sb.toString();
    }

    public int getSize() {
        return this.list.size();
    }

}
