public enum Gender {
    MALE("M"),
    FEMALE("F");

    private String gender;

    Gender(String value) {
        this.gender = value;
    }

    public String getValue() {
        return gender;
    }
}
