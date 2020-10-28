# Oppgavetekst

Lag en applikasjon som benytter en database på student.cs.hioa.no. Appen skal gjøre det mulig å registrere hus på OsloMet. 
Om husene lagres en beskrivelse, gateadresse og gps-koordinater og antall etasjer. Pass på at man ikke kan registrere noe midt 
i ingenting og at den faktisk finner en lovlig adresse. 

Husene skal vises som markører på et kart. Trykker man på markøren skal det komme fram mulighet til å registrere rom i bygget. 
Om rommene lagres etasje nr, romnummer, kapasitet og beskrivelse. Når hus og rom er registrert skal man kunne klikke på 
markør for hus, se rom som er registrerte i huset og lage en romreservasjon med dato og klokkeslett til og fra for dette rommet.

På studssh må det  være en web-service som gjør det mulig å hente data fra tabellene i form av JSON-objekter. 
Det må også være webtjenester som gjør det mulig å legge inn rom og reservasjoner. Web-tjenestene kodes i PHP. 

Applikasjonen bør zoome på Pilestredet ved oppstart. Ikke ta hensyn til Kjeller siden det skal avvikles der.


