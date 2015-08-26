package com.bestbuy.interview.parkinglot.model;

public class HandicapParkingSpace extends ParkingSpace {

	@Override
	public ParkingSpaceType getType() {
		return ParkingSpaceType.HANDICAP;
	}

}
