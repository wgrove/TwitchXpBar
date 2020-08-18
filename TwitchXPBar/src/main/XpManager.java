package main;

import java.util.concurrent.atomic.AtomicInteger;

public class XpManager {

	private AtomicInteger xp = new AtomicInteger();
	private AtomicInteger xpIncrease = new AtomicInteger();
	private AtomicInteger xpRemaining = new AtomicInteger();
	private AtomicInteger level = new AtomicInteger();
	
	public XpManager() {
		xp.set(0);
		xpIncrease.set(0);
		xpRemaining.set(3);
		level.set(1);		
	}
	
	public int setXp(Integer newXp) {
		
		// Status of -1 means there was no xp change
		int status = 0;
		
		xpIncrease.set(newXp - xp.get());
		
		if(xpIncrease.get() != 0) {
		xp.set(newXp);
	
		status = calculateLevel() ? 1 : 0;

		calculateXpRemaining();
		
		} else {
			// This means the xp was the same
			return -1;
		}
		
		return status;
	}
	
	public int getLevel() {
		return level.get();
	}
	
	public double getLevelCompletionPercentage() {
		
		int xpMinThreshold = getXpFromLevel(level.get());
		int xpThreshold = getXpFromLevel(level.get() + 1);
		
		int totalXpInLevel = xpThreshold - xpMinThreshold;
		
		int currentXpInLevel = totalXpInLevel - xpRemaining.get();
		
		return (1.0 * currentXpInLevel) / (1.0 * totalXpInLevel);
	}
	
	public int getRemainingXp() {
		return xpRemaining.get();
	}
	
	public int getXpChange() {
		return xpIncrease.get();
	}
	
	private boolean calculateLevel() {

		int currentLevelThreshold = getXpFromLevel(level.get());
		int nextLevelThreshold = getXpFromLevel(level.get() + 1);
		
		boolean increase = false;
		
		if(xp.get() < currentLevelThreshold) {
			level.set(level.decrementAndGet());
			calculateLevel();
		} else if (xp.get() >= nextLevelThreshold) {
			level.set(level.incrementAndGet());
			calculateLevel();
			increase = true;
		}
		
		return increase;
	}

	private int getXpFromLevel(int level) {
		
		if(level == 1 || level < 0) {
			return 0;
		}
		
		return (int) Math.pow(level, 1.7);
	}
	
	private void calculateXpRemaining() {
		int levelThreshold = getXpFromLevel(level.get() + 1);
		xpRemaining.set(levelThreshold - xp.get());
	}
}
