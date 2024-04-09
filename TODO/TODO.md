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

### 1/12/2023

### TO-DO:

### high priority:

- [ ] fixare il fatto che quando la bomba viene piazzata, non e' piu' possibile piazzare una bomba in quel tile
- [ ] fai spawnare e muovere i mob
- [ ] fixa il fatto che lo sprite del player e' disegnato sotto quello della bomba

### low priority:

- [ ] reworka collisioni
- [ ] reworka il metodo chooseRandomDirecton() di Mob
- [ ] reworka il metodo che fa finire il livello di ExitTile

### DONE:

- [x] fixato l'handling della bomba
- [x] fixato il modo in cui il package data viene passato alla gameview
- [x] reworkato il modo con cui viene disegnata l'esplosione
    - ora vengono diseganti tanti sprite quanti sono i blocchi attraversati dall'esplosione prima di fermarsi - viene
      disegnato sempre uno sprite nella posizione 0

***

===========================================================================
***

### 4/12/2023

### TO-DO:

### high priority:

- [ ] fai spawnare e muovere i mob

### low priority:

- [ ] reworka collisioni
- [ ] reworka il metodo chooseRandomDirecton() di Mob
- [ ] reworka il metodo che fa finire il livello di ExitTile
- [ ] fixa il fatto che lo sprite del player e' disegnato sotto quello della bomba

### DONE:

- [x] fixata la presenza della bomba sulla mappa
    - ora viene rimossa dopo l'esplosione

***

===========================================================================
***

### 4/12/2023 --> 13/02/2024

### TO-DO:

### high priority:

- [ ] fai collidere il player con l'esplosione della bomba
- [ ] fai collidere il player con i mob

### low priority:

- [ ] reworka collisioni del player

### DONE:

- [x] mob spawns
- [x] mob movement
- [x] mob collisions
- [x] mob collisions with bomb / explosions
- [x] reworkato il metodo chooseRandomDirection()
- [x] accorgimenti fatti alla gameview per la gestione dell'uccisione del mob
- [x] creato record per trasmettere i dati dell'uccisione di un mob

***

===========================================================================
***

### 13/02/2024

### TO-DO:

### high priority:

- [ ] fai collidere il player con i mob

### low priority:

- [ ] reworka collisioni del player

### DONE:

- [x] fai collidere il player con l'esplosione della bomba
- [x] implementato il sistema di invincibilita' dopo aver preso danno
- [x] sistema per la riduzione delle vite
- [x] sistema per gestire la morte del player

***

===========================================================================
***

### 14/02/2024

### TO-DO:

### high priority:

- [ ] creare il bordo collidabile del livello

### low priority:

- [ ] reworka collisioni del player

### DONE:

- [x] fai collidere il player con i mob
- [x] fixa il "last frame" nel keyhandler
- [x] fixare lo spawn del player, prima del muoversi il player spawna nella posizione sbagliata (ancora da
  monitorare, implementato il sistema per lo spawn, ma non funziona perche' javaFX non ti fa disegnare cose fuori
  dalla finestra a start)

***

===========================================================================
***

### 20/02/2024

### TO-DO:

### high priority:

- [ ] implementare un HUD
- [ ] implementare il drop dei powerup
- [ ] implementare il sistema di score

### low priority:

- [ ] reworka collisioni del player (si incolla alle pareti)
- [ ] fixa le hitbox dei mob, siccome ora sono spostate in alto a sinistra rispetto al loro sprite
- [ ] fixare lo spawn del player

### DONE:

- [x] creare il bordo collidabile del livello

***

===========================================================================
***

### 21/02/2024

### TO-DO:

### high priority:

- [ ] implementare la collisione tra il player e i powerup
- [ ] implementare un HUD
- [ ] implementare il sistema di score

### low priority:

- [ ] implementare timer per il despawn dei powerup
- [ ] reworka collisioni del player (si incolla alle pareti)
- [ ] fixa le hitbox dei mob, siccome ora sono spostate in alto a sinistra rispetto al loro sprite

### DONE:

- [x] fixare lo spawn del player (se si cambiano le dimensioni della finestra andranno cambiati di nuovo gli
  aggiustamenti fatti per centrare il player nel primo tile)
    - aggiustamenti fatti nelle posx posy nel parser di map e nella gameview
- [x] implementare il drop dei powerup

***

===========================================================================
***

### 22/02/2024

### TO-DO:

### high priority:

- [ ] implementare un HUD
- [ ] implementare il sistema di score

### low priority:

- [ ] reworka collisioni del player (si incolla alle pareti)
- [ ] fixa le hitbox dei mob, siccome ora sono spostate in alto a sinistra rispetto al loro sprite

### DONE:

- [x] implementare la collisione tra il player e i powerup
- [x] implementata l'applicazione del powerup al player
- [x] implementata la rimozione del powerup dalla mappa una volata che il player ha colliso con esso
- [x] implementare timer per il despawn dei powerup

***

===========================================================================
***

### 23/02/2024

### TO-DO:

### high priority:

- [ ] implementare il sistema che rende reattiva l'hud
- [ ] importare numeri con il font specifico
- [ ] implementare il sistema di score
- [ ] reworka collisioni del player (si incolla alle pareti)

### low priority:

- [ ] i mob e le bombe dovrebbero essere elementi nella anchor pane, ora come ora sono solo disegnati nel punto giusto
- [ ] fixa le hitbox dei mob, siccome ora sono spostate in alto a sinistra rispetto al loro sprite
- [ ] implementa l'exit tile

### DONE:

- [x] applicato un offset di 256 pixels a tutti gli elementi all'interno della gameview
- [x] setuppato il sistema per disegnare un HUD

***

===========================================================================
***

### 24/02/2024

### TO-DO:

### high priority:

- [ ] implementare di quanto e quando lo score varia durante la partita (dove piazzare gli incrementi di score e
  update, per ora fatto solo con l'uccisione di un mob)
- [ ] reworka collisioni del player (si incolla alle pareti)
- REQUISITI MANCANTI:
- [ ] animazioni
- [ ] suono
- [ ] player selection

### low priority:

- [ ] il player e la bomba non collidono tra loro
- [ ] i mob e le bombe dovrebbero essere elementi nella anchor pane, ora come ora sono solo disegnati nel punto giusto
- [ ] fixa le hitbox dei mob, siccome ora sono spostate in alto a sinistra rispetto al loro sprite
- [ ] implementa l'exit tile

### DONE:

- [x] implementare il sistema di score
- [x] implementare il sistema che rende reattiva l'hud
- [x] importare numeri con il font specifico

***

===========================================================================
***

### 25/02/2024

### TO-DO:

### high priority:

- [ ] il player e la bomba non collidono tra loro
- [ ] tutti gli sprite disegnati sulla mappa dovrebbero essere disegnati sotto il player
- [ ] i mob devono poter collidere tra loro

---

- [ ] feedback sull'uccisione del mob (animazione con punti che vanno verso l'alto)
- [ ] implementare la fine del livello quando player collide con exit tile
- [ ] reworka collisioni del player (si incolla alle pareti)

---

- REQUISITI MANCANTI:
- [ ] animazioni
- [ ] suono
- [ ] player selection

### low priority:

