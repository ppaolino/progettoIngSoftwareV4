/**
 * Classe per la configurazione dell'utente.
 * 
 * Invariante: L'oggetto Configuratore deve avere sempre un username e una password non nulli.
 */
public class Configuratore {

    private final String username; // Nome utente
    private final String password; // Password dell'utente
    private final boolean registrato; // Stato di registrazione dell'utente

    /**
     * Costruttore per inizializzare un Configuratore con username e password.
     * 
     * Precondizione: username e password non devono essere nulli.
     * Postcondizione: L'oggetto Configuratore è creato e registrato.
     * 
     * @param username Il nome utente dell'utente.
     * @param password La password dell'utente.
     */
    public Configuratore(String username, String password) {
        if (username == null || password == null) {
            throw new IllegalArgumentException("Username e password non possono essere nulli.");
        }
        this.username = username;
        this.password = password;
        this.registrato = true; // L'utente è considerato registrato
    }

    /**
     * Restituisce il nome utente.
     * 
     * Precondizione: nessuna.
     * Postcondizione: Il nome utente è restituito.
     * 
     * @return Il nome utente.
     */
    public String getUsername() {
        return username;
    }

    /**
     * Restituisce la password.
     * 
     * Precondizione: nessuna.
     * Postcondizione: La password è restituita.
     * 
     * @return La password.
     */
    public String getPassword() {
        return password;
    }

    /**
     * Verifica se l'utente è registrato.
     * 
     * Precondizione: nessuna.
     * Postcondizione: Ritorna true se l'utente è registrato, altrimenti false.
     * 
     * @return true se registrato, false altrimenti.
     */
    public boolean isRegistrato() {
        return registrato;
    }
}
