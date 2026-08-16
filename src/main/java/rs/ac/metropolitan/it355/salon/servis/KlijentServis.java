package rs.ac.metropolitan.it355.salon.servis;

import org.springframework.stereotype.Service;
import rs.ac.metropolitan.it355.salon.model.Klijent;
import rs.ac.metropolitan.it355.salon.model.Termin;
import rs.ac.metropolitan.it355.salon.skladiste.Skladiste;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * Biznis sloj za rad sa klijentima.
 * Kontroleri nikada ne pristupaju skladištu direktno, već isključivo preko servisa.
 */
@Service
public class KlijentServis {

    private final Skladiste skladiste;

    public KlijentServis(Skladiste skladiste) {
        this.skladiste = skladiste;
    }

    /** Svi klijenti, sortirani po prezimenu pa po imenu. */
    public List<Klijent> sviKlijenti() {
        List<Klijent> kopija = new ArrayList<>(skladiste.getKlijenti());
        kopija.sort(Comparator.comparing(Klijent::getPrezime).thenComparing(Klijent::getIme));
        return kopija;
    }

    /** Pronalazi klijenta po ID-ju ili vraća null ako ne postoji. */
    public Klijent pronadji(Long id) {
        if (id == null) {
            return null;
        }
        for (Klijent klijent : skladiste.getKlijenti()) {
            if (klijent.getId().equals(id)) {
                return klijent;
            }
        }
        return null;
    }

    /** Pretraga po imenu, prezimenu, telefonu ili email adresi. */
    public List<Klijent> pretrazi(String pojam) {
        if (pojam == null || pojam.isBlank()) {
            return sviKlijenti();
        }
        String trazeno = pojam.toLowerCase().trim();
        List<Klijent> rezultat = new ArrayList<>();
        for (Klijent klijent : sviKlijenti()) {
            if (klijent.getIme().toLowerCase().contains(trazeno)
                    || klijent.getPrezime().toLowerCase().contains(trazeno)
                    || klijent.getTelefon().contains(trazeno)
                    || klijent.getEmail().toLowerCase().contains(trazeno)) {
                rezultat.add(klijent);
            }
        }
        return rezultat;
    }

    /** Da li email adresu već koristi neki drugi klijent. */
    public boolean postojiEmail(String email, Long idKlijentaKojiSeMenja) {
        for (Klijent klijent : skladiste.getKlijenti()) {
            boolean istiEmail = klijent.getEmail().equalsIgnoreCase(email);
            boolean drugiKlijent = !klijent.getId().equals(idKlijentaKojiSeMenja);
            if (istiEmail && drugiKlijent) {
                return true;
            }
        }
        return false;
    }

    /** Dodaje novog klijenta (kada je ID prazan) ili menja postojećeg. */
    public void sacuvaj(Klijent klijent) {
        if (klijent.getId() == null) {
            klijent.setId(skladiste.sledeciIdKlijenta());
            skladiste.getKlijenti().add(klijent);
        } else {
            List<Klijent> lista = skladiste.getKlijenti();
            for (int i = 0; i < lista.size(); i++) {
                if (lista.get(i).getId().equals(klijent.getId())) {
                    lista.set(i, klijent);
                    return;
                }
            }
        }
    }

    /** Klijent se ne sme obrisati ako ima zakazane termine. */
    public boolean imaTermine(Long idKlijenta) {
        for (Termin termin : skladiste.getTermini()) {
            if (termin.getKlijent() != null && termin.getKlijent().getId().equals(idKlijenta)) {
                return true;
            }
        }
        return false;
    }

    public void obrisi(Long id) {
        skladiste.getKlijenti().removeIf(klijent -> klijent.getId().equals(id));
    }

    public int brojKlijenata() {
        return skladiste.getKlijenti().size();
    }
}
