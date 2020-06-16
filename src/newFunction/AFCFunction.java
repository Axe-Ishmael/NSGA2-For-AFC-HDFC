package newFunction;




import java.util.List;

public class AFCFunction implements ObjectiveFunction {

    @Override
    public double calculateObjective(Individual individual) {
        double sum = 0.0;
        for (int i = 0; i < individual.getEdcList().size(); i++)
        {
            sum += individual.getEdcList().get(i).getError_Coverage();
        }
        return sum / (individual.getEdcList().size());
    }

    @Override
    public double getObjectiveValue(Individual individual) {
        return individual.getAFC();
    }

    @Override
    public double getMostValue(Population population) {
        return population.getMaxAFC();
    }

    @Override
    public double getLeastValue(Population population) {
        return population.getMaxAFC();
    }
}
