package pl.edu.agh.po.dao;

import pl.edu.agh.po.model.Device;
import pl.edu.agh.po.model.DeviceStatus;
import pl.edu.agh.po.model.DeviceType;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;


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
                hostname TEXT,
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

    public void fillExampleData() {
        String routerConfig1 = """
                Building configuration...

                Current configuration : 2841 bytes
                !
                version 15.1
                service timestamps debug datetime msec
                service timestamps log datetime msec
                no service password-encryption
                !
                hostname CORE-ROUTER-01
                !
                boot-start-marker
                boot-end-marker
                !
                enable secret 5 $1$mERr$hx5rVt7rPNoS4wqbXKX7m0
                !
                no aaa new-model
                !
                ip cef
                !
                interface GigabitEthernet0/0
                 description WAN Link to ISP
                 ip address 203.0.113.1 255.255.255.252
                 duplex auto
                 speed auto
                !
                interface GigabitEthernet0/1
                 description LAN Core Network
                 ip address 10.0.1.1 255.255.255.0
                 duplex auto
                 speed auto
                !
                router ospf 1
                 network 10.0.1.0 0.0.0.255 area 0
                 network 203.0.113.0 0.0.0.3 area 0
                !
                ip route 0.0.0.0 0.0.0.0 203.0.113.2
                !
                line con 0
                line vty 0 4
                 login
                 transport input ssh
                !
                end""";

        String routerConfig2 = """
                Building configuration...

                Current configuration : 2156 bytes
                !
                version 15.2
                hostname BRANCH-ROUTER-01
                !
                enable secret 5 $1$mERr$9cTjUIEqNGurQiFU.ZeCi1
                !
                interface GigabitEthernet0/0
                 description WAN to HQ
                 ip address 192.168.100.1 255.255.255.0
                 duplex auto
                 speed auto
                !
                interface GigabitEthernet0/1
                 description Branch LAN
                 ip address 172.16.10.1 255.255.255.0
                 duplex auto
                 speed auto
                !
                router eigrp 100
                 network 172.16.10.0 0.0.0.255
                 network 192.168.100.0 0.0.0.255
                !
                ip dhcp pool BRANCH_LAN
                 network 172.16.10.0 255.255.255.0
                 default-router 172.16.10.1
                 dns-server 8.8.8.8 8.8.4.4
                !
                line con 0
                line vty 0 4
                 login local
                 transport input ssh
                !
                end""";

        String routerConfig3 = """
                Building configuration...

                hostname EDGE-ROUTER-02
                !
                interface GigabitEthernet0/0
                 description Internet Edge
                 ip address dhcp
                 ip nat outside
                !
                interface GigabitEthernet0/1
                 description Internal Network
                 ip address 192.168.1.1 255.255.255.0
                 ip nat inside
                !
                ip nat inside source list 1 interface GigabitEthernet0/0 overload
                !
                access-list 1 permit 192.168.1.0 0.0.0.255
                !
                ip route 0.0.0.0 0.0.0.0 GigabitEthernet0/0
                !
                end""";

        String routerConfig4 = """
                Building configuration...

                hostname DISTRIB-ROUTER-01
                !
                interface GigabitEthernet0/0
                 description Uplink to Core
                 ip address 10.0.2.1 255.255.255.252
                !
                interface GigabitEthernet0/1
                 description VLAN 10 Gateway
                 ip address 10.10.10.1 255.255.255.0
                !
                interface GigabitEthernet0/2
                 description VLAN 20 Gateway
                 ip address 10.10.20.1 255.255.255.0
                !
                router ospf 1
                 network 10.0.0.0 0.255.255.255 area 0
                !
                end""";

        String routerConfig5 = """
                Building configuration...

                hostname BACKUP-ROUTER-01
                !
                interface GigabitEthernet0/0
                 description Backup WAN Link
                 ip address 198.51.100.1 255.255.255.252
                 standby 1 ip 198.51.100.3
                 standby 1 priority 90
                !
                interface GigabitEthernet0/1
                 description Internal Backup Path
                 ip address 10.0.3.1 255.255.255.0
                !
                router bgp 65001
                 neighbor 198.51.100.2 remote-as 65000
                !
                end""";

        String switchConfig1 = """
                Building configuration...

                hostname CORE-SWITCH-01
                !
                vlan 10
                 name MANAGEMENT
                !
                vlan 20
                 name SERVERS
                !
                vlan 30
                 name USERS
                !
                interface GigabitEthernet1/0/1
                 description Uplink to Router
                 switchport mode trunk
                 switchport trunk allowed vlan 10,20,30
                !
                interface GigabitEthernet1/0/2
                 description Server Port
                 switchport access vlan 20
                 switchport mode access
                 spanning-tree portfast
                !
                interface GigabitEthernet1/0/3-24
                 switchport access vlan 30
                 switchport mode access
                 spanning-tree portfast
                !
                interface Vlan10
                 ip address 10.0.10.2 255.255.255.0
                !
                spanning-tree mode rapid-pvst
                !
                end""";

        String switchConfig2 = """
                Building configuration...

                hostname ACCESS-SWITCH-01
                !
                vlan 100
                 name WORKSTATIONS
                !
                interface GigabitEthernet0/1
                 description Uplink to Core
                 switchport mode trunk
                !
                interface range GigabitEthernet0/2-24
                 switchport access vlan 100
                 switchport mode access
                 spanning-tree portfast
                 spanning-tree bpduguard enable
                !
                interface Vlan100
                 ip address 10.100.1.2 255.255.255.0
                !
                ip default-gateway 10.100.1.1
                !
                end""";

        String switchConfig3 = """
                Building configuration...

                hostname DISTRIB-SWITCH-01
                !
                vlan 10,20,30,40,50
                !
                interface GigabitEthernet1/1
                 description Uplink-1
                 switchport trunk encapsulation dot1q
                 switchport mode trunk
                 channel-group 1 mode active
                !
                interface GigabitEthernet1/2
                 description Uplink-2
                 switchport trunk encapsulation dot1q
                 switchport mode trunk
                 channel-group 1 mode active
                !
                interface Port-channel1
                 switchport trunk encapsulation dot1q
                 switchport mode trunk
                !
                spanning-tree mode rapid-pvst
                spanning-tree extend system-id
                !
                end""";

        String apConfig1 = """
                Building configuration...

                hostname OFFICE-AP-01
                !
                dot11 ssid CORP-WIRELESS
                 vlan 40
                 authentication open eap eap_methods
                 authentication key-management wpa version 2
                 wpa-psk ascii CompanyWiFi2024!
                !
                interface Dot11Radio0
                 description 2.4GHz Radio
                 encryption mode ciphers aes-ccm
                 ssid CORP-WIRELESS
                 channel 6
                 station-role root
                !
                interface Dot11Radio1
                 description 5GHz Radio
                 encryption mode ciphers aes-ccm
                 ssid CORP-WIRELESS
                 channel 36
                 station-role root
                !
                interface GigabitEthernet0
                 description Wired uplink
                 switchport access vlan 40
                !
                ip address 10.0.40.10 255.255.255.0
                !
                end""";

        String apConfig2 = """
                Building configuration...

                hostname WAREHOUSE-AP-01
                !
                dot11 ssid WAREHOUSE-NET
                 vlan 50
                 authentication open eap eap_methods
                 authentication key-management wpa version 2
                 wpa-psk ascii Warehouse2024!
                !
                interface Dot11Radio0
                 description 2.4GHz Radio
                 encryption mode ciphers aes-ccm
                 ssid WAREHOUSE-NET
                 channel 1
                 power local 100
                 station-role root
                !
                interface GigabitEthernet0
                 description Wired Connection
                 switchport access vlan 50
                !
                ip address 10.0.50.10 255.255.255.0
                ip default-gateway 10.0.50.1
                !
                end""";

        String sql = """
                INSERT INTO devices (type, status, model, hostname, num_eth_int, configuration)
                VALUES
                    ('ROUTER', 'IN_USE', 'Cisco ISR 4331', 'CORE-ROUTER-01', 4, ?),
                    ('ROUTER', 'IN_USE', 'Cisco ISR 4321', 'BRANCH-ROUTER-01', 4, ?),
                    ('ROUTER', 'AVAILABLE', 'Cisco ISR 4221', 'EDGE-ROUTER-02', 2, ?),
                    ('ROUTER', 'IN_USE', 'Cisco ISR 4351', 'DISTRIB-ROUTER-01', 6, ?),
                    ('ROUTER', 'MAINTANCE', 'Cisco ISR 4431', 'BACKUP-ROUTER-01', 4, ?),
                    ('SWITCH', 'IN_USE', 'Cisco Catalyst 9300-48P', 'CORE-SWITCH-01', 48, ?),
                    ('SWITCH', 'IN_USE', 'Cisco Catalyst 2960X-24TS', 'ACCESS-SWITCH-01', 24, ?),
                    ('SWITCH', 'AVAILABLE', 'Cisco Catalyst 9500-40X', 'DISTRIB-SWITCH-01', 40, ?),
                    ('ACCESS_POINT', 'IN_USE', 'Cisco Aironet 2802I', 'OFFICE-AP-01', 1, ?),
                    ('ACCESS_POINT', 'IN_USE', 'Cisco Aironet 1832I', 'WAREHOUSE-AP-01', 1, ?);
                """;

        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, routerConfig1);
            pstmt.setString(2, routerConfig2);
            pstmt.setString(3, routerConfig3);
            pstmt.setString(4, routerConfig4);
            pstmt.setString(5, routerConfig5);
            pstmt.setString(6, switchConfig1);
            pstmt.setString(7, switchConfig2);
            pstmt.setString(8, switchConfig3);
            pstmt.setString(9, apConfig1);
            pstmt.setString(10, apConfig2);

            pstmt.execute();
        } catch (SQLException e) {
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
    public ArrayList<Device> findAll(){
        String sql = "SELECT * FROM devices";
        ArrayList<Device> devices = new ArrayList<>();
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
        String sql = "UPDATE devices SET type = ?, status = ?, model = ?, hostname = ?, num_eth_int = ?, configuration = ? WHERE id = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, device.getType().name());
            pstmt.setString(2, device.getStatus().name());
            pstmt.setString(3, device.getModel());
            pstmt.setString(4, device.getHostName());
            pstmt.setInt(5, device.getNumberOfEthernetInterfaces());
            pstmt.setString(6, device.getConfiguration());
            pstmt.setLong(7, device.getId());
            pstmt.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void save(Device device){
        String sql = "INSERT INTO devices ('type', 'status', 'model', 'hostname', 'num_eth_int', 'configuration') VALUES (?, ?, ?, ?, ?, ?)";
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
