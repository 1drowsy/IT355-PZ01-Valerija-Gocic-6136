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
import rs.ac.metropolitan.it355.salon.model.Klijent;
import rs.ac.metropolitan.it355.salon.servis.KlijentServis;

@Controller
@RequestMapping("/klijenti")
public class KlijentKontroler {

    private final KlijentServis klijentServis;

    public KlijentKontroler(KlijentServis klijentServis) {
        this.klijentServis = klijentServis;
    }

    /** Lista klijenata sa pretragom po imenu, telefonu ili email adresi. */
    @GetMapping
    public String lista(@RequestParam(required = false) String pojam, Model model) {
        model.addAttribute("klijenti", klijentServis.pretrazi(pojam));
        model.addAttribute("pojam", pojam);
        return "klijenti/lista";
    }

    @GetMapping("/novi")
    public String novi(Model model) {
        model.addAttribute("klijent", new Klijent());
        return "klijenti/forma";
    }

    @GetMapping("/izmena/{id}")
    public String izmena(@PathVariable Long id, Model model, RedirectAttributes poruke) {
        Klijent klijent = klijentServis.pronadji(id);
        if (klijent == null) {
            poruke.addFlashAttribute("greska", "Traženi klijent ne postoji.");
            return "redirect:/klijenti";
        }
        model.addAttribute("klijent", klijent);
        return "klijenti/forma";
    }

    @PostMapping("/sacuvaj")
    public String sacuvaj(@Valid @ModelAttribute("klijent") Klijent klijent,
                          BindingResult rezultat, RedirectAttributes poruke) {

        // Ista email adresa ne sme da se pojavi kod dva klijenta
        if (klijent.getEmail() != null && !klijent.getEmail().isBlank()
                && klijentServis.postojiEmail(klijent.getEmail(), klijent.getId())) {
            rezultat.rejectValue("email", "duplikat", "Klijent sa ovom email adresom već postoji.");
        }
        if (rezultat.hasErrors()) {
            return "klijenti/forma";
        }

        boolean noviKlijent = klijent.getId() == null;
        klijentServis.sacuvaj(klijent);
        poruke.addFlashAttribute("uspeh", noviKlijent
                ? "Klijent je uspešno dodat."
                : "Podaci o klijentu su izmenjeni.");
        return "redirect:/klijenti";
    }

    @PostMapping("/obrisi/{id}")
    public String obrisi(@PathVariable Long id, RedirectAttributes poruke) {
        if (klijentServis.imaTermine(id)) {
            poruke.addFlashAttribute("greska",
                    "Klijent ne može da se obriše jer ima termine u evidenciji.");
        } else {
            klijentServis.obrisi(id);
            poruke.addFlashAttribute("uspeh", "Klijent je obrisan.");
        }
        return "redirect:/klijenti";
    }
}
