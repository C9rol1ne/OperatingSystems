/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ur_os.memory;

import ur_os.process.ProcessMemoryManager;
import ur_os.process.ProcessMemoryManagerType;

/**
 *
 * @author super
 */
public class PMM_Contiguous extends ProcessMemoryManager {
    int base;
    int limit;

    public PMM_Contiguous() {
        this(0, 100);
    }

    public PMM_Contiguous(int base, int limit) {
        super(ProcessMemoryManagerType.CONTIGUOUS, limit);
        this.base = base;
        this.limit = limit;
    }

    public PMM_Contiguous(PMM_Contiguous pmm) {
        super(pmm);
        if (pmm.getType() == this.getType()) {
            this.base = pmm.base;
            this.limit = pmm.limit;
        } else {
            System.out.println("Error - Wrong PMM parameter");
        }
    }

    public int getBase() {
        return base;
    }

    public void setBase(int base) {
        this.base = base;
    }

    public int getLimit() {
        return limit;
    }

    public void setLimit(int limit) {
        this.limit = limit;
    }

    @Override
    public int getPhysicalAddress(int logicalAddress) {

        if (logicalAddress >= 0 && logicalAddress < limit) {
            int PhysicalAddress = base + logicalAddress;
            return PhysicalAddress;
        } else {
            throw new IllegalArgumentException("Logical address is greater than limit register");
        }

    }

    @Override
    public String toString() {
        return "Base: " + base + " Limit: " + limit;
    }

}
