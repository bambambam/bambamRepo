package com.bestbuy.interview.parkinglot.strategy;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Arrays;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.bestbuy.interview.parkinglot.model.ParkingLot;
import com.bestbuy.interview.parkinglot.model.ParkingSpace;
import com.bestbuy.interview.parkinglot.model.ParkingSpaceType;
import com.bestbuy.interview.parkinglot.model.Vehicle;

@RunWith(MockitoJUnitRunner.class)
public class ParkOrLeaveStrategyTest {

	@Mock
	private ParkingLot mockedLot;
	@Mock
	private Vehicle mockedVehicle;
	@Mock
	private ParkingSpace space1;
	@Mock
	private ParkingSpace space2;
	@Mock
	private ParkingSpace space3;

	private final ParkOrLeaveStrategy fixture = new ParkOrLeaveStrategy();

	@Before
	public void setup() {
		when(space1.getType()).thenReturn(ParkingSpaceType.COMPACT);
		when(space2.getType()).thenReturn(ParkingSpaceType.REGULAR);
		when(space3.getType()).thenReturn(ParkingSpaceType.HANDICAP);
		when(mockedLot.getAllParkingSpaces()).thenReturn(Arrays.asList(space1, space2, space3));

		when(mockedVehicle.getSpaceNeeded()).thenReturn(2);
	}

	@Test
	public void shouldFindAnySpaceToPark() {
		when(space2.isVacant()).thenReturn(true);
		when(space2.setTaken()).thenReturn(true);
		assertTrue(fixture.park(mockedLot, mockedVehicle));
		verify(mockedVehicle).getSpaceNeeded();
		verify(mockedLot).getAllParkingSpaces();
		verify(space2).setTaken();
	}

	@Test
	public void shouldNotParkWhenNoSpaceAvailable() {
		assertFalse(fixture.park(mockedLot, mockedVehicle));
	}

	@Test
	public void shouldNotParkWhenNoFittableSpaceAvailable() {
		when(space1.isVacant()).thenReturn(true);
		when(space1.setTaken()).thenReturn(true);
		assertFalse(fixture.park(mockedLot, mockedVehicle));
	}

	@Test
	public void shouldNotParkAtHandicapSpace() {
		when(space3.isVacant()).thenReturn(true);
		when(space3.setTaken()).thenReturn(true);
		assertFalse(fixture.park(mockedLot, mockedVehicle));
	}

	@Test
	public void shouldNotParkWhenASpaceWasOnceAvailableThenTakenBySomeoneElse() {
		when(space2.isVacant()).thenReturn(true);
		when(space2.setTaken()).thenReturn(false);
		assertFalse(fixture.park(mockedLot, mockedVehicle));
	}

	@Test
	public void shouldParkAtLargerPlaceWhenAvailable() {
		when(mockedVehicle.getSpaceNeeded()).thenReturn(1);
		when(space1.isVacant()).thenReturn(true);
		when(space1.setTaken()).thenReturn(true);
		when(space2.isVacant()).thenReturn(true);
		when(space2.setTaken()).thenReturn(true);
		assertTrue(fixture.park(mockedLot, mockedVehicle));
		verify(space2).setTaken();
		verify(space1, never()).setTaken();
	}
}
