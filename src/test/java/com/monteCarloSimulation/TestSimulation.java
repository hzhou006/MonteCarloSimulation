package com.monteCarloSimulation;

import org.junit.Test;

/**
 * Run a test simulation
 * @author Kenny
 *
 */
public class TestSimulation {

	@Test
	public void testSimulation(){
		Portfolio aggressive = new Portfolio("Aggressive", 100000, 0.094324, 0.15675);
		Portfolio conservative = new Portfolio("Very Conservative", 100000, 0.06189, 0.063438);

		Simulator simulator = new Simulator(aggressive, conservative);
		simulator.run();

		System.out.println(aggressive);
		System.out.println(conservative);
	}
}
