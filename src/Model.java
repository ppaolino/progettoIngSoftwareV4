import java.io.*;
import java.util.ArrayList;
import java.util.List;

public final class Model {

    // Oggetto Configuratore
    private Configuratore conf;
    private Fruitore fruit;
    
    // Nomi dei file di configurazione
    private static final String FILE_NAME_CONF = "Configuratori.txt";
    private static final String FILE_NAME = "comprensorio.txt";
    private static final String FILE_F = "Fruitori.txt";	//---------------------------------
    private static final String FILE_P = "proposte.txt";
    private static final String FILE_NAME_I = "insiemiChiusi.txt";

    
    // Singleton delle liste
    private ListaGerarchie listaG = ListaGerarchie.getInstance();
    private Fattorediconversione listaF = Fattorediconversione.getInstance();
    
    // Lista dei comprensori
    private ArrayList<Comprensorio> listaC = new ArrayList<>();

    private ArrayList<Proposta> listaP = new ArrayList<>();


    // Costruttore della classe Model
    public Model() {

        // Precondizione: Il file dei configuratori non deve esistere oppure essere vuoto
        // Postcondizione: Viene creato il file se non esiste
        // Invariante: FILE_NAME_CONF contiene la configurazione degli utenti

        // Configuratore
        File file = new File(FILE_NAME_CONF);
        try {
            if (!file.exists()) {
                file.createNewFile(); // Crea il file se non esiste
            }
        } catch (IOException e) {
            System.err.println("Errore nella creazione del file");
            // Esce se la creazione del file fallisce
        }

        File file2 = new File(FILE_F);
        try {
            if (!file2.exists()) {
                file2.createNewFile(); // Crea il file se non esiste
            }
        } catch (IOException e) {
            System.err.println("Errore nella creazione del file");
            // Esce se la creazione del file fallisce
        }

        // Precondizione: Il file "comprensorio.txt" deve esistere
        // Postcondizione: I comprensori sono caricati in listaC
        // Invariante: listaC contiene l'elenco aggiornato dei comprensori

        // Comprensori
        try (BufferedReader reader = new BufferedReader(new FileReader(FILE_NAME))) {
            String line;
            while ((line = reader.readLine()) != null) {
                // Suddivide la linea in parti
                String[] parti = line.split("; ");
                String nome = parti[0].split(": ")[1].trim();
                String[] comuni = parti[1].split(": ")[1].replaceAll(",\\s*$", "").split(", ");

                // Crea un nuovo oggetto Comprensorio
                Comprensorio c = new Comprensorio(nome);
                
                // Aggiunge i comuni al comprensorio
                for (String elem : comuni) {
                    c.aggiungiComune(elem);
                }
                listaC.add(c);
            }
        } catch (IOException e) {
            System.err.println("Errore nella lettura da file"); // Stampa un errore in caso di problemi di lettura
        }

        // caricamento da file delle richieste
        try (BufferedReader reader = new BufferedReader(new FileReader(FILE_P))) {
            String line;
            boolean crea;
            int i;
            while ((line = reader.readLine()) != null) {
                // Suddivide la linea in parti
                String[] parti = line.split("; ");
                crea = true;
                i = -1;
                //id proposta
                String[] ind = parti[0].trim().split(":");
                int index = Integer.parseInt(ind[1].trim());
                
                

                for (Proposta elem : listaP) {
                    if(elem.getId() == index){
                        crea = false;
                        i = listaP.lastIndexOf(elem);
                    }
                }


                //id richiesta
                ind = parti[1].trim().split(":");
                int index2 = Integer.parseInt(ind[1].trim());

                //foglia richiesta
                Foglia richiesta = getFoglia(index2);

                //ore richiesta
                ind = parti[2].trim().split(":");
                int orer = Integer.parseInt(ind[1].trim());

                //id offerta
                ind = parti[3].trim().split(":");
                int index3 = Integer.parseInt(ind[1].trim());

                //foglia offerta
                Foglia offerta = getFoglia(index3);

                //ore offerta
                ind = parti[4].trim().split(":");
                int oref = Integer.parseInt(ind[1].trim());

                //stato
                String stato = parti[5].split(": ")[1].trim();

                //id fruitore
                ind = parti[6].trim().split(":");
                int index4 = Integer.parseInt(ind[1].trim());

                String timestamp = parti[7].split(": ")[1].trim();
                Proposta p = new Proposta(index, richiesta, orer, offerta, oref, stato, index4, timestamp);
                if(crea)
                    listaP.add(p);
                else
                    listaP.set(i, p);
                
            }
        } catch (IOException e) {
            System.err.println("Errore nella lettura da file"); // Stampa un errore in caso di problemi di lettura
        }
        System.err.println(listaP.size());

    }

