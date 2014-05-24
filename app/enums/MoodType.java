package enums;

public enum MoodType {
	SMILE(0), LAUGH(1), COOL(2), CHEEKY(3), DEVIL(4), SAD(5), ANGRY(6), CRY(7);
	
	public int value;

	MoodType(int value) {
      this.value = value;
    }
	
	public static MoodType of(int moodValue) {

	    switch (moodValue) {
	        case 0: return SMILE;
	        case 1: return LAUGH;
	        case 2: return COOL;
	        case 3: return CHEEKY;
	        case 4: return DEVIL;
	        case 5: return SAD;
	        case 6: return ANGRY;
	        case 7: return CRY;
	        default: return SMILE;

	    }
	}
}
