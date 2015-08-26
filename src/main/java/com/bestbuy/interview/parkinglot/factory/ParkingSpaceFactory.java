package com.bestbuy.interview.parkinglot.factory;

import com.bestbuy.interview.parkinglot.model.CompactParkingSpace;
import com.bestbuy.interview.parkinglot.model.HandicapParkingSpace;
import com.bestbuy.interview.parkinglot.model.ParkingSpace;
import com.bestbuy.interview.parkinglot.model.ParkingSpaceType;
import com.bestbuy.interview.parkinglot.model.RegularParkingSpace;

public class ParkingSpaceFactory {

	public static ParkingSpace createProduct(ParkingSpaceType type) {
		ParkingSpace parkingSpace = null;
		switch (type) {
		case COMPACT:
			parkingSpace = new CompactParkingSpace();
			break;
		case REGULAR:
			parkingSpace = new RegularParkingSpace();
			break;
		case HANDICAP:
			parkingSpace = new HandicapParkingSpace();
			break;
		default:
			throw new RuntimeException("Invalid ParkingSpace type " + type);
		}
		return parkingSpace;
	}
}
