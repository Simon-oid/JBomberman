# TIMELINE:

### 17/5/23 (ragionando nel complesso)

>       ragiono come se sto aprendo il gioco per la prima volta, premo start
>       e il gioco carica il livello...

---

1. Carico il livello
    - [x] utilizzo il metodo load map per caricare la matrice che corrisponde alla mappa (loadmap deve avere come arg il
      path della mappa corrispettiva).
        * deve leggere un file cfg per ottenere la matrice.
    - [x] utilizzo loadPlayerPos; per ottenere la posizione iniziale del player.
        * deve leggere un file cfg per la posizione.
    - [x] utilizzo loadMobs; che avra come arg il tipo del mob e la posizione iniziale.
        * loadmobs deve leggere un file cfg con dentro: - lista mob, posizione mob, tipo mob.

***

      P.S. devo fixare la path ma per il resto sembra funzionare, alla fine ho optato per 
      un file .json che racchiude le variabili generiche di ciascuna entita, se si dovesse
      progettare un nuovo livello sarebbe necessario creare un nuovo .json con le variabili
      caretteristiche di mostri e giocatore.

***

2. Le entita' prendono le variabili direttamente dal file .json letto:
    - [x] aggiorna le variabili per le entita' in base a quello che e' stato letto dal file .json

***

===========================================================================
***

### 18/5/23 (fix generali)

1. Fix alla classe Entity e Player
    - [x] cambia i campi di Entity
    - [x] crea una classe MOB per i mobbini (utilizza un enum per i tipi di mob)
    - [x] crea la classe Player

2. Modifica i metodi che leggono il file .json
    -[x] implementa il parser
    -[x] itera sull'array e crea i mob mentre iteri sulla stringa

3. Modifica il metodo load level
    -[ ] fai in modo che i nomi dei file abbiano la stessa struttra se non per il numero del livello, facendo si che
     cambiando il numero all'interno del nome allora cambi anche il livello

***

===========================================================================
***

### 31/7/23 (ripresa del progetto)

oggi dopo aver ripreso il progetto dopo molto tempo ho implemetato il metodo che legge il parser e crea la lista delle
entita'

1. Creato il metodo jsonParser
    * creati i metodi allegati a jsonParser affinche funzioni

2. Revisione di loadPlayer e loadMob (non ho idea di cosa fare con questi due metodi)
    * Mentre scrivevo il punto spra mi e' venuta un idea, scorro la lista delle entita' e spawno sulla mappa il player e
      i mob
        -[x] implementare il metodo spawnEntities/spawnMob - spawnPlayer

3. Modifica il metodo load level
    -[ ] fai in modo che i nomi dei file abbiano la stessa struttra se non per il numero del livello, facendo si che
     cambiando il numero all'interno del nome allora cambi anche il livello

***

===========================================================================
***

### 1/8/23 (vari fix ai metodi)

1. Fix a tutti i metodi fino al load level
    -[x] fix a loadMap
    -[x] fix a readJson
    -[x] fix a jsonParser
    - [x] fix a spawnEntities
    -[x] fix a loadLevel

2. Modifica il metodo load level
    -[ ] fai in modo che i nomi dei file abbiano la stessa struttra se non per il numero del livello, facendo si che
     cambiando il numero all'interno del nome allora cambi anche il livello

***

===========================================================================
***
>     la seconda cosa che accade sul piano di gioco e' che le entita' 
>     iniziano a muoversi.

1. Le entita' si muovono...
    - [x] implementa il metodo move che sposta l'entita in modo randomico nella mappa.
        * il metodo deve presentare un override in player
    - [ ] overrida il metodo move() per player, il quale utilizzera' un metodo che aggiorna la sua direzione e posizione
      in base agli eventi gestiti da keyhandler().
        * l'evento corrisponde ad un tasto direzionale che viene premuto, ogni tasto direzionale aggiorna la posizione e
          la direzione del player

***

===========================================================================
***

### Riassunto da 2/8/2023 -> 6/8/2023

- implemetati diversi metodi per la gestione del movimento dei mob e player
- reworkato il modo i cui viene costruila la lista delle entita'
- fixato il keyHandler
- fixata la directory assoluta per i metodi che richiedono la lettura di un file
- resa la classe entity una classe astratta
- implementato il getter e setter in modo piu' elegante

### TO-DO:

- [ ] move di mob, deve prevedere la posizione successiva e fare il check in modo preventivo, in modo da non create
  stalli
