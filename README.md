# mobilny-vcard

## Dokumentacja
### Endpoint
* http://localhost:8080/find?profession={profession}
##### Parametry
profession - nazwa zawodu

##### Przykład
* http://localhost:8080/find?profession=hydraulik

Zwróci: Listę firm odpowiadających poszukiwanemu zawodowi - hydraulik - z możliwością pobrania pliku w formacie VCard.


### Endpoint
* http://localhost:8080/getVCard/{company}

##### Parametry
company - firma w formacie JSON

Zwróci: plik .vcf dla danej firmy

##### Przykład
```
BEGIN:VCARD
VERSION:4.0
FN;CHARSET=utf-8: Hydraulika Montaż Instalacji Sanitarnych i Grzewczych Robert Rosłoniec
ADR;CHARSET=utf-8;TYPE=WORK:;; ul. Wierzbowa 12, 05-503 Robercin
EMAIL: rrrobert@vp.pl
TEL;WORK: 501 083 795
END:VCARD
```