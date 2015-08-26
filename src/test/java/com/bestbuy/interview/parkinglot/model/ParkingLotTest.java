package com.bestbuy.interview.parkinglot.model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.Collection;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import com.bestbuy.interview.parkinglot.factory.ParkingSpaceFactory;

@RunWith(MockitoJUnitRunner.class)
public class ParkingLotTest {

	private static final int TOTAL_SPACE = 100;
	private static final int TOTAL_ENTRIES = 5;
	private static final int TOTAL_EXITS = 5;

	private Collection<LotGate> entries;
	private Collection<LotGate> exits;
	private Collection<ParkingSpace> parkingSpaces;

	private ParkingLot fixture;

	@Before
	public void init() {
		entries = buildCollection(TOTAL_ENTRIES, element -> new LotGate());
		exits = buildCollection(TOTAL_EXITS, element -> new LotGate());
		parkingSpaces = buildCollection(TOTAL_SPACE,
				parkSpace -> ParkingSpaceFactory.createProduct(ParkingSpaceType.REGULAR));
		fixture = new ParkingLot.ParkingLotBuilder(TOTAL_ENTRIES).build(entries, exits, parkingSpaces);
	}

	@Test
	public void shouldGetOpenedEntriesCount() {
		assertEquals(TOTAL_ENTRIES, fixture.getOpenedEntriesCount());
		entries.stream().forEach(lotGate -> lotGate.close());
		assertEquals(0, fixture.getOpenedEntriesCount());
	}

	@Test
	public void shouldReleaseParkingSpaceWhenVehicleUnpark() {
		Vehicle mockedVehicle = Mockito.mock(Vehicle.class);
		ParkingSpace space = new RegularParkingSpace();
		fixture.mapVehicleToSpace(mockedVehicle, space);
		space.setTaken();
		fixture.releaseParkingSpace(mockedVehicle);
		assertTrue(space.isVacant());
	}

	@Test
	public void shouldNotReleaseParkingSpaceWhenOtherVehicleUnpark() {
		Vehicle mockedVehicle = Mockito.mock(Vehicle.class);
		ParkingSpace space = new RegularParkingSpace();
		fixture.mapVehicleToSpace(mockedVehicle, space);
		space.setTaken();
		Vehicle anotherMockedVehicle = Mockito.mock(Vehicle.class);
		fixture.releaseParkingSpace(anotherMockedVehicle);
		assertFalse(space.isVacant());
	}

	@Test
	public void shouldCloseAllEntries() {
		int beforeOpenedEntries = fixture.getOpenedEntriesCount();
		assertEquals(TOTAL_ENTRIES, beforeOpenedEntries);
		// All spaces taken
		parkingSpaces.stream().forEach(parkSpace -> parkSpace.setTaken());
		fixture.controlEntries();
		int afterOpenedEntries = fixture.getOpenedEntriesCount();
		assertEquals(0, afterOpenedEntries);
	}

	@Test
	public void shouldOpenAllEntries() {
		entries.stream().forEach(lotGate -> lotGate.close());
		int beforeOpenedEntries = fixture.getOpenedEntriesCount();
		assertEquals(0, beforeOpenedEntries);
		// All entries closed and all spaces are free
		fixture.controlEntries();
		int afterOpenedEntries = fixture.getOpenedEntriesCount();
		assertEquals(TOTAL_ENTRIES, afterOpenedEntries);
	}

	@Test
	public void shouldNotCloseEntriesWhenVacantSpacesWithinClosingBoundary() {
		int beforeOpenedEntries = fixture.getOpenedEntriesCount();
		assertEquals(TOTAL_ENTRIES, beforeOpenedEntries);
		// 1 space taken (out of 100)
		parkingSpaces.stream().findFirst().get().setTaken();
		fixture.controlEntries();
		int afterOpenedEntries = fixture.getOpenedEntriesCount();
		assertEquals(TOTAL_ENTRIES, afterOpenedEntries);
	}

	@Test
	public void shouldCloseEntriesWhenVacantSpacesOnClosingBoundary() {
		int beforeOpenedEntries = fixture.getOpenedEntriesCount();
		assertEquals(TOTAL_ENTRIES, beforeOpenedEntries);
		// All entries opened and 20 spaces taken (out of 100)
		parkingSpaces.stream().limit(20).forEach(parkSpace -> parkSpace.setTaken());
		fixture.controlEntries();
		int afterOpenedEntries = fixture.getOpenedEntriesCount();
		assertEquals(4, afterOpenedEntries);
	}

	@Test
	public void shouldCloseEntriesWhenVacantSpacesPassClosingBoundary() {
		int beforeOpenedEntries = fixture.getOpenedEntriesCount();
		assertEquals(TOTAL_ENTRIES, beforeOpenedEntries);
		// All entries opened and 21 spaces taken (out of 100)
		parkingSpaces.stream().limit(21).forEach(parkSpace -> parkSpace.setTaken());
		fixture.controlEntries();
		int afterOpenedEntries = fixture.getOpenedEntriesCount();
		assertEquals(4, afterOpenedEntries);
	}

	@Test
	public void shouldOpenEntriesWhenVacantSpacesWithinOpeningBoundary() {
		entries.stream().forEach(lotGate -> lotGate.close());
		int beforeOpenedEntries = fixture.getOpenedEntriesCount();
		assertEquals(0, beforeOpenedEntries);
		// All entries closed and 1 space available (out of 100)
		parkingSpaces.stream().limit(99).forEach(parkSpace -> parkSpace.setTaken());
		fixture.controlEntries();
		int afterOpenedEntries = fixture.getOpenedEntriesCount();
		assertEquals(1, afterOpenedEntries);
	}

	@Test
	public void shouldOpenEntriesWhenVacantSpacesOnOpeningBoundary() {
		entries.stream().forEach(lotGate -> lotGate.close());
		int beforeOpenedEntries = fixture.getOpenedEntriesCount();
		assertEquals(0, beforeOpenedEntries);
		// All entries closed and 20 spaces available (out of 100)
		parkingSpaces.stream().limit(80).forEach(parkSpace -> parkSpace.setTaken());
		fixture.controlEntries();
		int afterOpenedEntries = fixture.getOpenedEntriesCount();
		assertEquals(1, afterOpenedEntries);
	}

	@Test
	public void shouldOpenEntriesWhenVacantSpacesPassOpeningBoundary() {
		entries.stream().forEach(lotGate -> lotGate.close());
		int beforeOpenedEntries = fixture.getOpenedEntriesCount();
		assertEquals(0, beforeOpenedEntries);
		// All entries closed and 21 spaces available (out of 100)
		parkingSpaces.stream().limit(79).forEach(parkSpace -> parkSpace.setTaken());
		fixture.controlEntries();
		int afterOpenedEntries = fixture.getOpenedEntriesCount();
		assertEquals(2, afterOpenedEntries);
	}

	private <T, R> Collection<R> buildCollection(int numElementsToBuild, Function<T, R> function) {
		return Stream.iterate(0, i -> ++i).limit(numElementsToBuild).map(element -> function.apply(null))
				.collect(Collectors.toList());
	}
}
