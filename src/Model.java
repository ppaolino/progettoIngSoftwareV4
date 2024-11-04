import java.io.*;
import java.util.ArrayList;
import java.util.List;

public final class Model {

    // Oggetto Configuratore
    private Configuratore conf;
    private Fruitore fruit;
    
    // Nomi dei file di configurazione e dei comprensori
    private static final String FILE_NAME_CONF = "Configuratori.txt";
    private static final String FILE_NAME = "comprensorio.txt";
    private static final String FILE_F = "Fruitori.txt";	//---------------------------------
    private static final String FILE_P = "proposte.txt";
    
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
            while ((line = reader.readLine()) != null) {
                // Suddivide la linea in parti
                String[] parti = line.split("; ");

                //id proposta
                String[] ind = parti[0].trim().split(":");
                int index = Integer.parseInt(ind[1].trim());

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
                

                listaP.add(new Proposta(index, richiesta, orer, offerta, oref, stato, index4));
            }
        } catch (IOException e) {
            System.err.println("Errore nella lettura da file"); // Stampa un errore in caso di problemi di lettura
        }


    }

    // Precondizione: 'nome' non deve già esistere nei comprensori
    // Postcondizione: Viene creato un nuovo comprensorio se il nome non è già usato
    public Comprensorio creaComprensorio(String nome) {
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
    // Postcondizione: Restituisce true se l'utente esiste, false altrimenti
    public boolean listaContains(String username, String password) {
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

    public boolean listaContainsF(String username, String password) {  
    	
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
    public boolean check(String c1, String c2, String username, String password) {
        return c1.equals(username) && c2.equals(password);
    }

    // Precondizione: 'username' e 'password' devono essere validi
    // Postcondizione: Salva le credenziali nel file di configurazione
    public void salvaConfig(String username, String password) {
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

    // Precondizione: 'username' non deve essere già in uso
    // Postcondizione: Restituisce true se il nome utente è già in uso
    public boolean inUso(String username) {  	//MDIFICATO IL METODO(stesso di LoginFruitore) ----------------------------
    	
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

    public boolean inUsoFruitore(String username) {  	
    	
        try (BufferedReader readerF = new BufferedReader(new FileReader(FILE_F));
             BufferedReader readerC = new BufferedReader(new FileReader(FILE_NAME_CONF))) {
            
            String linea;
            
            while ((linea = readerF.readLine()) != null) {
                String [] credenziale = linea.split(";");
                String name = credenziale[1];   				
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
        Nonfoglia padre = trovaNodo(listaG.getGerarchie(), id); // Trova il nodo padre
        if (padre != null) 
            padre.aggiungiFiglio(nf, dom); // Aggiunge il figlio se il padre esiste
    }

    // Metodo che fa l'aggiunta di un figlio di tipo foglia
    // Precondizione: 'nf' non deve essere null, 'id' deve essere valido
    // Postcondizione: Aggiunge 'nf' come figlio al nodo padre
    public void aggiungiFiglio(Foglia nf, int id, String dom) {
        Nonfoglia padre = trovaNodo(listaG.getGerarchie(), id); // Trova il nodo padre
        if (padre != null && "foglie".equals(padre.getTipoFigli()))  
            padre.aggiungiFiglio(nf, dom); // Aggiunge il figlio se il padre è di tipo foglia
    }

    // Metodo che verifica la disponibilità del nome di una radice
    // Precondizione: 'name' non deve essere null
    // Postcondizione: Restituisce true se il nome è disponibile, false altrimenti
    public boolean isNameClear(String name) {
        return listaG.isNameClear(name); // Verifica se il nome è libero
    }


    public Comprensorio getCompbyName(String nomeComp) {   	 
    	 
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
        if (radice == null) 
            return false; // Restituisce false se la radice è null

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
        return listaG.getFoglia(id); // Restituisce la foglia corrispondente all'id
    }

    // Verifica la disponibilità del nome di un comprensorio
    // Precondizione: 'nome' non deve essere null
    // Postcondizione: Restituisce true se il nome è disponibile, false altrimenti
    public boolean verificaDisp(String nome) {
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

    public void nuovaProposta(){
        new NewPropostaApp(this);
    }

    private Proposta propostaincorso;

    public void creaProposta(Foglia richiesta, int oreRichieste, Foglia offerta){
        propostaincorso = new Proposta(richiesta, oreRichieste, fruit.getId());
        propostaincorso.soddisfaProposta(offerta);
        listaP.add(propostaincorso);
        System.err.println(propostaincorso.getId());
        propostaincorso.save();
        System.err.println(propostaincorso.toString());
        if(trovaScambi(propostaincorso))
            System.err.println("proposta già soddisfatta");
        else
            System.err.println("non è possibile soddisfare la proposta al momento");
    }

    public ArrayList<Integer> getOreConvertite(int id, int ore){
        return listaF.getOreConvertite(id, ore);
    }

   

    private String getCompByFruitore(int id) {
    	
        try (BufferedReader reader = new BufferedReader(new FileReader(FILE_F))) {
           
            String linea;
            int countId = 0;
            if((linea = reader.readLine()) == null) {
                return null;
            }else {
                do{
                    String [] credenziale = linea.split(";");    			
                    countId = Integer.parseInt(credenziale[0]);
                    if(countId==id){
                        return credenziale[4];
                    }
                }while((linea = reader.readLine()) != null);
            }
        }catch (Exception e) {
            System.err.println("errore nella lettura da file");
        }
        return null;
    }

    public boolean trovaScambi(Proposta a){
        ArrayList<Proposta> cerchio = new ArrayList<>();
        ArrayList<Proposta> proposteNelComp = new ArrayList<>();
        String comp = getCompByFruitore(a.getIdFruitore());
        for (Proposta elem : listaP) {
            if(!elem.equals(a) && elem.getStato().equals("aperta") && getCompByFruitore(elem.getIdFruitore()).equals(comp))
                proposteNelComp.add(elem);
        }
        if(proposteNelComp.size() < 1) return false;
        cerchio.add(a);
        if(cercaCerchio(proposteNelComp, cerchio)){
            for (Proposta elem : cerchio) {
                elem.setStato("chiusa");
                elem.save();
            }
            return true;
        } 
        return false;
    }
    
    private boolean cercaCerchio(ArrayList<Proposta> lista, ArrayList<Proposta> cerchio){
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
}