    // Precondizione: 'nome' non deve già esistere nei comprensori
    // Postcondizione: Viene creato un nuovo comprensorio se il nome non è già usato
    public Comprensorio creaComprensorio(String nome) {
        assert nome != null : "nome non deve essere null";

        if (verificaDisp(nome)) {
            Comprensorio c = new Comprensorio(nome);
            listaC.add(c);
            return c;
        }
        return null; // Restituisce null se il nome esiste già
    }

    // Restituisce la lista di tutti i comprensori
    // Invariante: listaC contiene tutti i comprensori
    public ArrayList<Comprensorio> getComprensori() {
        return listaC;
    }

    public ArrayList<Proposta> getProposte() {
        return listaP;
    }

    // Restituisce l'oggetto configuratore
    // Invariante: conf contiene la configurazione dell'utente
    public Configuratore getConfiguratore() {
        return conf;
    }

    // Restituisce l'oggetto fruitore
    // Invariante: fruit contiene la configurazione dell'utente
    public Fruitore getFruitore() {
        return fruit;
    }

    private int getCounterId() {
    	
        try (BufferedReader reader = new BufferedReader(new FileReader(FILE_F))) {
           
            String linea;
            int countId = 0;
            if((linea = reader.readLine()) == null) {
                return 0;
            }else {
                do{
                    String [] credenziale = linea.split(";");    			
                    countId = Integer.parseInt(credenziale[0]);
                } while((linea = reader.readLine()) != null);
                return countId;
            }
        } catch (Exception e) {
            System.err.println("errore nella lettura da file");
         }
        return 0;
   }


    // Precondizione: 'user' e 'psw' devono essere validi
    // Postcondizione: Viene creato un nuovo oggetto Configuratore
    public void creaConfiguratore(String user, String psw) {
        conf = new Configuratore(user, psw);
    }
    public void creaFruitore(String username, String password, String mail, Comprensorio comp){
        fruit = new Fruitore(username, password, mail, comp, getCounterId() + 1);
        fruit.save();
    }

    // Precondizione: 'username' e 'password' non devono essere null
    // Postcondizione: Restituisce true se il configuratore esiste, false altrimenti
    public boolean listaContains(String username, String password) {
        assert username != null : "username non deve essere null";
        assert password != null : "password non deve essere null";

        try (BufferedReader reader = new BufferedReader(new FileReader(FILE_NAME_CONF))) {
            String linea;
            while ((linea = reader.readLine()) != null) {
                String[] credenziale = linea.split(";");
                String c1 = credenziale[0];
                String c2 = credenziale[1];
                if (check(c1, c2, username, password)) {
                    return true;
                }
            }
        } catch (Exception e) {
            System.err.println("Errore nella lettura del file");
        }
        return false;
    }

    // Precondizione: 'username' e 'password' non devono essere null
    // Postcondizione: Restituisce true se il fruitore esiste, false altrimenti
    public boolean listaContainsF(String username, String password) {  
        assert username != null : "username non deve essere null";
        assert password != null : "password non deve essere null";
    	
    	try (BufferedReader reader = new BufferedReader(new FileReader(FILE_F))) {
    		String linea;
    		while ((linea = reader.readLine()) != null) {
    			String[] credenziale = linea.split(";");
    			String c1 = credenziale[1];
                String c2 = credenziale[2];
                if(check(c1, c2, username, password)) {
                    fruit = new Fruitore(username, password, credenziale[3],getCompbyName(credenziale[4].trim()), Integer.parseInt(credenziale[0].trim()));
                    return true;      
                }
                	        
    		}	
    	} catch (Exception e) {
    		System.err.println("errore nella lettura da file");
    	}
    	return false;
	}


    // Verifica se username e password corrispondono
    // Precondizione: username, password c1 e c2 non devono essere null
    // Postcondizione: Restituisce true se il nome è già in uso (sia tra i fruitori che i configuratori)
    public boolean check(String c1, String c2, String username, String password) {
        assert username != null : "username non deve essere null";
        assert password != null : "password non deve essere null";
        assert c1 != null : "c1 non deve essere null";
        assert c2 != null : "c2 non deve essere null";

        return c1.equals(username) && c2.equals(password);
    }

