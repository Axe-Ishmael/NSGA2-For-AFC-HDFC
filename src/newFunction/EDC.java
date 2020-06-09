package newFunction;

public class EDC {

    private  int num;
    private double Error_Coverage;
    private double EDC_Time;

    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
    }

    public double getError_Coverage() {
        return Error_Coverage;
    }

    public void setError_Coverage(double error_Coverage) {
        Error_Coverage = error_Coverage;
    }

    public double getEDC_Time() {
        return EDC_Time;
    }

    public void setEDC_Time(double EDC_Time) {
        this.EDC_Time = EDC_Time;
    }


    //全参数构造方法
    public EDC(int num,double Error_Coverage,double EDC_Time){
        this.num = num;
        this.Error_Coverage = Error_Coverage;
        this.EDC_Time = EDC_Time;
    }

    //Error_Coverage构造方法
    public EDC(double Error_Coverage){
        this.num = 0;
        this.Error_Coverage = Error_Coverage;
        this.EDC_Time = 0.0;
    }

    //初始化构造方法
    public EDC(){
        this.num = 0;
        this.Error_Coverage = 0.0;
        this.EDC_Time = 0.0;
    }

}
