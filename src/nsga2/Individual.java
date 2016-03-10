package nsga2;

import java.util.ArrayList;
import java.util.List;

public class Individual {

	// The actual bitstring of the individual
	private String bitString;
	
	// The factors to be optimised
	private int leadingOnes;
	private int trailingZeros;

	// List of individuals being dominated by this individual
	private List<Individual> dominatedIndividuals;

	// The number of individuals dominating this one
	private int dominating;

	// The rank of the individual
	private int rank;

	// Crowding distance of the individual
	private double crowdingDistance;

	Individual(int zeros, int ones) {
		this.dominatedIndividuals = new ArrayList<Individual>();
		this.dominating = 0;
		this.bitString = "";
		
		for (int i = 0; i < zeros; i++) {
			this.bitString += '0';
		}
		
		for (int i = 0; i < ones; i++) {
			this.bitString += '1';
		}
	}

	// Gets the bitstring
	public String getBitstring() {
		return this.bitString;
	}
	
	public void setLeadingOnes(int ones) {
		this.leadingOnes = ones;
	}
	
	public int getLeadingOnes() {
		return this.leadingOnes;
	}
	
	public void setTrailingZeros(int zeros) {
		this.trailingZeros = zeros;
	}
	
	public int getTrailingZeros() {
		return this.trailingZeros;
	}

	// Adds a new individual to the list of individuals being dominated by this
	// one
	public void addDominatedIndividual(Individual q) {
		this.dominatedIndividuals.add(q);
	}

	public List<Individual> getDominatedIndividuals() {
		return this.dominatedIndividuals;
	}

	// Returns true if p (this individual) dominates q (the individual passed
	// as an argument), else returns false
	public boolean dominates(Individual q) {
		int pZeros = this.getTrailingZeros();
		int qZeros = q.getTrailingZeros();
		int pOnes = this.getLeadingOnes();
		int qOnes = q.getLeadingOnes();

		// In order for p to dominate q, p must be at least equally optimal for
		// all optimization functions, and more optimal in at least one
		if (pZeros < qZeros || pOnes < qOnes
				|| (pZeros == qZeros && pOnes == qOnes)) {
			return false;
		}

		return true;
	}

	// Adds one to the number of individuals dominating this one
	public void incrementDominating() {
		this.dominating++;
	}

	// Removes one from the number of individuals dominating this one
	public void decrementDominating() {
		this.dominating--;
	}

	// Returns the number of individuals dominating this one
	public int getDominating() {
		return this.dominating;
	}

	// Returns the rank of this individual
	public int getRank() {
		return this.rank;
	}

	// Sets the rank of this individual
	public void setRank(int r) {
		this.rank = r;
	}

	// Returns the crowding distance of the individual
	public double getCrowdingDistance() {
		return this.crowdingDistance;
	}

	// Sets the crowding distance of the individual
	public void setCrowdingDistance(double d) {
		this.crowdingDistance = d;
	}
	
	public boolean isFeasible(int maxBitstringLength) {
		int changeoverIndex;
		
		if ((changeoverIndex = this.bitString.indexOf('1')) >= 0) {
			if (this.bitString.indexOf('0', changeoverIndex) >= 0) {
				// Some ones precede some zeros
				return false;
			}
		}
		
		return (this.bitString.length() <= maxBitstringLength);
	}
}
