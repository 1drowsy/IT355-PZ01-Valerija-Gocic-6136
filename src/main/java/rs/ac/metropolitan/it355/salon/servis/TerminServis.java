package rs.ac.metropolitan.it355.salon.servis;

import org.springframework.stereotype.Service;
import rs.ac.metropolitan.it355.salon.model.StatusTermina;
import rs.ac.metropolitan.it355.salon.model.Termin;
import rs.ac.metropolitan.it355.salon.skladiste.Skladiste;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * Biznis sloj za rad sa terminima - najvažniji servis u aplikaciji.
 *
 * Pored običnog CRUD-a, ovde se nalaze i poslovna pravila salona:
 * radno vreme, zabrana zakazivanja u prošlosti i provera preklapanja termina
 * kod istog kozmetičara.
 */
@Service
public class TerminServis {

    /** Salon radi svakog dana od 08:00 do 20:00. */
    private static final LocalTime POCETAK_RADNOG_VREMENA = LocalTime.of(8, 0);
    private static final LocalTime KRAJ_RADNOG_VREMENA = LocalTime.of(20, 0);

    private final Skladiste skladiste;
    private final KlijentServis klijentServis;
    private final UslugaServis uslugaServis;
    private final KozmeticarServis kozmeticarServis;

    public TerminServis(Skladiste skladiste, KlijentServis klijentServis,
                        UslugaServis uslugaServis, KozmeticarServis kozmeticarServis) {
        this.skladiste = skladiste;
        this.klijentServis = klijentServis;
        this.uslugaServis = uslugaServis;
        this.kozmeticarServis = kozmeticarServis;
    }

    // ------------------------------------------------------------------
    // Čitanje podataka
    // ------------------------------------------------------------------

    /** Svi termini poređani hronološki. */
    public List<Termin> sviTermini() {
        List<Termin> kopija = new ArrayList<>(skladiste.getTermini());
        kopija.sort(Comparator.comparing(Termin::getDatumIVreme));
        return kopija;
    }

    public Termin pronadji(Long id) {
        if (id == null) {
            return null;
        }
        for (Termin termin : skladiste.getTermini()) {
            if (termin.getId().equals(id)) {
                return termin;
            }
        }
        return null;
    }

    /**
     * Filtriranje termina po kozmetičaru, datumu i statusu.
     * Parametri koji nisu izabrani u formi stižu kao null i tada se preskaču.
     */
    public List<Termin> filtriraj(Long idKozmeticara, LocalDate datum, StatusTermina status) {
        List<Termin> rezultat = new ArrayList<>();
        for (Termin termin : sviTermini()) {
            boolean odgovaraKozmeticar = (idKozmeticara == null)
                    || termin.getKozmeticar().getId().equals(idKozmeticara);
            boolean odgovaraDatum = (datum == null)
                    || termin.getDatumIVreme().toLocalDate().equals(datum);
            boolean odgovaraStatus = (status == null) || termin.getStatus() == status;

            if (odgovaraKozmeticar && odgovaraDatum && odgovaraStatus) {
                rezultat.add(termin);
            }
        }
        return rezultat;
    }

    /** Termini zakazani za današnji dan - prikazuju se na početnoj strani. */
    public List<Termin> terminiZaDanas() {
        return filtriraj(null, LocalDate.now(), null);
    }

    /** Nekoliko narednih zakazanih termina. */
    public List<Termin> predstojeciTermini(int koliko) {
        List<Termin> rezultat = new ArrayList<>();
        for (Termin termin : sviTermini()) {
            boolean uBuducnosti = termin.getDatumIVreme().isAfter(LocalDateTime.now());
            if (uBuducnosti && termin.getStatus() == StatusTermina.ZAKAZAN) {
                rezultat.add(termin);
            }
            if (rezultat.size() == koliko) {
                break;
            }
        }
        return rezultat;
    }

    public int brojPoStatusu(StatusTermina status) {
        return filtriraj(null, null, status).size();
    }