    // Precondizione: 'username' e 'password' devono essere validi
    // Postcondizione: Salva le credenziali nel file di configurazione
    public void salvaConfig(String username, String password) {
        assert username != null : "username non deve essere null";
        assert password != null : "password non deve essere null";

        String[] stringheDaSalvare = {username, password};
        try (FileWriter writer = new FileWriter(FILE_NAME_CONF, true)) {
            writer.write("\n");
            for (String stringa : stringheDaSalvare) {
                writer.write(stringa + ";");
            }
        } catch (IOException e) {
            System.err.println("Errore nella scrittura del file");
        }
    }

    // Precondizione: 'username' non deve essere null
    // Postcondizione: Restituisce true se il nome è già in uso (sia tra i fruitori che i configuratori)
    public boolean inUso(String username) {  	
        assert username != null : "username non deve essere null";


		try (BufferedReader readerF = new BufferedReader(new FileReader(FILE_F));
			 BufferedReader readerC = new BufferedReader(new FileReader(FILE_NAME_CONF))) {
			
			String linea;
			
			while ((linea = readerF.readLine()) != null) {
				String [] credenziale = linea.split(";");
				String name = credenziale[0];
				if (name.equals(username))
					return true;    				
			}
			while ((linea = readerC.readLine()) != null) {
				String [] credenziale = linea.split(";");
				String name = credenziale[0];
				if (name.equals(username))
					return true;    				
			}			   			    			    				   			
		} catch (Exception e) {
			System.err.println("errore nella lettura da file");
		}
	    return false;
    }

    // Precondizione: 'username' e 'password' non devono essere null
    // Postcondizione: Verifica se le credenziali sono corrette
    public boolean validateLogin(String username, String password) {
        assert username != null : "username non deve essere null";
        assert password != null : "password non deve essere null";

        return "a".equals(username) && "p".equals(password);
    }

    // Restituisce la lista delle gerarchie
    // Invariante: listaG contiene tutte le gerarchie
    public ArrayList<Nonfoglia> getListaGerarchie() {
        return listaG.getGerarchie();
    }

    // Precondizione: 'radici' non deve essere vuoto
    // Postcondizione: Restituisce il nodo con l'ID specificato o null se non trovato
    public Nonfoglia trovaNodo(ArrayList<Nonfoglia> radici, int id) {
        assert !radici.isEmpty() : "la lista delle radici non deve essere nulla";

        for (Nonfoglia radice : radici) {
            Nonfoglia risultato = cercaNodo(radice, id);
            if (risultato != null) {
                return risultato;
            }
        }
        return null; // Nodo non trovato
    }

    // Metodo ricorsivo per cercare il nodo con l'ID specificato
    // Precondizione: 'nodo' non deve essere null
    // Postcondizione: Restituisce il nodo con l'ID o null se non trovato
    public Nonfoglia cercaNodo(Nonfoglia nodo, int id) {
        assert nodo != null : "il nodo non deve essere nullo";

        // Se il nodo ha l'ID cercato, ritorniamo il nodo
        if (nodo.getId() == id) {
            return nodo;
        }

        // Se il nodo è una foglia, restituiamo null
        if ("foglie".equals(nodo.getTipoFigli())) return null;

        // Altrimenti, cerchiamo tra i figli del nodo
        for (Categoria figlio : nodo.getFigli()) {
            if (figlio instanceof Nonfoglia nonfoglia) {
                Nonfoglia risultato = cercaNodo(nonfoglia, id);
                if (risultato != null) {
                    return risultato;
                }
            }
        }
        return null; // Nodo non trovato tra i figli
    }

    // Restituisce la gerarchia associata all'ID specificato
    // Precondizione: 'id' deve essere un ID valido esistente nelle gerarchie
    // Postcondizione: Restituisce un oggetto Nonfoglia che rappresenta la gerarchia
    public Nonfoglia getGerarchia(int id) {
        assert id >= 0 : "l'id deve essere valido";

        return listaG.getGerarchiaID(id);
    }

    // Metodo che lancia il salvataggio di tutte le gerarchie e fattori di conversione
    // Precondizione: Nessuna
    // Postcondizione: Tutte le gerarchie e i fattori di conversione sono salvati
    public void salvaGerarchie() {
        listaG.save(); // Salva le gerarchie
        listaF.save(); // Salva i fattori di conversione
    }

