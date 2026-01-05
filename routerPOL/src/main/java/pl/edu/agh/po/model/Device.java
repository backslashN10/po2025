package pl.edu.agh.po.model;

public class Device {
    private long id;
    private DeviceType type;
    private DeviceStatus status;
    private String model;
    private String hostName;
    private int numberOfEthernetInterfaces;
    private String configuration;

    public Device(long id, DeviceType type, DeviceStatus status, String model, String hostname, int numberOfEthernetInterfaces, String configuration){
        this.id = id;
        this.type = type;
        this.status = status;
        this.model = model;
        this.hostName = hostname;
        this.numberOfEthernetInterfaces = numberOfEthernetInterfaces;
        this.configuration = configuration;
    }
    public Device(DeviceType type, DeviceStatus status, String model, String hostname, int numberOfEthernetInterfaces, String configuration){
        this.type = type;
        this.status = status;
        this.model = model;
        this.hostName = hostname;
        this.numberOfEthernetInterfaces = numberOfEthernetInterfaces;
        this.configuration = configuration;
    }
    public long getId(){
        return id;
    }
    public DeviceType getType(){
        return type;
    }
    public DeviceStatus getStatus(){
        return status;
    }
    public String getModel(){
        return model;
    }
    public String getHostName(){
        return hostName;
    }
    public int getNumberOfEthernetInterfaces(){
        return numberOfEthernetInterfaces;
    }
    public String getConfiguration(){
        return configuration;
    }
    public void setConfiguration(String configuration){
        this.configuration = configuration;
    }
}
