package dbmodules.types;

public enum TableType {
    STUDENT("student"),
    TEACHER("teacher"),
    GROUP("group");

    private String table;

    TableType(String value) {
        this.table = value;
    }

    public String getValue() {
        return table;
    }
}

