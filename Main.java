public class Main {
    public static void main(String[] args) {
        EstacionMonitoreo estacion = new EstacionMonitoreo();
        
        estacion.agregarSensor(new SensorHumedadSuelo("H-001", "Lote A"));
        estacion.agregarSensor(new SensorTemperatura("T-001", "Lote B"));
        
        System.out.println("=== PROCESANDO LECTURAS ===");
        estacion.procesarLecturas();
        
        System.out.println("\n=== FILTRANDO CRITICOS (Umbral > 80) ===");
        estacion.filtrarCriticos(80);
    }
}