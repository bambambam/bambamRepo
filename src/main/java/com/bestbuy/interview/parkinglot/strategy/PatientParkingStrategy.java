package com.bestbuy.interview.parkinglot.strategy;

import com.bestbuy.interview.parkinglot.model.ParkingLot;
import com.bestbuy.interview.parkinglot.model.Vehicle;

public class PatientParkingStrategy implements ParkingStrategy {

	private final int maxRetries;
	private final long waitInterval;

	public PatientParkingStrategy(int maxRetries, long waitInterval) {
		this.maxRetries = maxRetries;
		this.waitInterval = waitInterval;
	}

	@Override
	public boolean park(ParkingLot lot, Vehicle vehicle) {
		boolean parked = false;
		for (int i = 0; i < maxRetries; ++i) {
			if (parked = parkOrLeave(lot, vehicle)) {
				break;
			}
			try {
				Thread.sleep(waitInterval);
			} catch (InterruptedException e) {
			}
		}
		return parked;
	}

}
