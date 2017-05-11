package com.monteCarloSimulation;

import org.junit.Test;

/**
 * Run a test simulation
 * @author Kenny
 *
 */
public class TestSimulation {

    
	@Test
    public void initialMeanOrReturnInValid() {
        //Initial mean or return must be invalid
        System.out.println("-------testing error check if return or mean is invalid------");
        Portfolio aggressive = new Portfolio("Aggressive", 100000, 0.0, 0.0);
        Portfolio conservative = new Portfolio("Very Conservative", 100000, 0.0, 0.0);
        Portfolio[] portfolios = new Portfolio[]{aggressive,conservative};

		Simulator simulator = new Simulator(portfolios);
		simulator.run();
		System.out.println(aggressive);
		System.out.println(conservative);
    }
    @Test
    public void initialInvestmentInValid() {
        //Initial investment must be greater than equal to zero
        System.out.println("-------testing error check if the investment is invalid------");
        Portfolio aggressive = new Portfolio("Aggressive", 0, 0.094324, 0.15675);
        Portfolio conservative = new Portfolio("Very Conservative", 0, 0.06189, 0.063438);
        Portfolio[] portfolios = new Portfolio[]{aggressive,conservative};

		Simulator simulator = new Simulator(portfolios);
		simulator.run();

		System.out.println(aggressive);
		System.out.println(conservative);
    }
	@Test
	public void testSimulation(){
		
        System.out.println("-------testing if add muti portfolio------");    
        
        final Object[][] obj = new Object[][]{
        	{"Aggressive", 100000, 0.094324, 0.15675},
        	{"Very Conservative", 100000, 0.06189, 0.063438}
        };
        Portfolio[] portfolios = new Portfolio[obj.length];
        for (int i=0; i<obj.length; i++) {
        	 Portfolio portfolio = new Portfolio(obj[i][0].toString(), new Double(obj[i][1].toString()), new Double(obj[i][2].toString()),new Double(obj[i][3].toString()));
        	 portfolios[i] = portfolio;
        }
        Simulator simulator = new Simulator(portfolios);
	 	simulator.run();
	 	for(int i=0; i<obj.length; i++) {
	 	    System.out.println(portfolios[i]);
	 	}
	}
}
