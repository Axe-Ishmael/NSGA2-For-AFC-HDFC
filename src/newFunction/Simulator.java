package newFunction;

public class Simulator {

    int populationSize = 10; // Population size for each generation 每代的population大小
    int generations = 3; // Number of generations to simulate for  代数

    private double crossoverProbability = 0.8; //交叉率
    private double mutationProbability = 0.8;	//变异率

    private int EDCLength = 8;//EDC数组长度

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

        generation.nonDominatedSort();
        generation.getPopulation().getAll().forEach((individual) -> {
            System.out.println(individual.getAFC() + "," + individual.getHDFT());
        });

//        for (int i = 0;i<generation.getPopulation().getAll().size();i++){
//            System.out.println(generation.getPopulation().getAll().get(i).getAFC()+","+generation.getPopulation().getAll().get(i).getHDFT());
//        }


    }
}
