package rs.ac.metropolitan.it355.salon.kontroler;

import jakarta.validation.Valid;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import rs.ac.metropolitan.it355.salon.model.Klijent;
import rs.ac.metropolitan.it355.salon.model.Kozmeticar;
import rs.ac.metropolitan.it355.salon.model.StatusTermina;
import rs.ac.metropolitan.it355.salon.model.Termin;
import rs.ac.metropolitan.it355.salon.model.Usluga;
import rs.ac.metropolitan.it355.salon.servis.KlijentServis;
import rs.ac.metropolitan.it355.salon.servis.KozmeticarServis;
import rs.ac.metropolitan.it355.salon.servis.TerminServis;
import rs.ac.metropolitan.it355.salon.servis.UslugaServis;

import java.time.LocalDate;
import java.util.List;

/**
 * Kontroler za rad sa terminima: pregled, filtriranje, zakazivanje,
 * izmena, otkazivanje i brisanje.
 */
@Controller
@RequestMapping("/termini")
public class TerminKontroler {

    private final TerminServis terminServis;
    private final KlijentServis klijentServis;
    private final UslugaServis uslugaServis;
    private final KozmeticarServis kozmeticarServis;

    public TerminKontroler(TerminServis terminServis, KlijentServis klijentServis,
                           UslugaServis uslugaServis, KozmeticarServis kozmeticarServis) {
        this.terminServis = terminServis;
        this.klijentServis = klijentServis;
        this.uslugaServis = uslugaServis;
        this.kozmeticarServis = kozmeticarServis;
    }

    /** Podaci potrebni svim padajućim listama na stranama termina. */
    @ModelAttribute("sviKlijenti")
    public List<Klijent> sviKlijenti() {
        return klijentServis.sviKlijenti();
    }

    @ModelAttribute("sveUsluge")
    public List<Usluga> sveUsluge() {
        return uslugaServis.sveUsluge();
    }

    @ModelAttribute("sviKozmeticari")
    public List<Kozmeticar> sviKozmeticari() {
        return kozmeticarServis.sviKozmeticari();
    }

    @ModelAttribute("sviStatusi")
    public StatusTermina[] sviStatusi() {
        return StatusTermina.values();
    }

    /**
     * Lista termina sa filterom po kozmetičaru, datumu i statusu.
     * Svi parametri filtera su opcioni.
     */
    @GetMapping
    public String lista(@RequestParam(required = false) Long kozmeticarId,
                        @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate datum,
                        @RequestParam(required = false) StatusTermina status,
                        Model model) {
        model.addAttribute("termini", terminServis.filtriraj(kozmeticarId, datum, status));
        model.addAttribute("kozmeticarId", kozmeticarId);
        model.addAttribute("datum", datum);
        model.addAttribute("status", status);
        return "termini/lista";
    }

    /** Prikaz prazne forme za zakazivanje novog termina. */
    @GetMapping("/novi")
    public String novi(Model model) {
        model.addAttribute("termin", new Termin());
        return "termini/forma";
    }

    /** Prikaz forme popunjene podacima postojećeg termina. */
    @GetMapping("/izmena/{id}")
    public String izmena(@PathVariable Long id, Model model, RedirectAttributes poruke) {
        Termin termin = terminServis.pronadji(id);
        if (termin == null) {
            poruke.addFlashAttribute("greska", "Traženi termin ne postoji.");
            return "redirect:/termini";
        }
        model.addAttribute("termin", termin);
        return "termini/forma";
    }

    /**
     * Snimanje novog ili izmenjenog termina.
     * Prvo se proverava validacija forme, a zatim i poslovna pravila salona.
     */
    @PostMapping("/sacuvaj")
    public String sacuvaj(@Valid @ModelAttribute("termin") Termin termin,
                          BindingResult rezultat, RedirectAttributes poruke) {

        // Provera da li su izabrane vrednosti u padajućim listama
        if (termin.getKlijent().getId() == null) {
            rezultat.rejectValue("klijent.id", "obavezno", "Izaberite klijenta.");
        }
        if (termin.getUsluga().getId() == null) {
            rezultat.rejectValue("usluga.id", "obavezno", "Izaberite uslugu.");
        }
        if (termin.getKozmeticar().getId() == null) {
            rezultat.rejectValue("kozmeticar.id", "obavezno", "Izaberite kozmetičara.");
        }
        if (rezultat.hasErrors()) {
            return "termini/forma";
        }

        // Umesto samo ID-jeva u termin se upisuju kompletni objekti
        terminServis.popuniVeze(termin);

        String greska = terminServis.proveriTermin(termin);
        if (greska != null) {
            rezultat.reject("poslovnoPravilo", greska);
            return "termini/forma";
        }

        boolean noviTermin = termin.getId() == null;
        terminServis.sacuvaj(termin);
        poruke.addFlashAttribute("uspeh", noviTermin
                ? "Termin je uspešno zakazan."
                : "Termin je uspešno izmenjen.");
        return "redirect:/termini";
    }

    /** Otkazivanje termina - termin ostaje u evidenciji sa statusom OTKAZAN. */
    @PostMapping("/otkazi/{id}")
    public String otkazi(@PathVariable Long id, RedirectAttributes poruke) {
        terminServis.otkazi(id);
        poruke.addFlashAttribute("uspeh", "Termin je otkazan.");
        return "redirect:/termini";
    }

    /** Označavanje termina kao završenog. */
    @PostMapping("/zavrsi/{id}")
    public String zavrsi(@PathVariable Long id, RedirectAttributes poruke) {
        terminServis.zavrsi(id);
        poruke.addFlashAttribute("uspeh", "Termin je označen kao završen.");
        return "redirect:/termini";
    }

    /** Trajno brisanje termina. */
    @PostMapping("/obrisi/{id}")
    public String obrisi(@PathVariable Long id, RedirectAttributes poruke) {
        terminServis.obrisi(id);
        poruke.addFlashAttribute("uspeh", "Termin je obrisan iz evidencije.");
        return "redirect:/termini";
    }
}
