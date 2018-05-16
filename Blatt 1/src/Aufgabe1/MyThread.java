package Aufgabe1;

public class MyThread extends MyAccount implements Runnable{
    private static final int threadMax = 10;
    private static int runCount=0;

    public static synchronized void doStuff(){
        System.out.println(runCount+": "+Thread.currentThread().getName());
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static synchronized void doStuff2(){
        int before = MyAccount.getVariable();
        int increment;
        if(Math.random()<0.5){
            increment=-1;
        }else{
            increment=1;
        }
        MyAccount.setVariable(before+increment);
        System.out.println(before+" + "+increment+" = "+ MyAccount.getVariable());
    }
    public void run() {
        while(runCount++<100){
            doStuff2();
        }
    }

    public static void main(String[] args){
        for(int i=0; i<threadMax; i++){
            new Thread(new MyThread()).start();
        }
    }
}