    // Salva tutti i comprensori nella lista
    // Precondizione: Nessuna
    // Postcondizione: Tutti i comprensori vengono salvati
    public void salvaComprensori() {
        for (Comprensorio elem : listaC) 
            elem.save(); // Salva ciascun comprensorio
    }

    // Restituisce una lista di tutte le foglie di tutte le gerarchie
    // Precondizione: Nessuna
    // Postcondizione: Restituisce una lista di stringhe che rappresentano tutte le foglie
    public ArrayList<String> getAllLeaves() {
        ArrayList<String> result = new ArrayList<>(); // Lista per memorizzare il risultato
        for (Nonfoglia radice : listaG.getGerarchie()) {
            getLeavesFromNode(radice, result, radice.getNome()); // Ottiene le foglie da ciascun nodo
        }
        return result; // Restituisce la lista di foglie
    }

    // Metodo usato da getAllLeaves per ottenere le foglie da un nodo specifico
    // Precondizione: 'node' non deve essere null
    // Postcondizione: Aggiorna la lista result con le foglie trovate
    private void getLeavesFromNode(Nonfoglia node, List<String> result, String rootName) {
        assert node != null : "il nodo non deve essere null";

        for (Categoria child : node.getFigli()) {
            switch (child) {
                case null -> {} // Ignora i figli null
                case Foglia leaf -> result.add(rootName + ":" + leaf.getNome() + ":" + leaf.getId()); // Aggiunge la foglia alla lista
                case Nonfoglia nonfoglia -> getLeavesFromNode(nonfoglia, result, rootName); // Ricorsione per i nodi non foglia
                default -> {} // Caso di default
            }
        }
    }

    // Metodo che crea una categoria foglia
    // Precondizione: 'nome' e 'dom' devono essere validi
    // Postcondizione: Restituisce un oggetto Foglia creato e aggiunto
    public Foglia creaFoglia(String nome, float fattore, int idfattore, int idPadre, String dom) {
        assert nome != null : "nome non deve essere null";
        assert dom != null : "dominio non deve essere null";

        if (getAllLeaves().isEmpty())
            idfattore = -1; // Imposta idfattore se non ci sono foglie

        Foglia f = new Foglia(nome, fattore, idfattore); // Crea la foglia
        aggiungiFiglio(f, idPadre, dom); // Aggiunge la foglia al padre
        // listaF.calcolaFattori(f.getId(), fattore, idfattore); // Calcolo dei fattori (commentato)
        return f; // Restituisce la foglia creata
    }

    // Metodo che crea una categoria non foglia
    // Precondizione: 'nome', 'campo', 'dominio', 'desc' devono essere validi
    // Postcondizione: Restituisce un oggetto Nonfoglia creato e aggiunto
    public Nonfoglia creaNonFoglia(String nome, String campo, String[] dominio, String[] desc, boolean isRadice, int idPadre, String dom, String tipoFigli) {
        assert nome != null : "nome non deve essere null";
        assert campo != null : "campo non deve essere null";
        assert dominio != null : "dominio non deve essere null";
        assert desc != null : "desc non deve essere null";

        Nonfoglia nfoglia = new Nonfoglia(nome, campo, dominio, desc, isRadice); // Crea la non foglia
        nfoglia.setTipoFigli(tipoFigli); // Imposta il tipo di figli

        if (isRadice) 
            listaG.aggiungiGerarchia(nfoglia); // Aggiunge la non foglia alla lista se è radice
        else 
            aggiungiFiglio(nfoglia, idPadre, dom); // Aggiunge al padre se non è radice

        return nfoglia; // Restituisce la non foglia creata
    }

    // Metodo che fa l'aggiunta di un figlio di tipo nonfoglia
    // Precondizione: 'nf' non deve essere null, 'id' deve essere valido
    // Postcondizione: Aggiunge 'nf' come figlio del nodo padre
    public void aggiungiFiglio(Nonfoglia nf, int id, String dom) {
        assert nf != null : "la foglia non deve essere null";
        assert id >= 0 : "l'id deve essere positivo";

        Nonfoglia padre = trovaNodo(listaG.getGerarchie(), id); // Trova il nodo padre
        if (padre != null) 
            padre.aggiungiFiglio(nf, dom); // Aggiunge il figlio se il padre esiste
    }

