package newFunction;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Individual {

    //每个Individual包含一组EDC
    private List<EDC> edcList;

    //需要优化的参数
    private double AFC;
    private double HDFT;

    //--被该individual支配的individuals组成的List
    private List<Individual> dominatedIndividuals;

    // --支配当前该individual的individual数量
    private int dominating;

    //individual属于的front层数 = rank
    private int rank;

    //--计算Crowding distance
    private double crowdingDistance;

    public Individual(int EDCLength){
        this.edcList = new ArrayList<EDC>(EDCLength);
        this.dominatedIndividuals = new ArrayList<Individual>();

        Random random = new Random();
        for(int i =0;i<EDCLength;i++){
            this.edcList.add(new EDC(random.nextDouble()));
        }

        AFC = Functions.Calculate_std(this.edcList);
        HDFT = Functions.HDFT(this.edcList);


    }

    //Getter and setter--------------------------------------------------------------------//
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

    public double getHDFT() {
        return HDFT;
    }

    public void setHDFT(double HDFT) {
        this.HDFT = HDFT;
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
    //----------------------------------------------------------------------------------------------------//


    //  向被该individual支配的individuals 组成的List中添加一个新的individual
    public void addDominatedIndividual(Individual q) {
        this.dominatedIndividuals.add(q);
    }


    //如果当前individual支配作为参数被传入的individual q,则返回true
    public boolean dominates(Individual q) {
        double pAFC = this.getAFC();
        double qAFC = q.getAFC();
        double pHDFT = this.getHDFT();
        double qHDFT = q.getHDFT();

        // In order for p to dominate q, p must be at least equally optimal for
        // all optimization functions, and more optimal in at least one
        //AFC求最大值，HDFT求最小值
        if (pAFC > qAFC || pHDFT < qHDFT
                || (pAFC == qAFC && pHDFT == qHDFT)) {
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


    //判断Individual是否是可行 (AFC/HDFT 均等于小于1)
    public boolean isFeasible() {

        return (this.getAFC() <=1 && this.getHDFT()<=1 );
    }

    public boolean isFeasible(Individual individual){
        return (individual.getAFC() <=1 && individual.getHDFT()<=1 );
    }


}
