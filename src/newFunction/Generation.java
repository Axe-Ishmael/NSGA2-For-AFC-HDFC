package newFunction;


import java.util.List;

public class Generation {

    private int populationSize;

    private nsga2.Population existingPopulation; // Corresponds to Pt  t代
    private Population population; // Corresponds to P(t+1)  t+1代
    private List<Individual> matingPool;//交配池

    private List<Front> fronts;//前沿List


}
