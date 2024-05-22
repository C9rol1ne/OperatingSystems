/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ur_os.system;

import ur_os.process.ProcessBurstType;
import ur_os.process.ProcessBurst;
import ur_os.memory.contiguous.PMM_Contiguous;
import ur_os.memory.Memory;
import ur_os.memory.paging.PMM_Paging;
import ur_os.process.ProcessMemoryManager;
import ur_os.process.ProcessMemoryManagerType;
import ur_os.process.Process;
import java.util.ArrayList;
import java.util.Random;
import ur_os.memory.freememorymagament.FreeMemorySlotManagerType;
import ur_os.memory.segmentation.PMM_Segmentation;

/**
 *
 * @author super
 */
public class SystemOS implements Runnable {

    SimulationType simType;
    private static int clock = 0;
    private static final int MAX_SIM_CYCLES = 1000;
    private static final int MAX_SIM_PROC_CREATION_TIME = 50;
    private static final double PROB_PROC_CREATION = 0.1;
    public static final int MAX_PROC_SIZE = 1000;
    private static Random r = new Random(1235);
    private OS os;
    private CPU cpu;
    private IOQueue ioq;

    private Memory memory;
    public static final int PAGE_SIZE = 64; // Page size in bytes
    public static final ProcessMemoryManagerType PMM = ProcessMemoryManagerType.SEGMENTATION;
    public static final FreeMemorySlotManagerType MSM = FreeMemorySlotManagerType.FIRST_FIT;
    public static final int MEMORY_SIZE = 1_048_576; // 1MB

    protected ArrayList<Process> processes;
    ArrayList<Integer> execution;

    public SystemOS(SimulationType simType) {
        cpu = new CPU();
        ioq = new IOQueue();
        memory = new Memory(MEMORY_SIZE);
        os = new OS(this, cpu, ioq, memory);
        cpu.setOS(os);
        ioq.setOS(os);
        execution = new ArrayList();
        processes = new ArrayList();
        // initSimulationQueue();
        initSimulationQueueSimple();
        // initSimulationQueueSimpler();
        showProcesses();
        this.simType = simType;
    }

    public int getTime() {
        return clock;
    }

    public ArrayList<Process> getProcessAtI(int i) {
        ArrayList<Process> ps = new ArrayList();

        for (Process process : processes) {
            if (process.getTime_init() == i) {
                ps.add(process);
            }
        }

        return ps;
    }

    public void initSimulationQueue() {
        double tp;
        Process p;
        for (int i = 0; i < MAX_SIM_PROC_CREATION_TIME; i++) {
            tp = r.nextDouble();
            if (PROB_PROC_CREATION >= tp) {
                p = new Process();
                p.setTime_init(clock);
                processes.add(p);
            }
            clock++;
        }
        clock = 0;
    }

    public void initSimulationQueueSimple() {
        Process p;
        int cont = 0;
        for (int i = 0; i < MAX_SIM_PROC_CREATION_TIME; i++) {
            if (i % 4 == 0) {
                p = new Process(cont++, -1, true);
                p.setTime_init(clock);
                processes.add(p);
            }
            clock++;
        }
        clock = 0;
    }

    public void initSimulationQueueSimpler() {

        Process p = new Process(0, 0);
        ProcessBurst temp = new ProcessBurst(5, ProcessBurstType.CPU);
        p.addBurst(temp);
        temp = new ProcessBurst(4, ProcessBurstType.IO);
        p.addBurst(temp);
        temp = new ProcessBurst(3, ProcessBurstType.CPU);
        p.addBurst(temp);
        processes.add(p);

        // Process 1
        p = new Process(1, 2);
        temp = new ProcessBurst(3, ProcessBurstType.CPU);
        p.addBurst(temp);
        temp = new ProcessBurst(5, ProcessBurstType.IO);
        p.addBurst(temp);
        temp = new ProcessBurst(6, ProcessBurstType.CPU);
        p.addBurst(temp);
        processes.add(p);

        // Process 2
        p = new Process(2, 6);
        temp = new ProcessBurst(7, ProcessBurstType.CPU);
        p.addBurst(temp);
        temp = new ProcessBurst(3, ProcessBurstType.IO);
        p.addBurst(temp);
        temp = new ProcessBurst(5, ProcessBurstType.CPU);
        p.addBurst(temp);
        processes.add(p);

        // Process 3
        p = new Process(3, 8);
        temp = new ProcessBurst(4, ProcessBurstType.CPU);
        p.addBurst(temp);
        temp = new ProcessBurst(3, ProcessBurstType.IO);
        p.addBurst(temp);
        temp = new ProcessBurst(7, ProcessBurstType.CPU);
        p.addBurst(temp);
        processes.add(p);

        clock = 0;
    }

