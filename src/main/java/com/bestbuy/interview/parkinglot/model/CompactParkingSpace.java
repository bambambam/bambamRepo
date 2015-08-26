package com.bestbuy.interview.parkinglot.model;

public class CompactParkingSpace extends ParkingSpace {

	@Override
	public ParkingSpaceType getType() {
		return ParkingSpaceType.COMPACT;
	}

}
