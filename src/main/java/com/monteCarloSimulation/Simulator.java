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
		for (int i = 0; i < simulations; i++) {
			for (Portfolio portfolio : portfolios) {
				double simResult=portfolio.getInitialInvestment();//starting value
				for(int j=0;j<periods;j++){
					//get next random sample return for the portfolio
					double ret = progress.get(portfolio).nextSampleReturn();
					
					//end of period value
					simResult = (1+ret)*simResult;
					
					//adjust for inflation
					simResult = (1-inflationRate)*simResult;
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
}
