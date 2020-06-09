/*
 * Copyright (c) 2016 Barnaby Isaac Yves Taylor <github.com/barns>
 * This file is subject to the terms and conditions defined in
 * file 'LICENSE', which is part of this source code package.
 */
package nsga2;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Generation {

	private int populationSize;

	private Population existingPopulation; // Corresponds to Pt  t代
	private Population population; // Corresponds to P(t+1)  t+1代
	private List<Individual> matingPool;//交配池

	private List<Front> fronts;//前沿List

	private int maxBitstringLength;

	Generation(Population existingPopulation, int populationSize, int maxBitstringLength) {
		this.populationSize = populationSize;
		this.existingPopulation = existingPopulation;
		this.fronts = new ArrayList<Front>();
		this.maxBitstringLength = maxBitstringLength;
	}

	public Population getPopulation() {
		return this.existingPopulation;
	}

	// Sort the existing population based on non-domination 对当前种群进行非支配排序
	public void nonDominatedSort() {
		int frontCounter = 0;
		population = new Population(this.maxBitstringLength);

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

	//交叉
	private void crossover(double crossoverProbability, double mutationProbability) {
		Random rand = new Random();

		for (int i = 0; i < matingPool.size(); i++) {
			Individual parent1 = matingPool.get(i);
			Individual parent2;
			String chromosome1;
			String chromosome2;
			String[] offspring;

			if (Math.random() < crossoverProbability) {
				int j;

				do {
					j = rand.nextInt(matingPool.size() / 2);
					parent2 = matingPool.get(j);
				} while (i == j);

				chromosome1 = parent1.getBitstring();
				chromosome2 = parent2.getBitstring();

				offspring = crossover(chromosome1, chromosome2);

				parent1 = newIndividualFromChromosome(mutateChromosome(offspring[0], mutationProbability));

				parent2 = newIndividualFromChromosome(mutateChromosome(offspring[1], mutationProbability));

				if (!parent1.isFeasible(this.maxBitstringLength)) {
					matingPool.remove(i);
				}

				if (!parent2.isFeasible(this.maxBitstringLength)) {
					matingPool.remove(j);
				}
			}
		}
	}

	private Individual crowdedTournamentSelection(Individual candidate,
			Individual oponent) {
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

	//通过chromosome1、chromosome2交换一部分，组合形成新的offspring
	private String[] crossover(String chromosome1, String chromosome2) {
		int crossoverPoint1 = 0;
		int crossoverPoint2 = 0;
		String[] offsprings = new String[2];

		//首先确定要进行Crossover的点位
		//int indexOf(String str): 返回指定字符在字符串中第一次出现处的索引，如果此字符串中没有这样的字符，则返回 -1。
		crossoverPoint1 = chromosome1.indexOf('1');
		crossoverPoint2 = chromosome2.indexOf('1');

		if (crossoverPoint1 == -1 || crossoverPoint1 == chromosome1.length()) {
			crossoverPoint1 = chromosome1.length() - 1;
		}

		if (crossoverPoint2 == -1 || crossoverPoint2 == chromosome2.length()) {
			crossoverPoint2 = chromosome2.length() - 1;
		}

		offsprings[0] = chromosome1.substring(0, crossoverPoint1 + 1) + chromosome2.substring(crossoverPoint2 + 1);
		offsprings[1] = chromosome2.substring(0, crossoverPoint2 + 1) + chromosome1.substring(crossoverPoint1 + 1);

		return offsprings;
	}

	private Individual newIndividualFromChromosome(String chromosome) {
		int decisionVariable1 = 0;
		int decisionVariable2 = 0;

		for (int i = 0; i < chromosome.length(); i++) {
			//charAt(int index) 方法用于返回指定索引处的字符。索引范围为从 0 到 length() - 1。
			if (chromosome.charAt(i) == '1') {
				decisionVariable1++;
			} else if (chromosome.charAt(i) == '2') {
				decisionVariable2++;
			}
		}

		return new Individual(decisionVariable1, decisionVariable2);
	}

	//变异操作
	private String mutateChromosome(String chromosome, double mutationProbability) {
		boolean flipZero = Math.random() < mutationProbability;
		boolean flipOne = Math.random() < mutationProbability;

		int flipIndex = chromosome.indexOf('1');

		if (flipZero) {
			if (flipIndex != 0) {
				if (flipIndex == -1) {
					flipIndex = chromosome.length();
				}

				chromosome = chromosome.substring(0, flipIndex - 1) + "1" + chromosome.substring(flipIndex);
			}
		} else if (flipOne) {
			if (flipIndex != -1) {
				chromosome = chromosome.substring(0, flipIndex) + "0" + chromosome.substring(flipIndex + 1);
			}
		}

		return chromosome;
	}
}