    // Metodo che fa l'aggiunta di un figlio di tipo foglia
    // Precondizione: 'nf' non deve essere null, 'id' deve essere valido
    // Postcondizione: Aggiunge 'nf' come figlio al nodo padre
    public void aggiungiFiglio(Foglia nf, int id, String dom) {
        assert nf != null : "la foglia non deve essere null";
        assert id >= 0 : "l'id deve essere positivo";

        Nonfoglia padre = trovaNodo(listaG.getGerarchie(), id); // Trova il nodo padre
        if (padre != null && "foglie".equals(padre.getTipoFigli()))  
            padre.aggiungiFiglio(nf, dom); // Aggiunge il figlio se il padre è di tipo foglia
    }

    // Metodo che verifica la disponibilità del nome di una radice
    // Precondizione: 'name' non deve essere null
    // Postcondizione: Restituisce true se il nome è disponibile, false altrimenti
    public boolean isNameClear(String name) {
        assert name != null : "il nome non deve essere null";

        return listaG.isNameClear(name); // Verifica se il nome è libero
    }

    //Metodo che restituisce il comprensorio dato il nome
    //Precondizione: 'nomeComp' deve essere valido
    //Postcondizione: restituisce il comprensorio corrispondente al nome
    public Comprensorio getCompbyName(String nomeComp) {   
        assert nomeComp != null : "il nome non deve essere null";
    	 
    	for (Comprensorio elem : listaC) {
            if(elem.getNome().equals(nomeComp))
                return elem;

        }		 
        return null;
		
	}

    // Metodo che verifica se una gerarchia è completa
    // Precondizione: 'radice' non deve essere null
    // Postcondizione: Restituisce true se tutti i figli sono definiti, false altrimenti
    public boolean controllaFigliDefiniti(Nonfoglia radice) {
        assert radice != null : "la radice non deve essere null";

        for (Categoria figlio : radice.getFigli()) {
            if (figlio == null) {
                return false; // Se un figlio è null, ritorna false
            }
            if (figlio instanceof Nonfoglia nonfoglia) {
                if (!controllaFigliDefiniti(nonfoglia)) {
                    return false; // Controllo ricorsivo sui figli non foglia
                }
            }
        }
        return true; // Restituisce true se tutti i figli sono definiti
    }

    // Restituisce una foglia dato l'id
    // Precondizione: 'id' deve essere valido
    // Postcondizione: Restituisce l'oggetto Foglia corrispondente all'id
    public Foglia getFoglia(int id) {
        assert id >= 0 : "L'ID deve essere positivo";

        return listaG.getFoglia(id); // Restituisce la foglia corrispondente all'id
    }

