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

    protected LinkedList<MemorySlot> getList() { // Added getter method for list

        return list;
    }

    private void returnMemorySlot(MemorySlot m) {

        int i = 0;
        // Find the slot with a higher base address than the one inserted
        while (list.get(i).getBase() < m.getBase()) {
            i++;
        }

        // If it is not first slot
        if (i > 0) {
            i--; // Go to the previous slot

            // If the returned slot is contiguous to the previous slot, they must be joined
            if (list.get(i).getEnd() == m.getBase() - 1) {
                list.get(i).addSlot(m);
                // Keep checking if there are more slots to fuse in the list
                while (i < list.size() - 1) {
                    if (list.get(i).getEnd() == list.get(i + 1).getBase() - 1) {
                        list.get(i).addSlot(list.get(i + 1));
                        list.remove(list.get(i + 1));
                    } else {
                        i++;
                    }
                }
            } else {
                // If they are not connected, then the slot will be added after the one with
                // lower base
                list.add(i + 1, m);
            }

        } else {
            // The base is lower than the first slot, thus this should be in the begining in
            // the list.
            // If the returned slot is contiguous to the first slot, they must be joined
            if (m.getEnd() == list.getFirst().getBase() - 1) {
                list.getFirst().addSlot(m);
            } else {
                // If they are not connected, then the slot will be added first in the list
                list.addFirst(m);
            }
        }

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
