/*
 * Copyright (c) 2016 Barnaby Isaac Yves Taylor <github.com/barns>
 * This file is subject to the terms and conditions defined in
 * file 'LICENSE', which is part of this source code package.
 */
 package nsga2;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class Front {

	// List to store all individuals in the front
	List<Individual> front;

	Front() {
		front = new ArrayList<Individual>();
	}

	// Adds a new individual to the front
	public void add(Individual individual) {
		this.front.add(individual);
	}

	// Returns the number of individuals in the front
	public int size() {
		return this.front.size();
	}

	// Returns a list of all individuals in the front
	public List<Individual> getIndividuals() {
		return this.front;
	}

	// Removes and returns the first individual in the front
	public Individual pop() {
		return this.front.remove(0);
	}

	// Sets the ranks of all individuals in the front
	public void setIndividualRanks(int frontCounter, Front frontQ) {
		front.forEach((p) -> {
			p.getDominatedIndividuals().forEach((q) -> {
				// Decrement the domination count for individual q
					q.decrementDominating();

					// If q is not dominated by any individuals then it belongs
					// to
					// front Q
					if (q.getDominating() == 0) {
						q.setRank(frontCounter + 1);
						frontQ.add(q);
					}
				});
		});
	}

	// Assigns the crowding distance of each individual in the front which is
	// gives the euclidian distance between each individual based on their m
	// objectives in m dimensional space (for this problem m = 2)
	public void assignCrowdingDistance(Population population) {

		population.getObjectiveFunctions().forEach((objectiveFunction) -> {
			Collections.sort(this.front, new Comparator<Individual>() {
				@Override
				public int compare(Individual i, Individual j) {
					return Double.compare(
							objectiveFunction.getObjectiveValue(i),
							objectiveFunction.getObjectiveValue(j));
				}
			});

			// Assign a large distance to the boundary solutions
			front.get(0).setCrowdingDistance(Double.MAX_VALUE);
			front.get(front.size() - 1).setCrowdingDistance(Double.MAX_VALUE);

			for (int k = 1; k < front.size() - 1; k++) {
				double crowdingDistance = front.get(k).getCrowdingDistance();
				double nextIndividualValue = objectiveFunction
						.getObjectiveValue(front.get(k + 1));
				double lastIndividualValue = objectiveFunction
						.getObjectiveValue(front.get(k - 1));
				double valueDifference = objectiveFunction
						.getMostValue(population)
						- objectiveFunction.getLeastValue(population);

				crowdingDistance += (nextIndividualValue - lastIndividualValue)
						/ valueDifference;
				front.get(k).setCrowdingDistance(crowdingDistance);
			}
		});
	}

	public void sortByCrowdingDistance() {
		// Sorts the front in descending order of crowding distance
		Collections.sort(front, new Comparator<Individual>() {
			@Override
			public int compare(Individual p, Individual q) {
				return Double.compare(q.getCrowdingDistance(),
						p.getCrowdingDistance());
			}
		});
	}
}