    // Verifica la disponibilità del nome di un comprensorio
    // Precondizione: 'nome' non deve essere null
    // Postcondizione: Restituisce true se il nome è disponibile, false altrimenti
    public boolean verificaDisp(String nome) {
        assert nome != null : "il nome non deve essere nullo";

        File file = new File(FILE_NAME);
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;

            // Legge ogni riga del file
            while ((line = reader.readLine()) != null) {
                // Se la riga corrisponde a questo comprensorio (in base al nome)
                if (line.startsWith("Nome: " + nome + ";")) 
                    return false; // Nome già esistente
            }
        } catch (IOException e) {
            System.err.println("Errore nella lettura del file"); // Errore nella lettura
        }
        return true; // Nome disponibile
    }

    // Inizializza la classe per la funzione di aggiungere un comprensorio
    // Precondizione: Nessuna
    // Postcondizione: Viene avviata l'app di aggiunta di un comprensorio
    public void aggiungiComprensorio() {
        new ComprensorioApp(this); // Crea un'istanza dell'app per aggiungere un comprensorio
    }

    // Inizializza la classe per la funzione di visualizzare un comprensorio
    // Precondizione: Nessuna
    // Postcondizione: Viene avviata l'app di visualizzazione di un comprensorio
    public void visualizzaComprensorio() {
        new VisComprensorioApp(this); // Crea un'istanza dell'app per visualizzare un comprensorio
    }

    // Inizializza la classe per la funzione di visualizzare le proposte
    // Precondizione: Nessuna
    // Postcondizione: Viene avviata l'app di visualizzazione delle proposte
    public void visualizzaProposteF(){
        new VisProposteF(this);
    }

    // Inizializza la classe per la funzione di visualizzare gli insiemi chiusi
    // Precondizione: Nessuna
    // Postcondizione: Viene avviata l'app di visualizzazione degli insiemi chiusi
    public void visualizzaInsieme(){
        new VisInsiemiChiusi(this);
    }

    // Inizializza la classe per la funzione di aggiungere una gerarchia
    // Precondizione: Nessuna
    // Postcondizione: Viene avviata l'app di creazione di una gerarchia
    public void creaGerarchia() {
        new CreaGerarchia(this); // Crea un'istanza dell'app per aggiungere una gerarchia
    }

    // Inizializza la classe per la funzione di visualizzare una gerarchia
    // Precondizione: Nessuna
    // Postcondizione: Viene avviata l'app di visualizzazione di una gerarchia
    public void visualizzaGerarchia() {
        new VisGerarchiaApp(this); // Crea un'istanza dell'app per visualizzare una gerarchia
    }

    // Inizializza la classe per la funzione di creare una nuova proposta
    // Precondizione: Nessuna
    // Postcondizione: Viene avviata l'app di creazione di una nuova proposta
    public void nuovaProposta(){
        new NewPropostaApp(this);
    }

    private Proposta propostaincorso;

    /**
     * Crea una nuova proposta basata sui parametri forniti.
     * 
     * Precondizioni:
     * - `richiesta` e `offerta` non devono essere null.
     * - `oreRichieste` deve essere un numero positivo.
     * - L'oggetto `fruit` deve essere inizializzato correttamente.
     * 
     * Postcondizioni:
     * - Una nuova proposta viene creata e aggiunta alla lista `listaP`.
     * - La proposta viene salvata persistentemente.
     * - Se `trovaScambi` restituisce true, la proposta è soddisfatta; altrimenti, non può essere soddisfatta al momento.
     */
    public void creaProposta(Foglia richiesta, int oreRichieste, Foglia offerta) {
        assert richiesta != null : "La richiesta non può essere null";
        assert offerta != null : "L'offerta non può essere null";
        assert oreRichieste >= 0 : "Le ore richieste devono essere positive";
        assert fruit != null : "L'oggetto fruit non può essere null";

        propostaincorso = new Proposta(richiesta, oreRichieste, fruit.getId());
        propostaincorso.soddisfaProposta(offerta);
        listaP.add(propostaincorso);
        System.err.println(propostaincorso.getId());
        propostaincorso.save();
        System.err.println(propostaincorso.toString());

        if (trovaScambi(propostaincorso)) {
            System.err.println("Proposta già soddisfatta");
        } else {
            System.err.println("Non è possibile soddisfare la proposta al momento");
        }
    }

    /**
     * Restituisce una lista di interi rappresentanti le ore convertite per un dato ID e un numero di ore specifico.
     * 
     * Precondizioni:
     * - `id` deve essere un numero positivo.
     * - `ore` deve essere un numero positivo.
     * 
     * Postcondizioni:
     * - Restituisce un ArrayList contenente le ore convertite.
     */
    public ArrayList<Integer> getOreConvertite(int id, int ore) {
        assert id >= 0 : "L'ID deve essere positivo";
        assert ore >= 0 : "Le ore devono essere positive";
        
        return listaF.getOreConvertite(id, ore);
    }

    /**
     * Restituisce il nome del comprensorio associato al fruitore specificato tramite il suo ID.
     * 
     * Precondizioni:
     * - `id` deve essere un numero positivo.
     * 
     * Postcondizioni:
     * - Restituisce una stringa che rappresenta il comprensorio, oppure `null` se non trovato.
     */
    private String getCompByFruitore(int id) {
        assert id >= 0 : "L'ID deve essere positivo";

        try (BufferedReader reader = new BufferedReader(new FileReader(FILE_F))) {
            String linea;
            int countId;
            
            if ((linea = reader.readLine()) == null) {
                return null;
            } else {
                do {
                    String[] credenziale = linea.split(";");
                    countId = Integer.parseInt(credenziale[0]);
                    if (countId == id) {
                        return credenziale[4];
                    }
                } while ((linea = reader.readLine()) != null);
            }
        } catch (Exception e) {
            System.err.println("Errore nella lettura da file");
        }
        return null;
    }

    /**
     * Trova scambi validi per una proposta specificata.
     * 
     * Precondizioni:
     * - `a` non deve essere null.
     * 
     * Postcondizioni:
     * - Restituisce `true` se è stato trovato uno scambio soddisfacente, `false` altrimenti.
     */
    public boolean trovaScambi(Proposta a) {
        assert a != null : "La proposta non può essere null";

        ArrayList<Proposta> cerchio = new ArrayList<>();
        ArrayList<Proposta> proposteNelComp = new ArrayList<>();
        String comp = getCompByFruitore(a.getIdFruitore());

        for (Proposta elem : listaP) {
            if (!elem.equals(a) && elem.getStato().equals("aperta") && getCompByFruitore(elem.getIdFruitore()).equals(comp)) {
                proposteNelComp.add(elem);
            }
        }
        if (proposteNelComp.size() < 1) return false;

        cerchio.add(a);
        if (cercaCerchio(proposteNelComp, cerchio)) {
            salvaInsieme(cerchio);
            return true;
        }
        return false;
    }

    /**
     * Cerca di formare un cerchio di scambi che soddisfi tutte le proposte.
     * 
     * Precondizioni:
     * - `lista` e `cerchio` non devono essere null.
     * 
     * Postcondizioni:
     * - Restituisce `true` se è stato trovato un cerchio valido, `false` altrimenti.
     */
    private boolean cercaCerchio(ArrayList<Proposta> lista, ArrayList<Proposta> cerchio){
        assert lista != null : "la lista non può essere nulla";
        assert cerchio != null : "la lista cerchio non deve essere nulla";

        int dimCerchio = cerchio.size();
        if(dimCerchio > 1)
            if(cerchio.get(0).getRichiesta().getNome().equals(cerchio.get(dimCerchio-1).getOfferta().getNome()) && cerchio.get(0).getOreRichieste() == cerchio.get(dimCerchio -1).getOreOfferte())
                return true;

        for(Proposta p : lista){
            if(cerchio.contains(p)) continue;
            System.err.println(cerchio.get(dimCerchio - 1).getOfferta().getNome() + " == " + p.getRichiesta().getNome() + " && " + cerchio.get(dimCerchio - 1).getOreOfferte()  + "=="  + p.getOreRichieste());
            if((cerchio.get(dimCerchio - 1).getOfferta().getNome() == null ? p.getRichiesta().getNome() == null : cerchio.get(dimCerchio - 1).getOfferta().getNome().equals(p.getRichiesta().getNome())) && cerchio.get(dimCerchio - 1).getOreOfferte() == p.getOreRichieste()){
                cerchio.add(p);
                if(cercaCerchio(lista, cerchio)) return true;
                cerchio.remove(dimCerchio - 1);
            }
        }
        return false;
    }

     /**
         * Precondizioni:
         * - `id` deve essere un numero intero positivo (> 0).
         * - `stato` deve essere una stringa non null e non vuota.
         * - `listaP` non deve essere null.
         * 
         * Postcondizioni:
         * - Se esiste una `Proposta` in `listaP` con `id` corrispondente, il suo stato viene aggiornato a "ritirata" 
         *   e le modifiche vengono salvate tramite `p.save()`.
         * - Se non esiste nessuna proposta con l'id fornito, il metodo non ha alcun effetto.
         */
    public void aggiornaStato(int id, String stato) {
        assert id >= 0 : "id deve essere un numero positivo";
        assert stato != null : "stato non deve essere null";
        assert !listaP.isEmpty() : "la lista delle proposte non deve essere null";

        for (Proposta p : listaP) {
            if (p.getId() == id) {
                p.setStato("ritirata");
                p.save();
            }
        }
    }
    
    /**
     * Precondizioni:
     * - `id` deve essere un numero intero positivo (> 0).
     * 
     * Postcondizioni:
     * - Restituisce il nome del fruitore se esiste un record corrispondente all'`id` fornito nel file `FILE_F`.
     * - Restituisce `null` se l'`id` non è trovato o se si verifica un errore durante la lettura del file.
     */
    public String getFruitoreName(int id) {
        assert id >= 0 : "l'id deve essere un numero positivo";
    	
        try (BufferedReader reader = new BufferedReader(new FileReader(FILE_F))) {
           
            String linea;
            int countId;
            if((linea = reader.readLine()) == null) {
                return null;
            }else {
                do{
                    String [] credenziale = linea.split(";");    			
                    countId = Integer.parseInt(credenziale[0]);
                    if(countId==id){
                        return credenziale[1];
                    }
                }while((linea = reader.readLine()) != null);
            }
        }catch (Exception e) {
            System.err.println("errore nella lettura da file");
        }
        return null;
    }

    /**
     * Precondizioni:
     * - `id` deve essere un numero intero positivo (> 0).
     * 
     * Postcondizioni:
     * - Restituisce l'indirizzo email del fruitore se esiste un record corrispondente all'`id` fornito nel file `FILE_F`.
     * - Restituisce `null` se l'`id` non è trovato o se si verifica un errore durante la lettura del file.
     */
    public String getFruitoreMail(int id) {
    	assert id >= 0 : "l'id deve essere un numero positivo";

        try (BufferedReader reader = new BufferedReader(new FileReader(FILE_F))) {
           
            String linea;
            int countId;
            if((linea = reader.readLine()) == null) {
                return null;
            }else {
                do{
                    String [] credenziale = linea.split(";");    			
                    countId = Integer.parseInt(credenziale[0]);
                    if(countId==id){
                        return credenziale[3];
                    }
                }while((linea = reader.readLine()) != null);
            }
        }catch (Exception e) {
            System.err.println("errore nella lettura da file");
        }
        return null;
    }


    /**
     * Precondizioni:
     * - `cerchio` non deve essere null.
     * - `cerchio` deve contenere almeno un elemento (cerchio.size() > 0).
     * - Ogni elemento di `cerchio` non deve essere null.
     * 
     * Postcondizioni:
     * - Lo stato di ogni `Proposta` in `cerchio` viene impostato su "chiusa" e salvato.
     * - Se il file `FILE_NAME_I` non esiste, viene creato.
     * - Il contenuto del `cerchio` viene aggiunto al file `FILE_NAME_I`, seguito da una riga "---".
     * - In caso di errori durante la creazione, lettura o scrittura del file, viene stampato un messaggio di errore.
     */
    public void salvaInsieme(ArrayList<Proposta> cerchio){
        assert !cerchio.isEmpty() : "cerchio vuoto non produce alcun effetto";

        for (Proposta elem : cerchio) {
            elem.setStato("chiusa");
            elem.save();
        }

        // Verifica se il file esiste e lo crea se necessario
        File file = new File(FILE_NAME_I);
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

            // Legge ogni riga del file
            while ((line = reader.readLine()) != null) 
                fileContent.add(line); // Mantiene le altre righe invariate

        } catch (IOException e) {
            System.err.println("errore nella lettura del file");
        }

        for (Proposta elem : cerchio)
                fileContent.add(elem.toString());

        fileContent.add("---");

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

    /**
     * Precondizioni:
     * - `FILE_NAME_I` deve essere un percorso valido leggibile.
     * 
     * Postcondizioni:
     * - Restituisce un `ArrayList` contenente i gruppi di stringhe letti dal file `FILE_NAME_I`.
     * - Ogni gruppo è delimitato da una linea che inizia con "-".
     * - Se il file è vuoto o si verifica un errore durante la lettura, restituisce un `ArrayList` vuoto.
     */
    public ArrayList<String> caricaInsiemiChiusi(){
        ArrayList<String> insiemi = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(FILE_NAME_I))) {
            String line;
            StringBuilder ins = new StringBuilder();
            while ((line = reader.readLine()) != null) {
                if(line.startsWith("-")){
                    insiemi.add(ins.toString());
                    ins.setLength(0);
                }   
                else{
                    ins.append(line).append("%");
                } 
            }
        } catch (IOException e) {
            System.err.println("Errore nella lettura da file"); // Stampa un errore in caso di problemi di lettura
        }

        return insiemi;
    }

    /**
     * Precondizioni:
     * - `id` deve essere un numero intero positivo (> 0).
     * - `FILE_P` deve essere un percorso valido leggibile.
     * 
     * Postcondizioni:
     * - Restituisce un `ArrayList` contenente tutte le proposte nel file `FILE_P` che corrispondono all'`id` dato.
     * - Se non ci sono proposte corrispondenti, restituisce un `ArrayList` vuoto.
     * - In caso di errore durante la lettura del file, restituisce un `ArrayList` vuoto e stampa un messaggio di errore.
     */
    public ArrayList<String> caricaProposteById(int id){
        assert id >= 0 : "id deve essere un numero positivo";

        ArrayList<String> lista = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(FILE_P))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] elems = line.split(";");
                int idRic = Integer.parseInt(elems[1].split(":")[1].trim());
                int idProp = Integer.parseInt(elems[3].split(":")[1].trim());
                if(id == idRic || id == idProp){
                    lista.add(line);
                }
            }
        } catch (IOException e) {
            System.err.println("Errore nella lettura da file"); // Stampa un errore in caso di problemi di lettura
        }

        return lista;
    }

}
