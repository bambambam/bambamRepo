package com.bestbuy.interview.parkinglot.strategy;

import com.bestbuy.interview.parkinglot.model.ParkingLot;
import com.bestbuy.interview.parkinglot.model.Vehicle;

public class ParkOrLeaveStrategy implements ParkingStrategy {

	@Override
	public boolean park(ParkingLot lot, Vehicle vehicle) {
		return parkOrLeave(lot, vehicle);
	}
}
