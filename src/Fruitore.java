import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Fruitore {

    private final String username;
    private final String password;
    private final String mail;
    private static int idCounter = 0;
    private final int id;
    private final Comprensorio comp;
    private static final String FILE_F = "Fruitori.txt";

    /**
     * Costruttore con ID specificato (per caricamento da file)
     *
     * Precondizioni:
     * - `username`, `password`, `mail`, `comp` non devono essere null.
     * - `id` deve essere un numero intero non negativo.
     *
     * Postcondizioni:
     * - Crea un'istanza di `Fruitore` con un ID specificato.
     * - Incrementa `idCounter` in base al valore dell'ID fornito.
     */
    public Fruitore(String username, String password, String mail, Comprensorio comp, int id) {
        this.username = username;
        this.password = password;
        this.id = id;
        this.comp = comp;
        this.mail = mail;
        idCounter = id + 1;
    }

    /**
     * Costruttore senza ID specificato.
     *
     * Precondizioni:
     * - `username`, `password`, `mail`, `comp` non devono essere null.
     *
     * Postcondizioni:
     * - Crea un'istanza di `Fruitore` con un ID incrementale unico.
     * - Incrementa `idCounter` di 1.
     */
    public Fruitore(String username, String password, String mail, Comprensorio comp) {
        this.username = username;
        this.password = password;
        this.id = idCounter++;
        this.comp = comp;
        this.mail = mail;
    }

    /**
     * Restituisce l'ID del fruitore.
     *
     * Precondizioni:
     * - Nessuna.
     *
     * Postcondizioni:
     * - Restituisce un intero che rappresenta l'ID univoco dell'istanza.
     */
    public int getId() {
        return id;
    }

    /**
     * Restituisce il comprensorio associato.
     *
     * Precondizioni:
     * - Nessuna.
     *
     * Postcondizioni:
     * - Restituisce un'istanza di `Comprensorio` associata a questo fruitore.
     */
    public Comprensorio getComprensorio() {
        return comp;
    }

    /**
     * Restituisce la password del fruitore.
     *
     * Precondizioni:
     * - Nessuna.
     *
     * Postcondizioni:
     * - Restituisce una stringa contenente la password.
     */
    public String getPassword() {
        return password;
    }

    /**
     * Restituisce l'indirizzo email del fruitore.
     *
     * Precondizioni:
     * - Nessuna.
     *
     * Postcondizioni:
     * - Restituisce una stringa contenente l'indirizzo email.
     */
    public String getMail() {
        return mail;
    }

    /**
     * Restituisce il nome utente del fruitore.
     *
     * Precondizioni:
     * - Nessuna.
     *
     * Postcondizioni:
     * - Restituisce una stringa contenente il nome utente.
     */
    public String getUsername() {
        return username;
    }

    /**
     * Salva o aggiorna l'istanza del fruitore nel file.
     *
     * Precondizioni:
     * - Il file "Fruitori.txt" deve essere accessibile in lettura/scrittura.
     * - `toString()` deve restituire una stringa non null.
     *
     * Postcondizioni:
     * - Se l'ID è già presente nel file, i dati del fruitore vengono aggiornati.
     * - Se l'ID non è presente, viene aggiunta una nuova voce alla fine del file.
     * - Il file viene sovrascritto con i dati aggiornati.
     */
    public void save() {
        File file = new File(FILE_F);
        try {
            if (!file.exists()) {
                file.createNewFile();
            }
        } catch (IOException e) {
            System.err.println("errore nella lettura del file");
            return;
        }

        List<String> fileContent = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            boolean updated = false;

            while ((line = reader.readLine()) != null) {
                if (line.startsWith(id + ";")) {
                    fileContent.add(toString()); // Aggiorna i dati dell'istanza
                    updated = true;
                } else {
                    fileContent.add(line);
                }
            }

            if (!updated) {
                fileContent.add(toString());
            }

        } catch (IOException e) {
            System.err.println("errore nella lettura del file");
        }

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
            for (String line : fileContent) {
                writer.write(line);
                writer.newLine();
            }
        } catch (IOException e) {
            System.err.println("errore nella scrittura del file");
        }
    }

    /**
     * Restituisce una rappresentazione stringa del fruitore.
     *
     * Precondizioni:
     * - `comp.getNome()` deve restituire un nome valido e non null.
     *
     * Postcondizioni:
     * - Restituisce una stringa formattata contenente l'ID, username, password, email e nome del comprensorio.
     */
    @Override
    public String toString() {
        return id + ";" + username + ";" + password + ";" + mail + ";" + comp.getNome();
    }
}
