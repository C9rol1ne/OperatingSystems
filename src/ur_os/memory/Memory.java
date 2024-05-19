/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ur_os.memory;

import java.util.ArrayList;
import ur_os.system.SystemOS;

/**
 *
 * @author super
 */
public class Memory {
    
    byte[] bytes;
    int size;
    
    public Memory(){
        this(SystemOS.MEMORY_SIZE);
    }
    
    public Memory(int size){
        this.size = size;
        bytes = new byte[size];
        
        for (int i = 0; i<size;i++) {
            bytes[i] = (byte)0;
        }
        
    }
    
    public byte get(int i){
        return bytes[i];
    }
    
    public void set(int i, byte b){
        bytes[i]= b;
    }
    
    public int getSize(){
        return size;
    }
    
    public void showNotNullBytes(){
        for (int i = 0; i<size;i++) {
            if(bytes[i] != 0){
                System.out.println(""+i+" - "+bytes[i]+"\n");
            }
        }
    }
    
    
    
}
