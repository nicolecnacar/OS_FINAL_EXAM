/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rr;

import java.awt.Color;
import java.awt.Graphics;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import javax.swing.JFrame;
import rr.RR_GUI;

/**
 *
 * @author valar
 */
public class RR_GUI {
    
        static int minimumArrivalTime,sumCPUBurstTime;
        static int lengthOfEachBlock;
        static final int SCREEN_WIDTH=700,SCREEN_HEIGHT=280;
        static final int rectangleUpperPadding=50,rectangleHeight=100;
        static int numberOfProcesses;
        static int CPUBurstTime[],arrivalTime[];
        static BufferedReader br;
        static RR_GUI obj;
                
        RR_GUI(){
            this.obj=this;
        }
        
    public static void main(String args[]) throws NumberFormatException, IOException {
        
        br = new BufferedReader(new InputStreamReader(System.in));
        
        System.out.println("Enter the number of processes: ");
        
        numberOfProcesses = Integer.parseInt(br.readLine());
        int pid[] = new int[numberOfProcesses];   // process ids
        CPUBurstTime = new int[numberOfProcesses];     // burst or execution times
        arrivalTime = new int[numberOfProcesses];
        System.out.println ("Enter quantum time: ");
	int qt = Integer.parseInt(br.readLine());
        
        for(int i = 0; i < numberOfProcesses; i++){
            System.out.println("Enter the data for the process: "+(i+1));
            System.out.print("Enter the burst time: ");
            CPUBurstTime[i]=Integer.parseInt(br.readLine());
            System.out.print("Enter the arrival time: ");
            arrivalTime[i]=Integer.parseInt(br.readLine());

        }
        
        findavgTime(pid, numberOfProcesses, CPUBurstTime, qt); 
        
        drawGanttChart();
        
    }
    
    static void findWaitingTime(int processes[], int n, 
				int bt[], int wt[], int quantum) 
	{ 
		// Make a copy of burst times bt[] to store remaining 
		// burst times. 
		int rem_bt[] = new int[n]; 
		for (int i = 0 ; i < n ; i++) 
			rem_bt[i] = bt[i]; 
	
		int t = 0; // Current time 
	
		// Keep traversing processes in round robin manner 
		// until all of them are not done. 
		while(true) 
		{ 
			boolean done = true; 
	
			// Traverse all processes one by one repeatedly 
			for (int i = 0 ; i < n; i++) 
			{ 
				// If burst time of a process is greater than 0 
				// then only need to process further 
				if (rem_bt[i] > 0) 
				{ 
					done = false; // There is a pending process 
	
					if (rem_bt[i] > quantum) 
					{ 
						// Increase the value of t i.e. shows 
						// how much time a process has been processed 
						t += quantum; 
	
						// Decrease the burst_time of current process 
						// by quantum 
						rem_bt[i] -= quantum; 
					} 
	
					// If burst time is smaller than or equal to 
					// quantum. Last cycle for this process 
					else
					{ 
						// Increase the value of t i.e. shows 
						// how much time a process has been processed 
						t = t + rem_bt[i]; 
	
						// Waiting time is current time minus time 
						// used by this process 
						wt[i] = t - bt[i]; 
	
						// As the process gets fully executed 
						// make its remaining burst time = 0 
						rem_bt[i] = 0; 
					} 
				} 
			} 
	
			// If all processes are done 
			if (done == true) 
			break; 
		} 
	} 
	
	// Method to calculate turn around time 
	static void findTurnAroundTime(int processes[], int n, 
							int bt[], int wt[], int tat[]) 
	{ 
		// calculating turnaround time by adding 
		// bt[i] + wt[i] 
		for (int i = 0; i < n ; i++) 
			tat[i] = bt[i] + wt[i]; 
	} 
	
	// Method to calculate average time 
	static void findavgTime(int processes[], int n, int bt[], int quantum){
            
		int wt[] = new int[n], tat[] = new int[n]; 
		int total_wt = 0, total_tat = 0; 
	
		// Function to find waiting time of all processes 
		findWaitingTime(processes, n, bt, wt, quantum); 
	
		// Function to find turn around time for all processes 
		findTurnAroundTime(processes, n, bt, wt, tat); 
	
		// Display processes along with all details 
		System.out.println("Processes " + " Burst Time " + " Waiting Time " + " Turn Around Time"); 
	
		// Calculate total waiting time and total turn 
		// around time 
		for (int i=0; i<n; i++) 
		{ 
			total_wt = total_wt + wt[i]; 
			total_tat = total_tat + tat[i]; 
			System.out.println(" " + (i+1) + "\t\t" + bt[i] +"\t " + wt[i] +"\t\t " + tat[i]); 
		} 
	
		System.out.println("Average WAT = " + (float)total_wt / (float)n); 
		System.out.println("Average TAT = " + (float)total_tat / (float)n); 
	} 
    
    public static void drawGanttChart() throws NumberFormatException, IOException{
    
        int choice;
        sumCPUBurstTime=0;
        
        /* calculating the sum of all cpu burst time */
        for(int i=0;i<numberOfProcesses;i++){
            sumCPUBurstTime+=CPUBurstTime[i];
        }
        
        /* now the total width of the screen is to be divided into sumCPUBurstTime equal parts */
        lengthOfEachBlock=SCREEN_WIDTH/sumCPUBurstTime;
        
        /*
        * claculating the minimum arrival time
        */
        
        minimumArrivalTime=Integer.MAX_VALUE;
        
        for(int i=0;i<numberOfProcesses;i++){
            if(minimumArrivalTime>arrivalTime[i])
                minimumArrivalTime=arrivalTime[i];
            }
        
        new FrameForRR(obj);
    }
    
}

class FrameForRR extends JFrame {
    
    int arrivalTime[];
    RR_GUI obj;
    
    FrameForRR(RR_GUI obj){
        super("RR");
        this.obj=obj;
        this.setVisible(true);
        this.setSize(obj.SCREEN_WIDTH+100, obj.SCREEN_HEIGHT);
        arrivalTime=obj.arrivalTime.clone();
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }
    
    @Override
    
    public void paint(Graphics g){
        super.paint(g);
        this.getContentPane().setBackground(Color.white);
        
        int currentTime = obj.minimumArrivalTime;
        arrivalTime = obj.arrivalTime.clone();
        
        int i,j,min,mini = 0;
        int leftStart = 50;
        g = this.getContentPane().getGraphics();
        g.drawString("" +obj.minimumArrivalTime, leftStart, obj.rectangleUpperPadding + obj.rectangleHeight + 20);
        
        for(j = 0; j < obj.numberOfProcesses; j++){
            
            min = Integer.MAX_VALUE;
            
            for(i = 0; i < obj.numberOfProcesses; i++){
                
                if(min > arrivalTime[i]){
                    min = arrivalTime[i];
                    mini = i;
                }
            }
            
            arrivalTime[mini] = Integer.MAX_VALUE;
            
            g = this.getContentPane().getGraphics();
            g.drawRect(leftStart, obj.rectangleUpperPadding, obj.lengthOfEachBlock*obj.CPUBurstTime[mini], obj.rectangleHeight);
            g.drawString("P"+(mini+1),leftStart+5,obj.rectangleUpperPadding+50);
            leftStart+=obj.lengthOfEachBlock*obj.CPUBurstTime[mini];
            currentTime+=obj.CPUBurstTime[mini];
            g.drawString(""+currentTime,leftStart,obj.rectangleUpperPadding+obj.rectangleHeight+20);
        }
    }
}
