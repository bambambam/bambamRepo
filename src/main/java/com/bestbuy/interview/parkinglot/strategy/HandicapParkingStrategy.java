package com.bestbuy.interview.parkinglot.strategy;

import java.util.Collection;
import java.util.stream.Stream;

import com.bestbuy.interview.parkinglot.model.ParkingLot;
import com.bestbuy.interview.parkinglot.model.ParkingSpace;
import com.bestbuy.interview.parkinglot.model.ParkingSpaceType;
import com.bestbuy.interview.parkinglot.model.Vehicle;

public class HandicapParkingStrategy implements ParkingStrategy {

	@Override
	public boolean park(ParkingLot lot, Vehicle vehicle) {
		return parkOrLeave(lot, vehicle);
	}

	@Override
	public Stream<ParkingSpace> getAvailableParkingSpaces(Collection<ParkingSpace> parkingSpaces, int spaceNeeded) {
		return parkingSpaces.stream().filter(parkingSpace -> parkingSpace.isVacant())
				.filter(parkingSpace -> parkingSpace.getType().getSpaceSize() >= spaceNeeded)
				.sorted((space1, space2) -> {
					if (space1.getType() == ParkingSpaceType.HANDICAP) {
						return -1;
					} else if (space2.getType() == ParkingSpaceType.HANDICAP) {
						return 1;
					} else {
						return space2.getType().getSpaceSize() - space1.getType().getSpaceSize();
					}
				});
	}
}
