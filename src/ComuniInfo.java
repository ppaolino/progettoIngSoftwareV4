import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ArrayList;
import org.json.JSONArray;
import org.json.JSONObject;

/**
 * Classe singleton per la gestione delle informazioni sui comuni e dei loro comuni limitrofi.
 * 
 * Invariante: La mappa dei comuni limitrofi è sempre popolata con dati corretti.
 */
public class ComuniInfo {
    private static ComuniInfo instance;
    private final Map<String, List<String>> comuniLimitrofi;
    private static final String FILE_NAME = "lista.json";

    // Costruttore privato per il singleton
    private ComuniInfo() {
        comuniLimitrofi = new HashMap<>();
        caricaDatiDaJson(FILE_NAME);
    }

    // Metodo per ottenere l'istanza singleton
    public static synchronized ComuniInfo getInstance() {
        if (instance == null) {
            instance = new ComuniInfo();
        }
        return instance;
    }

    /**
     * Carica i dati dal file JSON specificato e popola la mappa dei comuni e limitrofi.
     * 
     * Precondizione: Il file JSON deve esistere e avere una struttura valida.
     * Postcondizione: La mappa dei comuni limitrofi è popolata con i dati dal file JSON.
     * 
     * @param filename Il nome del file JSON contenente i dati dei comuni e limitrofi.
     */
    private void caricaDatiDaJson(String filename) {
        try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }
            String jsonData = sb.toString();

            // Parsing del JSON
            JSONObject jsonObject = new JSONObject(jsonData);

            // Per ogni comune nel file JSON, crea la lista dei comuni limitrofi
            for (String comune : jsonObject.keySet()) {
                JSONArray limitrofiArray = jsonObject.getJSONArray(comune);
                List<String> limitrofiList = new ArrayList<>();
                
                for (int i = 0; i < limitrofiArray.length(); i++) {
                    limitrofiList.add(limitrofiArray.getString(i));
                }
                comuniLimitrofi.put(comune, limitrofiList);
            }
        } catch (IOException e) {
            System.err.println("Errore nella lettura del file JSON " + filename + ": " + e.getMessage());
        } catch (Exception e) {
            System.err.println("Errore generico durante il caricamento del file JSON: " + e.getMessage());
        }
    }

    /**
     * Ritorna la lista di comuni limitrofi per un dato comune.
     * 
     * Precondizione: Il comune deve esistere nella mappa.
     * Postcondizione: La lista dei comuni limitrofi è restituita correttamente.
     * 
     * @param comune Il nome del comune di cui si vogliono i limitrofi.
     * @return La lista dei comuni limitrofi, o una lista vuota se il comune non esiste.
     */
    public List<String> getLimitrofi(String comune) {
        return comuniLimitrofi.getOrDefault(comune, new ArrayList<>());
    }

    /**
     * Verifica se un comune esiste nella mappa dei comuni limitrofi.
     * 
     * Precondizione: nessuna.
     * Postcondizione: Ritorna true se il comune esiste, altrimenti false.
     * 
     * @param comune Il nome del comune da verificare.
     * @return true se il comune esiste, altrimenti false.
     */
    public boolean esisteComune(String comune) {
        return comuniLimitrofi.containsKey(comune);
    }

    /**
     * Verifica se due comuni sono reciprocamente limitrofi.
     * 
     * Precondizione: I comuni devono esistere nella mappa.
     * Postcondizione: Ritorna true se i comuni sono reciprocamente limitrofi, altrimenti false.
     * 
     * @param comune1 Il nome del primo comune.
     * @param comune2 Il nome del secondo comune.
     * @return true se i comuni sono reciprocamente limitrofi, altrimenti false.
     */
    public boolean sonoLimitrofi(String comune1, String comune2) {
        List<String> limitrofiComune1 = comuniLimitrofi.get(comune1);
        List<String> limitrofiComune2 = comuniLimitrofi.get(comune2);
        
        if (limitrofiComune1 != null && limitrofiComune2 != null) {
            return limitrofiComune1.contains(comune2) && limitrofiComune2.contains(comune1);
        }
        return false;
    }

    /**
     * Restituisce la lista di tutti i comuni caricati dal file JSON.
     * 
     * Precondizione: La mappa dei comuni limitrofi deve essere popolata.
     * Postcondizione: Una lista contenente tutti i nomi dei comuni è restituita.
     * 
     * @return Una lista contenente tutti i nomi dei comuni.
     */
    public List<String> getTuttiComuni() {
        return new ArrayList<>(comuniLimitrofi.keySet());
    }
}
