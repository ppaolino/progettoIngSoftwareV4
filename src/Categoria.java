/**
 * Interfaccia per rappresentare una Categoria.
 * 
 * @invariant Nome e ID di una Categoria devono essere unici.
 */
public interface Categoria {

    /**
     * Restituisce il nome della categoria.
     *
     * @pre nessuna condizione predefinita.
     * @post restituisce il nome della categoria come String.
     * @return il nome della categoria.
     */
    public String getNome();

    /**
     * Restituisce l'ID della categoria.
     *
     * @pre nessuna condizione predefinita.
     * @post restituisce l'ID della categoria come intero.
     * @return l'ID della categoria.
     */
    public int getId();
}

