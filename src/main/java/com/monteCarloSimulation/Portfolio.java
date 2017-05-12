package com.monteCarloSimulation;

import java.text.DecimalFormat;

/**
 * A portfolio
 * @author Kenny
 *
 */
public class Portfolio {
	private String name;
	private double initialInvestment;
	private double mean;//return
	private double standardDeviation;//risk
	
	//simulated values
	private double simulationMedian;
	private double simulationTop10;
	private double simulationLast10;
	private DecimalFormat percent;
	private DecimalFormat val;	
	public Portfolio(String name, double initialInvestment, double mean, double standardDeviation) {
    	        this.name=name;
		this.initialInvestment=initialInvestment;
		this.mean=mean;
		this.standardDeviation=standardDeviation;	
		this.percent = new DecimalFormat("##.##%");
		this.val = new DecimalFormat("##.##");
        }
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public double getInitialInvestment() {
		return initialInvestment;
	}

	public void setInitialInvestment(double initialInvestment) {
		this.initialInvestment = initialInvestment;
	}

	public Double getMean() {
		return mean;
	}

	public void setMean(Double mean) {
		this.mean = mean;
	}

	public double getStandardDeviation() {
		return standardDeviation;
	}

	public void setStandardDeviation(double standardDeviation) {
		this.standardDeviation = standardDeviation;
	}

	public double getSimulationMedian() {
		return simulationMedian;
	}

	public void setSimulationMedian(double simulationMedian) {
		this.simulationMedian = simulationMedian;
	}

	public double getSimulationTop10() {
		return simulationTop10;
	}

	public void setSimulationTop10(double simulationTop10) {
		this.simulationTop10 = simulationTop10;
	}

	public double getSimulationLast10() {
		return simulationLast10;
	}

	public void setSimulationLast10(double simulationLast10) {
		this.simulationLast10 = simulationLast10;
	}

	@Override
	public String toString() {
		return "Portfolio [Name=" + name + ", InitialInvestment="
				+ val.format(initialInvestment) + ", Return=" + percent.format(mean) + ", Risk="
				+ percent.format(standardDeviation) + ", MedianOf20thYear=" + val.format(simulationMedian)
				+ ", 10% Best=" + val.format(simulationTop10)
				+ ", 10% Worst=" + val.format(simulationLast10) + "]";
	}
	
}
