/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ur_os;

import java.io.Console;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

/**
 *
 * @author super
 */
public class SystemOS implements Runnable {

    private static int clock = 0;
    private static final int MAX_SIM_CYCLES = 50;
    private static final int MAX_SIM_PROC_CREATION_TIME = 50;
    private static final double PROB_PROC_CREATION = 0.1;
    private static Random r = new Random(1235);
    private OS os;
    private CPU cpu;
    private IOQueue ioq;

    protected Scheduler s;
    protected ArrayList<Process> processes;

    ArrayList<Integer> execution;

    public SystemOS() {
        cpu = new CPU();
        ioq = new IOQueue();
        os = new OS(this, cpu, ioq);
        cpu.setOS(os);
        ioq.setOS(os);
        execution = new ArrayList();
        processes = new ArrayList();
        // initSimulationQueue();
        // initSimulationQueueSimple();
        initSimulationsumrolled();
        showProcesses();
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
        int sum = 0;
        for (int i = 0; i < MAX_SIM_PROC_CREATION_TIME; i++) {
            if (i % 4 == 0) {
                p = new Process(sum++, -1);
                p.setTime_init(clock);
                processes.add(p);
            }
            clock++;
        }
        clock = 0;
    }

    public void initSimulationsumrolled() {
        Process p;
        p = new Process(0, 0, 10, 1, 2);
        processes.add(p);
        p = new Process(0, 3, 4, 1, 4);
        processes.add(p);
        p = new Process(0, 2, 2, 0, 0);
        processes.add(p);
        p = new Process(0, 4, 1, 0, 0);
        processes.add(p);

    }

    public boolean isSimulationFinished() {

        boolean finished = true;

        for (Process p : processes) {
            finished = finished && p.isFinished();
        }

        return finished;

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
            // System.out.println(cpu);
            // System.out.println(ioq);

            if (i == 2) {
                System.out.print("");
            }

            // Crear procesos, si aplica en el ciclo actual
            ps = getProcessAtI(i);
            for (Process p : ps) {
                os.create_process(p);
            } // If the scheduler is preemtive, this action will trigger the extraction from
              // the CPU, is any process is there.

            // Actualizar el OS, quien va actualizar el Scheduler
            os.update();
            clock++;
            // os.update() prepares the system for execution. It runs at the beginning of
            // the cycle.

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

            // REVISAR PROBLEMA DE DEPENDENCIA ENTRE IO Y CPU EN EL MISMO CICLO!!!

            // Las actualizaciones de CPU y IO pueden generar interrupciones que actualizan
            // a cola de listos, cuando salen los procesos

            Console console = System.console();
            console.printf("\u001B[31mAfter the cycle: \u001B[0m%n");
            System.out.println(cpu);
            System.out.println(ioq);

            i++;
            // clock++;

            for (Integer num : execution) {
                System.out.print(num + " ");
            }
            System.out.println("");
        }
        System.out.println("******SIMULATION FINISHES******");
        // os.showProcesses();

        System.out.println("******Process Execution******");
        for (Integer num : execution) {
            System.out.print(num + " ");
        }
        System.out.println("");

        // Calculation of stats here
        int numberProcesses = processes.size();
        double idleCpuCycles = execution.stream().filter(element -> element == -1).count(); // counts the -1 in
                                                                                            // execution
        int totalCpuCycles = execution.size();
        int cpuBusyCycles = totalCpuCycles - (int) idleCpuCycles;
        double Throughput = (double) numberProcesses / totalCpuCycles;
        double cpuUtilization = (double) cpuBusyCycles / totalCpuCycles;
        List<Integer> turnaroundsPerProcess = new ArrayList<>();
        List<Integer> waitingTimePerProcess = new ArrayList<>();

        for (Process p : processes) {
            int turnaround = p.getTime_finished() - p.getTime_init();
            turnaroundsPerProcess.add(turnaround);
            ArrayList<ProcessBurst> bursts = p.getPBL().getList();
            int cyclesCont = turnaround;

            for (ProcessBurst burst : bursts) {
                cyclesCont -= burst.getCycles();
            }

            waitingTimePerProcess.add(cyclesCont);
        }

        int previousProcess = -1; // cpu starts idle
        int nContextSwitches = 0;

        for (int p : execution) {
            if (p != previousProcess && p != -1) {
                nContextSwitches++;
            }
            previousProcess = p;

        }

        int sumTurnaroundsPerProcess = turnaroundsPerProcess.stream().mapToInt(Integer::intValue).sum();
        int sumWaitingTimePerProcess = waitingTimePerProcess.stream().mapToInt(Integer::intValue).sum();
        double turnaroundTime = (double) sumTurnaroundsPerProcess / numberProcesses;
        double waitingTime = (double) sumWaitingTimePerProcess / numberProcesses;
        double contextSwitches = (double) nContextSwitches / numberProcesses;

        System.out.println(" ");
        System.out.println("******Simulation Statistics******");
        System.out.println("CPU Utilization: " + cpuUtilization);
        System.out.println("Turnaround Time: " + turnaroundTime);
        System.out.println("Throughput: " + Throughput);
        System.out.println("Waiting Time: " + waitingTime);
        System.out.println("Context Switches: " + contextSwitches);
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

}