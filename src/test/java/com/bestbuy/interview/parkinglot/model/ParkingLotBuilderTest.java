package com.bestbuy.interview.parkinglot.model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

public class ParkingLotBuilderTest {

	private static final int TOTAL_SPACE = 100;
	private static final int TOTAL_ENTRIES = 5;
	private static final int TOTAL_EXITS = 5;
	private static final int NUM_COMPACT_SPACES = 30;
	private static final int NUM_REGULAR_SPACES = 20;
	private static final int NUM_HANDICAP_SPACES = 10;

	@Rule
	public ExpectedException thrown = ExpectedException.none();

	@Test
	public void shouldBuildParkingLotWithBuilder() {
		ParkingLot fixture = new ParkingLot.ParkingLotBuilder(TOTAL_SPACE).buildEntries(TOTAL_ENTRIES)
				.buildExits(TOTAL_EXITS).buildCompactParkingSpaces(NUM_COMPACT_SPACES)
				.buildRegularParkingSpaces(NUM_REGULAR_SPACES).buildHandicapParkingSpaces(NUM_HANDICAP_SPACES).build();
		assertNotNull(fixture);
		assertEquals(NUM_COMPACT_SPACES + NUM_REGULAR_SPACES + NUM_HANDICAP_SPACES,
				fixture.getAllParkingSpaces().size());
		assertEquals(TOTAL_ENTRIES, fixture.getOpenedEntriesCount());
		assertEquals(TOTAL_EXITS, fixture.getOpenedExitsCount());
	}

	@Test
	public void shouldThrowIllegalArgumentExceptionWhenBuildingParkingSpacesExceedTotalSpace() {

		thrown.equals(IllegalArgumentException.class);
		thrown.expectMessage("Space used cannot exceed the total space. Space Used: 100; Total Space: 1");

		ParkingLot fixture = new ParkingLot.ParkingLotBuilder(1).buildEntries(TOTAL_ENTRIES).buildExits(TOTAL_EXITS)
				.buildCompactParkingSpaces(NUM_COMPACT_SPACES).buildRegularParkingSpaces(NUM_REGULAR_SPACES)
				.buildHandicapParkingSpaces(NUM_HANDICAP_SPACES).build();
		assertNull(fixture);
	}
}