- [ ] reworkare il random in modo che se una posizione presa in modo randomico, non si puo fare allora la "tolgo" dalla
  lista
- [ ] crea il metodo moveEntities(), che fa muovere tutte le entita'
- [ ] trova il modo per cambiare i tile sulla mappa quando si muovono le entita'
- [x] move() di Player puo' essere implementato in modo molto simile a quello di Mob, inplementalo in modo che poi possa
  essere richiamato dal kekyhandler a tempo debito
- [ ] rework generale del parser perche' e' disgustoso

***

===========================================================================
***

### 7/08/2023

- [x] implementazione del metodo move() per Player

***

===========================================================================
***

### 9/08/2023

### DONE

- [x] cambio al metodo move() dei mob in modo che il check venga fatto preventivamente, modo per non creare stalli
- [x] cambio alla selezione randomica di Mob in modo che quando una posizione non e' valida venga tolta dalle possibili
  opzioni
- [x] crea il metodo moveEntities(), che fa muovere tutte le entita'
- [x] trova il modo per cambiare i tile sulla mappa quando si muovono le entita'
- [x] rework generale del parser perche' e' disgustoso
- [x] fixato il parametro mancante di Player(score) che non era stato contemplato prima

***

### TO-DO:

- [x] cambio al metodo move() dei mob in modo che il check venga fatto preventivamente, modo per non creare stalli
- [x] cambio alla selezione randomica di Mob in modo che quando una posizione non e' valida venga tolta dalle possibili
  opzioni
- [x] crea il metodo moveEntities(), che fa muovere tutte le entita'
- [x] trova il modo per cambiare i tile sulla mappa quando si muovono le entita'
- [x] rework generale del parser perche' e' disgustoso
- [ ] implementare la logica per riconoscere quando una mossa e' valida o meno

***

===========================================================================
***

## Le entita' non si muovono sul void

      dopo aver progettato il modo in cui viene caricato il livello e gestita la mappa, 
      ho fatto muovere le entita', le entita' hanno ancora un metodo che deve essere progettato,
      quello del isValidMove() per il quale devo progettare il model per i Tiles e le loro
      caratteristiche.

### Tipi di Tile

- **Tile di erba**: tile base dove le entita' possono muoversi senza problemi
- **Tile inemovibile**: tile che viene spawnato in base alle specifiche del livello, non puo' essere attraversato ne
  distrutto
- **Tile distruttibile**: tile che non e' attraversabile ma puo' essere distrutto dal player attraverso l'esplosione
  delle bombe
- **Tile di Bordo**: delimita il limite della mappa, non sono ne attraversabili ne distruttibli e vengono spawnati
  durante il caricamento della mappa. Questi blocchi hanno la caratteristica di essere mezzi blocchi / blocchi interi /
  angoli
- **Tile exit**: questo Tile puo' essere generato randomicamente, alla distruzione di un blocco ma non puo' essere
  utilizzato se prima non si uccidono tutti i nemici presenti nel livello. Se viene colpito da una bomba spawna un
  IceScream (easter egg)

***

===========================================================================
***

### 10/08/2023

      classe generica Tile, possiede:
      Type,
      boolean collidable, (se e' un blocco con collisoine o meno)

### TO-DO:

- DestroyableTile:
    - [ ] logica che si prende cura della distruzione del blocco
    - [ ] Handle the spawned power-up (e.g., add it to the game world) (nel senso che viene cambiato il numero sulla
      matrice in modo che possa poi essere disegnato correttamente)

- PowerUpFactory:
    - [ ] controllare che la chance venga aggiornata correttamente

- AllPowerUps:
    - [ ] incorporare la logica dell'apply con quella delle collisioni (se player e powerup collidono allora faccio
      apply powerup)

- ExitTile:
    - [ ] creare il metodo che fa finire il livello
        - P.S la collisione viene gestita dal controller, quando il Player collide con il centro del Tile allora finisce
          il livello e parte un animazione apposita
    - [ ] creare il metodo che spawna il "powerup" gelato, che viene spawnato quando l'esplosione della bomba collide
      con il tile
        - aggiornare il metodo isValidTile() in modo tale da utilizzare la mappa updatata per controllare se un tile e'
          valido o meno
    - [ ] cambia il metodo is validTile() e utilizzalo all'interno di Map invece che ExitTile

***

DONE:

