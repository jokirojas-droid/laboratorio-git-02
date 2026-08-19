public class SensorTemperatura extends Sensor {

    public SensorTemperatura(String id, String location) {
        super(id, location);
    }

    @Override
    public double tomarLectura() {
        return 15 + (Math.random() * 30);
    }
}