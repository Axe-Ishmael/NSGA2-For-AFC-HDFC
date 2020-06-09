/*
 * Copyright (c) 2016 Barnaby Isaac Yves Taylor <github.com/barns>
 * This file is subject to the terms and conditions defined in
 * file 'LICENSE', which is part of this source code package.
 */
package nsga2;

public class Simulation {

	int populationSize = 100; // Population size for each generation 每代的population大小
	int generations = 10; // Number of generations to simulate for  代数

	private double crossoverProbability = 0.5; //交叉率
	private double mutationProbability = 0.1;	//变异率

	int maxBitstringLength = 10; // Maximum allowable bitstring length 最大允许的位串长度

	public void start() {
		Generation generation = null;
		Population initialPopulation = new Population(maxBitstringLength);
		initialPopulation.initializeRandom(populationSize);

		for (int generationCounter = 0; generationCounter < generations; generationCounter ++) {
			generation = new Generation(initialPopulation, populationSize, maxBitstringLength);
			generation.nonDominatedSort();
			initialPopulation = generation.createOffspring(crossoverProbability,
					mutationProbability);
		}

		generation.nonDominatedSort();
		generation.getPopulation().getAll().forEach((individual) -> {
			System.out.println(individual.getLeadingOnes() + "," + individual.getTrailingZeros());
		});
	}
}
