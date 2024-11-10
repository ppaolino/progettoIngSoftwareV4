import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class Proposta {
    private final Foglia richiesta;
    private final int oreRichieste;
    private static int idCounter = 0;
    private final int id;
    private final int idFruitore;
    private String stato;
    private Foglia offerta;
    private int oreOfferte;
    private final String timestamp;
    private final String FILE_NAME = "proposte.txt";

    /**
     * Precondizioni:
     * - `richiesta` non deve essere null.
     * - `oreRichieste` deve essere un intero positivo (> 0).
     * - `idFruitore` deve essere un intero positivo (> 0).
     * 
     * Postcondizioni:
     * - Viene creato un oggetto `Proposta` con un nuovo ID.
     * - Lo stato iniziale è impostato su "aperta".
     * - Il timestamp corrente viene salvato.
     */
    public Proposta(Foglia richiesta, int oreRichieste, int idFruitore) {
        this.id = idCounter++;
        this.idFruitore = idFruitore;
        this.richiesta = richiesta;
        this.oreRichieste = oreRichieste;
        this.stato = "aperta";
        LocalDateTime currentTime = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
        timestamp = currentTime.format(formatter);
    }

    /**
     * Precondizioni:
     * - `id` deve essere un intero positivo (> 0).
     * - `richiesta` e `offerta` non devono essere null.
     * - `oreRichieste` e `oreOfferte` devono essere interi positivi (> 0).
     * - `stato` non deve essere null e deve essere un valore valido ("aperta" o "chiusa").
     * - `idFruitore` deve essere un intero positivo (> 0).
     * - `timestamp` non deve essere null e deve essere una data valida.
     * 
     * Postcondizioni:
     * - Viene creato un oggetto `Proposta` con i parametri specificati.
     * - `idCounter` viene aggiornato al valore successivo.
     */
    public Proposta(int id, Foglia richiesta, int oreRichieste, Foglia offerta, int oreOfferte, String stato, int idFruitore, String timestamp) {
        this.id = id;
        this.idFruitore = idFruitore;
        this.richiesta = richiesta;
        this.oreRichieste = oreRichieste;
        this.offerta = offerta;
        this.oreOfferte = oreOfferte;
        this.stato = stato;
        this.timestamp = timestamp;
        idCounter = id + 1;
    }

    /**
     * Precondizioni:
     * - `offerta` non deve essere null.
     * - Deve esistere un'istanza valida di `Fattorediconversione`.
     * 
     * Postcondizioni:
     * - L'attributo `offerta` viene impostato.
     * - `oreOfferte` viene calcolato e aggiornato in base al fattore di conversione.
     */
    public void soddisfaProposta(Foglia offerta) {
        assert offerta != null : "offerta non deve essere nulla";
        
        Fattorediconversione fattore = Fattorediconversione.getInstance();
        this.offerta = offerta;
        oreOfferte = fattore.getOreConvertite(richiesta.getId(), oreRichieste).get(offerta.getId());
    }

    @Override
    public String toString() {
        return "ID: " + id + "; Richiesta: " + richiesta.getId() + "; OreRichieste: " + oreRichieste +
               "; Offerta: " + offerta.getId() + "; OreOfferte: " + oreOfferte + "; Stato: " + stato +
               "; IdFruitore: " + idFruitore + "; Ora: " + timestamp;
    }

    public int getId() {
        return id;
    }

    public Foglia getRichiesta() {
        return richiesta;
    }

    public int getOreRichieste() {
        return oreRichieste;
    }

    public int getIdFruitore() {
        return idFruitore;
    }

    public String getStato() {
        return stato;
    }

    public void setStato(String stato) {
        this.stato = stato;
    }

    public Foglia getOfferta() {
        return offerta;
    }

    public int getOreOfferte() {
        return oreOfferte;
    }

    public String getTime() {
        return timestamp;
    }

    /**
     * Precondizioni:
     * - `FILE_NAME` deve essere un percorso valido scrivibile.
     * 
     * Postcondizioni:
     * - Se il file `proposte.txt` non esiste, viene creato.
     * - Se esiste una proposta con lo stesso ID, viene aggiornata.
     * - Se non esiste una proposta con lo stesso ID, ne viene aggiunta una nuova.
     * - In caso di errore nella creazione, lettura o scrittura del file, viene stampato un messaggio di errore.
     */
    public void save() {
        File file = new File(FILE_NAME);
        try {
            if (!file.exists()) {
                file.createNewFile();
            }
        } catch (IOException e) {
            System.err.println("errore nella creazione del file");
            return;
        }

        List<String> fileContent = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            boolean updated = false;

            while ((line = reader.readLine()) != null) {
                if (line.startsWith("ID: " + id + ";")) {
                    line = toString();
                    fileContent.add(line);
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
}
