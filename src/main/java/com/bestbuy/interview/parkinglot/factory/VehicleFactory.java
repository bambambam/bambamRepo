package com.bestbuy.interview.parkinglot.factory;

import com.bestbuy.interview.parkinglot.model.Vehicle;

public class VehicleFactory {

	public static <T extends Vehicle> T createVehicle(Class<T> clazz) {
		try {
			return clazz.newInstance();
		} catch (InstantiationException | IllegalAccessException e) {
			throw new RuntimeException(String.format("Failed create Vehicle %s\n", clazz.getName()), e);
		}
	}
}
