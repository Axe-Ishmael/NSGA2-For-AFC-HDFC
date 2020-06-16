package newFunction;




import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class Population {

    private List<Individual> population;
    private List<ObjectiveFunction> objectiveFunctions;
    private int PopulationSize;//定义种群规模
    private double[][] funcValues;//存储population中所有个体的AFC和HDFC值 [0][x]:AFC [1][x]:HDFC

    int EDCLength;//定义每个Individual的EDC数组长度

    //用于计算crowding distance的最大最小值
    private double leastAFC;
    private double maxAFC;
    private double leastHDFC;
    private double maxHDFC;

    public Population(int PopulationSize,int EDCLength){
        this.population = new ArrayList<Individual>();
        this.objectiveFunctions = new ArrayList<ObjectiveFunction>();
        this.PopulationSize = PopulationSize;
        this.EDCLength = EDCLength;


        this.leastAFC = 1.0;
        this.leastHDFC = 1.0;
        this.maxAFC = 0;
        this.maxHDFC = 0;

        objectiveFunctions.add(new AFCFunction());
        objectiveFunctions.add(new HDFCFuntion());

    }

    public void initializeRandom(int populationSize){

        while (this.population.size() < populationSize){
            addFeasibleCandidate(this.EDCLength);
        }
        initFuncValues();
//        initMinMax(funcValues);

    }


    public Individual get(int i) {
        return this.population.get(i);
    }

    public List<Individual> getAll() {
        return this.population;
    }

    public void add(Individual individual) {
        this.population.add(individual);
    }

    public void addAll(List<Individual> individuals) {
        this.population.addAll(individuals);
    }

    public Individual remove(int i) {
        return this.population.remove(i);
    }

    public int size() {
        return this.population.size();
    }

    public double getLeastAFC() {
        return leastAFC;
    }

    public void setLeastAFC(double leastAFC) {
        this.leastAFC = leastAFC;
    }

    public double getMaxAFC() {
        return maxAFC;
    }

    public void setMaxAFC(double maxAFC) {
        this.maxAFC = maxAFC;
    }

    public double getLeastHDFC() {
        return leastHDFC;
    }

    public void setLeastHDFC(double leastHDFC) {
        this.leastHDFC = leastHDFC;
    }

    public double getMaxHDFC() {
        return maxHDFC;
    }

    public void setMaxHDFC(double maxHDFC) {
        this.maxHDFC = maxHDFC;
    }

    //返回计算不同指标的计算方式，这些计算方式均继承自ObjectiveFunction接口 暂时没用
    public List<ObjectiveFunction> getObjectiveFunctions() {
        return this.objectiveFunctions;
    }

    //判断在整个Population中 第i个individual被多少其他individual支配，同时第i个individual又支配了多少个其他individual
    public void setDomination(int i) {
        Individual p = this.population.get(i);

        for (int j = 0; j < this.population.size(); j++) {
            if (i != j) {
                Individual q = this.population.get(j);

                if (p.dominates(q)) {
                    p.addDominatedIndividual(q);
                } else if (q.dominates(p)) {
                    p.incrementDominating();
                }
            }
        }
    }

    //新产生一个Individual个体，并检查其是否可行，如果可行则加入到Population中去，并且更新最大最小值
    private void addFeasibleCandidate(int EDCLength) {
        // Initialize a new individual with random decision variables
        Individual candidate = null;

        do {
            candidate = new Individual(EDCLength);
        }while (!candidate.isFeasible());// 检查是否可行

        // Add to the population
        this.add(candidate);//如果可行则加入到population中去

        // Update the max and min optimization values for the population
        updateMaxAndMin(candidate);//更新最大最小值


    }

    private void updateMaxAndMin(Individual individual) {

        double idvAFC = individual.getAFC();
        double idvHDFC = individual.getHDFC();
        this.leastAFC = Math.min(this.leastAFC,idvAFC);
        this.maxAFC = Math.max(this.maxAFC,idvAFC);
        this.leastHDFC = Math.min(this.leastHDFC,idvHDFC);
        this.maxHDFC = Math.max(this.maxHDFC,idvHDFC);
    }


    //记录population中所有个体的目标函数值AFC HDFC
    private void initFuncValues(){
        funcValues = new double[2][population.size()];
        for (int i = 0;i < population.size();i++){
            funcValues[0][i] = population.get(i).getAFC();
            funcValues[1][i] = population.get(i).getHDFC();
        }
    }

    private void initMinMax(double[][] funcValues){

        List<Double> afcValues = new ArrayList<Double>();
        List<Double> hdfcValues = new ArrayList<Double>();

        for (int i = 0;i < funcValues[0].length;i++){
            afcValues.add(funcValues[0][i]);
            hdfcValues.add(funcValues[1][i]);
        }

        this.leastAFC = Collections.min(afcValues);
        this.leastHDFC = Collections.min(hdfcValues);
        this.maxAFC = Collections.max(afcValues);
        this.maxHDFC = Collections.max(hdfcValues);

    }


}
