package enums;

public enum AttendanceStatus {
	PRESENT(0), LATE(1), ABSENT(2), MC(3);
	
	private final int value;

    private AttendanceStatus(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}
