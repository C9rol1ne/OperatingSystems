/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ur_os.memory;

import java.util.LinkedList;
import ur_os.system.SystemOS;

/**
 *
 * @author super
 */
public class FreeFramesList {
    
    LinkedList<Integer> freeFrames;
    
    public FreeFramesList(){
        this(SystemOS.MEMORY_SIZE);
    }
    
    public FreeFramesList(int size){
        freeFrames = new LinkedList();
        int numFrames = size/SystemOS.PAGE_SIZE;
        for (int i = 0; i < numFrames; i++) {
            freeFrames.add(i);
        }
    }
    
    public int getFrame(){
        return freeFrames.pop();
    }
    
    public void addFrame(int f){
        freeFrames.add(f);
    }
    
    public int getSize(){
        return freeFrames.size();
    }
    
}
