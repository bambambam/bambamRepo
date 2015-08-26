package com.bestbuy.interview.parkinglot.model;

public enum ParkingSpaceType {

	COMPACT(1), REGULAR(2), HANDICAP(3);

	private final int spaceSize;

	private ParkingSpaceType(int spaceSize) {
		this.spaceSize = spaceSize;
	}

	public int getSpaceSize() {
		return spaceSize;
	}
}
