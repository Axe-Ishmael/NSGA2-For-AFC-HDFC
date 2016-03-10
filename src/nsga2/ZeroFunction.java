package nsga2;

public class ZeroFunction implements ObjectiveFunction {

	public int calculateObjective(Individual individual) {
		int trailingZeros = 0;
		String bitstring = individual.getBitstring();
		
		for(int i = 0; i < bitstring.length(); i++) {
		    if (bitstring.charAt(i) == '0') {
		    	trailingZeros++;
		    }
		}
		
		return trailingZeros;
	}

	public int getObjectiveValue(Individual individual) {
		return individual.getTrailingZeros();
	}

	@Override
	public int getMostValue(Population population) {
		return population.getMostZeros();
	}

	@Override
	public int getLeastValue(Population population) {
		return population.getLeastZeros();
	}

}