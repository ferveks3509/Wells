package dto;

import java.util.Objects;

public class WellsEQ {
    private String name;
    private int count;

    public WellsEQ(String name, int count) {
        this.name = name;
        this.count = count;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        WellsEQ wellsEQ = (WellsEQ) o;
        return count == wellsEQ.count && Objects.equals(name, wellsEQ.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, count);
    }

    @Override
    public String toString() {
        return "WellsEQ{" +
                "name='" + name + '\'' +
                ", count=" + count +
                '}';
    }
}
