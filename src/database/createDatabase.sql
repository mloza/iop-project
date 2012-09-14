-- OBSŁUGA BAZY DANYCH H2
--
-- 1. Klikamy 2x na plik h2*.jar w katalogu (...)/h2/bin aby uruchomić konsolę do obsługi bazy
-- 2. Tworzymy bazę inteligenteye oraz użytkownika pk z hasłem pk
-- 3. Wykonujemy na bazie poniższe zapytania:

CREATE TABLE person (
    personId INT(8) NOT NULL AUTO_INCREMENT,
    firstname VARCHAR(45) NOT NULL,
    lastname VARCHAR(45) NOT NULL,
    age INT(3) NOT NULL,
    country VARCHAR(45) NOT NULL,
    PRIMARY KEY(personId)
);