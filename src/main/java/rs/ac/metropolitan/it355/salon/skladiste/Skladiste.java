package rs.ac.metropolitan.it355.salon.skladiste;

import jakarta.annotation.PostConstruct;
import jakarta.servlet.ServletContext;
import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.ApplicationScope;
import rs.ac.metropolitan.it355.salon.model.KategorijaUsluge;
import rs.ac.metropolitan.it355.salon.model.Klijent;
import rs.ac.metropolitan.it355.salon.model.Kozmeticar;
import rs.ac.metropolitan.it355.salon.model.Recenzija;
import rs.ac.metropolitan.it355.salon.model.StatusTermina;
import rs.ac.metropolitan.it355.salon.model.Termin;
import rs.ac.metropolitan.it355.salon.model.Usluga;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Skladište podataka aplikacije - zamena za bazu podataka.
 *
 * Objekat je u application scope-u ({@link ApplicationScope}), što znači da postoji
 * samo jedan primerak za celu web aplikaciju i da je zajednički za sve korisnike i
 * sve sesije, sve dok se server ne ugasi. Dodatno je upisan i u {@link ServletContext}
 * pod imenom "skladiste", pa mu se može pristupiti i direktno preko konteksta aplikacije.
 *
 * Podaci se čuvaju u običnim listama, a brojači {@link AtomicLong} generišu nove ID-jeve
 * na isti način na koji bi to radila baza (auto increment).
 */
@Component
@ApplicationScope
public class Skladiste {

    private final List<Klijent> klijenti = new ArrayList<>();
    private final List<Usluga> usluge = new ArrayList<>();
    private final List<Kozmeticar> kozmeticari = new ArrayList<>();
    private final List<Termin> termini = new ArrayList<>();
    private final List<Recenzija> recenzije = new ArrayList<>();

    private final AtomicLong brojacKlijenata = new AtomicLong(0);
    private final AtomicLong brojacUsluga = new AtomicLong(0);
    private final AtomicLong brojacKozmeticara = new AtomicLong(0);
    private final AtomicLong brojacTermina = new AtomicLong(0);
    private final AtomicLong brojacRecenzija = new AtomicLong(0);

    private final ServletContext servletContext;

    public Skladiste(ServletContext servletContext) {
        this.servletContext = servletContext;
        ubaciPocetnePodatke();
    }

    /** Skladište se upisuje u kontekst aplikacije čim se objekat kreira. */
    @PostConstruct
    public void registrujUKontekstAplikacije() {
        servletContext.setAttribute("skladiste", this);
    }

    // ------------------------------------------------------------------
    // Pristup kolekcijama (koriste ih isključivo klase servisnog sloja)
    // ------------------------------------------------------------------

    public List<Klijent> getKlijenti() {
        return klijenti;
    }

    public List<Usluga> getUsluge() {
        return usluge;
    }

    public List<Kozmeticar> getKozmeticari() {
        return kozmeticari;
    }

    public List<Termin> getTermini() {
        return termini;
    }

    public List<Recenzija> getRecenzije() {
        return recenzije;
    }

    public Long sledeciIdKlijenta() {
        return brojacKlijenata.incrementAndGet();
    }

    public Long sledeciIdUsluge() {
        return brojacUsluga.incrementAndGet();
    }

    public Long sledeciIdKozmeticara() {
        return brojacKozmeticara.incrementAndGet();
    }

    public Long sledeciIdTermina() {
        return brojacTermina.incrementAndGet();
    }

    public Long sledeciIdRecenzije() {
        return brojacRecenzija.incrementAndGet();
    }

    // ------------------------------------------------------------------
    // Početni (mock) podaci da aplikacija ne bude prazna pri pokretanju
    // ------------------------------------------------------------------

