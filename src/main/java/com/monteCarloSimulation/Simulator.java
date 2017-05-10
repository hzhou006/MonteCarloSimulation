package com.monteCarloSimulation;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.math3.distribution.NormalDistribution;
import org.apache.commons.math3.stat.descriptive.DescriptiveStatistics;

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

	public Simulator(Portfolio... portfolios) {
		this.portfolios = portfolios;
		
		//init progress
		this.progress = new HashMap<Portfolio, Simulator.SimulatorState>(portfolios.length);
		for(Portfolio p: portfolios){
			SimulatorState simulatorState = new SimulatorState(p.getMean(),p.getStandardDeviation());
			progress.put(p, simulatorState);
		}

		// defaults
		this.inflationRate = 0.035;// inflation rate of 3.5%
		this.periods = 20;//20 years 
		this.simulations = 10000; //run 10,000 simulations of projecting
	}

	public void run() {
		for (int i = 0; i < simulations; i++) {
			for (Portfolio p : portfolios) {
				double simResult=p.getInitialInvestment();//starting value
				for(int j=0;j<periods;j++){
					//get next random sample return for the portfolio
					double r = progress.get(p).nextSampleReturn();
					
					//end of period value
					simResult = (1+r)*simResult;
					
					//adjust for inflation
					simResult = (1-inflationRate)*simResult;
				}
				//save result
				progress.get(p).saveSimulationResult(simResult);
			}
		}
		
		//update portfolios at end of simulations
		for (Portfolio p : portfolios) {
			SimulatorState simulatorState = progress.get(p);
			p.setSimulationMedian(simulatorState.getPercentile(50));
			p.setSimulationBottom10(simulatorState.getPercentile(10));
			p.setSimulationTop10(simulatorState.getPercentile(90));
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
