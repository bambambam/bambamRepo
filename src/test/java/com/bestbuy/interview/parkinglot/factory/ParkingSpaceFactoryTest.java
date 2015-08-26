package com.bestbuy.interview.parkinglot.factory;

import static org.junit.Assert.assertTrue;

import org.junit.Test;

import com.bestbuy.interview.parkinglot.model.CompactParkingSpace;
import com.bestbuy.interview.parkinglot.model.HandicapParkingSpace;
import com.bestbuy.interview.parkinglot.model.ParkingSpace;
import com.bestbuy.interview.parkinglot.model.ParkingSpaceType;
import com.bestbuy.interview.parkinglot.model.RegularParkingSpace;

public class ParkingSpaceFactoryTest {

	@Test
	public void shouldCreateCompactParkingSpace() {
		ParkingSpace compact = ParkingSpaceFactory.createProduct(ParkingSpaceType.COMPACT);
		assertTrue(compact instanceof CompactParkingSpace);
	}

	@Test
	public void shouldCreateRegularParkingSpace() {
		ParkingSpace compact = ParkingSpaceFactory.createProduct(ParkingSpaceType.REGULAR);
		assertTrue(compact instanceof RegularParkingSpace);
	}

	@Test
	public void shouldCreateHandicapParkingSpace() {
		ParkingSpace compact = ParkingSpaceFactory.createProduct(ParkingSpaceType.HANDICAP);
		assertTrue(compact instanceof HandicapParkingSpace);
	}
}
