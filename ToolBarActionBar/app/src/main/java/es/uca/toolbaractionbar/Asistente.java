package es.uca.toolbaractionbar;

public class Asistente {
    private String nombre, dni, telefono, fechaNac, fechaIns;

    public Asistente(String nombre, String dni, String telefono, String fechaNac, String fechaIns) {
        this.nombre = nombre;
        this.dni = dni;
        this.telefono = telefono;
        this.fechaNac = fechaNac;
        this.fechaIns = fechaIns;
    }

    public String getNombre() { return nombre; }
    public String getDNI() { return dni; }
    public String getTelefono() { return telefono; }
    public String getFechaNac() { return fechaNac; }
    public String getFechaIns() { return fechaIns; }

    public void setNombre(String nombre) { this.nombre = nombre; }
    public void setDNI(String dni) { this.dni = dni; }
    public void setTelefono(String telefono) { this.telefono = telefono; }
    public void setFechaNac(String fechaNac) { this.fechaNac = fechaNac; }
    public void setFechaIns(String fechaIns) { this.fechaIns = fechaIns; }
}
