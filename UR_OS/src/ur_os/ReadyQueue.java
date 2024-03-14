/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ur_os;

import java.util.ArrayList;

/**
 *
 * @author super
 */
public class ReadyQueue {

    Scheduler s;
    OS os;

    public ReadyQueue(OS os) {
        this.os = os;

        // s = new SJF_P(os);

        // s = new SJF_NP(os);

        // s = new FCFS(os);

        s = new RoundRobin(os, 4);
    }

    public ReadyQueue(OS OS, Scheduler s) {
        this.os = os;
        this.s = s;
    }

    public void addProcess(Process p) {
        s.addProcess(p);
    }

    public Process removeProcess(Process p) {
        return s.removeProcess(p);
    }

    public void update() {
        s.update();
    }

    public String toString() {

        return s.toString();
    }

}
