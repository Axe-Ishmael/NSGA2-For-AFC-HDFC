package newFunction;


import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Generation {

    private int populationSize;

    private Population existingPopulation; // Corresponds to Pt  t代
    private Population population; // Corresponds to P(t+1)  t+1代
    private List<Individual> matingPool;//交配池

    private List<Front> fronts;//前沿List

    private int EDCLength;//EDC数组长度

    public Generation(Population existingPopulation, int populationSize,int EDCLength){
        this.populationSize = populationSize;
        this.existingPopulation = existingPopulation;
        this.fronts = new ArrayList<Front>();
        this.EDCLength = EDCLength;

    }

    public Population getPopulation() {
        return this.existingPopulation;
    }

    // Sort the existing population based on non-domination 对当前种群进行非支配排序
    public void nonDominatedSort() {
        int frontCounter = 0;
        population = new Population(populationSize,EDCLength);

        // Create front 1 创建第一层前沿
        fronts.add(new Front());

        for (int i = 0; i < existingPopulation.size(); i++) {
            existingPopulation.setDomination(i);

            // If no individuals dominate p, then it belongs to the first front
            //如果i没有被任何其他个体支配，则它属于第一层front
            if (existingPopulation.get(i).getDominating() == 0) {
                // Set rank of p to 1, since front counter is zero-indexed
                //将i的rank设置为1，说明i属于第一层front
                existingPopulation.get(i).setRank(frontCounter + 1);
                // Add p to the first front
                //将i添加进第一层front
                fronts.get(frontCounter).add(existingPopulation.get(i));
            }
        }

        // While ith front is non-empty and there is still room in the new
        // population
        //当第i层front不为空，且在population（不是existingPopulation）中还有剩余的位置时
        while (fronts.get(frontCounter).size() > 0
                && population.size() < populationSize) {
            // Create front Q (the (i+1)th front) and add to fronts list
            //创建front Q(第i+1层front)并加入fronts list
            Front front = fronts.get(frontCounter);
            fronts.add(new Front());

            //将front(ith)中的个体进一步进行层次划分，将划分出的个体加入到(i+1)th frontQ中,对应的rank为i+1
            front.setIndividualRanks(frontCounter, fronts.get(frontCounter + 1));
            if (population.size() + front.size() <= populationSize) {
                population.addAll(front.getIndividuals());
            } else {
                front.assignCrowdingDistance(existingPopulation);
                front.sortByCrowdingDistance();
                while (population.size() < populationSize) {
                    population.add(front.pop());
                }
            }

            frontCounter++;
        }

        // Selection complete, make P(t+1) into Pt
        existingPopulation = population;
    }

    public Population createOffspring(double crossoverProbability, double mutationProbability) {
        spawnMatingPool();//初始化MatingPool

        crossover(crossoverProbability, mutationProbability);

        existingPopulation.addAll(matingPool);

        return existingPopulation;
    }


    //产生交配池
    private void spawnMatingPool() {

        matingPool = new ArrayList<Individual>();
        int populationSize = existingPopulation.size();

        for (int i = 0; i < populationSize; i++) {
            Individual candidate = existingPopulation.get(i);
            Individual oponent;

            if (i != populationSize - 1) {
                oponent = existingPopulation.get(i + 1);
            } else {
                oponent = existingPopulation.get(0);
            }

            //返回两个参数中rank更高，或rank相同情况下crowding distance更大的加入matingPool
            matingPool.add(crowdedTournamentSelection(candidate, oponent));
        }
    }


    private Individual crowdedTournamentSelection(Individual candidate,Individual oponent) {
        // If both individuals have different non-domination ranks, this is used
        // to decide the winner
        if (candidate.getRank() != oponent.getRank()) {
            // The individual with the higher non-domination rank wins
            if (candidate.getRank() > oponent.getRank()) {
                return candidate;
            } else {
                return oponent;
            }
        } else {
            // The individual with the better crowding distance (higher, or less
            // crowded) wins
            if (candidate.getCrowdingDistance() > oponent.getCrowdingDistance()) {
                return candidate;
            } else {
                return oponent;
            }
        }
    }

    //交叉
    private void crossover(double crossoverProbability, double mutationProbability) {
        Random rand = new Random();

        for (int i = 0; i < matingPool.size(); i++) {
            Individual parent1 = matingPool.get(i);
            Individual parent2;

            double[] cross_error_Coverage = new double[2];
            double error_Coverage1;
            double error_Coverage2;


            if (Math.random() < crossoverProbability) {
                int j;

                do {
                    j = rand.nextInt(matingPool.size() / 2);
                    parent2 = matingPool.get(j);
                } while (i == j);



                int m ,k;
                do{
                    m = rand.nextInt(parent1.getEdcList().size()/2);//确定进行EDC值突变的位置
                    k = rand.nextInt(parent1.getEdcList().size()/2);//确定进行EDC值突变的位置
                }while (m == k);



                error_Coverage1 = parent1.getEdcList().get(m).getError_Coverage();
                error_Coverage2 = parent2.getEdcList().get(m).getError_Coverage();
                cross_error_Coverage[0] = (error_Coverage1*mutationProbability)+(1-mutationProbability)*error_Coverage2;

                error_Coverage1 = parent1.getEdcList().get(k).getError_Coverage();
                error_Coverage2 = parent2.getEdcList().get(k).getError_Coverage();
                cross_error_Coverage[1] = (error_Coverage1*mutationProbability)+(1-mutationProbability)*error_Coverage2;

                List<EDC> edcList1 = parent1.setEDCList(m,cross_error_Coverage[0]);
                List<EDC> edcList2 = parent2.setEDCList(k,cross_error_Coverage[1]);


                parent1 = new Individual(edcList1);
                parent2 = new Individual(edcList2);



                if (!parent1.isFeasible(parent1)) {
                    matingPool.remove(i);
                }

                if (!parent2.isFeasible(parent2)) {
                    matingPool.remove(j);
                }
            }
        }
    }

    public List<Front> getFronts(){
        return this.fronts;
    }
    public int getFrontListSize(){
        return fronts.size();
    }


}
