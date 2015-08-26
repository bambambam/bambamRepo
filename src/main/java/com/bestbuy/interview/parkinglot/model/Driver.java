package com.bestbuy.interview.parkinglot.model;

import com.bestbuy.interview.parkinglot.strategy.ParkingStrategy;

public class Driver {

	private final Vehicle vehicle;
	private final ParkingStrategy parkingStrategy;

	public Driver(Vehicle vehicle, ParkingStrategy parkingStrategy) {
		this.vehicle = vehicle;
		this.parkingStrategy = parkingStrategy;
	}

	public boolean park(ParkingLot parkingLot) {
		return parkingStrategy.park(parkingLot, vehicle);
	}

	public void unpark(ParkingLot parkingLot) {
		parkingLot.releaseParkingSpace(vehicle);
	}
}
