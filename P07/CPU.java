import java.util.Scanner;

class Process
{
    public int arrival;
    public int burst;
    public int priority;
    public int remainingTime;
    public float newPriority;
    public int endTime;
    
    public boolean isFinished;
    public boolean isRunning;

    Process(int arrival, int burst, int priority)
    {
        this.arrival = arrival;
        this.burst = burst;
        this.priority = priority;
        this.remainingTime = burst;
        this.newPriority = -1;
        this.endTime = -1;
        this.isRunning = false;
        this.isFinished = false;
    }

    public void updatePriority(int time)
    {
        if (!isFinished && arrival <= time)
        {
            newPriority = priority / remainingTime;

            if (arrival <= time && isRunning == false)
            {
                newPriority += 1;
            }
        }
    }

    public int getArrival() { return arrival; }
    public int getBurst() { return burst; }
    public int getPriority() { return priority; }
    public int getRemainingTime() { return remainingTime; }
    public float getNewPriority() { return newPriority; }

    public boolean arrived(int time)
    {
        if (arrival <= time)
        {
            return true;
        }
        return false;
    }

    public void run()
    {
        remainingTime = remainingTime - 1;
    }
}

class Scheduling
{
    private int num;
    public int totalTime;
    private Process process[];

    Scheduling()
    {
        Scanner in = new Scanner(System.in);

        System.out.print("Enter the number of processes :- ");
        num = in.nextInt();
        process = new Process[num];
        totalTime = 0;

        System.out.println("Enter the arrival time, burst time and priority of each process :- \n");
        for (int i = 0; i < num; i++)
        {
            int arr, bur, pri;
            arr = in.nextInt();
            bur = in.nextInt();
            pri = in.nextInt();

            totalTime += bur;

            process[i] = new Process(arr, bur, pri);
        }
        in.close();

        display();
    }

    public void updatePriorities(int time)
    {
        for (int i = 0; i < num; i++)
        {
            process[i].updatePriority(time);
        }
    }

    public boolean allFinished()
    {
        for (int i = 0; i < num; i++)
        {
            if (!process[i].isFinished)
            {
                return false;
            }
        }
        return true;
    }

    public int getMaxPriorityProcess(int time)
    {
        int maxId = -1;
        int val = 0;
        for (int i = 0; i < num; i++)
        {
            if (process[i].getArrival() <= time && val < process[i].getNewPriority() && !process[i].isFinished)
            {
                //val = process[i].getNewPriority();
                maxId = i;
            }
        }

        return maxId;
    }

    void schedule()
    {
        for (int i = 0; i < num; i++)
        {
            process[i].newPriority = (float)(process[i].priority / (float)(process[i].remainingTime));
            System.out.println(process[i].newPriority);
        }
    }

    public void display()
    {
        System.out.println("The times for each process are as follows :- \n");
        for (int i = 0; i < num; i++)
        {
            System.out.println("Process " + (int)(i + 1) + " :- " + process[i].getArrival() + " " + process[i].getBurst() + " " + process[i].getPriority() + " ");
        }
    }
}

class CPU
{
    public static void main(String args[])
    {
        Scheduling object = new Scheduling();
        object.schedule();
    }
}
