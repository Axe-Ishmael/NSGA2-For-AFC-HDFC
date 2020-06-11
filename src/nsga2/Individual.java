/*
 * Copyright (c) 2016 Barnaby Isaac Yves Taylor <github.com/barns>
 * This file is subject to the terms and conditions defined in
 * file 'LICENSE', which is part of this source code package.
 */

//定义每个个体-- Defination of every Individual
//改写后得Individua类应该包含一个EDC数组
package nsga2;

import java.util.ArrayList;
import java.util.List;

public class Individual {

	// The actual bitstring of the individual
	private String bitString;

	// The factors to be optimised --被优化的参数
	private int leadingOnes;
	private int trailingZeros;

	// List of individuals being dominated by this individual --被该individual支配的individuals组成的List
	private List<Individual> dominatedIndividuals;

	// The number of individuals dominating this one --支配当前该individual的individual数量
	private int dominating;

	// The rank of the individual --individual属于的front层数 = rank
	private int rank;

	// Crowding distance of the individual --计算Crowding distance
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

	//    --向被该individual支配的individuals 组成的List中添加一个新的individual
	public void addDominatedIndividual(Individual q) {
		this.dominatedIndividuals.add(q);
	}
	//返回被该individual支配的individuals组成的List
	public List<Individual> getDominatedIndividuals() {
		return this.dominatedIndividuals;
	}


	//如果当前individual支配作为参数被传入的individual q,则返回true
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


	//-- 支配该individual的individuals数量+1
	public void incrementDominating() {
		this.dominating++;
	}

	// 支配该individual的individuals数量-1
	public void decrementDominating() {
		this.dominating--;
	}

	// 返回支配该individual的individuals的数量
	public int getDominating() {
		return this.dominating;
	}

	// 返回该individual的rank
	public int getRank() {
		return this.rank;
	}

	// 设置该individual的rank rank代表该individual属于第几层front
	public void setRank(int r) {
		this.rank = r;
	}

	// 返回 the crowding distance of the individual
	public double getCrowdingDistance() {
		return this.crowdingDistance;
	}

	// 设置 the crowding distance of the individual
	public void setCrowdingDistance(double d) {
		this.crowdingDistance = d;
	}

	//判断是否是可行 (<maxBitstringLength)
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