- POWERUPS:
    - creato BombUpPowerup
    - creato PowerUp
    - creato PowerUpFactory
    - creato PowerUpType
    - creato SkatePowerUp
- Tiles:
    - creato BorderTile
    - creato DestroyableTile
    - creato ExitTile
    - creato GrassTile
    - creato ImmovableTile
    - creato Tile
    - creato TileType
- modificato il parser che carica il livello in modo da caricare anche la probabilita' di spawn dei powerup
- cambia il metodo is validTile() e utilizzalo all'intero di map ivece di ExitTile
- Handle the spawned power-up (e.g., add it to the game world) (non c'e' bisogno di cambiare il numero della matrice,
  posso semplicemente disegnarlo sopra al blocco di erba)
- reworkato il metodo che trova il blocco adiacente valido in affinche' non sia rivoltante

***

===========================================================================
***

### 11 / 08 / 2023

### TO-DO:

- DestroyableTile:
    - [ ] logica che si prende cura della distruzione del blocco

- ExitTile:
    - [ ] creare il metodo che fa finire il livello
        - P.S la collisione viene gestita dal controller, quando il Player collide con il centro del Tile allora finisce
          il livello e parte un animazione apposita

- IceCreamPowerUp:
    - [ ] implementare il metodo che applica i powerup al player, fonte: (https://www.youtube.com/watch?v=NzE-DG2Ys6k)

- AllPowerUps:
    - [ ] incorporare la logica dell'apply con quella delle collisioni (se player e powerup collidono allora faccio
      apply powerup)

***

===========================================================================
***

### 12 / 08 / 2023

### TO-DO:

- DestroyableTile:
    - [ ] logica che si prende cura della distruzione del blocco

- ExitTile:
    - [ ] creare il metodo che fa finire il livello
        - P.S la collisione viene gestita dal controller, quando il Player collide con il centro del Tile allora finisce
          il livello e parte un animazione apposita

- IceCreamPowerUp:
    - [ ] implementare il metodo che applica i powerup al player, fonte: (https://www.youtube.com/watch?v=NzE-DG2Ys6k)

- AllPowerUps:
    - [ ] incorporare la logica dell'apply con quella delle collisioni (se player e powerup collidono allora faccio
      apply powerup)

### DONE:

- IceCreamPowerUp:
    - implementare il metodo che applica i powerup al player, fonte: (https://www.youtube.com/watch?v=NzE-DG2Ys6k)

***

===========================================================================
***

### 13 / 08 / 2023

### TO-DO:

- (possibile rework al metodo che fa muovere i mob)
    - nel gioco i mob non si mouvono in una direzione randomica quando incontrano un ostacolo, questo perche' il gioco
      sarebbe troppo difficile, si muovono infatti in un unica riga / colonna, fino a che non gli e' piu' possibile)
    - un altro modo in cui si puo' implementare il movimento e' progettare un metodo che detecta la colonna / riga piu'
      lunga disponibile e poi impostare quella come riga che devono seguire
    - i mob non sono consapevoli degli altri mob sulla mappa

###

- AllPowerUps:
    - [ ] incorporare la logica dell'apply con quella delle collisioni (se player e powerup collidono allora faccio
      apply powerup)

- isVulnerable:
    -  [ ] aggiungi ad Entita' il parametro isVulnerable e implementa il methodo invulnerable(), che rende il giocatore
       e il mob invulnerabile brevemente quando spawna

***

    implementa la meccanica della bomba

- BOMBA E BOMB BLAST:
    - [ ] implementa la meccanica della bomab ed esplosione nel gioco

### Richiedono implementazione Bomba:

- DestroyableTile:
    - [ ] logica che si prende cura della distruzione del blocco

- ExitTile:
    - [ ] creare il metodo che fa finire il livello
    - [ ] implementare la logica per cui se l'esplosione della bomba colpisce il tile allora spawna IceCream


- BOMBA E BOMB BLAST:
    - [ ] implementa la meccanica della bomab ed esplosione nel gioco

### DONE:

- chiedi a fede collision detection
- implementate collisioni per player
- implementate collisioni per mob
- implementate collisioni per ExitTile (metodo che finisce il livello)
- implementata applicazione dei powerup quando avviene la collisione con il player
- implementato il metodo decrease lives
- implementato il metodo getMobs in map
- implementata la logica che detecta se un mob collide con un player
- implementato il metodo takeDamage()
- implementato il metodo spawn()
- implementato isVulnerable

