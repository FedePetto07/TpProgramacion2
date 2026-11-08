package Clases;

public class Embarcacion {

    private String nombre;
    private String matricula;
    private double precioDiario;
    private boolean disponible;

    public Embarcacion(String nombre, String matricula, double precioDiario, boolean disponible) {
        this.nombre = nombre;
        this.matricula = matricula;
        this.precioDiario = precioDiario;
        this.disponible = disponible;
    }

    public String getNombre() {
        return nombre;
    }

    public String getMatricula() {
        return matricula;
    }

    public double getPrecioDiario() {
        return precioDiario;
    }

    public boolean isDisponible() {
        return disponible;
    }

    public void setDisponible(boolean disponible) {
        this.disponible = disponible;
    }

    public void setPrecioDiario(double precioDiario) {
        this.precioDiario = precioDiario;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    @Override
    public String toString() {
        return  "\n |nombre:'" + nombre + '\'' +
                "|\n |matricula:'" + matricula + '\'' +
                "|\n |precioDiario:" + precioDiario +
                "|\n |disponible:" + disponible +
                '|';
    }

}
