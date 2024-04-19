package com.testRunner;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;

import com.intuit.karate.Results;
import com.intuit.karate.junit4.Karate;

//@RunWith(Karate.class)
public class Runner {

	@Test
	void testParallel() {
		Results result = com.intuit.karate.Runner.path("classpath.features").parallel(4);
		assertEquals(0, result.getFailCount(), result.getErrorMessages());
	}
	
}