***

===========================================================================
***

### 14 / 08 / 2023

### TO-DO:

- (possibile rework al metodo che fa muovere i mob)
    - nel gioco i mob non si mouvono in una direzione randomica quando incontrano un ostacolo, questo perche' il gioco
      sarebbe troppo difficile, si muovono infatti in un unica riga / colonna, fino a che non gli e' piu' possibile)
    - un altro modo in cui si puo' implementare il movimento e' progettare un metodo che detecta la colonna / riga piu'
      lunga disponibile e poi impostare quella come riga che devono seguire
    - i mob non sono consapevoli degli altri mob sulla mappa

###

- [ ] cambiare collisioni
- [ ] cambiare movement
    - sia collisioni che movement devono essere controllati attraverso coordinate e non attraverso la posizione sulla
      matrice

###

- [ ] fare in modo che i powerup "spawinino" all'interno di Map
- [ ] eliminare PowerUpManager (i power up sono gestiti direttamente dentro map)

***

    implementa la meccanica della bomba

- BOMBA E BOMB BLAST:
    - [ ] implementa la meccanica della bomab ed esplosione nel gioco

### Richiedono implementazione Bomba:

- DestroyableTile:
    - [ ] logica che si prende cura della distruzione del blocco

- ExitTile:
    - [ ] creare il metodo che fa finire il livello
    - [ ] implementare la logica per cui se l'esplosione della bomba colpisce il tile allora spawna IceCream


- BOMBA E BOMB BLAST:
    - [ ] implementa la meccanica della bomab ed esplosione nel gioco

***

===========================================================================
***

### 16 / 08 / 2023

### TO-DO:

    focus sulla view
        
    per ora della view e' stato fatto solo il controllo delle scene in modo da poter accedere 
    alla generazione del livello per il testing FINIRE DOPO AVER FATTO IL RESTO

- [ ] finire la parte della view che riguarda i menu etc

***

- [ ] impostare il Tile Pane
    - il Tile Grid ha tanti tile quanti sono quelli all'interno della matrice
- [ ] displayare al mappa sul Tile Pane
    - il tile grid dovra' essere in grado di ridimensionarsi in base alle impostazioni del gioco

***

===========================================================================
***

### 17 / 08 / 2023

### DONE:

- creato il file FXML ChiLoSa a fine di testing
- creato il file FXML LevelSelection
- creato il file FXML Menu
- creato il file FXML Settings
- implementanti bottoni per LevelSelection
- implementati bottoni per Menu
- creato Observer per classe Map
- implementato metodo loadMap dentro Map, questo permette di caricare la mappa e manda i dati della mappa caricata a
  SceneController
- creato il PackageData packagedata per l'implementazione dell'observer
- creato l'ENUM PackageType per la selezione dei vari packagetype
- reworkato il metodo loadlever per farlo funzionare in accordanza con i metodi implementati
- creato il SceneController
- creato il SettingsController
- creata la GameView
- creato l'ENUM Entities
- creato l'ENUM Tiles
- creato l'ENUM Roots

***

    a fine giornata sono riuscito a creare una TileMap e a trasferire i dati necessario attraverso 
    l'observer in Map

***

===========================================================================
***

### 18 / 08 / 2023

### DONE:

- diplayata la mappa sul TilePane
- reworkato il metodo loadMap(), ora legge e crea la matrice correttamente
- reworkato il metodo update(), ora trasferisce un altro tipo di packageData
- reworkato il KeyHandler in modo che il movement del player possa essre gestito da un AnimationTimer
- reworkato il metodo move() di player in modo che possa essere gestito dal KeyHandler
- reworkato il metodo loadEntities() che prima era il JsonParser, in modo da usare un parser invece di crearne uno
- reworkato il metodo moveEntities() ora i mob si muovono con l'ausilio di un thread e il player attraverso un
  ActionTimer
- creato il metodo movePlayer() dentro map in modo da poter utilizzare un Observer per passare la posizioner del player
  attraverso Map alla GameView
- reworkato il metodo handle() dentro KeyHandler in modo da muovere il player 30 volte al secondo
- rework a movement del player
- create le animazioni del per il movement del Player
- reworkato il metodo exit(), ora funziona correttamente
- cambiata la dimensione della matrice in modo da poter lavorare separatamente la parte giocabile di terreno dalla
  cornice

***

===========================================================================
***

