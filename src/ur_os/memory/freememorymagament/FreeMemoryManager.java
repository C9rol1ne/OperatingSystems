/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package ur_os.memory.freememorymagament;

import ur_os.process.Process;

/**
 *
 * @author super
 */
public abstract class FreeMemoryManager {
    
    public abstract void reclaimMemory(Process p);
    
}
