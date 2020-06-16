package newFunction;

public class EDC {

    private  int num;
    private double Error_Coverage;
    private double EDC_Time;
    private int soh;//Soft or Hardware 0:Soft方式实现 1:Hardware方式实现
    private int FPGA_Sqare;//消耗的FPGA面积 只有选择Hardware实现方式才消耗FPGA面积


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

    public void setSoh(int soh){
        this.soh = soh;
    }

    public int getSoh(){
        return this.soh;
    }

    public int getFPGA_Sqare() {
        return FPGA_Sqare;
    }

    public void setFPGA_Sqare(int FPGA_Sqare) {
        this.FPGA_Sqare = FPGA_Sqare;
    }


    //全参数构造方法 Hardware
    public EDC(int num,double Error_Coverage,double EDC_Time,int soh,int FPGA_Sqare){
        this.num = num;
        this.Error_Coverage = Error_Coverage;
        this.EDC_Time = EDC_Time;
        this.soh = soh;
        this.FPGA_Sqare = FPGA_Sqare;
    }

    //全参数构造方法 soft
    public EDC(int num,double Error_Coverage,double EDC_Time,int soh){
        this.num = num;
        this.Error_Coverage = Error_Coverage;
        this.EDC_Time = EDC_Time;
        this.soh = soh;
        this.FPGA_Sqare = 0;
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
        this.soh = 0;
        this.FPGA_Sqare = 0;
    }

}
