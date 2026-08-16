package rs.ac.metropolitan.it355.salon.kontroler;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import rs.ac.metropolitan.it355.salon.model.StatusTermina;
import rs.ac.metropolitan.it355.salon.servis.KlijentServis;
import rs.ac.metropolitan.it355.salon.servis.KozmeticarServis;
import rs.ac.metropolitan.it355.salon.servis.RecenzijaServis;
import rs.ac.metropolitan.it355.salon.servis.TerminServis;
import rs.ac.metropolitan.it355.salon.servis.UslugaServis;

/**
 * Kontroler početne strane i strane "O salonu".
 */
@Controller
public class PocetnaKontroler {

    private final TerminServis terminServis;
    private final UslugaServis uslugaServis;
    private final KlijentServis klijentServis;
    private final KozmeticarServis kozmeticarServis;
    private final RecenzijaServis recenzijaServis;

    public PocetnaKontroler(TerminServis terminServis, UslugaServis uslugaServis,
                            KlijentServis klijentServis, KozmeticarServis kozmeticarServis,
                            RecenzijaServis recenzijaServis) {
        this.terminServis = terminServis;
        this.uslugaServis = uslugaServis;
        this.klijentServis = klijentServis;
        this.kozmeticarServis = kozmeticarServis;
        this.recenzijaServis = recenzijaServis;
    }

    /** Početna strana sa kratkim pregledom stanja u salonu. */
    @GetMapping("/")
    public String pocetna(Model model) {
        model.addAttribute("brojKlijenata", klijentServis.brojKlijenata());
        model.addAttribute("brojUsluga", uslugaServis.brojUsluga());
        model.addAttribute("brojKozmeticara", kozmeticarServis.brojKozmeticara());
        model.addAttribute("brojZakazanih", terminServis.brojPoStatusu(StatusTermina.ZAKAZAN));
        model.addAttribute("terminiDanas", terminServis.terminiZaDanas());
        model.addAttribute("predstojeci", terminServis.predstojeciTermini(5));
        model.addAttribute("recenzije", recenzijaServis.sveRecenzije());
        return "pocetna";
    }

    /** Statična strana sa informacijama o salonu. */
    @GetMapping("/o-nama")
    public String oNama(Model model) {
        model.addAttribute("kozmeticari", kozmeticarServis.sviKozmeticari());
        model.addAttribute("prihod", terminServis.ukupanPrihod());
        model.addAttribute("brojZavrsenih", terminServis.brojPoStatusu(StatusTermina.ZAVRSEN));
        model.addAttribute("brojOtkazanih", terminServis.brojPoStatusu(StatusTermina.OTKAZAN));
        return "o-nama";
    }
}
