public class SensorHumedadSuelo extends Sensor {

    public SensorHumedadSuelo(String id, String location) {
        super(id, location);
    }

    @Override
    public double tomarLectura() {
        return Math.random() * 100;
    }
}