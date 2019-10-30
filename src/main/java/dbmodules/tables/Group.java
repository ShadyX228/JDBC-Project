package dbmodules.tables;

import javax.persistence.*;

@Entity
@javax.persistence.Table (name = "studentgroupteacher.group")
public class Group extends Table {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="group_id")
    private int id;

    @Column(name = "Number")
    private int number;

    public Group() {}
    public Group(int id, int number)  {
        this.id = id;
        this.number = number;

    }
    public Group(int number) {
        this.number = number;
    }

    public int getId() {
        return id;
    }
    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public String toString() {
        return "group_id: " + getId() + "; number: " + getNumber();
    }
}
