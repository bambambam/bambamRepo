package com.bestbuy.interview.parkinglot.model;

public class RegularParkingSpace extends ParkingSpace {

	@Override
	public ParkingSpaceType getType() {
		return ParkingSpaceType.REGULAR;
	}

}