- [ ] i mob e le bombe dovrebbero essere elementi nella anchor pane, ora come ora sono solo disegnati nel punto giusto

### DONE:

- [x] implementare di quanto e quando lo score varia durante la partita (dove piazzare gli incrementi di score e
  update, per ora fatto solo con l'uccisione di un mob)
- [x] implementa il tile exit
- [x] fixa le hitbox dei mob, siccome ora sono spostate in alto a sinistra rispetto al loro sprite (le hitbox dei
  mob possono essere 48x48)

***

===========================================================================
***

### 26/02/2024

### TO-DO:

### high priority:

- [ ] implementare il trigger del game over quando il player muore
- [ ] implementare la fine del livello quando player collide con exit tile

---

- [ ] animazione dei punti che da uccidere un mob
- [ ] reworka collisioni del player (si incolla alle pareti)

---

- REQUISITI MANCANTI:
- [ ] animazioni
- [ ] suono
- [ ] player selection

### low priority:

- [ ] i mob e le bombe dovrebbero essere elementi nella anchor pane, ora come ora sono solo disegnati nel punto giusto
- [ ] la "priorita'" di come sono disegnati gli sprite non e' fatta in modo intelligente, ogni volta che un nuovo
  sprite viene disegnato, il player viene rimosso e ridisegnato (potrebbe non funzionare quando le animazioni sono
  implementate)

### DONE:

- [x] il player e la bomba non collidono tra loro
- [x] tutti gli sprite disegnati sulla mappa dovrebbero essere disegnati sotto il player
- [x] i mob devono poter collidere tra loro
- [x] feedback sull'uccisione del mob

***

===========================================================================
***

### 27/02/2024

### TO-DO:

### high priority: [working on animations rn]

- [ ] reworka collisioni del player (si incolla alle pareti)

---

- [ ] animazione del movimento del player (abbozzato un mezzo sistema per l'animazione ma non funziona bene)
- [ ] animazione dei Tile
- [ ] animazione del movimento dei mob
- [ ] animazione della bomba
- [ ] animazione dell'esplosione
- [ ] animazione della distruzione dei un Tile
- [ ] animazione dell'uccisione di un mob
- [ ] animazione dei powerup
- [ ] animazione del TileExit
- [ ] animazione dei punti che da uccidere un mob
- [ ] animazione dell'hud
- [ ] animazione della morte del player
- [ ] animazione dello switch del livello

---

- REQUISITI MANCANTI:
- [ ] animazioni
- [ ] suono
- [ ] player selection

### low priority:

- [ ] i mob e le bombe dovrebbero essere elementi nella anchor pane, ora come ora sono solo disegnati nel punto giusto
- [ ] la "priorita'" di come sono disegnati gli sprite non e' fatta in modo intelligente, ogni volta che un nuovo
  sprite viene disegnato, il player viene rimosso e ridisegnato (potrebbe non funzionare quando le animazioni sono
  implementate)

### DONE:

- [x] implementare la fine del livello quando player collide con exit tile (implementata la meccanica di switching
  tra livelli)
- [x] implementare il trigger del game over quando il player muore

***

===========================================================================
***

### 28/02/2024

### TO-DO:

### high priority: [working on animations rn]

- [ ] reworka collisioni del player (si incolla alle pareti)

---

- [ ] animazione dei Tile
- [ ] animazione del movimento dei mob
- [ ] animazione della bomba
- [ ] animazione dell'esplosione
- [ ] animazione della distruzione dei un Tile
- [ ] animazione dell'uccisione di un mob
- [ ] animazione dei powerup
- [ ] animazione del TileExit
- [ ] animazione dei punti che da uccidere un mob
- [ ] animazione dell'hud
- [ ] animazione della morte del player
- [ ] animazione dello switch del livello
- [ ] trovare un modo per sistemare il sistema di flag nell'animazione del player

---

- REQUISITI MANCANTI:
- [ ] animazioni
- [ ] suono
- [ ] player selection

### low priority:

- [ ] i mob e le bombe dovrebbero essere elementi nella anchor pane, ora come ora sono solo disegnati nel punto giusto
- [ ] la "priorita'" di come sono disegnati gli sprite non e' fatta in modo intelligente, ogni volta che un nuovo
  sprite viene disegnato, il player viene rimosso e ridisegnato (potrebbe non funzionare quando le animazioni sono
  implementate)

### DONE:

- [x] animazione del movimento del player (abbozzato un mezzo sistema per l'animazione ma non funziona bene)

***

===========================================================================
***

### 29/02/2024

### TO-DO:

### high priority: [working on animations rn]

- [ ] reworka collisioni del player (si incolla alle pareti)

---

- [ ] animazione del movimento dei mob
- [ ] animazione della bomba
- [ ] animazione dell'esplosione
- [ ] animazione della distruzione dei un Tile
- [ ] animazione dell'uccisione di un mob
- [ ] animazione dei powerup
- [ ] animazione del TileExit
- [ ] animazione dei punti che da uccidere un mob
- [ ] animazione dell'hud
- [ ] animazione della morte del player
- [ ] animazione dello switch del livello
- [ ] trovare un modo per sistemare il sistema di flag nell'animazione del player

---

- REQUISITI MANCANTI:
- [ ] animazioni
- [ ] suono
- [ ] player selection

### low priority:

- [ ] i mob e le bombe dovrebbero essere elementi nella anchor pane, ora come ora sono solo disegnati nel punto giusto
- [ ] la "priorita'" di come sono disegnati gli sprite non e' fatta in modo intelligente, ogni volta che un nuovo
  sprite viene disegnato, il player viene rimosso e ridisegnato (potrebbe non funzionare quando le animazioni sono
  implementate)

### DONE:

- [x] animazione dei Tile
- [x] migliorati leggermente gli sprite dei bordi ( mancano ancora gli angoli e la parte sotto fatta bene)
- [x] aggiustata grandezza dei mob

***

===========================================================================
***

### 1/03/2024

### TO-DO:

### high priority: [working on animations rn]

- [ ] reworka collisioni del player (si incolla alle pareti)

---

- [ ] animazione della distruzione dei un Tile
- [ ] animazione dell'uccisione di un mob
- [ ] animazione dei powerup
- [ ] animazione del TileExit
- [ ] animazione dei punti che da uccidere un mob
- [ ] animazione dell'hud
- [ ] animazione della morte del player
- [ ] animazione dello switch del livello
- [ ] animazione del movimento dei mob
- [ ] trovare un modo per sistemare il sistema di flag nell'animazione del player

---

- REQUISITI MANCANTI:
- [ ] animazioni
- [ ] suono
- [ ] player selection

### low priority:

- [ ] i mob e le bombe dovrebbero essere elementi nella anchor pane, ora come ora sono solo disegnati nel punto giusto
- [ ] la "priorita'" di come sono disegnati gli sprite non e' fatta in modo intelligente, ogni volta che un nuovo
  sprite viene disegnato, il player viene rimosso e ridisegnato (potrebbe non funzionare quando le animazioni sono
  implementate)

### DONE:

- [x] animazione della bomba
- [x] animazione dell'esplosione
- [x] fixato l'update dei tile ombreggiati sotto i tile distruttibili, se ora c'e' un tile ombreggiato sotto un tile
  distruttibile, il tile ombreggiato divente un tile di erba

***

===========================================================================
***

### 2/03/2024

### TO-DO:

### high priority: [working on animations rn]

- [ ] reworka collisioni del player (si incolla alle pareti)

---

- [ ] animazione del TileExit
- [ ] animazione dei punti che da uccidere un mob
- [ ] animazione dell'hud
- [ ] animazione della morte del player
- [ ] animazione dello switch del livello
- [ ] animazione del movimento dei mob
- [ ] animazione dell'uccisione di un mob
- [ ] trovare un modo per sistemare il sistema di flag nell'animazione del player

---

- REQUISITI MANCANTI:
- [ ] animazioni
- [ ] suono
- [ ] player selection

### low priority:

- [ ] i mob e le bombe dovrebbero essere elementi nella anchor pane, ora come ora sono solo disegnati nel punto giusto
- [ ] la "priorita'" di come sono disegnati gli sprite non e' fatta in modo intelligente, ogni volta che un nuovo
  sprite viene disegnato, il player viene rimosso e ridisegnato (potrebbe non funzionare quando le animazioni sono
  implementate)

### DONE:

- [x] animazione della distruzione dei un Tile
- [x] fixato timing dello spawn di un powerup per fittare l'animatione della distruzione di un tile
- [x] fixato timing dello spawn del tile exit per fittare l'animatione della distruzione di un tile
- [x] animazione dei powerup

***

===========================================================================
***

### 3/03/2024

### TO-DO:

### high priority: [working on animations rn]

- [ ] reworka collisioni del player (si incolla alle pareti)
- [ ] implementa il supporto per piu' mob dello stesso tipo [provare hasmap di liste per type]
- [ ] il denkyun dovrebbe poter tankare 2 hit
- [ ] il denkyun dovrebbe avere una possibilita' di respawnare se il tile exit e' sul campo
- [ ] le hitbox dei mob non possono essere la stessa grandezza di un tile, per via dei gametick irregolari, a volte
  la collisione viene detectata pochi pixel prima di quello che e' il bordo del tile / entita' [provare con hitbox
  piu' piccole e con detection delle collisioni con un offset es. hitbox del mob 45x45 ma la collisione avviene
  sempre con un offset di 3 pixels]

---

- [ ] animazione dei punti che da uccidere un mob
- [ ] animazione dell'hud
- [ ] animazione della morte del player
- [ ] animazione dello switch del livello
- [ ] animazione dell'uccisione di un mob

---

- REQUISITI MANCANTI:
- [ ] animazioni
- [ ] suono
- [ ] player selection

### low priority:

### DONE:

- [x] animazione del TileExit
- [x] trovare un modo per sistemare il sistema di flag nell'animazione del player
- [x] animazione del movimento dei mob
- [x] importare sprites di Denkyun
- [x] i mob e le bombe dovrebbero essere elementi nella anchor pane, ora come ora sono solo disegnati nel punto giusto
- [x] la "priorita'" di come sono disegnati gli sprite non e' fatta in modo intelligente, ogni volta che un nuovo
  sprite viene disegnato, il player viene rimosso e ridisegnato (potrebbe non funzionare quando le animazioni sono
  implementate)

***

===========================================================================
***

### 4/03/2024

### TO-DO:

### high priority: [working on animations rn]

- [ ] creata l'animazione di quando il player viene colpito, bisogna modificare il model cosi' che quando il player
  viene colpito, il keyhandler si ferma e riprende quando l'animazione e' finita e il player e' respawnato alle
  coordinate iniziali
- [ ] implementa il supporto per piu' mob dello stesso tipo [provare hasmap di liste per type]
- [ ] il denkyun dovrebbe poter tankare 2 hit
- [ ] il denkyun dovrebbe avere una possibilita' di respawnare se il tile exit e' sul campo
- [ ] le hitbox dei mob non possono essere la stessa grandezza di un tile, per via dei gametick irregolari, a volte
  la collisione viene detectata pochi pixel prima di quello che e' il bordo del tile / entita' [provare con hitbox
  piu' piccole e con detection delle collisioni con un offset es. hitbox del mob 45x45 ma la collisione avviene
  sempre con un offset di 3 pixels]

---

- [ ] animazione dello switch del livello
- [ ] animazione dell'uccisione di un mob

---

- REQUISITI MANCANTI:
- [ ] animazioni
- [ ] suono
- [ ] player selection

### low priority:

- [ ] reworka collisioni del player (si incolla alle pareti)

### DONE:

- [x] animazione dell'hud
- [x] animazione dei punti che da uccidere un mob
- [x] animazione della morte del player

***

===========================================================================
***

### 5/03/2024

### TO-DO:

### high priority: [working on animations rn]

- [ ] il denkyun dovrebbe poter tankare 2 hit
- [ ] il denkyun dovrebbe avere una possibilita' di respawnare se il tile exit e' sul campo
- [ ] le hitbox dei mob non possono essere la stessa grandezza di un tile, per via dei gametick irregolari, a volte
  la collisione viene detectata pochi pixel prima di quello che e' il bordo del tile / entita' [provare con hitbox
  piu' piccole e con detection delle collisioni con un offset es. hitbox del mob 45x45 ma la collisione avviene
  sempre con un offset di 3 pixels]

---

- [ ] animazione dello switch del livello
- [ ] animazione dell'uccisione di un mob

---

- REQUISITI MANCANTI:
- [ ] animazioni
- [ ] suono
- [ ] player selection

### low priority:

- [ ] reworka collisioni del player (si incolla alle pareti)
- [ ] creata l'animazione di quando il player viene colpito, bisogna modificare il model cosi' che quando il player
  viene colpito, il player e' respawnato alle
  coordinate iniziali

### DONE:

- [x] il keyhandler si ferma e riprende quando l'animazione e' finita e
- [x] fixato il timing dell'esplosionde della bomba
- [x] implementa il supporto per piu' mob dello stesso tipo [provare hasmap di liste per type]

***

===========================================================================
***

### 6/03/2024

### TO-DO:

### high priority: [working on animations rn]

- [ ] fixare il fatto che ora come ora, quando le imageview dei mob sono inizializzate, per via di una shallow copy
  della lista dei mob, soltanto le ultime due imageview per tipo sono utilizzate per disegnare i mob (sulle ultime
  imageview vengono disegnate le animazioni dei primi due mob )
- [ ] il denkyun dovrebbe poter tankare 2 hit
- [ ] il denkyun dovrebbe avere una possibilita' di respawnare se il tile exit e' sul campo
- [ ] le hitbox dei mob non possono essere la stessa grandezza di un tile, per via dei gametick irregolari, a volte
  la collisione viene detectata pochi pixel prima di quello che e' il bordo del tile / entita' [provare con hitbox
  piu' piccole e con detection delle collisioni con un offset es. hitbox del mob 45x45 ma la collisione avviene
  sempre con un offset di 3 pixels]

---

- [ ] animazione dello switch del livello
- [ ] animazione dell'uccisione di un mob

---

- REQUISITI MANCANTI:
- [ ] animazioni
- [ ] suono
- [ ] player selection

### low priority:

- [ ] reworka collisioni del player (si incolla alle pareti)
- [ ] creata l'animazione di quando il player viene colpito, bisogna modificare il model cosi' che quando il player
  viene colpito, il player e' respawnato alle
  coordinate iniziali

### DONE:

***

===========================================================================
***

### 7/03/2024

### TO-DO:

### high priority: [working on animations rn]

- [ ] fixare il fatto che ora come ora, quando le imageview dei mob sono inizializzate, per via di una shallow copy
  della lista dei mob, soltanto le ultime due imageview per tipo sono utilizzate per disegnare i mob (sulle ultime
  imageview vengono disegnate le animazioni dei primi due mob )
- [ ] il denkyun dovrebbe poter tankare 2 hit
- [ ] il denkyun dovrebbe avere una possibilita' di respawnare se il tile exit e' sul campo
- [ ] le hitbox dei mob non possono essere la stessa grandezza di un tile, per via dei gametick irregolari, a volte
  la collisione viene detectata pochi pixel prima di quello che e' il bordo del tile / entita' [provare con hitbox
  piu' piccole e con detection delle collisioni con un offset es. hitbox del mob 45x45 ma la collisione avviene
  sempre con un offset di 3 pixels]

---

- [ ] animazione dello switch del livello
- [ ] animazione dell'uccisione di un mob

---

- REQUISITI MANCANTI:
- [ ] animazioni
- [ ] suono
- [ ] player selection

### low priority:

- [ ] reworka collisioni del player (si incolla alle pareti)
- [ ] creata l'animazione di quando il player viene colpito, bisogna modificare il model cosi' che quando il player
  viene colpito, il player e' respawnato alle
  coordinate iniziali

### DONE:

- [x] sprites per entities flicker

***

===========================================================================
***

### 8/03/2024

### TO-DO:

### high priority: [working on animations rn]

- [ ] implementare il fatto che quando viene caricato il livello c'e' ancora piu' delay quando la mappa viene
  caricata, cosi' da poter displayare il livello attuale
- [ ] implementare funzioni fadeIn e slideIn per far si che allo switch del livello vengano replayate le animazioni
  nel costruttore della gameview

---

- [ ] fixare il fatto che ora come ora, quando le imageview dei mob sono inizializzate, per via di una shallow copy
  della lista dei mob, soltanto le ultime due imageview per tipo sono utilizzate per disegnare i mob (sulle ultime
  imageview vengono disegnate le animazioni dei primi due mob )
- [ ] il denkyun dovrebbe poter tankare 2 hit
- [ ] il denkyun dovrebbe avere una possibilita' di respawnare se il tile exit e' sul campo
- [ ] le hitbox dei mob non possono essere la stessa grandezza di un tile, per via dei gametick irregolari, a volte
  la collisione viene detectata pochi pixel prima di quello che e' il bordo del tile / entita' [provare con hitbox
  piu' piccole e con detection delle collisioni con un offset es. hitbox del mob 45x45 ma la collisione avviene
  sempre con un offset di 3 pixels]

---

- REQUISITI MANCANTI:
- [ ] animazioni
- [ ] suono
- [ ] player selection

### low priority:

- [ ] reworka collisioni del player (si incolla alle pareti)
- [ ] bisogna modificare il model cosi' che quando il player
  viene colpito, il player e' respawnato alle
  coordinate iniziali

### DONE:

- [x] fixata animazione per il player
- [x] creata animazione per quando un mob viene danneggiato
- [x] creata animazione per quando il player viene danneggiato + invulnerabilita'
- [x] create funzioni di slideIn e fadeIn per creare animazioni di switch del livello

***

===========================================================================
***

### 10/03/2024

### TO-DO:

### high priority: [working on animations rn]

- [ ] fixare il fatto che i mob non vengono eliminati quando il livello finisce, stessa cosa per power up e mob [
  usa i setVisible() ]
- [ ] implementare il fatto che quando viene caricato il livello c'e' ancora piu' delay quando la mappa viene
  caricata, cosi' da poter displayare il livello attuale

---

- [ ] fixare il fatto che ora come ora, quando le imageview dei mob sono inizializzate, per via di una shallow copy
  della lista dei mob, soltanto le ultime due imageview per tipo sono utilizzate per disegnare i mob (sulle ultime
  imageview vengono disegnate le animazioni dei primi due mob )
- [ ] il denkyun dovrebbe poter tankare 2 hit
- [ ] il denkyun dovrebbe avere una possibilita' di respawnare se il tile exit e' sul campo
- [ ] le hitbox dei mob non possono essere la stessa grandezza di un tile, per via dei gametick irregolari, a volte
  la collisione viene detectata pochi pixel prima di quello che e' il bordo del tile / entita' [provare con hitbox
  piu' piccole e con detection delle collisioni con un offset es. hitbox del mob 45x45 ma la collisione avviene
  sempre con un offset di 3 pixels]

---

- REQUISITI MANCANTI:
- [ ] animazioni
- [ ] suono
- [ ] player selection

### low priority:

- [ ] reworka collisioni del player (si incolla alle pareti)
- [ ] bisogna modificare il model cosi' che quando il player
  viene colpito, il player e' respawnato alle
  coordinate iniziali

### DONE:

- [x] implementare funzioni fadeIn e slideIn per far si che allo switch del livello vengano replayate le animazioni
  nel costruttore della gameview
- [x] fare in modo che la tilepane venga rimossa e poi aggiunta con il tempismo giusto cosi' da non farla vedere
  all'inizio dello switch del livello

***

===========================================================================
***

### 11/03/2024

### TO-DO:

### high priority: [working on animations rn]

---

- [ ] quando una bomba esplode, viene ridata al player (somma 1 al bombcount quando esplode una bomba)
- [ ] fixare il fatto che ora come ora, quando le imageview dei mob sono inizializzate, per via di una shallow copy
  della lista dei mob, soltanto le ultime due imageview per tipo sono utilizzate per disegnare i mob (sulle ultime
  imageview vengono disegnate le animazioni dei primi due mob )
- [ ] il denkyun dovrebbe poter tankare 2 hit
- [ ] il denkyun dovrebbe avere una possibilita' di respawnare se il tile exit e' sul campo
- [ ] le hitbox dei mob non possono essere la stessa grandezza di un tile, per via dei gametick irregolari, a volte
  la collisione viene detectata pochi pixel prima di quello che e' il bordo del tile / entita' [provare con hitbox
  piu' piccole e con detection delle collisioni con un offset es. hitbox del mob 45x45 ma la collisione avviene
  sempre con un offset di 3 pixels]

---

- REQUISITI MANCANTI:
- [ ] animazioni
- [ ] suono
- [ ] player selection

### low priority:

- [ ] reworka collisioni del player (si incolla alle pareti)
- [ ] bisogna modificare il model cosi' che quando il player
  viene colpito, il player e' respawnato alle
  coordinate iniziali

### DONE:

- [x] fixare il fatto che i mob non vengono eliminati quando il livello finisce, stessa cosa per power up e mob [
  usa i setVisible() ]
- [x] implementare il fatto che quando viene caricato il livello c'e' ancora piu' delay quando la mappa viene
  caricata, cosi' da poter displayare il livello attuale
- [x] animazioni slide left to right per level swap font

***

===========================================================================
***

### 13/03/2024

### TO-DO:

### high priority: [working on animations rn]

---

- [ ] il denkyun dovrebbe avere una possibilita' di respawnare se il tile exit e' sul campo[creata la logica dentro
  handleMobHit ma bisogna implementare i metodi: isExitTilePresent() e respawnMobAtExitTile()]
- [ ] animazione per tile exit
- [ ] le hitbox dei mob non possono essere la stessa grandezza di un tile, per via dei gametick irregolari, a volte
  la collisione viene detectata pochi pixel prima di quello che e' il bordo del tile / entita' [provare con hitbox
  piu' piccole e con detection delle collisioni con un offset es. hitbox del mob 45x45 ma la collisione avviene
  sempre con un offset di 3 pixels]
- [ ] fixare il fatto che ora come ora, quando le imageview dei mob sono inizializzate, per via di una shallow copy
  della lista dei mob, soltanto le ultime due imageview per tipo sono utilizzate per disegnare i mob (sulle ultime
  imageview vengono disegnate le animazioni dei primi due mob )

---

- REQUISITI MANCANTI:
- [ ] animazioni
- [ ] suono
- [ ] player selection

### low priority:

- [ ] reworka collisioni del player (si incolla alle pareti)
- [ ] bisogna modificare il model cosi' che quando il player
  viene colpito, il player e' respawnato alle
  coordinate iniziali

### DONE:

- [x] quando una bomba esplode, viene ridata al player (somma 1 al bombcount quando esplode una bomba)
- [x] il denkyun dovrebbe poter tankare 2 hit
- [x] impostata logica per il respawn del denkyun

***

===========================================================================
***

### 14/03/2024

### TO-DO:

### high priority: [working on animations rn]

---

- [ ] animazione per tile exit
- [ ] le hitbox dei mob non possono essere la stessa grandezza di un tile, per via dei gametick irregolari, a volte
  la collisione viene detectata pochi pixel prima di quello che e' il bordo del tile / entita' [provare con hitbox
  piu' piccole e con detection delle collisioni con un offset es. hitbox del mob 45x45 ma la collisione avviene
  sempre con un offset di 3 pixels]
- [ ] fixare il fatto che ora come ora, quando le imageview dei mob sono inizializzate, per via di una shallow copy
  della lista dei mob, soltanto le ultime due imageview per tipo sono utilizzate per disegnare i mob (sulle ultime
  imageview vengono disegnate le animazioni dei primi due mob )

---

- REQUISITI MANCANTI:
- [ ] animazioni
- [ ] suono
- [ ] player selection

### low priority:

- [ ] reworka collisioni del player (si incolla alle pareti)
- [ ] bisogna modificare il model cosi' che quando il player
  viene colpito, il player e' respawnato alle
  coordinate iniziali

### DONE:

- [ ] il denkyun dovrebbe avere una possibilita' di respawnare se il tile exit e' sul campo[creata la logica dentro
  handleMobHit ma bisogna implementare i metodi: isExitTilePresent() e respawnMobAtExitTile()]
- [x] creati metodi, isExitTilePresent(), respawnMobOnExitTile(), findExitTilePosition()

***

===========================================================================
***

### 15/03/2024

### TO-DO:

### high priority: [animations done]

---

- [ ] le hitbox dei mob non possono essere la stessa grandezza di un tile, per via dei gametick irregolari, a volte
  la collisione viene detectata pochi pixel prima di quello che e' il bordo del tile / entita' [provare con hitbox
  piu' piccole e con detection delle collisioni con un offset es. hitbox del mob 45x45 ma la collisione avviene
  sempre con un offset di 3 pixels]
- [ ] fixare il fatto che ora come ora, quando le imageview dei mob sono inizializzate, per via di una shallow copy
  della lista dei mob, soltanto le ultime due imageview per tipo sono utilizzate per disegnare i mob (sulle ultime
  imageview vengono disegnate le animazioni dei primi due mob )

---

- REQUISITI MANCANTI:

- [ ] suono
- [ ] player selection

### low priority:

- [ ] reworka collisioni del player (si incolla alle pareti)
- [ ] bisogna modificare il model cosi' che quando il player
  viene colpito, il player e' respawnato alle
  coordinate iniziali

### DONE:

- [x] animazione per tile exit
- [x] il denkyun dovrebbe avere una possibilita' di respawnare se il tile exit e' sul campo[creata la logica dentro
  handleMobHit ma bisogna implementare i metodi: isExitTilePresent() e respawnMobAtExitTile()]
- [x] animazioni

***

===========================================================================
***

### 16/03/2024

### TO-DO:

### high priority: [animations done]

---

- [ ] fixare il fatto che ora come ora, quando le imageview dei mob sono inizializzate, per via di una shallow copy
  della lista dei mob, soltanto le ultime due imageview per tipo sono utilizzate per disegnare i mob (sulle ultime
  imageview vengono disegnate le animazioni dei primi due mob )

---

- REQUISITI MANCANTI:

- [ ] suono
- [ ] player selection

### low priority:

- [ ] reworka collisioni del player (si incolla alle pareti)
- [ ] bisogna modificare il model cosi' che quando il player
  viene colpito, il player e' respawnato alle
  coordinate iniziali

### DONE:

- [x] [NON FUNZIONA PROVARE UN ALTRO METODO]le hitbox dei mob non possono essere la stessa grandezza di un tile, per
  via
  dei gametick
  irregolari, a
  volte
  la collisione viene detectata pochi pixel prima di quello che e' il bordo del tile / entita' [provare con hitbox
  piu' piccole e con detection delle collisioni con un offset es. hitbox del mob 45x45 ma la collisione avviene
  sempre con un offset di 3 pixels]

***

===========================================================================
***

### 18/03/2024

### TO-DO:

### high priority: [doing audio stuff rn]

---

- [ ] fare l'animazione per quando il player entra nel tile exit
- [ ] fixare il fatto che ora come ora, quando le imageview dei mob sono inizializzate, per via di una shallow copy
  della lista dei mob, soltanto le ultime due imageview per tipo sono utilizzate per disegnare i mob (sulle ultime
  imageview vengono disegnate le animazioni dei primi due mob )

---

- REQUISITI MANCANTI:

- [ ] suono
- [ ] player selection

### low priority:

- [ ] reworka collisioni del player (si incolla alle pareti)
- [ ] bisogna modificare il model cosi' che quando il player
  viene colpito, il player e' respawnato alle
  coordinate iniziali

### DONE:

***

===========================================================================
***

### 19/03/2024

### TO-DO:

### high priority: [doing audio stuff rn]

- [ ] fixare il fatto che se la soundtrack viene messa in pausa, poi no si puo' piu' restartare

---

- [ ] l'exit tile non viene rimosso correttamente quando un nuovo livello viene caricato, rimane nella vecchia
  posizione ma viene nascosto dai nuovi sprites
- [ ] quando il player collide con il tile exit, il numero del livello continua ad aumentare
- [ ] fare l'animazione per quando il player entra nel tile exit
- [ ] fixare il fatto che ora come ora, quando le imageview dei mob sono inizializzate, per via di una shallow copy
  della lista dei mob, soltanto le ultime due imageview per tipo sono utilizzate per disegnare i mob (sulle ultime
  imageview vengono disegnate le animazioni dei primi due mob )

---

- REQUISITI MANCANTI:

- [ ] suono
- [ ] player selection
- [ ] settings

### low priority:

- [ ] reworka collisioni del player (si incolla alle pareti)
- [ ] bisogna modificare il model cosi' che quando il player
  viene colpito, il player e' respawnato alle
  coordinate iniziali

### DONE:

-[x] suono per l'esplosione della bomba
- [x] suono per l'uccisione di un mob
- [x] suono per la raccolta di un powerup
- [x] suono per la morte del player
- [x] suono per il cambio di stage
- [x] suono per stage clear
- [x] suono per posizionamento della bomba
- [x] suono per il movimento del player
- [x] suono per la morte di un mob
- [x] implementata una bozza per l'handling della soundtrack per livello [ fixare il fatto che se si stoppa la
  traccia e la si fa riapartire, la traccia non riparte ]

***

===========================================================================
***

### 20/03/2024

### TO-DO:

### high priority:

---

- [ ] fixare il fatto che ora come ora, quando le imageview dei mob sono inizializzate, per via di una shallow copy
  della lista dei mob, soltanto le ultime due imageview per tipo sono utilizzate per disegnare i mob (sulle ultime
  imageview vengono disegnate le animazioni dei primi due mob )

---

- REQUISITI MANCANTI:


- [ ] player selection
- [ ] settings

### low priority:

- [ ] reworka collisioni del player (si incolla alle pareti)
- [ ] bisogna modificare il model cosi' che quando il player
  viene colpito, il player e' respawnato alle
  coordinate iniziali
- [ ] l'animazione del player che entra nel file exit fa schifo
- [ ] puoi udare lo sprite sheet goated per l'animazione della morte del player
-

### DONE:

- [x] fixare il fatto che se la soundtrack viene messa in pausa, poi no si puo' piu' restartare
- [x] suono
- [x] l'exit tile non viene rimosso correttamente quando un nuovo livello viene caricato, rimane nella vecchia
  posizione ma viene nascosto dai nuovi sprites
- [x] quando il player collide con il tile exit, il numero del livello continua ad aumentare
- [x] fare l'animazione per quando il player entra nel tile exit

***

===========================================================================
***

### 21/03/2024

### TO-DO:

### high priority: [doing ui stuff rn]

- [ ] finire schermata YOUWIN
- [ ] implementare la schermata GameOver
- [ ] implementare il profilo utente
- [ ] implementare un avatar selezionabile
- [ ] creare un mini database con all'interno tutti i punteggi delle persone che hanno giocato al gioco, (leaderboard)

---

- [ ] fixare la probabilita' con cui puo' spawnare l'exit tile prima di arrivare all'ultimo blocco distruttibile
  sulla mappa
- [ ] fixare il modo in cui vengono creati i powerup, da fixare la probebilita' con cui vengono droppati i powerup
  (ora come ora esce PER FORZA almeno un powerup per blocco)
- [ ] implementare ultimo powerup
- [ ] fixare il fatto che ora come ora, quando le imageview dei mob sono inizializzate, per via di una shallow copy
  della lista dei mob, soltanto le ultime due imageview per tipo sono utilizzate per disegnare i mob (sulle ultime
  imageview vengono disegnate le animazioni dei primi due mob )

---

- REQUISITI MANCANTI:


- [ ] gestione del profilo utente

### low priority:

- [ ] reworka collisioni del player (si incolla alle pareti)
- [ ] bisogna modificare il model cosi' che quando il player
  viene colpito, il player e' respawnato alle
  coordinate iniziali
- [ ] puoi udare lo sprite sheet goated per l'animazione della morte del player

### DONE:

- [x] fixata l'animazione del player che entra nel tile exit
- [x] implementato il winning screen
- [x] ora quando il player vince, la soundtrack per l'ending playa
- [x] importati sprite per la schermata YOUWIN
- [x] trovata soluzione per la limitazione del numero dei colori supportati (GIMP: export in jpeg -> rifinitura ->
  export in png)
- [x] fix vari

***

===========================================================================
***

### 22/03/2024

### TO-DO:

### high priority: [doing ui stuff rn]

- [ ] implementare la schermata GameOver
- [ ] implementare il profilo utente
- [ ] implementare un avatar selezionabile
- [ ] creare un mini database con all'interno tutti i punteggi delle persone che hanno giocato al gioco, (leaderboard)

---

- [ ] fixare la probabilita' con cui puo' spawnare l'exit tile prima di arrivare all'ultimo blocco distruttibile
  sulla mappa
- [ ] fixare il modo in cui vengono creati i powerup, da fixare la probebilita' con cui vengono droppati i powerup
  (ora come ora esce PER FORZA almeno un powerup per blocco)
- [ ] implementare ultimo powerup
- [ ] fixare il fatto che ora come ora, quando le imageview dei mob sono inizializzate, per via di una shallow copy
  della lista dei mob, soltanto le ultime due imageview per tipo sono utilizzate per disegnare i mob (sulle ultime
  imageview vengono disegnate le animazioni dei primi due mob )

---

- REQUISITI MANCANTI:


- [ ] gestione del profilo utente

### low priority:

- [ ] reworka collisioni del player (si incolla alle pareti)
- [ ] bisogna modificare il model cosi' che quando il player
  viene colpito, il player e' respawnato alle
  coordinate iniziali
- [ ] puoi udare lo sprite sheet goated per l'animazione della morte del player

### DONE:

- [x] finire schermata YOUWIN

***

===========================================================================
***

### 23/03/2024

### TO-DO:

### high priority: [doing ui stuff rn]

- [ ] implementare il profilo utente
- [ ] implementare un avatar selezionabile
- [ ] creare un mini database con all'interno tutti i punteggi delle persone che hanno giocato al gioco, (leaderboard)

---

- [ ] fixare la directory che viene presa per caricare i file all'interno degli enum per i sample audio
- [ ] fixare la probabilita' con cui puo' spawnare l'exit tile prima di arrivare all'ultimo blocco distruttibile
  sulla mappa
- [ ] fixare il modo in cui vengono creati i powerup, da fixare la probebilita' con cui vengono droppati i powerup
  (ora come ora esce PER FORZA almeno un powerup per blocco)
- [ ] implementare ultimo powerup
- [ ] fixare il fatto che ora come ora, quando le imageview dei mob sono inizializzate, per via di una shallow copy
  della lista dei mob, soltanto le ultime due imageview per tipo sono utilizzate per disegnare i mob (sulle ultime
  imageview vengono disegnate le animazioni dei primi due mob )

---

- REQUISITI MANCANTI:


- [ ] gestione del profilo utente

### low priority:

- [ ] reworka collisioni del player (si incolla alle pareti)
- [ ] bisogna modificare il model cosi' che quando il player
  viene colpito, il player e' respawnato alle
  coordinate iniziali
- [ ] puoi udare lo sprite sheet goated per l'animazione della morte del player

### DONE:

- [x] implementare la schermata GameOver

***

===========================================================================
***

### 24/03/2024

### TO-DO:

### high priority: [doing ui stuff rn]

- [ ] implementare il profilo utente
- [ ] implementare un avatar selezionabile
- [ ] creare un mini database con all'interno tutti i punteggi delle persone che hanno giocato al gioco, (leaderboard)

---

- [ ] fixare la directory che viene presa per caricare i file all'interno degli enum per i sample audio
- [ ] fixare la probabilita' con cui puo' spawnare l'exit tile prima di arrivare all'ultimo blocco distruttibile
  sulla mappa
- [ ] fixare il modo in cui vengono creati i powerup, da fixare la probebilita' con cui vengono droppati i powerup
  (ora come ora esce PER FORZA almeno un powerup per blocco)
- [ ] implementare ultimo powerup
- [ ] fixare il fatto che ora come ora, quando le imageview dei mob sono inizializzate, per via di una shallow copy
  della lista dei mob, soltanto le ultime due imageview per tipo sono utilizzate per disegnare i mob (sulle ultime
  imageview vengono disegnate le animazioni dei primi due mob )

---

- REQUISITI MANCANTI:


- [ ] gestione del profilo utente

### low priority:

- [ ] reworka collisioni del player (si incolla alle pareti)
- [ ] bisogna modificare il model cosi' che quando il player
  viene colpito, il player e' respawnato alle
  coordinate iniziali
- [ ] puoi udare lo sprite sheet goated per l'animazione della morte del player

### DONE:

***

===========================================================================
***

### 26/03/2024

### TO-DO:

### high priority: [doing ui stuff rn]

- [ ] implementare il fatto che quando il player vince oppure muore, il nome del player viene disegnato nelle
  schermate GameOver e YOUWIN
- [ ] fixare il fatto che se perdi e fai continua oppure torni al menu e poi restarti un livello, il livello non si
  carica
- [ ] fai design per il MENU
- [ ] fai design per la PlayerSelection

---

- [ ] fixare la directory che viene presa per caricare i file all'interno degli enum per i sample audio
- [ ] fixare la probabilita' con cui puo' spawnare l'exit tile prima di arrivare all'ultimo blocco distruttibile
  sulla mappa
- [ ] fixare il modo in cui vengono creati i powerup, da fixare la probebilita' con cui vengono droppati i powerup
  (ora come ora esce PER FORZA almeno un powerup per blocco)
- [ ] implementare ultimo powerup
- [ ] fixare il fatto che ora come ora, quando le imageview dei mob sono inizializzate, per via di una shallow copy
  della lista dei mob, soltanto le ultime due imageview per tipo sono utilizzate per disegnare i mob (sulle ultime
  imageview vengono disegnate le animazioni dei primi due mob )

---

- REQUISITI MANCANTI:


- [ ] gestione del profilo utente

### low priority:

- [ ] reworka collisioni del player (si incolla alle pareti)
- [ ] bisogna modificare il model cosi' che quando il player
  viene colpito, il player e' respawnato alle
  coordinate iniziali
- [ ] puoi udare lo sprite sheet goated per l'animazione della morte del player

### DONE:

- [x] creare un mini database con all'interno tutti i punteggi delle persone che hanno giocato al gioco, (leaderboard)
- [x] implementare il profilo utente

***

===========================================================================
***

### 27/03/2024

### TO-DO:

### high priority: [doing ui stuff rn]

- [ ] implementare il fatto che quando il player vince oppure muore, il nome del player viene disegnato nelle
  schermate GameOver e YOUWIN
- [ ] fixare il fatto che se perdi e fai continua oppure torni al menu e poi restarti un livello, il livello non si
  carica
- [ ] fai design per il MENU
- [ ] fai design per la PlayerSelection

---

- [ ] fixare la directory che viene presa per caricare i file all'interno degli enum per i sample audio
- [ ] fixare la probabilita' con cui puo' spawnare l'exit tile prima di arrivare all'ultimo blocco distruttibile
  sulla mappa
- [ ] fixare il modo in cui vengono creati i powerup, da fixare la probebilita' con cui vengono droppati i powerup
  (ora come ora esce PER FORZA almeno un powerup per blocco)
- [ ] implementare ultimo powerup
- [ ] fixare il fatto che ora come ora, quando le imageview dei mob sono inizializzate, per via di una shallow copy
  della lista dei mob, soltanto le ultime due imageview per tipo sono utilizzate per disegnare i mob (sulle ultime
  imageview vengono disegnate le animazioni dei primi due mob )

---

- REQUISITI MANCANTI:


- [ ] gestione del profilo utente

### low priority:

- [ ] reworka collisioni del player (si incolla alle pareti)
- [ ] bisogna modificare il model cosi' che quando il player
  viene colpito, il player e' respawnato alle
  coordinate iniziali
- [ ] puoi udare lo sprite sheet goated per l'animazione della morte del player

### DONE:

***

===========================================================================
***

### 28/03/2024

### TO-DO:

### high priority: [doing ui stuff rn]

- [ ] fai design per il MENU
- [ ] fai design per la PlayerSelection

---

- [ ] fixare la directory che viene presa per caricare i file all'interno degli enum per i sample audio
- [ ] fixare la probabilita' con cui puo' spawnare l'exit tile prima di arrivare all'ultimo blocco distruttibile
  sulla mappa
- [ ] fixare il modo in cui vengono creati i powerup, da fixare la probebilita' con cui vengono droppati i powerup
  (ora come ora esce PER FORZA almeno un powerup per blocco)
- [ ] implementare ultimo powerup
- [ ] fixare il fatto che ora come ora, quando le imageview dei mob sono inizializzate, per via di una shallow copy
  della lista dei mob, soltanto le ultime due imageview per tipo sono utilizzate per disegnare i mob (sulle ultime
  imageview vengono disegnate le animazioni dei primi due mob )

---

- REQUISITI MANCANTI:


- [ ] gestione del profilo utente

### low priority:

- [ ] reworka collisioni del player (si incolla alle pareti)
- [ ] bisogna modificare il model cosi' che quando il player
  viene colpito, il player e' respawnato alle
  coordinate iniziali
- [ ] puoi udare lo sprite sheet goated per l'animazione della morte del player

### DONE:

- [x] fixare il fatto che se perdi e fai continua oppure torni al menu e poi restarti un livello, il livello non si
  carica
- [x] implementare il fatto che quando il player vince oppure muore, il nome del player viene disegnato nelle
  schermate GameOver e YOUWIN (scartata, non mi piace l'effetto che da con diverse lunghezze di stringhe)

***

===========================================================================
***

### 29/03/2024

### TO-DO:

### high priority: [doing ui stuff rn]

---

- [ ] fixare la directory che viene presa per caricare i file all'interno degli enum per i sample audio
- [ ] fixare la probabilita' con cui puo' spawnare l'exit tile prima di arrivare all'ultimo blocco distruttibile
  sulla mappa
- [ ] fixare il modo in cui vengono creati i powerup, da fixare la probebilita' con cui vengono droppati i powerup
  (ora come ora esce PER FORZA almeno un powerup per blocco)
- [ ] implementare ultimo powerup
- [ ] fixare il fatto che ora come ora, quando le imageview dei mob sono inizializzate, per via di una shallow copy
  della lista dei mob, soltanto le ultime due imageview per tipo sono utilizzate per disegnare i mob (sulle ultime
  imageview vengono disegnate le animazioni dei primi due mob )

---

- REQUISITI MANCANTI:

### low priority:

- [ ] reworka collisioni del player (si incolla alle pareti)
- [ ] bisogna modificare il model cosi' che quando il player
  viene colpito, il player e' respawnato alle
  coordinate iniziali
- [ ] puoi udare lo sprite sheet goated per l'animazione della morte del player

### DONE:

- [x] fai design per il MENU
- [x] fai design per la PlayerSelection
- [x] implementato metodo stopAll() per l'audio manager
- [x] gestione del profilo utente

***

===========================================================================
***

### 04/04/2024

### TO-DO:

### high priority: [fixing stuff rn]

---

- [ ] fixare il fatto che ora come ora, quando le imageview dei mob sono inizializzate, per via di una shallow copy
  della lista dei mob, soltanto le ultime due imageview per tipo sono utilizzate per disegnare i mob (sulle ultime
  imageview vengono disegnate le animazioni dei primi due mob )

---

- REQUISITI MANCANTI:

### low priority:

- [ ] reworka collisioni del player (si incolla alle pareti)
- [ ] bisogna modificare il model cosi' che quando il player
  viene colpito, il player e' respawnato alle
  coordinate iniziali
- [ ] puoi usare lo sprite sheet goated per l'animazione della morte del player

### DONE:

- [x] fixare la directory che viene presa per caricare i file all'interno degli enum per i sample audio
- [x] fixare la probabilita' con cui puo' spawnare l'exit tile prima di arrivare all'ultimo blocco distruttibile
  sulla mappa
- [x] fixare il modo in cui vengono creati i powerup, da fixare la probebilita' con cui vengono droppati i powerup
  (ora come ora esce PER FORZA almeno un powerup per blocco)
- [x] implementato lo spawn dei powerup ogni volta che un mob muore
- [x] implementare ultimo powerup

***

===========================================================================
***

### 05/04/2024

### TO-DO:

### high priority: [fixing stuff rn]

---

- [ ] fixare il fatto che ora come ora, quando le imageview dei mob sono inizializzate, per via di una shallow copy
  della lista dei mob, soltanto le ultime due imageview per tipo sono utilizzate per disegnare i mob (sulle ultime
  imageview vengono disegnate le animazioni dei primi due mob )
    - un possibile modo di implementare questa funzione e' utilizzare un hasmap<imageview, timeline>

---

- REQUISITI MANCANTI:

### low priority:

- [ ] reworka collisioni del player (si incolla alle pareti)
- [ ] bisogna modificare il model cosi' che quando il player
  viene colpito, il player e' respawnato alle
  coordinate iniziali
- [ ] puoi usare lo sprite sheet goated per l'animazione della morte del player

### DONE:

- [x] fixate l'update delle imageview dei mob quandi si muovono sull'asse delle Y, prima si muovevano piu'
  velocemente sull'asse delle Y

***

===========================================================================
***

### 06/04/2024

### TO-DO:

### high priority: [fixing stuff rn]

---

- [ ] fixare il fatto che ora come ora, quando le imageview dei mob sono inizializzate, per via di una shallow copy
  della lista dei mob, soltanto le ultime due imageview per tipo sono utilizzate per disegnare i mob (sulle ultime
  imageview vengono disegnate le animazioni dei primi due mob )
    - un possibile modo di implementare questa funzione e' utilizzare un hasmap<imageview, timeline>

---

- REQUISITI MANCANTI:

### low priority:

- [ ] reworka collisioni del player (si incolla alle pareti)
- [ ] bisogna modificare il model cosi' che quando il player
  viene colpito, il player e' respawnato alle
  coordinate iniziali
- [ ] puoi usare lo sprite sheet goated per l'animazione della morte del player

### DONE:

- [x] fixato il movement dei mob, ora non si incastrano piu' randomicamente
- [x] aumentata stabilita' per la scoreboard, ora se un player esce dal gioco senza salvare, il nome del player non
  viene salvato senza lo score della run
- [x] completati i tileset per il bordo del livello

***

===========================================================================
***

### 07/04/2024

### TO-DO:

### high priority: [fixing stuff rn]

---

- [ ] fixare il fatto che ora come ora, quando le imageview dei mob sono inizializzate, per via di una shallow copy
  della lista dei mob, soltanto le ultime due imageview per tipo sono utilizzate per disegnare i mob (sulle ultime
  imageview vengono disegnate le animazioni dei primi due mob )
    - possiamo implementare un sistema di id, ogni mob ha un id nel suo costruttore, quando il mob viene caricato
      (letto dal json) un id viene assegnato al mob e poi utilizzato per identificarlo nella gameview

---

- REQUISITI MANCANTI:

### low priority:

- [ ] reworka collisioni del player (si incolla alle pareti)
- [ ] bisogna modificare il model cosi' che quando il player
  viene colpito, il player e' respawnato alle
  coordinate iniziali
- [ ] puoi usare lo sprite sheet goated per l'animazione della morte del player

### DONE:

***

===========================================================================
***

### 08/04/2024

### TO-DO:

### high priority:

---
FINISHED VERSION 1.0
---

- REQUISITI MANCANTI:
- [ ] fare in modo che il tile exit sia positionato sotto ai mob
- [ ] diagramma
- [ ] progetto eclipse
- [ ] java doc
- [ ] relazione
-

### low priority:

- [ ] reworka collisioni del player (si incolla alle pareti)
- [ ] bisogna modificare il model cosi' che quando il player
  viene colpito, il player e' respawnato alle
  coordinate iniziali
- [ ] puoi usare lo sprite sheet goated per l'animazione della morte del player

### DONE:

- [x] fixare il fatto che ora come ora, quando le imageview dei mob sono inizializzate, per via di una shallow copy
  della lista dei mob, soltanto le ultime due imageview per tipo sono utilizzate per disegnare i mob (sulle ultime
  imageview vengono disegnate le animazioni dei primi due mob )
    - possiamo implementare un sistema di id, ogni mob ha un id nel suo costruttore, quando il mob viene caricato
      (letto dal json) un id viene assegnato al mob e poi utilizzato per identificarlo nella gameview

- [x] fixato handling per le animation timeline per ogni mob presente sul terreno
- [x] reworkato il sistema di handling per gli ID di mob
- [x] reworkato l'handling degli sprite per all'interno della gameview
- [x] reworkato il sistema di handling per le timeline
- [x] reworkati alcuni metodi che riguardavano la gestionde della distruzione dei tile
- [x] reworkati tutti i metodi che utilizzavano mobSprites, ora utilizza una mappa che comprende anche gli id dei mob
- [x] vari fix che riguardavano bug sorti dal cambiamento di altri metodi fondamentali

***

===========================================================================
***

### 08/04/2024

### TO-DO:

### high priority:

---
FINISHED VERSION 1.0
---

- REQUISITI MANCANTI:
- [ ] diagramma
- [ ] progetto eclipse
- [ ] java doc
- [ ] relazione

### low priority:

- [ ] reworka collisioni del player (si incolla alle pareti)
- [ ] bisogna modificare il model cosi' che quando il player
  viene colpito, il player e' respawnato alle
  coordinate iniziali
- [ ] puoi usare lo sprite sheet goated per l'animazione della morte del player

### DONE:

- [ ] fare in modo che il tile exit sia positionato sotto ai mob

***

===========================================================================
***









