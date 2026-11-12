package Clases;

import Interfaces.Identificable;
import java.util.ArrayList;
import java.util.Iterator;

public class Generica <T extends Identificable>{

    private ArrayList<T> lista;

    public Generica() {
        this.lista = new ArrayList<>();
    }

    public boolean guardar(T elemento) {
        return lista.add(elemento);
    }

    public T BuscarPorId(Long id){

        for(T elemento : lista){
            if (elemento.getID().equals(id)) {

                return elemento;

            }
        }
        return null;
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

    public boolean eliminar(Long id) {
        Iterator<T> it = lista.iterator();
        while (it.hasNext()) {
            if (it.next().getID().equals(id)) {
                it.remove();
                return true;
            }
        }
        return false;
    }
    public String listarElementos()
    {
        String rta ="";
        for (T elemento : lista)
        {
            rta += elemento.toString();
            rta +="\n";
        }
        return rta;
    }

}
