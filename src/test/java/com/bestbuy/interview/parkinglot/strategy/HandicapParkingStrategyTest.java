package com.bestbuy.interview.parkinglot.strategy;

import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Arrays;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import com.bestbuy.interview.parkinglot.model.ParkingLot;
import com.bestbuy.interview.parkinglot.model.ParkingSpace;
import com.bestbuy.interview.parkinglot.model.ParkingSpaceType;
import com.bestbuy.interview.parkinglot.model.Vehicle;

@RunWith(MockitoJUnitRunner.class)
public class HandicapParkingStrategyTest {

	@Mock
	private ParkingLot mockedLot;
	@Mock
	private Vehicle mockedVehicle;
	@Mock
	private ParkingSpace handicapSpace;
	@Mock
	private ParkingSpace otherSpace;

	private final HandicapParkingStrategy fixture = new HandicapParkingStrategy();

	@Before
	public void setup() {

		when(handicapSpace.getType()).thenReturn(ParkingSpaceType.HANDICAP);
		when(otherSpace.getType()).thenReturn(ParkingSpaceType.COMPACT);
		when(mockedLot.getAllParkingSpaces()).thenReturn(Arrays.asList(handicapSpace, otherSpace));
		when(mockedVehicle.getSpaceNeeded()).thenReturn(1);
	}

	@Test
	public void shouldParkAtHandicapSpace() {
		when(handicapSpace.isVacant()).thenReturn(true);
		when(handicapSpace.setTaken()).thenReturn(true);
		assertTrue(fixture.park(mockedLot, mockedVehicle));
	}

	@Test
	public void shouldNotParkAtOtherSpaceWhenHandicapSpaceIsAvailable() {
		when(otherSpace.isVacant()).thenReturn(true);
		when(otherSpace.setTaken()).thenReturn(true);
		when(handicapSpace.isVacant()).thenReturn(true);
		when(handicapSpace.setTaken()).thenReturn(true);
		assertTrue(fixture.park(mockedLot, mockedVehicle));
		verify(handicapSpace).setTaken();
		verify(otherSpace, never()).setTaken();
	}

	@Test
	public void shouldParkAtOtherSpaceWhenHandicapSpaceIsNotAvailable() {
		when(handicapSpace.isVacant()).thenReturn(false);
		when(otherSpace.isVacant()).thenReturn(true);
		when(otherSpace.setTaken()).thenReturn(true);
		assertTrue(fixture.park(mockedLot, mockedVehicle));
	}

	@Test
	public void shouldParkAtLargerSpaceWhenHandicapSpaceIsNotAvailable() {
		ParkingSpace largerPlace = Mockito.mock(ParkingSpace.class);
		when(largerPlace.getType()).thenReturn(ParkingSpaceType.REGULAR);
		when(handicapSpace.isVacant()).thenReturn(false);
		when(otherSpace.isVacant()).thenReturn(true);
		when(otherSpace.setTaken()).thenReturn(true);
		when(largerPlace.isVacant()).thenReturn(true);
		when(largerPlace.setTaken()).thenReturn(true);
		when(mockedLot.getAllParkingSpaces()).thenReturn(Arrays.asList(handicapSpace, otherSpace, largerPlace));
		assertTrue(fixture.park(mockedLot, mockedVehicle));
		verify(largerPlace).setTaken();
		verify(otherSpace, never()).setTaken();
	}
}
