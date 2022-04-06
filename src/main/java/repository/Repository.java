package repository;

import dto.WellsEQ;
import model.Equipment;
import model.Well;

import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class Repository implements AutoCloseable {

    private Connection connection;

    public void init() throws IOException {
        try {
            Class.forName("org.sqlite.JDBC");
            connection = DriverManager.getConnection("jdbc:sqlite:test.db");
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
    }

    public Well createWell(Well well) {
        try (PreparedStatement ps = connection.prepareStatement
                ("insert into well(name)values (?)", Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, well.getName());
            ps.execute();
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    well.setId(rs.getInt(1));
                }
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return well;
    }

    public Equipment createEquipment(Equipment equipment) {
        try (PreparedStatement ps = connection.prepareStatement
                ("insert into equipment(name, well_id) values (?,?)", Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, equipment.getName());
            ps.setInt(2, equipment.getWell_id());
            ps.execute();
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    equipment.setId(rs.getInt(1));
                }
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return equipment;
    }

    public Well findWellByName(String name) {
        Well well = new Well();
        try (PreparedStatement ps = connection.prepareStatement("select * from well where name = ?")) {
            ps.setString(1, name);
            ps.execute();
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    well.setId(rs.getInt("id"));
                    well.setName(rs.getString("name"));
                }
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return well;
    }

    public List<WellsEQ> findAllWellsAndCountEquipments(String[] names) {
        List<WellsEQ> wells = new ArrayList<>();
        try (PreparedStatement ps = connection.prepareStatement
                ("select well.name, count(e.id) as equipments " +
                        "from well left join equipment e on well.id = e.well_id " +
                        "where well.name in(?,?,?)" +
                        "group by well.name;")) {
            ps.setString(1, names[0]);
            ps.setString(2, names[1]);
            ps.setString(3, names[2]);
            ps.execute();
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    wells.add(new WellsEQ(
                            rs.getString("name"),
                            rs.getInt("equipments")
                    ));
                }
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return wells;
    }

    public List<Well> findAllWells() {
        List<Well> rsl = new ArrayList<>();
        try (PreparedStatement ps = connection.prepareStatement("select * from well")) {
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    rsl.add(new Well(
                            rs.getInt(1),
                            rs.getString("name")
                    ));
                }
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return rsl;
    }

    public List<Equipment> findAllEquipments() throws SQLException {
        List<Equipment> rsl = new ArrayList<>();
        try (PreparedStatement ps = connection.prepareStatement("select * from equipment;")) {
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    rsl.add(new Equipment(
                            rs.getInt(1),
                            rs.getString("name"),
                            rs.getInt("well_id")
                    ));
                }
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        }
        return rsl;
    }

    public void close() throws SQLException {
        if (connection != null) {
            connection.close();
        }
    }
}
