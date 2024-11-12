# Manuale utente

## Introduzione

Benvenuto nel manuale di installazione del progetto di ingegneria del software di Paolo Iore, Luca Ravelli e Giovanni Mapelli. Questo documento ti guiderà attraverso il processo di installazione, configurazione ed esecuzione del nostro programma scritto in Java. Assicurati di seguire attentamente ogni passo per evitare problemi.
## Requisiti di Sistema
### Hardware
•	CPU: Dual-core 2.0 GHz o superiore
•	RAM: Minimo 4 GB (consigliati 8 GB)
### Software
•	Java Development Kit (JDK): Versione 17 o superiore
•	Git (opzionale): Per clonare il repository da GitHub
## Download
•	Scarica l'ultima versione dell’applicazione dal repository ufficiale GitHub.
•	Salva il file progettoIngSoftwareV4-main.zip in una cartella sul tuo sistema (es. C:\Downloads\progettoIngSoftwareV4).
## Installazione del programma
Estrazione dei File
•	Naviga alla cartella in cui hai scaricato il file .zip.
•	Estrai il contenuto utilizzando un tool come WinRAR, 7-Zip 
## Esecuzione del Programma
•	Da Linea di Comando
o	Apri il terminale (Prompt dei comandi su Windows, Terminal su macOS/Linux).
o	Naviga nella cartella del programma:
	Esempio: cd /path/to/progettoIngSoftwareV4
o	Esegui il programma:
	java -jar progettoIngSoftwareV4.jar
•	Da IDE (Visual Studio Code, Eclipse)
o	Apri il progetto nell'IDE.
o	Esegui il programma utilizzando il pulsante Run.
## Disinstallazione
Elimina la cartella di installazione di progettoIngSoftwareV4.
Supporto Tecnico
Per ulteriori informazioni o supporto tecnico, contatta:

Email: l.ravelli002@studenti.unibs.it 
Email: p.iore@studenti.unibs.it
Email: g.mapelli003@studenti.unibs.it

# Manuale d’uso – Configuratore
## Accesso

Il configuratore per poter accedere all’applicazione dovrà come prima cosa selezionare la modalità configuratore come mostrato nella Figura 1. Successivamente si aprirà una seconda schermata (Figura 2) dove il configuratore potrà scegliere se accedere o registrarsi.

Per l’accesso il configuratore dovrà inserire le proprie credenziali per poi cliccare sul pulsante Login. Mentre per potersi registrare dovrà conoscere le credenziali predefinite (nel nostro caso per questioni di semplicità saranno username = a e password = p) e successivamente dovrà creare le proprie credenziali per gli accessi futuri (username deve essere unico, non ci possono essere duplicati).

Dopo aver effettuato l’accesso, il configuratore si troverà davanti alla schermata principale dell’applicazione (Figura 3). Da cui potrà selezionare la funzionalità desiderata.

## Visualizza una gerarchia e proposte create


Selezionando la funzionalità “Visualizza una gerarchia” il configuratore potrà navigare tra le gerarchie presenti nell’applicazione (Figura 4), visualizzare i fattori di conversione delle varie attività (per poterli vedere deve posizionare il mouse sulla foglia desiderata) o le informazioni relative alle proposte corrispondenti alla foglia desiderata (per poter aprire la visualizzazione il configuratore dovrà cliccarci sopra).
## Visualizzazione di un comprensorio geografico
Selezionando la funzionalità “Visualizza un comprensorio geografico” il configuratore sarà in grado di poter navigare tra i comprensori geografici creati e selezionandoli potrà vedere i dettagli relativi ad essi (i comuni che lo compongono).
## Crea una nuova gerarchia
Selezionando la funzionalità “Crea una nuova gerarchia”, il configuratore sarà in grado di creare una nuova gerarchia, inserendo per prima cosa il nome (che deve essere unico, quindi non un duplicato), dopodiché può procedere inserendo tutte le informazioni relative alla gerarchia che intende creare, inserendo poi per ogni foglia il fattore di conversione relativo ad un’altra foglia già presente all’interno dell’insieme delle gerarchie.

Nota:
Per procedere al salvataggio della gerarchia devono essere presenti tutte le informazioni della gerarchia (in questa versione dell’applicazione le gerarchie non sono modificabili post inserimento)


Nota:
le categorie non complete sono evidenziate con un diverso colore, per permettere al configuratore di capire quali dati mancanti deve inserire per concludere la creazione.
## Crea un nuovo comprensorio geografico
Selezionando la funzionalità “Crea un nuovo comprensorio geografico” il configuratore sarà in grado di poter creare un nuovo comprensorio geografico, inserendo il nome univoco di quest’ultimo e dopo aver confermato, potrà scegliere quali comuni ne fanno parte (i comuni devono essere limitrofi altrimenti il comprensorio non verrà creato)

Nota:
i comuni sono stati presi dal sito ufficiale https://www.comuni-italiani.it/ , tramite uno script python per poterli lavorare in formato json e renderli disponibili all’uso.

## Visualizza gli insiemi chiusi
Selezionando la funzionalità “Visualizza gli insiemi chiusi” il configuratore sarà in grado di visualizzare tutti gli estremi delle proposte appartenenti a tutti gli insiemi chiusi, con le informazioni relative ai fruitori associati. 
 
# Manuale d’uso – fruitore
## Accesso


Il fruitore per poter accedere all’applicazione dovrà come prima cosa selezionare la modalità fruitore come mostrato nella Figura 5. Successivamente si aprirà una seconda schermata (Figura 6) dove il fruitore potrà scegliere se accedere o registrarsi.

Per l’accesso il fruitore dovrà inserire le proprie credenziali per poi cliccare sul pulsante Login. Mentre per potersi registrare dovrà cliccare sul pulsante nuovo fruitore. Dopo aver cliccato, dovrà selezionare il comprensorio geografico a cui appartiene, tra quelli mostrati. Selezionato il comprensorio di appartenenza, il fruitore dovrà poi inserire il proprio username (unico, non possono esistere duplicati), la password e il proprio indirizzo di posta elettronica.

Dopo aver effettuato l’accesso, il configuratore si troverà davanti alla schermata principale dell’applicazione (Figura 7). Da cui potrà selezionare la funzionalità desiderata.

## Visualizza una gerarchia
Selezionando la funzionalità “Visualizza una gerarchia” il fruitore potrà navigare tra le gerarchie presenti nell’applicazione e visualizzare i fattori di conversione delle varie attività (per poterli vedere deve posizionare il mouse sulla foglia desiderata).

## Crea una proposta
Selezionando la funzionalità “Crea proposta” il fruitore sarà in grado di creare una proposta desiderata. Per fare ciò dovrà, navigando tra le gerarchie, selezionare la categoria foglia richiesta e inserire il numero di ore richieste. In seguito, l’applicazione chiederà di selezionare la categoria foglia offerta per soddisfare la richiesta (l’applicazione mostra le ore necessarie per completare lo scambio in base ai fattori di conversione inseriti dal configuratore, come mostrato in figura 8).

Nota:
in ogni momento se il fruitore non è soddisfatto dei termini di scambio può abortire la creazione.



## Visualizzazione e ritiro proposte
Scegliendo la funzionalità “Visualizza proposte”, il fruitore sarà in grado di visualizzare tutte le proposte di cui è autore e, nel caso la proposta sia aperta quindi non ancora soddisfatta, esso avrà la possibilità di ritirarla cliccando sul pulsante “Ritira la proposta”.
