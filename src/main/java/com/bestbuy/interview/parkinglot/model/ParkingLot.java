package com.bestbuy.interview.parkinglot.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.apache.commons.lang3.Validate;

import com.bestbuy.interview.parkinglot.factory.ParkingSpaceFactory;
import com.google.common.annotations.VisibleForTesting;

public class ParkingLot {

	private final Collection<LotGate> lotEntries;
	private final Collection<LotGate> lotExits;
	private final Collection<ParkingSpace> parkingSpaces;
	private final Map<Vehicle, ParkingSpace> vehicleParkingSpaceMap = new ConcurrentHashMap<>();

	private ParkingLot(Collection<LotGate> lotEntries, Collection<LotGate> lotExits,
			Collection<ParkingSpace> parkingSpaces) {

		this.lotEntries = lotEntries;
		this.lotExits = lotExits;
		this.parkingSpaces = parkingSpaces;
	}

	public synchronized void controlEntries() {
		double vacantPercentage = getVacantParkingSpacesCount() * 1.0 / parkingSpaces.size();
		int numEntriesShouldOpen = (int) Math.ceil(vacantPercentage * lotEntries.size());
		int currentOpenedEntriesCount = (int) lotEntries.stream().filter(lotEntry -> lotEntry.isOpen()).count();
		int delta = currentOpenedEntriesCount - numEntriesShouldOpen;

		if (delta == 0) {
			return;
		}

		boolean shouldClose = delta > 0 ? true : false;

		lotEntries.stream().filter(shouldClose ? lotEntry -> lotEntry.isOpen() : lotEntry -> !lotEntry.isOpen())
		.limit(Math.abs(delta)).forEach(shouldClose ? LotGate::close : LotGate::open);
	}

	public Collection<ParkingSpace> getAllParkingSpaces() {
		return parkingSpaces;
	}

	public int getOpenedEntriesCount() {
		return (int) lotEntries.stream().filter(lotEntry -> lotEntry.isOpen()).count();
	}

	public int getOpenedExitsCount() {
		return lotExits.size();
	}

	public void mapVehicleToSpace(Vehicle vehicle, ParkingSpace parkingSpace) {
		vehicleParkingSpaceMap.put(vehicle, parkingSpace);
	}

	public void releaseParkingSpace(Vehicle vehicle) {
		ParkingSpace spaceToRelease = vehicleParkingSpaceMap.remove(vehicle);
		if (spaceToRelease != null) {
			spaceToRelease.setVacant();
		}
	}

	private int getVacantParkingSpacesCount() {
		return Long.valueOf(parkingSpaces.stream().filter(parkingSpace -> parkingSpace.isVacant()).count()).intValue();
	}

	public static class ParkingLotBuilder {

		private final int totalSpace;
		private final Collection<LotGate> lotEntries = new ArrayList<>();
		private final Collection<LotGate> lotExits = new ArrayList<>();
		private final Collection<ParkingSpace> parkingSpaces = new ArrayList<>();

		private int spaceUsed = 0;

		public ParkingLotBuilder(int totalSpace) {
			this.totalSpace = totalSpace;
		}

		public ParkingLotBuilder buildEntries(int quantities) {
			lotEntries.addAll(buildCollection(quantities, entry -> new LotGate()));
			return this;
		}

		public ParkingLotBuilder buildExits(int quantities) {
			lotExits.addAll(buildCollection(quantities, exit -> new LotGate()));
			return this;
		}

		public ParkingLotBuilder buildCompactParkingSpaces(int quantities) {
			parkingSpaces.addAll(
					buildCollection(quantities, space -> ParkingSpaceFactory.createProduct(ParkingSpaceType.COMPACT)));
			accumulateSpaceUsed(quantities * ParkingSpaceType.COMPACT.getSpaceSize());
			return this;
		}

		public ParkingLotBuilder buildRegularParkingSpaces(int quantities) {
			parkingSpaces.addAll(
					buildCollection(quantities, space -> ParkingSpaceFactory.createProduct(ParkingSpaceType.REGULAR)));
			accumulateSpaceUsed(quantities * ParkingSpaceType.REGULAR.getSpaceSize());
			return this;
		}

		public ParkingLotBuilder buildHandicapParkingSpaces(int quantities) {
			parkingSpaces.addAll(
					buildCollection(quantities, space -> ParkingSpaceFactory.createProduct(ParkingSpaceType.HANDICAP)));
			accumulateSpaceUsed(quantities * ParkingSpaceType.HANDICAP.getSpaceSize());
			return this;
		}

		public ParkingLot build() {
			validateSpaceUsed();
			return new ParkingLot(lotEntries, lotExits, parkingSpaces);
		}

		@VisibleForTesting
		ParkingLot build(Collection<LotGate> lotEntries, Collection<LotGate> lotExits,
				Collection<ParkingSpace> parkingSpaces) {
			return new ParkingLot(lotEntries, lotExits, parkingSpaces);
		}

		private void validateSpaceUsed() {
			Validate.isTrue(spaceUsed <= totalSpace,
					String.format("Space used cannot exceed the total space. Space Used: %d; Total Space: %d",
							spaceUsed, totalSpace));
		}

		private void accumulateSpaceUsed(int newSpacesUsed) {
			spaceUsed += newSpacesUsed;
		}

		private <T, R> Collection<R> buildCollection(int quantities, Function<T, R> function) {
			return Stream.iterate(0, i -> ++i).limit(quantities).map(element -> function.apply(null))
					.collect(Collectors.toList());
		}

	}
}
