package com.bestbuy.interview.parkinglot.model;

import java.util.concurrent.atomic.AtomicBoolean;

public abstract class ParkingSpace {

	private final AtomicBoolean vacant = new AtomicBoolean(true);

	public abstract ParkingSpaceType getType();

	public boolean setTaken() {
		return vacant.compareAndSet(true, false);
	}

	public void setVacant() {
		vacant.set(true);
	}

	public boolean isVacant() {
		return vacant.get();
	}
}
