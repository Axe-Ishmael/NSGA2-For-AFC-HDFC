package newFunction;



public class NSGA2_AFC {

    public static void main(String [] args) {

        long  startTime = System.currentTimeMillis();//获取开始时间

        Simulator simulation = new Simulator();

        simulation.start();

        long endTime = System.currentTimeMillis();    //获取结束时间
        System.out.println("程序运行时间：" + (endTime - startTime) + "ms");    //输出程序运行时间
    }
}
