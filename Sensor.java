
public abstract class Sensor {
    private String id;
    private String location;
    
    public Sensor(String id, String location) {
        this.id = id;
        this.location = location;
    }

    public String getId() { return id; }
    public String getLocation() { return location;}

    public abstract double tomarLectura();

}
