import java.io.*;
import java.util.ArrayList;
import java.util.Collections;

public final class Fattorediconversione {
    // Lista bidimensionale per memorizzare i fattori di conversione
    private static final ArrayList<ArrayList<Float>> fattori = new ArrayList<>();
    // Nome del file in cui vengono salvati i fattori di conversione
    private static final String FILE_NAME = "fattori.txt";
    // Limiti minimo e massimo per il fattore di conversione
    private final float min = 0.5f;
    private final float max = 2f;

    private static final Fattorediconversione instance = new Fattorediconversione();

    // Costruttore privato che inizializza caricando i fattori dal file
    private Fattorediconversione() {
        loadFattoriFromFile();  // Chiama il metodo per caricare i fattori dal file
    }

    /**
     * Restituisce l'istanza singleton di Fattorediconversione.
     * Precondizione: Nessuna.
     * Postcondizione: Viene restituita l'unica istanza di Fattorediconversione.
     * 
     * @return Istanza singleton di Fattorediconversione.
     */
    public static Fattorediconversione getInstance() {
        return instance;
    }

    /**
     * Carica i dati dei fattori dal file `fattori.txt`.
     * Precondizione: Il file `fattori.txt` deve essere accessibile e nel formato corretto.
     * Postcondizione: La lista `fattori` viene popolata con i valori presenti nel file.
     */
    public void loadFattoriFromFile() {
        File file = new File(FILE_NAME); // Puntatore al file fattori.txt

        if (!file.exists()) {
            System.err.println(FILE_NAME + " non esiste. Impossibile caricare i dati.");
            return; // Esce dal metodo per evitare errori
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(":");

                if (parts.length != 2) {
                    continue;
                }

                int index = Integer.parseInt(parts[0].trim());
                String fattoriPart = parts[1].trim().replace("[", "").replace("]", "");

                String[] factorValues = fattoriPart.split(",");
                ArrayList<Float> fattoriList = new ArrayList<>();

                for (String factor : factorValues) {
                    try {
                        fattoriList.add(Float.valueOf(factor.trim()));
                    } catch (NumberFormatException e) {
                        System.err.println("Errore nel parsing del valore del fattore: " + factor);
                    }
                }

                while (fattori.size() <= index) {
                    fattori.add(new ArrayList<>());
                }

                fattori.set(index, fattoriList);
            }
        } catch (IOException e) {
            System.err.println("errore nella lettura del file");
        }
    }

    /**
     * Verifica se il file `fattori.txt` esiste, e lo crea se non esiste.
     * Precondizione: Nessuna.
     * Postcondizione: `fattori.txt` è presente nel filesystem.
     */
    private void checkAndCreateFile() {
        File file = new File(FILE_NAME);
        try {
            if (!file.exists()) {
                file.createNewFile();
                System.out.println(FILE_NAME + " creato.");
            } else {
                System.out.println(FILE_NAME + " esiste già.");
            }
        } catch (IOException e) {
            System.err.println("errore nella creazione del file");
        }
    }

    /**
     * Restituisce i fattori di una specifica categoria.
     * Precondizione: `index` deve essere un valore valido (>= 0 e < fattori.size()).
     * Postcondizione: Viene restituita una lista di fattori per l'indice specificato.
     * 
     * @param index Indice della categoria dei fattori.
     * @return Lista di fattori per la categoria indicata.
     */
    public ArrayList<Float> getFattoriCategoria(int index) {
        assert index >= 0 : "index deve essere un numero positivo";

        System.err.println(fattori.size());
        return fattori.get(index);
    }

    /**
     * Aggiorna i fattori in una specifica categoria.
     * Precondizione: `index` deve essere un valore valido e `fattoridainserire` non deve essere nullo.
     * Postcondizione: La categoria specificata viene aggiornata con i nuovi fattori.
     * 
     * @param index Indice della categoria da aggiornare.
     * @param fattoridainserire Lista di fattori da inserire nella categoria.
     */
    public void updateFattori(int index, ArrayList<Float> fattoridainserire) {
        assert index >= 0 : "index deve essere un numero positivo";

        fattori.set(index, fattoridainserire);
    }

    /**
     * Serializza i dati dei fattori in una stringa con l'indice.
     * Precondizione: Nessuna.
     * Postcondizione: Restituisce una stringa rappresentante lo stato attuale dei fattori.
     * 
     * @return Stringa serializzata dei fattori.
     */
    private String serializeFattori() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < fattori.size(); i++) {
            sb.append(i).append(": ").append(fattori.get(i).toString()).append(System.lineSeparator());
        }
        return sb.toString();
    }

    /**
     * Salva i dati dei fattori sul file `fattori.txt`.
     * Precondizione: `fattori.txt` deve essere accessibile per la scrittura.
     * Postcondizione: Lo stato corrente dei fattori viene salvato su `fattori.txt`.
     */
    public void save() {
        checkAndCreateFile();

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_NAME))) {
            writer.write(serializeFattori());
        } catch (IOException e) {
            System.err.println("errore nella scrittura del file");
        }
    }

    /**
     * Aggiunge una nuova riga di fattori.
     * Precondizione: `newFattori` non deve essere nullo.
     * Postcondizione: `newFattori` viene aggiunto alla lista `fattori`.
     * 
     * @param newFattori Lista di nuovi fattori da aggiungere.
     */
    public void addFattori(ArrayList<Float> newFattori) {
        assert newFattori != null : "newFattori deve esistere";
        fattori.add(newFattori);
    }

    /**
     * Arrotonda il valore `num` ai limiti minimi e massimi definiti.
     * Precondizione: `num` è un numero float.
     * Postcondizione: Viene restituito un valore tra `min` e `max`.
     * 
     * @param num Valore da arrotondare.
     * @return Valore arrotondato.
     */
    private float roundResult(float num) {
        return Math.max(min, Math.min(max, num));
    }

    /**
     * Calcola i fattori tra due categorie e aggiorna la matrice dei fattori.
     * Precondizione: `c1id` e `c2id` sono indici validi.
     * Postcondizione: La matrice dei fattori viene aggiornata con i nuovi calcoli.
     * 
     * @param c1id Indice della prima categoria.
     * @param c1fattore Fattore della prima categoria.
     * @param c2id Indice della seconda categoria (o -1 per una nuova categoria).
     */
    public void calcolaFattori(int c1id, float c1fattore, int c2id) {
        ArrayList<Float> rigaC1 = new ArrayList<>();

        if (fattori.isEmpty() && c2id == -1) {
            ArrayList<Float> rigaprecedente = new ArrayList<>();
            rigaprecedente.add(0.0f);
            fattori.add(rigaprecedente);
        } else {
            ArrayList<Float> rigaC2 = fattori.get(c2id);
            rigaC1.add(c1fattore);

            for (int i = fattori.size() - 1; i >= 0; i--) {
                if (i != c2id)
                    rigaC1.add(roundResult(c1fattore * rigaC2.get(i)));
                else
                    rigaC1.add(c1fattore);
            }

            ArrayList<Float> rigaC1Invertita = new ArrayList<>(rigaC1);
            Collections.reverse(rigaC1Invertita);
            fattori.add(rigaC1Invertita);

            for (int i = fattori.size() - 2; i >= 0; i--) {
                ArrayList<Float> supporto = fattori.get(i);
                supporto.add(roundResult((float)Math.pow(rigaC1Invertita.get(i), -1)));
                fattori.set(i, supporto);
            }
        }
    }
   /**
     * Arrotonda un numero float all'intero più vicino.
     *
     * Precondizioni:
     * - `numero` deve essere un valore di tipo float.
     *
     * Postcondizioni:
     * - Restituisce un valore intero ottenuto arrotondando il numero al più vicino intero.
     * - Il risultato sarà:
     *   - `Math.floor(numero + 0.5)` se `numero` è positivo.
     *   - `Math.ceil(numero - 0.5)` se `numero` è negativo.
     */
    private int round(float numero) {
        return Math.round(numero);
    }

    /**
     * Converte un numero di ore in base ai fattori associati all'id specificato.
     *
     * Precondizioni:
     * - `id` deve essere un indice valido all'interno della lista `fattori` (i.e., `id >= 0 && id < fattori.size()`).
     * - `fattori.get(id)` deve restituire una lista di numeri decimali (`float` o `double`).
     * - `ore` deve essere un numero intero positivo o zero.
     * - La lista restituita da `fattori.get(id)` non deve essere vuota.
     *
     * Postcondizioni:
     * - Restituisce una `ArrayList<Integer>` contenente i valori ottenuti moltiplicando ogni elemento della lista dei fattori associati a `id` per `ore` e arrotondandolo all'intero più vicino.
     * - L'elemento alla posizione `id` della lista restituita verrà impostato a `null`.
     * - Stampa sullo standard error (`System.err`) il contenuto della lista risultante.
     */
    public ArrayList<Integer> getOreConvertite(int id, int ore) {
        assert id >= 0 && id < fattori.size() : "id deve essere un valore valido";
        assert ore >= 0 : "ore deve essere un valore valido";
        
        ArrayList<Integer> result = new ArrayList<>();

        for (int i = 0; i < fattori.get(id).size(); i++) {
            result.add(round(fattori.get(id).get(i) * ore));
        }
        
        // Imposta il valore a null nella posizione 'id'
        result.set(id, null);
        
        // Stampa il risultato sullo standard error
        System.err.println(result.toString());

        return result;
    }

}
