import java.util.ArrayList;
import java.util.List;

public class EstacionMonitoreo {
    private List<Sensor> sensores;

    public EstacionMonitoreo() {
        sensores = new ArrayList<>();
    }

    public void agregarSensor(Sensor sensor) {
        sensores.add(sensor);
    }

    public void procesarLecturas() {
        for (Sensor sensor : sensores) {
            double lectura = sensor.tomarLectura();
            System.out.println("Sensor ID: " + sensor.getId() + ", Ubicacion: " + sensor.getLocation() + ", Lectura: " + lectura);
        }
    }

    public void filtrarCriticos(double umbral) {
        for (Sensor sensor : sensores) {
            double lectura = sensor.tomarLectura();
            if (lectura > umbral) {
                System.out.println("Alerta critica! Sensor ID: " + sensor.getId() + ", Ubicacion: " + sensor.getLocation() + ", Lectura: " + lectura);
            }
        }
    }
}