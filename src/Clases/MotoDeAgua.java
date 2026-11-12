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

        if (!requiereLicencia && cilindrada > 1000) {
            throw new DatoErroneoEx("Las motos para principiantes no pueden superar los 1000 cc.");
        }

        if (!requiereLicencia && limiteVelocidad > 60) {
            throw new DatoErroneoEx("Las motos para principiantes no pueden superar los 60 km/h.");
        }

        if (cilindrada < 1000) {
            throw new DatoErroneoEx("La cilindrada no puede ser menor a 700.");
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

    /// ToString

    @Override
    public String toString() {
        return "\nMotoDeAgua" + super.toString() +
                " \n |cilindrada:" + cilindrada +
                "\n |limiteVelocidad:" + limiteVelocidad +
                "\n |requiereLicencia:" + requiereLicencia +
                '|';
    }

}
