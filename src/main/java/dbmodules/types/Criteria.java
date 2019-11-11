package dbmodules.types;

public enum Criteria {
    ID(1),
    NAME(2),
    BIRTH(3),
    GENDER(4),
    GROUP(5),
    ALL(6);

    private int critId;

    Criteria(int value) {
        this.critId = value;
    }

    public int getValue() {
        return critId;
    }
}
