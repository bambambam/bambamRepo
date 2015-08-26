package com.bestbuy.interview.parkinglot.model;

import java.util.concurrent.atomic.AtomicBoolean;

public class LotGate {

	private final AtomicBoolean isOpen = new AtomicBoolean(true);
	
	public void close() {
		isOpen.set(false);
	}
	
	public void open() {
		isOpen.set(true);
	}
	
	public boolean isOpen() {
		return isOpen.get();
	}
}
