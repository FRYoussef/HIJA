package model.representation;

public enum Draws {
	OpenEndedStraight,
	GutShotStraight,
	FlushDraw;
	
	private static final String NAMES [] = {"Open Ended Straight Draw","Gutshot Straight Draw","Flush Draw"};
	
	public static final int NUM_DRAWS = 3;
	
	@Override
	public String toString() {
		return NAMES[this.ordinal()];
	}
	
}
