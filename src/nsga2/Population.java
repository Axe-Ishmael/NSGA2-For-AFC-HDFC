package nsga2;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Population {

	private List<Individual> population;
	private List<ObjectiveFunction> objectiveFunctions;
	
	private int maxBitstringLength;

	// Min and max optimization values used to calculate crowding distance
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
		objectiveFunctions.add(new ZeroFunction());
		objectiveFunctions.add(new OneFunction());
	}

	public void initializeRandom(int populationSize) {
		Random rand = new Random();

		while (this.population.size() < populationSize) {
			// Calculate random decision variables using given maximum
			int randomZeros = rand.nextInt(maxBitstringLength);
			int randomOnes = rand.nextInt(maxBitstringLength);
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

	private void addFeasibleCandidate(int randomZeros, int randomOnes) {
		// Initialize a new individual with random decision variables
		Individual candidate = new Individual(randomZeros, randomOnes);

		// Set the first and second optimization values using two objective
		// functions
		candidate.setTrailingZeros(objectiveFunctions.get(0).calculateObjective(
				candidate));
		candidate.setLeadingOnes(objectiveFunctions.get(1).calculateObjective(
				candidate));

		// System.out.println(candidate.calculateStress() + " < " +
		// allowableStrength + "; " + candidate.getDeflection() + "<" +
		// maxDeflection);
		// Check whether candidate is feasible
		if (candidate.isFeasible(this.maxBitstringLength)) {
			// Add to the population
			this.add(candidate);

			// Update the max and min optimization values for the population
			updateMaxAndMin(candidate);
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
