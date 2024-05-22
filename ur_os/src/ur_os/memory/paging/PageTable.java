/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ur_os.memory.paging;

import java.util.ArrayList;
import ur_os.system.SystemOS;

/**
 *
 * @author super
 */
public class PageTable {
    
    ArrayList<PageTableEntry> pageTable;
    public static final int SAMPLE_PROCESS_SIZE = 100;
    int programSize; //Size of the program in bytes
    int size; //Number of pages that the program uses
    
    public PageTable(){
        this((SAMPLE_PROCESS_SIZE/SystemOS.PAGE_SIZE)+1);
    }
       
    public PageTable(int programSize){
        this.programSize = programSize;
        size = java.lang.Math.floorDiv(programSize,SystemOS.PAGE_SIZE)+1;
        pageTable = new ArrayList(size); 
    }
    
    public PageTable(PageTable pt){
        this(pt.getProgramSize());
        pageTable = new ArrayList(pt.getTable());
    }
    
    private ArrayList<PageTableEntry> getTable(){
        return pageTable;
    }
    
    public int getFrameIdFromPage(int page){
        if(page >= 0 && page < size){
            return pageTable.get(page).getFrameId();
        }
        System.out.println("ERROR in memory access - Invalid page");
        return -1;
    }
    
    public void addFrameID(int frame){
        pageTable.add(new PageTableEntry(frame));
    }
    
    public void setFrameID(int page, int frame){
        if(page == pageTable.size()){
            pageTable.add(new PageTableEntry(frame)); //If it is a new page
        }else if(page < pageTable.size()){
            pageTable.get(page).setFrameId(frame); //Update a frameID for an existing page
        }else{
            System.out.println("Error - Including erroneous page number");
        }
        
    }

    public int getSize() {
        return size;
    }

    public int getProgramSize() {
        return programSize;
    }
    
    public static int getPageSize() {
        return SystemOS.PAGE_SIZE;
    }
    
    public ArrayList<PageTableEntry> getList(){
        return this.pageTable;
    }
    
    public String toString(){
        StringBuilder sb = new StringBuilder();
        int count = 0;
        for (PageTableEntry pageTableEntry : pageTable) {
            sb.append("Page: ");
            sb.append(count++);
            sb.append(" ");
            sb.append(pageTableEntry.toString());
            sb.append("\n");
        }
        
        return sb.toString();
    }
}
