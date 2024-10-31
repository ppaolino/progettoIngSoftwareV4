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
	private static int idCounter=0;
	private final int id;
	private final Comprensorio comp;
	private static final String FILE_F = "Fruitori.txt";	//---------------------------------
	
	public Fruitore(String username, String password, String mail, Comprensorio comp, int id) {
		
		this.username = username;
		this.password = password;
		this.id = id;
		this.comp = comp;
		this.mail = mail;
		idCounter = id + 1;
	}

	public Fruitore(String username, String password, String mail, Comprensorio comp) {
		this.username = username;
		this.password = password;
		this.id=idCounter++;
		this.comp = comp;
		this.mail = mail;
	}
	
	public int getId () {
		return id;
	}

	public Comprensorio getComprensorio(){
		return comp;
	}

	public String getPassword(){
		return password;
	}

	public String getMail(){
		return mail;
	}

	public String getUsername(){
		return username;
	}

	// Metodo per salvare o aggiornare l'istanza della Foglia nel file
    public void save() {
        // Verifica se il file esiste e lo crea se necessario
        File file = new File(FILE_F);
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
                if (line.startsWith(id + ";")) {
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

        @Override
	public String toString(){
		return id + ";" + username + ";" + password + ";" + mail + ";" + comp.getNome();
	}
	
}
