package model.representation.game;

public enum Play implements Comparable<Play> {
	HighCard,
	Pair,
	TwoPair,
	ThreeOfAKind,
	Straight,
	Flush,
	FullHouse,
	FourOfAKind,
	StraightFlush;

	public static final int NUM_PLAYS = 9;
	public static final int CARDS_PER_PLAY = 5;

	@Override
	public String toString() {
		return this.name();
	}
}
