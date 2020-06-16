package newFunction;


import java.util.List;

public interface ObjectiveFunction {
    // Returns the objective for this function (e.g. zeros, ones), using the
    // given individual's decision variables
    public double calculateObjective(Individual individual);

    // Returns the objective value of the given individual. A generic getter is
    // useful for assigning crowding distance
    public double getObjectiveValue(Individual individual);

    // Returns the maximum objective value for the given population. A generic
    // getter is useful for assigning crowding distance
    public double getMostValue(Population population);

    // Returns the minimum objective value for the given population. A generic
    // getter is useful for assigning crowding distance
    public double getLeastValue(Population population);
}