    public void initSimulationQueueSimpler2() {

        Process p = new Process(false);
        ProcessBurst temp = new ProcessBurst(15, ProcessBurstType.CPU);
        p.addBurst(temp);
        temp = new ProcessBurst(12, ProcessBurstType.IO);
        p.addBurst(temp);
        temp = new ProcessBurst(21, ProcessBurstType.CPU);
        p.addBurst(temp);
        p.setTime_init(0);
        p.setPid(0);
        processes.add(p);

        p = new Process(false);
        temp = new ProcessBurst(8, ProcessBurstType.CPU);
        p.addBurst(temp);
        temp = new ProcessBurst(4, ProcessBurstType.IO);
        p.addBurst(temp);
        temp = new ProcessBurst(16, ProcessBurstType.CPU);
        p.addBurst(temp);
        p.setTime_init(2);
        p.setPid(1);
        processes.add(p);

        p = new Process(false);
        temp = new ProcessBurst(10, ProcessBurstType.CPU);
        p.addBurst(temp);
        temp = new ProcessBurst(5, ProcessBurstType.IO);
        p.addBurst(temp);
        temp = new ProcessBurst(12, ProcessBurstType.CPU);
        p.addBurst(temp);
        p.setTime_init(6);
        p.setPid(2);
        processes.add(p);

        p = new Process(false);
        temp = new ProcessBurst(9, ProcessBurstType.CPU);
        p.addBurst(temp);
        temp = new ProcessBurst(6, ProcessBurstType.IO);
        p.addBurst(temp);
        temp = new ProcessBurst(17, ProcessBurstType.CPU);
        p.addBurst(temp);
        p.setTime_init(8);
        p.setPid(3);
        processes.add(p);

        clock = 0;
    }

    public boolean isSimulationFinished() {

        boolean finished = true;

        for (Process p : processes) {
            finished = finished && p.isFinished();
        }

        return finished;

    }

    public SimulationType getSimulationType() {
        return simType;
    }

    @Override
    public void run() {
        double tp;
        ArrayList<Process> ps;

        System.out.println("******SIMULATION START******");

        int i = 0;
        Process temp_exec;
        int tempID;
        while (!isSimulationFinished() && i < MAX_SIM_CYCLES) {// MAX_SIM_CYCLES is the maximum simulation time, to
                                                               // avoid infinite loops
            System.out.println("******Clock: " + i + "******");

            if (this.getSimulationType() == SimulationType.ALL
                    || this.getSimulationType() == SimulationType.PROCESS_PLANNING) {
                System.out.println(cpu);
                System.out.println(ioq);
            }

            // Crear procesos, si aplica en el ciclo actual
            ps = getProcessAtI(i);
            for (Process p : ps) {
                os.create_process(p);
                showFreeMemory();
            } // If the scheduler is preemtive, this action will trigger the extraction from
              // the CPU, is any process is there.

            // Actualizar el OS, quien va actualizar el Scheduler

            os.update();
            // os.update() prepares the system for execution. It runs at the beginning of
            // the cycle.

            clock++;

            temp_exec = cpu.getProcess();
            if (temp_exec == null) {
                tempID = -1;
            } else {
                tempID = temp_exec.getPid();
            }
            execution.add(tempID);

            // Actualizar la CPU
            cpu.update();

            /// Actualizar la IO
            ioq.update();

            // Las actualizaciones de CPU y IO pueden generar interrupciones que actualizan
            // a cola de listos, cuando salen los procesos

            if (this.getSimulationType() == SimulationType.ALL
                    || this.getSimulationType() == SimulationType.PROCESS_PLANNING) {
                System.out.println("After the cycle: ");
                System.out.println(cpu);
                System.out.println(ioq);
            }
            i++;

        }
        System.out.println("******SIMULATION FINISHES******");
        // os.showProcesses();

        System.out.println("******Process Execution******");
        for (Integer num : execution) {
            System.out.print(num + " ");
        }
        System.out.println("");

        System.out.println("******Performance Indicators******");
        System.out.println("Total execution cycles: " + clock);
        System.out.println("CPU Utilization: " + this.calcCPUUtilization());
        System.out.println("Throughput: " + this.calcThroughput());
        System.out.println("Average Turnaround Time: " + this.calcTurnaroundTime());
        System.out.println("Average Waiting Time: " + this.calcAvgWaitingTime());
        System.out.println("Average Context Switches: " + this.calcAvgContextSwitches());

        showProcesses();
        memory.showNotNullBytes();
        showFreeMemory();

    }

    public void showFreeMemory() {
        if (PMM == ProcessMemoryManagerType.PAGING) {
            System.out.println("Free frame number: " + os.freeFrames.getSize());
        } else {
            System.out.println("Free Memory Slots (" + os.msm.getSize() + "): ");
            System.out.println(os.msm);
        }
    }

    public void showProcesses() {
        System.out.println("Process list:");
        StringBuilder sb = new StringBuilder();

        for (Process process : processes) {
            sb.append(process);
            sb.append("\n");
        }

        System.out.println(sb.toString());
    }

    public double calcCPUUtilization() {
        int cont = 0;
        for (Integer num : execution) {
            if (num == -1)
                cont++;
        }

        return (execution.size() - cont) / (double) execution.size();
    }

    public double calcTurnaroundTime() {

        double tot = 0;

        for (Process p : processes) {
            tot = tot + (p.getTime_finished() - p.getTime_init());
        }

        return tot / processes.size();
    }

    public double calcThroughput() {
        return (double) processes.size() / execution.size();
    }

    public double calcAvgWaitingTime() {
        double tot = 0;

        for (Process p : processes) {
            tot = tot + ((p.getTime_finished() - p.getTime_init()) - p.getTotalExecutionTime());
        }

        return tot / processes.size();
    }

    public double calcAvgContextSwitches() {
        int cont = 1;
        int prev = execution.get(0);
        for (Integer i : execution) {
            if (prev != i) {
                cont++;
                prev = i;
            }
        }

        return cont / (double) processes.size();
    }

}
