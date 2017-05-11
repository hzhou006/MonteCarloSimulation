package com.monteCarloSimulation;

import org.apache.commons.math3.distribution.NormalDistribution;
import org.apache.commons.math3.stat.descriptive.DescriptiveStatistics;

import java.util.HashMap;
import java.util.Map;

/**
 * Running the simulation
 * 
 * @author Kenny
 *
 */
public class Simulator {
	private Portfolio[] portfolios;
	private Map<Portfolio, SimulatorState> progress;
	
	private double inflationRate;
	private long simulations;
	private int periods;

	public Simulator(Portfolio[] portfolios) {
		this.portfolios = portfolios;
		//init progress
		this.progress = new HashMap<Portfolio, Simulator.SimulatorState>(portfolios.length);
		for(Portfolio portfolio: portfolios){
			SimulatorState simulatorState = new SimulatorState(portfolio.getMean(), portfolio.getStandardDeviation());
			progress.put(portfolio, simulatorState);
		}
		// defaults
		this.inflationRate = 0.035;// inflation rate of 3.5%
		this.periods = 20;//20 years 
		this.simulations = 10000; //run 10,000 simulations of projecting
	}

	public void run() {
		if(simulations <= 0 || periods <= 0 || inflationRate <= 0) return;
		setSimulations(simulations);
		setPeriods(periods);
		setInflationRate(inflationRate);
		for (int i = 0; i < getSimulations(); i++) {
			for (Portfolio portfolio : portfolios) {
				double simResult=portfolio.getInitialInvestment();//starting value
				double mean = portfolio.getMean();
				double standardDeviation = portfolio.getStandardDeviation();
				if( mean <= 0 || standardDeviation <= 0 || simResult <= 0) return;

				for(int j=0;j<getPeriods();j++){
					//get next random sample return for the portfolio
					double ret = progress.get(portfolio).nextSampleReturn();
					
					//each year of value
					simResult = (ret/100+1)*simResult;
					
					//adjust for inflation
					simResult = (1-getInflationRate())*simResult;
				}
				//save result
				progress.get(portfolio).saveSimulationResult(simResult);
			}
		}
		
		//update portfolios at end of simulations
		for (Portfolio portfolio : portfolios) {
			SimulatorState simulatorState = progress.get(portfolio);
			portfolio.setSimulationMedian(simulatorState.getPercentile(50));
			//10th Percentile value among the 10,000 simulations.
			portfolio.setSimulationLast10(simulatorState.getPercentile(10));
			// 90th Percentile value among the 10,000 simulations
			portfolio.setSimulationTop10(simulatorState.getPercentile(90));
		}
	}


	//internal class to save state during a simulation run
	private class SimulatorState{
		private NormalDistribution normalDistribution;
		private DescriptiveStatistics stats;

		public SimulatorState(double mean, double standardDeviation) {
			//init distribution for sampling
			//using default Randomizer
			if(mean <= 0 || standardDeviation <= 0) return;
			this.normalDistribution = new NormalDistribution(mean, standardDeviation);
			
			//to store results and compute percentiles
			this.stats = new DescriptiveStatistics();
		}

		public void saveSimulationResult(double simResult) { 
			this.stats.addValue(simResult);
		}

		public double nextSampleReturn() {			
			return this.normalDistribution.sample();
		}
		
		public double getPercentile(double n){
			return this.stats.getPercentile(n);
		}
	}
	public double getInflationRate() {
		return inflationRate;
	}

	public void setInflationRate(double inflationRate) {
		this.inflationRate = inflationRate;
	}

	public long getSimulations() {
		return simulations;
	}

	public void setSimulations(long simulations) {
		this.simulations = simulations;
	}

	public int getPeriods() {
		return periods;
	}

	public void setPeriods(int periods) {
		this.periods = periods;
	}

}
