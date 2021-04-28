### veľmi stručne o práci, vízia, nápady

Práca sa zaoberá hľadaním a analýzou stratégií v hre No Thanks pre 3 hráčov.

## cieľ

implementovať modifikovaný minimax (+ pruning), príp.pohľadať či sa dajú ďalšie techniky z "šachoidných" programov použiť.
možno použiť memoizáciu?

### pozorovania

vytvoril som niekoľko základných stratégií, do pozornosti dávam dve:
  - Basic N: podľa toho aké veľké je N sa vždy rozhodneme aký ťah spraviť na základe prezretia stromu do hĺbky N. Použiteľné do N<15
  - Sequence: Keďže vieme o budúcnosti, tak si vieme pozrieť akej najväčšej postupnosti hodnôt vie byť práve vyložená karta súčasťou. Či si kartu s hodnotou I vezmeme alebo nie závisí od toho či sa na nej nachádza aspoň I/N žetónov. Ak si karty vyberáme iba podľa tohto kritéria, tak v optimistickom predpoklade si zoberieme celú postupnosť s celkovým súčtom žetónov aspoň takým aká je najmenšia karta z postupnosti.

## stojí za povšimnutie / zamyslenie sa

Niekoľko stoviek ráz som spustil hry s dvomi botmi so stratégiou Basic N (skúšal som rôzne hodnoty od 6 do 15, ale zásadné rozdiely vo výsledkoh vidno nebolo) a jedného so stratégiou Sequence. Z výsledkov bolo vidno že Sequence najčastejšie vyhrával. Čo však stojí za povšimnutie, tak hráč v poradí po Sequence (so stratégiou Basic N) dosahoval výrazne lepší výsledok ako ten ktorý bol v poradí pred ním. Skúšal som rôzne poradia a vždy som dospel k tomuto záveru. Určite je to hodné pozorovania, aj keď stratégie ešte zďaleka nie sú doladené ako by som chcel, takže príliš dlho sa týmto zdržovať nechcem.