    private void ubaciPocetnePodatke() {
        // --- Klijenti ---
        klijenti.add(new Klijent(sledeciIdKlijenta(), "Marija", "Jovanović", "0641234567", "marija.j@gmail.com"));
        klijenti.add(new Klijent(sledeciIdKlijenta(), "Ana", "Petrović", "0629876543", "ana.petrovic@gmail.com"));
        klijenti.add(new Klijent(sledeciIdKlijenta(), "Jelena", "Nikolić", "0655544332", "jelena.n@yahoo.com"));
        klijenti.add(new Klijent(sledeciIdKlijenta(), "Milan", "Stojanović", "0607778899", "milan.s@gmail.com"));

        // --- Usluge ---
        usluge.add(new Usluga(sledeciIdUsluge(), "Dubinsko čišćenje lica",
                "Tretman čišćenja pora ultrazvučnom špatulom uz završnu masku.", 60, 3500.0, KategorijaUsluge.LICE));
        usluge.add(new Usluga(sledeciIdUsluge(), "Anti-age tretman",
                "Tretman protiv bora sa hijaluronskim serumom i masažom lica.", 75, 5000.0, KategorijaUsluge.LICE));
        usluge.add(new Usluga(sledeciIdUsluge(), "Relax masaža tela",
                "Opuštajuća masaža celog tela aromatičnim uljima.", 50, 2800.0, KategorijaUsluge.TELO));
        usluge.add(new Usluga(sledeciIdUsluge(), "Manikir sa gel lakom",
                "Klasičan manikir uz nanošenje gel laka po izboru.", 45, 1800.0, KategorijaUsluge.NOKTI));
        usluge.add(new Usluga(sledeciIdUsluge(), "Pedikir",
                "Medicinski pedikir sa piling tretmanom stopala.", 60, 2200.0, KategorijaUsluge.NOKTI));
        usluge.add(new Usluga(sledeciIdUsluge(), "Depilacija nogu voskom",
                "Depilacija celih nogu toplim voskom.", 40, 1500.0, KategorijaUsluge.DEPILACIJA));
        usluge.add(new Usluga(sledeciIdUsluge(), "Svečano šminkanje",
                "Šminkanje za svečane prilike uz postavljanje trepavica.", 60, 3000.0, KategorijaUsluge.SMINKANJE));

        // --- Kozmetičari ---
        kozmeticari.add(new Kozmeticar(sledeciIdKozmeticara(), "Ivana", "Marković", "Tretmani lica", 4.8));
        kozmeticari.add(new Kozmeticar(sledeciIdKozmeticara(), "Sandra", "Ilić", "Manikir i pedikir", 4.6));
        kozmeticari.add(new Kozmeticar(sledeciIdKozmeticara(), "Tijana", "Đorđević", "Masaže i nega tela", 4.9));
        kozmeticari.add(new Kozmeticar(sledeciIdKozmeticara(), "Nevena", "Pavlović", "Šminkanje i depilacija", 4.5));

        // --- Termini ---
        LocalDate danas = LocalDate.now();
        termini.add(new Termin(sledeciIdTermina(), LocalDateTime.of(danas, LocalTime.of(9, 0)),
                klijenti.get(0), usluge.get(0), kozmeticari.get(0), StatusTermina.ZAKAZAN,
                "Klijentkinja ima osetljivu kožu."));
        termini.add(new Termin(sledeciIdTermina(), LocalDateTime.of(danas, LocalTime.of(11, 30)),
                klijenti.get(1), usluge.get(3), kozmeticari.get(1), StatusTermina.ZAKAZAN,
                "Želi svetlo roze nijansu."));
        termini.add(new Termin(sledeciIdTermina(), LocalDateTime.of(danas, LocalTime.of(14, 0)),
                klijenti.get(2), usluge.get(2), kozmeticari.get(2), StatusTermina.ZAKAZAN, ""));
        termini.add(new Termin(sledeciIdTermina(), LocalDateTime.of(danas.plusDays(1), LocalTime.of(10, 0)),
                klijenti.get(3), usluge.get(5), kozmeticari.get(3), StatusTermina.ZAKAZAN, ""));
        termini.add(new Termin(sledeciIdTermina(), LocalDateTime.of(danas.plusDays(2), LocalTime.of(16, 0)),
                klijenti.get(0), usluge.get(1), kozmeticari.get(0), StatusTermina.ZAKAZAN,
                "Termin pred venčanje."));
        termini.add(new Termin(sledeciIdTermina(), LocalDateTime.of(danas.minusDays(1), LocalTime.of(12, 0)),
                klijenti.get(1), usluge.get(4), kozmeticari.get(1), StatusTermina.ZAVRSEN, ""));
        termini.add(new Termin(sledeciIdTermina(), LocalDateTime.of(danas.minusDays(2), LocalTime.of(13, 0)),
                klijenti.get(2), usluge.get(6), kozmeticari.get(3), StatusTermina.OTKAZAN,
                "Klijentkinja je otkazala dan ranije."));

        // --- Recenzije ---
        recenzije.add(new Recenzija(sledeciIdRecenzije(), "Marija J.", 5,
                "Presrećna sam kožom lica posle tretmana, Ivana je vrhunski profesionalac!",
                danas.minusDays(3), kozmeticari.get(0)));
        recenzije.add(new Recenzija(sledeciIdRecenzije(), "Ana P.", 4,
                "Gel lak je izdržao skoro mesec dana, jedino se malo čekalo na termin.",
                danas.minusDays(5), kozmeticari.get(1)));
        recenzije.add(new Recenzija(sledeciIdRecenzije(), "Jelena N.", 5,
                "Najbolja masaža u gradu, atmosfera u salonu je predivna.",
                danas.minusDays(1), kozmeticari.get(2)));
    }
}