    /** Ukupan prihod od svih završenih termina. */
    public double ukupanPrihod() {
        double zbir = 0;
        for (Termin termin : skladiste.getTermini()) {
            if (termin.getStatus() == StatusTermina.ZAVRSEN && termin.getUsluga() != null) {
                zbir += termin.getUsluga().getCena();
            }
        }
        return zbir;
    }

    // ------------------------------------------------------------------
    // Poslovna pravila
    // ------------------------------------------------------------------

    /**
     * Iz forme stižu samo ID-jevi izabranog klijenta, usluge i kozmetičara,
     * pa se ovde učitavaju kompletni objekti iz skladišta.
     */
    public void popuniVeze(Termin termin) {
        termin.setKlijent(klijentServis.pronadji(termin.getKlijent().getId()));
        termin.setUsluga(uslugaServis.pronadji(termin.getUsluga().getId()));
        termin.setKozmeticar(kozmeticarServis.pronadji(termin.getKozmeticar().getId()));
    }

    /**
     * Provera poslovnih pravila pre snimanja termina.
     *
     * @return tekst greške ili null ako je termin ispravan
     */
    public String proveriTermin(Termin termin) {
        LocalDateTime pocetak = termin.getDatumIVreme();
        LocalDateTime kraj = termin.getKrajTermina();

        // Novi termin ne može da se zakaže u prošlosti (postojeći se sme ispravljati).
        if (termin.getId() == null && pocetak.isBefore(LocalDateTime.now())) {
            return "Termin ne može da se zakaže u prošlosti.";
        }

        // Termin mora u celosti da stane u radno vreme salona.
        if (pocetak.toLocalTime().isBefore(POCETAK_RADNOG_VREMENA)
                || kraj.toLocalTime().isAfter(KRAJ_RADNOG_VREMENA)
                || !kraj.toLocalDate().equals(pocetak.toLocalDate())) {
            return "Salon radi od 08:00 do 20:00, a izabrana usluga traje "
                    + termin.getUsluga().getTrajanjeUMinutima() + " minuta.";
        }

        // Isti kozmetičar ne sme da ima dva termina koja se preklapaju.
        for (Termin postojeci : skladiste.getTermini()) {
            boolean istiTermin = postojeci.getId().equals(termin.getId());
            boolean otkazan = postojeci.getStatus() == StatusTermina.OTKAZAN;
            boolean istiKozmeticar = postojeci.getKozmeticar().getId().equals(termin.getKozmeticar().getId());

            if (istiTermin || otkazan || !istiKozmeticar) {
                continue;
            }
            boolean preklapaSe = pocetak.isBefore(postojeci.getKrajTermina())
                    && postojeci.getDatumIVreme().isBefore(kraj);
            if (preklapaSe) {
                return "Kozmetičar " + termin.getKozmeticar().getPunoIme()
                        + " već ima zakazan termin u tom periodu.";
            }
        }
        return null;
    }

    // ------------------------------------------------------------------
    // Izmena podataka
    // ------------------------------------------------------------------

    public void sacuvaj(Termin termin) {
        if (termin.getId() == null) {
            termin.setId(skladiste.sledeciIdTermina());
            skladiste.getTermini().add(termin);
        } else {
            List<Termin> lista = skladiste.getTermini();
            for (int i = 0; i < lista.size(); i++) {
                if (lista.get(i).getId().equals(termin.getId())) {
                    lista.set(i, termin);
                    return;
                }
            }
        }
    }

    /** Otkazivanje termina - termin ostaje u evidenciji, samo menja status. */
    public void otkazi(Long id) {
        Termin termin = pronadji(id);
        if (termin != null) {
            termin.setStatus(StatusTermina.OTKAZAN);
        }
    }

    /** Označavanje termina kao završenog nakon obavljene usluge. */
    public void zavrsi(Long id) {
        Termin termin = pronadji(id);
        if (termin != null) {
            termin.setStatus(StatusTermina.ZAVRSEN);
        }
    }

    /** Trajno brisanje termina iz evidencije. */
    public void obrisi(Long id) {
        skladiste.getTermini().removeIf(termin -> termin.getId().equals(id));
    }
}
