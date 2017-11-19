package model.representation.rank;

public enum PairType {
	WeakPair,
	MiddlePair,
	BelowPair,
	TopPair,
	OverPair;
	
	
	@Override
	public String toString() {
		String res = "";
		if (this == WeakPair) {
			res = "Weak Pair";
		}
		else if (this == MiddlePair) {
			res = "Middle Pair";
		}
		else if (this == BelowPair) {
			res = "Below Pair";
		}
		else if (this == TopPair) {
			res = "Top Pair";
		}
		else if (this == OverPair) {
			res = "Over Pair";
		}		
		return res;
	}
}


