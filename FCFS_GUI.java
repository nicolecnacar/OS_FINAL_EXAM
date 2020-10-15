/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package scheduling;

import java.awt.Color;
import java.awt.Graphics;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import javax.swing.JFrame;

/**
 *
 * @author valar
 */
public class FCFS_GUI {
    
        static int minimumArrivalTime,sumCPUBurstTime;
        static int lengthOfEachBlock;
        static final int SCREEN_WIDTH=700,SCREEN_HEIGHT=280;
        static final int rectangleUpperPadding=50,rectangleHeight=100;
        static int numberOfProcesses;
        static int CPUBurstTime[],arrivalTime[];
        static BufferedReader br;
        static FCFS_GUI obj;
                
        FCFS_GUI(){
            this.obj=this;
        }
        
    public static void main(String args[]) throws NumberFormatException, IOException {
        
        br = new BufferedReader(new InputStreamReader(System.in));
        
        int temp;
        float avgwt = 0, avgta = 0;
        
        System.out.println("Enter the number of processes: ");
        
        numberOfProcesses = Integer.parseInt(br.readLine());
        int pid[] = new int[numberOfProcesses];   // process ids
        arrivalTime = new int[numberOfProcesses];     // arrival times
        CPUBurstTime = new int[numberOfProcesses];     // burst or execution times
        int ct[] = new int[numberOfProcesses];     // completion times
        int ta[] = new int[numberOfProcesses];     // turn around times
        int wt[] = new int[numberOfProcesses];     // waiting times
        
        for(int i = 0; i < numberOfProcesses; i++){
            System.out.println("Enter the data for the process: "+(i+1));
            System.out.print("Enter the arrival time: ");
            arrivalTime[i]=Integer.parseInt(br.readLine());
            System.out.print("Enter the burst time: ");
            CPUBurstTime[i]=Integer.parseInt(br.readLine());
        }
        
        for(int i = 0 ; i < numberOfProcesses; i++)
		{
			for(int  j=0;  j < numberOfProcesses-(i+1) ; j++)
			{
				if( arrivalTime[j] > arrivalTime[j+1] )
				{
					temp = arrivalTime[j];
					arrivalTime[j] = arrivalTime[j+1];
					arrivalTime[j+1] = temp;
					temp = CPUBurstTime[j];
					CPUBurstTime[j] = CPUBurstTime[j+1];
					CPUBurstTime[j+1] = temp;
					temp = pid[j];
					pid[j] = pid[j+1];
					pid[j+1] = temp;
				}
			}
		}
		
		// finding completion times
		for(int  i = 0 ; i < numberOfProcesses; i++)
		{
			if( i == 0)
			{	
				ct[i] = arrivalTime[i] + CPUBurstTime[i];
			}
			else
			{
				if( arrivalTime[i] > ct[i-1])
				{
					ct[i] = arrivalTime[i] + CPUBurstTime[i];
				}
				else
					ct[i] = ct[i-1] + CPUBurstTime[i];
			}
			ta[i] = ct[i] - arrivalTime[i] ;          // turnaround time= completion time- arrival time
			wt[i] = ta[i] - CPUBurstTime[i] ;          // waiting time= turnaround time- burst time
			avgwt += wt[i] ;               // total waiting time
			avgta += ta[i] ;               // total turnaround time
		}
		
		System.out.println("\nPID  Arrival  Burst  Complete Turn Waiting");
		for(int  i = 0 ; i < numberOfProcesses;  i++)
		{
			System.out.println( (i+1) + "  \t " + arrivalTime[i] + "\t" + CPUBurstTime[i] + "\t" + ct[i] + "\t" + ta[i] + "\t"  + wt[i]);
		}
                
		System.out.println("\nAverage Waiting Time: "+ (avgwt/numberOfProcesses));     // printing average waiting time.
		System.out.println("Average Turnaround Time: "+(avgta/numberOfProcesses));    // printing average turnaround time.
        
        drawGanttChart();
        
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
        
        new FrameForFCFS(obj);
    }
    
}

class FrameForFCFS extends JFrame {
    
    int arrivalTime[];
    FCFS_GUI obj;
    
    FrameForFCFS(FCFS_GUI obj){
        super("FCFS");
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
