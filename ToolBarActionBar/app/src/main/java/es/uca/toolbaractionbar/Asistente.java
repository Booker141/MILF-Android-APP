package es.uca.toolbaractionbar;

import java.sql.Date;
import java.sql.Timestamp;

public class Asistente {
    private String nombre;
    private String dni;
    private String telefono;
    private Date fechaNac;
    private Timestamp fechaIns;

    public Asistente(String nombre, String dni, String telefono, Date fechaNac, Timestamp fechaIns) {
        this.nombre = nombre;
        this.dni = dni;
        this.telefono = telefono;
        this.fechaNac = fechaNac;
        this.fechaIns = fechaIns;
    }

    public String getNombre() { return nombre; }
    public String getDNI() { return dni; }
    public String getTelefono() { return telefono; }
    public Date getFechaNac() { return fechaNac; }
    public Timestamp getFechaIns() { return fechaIns; }

    public void setNombre(String nombre) { this.nombre = nombre; }
    public void setDNI(String dni) { this.dni = dni; }
    public void setTelefono(String telefono) { this.telefono = telefono; }
    public void setFechaNac(Date fechaNac) { this.fechaNac = fechaNac; }
    public void setFechaIns(Timestamp fechaIns) { this.fechaIns = fechaIns; }
}
