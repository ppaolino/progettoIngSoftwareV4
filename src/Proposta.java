import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class Proposta {
    private final Foglia richiesta;
    private final int oreRichieste;
    private static int idCounter=0;
    private final int id;
    private final int idFruitore;
    private String stato;
    private Foglia offerta;
    private int oreOfferte;
    private final String FILE_NAME="proposte.txt";


    public Proposta(Foglia richiesta, int oreRichieste, int idFruitore){
        this.id=idCounter++;
        this.idFruitore = idFruitore;
        this.richiesta=richiesta;
        this.oreRichieste=oreRichieste;
        this.stato="aperta";
    }

    public Proposta(int id, Foglia richiesta, int oreRichieste, Foglia offerta, int oreOfferte, String stato, int idFruitore){
        this.id=id;
        this.idFruitore = idFruitore;
        this.richiesta=richiesta;
        this.oreRichieste=oreRichieste;
        this.offerta=offerta;
        this.oreOfferte=oreOfferte;
        this.stato=stato;
        idCounter = id +1;
    }

    public void soddisfaProposta(Foglia offerta){
        Fattorediconversione fattore = Fattorediconversione.getInstance();
        this.offerta=offerta;
        oreOfferte = fattore.getOreConvertite(richiesta.getId(), oreRichieste).get(offerta.getId());
    }

    @Override
    public String toString() {
        LocalDateTime currentTime = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
        String timestamp = currentTime.format(formatter);
        return "ID: " + id + "; Richiesta: " + richiesta.getId() + "; OreRichieste: " + oreRichieste + "; Offerta: " + offerta.getId() + "; OreOfferte: " + oreOfferte + "; Stato: " + stato + "; IdFruitore: " + idFruitore + "; Ora: " + timestamp;
    }

    public int getId(){
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

    public void setStato(String stato){
        this.stato=stato;
    }

    public Foglia getOfferta() {
        return offerta;
    }

    public int getOreOfferte() {
        return oreOfferte;
    }

    // Metodo per salvare o aggiornare l'istanza della Foglia nel file
    public void save() {
        // Verifica se il file esiste e lo crea se necessario
        File file = new File(FILE_NAME);
        try {
            if (!file.exists()) {
                file.createNewFile(); // Crea il file se non esiste
            }
        } catch (IOException e) {
            System.err.println("errore nella lettura del file");
            return; // Esce dal metodo se la creazione del file fallisce
        }

        List<String> fileContent = new ArrayList<>(); // Lista per memorizzare il contenuto del file

        // Legge il contenuto dell'intero file
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            boolean updated = false;

            // Legge ogni riga del file
            while ((line = reader.readLine()) != null) {
                // Se la riga corrisponde a questa istanza di Foglia (basata sull'ID), la sostituisce
                if (line.startsWith("ID: " + id + ";")) {
                    fileContent.add(line); // Sostituisce con i dati aggiornati
                    line = toString();
                    fileContent.add(line); // Sostituisce con i dati aggiornati
                    updated = true;
                } else {
                    fileContent.add(line); // Mantiene le altre righe invariate
                }
            }

            // Se l'ID non è stato trovato, aggiunge una nuova voce alla fine del file
            if (!updated) {
                fileContent.add(toString());
            }

        } catch (IOException e) {
            System.err.println("errore nella lettura del file");
        }

        // Scrive il contenuto aggiornato nel file
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
            for (String line : fileContent) {
                writer.write(line);  // Scrive ogni riga nel file
                writer.newLine();    // Aggiunge una nuova linea dopo ogni riga
            }
        } catch (IOException e) {
            System.err.println("errore nella scrittura del file");
        }
    }

}
