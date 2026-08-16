package rs.ac.metropolitan.it355.salon.servis;

import org.springframework.stereotype.Service;
import rs.ac.metropolitan.it355.salon.model.Kozmeticar;
import rs.ac.metropolitan.it355.salon.model.Recenzija;
import rs.ac.metropolitan.it355.salon.skladiste.Skladiste;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * Biznis sloj za rad sa recenzijama klijenata.
 * Nakon svake izmene recenzija ponovo se računa prosečna ocena kozmetičara.
 */
@Service
public class RecenzijaServis {

    private final Skladiste skladiste;
    private final KozmeticarServis kozmeticarServis;

    public RecenzijaServis(Skladiste skladiste, KozmeticarServis kozmeticarServis) {
        this.skladiste = skladiste;
        this.kozmeticarServis = kozmeticarServis;
    }

    /** Sve recenzije, najnovije na vrhu. */
    public List<Recenzija> sveRecenzije() {
        List<Recenzija> kopija = new ArrayList<>(skladiste.getRecenzije());
        kopija.sort(Comparator.comparing(Recenzija::getDatum).reversed());
        return kopija;
    }

    public Recenzija pronadji(Long id) {
        if (id == null) {
            return null;
        }
        for (Recenzija recenzija : skladiste.getRecenzije()) {
            if (recenzija.getId().equals(id)) {
                return recenzija;
            }
        }
        return null;
    }

    /** Filtriranje recenzija po kozmetičaru. */
    public List<Recenzija> poKozmeticaru(Long idKozmeticara) {
        if (idKozmeticara == null) {
            return sveRecenzije();
        }
        List<Recenzija> rezultat = new ArrayList<>();
        for (Recenzija recenzija : sveRecenzije()) {
            if (recenzija.getKozmeticar().getId().equals(idKozmeticara)) {
                rezultat.add(recenzija);
            }
        }
        return rezultat;
    }

    public void sacuvaj(Recenzija recenzija) {
        recenzija.setKozmeticar(kozmeticarServis.pronadji(recenzija.getKozmeticar().getId()));
        if (recenzija.getDatum() == null) {
            recenzija.setDatum(LocalDate.now());
        }
        if (recenzija.getId() == null) {
            recenzija.setId(skladiste.sledeciIdRecenzije());
            skladiste.getRecenzije().add(recenzija);
        } else {
            List<Recenzija> lista = skladiste.getRecenzije();
            for (int i = 0; i < lista.size(); i++) {
                if (lista.get(i).getId().equals(recenzija.getId())) {
                    lista.set(i, recenzija);
                    break;
                }
            }
        }
        osveziOcenuKozmeticara(recenzija.getKozmeticar());
    }

    public void obrisi(Long id) {
        Recenzija recenzija = pronadji(id);
        if (recenzija != null) {
            skladiste.getRecenzije().remove(recenzija);
            osveziOcenuKozmeticara(recenzija.getKozmeticar());
        }
    }

    /** Prosečna ocena kozmetičara, zaokružena na jednu decimalu. */
    public double prosecnaOcena(Long idKozmeticara) {
        List<Recenzija> recenzije = poKozmeticaru(idKozmeticara);
        if (recenzije.isEmpty()) {
            return 0;
        }
        int zbir = 0;
        for (Recenzija recenzija : recenzije) {
            zbir += recenzija.getOcena();
        }
        return Math.round((double) zbir / recenzije.size() * 10) / 10.0;
    }

    public int brojRecenzija() {
        return skladiste.getRecenzije().size();
    }

    /** Ocena kozmetičara se automatski usklađuje sa prosekom njegovih recenzija. */
    private void osveziOcenuKozmeticara(Kozmeticar kozmeticar) {
        if (kozmeticar == null) {
            return;
        }
        double prosek = prosecnaOcena(kozmeticar.getId());
        if (prosek > 0) {
            kozmeticar.setOcena(prosek);
        }
    }
}
