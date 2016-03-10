package nsga2;

public class OneFunction implements ObjectiveFunction {

	public int calculateObjective(Individual individual) {
		int leadingOnes = 0;
		String bitstring = individual.getBitstring();
		
		for(int i = 0; i < bitstring.length(); i++) {
		    if (bitstring.charAt(i) == '1') {
		    	leadingOnes++;
		    }
		}
		
		return leadingOnes;
	}

	public int getObjectiveValue(Individual individual) {
		return individual.getLeadingOnes();
	}

	@Override
	public int getMostValue(Population population) {
		return population.getMostOnes();
	}

	@Override
	public int getLeastValue(Population population) {
		return population.getLeastOnes();
	}

}