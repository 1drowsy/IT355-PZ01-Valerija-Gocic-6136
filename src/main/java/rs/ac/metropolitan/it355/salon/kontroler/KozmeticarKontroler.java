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
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import rs.ac.metropolitan.it355.salon.model.Kozmeticar;
import rs.ac.metropolitan.it355.salon.servis.KozmeticarServis;
import rs.ac.metropolitan.it355.salon.servis.RecenzijaServis;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Kontroler za pregled i održavanje podataka o kozmetičarima.
 */
@Controller
@RequestMapping("/kozmeticari")
public class KozmeticarKontroler {

    private final KozmeticarServis kozmeticarServis;
    private final RecenzijaServis recenzijaServis;

    public KozmeticarKontroler(KozmeticarServis kozmeticarServis, RecenzijaServis recenzijaServis) {
        this.kozmeticarServis = kozmeticarServis;
        this.recenzijaServis = recenzijaServis;
    }

    @GetMapping
    public String lista(Model model) {
        List<Kozmeticar> kozmeticari = kozmeticarServis.sviKozmeticari();

        // Uz svakog kozmetičara prikazuje se i broj njegovih recenzija
        Map<Long, Integer> brojRecenzija = new HashMap<>();
        for (Kozmeticar kozmeticar : kozmeticari) {
            brojRecenzija.put(kozmeticar.getId(), recenzijaServis.poKozmeticaru(kozmeticar.getId()).size());
        }

        model.addAttribute("kozmeticari", kozmeticari);
        model.addAttribute("brojRecenzija", brojRecenzija);
        return "kozmeticari/lista";
    }

    @GetMapping("/novi")
    public String novi(Model model) {
        model.addAttribute("kozmeticar", new Kozmeticar());
        return "kozmeticari/forma";
    }

    @GetMapping("/izmena/{id}")
    public String izmena(@PathVariable Long id, Model model, RedirectAttributes poruke) {
        Kozmeticar kozmeticar = kozmeticarServis.pronadji(id);
        if (kozmeticar == null) {
            poruke.addFlashAttribute("greska", "Traženi kozmetičar ne postoji.");
            return "redirect:/kozmeticari";
        }
        model.addAttribute("kozmeticar", kozmeticar);
        return "kozmeticari/forma";
    }

    @PostMapping("/sacuvaj")
    public String sacuvaj(@Valid @ModelAttribute("kozmeticar") Kozmeticar kozmeticar,
                          BindingResult rezultat, RedirectAttributes poruke) {
        if (rezultat.hasErrors()) {
            return "kozmeticari/forma";
        }
        boolean novi = kozmeticar.getId() == null;
        kozmeticarServis.sacuvaj(kozmeticar);
        poruke.addFlashAttribute("uspeh", novi
                ? "Kozmetičar je uspešno dodat."
                : "Podaci o kozmetičaru su izmenjeni.");
        return "redirect:/kozmeticari";
    }

    @PostMapping("/obrisi/{id}")
    public String obrisi(@PathVariable Long id, RedirectAttributes poruke) {
        if (kozmeticarServis.imaTermine(id)) {
            poruke.addFlashAttribute("greska",
                    "Kozmetičar ne može da se obriše jer ima termine u evidenciji.");
        } else {
            kozmeticarServis.obrisi(id);
            poruke.addFlashAttribute("uspeh", "Kozmetičar je obrisan.");
        }
        return "redirect:/kozmeticari";
    }
}
