package model;

import java.util.Objects;

public class Equipment {
    private int id;
    private String name;
    private int well_id;

    public Equipment(int well_id) {
        this.well_id = well_id;
    }

    public Equipment(String name, int well_id) {
        this.name = name;
        this.well_id = well_id;
    }

    public Equipment(int id, String name, int well_id) {
        this.id = id;
        this.name = name;
        this.well_id = well_id;
    }

    public Equipment(String name) {
        this.name = name;
    }

    public Equipment() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getWell_id() {
        return well_id;
    }

    public void setWell_id(int well_id) {
        this.well_id = well_id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Equipment equipment = (Equipment) o;
        return id == equipment.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "Equipment{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", well_id=" + well_id +
                '}';
    }
}
