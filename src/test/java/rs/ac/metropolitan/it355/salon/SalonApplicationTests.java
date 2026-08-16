package rs.ac.metropolitan.it355.salon;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import rs.ac.metropolitan.it355.salon.servis.KlijentServis;
import rs.ac.metropolitan.it355.salon.servis.UslugaServis;

import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Provera da se Spring kontekst uspešno podiže i da su početni podaci učitani.
 */
@SpringBootTest
class SalonApplicationTests {

    @Autowired
    private KlijentServis klijentServis;

    @Autowired
    private UslugaServis uslugaServis;

    @Test
    void kontekstSePodize() {
        assertTrue(klijentServis.brojKlijenata() > 0, "Skladište treba da ima početne klijente.");
        assertTrue(uslugaServis.brojUsluga() > 0, "Skladište treba da ima početne usluge.");
    }
}
