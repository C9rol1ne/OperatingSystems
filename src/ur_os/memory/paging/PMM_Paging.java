/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ur_os.memory.paging;

import ur_os.memory.MemoryAddress;
import ur_os.process.ProcessMemoryManager;
import ur_os.process.ProcessMemoryManagerType;

/**
 *
 * @author super
 */
public class PMM_Paging extends ProcessMemoryManager {

    PageTable pt;
    int pageSize = ur_os.memory.paging.PageTable.getPageSize();

    public PMM_Paging(int processSize) {
        super(ProcessMemoryManagerType.PAGING, processSize);
        pt = new PageTable(processSize);
    }

    public PMM_Paging(PageTable pt) {
        this.pt = pt;
    }

    public PMM_Paging(PMM_Paging pmm) {
        super(pmm);
        if (pmm.getType() == this.getType()) {
            this.pt = new PageTable(pmm.getPt());
        } else {
            System.out.println("Error - Wrong PMM parameter");
        }
    }

    public PageTable getPt() {
        return pt;
    }

    public void addFrameID(int frame) {
        pt.addFrameID(frame);
    }

    public MemoryAddress getPageMemoryAddressFromLocalAddress(int locAdd) {
        // Include the code to calculate the corresponding page and offset for a logical
        // address
        int pageNumber = locAdd / pageSize;
        int offset = locAdd % pageSize;
        return new MemoryAddress(pageNumber, offset);
    }

    public MemoryAddress getFrameMemoryAddressFromLogicalMemoryAddress(MemoryAddress m) {
        // Include the code to calculate the corresponding frame and offset for a
        // logical address
        int frame = pt.getFrameIdFromPage(m.getDivision());
        int offset = m.getOffset();
        return new MemoryAddress(frame, offset);
    }

    @Override
    public int getPhysicalAddress(int logicalAddress) {

        // Include the code to calculate the physical address here
        MemoryAddress logAdd = getPageMemoryAddressFromLocalAddress(logicalAddress);

        int offset = logAdd.getOffset();
        int frame = getFrameMemoryAddressFromLogicalMemoryAddress(logAdd).getDivision();
        int physicalAddress = (frame * pageSize) + offset;

        return physicalAddress;
    }

    @Override
    public String toString() {
        return pt.toString();
    }

}
