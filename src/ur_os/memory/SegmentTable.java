/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ur_os.memory;

import java.util.ArrayList;
import ur_os.system.SystemOS;
import java.util.Random;

/**
 *
 * @author super
 */
public class SegmentTable {
    
    ArrayList<SegmentTableEntry> segmentTable;
    public static final int SAMPLE_PROGRAM_SIZE = 100;
    public static final int SAMPLE_SEGMENT_NUMBER = 5;
    int programSize; //Size of the program in bytes
    int segmentNumber; //
    Random r;
    
    public SegmentTable(){
        this(SAMPLE_PROGRAM_SIZE, SAMPLE_SEGMENT_NUMBER);
    }
    
    public SegmentTable(int programSize){
        this(programSize, SAMPLE_SEGMENT_NUMBER);
    }
       
    public SegmentTable(int programSize, int segmentNumber){
        this.programSize = programSize;
        this.segmentNumber = segmentNumber;
        segmentTable = new ArrayList(segmentNumber); 
        r = new Random();
        createSegments();
    }
    
    public void createSegments(){
        int[] vals = new int[segmentNumber];
        float total = 0;
        float total2 = 0;
        int base = 0;
        for (int i = 0; i < segmentNumber; i++) {
            vals[i] = r.nextInt(100);
            total += vals[i];
        }
        for (int i = 0; i < segmentNumber; i++) {
            vals[i] = java.lang.Math.round((vals[i]/total)*this.programSize);
            total2 += vals[i];
        }
        vals[segmentNumber-1] += this.programSize - total2;
        
        for (int i = 0; i < segmentNumber; i++) {
            this.addSegment(base, vals[i]);
            base += vals[i];
        }
        
    }
    
    public SegmentTable(SegmentTable pt){
        this(pt.getProgramSize(), pt.getSize());
        segmentTable = new ArrayList(pt.getTable());
    }
    
    private ArrayList<SegmentTableEntry> getTable(){
        return segmentTable;
    }
    
    public MemoryAddress getSegmentMemoryAddressFromLocalAddress(int locAdd){
        //Implement getting the segment that holds the logical address and return the segment number and the offset
        
        return new MemoryAddress(-1, -1);
    }
    
    public MemoryAddress getPhysicalMemoryAddressFromLogicalMemoryAddress(MemoryAddress m){
        //Return the MemoryAddress that holds the two components of the physical address: base and offset
        return new MemoryAddress(-1, -1);
    }
    
    public SegmentTableEntry getSegment(int i){
        return segmentTable.get(i);
    }
    
    public void addSegment(int base, int limit){
        segmentTable.add(new SegmentTableEntry(base, limit));
    }
    
    public void setSegment(int segment, int base, int limit){
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
