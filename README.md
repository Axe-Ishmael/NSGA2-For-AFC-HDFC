# Java Implementation of NSGA-II

## Introduction
This is an implementation of NSGA-II, written in Java, based upon the description contained in the book *Multi-Objective Optimisation using Evolutionary Algorithms* by Kalyanmoy Deb, using the "Leading Ones, Trailing Zeros (LOTZ) problem.

Java isn't the most efficient language, so this algorithm won't run as quickly as in, say, C++, but some find it easier to understand, and it's useful for some applications.

## Usage
To start, simply download and run the `main()` method contained within `NSGA2.java`. You can alter parameters such as the population size and number of generations to run for from within the `Simulator` class.

If you want to optimise a problem other than LOTZ, you can create your own objective function classes, which extend the `ObjectiveFunction` interface. You'll also have to rewrite the mutation, and probably the crossover methods, since they're very specific to the type of data being optimised, and finally you'll probably have to adjust some of the code to suit, but I'll let you read through and work that out for yourself :)
