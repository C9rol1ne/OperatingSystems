/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ur_os.memory.segmentation;

import java.util.ArrayList;

import ur_os.memory.MemoryAddress;
import ur_os.process.ProcessMemoryManager;
import ur_os.process.ProcessMemoryManagerType;

/**
 *
 * @author super
 */
public class PMM_Segmentation extends ProcessMemoryManager {

    SegmentTable st;
    private ArrayList<Integer> cumulativeLogAddSpace;

    public PMM_Segmentation(int processSize) {
        super(ProcessMemoryManagerType.SEGMENTATION, processSize);
        st = new SegmentTable(processSize);
        cumulativeLogAddSpace = getCumulativeLogicalAddressSpace(st); // Calculate CLAS
    }

    public PMM_Segmentation(SegmentTable st) {
        this.st = st;
    }

    public PMM_Segmentation(PMM_Segmentation pmm) {
        super(pmm);
        if (pmm.getType() == this.getType()) {
            this.st = new SegmentTable(pmm.getSt());
        } else {
            System.out.println("Error - Wrong PMM parameter");
        }
    }

    public SegmentTable getSt() {
        return st;
    }

    public void addSegment(int base, int limit) {
        st.addSegment(base, limit);
    }

    public SegmentTableEntry getSegment(int i) {
        return st.getSegment(i);
    }

    public MemoryAddress getSegmentMemoryAddressFromLocalAddress(int locAdd) {

        return st.getSegmentMemoryAddressFromLocalAddress(locAdd);
    }

    public int getSegmentNumber(int logAdd) { // helper function

        for (int i = 0; i < st.getSize(); i++) {
            if (logAdd < cumulativeLogAddSpace.get(i)) {
                return i;
            }
        }
        return -1; // handle exception
    }

    public int getOffset(int segmentNumber, int logAdd) { // helper function

        int logicalBase;
        int offset;

        if (segmentNumber == 0) {
            logicalBase = 0;
        } else {
            logicalBase = cumulativeLogAddSpace.get(segmentNumber - 1);
        }
        offset = logAdd - logicalBase;

        return offset;

    }

    public ArrayList getCumulativeLogicalAddressSpace(SegmentTable S) {

        ArrayList<Integer> CLAS = new ArrayList();

        int limit = st.getSegment(0).getLimit();
        CLAS.add(limit); // initialize CLAS

        for (int i = 1; i < st.getSize(); i++) {
            SegmentTableEntry segment = st.getSegment(i);
            int length = segment.getLimit();
            int clas = CLAS.get(i - 1);
            CLAS.add(length + clas);
        }
        return CLAS;
    }

    public MemoryAddress getPhysicalMemoryAddressFromLogicalMemoryAddress(MemoryAddress m) {

        return st.getPhysicalMemoryAddressFromLogicalMemoryAddress(m);
    }

    @Override
    public int getPhysicalAddress(int logicalAddress) {

        int segmentNumber = getSegmentNumber(logicalAddress);
        int offset = getOffset(segmentNumber, logicalAddress);
        int physicalAdd = 0;

        SegmentTableEntry segment = st.getSegment(segmentNumber);
        if (offset < segment.getLimit()) {
            physicalAdd = offset + segment.getBase();
        } else {
            throw new IllegalArgumentException("offset is greater than limit");
        }
        return physicalAdd;
    }

    @Override
    public String toString() {
        return st.toString();
    }

}