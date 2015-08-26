package com.bestbuy.interview.parkinglot.strategy;

import java.util.Collection;
import java.util.Optional;
import java.util.stream.Stream;

import com.bestbuy.interview.parkinglot.model.ParkingLot;
import com.bestbuy.interview.parkinglot.model.ParkingSpace;
import com.bestbuy.interview.parkinglot.model.ParkingSpaceType;
import com.bestbuy.interview.parkinglot.model.Vehicle;

public interface ParkingStrategy {

	boolean park(ParkingLot lot, Vehicle vehicle);

	default boolean parkOrLeave(ParkingLot lot, Vehicle vehicle) {
		int spaceNeeded = vehicle.getSpaceNeeded();
		Optional<ParkingSpace> takenSpace = getAvailableParkingSpaces(lot.getAllParkingSpaces(), spaceNeeded)
				.filter(space -> space.setTaken()).findFirst();
		boolean parked = takenSpace.isPresent();
		if (parked) {
			lot.mapVehicleToSpace(vehicle, takenSpace.get());
		}
		return parked;
	}

	default Stream<ParkingSpace> getAvailableParkingSpaces(Collection<ParkingSpace> parkingSpaces, int spaceNeeded) {
		return parkingSpaces.stream().filter(parkingSpace -> parkingSpace.isVacant())
				.filter(parkingSpace -> parkingSpace.getType().getSpaceSize() >= spaceNeeded)
				.filter(parkingSpace -> parkingSpace.getType() != ParkingSpaceType.HANDICAP)
				.sorted((space1, space2) -> {
					return space2.getType().getSpaceSize() - space1.getType().getSpaceSize();
				});
	}
}
