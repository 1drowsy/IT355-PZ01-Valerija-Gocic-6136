package rs.ac.metropolitan.it355.salon.servis;

import org.springframework.stereotype.Service;
import rs.ac.metropolitan.it355.salon.model.KategorijaUsluge;
import rs.ac.metropolitan.it355.salon.model.Termin;
import rs.ac.metropolitan.it355.salon.model.Usluga;
import rs.ac.metropolitan.it355.salon.skladiste.Skladiste;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * Biznis sloj za rad sa uslugama salona (kompletan CRUD + filtriranje i pretraga).
 */
@Service
public class UslugaServis {

    private final Skladiste skladiste;

    public UslugaServis(Skladiste skladiste) {
        this.skladiste = skladiste;
    }

    /** Sve usluge, sortirane po kategoriji pa po nazivu. */
    public List<Usluga> sveUsluge() {
        List<Usluga> kopija = new ArrayList<>(skladiste.getUsluge());
        kopija.sort(Comparator.comparing((Usluga u) -> u.getKategorija().getNaziv())
                .thenComparing(Usluga::getNaziv));
        return kopija;
    }

    public Usluga pronadji(Long id) {
        if (id == null) {
            return null;
        }
        for (Usluga usluga : skladiste.getUsluge()) {
            if (usluga.getId().equals(id)) {
                return usluga;
            }
        }
        return null;
    }

    /**
     * Filtriranje liste usluga po kategoriji i pretraga po delu naziva.
     * Oba parametra su opciona - ako nisu prosleđeni, filter se ne primenjuje.
     */
    public List<Usluga> filtriraj(KategorijaUsluge kategorija, String pojam) {
        List<Usluga> rezultat = new ArrayList<>();
        for (Usluga usluga : sveUsluge()) {
            boolean odgovaraKategoriji = (kategorija == null) || usluga.getKategorija() == kategorija;
            boolean odgovaraPojmu = (pojam == null || pojam.isBlank())
                    || usluga.getNaziv().toLowerCase().contains(pojam.toLowerCase().trim());
            if (odgovaraKategoriji && odgovaraPojmu) {
                rezultat.add(usluga);
            }
        }
        return rezultat;
    }

    /** Usluge jedne kategorije - koristi se na početnoj strani (cenovnik). */
    public List<Usluga> poKategoriji(KategorijaUsluge kategorija) {
        return filtriraj(kategorija, null);
    }

    /** Provera da li već postoji usluga sa istim nazivom (bez obzira na velika/mala slova). */
    public boolean postojiNaziv(String naziv, Long idUslugeKojaSeMenja) {
        for (Usluga usluga : skladiste.getUsluge()) {
            boolean istiNaziv = usluga.getNaziv().equalsIgnoreCase(naziv.trim());
            boolean drugaUsluga = !usluga.getId().equals(idUslugeKojaSeMenja);
            if (istiNaziv && drugaUsluga) {
                return true;
            }
        }
        return false;
    }

    public void sacuvaj(Usluga usluga) {
        if (usluga.getId() == null) {
            usluga.setId(skladiste.sledeciIdUsluge());
            skladiste.getUsluge().add(usluga);
        } else {
            List<Usluga> lista = skladiste.getUsluge();
            for (int i = 0; i < lista.size(); i++) {
                if (lista.get(i).getId().equals(usluga.getId())) {
                    lista.set(i, usluga);
                    return;
                }
            }
        }
    }

    /** Usluga koja je vezana za neki termin ne sme da se obriše. */
    public boolean koristiSeUTerminima(Long idUsluge) {
        for (Termin termin : skladiste.getTermini()) {
            if (termin.getUsluga() != null && termin.getUsluga().getId().equals(idUsluge)) {
                return true;
            }
        }
        return false;
    }

    public void obrisi(Long id) {
        skladiste.getUsluge().removeIf(usluga -> usluga.getId().equals(id));
    }

    public int brojUsluga() {
        return skladiste.getUsluge().size();
    }
}
