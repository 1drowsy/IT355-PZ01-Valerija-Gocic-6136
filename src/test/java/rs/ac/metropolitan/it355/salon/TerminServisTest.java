package rs.ac.metropolitan.it355.salon;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import rs.ac.metropolitan.it355.salon.model.StatusTermina;
import rs.ac.metropolitan.it355.salon.model.Termin;
import rs.ac.metropolitan.it355.salon.servis.KlijentServis;
import rs.ac.metropolitan.it355.salon.servis.KozmeticarServis;
import rs.ac.metropolitan.it355.salon.servis.TerminServis;
import rs.ac.metropolitan.it355.salon.servis.UslugaServis;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

/**
 * Testovi poslovnih pravila servisnog sloja za termine.
 */
@SpringBootTest
class TerminServisTest {

    @Autowired
    private TerminServis terminServis;

    @Autowired
    private KlijentServis klijentServis;

    @Autowired
    private UslugaServis uslugaServis;

    @Autowired
    private KozmeticarServis kozmeticarServis;

    /** Pomoćna metoda koja pravi termin za zadato vreme. */
    private Termin napraviTermin(LocalDateTime vreme) {
        Termin termin = new Termin();
        termin.setDatumIVreme(vreme);
        termin.setKlijent(klijentServis.sviKlijenti().get(0));
        termin.setUsluga(uslugaServis.sveUsluge().get(0));
        termin.setKozmeticar(kozmeticarServis.sviKozmeticari().get(0));
        termin.setStatus(StatusTermina.ZAKAZAN);
        return termin;
    }

    @Test
    void ispravanTerminProlaziProveru() {
        Termin termin = napraviTermin(LocalDateTime.of(LocalDate.now().plusDays(10), LocalTime.of(10, 0)));
        assertNull(terminServis.proveriTermin(termin), "Ispravan termin ne sme da vrati grešku.");
    }

    @Test
    void terminUProslostiNijeDozvoljen() {
        Termin termin = napraviTermin(LocalDateTime.now().minusDays(1));
        assertNotNull(terminServis.proveriTermin(termin), "Termin u prošlosti mora da vrati grešku.");
    }

    @Test
    void terminVanRadnogVremenaNijeDozvoljen() {
        Termin termin = napraviTermin(LocalDateTime.of(LocalDate.now().plusDays(10), LocalTime.of(22, 0)));
        assertNotNull(terminServis.proveriTermin(termin), "Termin van radnog vremena mora da vrati grešku.");
    }

    @Test
    void dvaTerminaIstogKozmeticaraSePreklapaju() {
        LocalDateTime vreme = LocalDateTime.of(LocalDate.now().plusDays(11), LocalTime.of(12, 0));
        terminServis.sacuvaj(napraviTermin(vreme));

        // Drugi termin počinje 15 minuta kasnije, dok prvi jos traje
        Termin drugi = napraviTermin(vreme.plusMinutes(15));
        assertNotNull(terminServis.proveriTermin(drugi), "Preklapanje termina mora da vrati grešku.");
    }

    @Test
    void otkazivanjeMenjaStatusTermina() {
        Termin termin = napraviTermin(LocalDateTime.of(LocalDate.now().plusDays(12), LocalTime.of(9, 0)));
        terminServis.sacuvaj(termin);

        terminServis.otkazi(termin.getId());

        assertEquals(StatusTermina.OTKAZAN, terminServis.pronadji(termin.getId()).getStatus());
    }
}
