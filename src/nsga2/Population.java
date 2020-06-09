/*
 * Copyright (c) 2016 Barnaby Isaac Yves Taylor <github.com/barns>
 * This file is subject to the terms and conditions defined in
 * file 'LICENSE', which is part of this source code package.
 */
package nsga2;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Population {

	private List<Individual> population;
	private List<ObjectiveFunction> objectiveFunctions;

	private int maxBitstringLength;


	//用于计算crowding distance的最大最小值
	private int leastZeros;
	private int mostZeros;
	private int leastOnes;
	private int mostOnes;

	Population(int maxBitstringLength) {
		this.population = new ArrayList<Individual>();
		this.objectiveFunctions = new ArrayList<ObjectiveFunction>();
		this.maxBitstringLength = maxBitstringLength;

		this.leastZeros = 0;
		this.mostZeros = maxBitstringLength;
		this.leastOnes = 0;
		this.mostOnes = maxBitstringLength;

		// Add the relevant objective functions
		//加入不同指标的计算方式
		objectiveFunctions.add(new ZeroFunction());
		objectiveFunctions.add(new OneFunction());
	}

	//随机初始化种群
	public void initializeRandom(int populationSize) {
		Random rand = new Random();

		while (this.population.size() < populationSize) {
			// Calculate random decision variables using given maximum
			int randomZeros = rand.nextInt(maxBitstringLength);
			int randomOnes = rand.nextInt(maxBitstringLength);
			//新产生一个Individual个体，并检查其是否可行，如果可行则加入到Population中去，并且更新最大最小值
			addFeasibleCandidate(randomZeros, randomOnes);
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

	//返回计算不同指标的计算方式，这些计算方式均继承自ObjectiveFunction接口
	public List<ObjectiveFunction> getObjectiveFunctions() {
		return this.objectiveFunctions;
	}

	public int getLeastZeros() {
		return this.leastZeros;
	}

	public int getMostZeros() {
		return this.mostZeros;
	}

	public int getLeastOnes() {
		return this.leastOnes;
	}

	public int getMostOnes() {
		return this.mostOnes;
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
	private void addFeasibleCandidate(int randomZeros, int randomOnes) {
		// Initialize a new individual with random decision variables
		Individual candidate = new Individual(randomZeros, randomOnes);

		// Set the first and second optimization values using two objective
		// functions
		candidate.setTrailingZeros(objectiveFunctions.get(0).calculateObjective(
				candidate));
		candidate.setLeadingOnes(objectiveFunctions.get(1).calculateObjective(
				candidate));


		// 检查是否可行
		if (candidate.isFeasible(this.maxBitstringLength)) {
			// Add to the population
			this.add(candidate);//如果可行则加入到population中去

			// Update the max and min optimization values for the population
			updateMaxAndMin(candidate);//更新最大最小值
		}
	}

	private void updateMaxAndMin(Individual individual) {
		if (individual.getTrailingZeros() < this.leastZeros) {
			this.leastZeros = individual.getTrailingZeros();
		} else if (individual.getTrailingZeros() > this.mostZeros) {
			this.mostZeros = individual.getTrailingZeros();
		}

		if (individual.getLeadingOnes() < this.leastOnes) {
			this.leastOnes = individual.getLeadingOnes();
		} else if (individual.getLeadingOnes() > this.mostOnes) {
			this.mostOnes = individual.getLeadingOnes();
		}
	}
}