### 19 / 08 / 2023

### TO-DO:

- [ ] rework nel pacchetto listener, da creare un interfaccia
- [ ] creare packagedata che permette di muovere il player correttamente
- [ ] reworka collisioni
- [ ] reworka il metodo chooseRandomDirecton() di Mob
- [ ] reworka il metodo che aggiorna la mappa dopo la distruzione di un Tile Distruttibile
- [ ] reworka il metodo che fa finire il livello di ExitTile
- [ ] implementare isHitByExplosion dentro Tile()

### DONE:

- [X] rework nel pacchetto listener, da creare un interfaccia
- [X] creare packagedata che permette di muovere il player correttamente

***

===========================================================================
***

### 15 / 10 / 2023

### TO-DO:

- [ ] reworka collisioni
- [ ] reworka il metodo chooseRandomDirecton() di Mob
- [ ] reworka il metodo che aggiorna la mappa dopo la distruzione di un Tile Distruttibile
- [ ] reworka il metodo che fa finire il livello di ExitTile
- [ ] implementare isHitByExplosion dentro Tile()

***

===========================================================================
***

FF >>> 22/10/2023

### TO-DO:

- [ ] reworka collisioni
- [ ] reworka il metodo chooseRandomDirecton() di Mob
- [ ] reworka il metodo che aggiorna la mappa dopo la distruzione di un Tile Distruttibile
- [ ] reworka il metodo che fa finire il livello di ExitTile
- [ ] implementare isHitByExplosion dentro Tile()

### DONE:

- [x] metodo che aggiorna la mappa dopo la distruzione di un TileDistruttibile
- [x] implementare isHitByExplosion dentro Tile() (metodo inutile)

***

===========================================================================
***

### 22/10/2023

### TO-DO:

### low priority:

- [ ] reworka collisioni
- [ ] reworka il metodo chooseRandomDirecton() di Mob
- [ ] reworka il metodo che fa finire il livello di ExitTile

### high priority:

- [ ] fixa il bug per cui il range del player non e' una croce, bensi' un cerchio (non so come sia successo)

***

===========================================================================
***

### 24/10/2023

### TO-DO:

### low priority:

- [ ] reworka collisioni
- [ ] reworka il metodo chooseRandomDirecton() di Mob
- [ ] reworka il metodo che fa finire il livello di ExitTile

### high priority:

- [ ] fixare il fatto che se la bomba trova nella stessa croce sia un blocco distruttibile che un blocco non
  distruttibile il codice si ferma completamente non distruggendo il blocco distruttibile in questione

### IDK:

- [ ] rework a come la bomba interagisce come le cose, check vs hitbox

### DONE:

- [x] fixa il bug per cui il range del player non e' una croce, bensi' un cerchio (non so come sia successo)
- [x] creata la logica che gestisce l'esplosione quando incontra un blocco collidable (FIXME)

***

===========================================================================
***

### 28/10/2023

### TO-DO:

### high priority:

- [ ] fixare il fatto che se due tiles distruttibli sono presenti nel range e nella stessa linea vengono distrutti
  entrambi
- [ ] cambiare il metodo con cui sono definiti i range e passati tramite il package: ogni direzione ha un range che
  viene salvato all'interno di un arraylist dentro explodeAdjacentTiles, i range hanno come value massima il range del
  player e come minima 0, i range aumentano fino a che non incontrano un tile collidabile oppure raggiungono la value
  massima
    - [ ] fare prima un for sulle direzioni e poi sul range ( nel senso che ogni check necessario viene effettuato nel
      seguente modo:
    - right(distruggo tutti i blocchi distruttibli e trovo il range)
    - -> left(distruggo tutti i blocchi distruttibli e trovo il range)
    - -> up(distruggo tutti i blocchi distruttibli e trovo il range)
    - -> down(distruggo tutti i blocchi distruttibli e trovo il range))

### low priority:

- [ ] reworka collisioni
- [ ] reworka il metodo chooseRandomDirecton() di Mob
- [ ] reworka il metodo che fa finire il livello di ExitTile

### DONE:

- [x] fixare il fatto che se la bomba trova nella stessa croce sia un blocco distruttibile che un blocco non
  distruttibile il codice si ferma completamente non distruggendo il blocco distruttibile in questione
- [x] fixato il problema per cui la bomba non distruggeva tutti i blocchi distruttibli presenti nelle 4 direzioni

***

===========================================================================
***



