package Clases;

import Excepciones.DatoErroneoEx;

public class MotoDeAgua extends Embarcacion {

    /// ATRIBUTOS

    private int cilindrada;
    private double limiteVelocidad;
    private boolean requiereLicencia;

    /// CONSTRUCTOR

    public MotoDeAgua(String nombre, String matricula, double precioDiario, boolean disponible, int capacidadPersonas, int cilindrada, double limiteVelocidad, boolean requiereLicencia) throws DatoErroneoEx {
        super(nombre, matricula, precioDiario, disponible, capacidadPersonas);

        if (getCapacidadPersonas() > 3 || getCapacidadPersonas() <= 0) {
            throw new DatoErroneoEx("Las motos de agua tienen que tener de capacidad entre 1 y 3");
        }

        if (!requiereLicencia && cilindrada > 1000) {
            throw new DatoErroneoEx("Las motos para principiantes no pueden superar los 1000 cc.");
        }

        if (!requiereLicencia && limiteVelocidad > 60) {
            throw new DatoErroneoEx("Las motos para principiantes no pueden superar los 60 km/h.");
        }

        if (cilindrada < 1000) {
            throw new DatoErroneoEx("La cilindrada no puede ser menor a 1000.");
        }

        if (limiteVelocidad < 60) {
            throw new DatoErroneoEx("El límite de velocidad debe ser como minimo 60km/h.");
        }

        this.cilindrada = cilindrada;
        this.limiteVelocidad = limiteVelocidad;
        this.requiereLicencia = requiereLicencia;
    }

    /// GETTERS

    public int getCilindrada() {
        return cilindrada;
    }

    public double getLimiteVelocidad() {
        return limiteVelocidad;
    }

    public boolean isRequiereLicencia() {
        return requiereLicencia;
    }

    @Override
    public double calcularPrecio() {
        Double precio = 0.0;

        precio += cilindrada*10.0;
        precio += limiteVelocidad*100;
        precio += getCapacidadPersonas()*50;

        return precio;
    }

    /// ToString

    @Override
    public String toString() {
        return "\nMoto de agua" + super.toString() +
                " \n |cilindrada:" + cilindrada +
                "\n |limiteVelocidad:" + limiteVelocidad +
                "\n |requiereLicencia:" + requiereLicencia +
                '|';
    }

}
