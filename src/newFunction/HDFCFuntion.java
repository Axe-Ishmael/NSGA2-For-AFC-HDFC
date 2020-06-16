package newFunction;


import java.util.List;

public class HDFCFuntion implements ObjectiveFunction {
    @Override
    public double calculateObjective(Individual individual) {
        double average = Average_Coverage(individual.getEdcList());
        double sum = 0;
        for (int i = 0; i < individual.getEdcList().size(); i++)
        {
            sum += (individual.getEdcList().get(i).getError_Coverage() - average)*(individual.getEdcList().get(i).getError_Coverage() - average);
        }
        sum = Math.sqrt(sum / individual.getEdcList().size());
        return sum;
    }

    @Override
    public double getObjectiveValue(Individual individual) {
        return individual.getHDFC();
    }

    @Override
    public double getMostValue(Population population) {
        return population.getMaxHDFC();
    }

    @Override
    public double getLeastValue(Population population) {
        return population.getLeastHDFC();
    }

    //求解平均错误覆盖率
    public static double Average_Coverage(List<EDC> edc)
    {
        double sum = 0.0;
        for (int i = 0; i < edc.size(); i++)
        {
            sum += edc.get(i).getError_Coverage();
        }
        return sum / (edc.size());
    }
}
