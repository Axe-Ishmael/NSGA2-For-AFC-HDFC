package newFunction;




import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Population {

    private List<Individual> population;
    private List<ObjectiveFunction> objectiveFunctions;
    private int PopulationSize;//定义种群规模

    int EDCLength;//定义每个Individual的EDC数组长度

    //用于计算crowding distance的最大最小值
    private double leastAFC;
    private double maxAFC;
    private double leastHDFT;
    private double maxHDFT;

    public Population(int PopulationSize,int EDCLength){
        this.population = new ArrayList<Individual>();
        this.objectiveFunctions = new ArrayList<ObjectiveFunction>();
        this.PopulationSize = PopulationSize;
        this.EDCLength = EDCLength;

        this.leastAFC = 0;
        this.leastHDFT = 0;
        this.maxAFC = 1.0;
        this.maxHDFT = 1.0;

        objectiveFunctions.add(new AfcFunction());
        objectiveFunctions.add(new HdfcFuntion());

    }

    public void initializeRandom(int populationSize){

        while (this.population.size() < populationSize){
            addFeasibleCandidate(this.EDCLength);
        }

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

    public double getLeastHDFT() {
        return leastHDFT;
    }

    public void setLeastHDFT(double leastHDFT) {
        this.leastHDFT = leastHDFT;
    }

    public double getMaxHDFT() {
        return maxHDFT;
    }

    public void setMaxHDFT(double maxHDFT) {
        this.maxHDFT = maxHDFT;
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
        Individual candidate = new Individual(EDCLength);

        // 检查是否可行
        if (candidate.isFeasible()) {
            // Add to the population
            this.add(candidate);//如果可行则加入到population中去

            // Update the max and min optimization values for the population
            updateMaxAndMin(candidate);//更新最大最小值
        }
    }

    private void updateMaxAndMin(Individual individual) {
        if (individual.getAFC() < this.leastAFC) {
            this.leastAFC = individual.getAFC();
        } else if (individual.getAFC() > this.maxAFC) {
            this.maxAFC = individual.getAFC();
        }

        if (individual.getHDFT() < this.leastHDFT) {
            this.leastHDFT = individual.getHDFT();
        } else if (individual.getHDFT() > this.maxHDFT) {
            this.maxHDFT = individual.getHDFT();
        }
    }


}
