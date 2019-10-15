public enum Gender {
    NULL("NULL"),
    MALE("MALE"),
    FEMALE("FEMALE");

    private String gender;

    Gender(String value) {
        this.gender = value;
    }

    public String getValue() {
        return gender;
    }
}
