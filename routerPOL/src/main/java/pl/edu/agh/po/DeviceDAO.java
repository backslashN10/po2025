package pl.edu.agh.po;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class DeviceDAO {
    private static DeviceDAO instance;
    private static Connection connection;

    private DeviceDAO(){
        try {
            connection = DriverManager.getConnection("jdbc:sqlite:rp.db");
            createTable();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public static DeviceDAO getInstance(){
        if (instance == null){
            instance = new DeviceDAO();
        }
        return instance;
    }
    private void createTable(){
        String sql = """
                CREATE TABLE IF NOT EXISTS devices (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                type TEXT NOT NULL,
                status TEXT NOT NULL,
                model TEXT NOT NULL,
                host_name TEXT,
                num_eth_int INTEGER NOT NULL,
                configuration TEXT
                );
                """;
        try (Statement stmt = connection.createStatement()){
            stmt.execute(sql);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public Device findByID(long id){
        String sql = "SELECT * FROM devices WHERE id = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setLong(1, id);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next() == true){
                return new Device(rs.getLong("id"), DeviceType.valueOf(rs.getString("type")), DeviceStatus.valueOf(rs.getString("status")), rs.getString("model"), rs.getString("hostname"), rs.getInt("num_eth_int"), rs.getString("configuration"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    public Device findByType(DeviceType type){
        String sql = "SELECT * FROM devices WHERE type = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, type.name());
            ResultSet rs = pstmt.executeQuery();
            if (rs.next() == true){
                return new Device(rs.getLong("id"), DeviceType.valueOf(rs.getString("type")), DeviceStatus.valueOf(rs.getString("status")), rs.getString("model"), rs.getString("hostname"), rs.getInt("num_eth_int"), rs.getString("configuration"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    public Device findByStatus(DeviceStatus status){
        String sql = "SELECT * FROM devices WHERE status = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, status.name());
            ResultSet rs = pstmt.executeQuery();
            if (rs.next() == true){
                return new Device(rs.getLong("id"), DeviceType.valueOf(rs.getString("type")), DeviceStatus.valueOf(rs.getString("status")), rs.getString("model"), rs.getString("hostname"), rs.getInt("num_eth_int"), rs.getString("configuration"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    public List<Device> findAll(){
        String sql = "SELECT * FROM devices";
        List<Device> devices = new ArrayList<>();
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            ResultSet rs = pstmt.executeQuery();
            while (rs.next() == true){
                devices.add(new Device(rs.getLong("id"), DeviceType.valueOf(rs.getString("type")), DeviceStatus.valueOf(rs.getString("status")), rs.getString("model"), rs.getString("hostname"), rs.getInt("num_eth_int"), rs.getString("configuration")));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return devices;
    }
    public void deleteByID(long id){
        String sql = "DELETE FROM devices WHERE id = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)){
            pstmt.setLong(1, id);
            pstmt.execute();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void updateData(Device device){
        String sql = "UPDATE devices SET type = ?, status = ?, model = ?, host_name = ?, num_eth_int = ?, configuration = ? WHERE id = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, device.getType().name());
            pstmt.setString(2, device.getStatus().name());
            pstmt.setString(3, device.getModel());
            pstmt.setString(4, device.getHostName());
            pstmt.setInt(5, device.getNumberOfEthernetInterfaces());
            pstmt.setString(6, device.getConfiguration());
            pstmt.setLong(7, device.getId());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void save(Device device){
        String sql = "INSERT INTO devices ('type', 'status', 'model', 'host_name', 'num_eth_int', 'configuration') VALUES (?, ?, ?, ?, ?, ?)";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, device.getType().name());
            pstmt.setString(2, device.getStatus().name());
            pstmt.setString(3, device.getModel());
            pstmt.setString(4, device.getHostName());
            pstmt.setInt(5, device.getNumberOfEthernetInterfaces());
            pstmt.setString(6, device.getConfiguration());
            pstmt.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
