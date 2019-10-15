public enum Table {
    STUDENT("student"),
    GROUP("group"),
    TEACHER("teacher");

    private String gender;

    Table(String value) {
        this.gender = value;
    }

    public String getValue() {
        return gender;
    }
}
