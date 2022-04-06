package model;

import java.util.Objects;

public class Well {
    private int id;
    private String name;

    public Well(String name) {
        this.name = name;
    }

    public Well(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public Well() {
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Well well = (Well) o;
        return id == well.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "Well{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }
}
