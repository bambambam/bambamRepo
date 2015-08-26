package com.bestbuy.interview.parkinglot.strategy;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
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
public class PatientParkingStrategyTest {

	private static final long WAIT_INTERVAL = 50L;
	private static final int MAX_RETRIES = 3;
	@Mock
	private ParkingLot mockedLot;
	@Mock
	private Vehicle mockedVehicle;
	@Mock
	private ParkingSpace space;

	private final PatientParkingStrategy fixture = new PatientParkingStrategy(MAX_RETRIES, WAIT_INTERVAL);

	@Before
	public void setup() {
		when(space.getType()).thenReturn(ParkingSpaceType.REGULAR);
		when(mockedLot.getAllParkingSpaces()).thenReturn(Arrays.asList(space));
		when(mockedVehicle.getSpaceNeeded()).thenReturn(1);
	}

	@Test
	public void shouldWaitAndRetryWhenNoSpaceAvailable() {
		when(space.isVacant()).thenReturn(false, true, false);
		when(space.setTaken()).thenReturn(true);
		assertTrue(fixture.park(mockedLot, mockedVehicle));
	}

	@Test
	public void shouldLeaveAfterMaximuRetries() {
		when(space.isVacant()).thenReturn(false, false, false, true);
		when(space.setTaken()).thenReturn(true);
		assertFalse(fixture.park(mockedLot, mockedVehicle));
	}
}
