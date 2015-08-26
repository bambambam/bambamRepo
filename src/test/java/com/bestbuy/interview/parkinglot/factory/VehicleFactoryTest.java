package com.bestbuy.interview.parkinglot.factory;

import static org.junit.Assert.assertTrue;

import org.junit.Test;

import com.bestbuy.interview.parkinglot.model.Sedan;
import com.bestbuy.interview.parkinglot.model.SUV;
import com.bestbuy.interview.parkinglot.model.Vehicle;

public class VehicleFactoryTest {

	@Test
	public void shouldCreateCar() {
		Vehicle car = VehicleFactory.createVehicle(Sedan.class);
		assertTrue(car instanceof Sedan);
	}

	@Test
	public void shouldCreateTruck() {
		Vehicle car = VehicleFactory.createVehicle(SUV.class);
		assertTrue(car instanceof SUV);
	}
}
