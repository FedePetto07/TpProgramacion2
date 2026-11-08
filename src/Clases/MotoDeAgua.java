package Clases;

public class MotoDeAgua extends Embarcacion {

    private int cilindrada;
    private double limiteVelocidad;
    private boolean requiereLicencia;

    public MotoDeAgua(String nombre, String matricula, double precioDiario, boolean disponible, int cilindrada, double limiteVelocidad, boolean requiereLicencia) {
        super(nombre, matricula, precioDiario, disponible);
        this.cilindrada = cilindrada;
        this.limiteVelocidad = limiteVelocidad;
        this.requiereLicencia = requiereLicencia;
    }

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
    public String toString() {
        return "\nMotoDeAgua" + super.toString() +
                " \n |cilindrada:" + cilindrada +
                "\n |limiteVelocidad:" + limiteVelocidad +
                "\n |requiereLicencia:" + requiereLicencia +
                '|';
    }

}
