package newFunction;

public class Simulator {

    int populationSize = 30; // Population size for each generation 每代的population大小
    int generations = 4; // Number of generations to simulate for  代数

    private double crossoverProbability = 0.2; //交叉率
    private double mutationProbability = 0.1;	//变异率

    private int EDCLength = 10;//EDC数组长度

    public static double timeLimit = 450;//时间限制
    public static int FPGALimit = 4500;//FPGA面积显示

    public void start(){
        Generation generation = null;
        Population initialPopulation = new Population(populationSize,EDCLength);
        initialPopulation.initializeRandom(populationSize);

        for (int generationCounter = 0; generationCounter < generations; generationCounter ++) {
            generation = new Generation(initialPopulation, populationSize, EDCLength);
            generation.nonDominatedSort();
            initialPopulation = generation.createOffspring(crossoverProbability,
                    mutationProbability);
        }

//        generation.nonDominatedSort();

        int frontListSize = generation.getFrontListSize();
        Front front = generation.getFronts().get(frontListSize-1);
        if (front.getIndividuals().size() == 0){
            frontListSize--;
            front = generation.getFronts().get(frontListSize-1);
        }

        front.getIndividuals().forEach(individual -> {
            System.out.println(individual.getAFC() + "," + individual.getHDFC());
        });






    }
}
