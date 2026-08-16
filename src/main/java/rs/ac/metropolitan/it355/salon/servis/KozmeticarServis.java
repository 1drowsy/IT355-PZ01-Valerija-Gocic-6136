package rs.ac.metropolitan.it355.salon.servis;

import org.springframework.stereotype.Service;
import rs.ac.metropolitan.it355.salon.model.Kozmeticar;
import rs.ac.metropolitan.it355.salon.model.Termin;
import rs.ac.metropolitan.it355.salon.skladiste.Skladiste;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * Biznis sloj za rad sa kozmetičarima.
 */
@Service
public class KozmeticarServis {

    private final Skladiste skladiste;

    public KozmeticarServis(Skladiste skladiste) {
        this.skladiste = skladiste;
    }

    /** Svi kozmetičari, sortirani od najbolje ocenjenog. */
    public List<Kozmeticar> sviKozmeticari() {
        List<Kozmeticar> kopija = new ArrayList<>(skladiste.getKozmeticari());
        kopija.sort(Comparator.comparing(Kozmeticar::getOcena).reversed());
        return kopija;
    }

    public Kozmeticar pronadji(Long id) {
        if (id == null) {
            return null;
        }
        for (Kozmeticar kozmeticar : skladiste.getKozmeticari()) {
            if (kozmeticar.getId().equals(id)) {
                return kozmeticar;
            }
        }
        return null;
    }

    public void sacuvaj(Kozmeticar kozmeticar) {
        if (kozmeticar.getId() == null) {
            kozmeticar.setId(skladiste.sledeciIdKozmeticara());
            skladiste.getKozmeticari().add(kozmeticar);
        } else {
            List<Kozmeticar> lista = skladiste.getKozmeticari();
            for (int i = 0; i < lista.size(); i++) {
                if (lista.get(i).getId().equals(kozmeticar.getId())) {
                    lista.set(i, kozmeticar);
                    return;
                }
            }
        }
    }

    /** Kozmetičar koji ima zakazane termine ne sme da se obriše. */
    public boolean imaTermine(Long idKozmeticara) {
        for (Termin termin : skladiste.getTermini()) {
            if (termin.getKozmeticar() != null && termin.getKozmeticar().getId().equals(idKozmeticara)) {
                return true;
            }
        }
        return false;
    }

    public void obrisi(Long id) {
        skladiste.getKozmeticari().removeIf(kozmeticar -> kozmeticar.getId().equals(id));
    }

    public int brojKozmeticara() {
        return skladiste.getKozmeticari().size();
    }
}
