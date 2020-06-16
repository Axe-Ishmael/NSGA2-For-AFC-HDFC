package newFunction;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Individual {

    //每个Individual包含一组EDC
    private List<EDC> edcList;

    //需要优化的参数
    private double AFC;
    private double HDFC;

    private double totalTime;//edcList总运行时间
    private int totalFPGASquare;//edcList总FPGA面积

    //--被该individual支配的individuals组成的List
    private List<Individual> dominatedIndividuals;

    // --支配当前该individual的individual数量
    private int dominating;

    //individual属于的front层数 = rank
    private int rank;

    //--计算Crowding distance
    private double crowdingDistance;

    private double timeLimit;
    private int FPGALmit;

    public Individual(int EDCLength){
        this.edcList = new ArrayList<EDC>(EDCLength);
        this.dominatedIndividuals = new ArrayList<Individual>();

        Random random = new Random();
        int soh;
        for(int i =0;i<EDCLength;i++){
            soh = random.nextInt(2);
            this.edcList.add(generateEDCduetoTable(soh,random));
        }

        this.totalTime = Functions.Total_Time(edcList);
        this.totalFPGASquare = Functions.Total_FPGA_SQUARE(edcList);

        AFC = Functions.Average_Coverage(this.edcList);
        HDFC = Functions.Calculate_std(this.edcList);


    }

    public Individual(int EDCLength,double timeLimit,int FPGALmit){
        this.edcList = new ArrayList<EDC>(EDCLength);
        this.dominatedIndividuals = new ArrayList<Individual>();

        Random random = new Random();

        do {
            int soh;
            for(int i =0;i<EDCLength;i++){
                soh = random.nextInt(2);
                this.edcList.add(generateEDCduetoTable(soh,random));
            }

            this.totalTime = Functions.Total_Time(edcList);
            this.totalFPGASquare = Functions.Total_FPGA_SQUARE(edcList);

        }while (totalTime > timeLimit ||totalFPGASquare > FPGALmit);

        AFC = Functions.Average_Coverage(this.edcList);
        HDFC = Functions.Calculate_std(this.edcList);

    }

    //Getter and setter--------------------------------------------------------------------//
    public double getTotalTime() {
        return totalTime;
    }

    public void setTotalTime(double totalTime) {
        this.totalTime = totalTime;
    }

    public int getTotalFPGASquare() {
        return totalFPGASquare;
    }

    public void setTotalFPGASquare(int totalFPGASquare) {
        this.totalFPGASquare = totalFPGASquare;
    }

    public double getTimeLimit() {
        return timeLimit;
    }

    public void setTimeLimit(double timeLimit) {
        this.timeLimit = timeLimit;
    }

    public int getFPGALmit() {
        return FPGALmit;
    }

    public void setFPGALmit(int FPGALmit) {
        this.FPGALmit = FPGALmit;
    }

    public List<EDC> getEdcList() {
        return edcList;
    }

    public void setEdcList(List<EDC> edcList) {
        this.edcList = edcList;
    }

    public double getAFC() {
        return AFC;
    }

    public void setAFC(double AFC) {
        this.AFC = AFC;
    }

    public double getHDFC() {
        return HDFC;
    }

    public void setHDFC(double HDFC) {
        this.HDFC = HDFC;
    }

    //返回被该individual支配的individuals组成的List
    public List<Individual> getDominatedIndividuals() {
        return this.dominatedIndividuals;
    }

    public void setDominatedIndividuals(List<Individual> dominatedIndividuals) {
        this.dominatedIndividuals = dominatedIndividuals;
    }

    public int getDominating() {
        return dominating;
    }

    public void setDominating(int dominating) {
        this.dominating = dominating;
    }

    public int getRank() {
        return rank;
    }

    public void setRank(int rank) {
        this.rank = rank;
    }

    public double getCrowdingDistance() {
        return crowdingDistance;
    }

    public void setCrowdingDistance(double crowdingDistance) {
        this.crowdingDistance = crowdingDistance;
    }
//-----------------------------------------------------------------------------------------------------------------//


    //  向被该individual支配的individuals 组成的List中添加一个新的individual
    public void addDominatedIndividual(Individual q) {
        this.dominatedIndividuals.add(q);
    }


    //如果当前individual支配作为参数被传入的individual q,则返回true
    public boolean dominates(Individual q) {
        double pAFC = this.getAFC();
        double qAFC = q.getAFC();
        double pHDFC = this.getHDFC();
        double qHDFC = q.getHDFC();

        // In order for p to dominate q, p must be at least equally optimal for
        // all optimization functions, and more optimal in at least one
        //AFC求最大值，HDFC求最小值
        if (pAFC < qAFC || pHDFC > qHDFC
                || (pAFC == qAFC && pHDFC == qHDFC)) {
            return false;
        }

        return true;
    }

    //-- 支配该individual的individuals数量+1
    public void incrementDominating() {
        this.dominating++;
    }

    // 支配该individual的individuals数量-1
    public void decrementDominating() {
        this.dominating--;
    }


    //判断Individual是否是可行
    //timeLimit & FPGALimit
    public boolean isFeasible() {

        return (Functions.Total_Time(this.edcList) <=Simulator.timeLimit && Functions.Total_FPGA_SQUARE(this.edcList)<=Simulator.FPGALimit );
    }

    //判断Individual是否是可行
    //timeLimit & FPGALimit
    public boolean isFeasible(Individual individual) {

        return (individual.getTotalTime() <=Simulator.timeLimit && individual.getTotalFPGASquare()<=Simulator.FPGALimit );
    }



    //替换EDCList中第index个EDC对象
    public List<EDC> setEDCList(int index, double Error_Coverage){

        if (index > this.edcList.size()){
            return this.edcList;
        }

        EDC edc = new EDC(Error_Coverage);
        edcList.set(index,edc);
        return edcList;

    }

    //重新计算AFC和HDFC的值
    public void reCalculatePara(){

        AFC = Functions.Average_Coverage(this.edcList);
        HDFC = Functions.Calculate_std(this.edcList);
    }

    public Individual(List<EDC>edcList){
        this.edcList = edcList;
        this.dominatedIndividuals = new ArrayList<Individual>();

        AFC = Functions.Average_Coverage(this.edcList);
        HDFC = Functions.Calculate_std(this.edcList);

    }

    public EDC generateEDCduetoTable(int soh,Random random){
        EDC edc = null;
        int num;
        do {
            num = random.nextInt(11);//产生0-10的整数
        }while (num < 1);

        switch (soh){
            case 0:// 选择软件实现方式
                switch (num){
                    case 1:
                        edc = new EDC(1,0.8716,19.5,soh);
                        break;
                    case 2:
                        edc = new EDC(2,0.8719,22,soh);
                        break;
                    case 3:
                        edc = new EDC(3,0.9126,24.5,soh);
                        break;
                    case 4:
                        edc = new EDC(4,0.924,26,soh);
                        break;
                    case 5:
                        edc = new EDC(5,0.9249,28.5,soh);
                        break;
                    case 6:
                        edc = new EDC(6,0.9291,30,soh);
                        break;
                    case 7:
                        edc = new EDC(7,0.9337,32.5,soh);
                        break;
                    case 8:
                        edc = new EDC(8,0.9457,34,soh);
                        break;
                    case 9:
                        edc = new EDC(9,0.9732,35.5,soh);
                        break;
                    case 10:
                        edc = new EDC(10,1.0,41,soh);
                        break;
                }
                break;

            case 1:// 选择硬件

                switch (num){
                    case 1:
                        edc = new EDC(1,0.8716,1.95,soh,600);
                        break;
                    case 2:
                        edc = new EDC(2,0.8719,2.2,soh,614);
                        break;
                    case 3:
                        edc = new EDC(3,0.9126,2.45,soh,724);
                        break;
                    case 4:
                        edc = new EDC(4,0.924,2.6,soh,609);
                        break;
                    case 5:
                        edc = new EDC(5,0.9249,2.85,soh,611);
                        break;
                    case 6:
                        edc = new EDC(6,0.9291,3.0,soh,627);
                        break;
                    case 7:
                        edc = new EDC(7,0.9337,3.25,soh,727);
                        break;
                    case 8:
                        edc = new EDC(8,0.9457,3.4,soh,827);
                        break;
                    case 9:
                        edc = new EDC(9,0.9732,3.55,soh,925);
                        break;
                    case 10:
                        edc = new EDC(10,1.0,4.1,soh,1168);
                        break;
                }

                break;

            default:

        }

        return edc;

    }



}
