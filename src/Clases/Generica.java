package Clases;

import Interfaces.Identificable;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class Generica <T extends Identificable>{

    private ArrayList<T> lista;

    public Generica() {
        this.lista = new ArrayList<>();
    }

    public boolean guardar(T elemento) {
        return lista.add(elemento);
    }

    public T BuscarPorId(int id){

        for(T elemento : lista){
            if (elemento.getID().equals(id)) {

                return elemento;

            }
        }
        return null;
    }

    public ArrayList<T> getLista() {
        return lista;
    }

    public void eliminar(int id) {
        for (T elemento : lista) {
            if (elemento.getID().equals(id)) {
                lista.remove(elemento);
            }
        }
    }

}
