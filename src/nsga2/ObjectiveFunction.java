/*
 * Copyright (c) 2016 Barnaby Isaac Yves Taylor <github.com/barns>
 * This file is subject to the terms and conditions defined in
 * file 'LICENSE', which is part of this source code package.
 */
package nsga2;

/**
 * Assumes that both functions are minimization problems. Multiply by -1 to
 * convert a minimization function to a maximization one.
 */
public interface ObjectiveFunction {

	// Returns the objective for this function (e.g. zeros, ones), using the
	// given individual's decision variables
	public int calculateObjective(Individual individual);

	// Returns the objective value of the given individual. A generic getter is
	// useful for assigning crowding distance
	public int getObjectiveValue(Individual individual);

	// Returns the maximum objective value for the given population. A generic
	// getter is useful for assigning crowding distance
	public int getMostValue(Population population);

	// Returns the minimum objective value for the given population. A generic
	// getter is useful for assigning crowding distance
	public int getLeastValue(Population population);
}
