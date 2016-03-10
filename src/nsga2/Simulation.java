/*
 * Copyright (c) 2016 Barnaby Isaac Yves Taylor <github.com/barns>
 * This file is subject to the terms and conditions defined in
 * file 'LICENSE', which is part of this source code package.
 */
package nsga2;

public class Simulation {

	int populationSize = 5000; // Population size for each generation
	int generations = 10; // Number of generations to simulate for

	private double crossoverProbability = 0.5;
	private double mutationProbability = 0.1;

	int maxBitstringLength = 50; // Maximum allowable bitstring length

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
