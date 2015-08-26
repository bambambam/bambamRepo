package com.bestbuy.interview.parkinglot;

import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import com.bestbuy.interview.parkinglot.factory.VehicleFactory;
import com.bestbuy.interview.parkinglot.model.Driver;
import com.bestbuy.interview.parkinglot.model.ParkingLot;
import com.bestbuy.interview.parkinglot.model.SUV;
import com.bestbuy.interview.parkinglot.model.Sedan;
import com.bestbuy.interview.parkinglot.model.Vehicle;
import com.bestbuy.interview.parkinglot.strategy.HandicapParkingStrategy;
import com.bestbuy.interview.parkinglot.strategy.ParkOrLeaveStrategy;
import com.bestbuy.interview.parkinglot.strategy.ParkingStrategy;
import com.bestbuy.interview.parkinglot.strategy.PatientParkingStrategy;

public class ParkingLotSimulator {

	private static final int NUM_HANDICAP_SPACES = 10;
	private static final int NUM_REGULAR_SPACES = 30;
	private static final int NUM_COMPACT_SPACES = 20;
	private static final int NUM_EXITS = 5;
	private static final int NUM_ENTRIES = 5;
	private static final int TOTAL_LOT_SPACE = 200;

	private static final int NUM_DRIVERS = 100;

	private static final Random random = new Random();
	private static final AtomicInteger driversCreated = new AtomicInteger();

	public static void main(String[] args) throws InterruptedException {
		ParkingLot parkingLot = new ParkingLot.ParkingLotBuilder(TOTAL_LOT_SPACE).buildEntries(NUM_ENTRIES).buildExits(NUM_EXITS)
				.buildCompactParkingSpaces(NUM_COMPACT_SPACES).buildRegularParkingSpaces(NUM_REGULAR_SPACES).buildHandicapParkingSpaces(NUM_HANDICAP_SPACES).build();

		int cores = Runtime.getRuntime().availableProcessors();

		ExecutorService service = new ThreadPoolExecutor(cores, cores, 0L, TimeUnit.MILLISECONDS,
				new LinkedBlockingQueue<Runnable>(100), Executors.defaultThreadFactory(),
				new ThreadPoolExecutor.CallerRunsPolicy());

		service.submit(() -> controlEntries(parkingLot));
		while (driversCreated.get() < NUM_DRIVERS) {
			service.submit(() -> simulateParkingTrip(parkingLot, driversCreated.getAndIncrement()));
		}

		service.shutdown();
		System.exit(0);
	}

	private static Void controlEntries(ParkingLot parkingLot) {
		while (true) {
			try {
				Thread.sleep(1000L);
			} catch (InterruptedException e) {
			}
			parkingLot.controlEntries();
			System.out.println("Parking lot opening entries count: " + parkingLot.getOpenedEntriesCount());
		}
	}

	private static Void simulateParkingTrip(ParkingLot parkingLot, int driverNumber) throws InterruptedException {

		Vehicle vehicle = null;
		if (random.nextInt(2) == 0) {
			vehicle = VehicleFactory.createVehicle(Sedan.class);
		} else {
			vehicle = VehicleFactory.createVehicle(SUV.class);
		}

		List<ParkingStrategy> parkingStrategies = Arrays.asList(new ParkOrLeaveStrategy(),
				new PatientParkingStrategy(3, 50), new HandicapParkingStrategy());

		ParkingStrategy parkingStrategy = parkingStrategies.get(random.nextInt(3));
		Driver driver = new Driver(vehicle, parkingStrategy);

		System.out.println(String.format("Drive %d is driving a %s with %s trying to enter the parking lot.",
				driverNumber, vehicle.getClass().getSimpleName(), parkingStrategy.getClass().getSimpleName()));

		if (parkingLot.getOpenedEntriesCount() > 0) {
			System.out.println(String.format("Driver %d entered the parking lot.", driverNumber));
			if (driver.park(parkingLot)) {
				System.out.println(String.format("Driver %d parked.", driverNumber));
				Thread.sleep(random.nextInt(5000 - 1000) + 1000);
				driver.unpark(parkingLot);
				System.out.println(String.format("Driver %d unparked.", driverNumber));
			}
		} else {
			System.out.println(
					String.format("All entries are closed. Driver %d couldn't enter the parking lot.", driverNumber));
		}
		System.out.println(String.format("Driver %d left the parking lot.", driverNumber));
		return null;
	}
}
