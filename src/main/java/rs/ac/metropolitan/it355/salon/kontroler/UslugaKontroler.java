package rs.ac.metropolitan.it355.salon.kontroler;

import jakarta.validation.Valid;
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
import rs.ac.metropolitan.it355.salon.model.KategorijaUsluge;
import rs.ac.metropolitan.it355.salon.model.Usluga;
import rs.ac.metropolitan.it355.salon.servis.UslugaServis;

/**
 * Kontroler za kompletan CRUD nad uslugama salona,
 * uz filtriranje po kategoriji i pretragu po nazivu.
 */
@Controller
@RequestMapping("/usluge")
public class UslugaKontroler {

    private final UslugaServis uslugaServis;

    public UslugaKontroler(UslugaServis uslugaServis) {
        this.uslugaServis = uslugaServis;
    }

    /** Sve kategorije se koriste i u filteru i u formi. */
    @ModelAttribute("sveKategorije")
    public KategorijaUsluge[] sveKategorije() {
        return KategorijaUsluge.values();
    }

    /** Lista usluga sa filterom po kategoriji i pretragom po nazivu. */
    @GetMapping
    public String lista(@RequestParam(required = false) KategorijaUsluge kategorija,
                        @RequestParam(required = false) String pojam,
                        Model model) {
        model.addAttribute("usluge", uslugaServis.filtriraj(kategorija, pojam));
        model.addAttribute("kategorija", kategorija);
        model.addAttribute("pojam", pojam);
        return "usluge/lista";
    }

    @GetMapping("/nova")
    public String nova(Model model) {
        model.addAttribute("usluga", new Usluga());
        return "usluge/forma";
    }

    @GetMapping("/izmena/{id}")
    public String izmena(@PathVariable Long id, Model model, RedirectAttributes poruke) {
        Usluga usluga = uslugaServis.pronadji(id);
        if (usluga == null) {
            poruke.addFlashAttribute("greska", "Tražena usluga ne postoji.");
            return "redirect:/usluge";
        }
        model.addAttribute("usluga", usluga);
        return "usluge/forma";
    }

    @PostMapping("/sacuvaj")
    public String sacuvaj(@Valid @ModelAttribute("usluga") Usluga usluga,
                          BindingResult rezultat, RedirectAttributes poruke) {

        // Naziv usluge mora da bude jedinstven
        if (usluga.getNaziv() != null && !usluga.getNaziv().isBlank()
                && uslugaServis.postojiNaziv(usluga.getNaziv(), usluga.getId())) {
            rezultat.rejectValue("naziv", "duplikat", "Usluga sa ovim nazivom već postoji.");
        }
        if (rezultat.hasErrors()) {
            return "usluge/forma";
        }

        boolean novaUsluga = usluga.getId() == null;
        uslugaServis.sacuvaj(usluga);
        poruke.addFlashAttribute("uspeh", novaUsluga
                ? "Usluga je uspešno dodata."
                : "Usluga je uspešno izmenjena.");
        return "redirect:/usluge";
    }

    @PostMapping("/obrisi/{id}")
    public String obrisi(@PathVariable Long id, RedirectAttributes poruke) {
        if (uslugaServis.koristiSeUTerminima(id)) {
            poruke.addFlashAttribute("greska",
                    "Usluga ne može da se obriše jer je vezana za postojeće termine.");
        } else {
            uslugaServis.obrisi(id);
            poruke.addFlashAttribute("uspeh", "Usluga je obrisana.");
        }
        return "redirect:/usluge";
    }
}
