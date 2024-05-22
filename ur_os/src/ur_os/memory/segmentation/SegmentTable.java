/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ur_os.memory.segmentation;

import java.util.ArrayList;
import ur_os.system.SystemOS;
import java.util.Random;
import ur_os.memory.MemoryAddress;

/**
 *
 * @author super
 */
public class SegmentTable {
    
    ArrayList<SegmentTableEntry> segmentTable;
    public static final int SAMPLE_PROGRAM_SIZE = 100;
    public static final int SAMPLE_SEGMENT_NUMBER = 5;
    int programSize; //Size of the program in bytes
    int segmentNumber; //Size of the program in bytes
    Random r;
    
    public SegmentTable(){
        this(SAMPLE_PROGRAM_SIZE, SAMPLE_SEGMENT_NUMBER);
    }
    
    public SegmentTable(int programSize){
        this(programSize, SAMPLE_SEGMENT_NUMBER);
    }
       
    public SegmentTable(int programSize, int segmentNumber){
        this(programSize, segmentNumber, true);
    }
    
    public SegmentTable(int programSize, int segmentNumber, boolean auto){
        this.programSize = programSize;
        this.segmentNumber = segmentNumber;
        segmentTable = new ArrayList(segmentNumber); 
        r = new Random();
        if(auto)
            createSegments();
    }
    
    public void createSegments(){
        int[] vals = new int[segmentNumber];
        float total = 0;
        float total2 = 0;
        int base = 0;
        //Generate random numbers from 1 to 99
        for (int i = 0; i < segmentNumber; i++) {
            do{
                vals[i] = r.nextInt(100);
            }while(vals[i] == 0);
            total += vals[i];
        }
        
        for (int i = 0; i < segmentNumber; i++) {
            //The segment size is the percentage of the random value agains the total sum times de program size
            vals[i] = java.lang.Math.round((vals[i]/total)*this.programSize);
            total2 += vals[i];
        }
        
        //Any difference produced by the rounding will be addedd to the final segment
        vals[segmentNumber-1] += this.programSize - total2;
        
        //All bases are 0 because they will be set when the memory slot is assigned to the segment
        for (int i = 0; i < segmentNumber; i++) {
            this.addSegment(base, vals[i]);
        }
        
    }
    
    public SegmentTable(SegmentTable pt){
        this(pt.getProgramSize(), pt.getSize());
        segmentTable = new ArrayList(pt.getTable());
    }
    
    public ArrayList<SegmentTableEntry> getTable(){
        return segmentTable;
    }
    
    public MemoryAddress getSegmentMemoryAddressFromLocalAddress(int locAdd){
        //To do
        return null;
    }
    
    public MemoryAddress getPhysicalMemoryAddressFromLogicalMemoryAddress(MemoryAddress m){
        //To do
        return null;
    }
    
    public SegmentTableEntry getSegment(int i){
        return segmentTable.get(i);
    }
    
    public void addSegment(int base, int limit){
        segmentTable.add(new SegmentTableEntry(base, limit));
    }
    
    public void setFrameID(int segment, int base, int limit){
        if(segment == segmentTable.size()){
            segmentTable.add(new SegmentTableEntry(base, limit)); //If it is a new segment
        }else if(segment < segmentTable.size()){
            segmentTable.get(segment).setSegment(base, limit); //Update base and limit for an existing segment
        }else{
            System.out.println("Error - Including erroneous segment number");
        }
        
    }

    public int getSize() {
        return segmentNumber;
    }

    public int getProgramSize() {
        return programSize;
    }
    
    
    public String toString(){
        StringBuilder sb = new StringBuilder();
        int count = 0;
        for (SegmentTableEntry segmentTableEntry : segmentTable) {
            sb.append("Segment: ");
            sb.append(count++);
            sb.append(" ");
            sb.append(segmentTableEntry.toString());
            sb.append("\n");
        }
        
        return sb.toString();
    }
}
