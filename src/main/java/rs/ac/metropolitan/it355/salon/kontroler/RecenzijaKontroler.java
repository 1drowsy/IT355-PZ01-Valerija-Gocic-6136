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
import rs.ac.metropolitan.it355.salon.model.Kozmeticar;
import rs.ac.metropolitan.it355.salon.model.Recenzija;
import rs.ac.metropolitan.it355.salon.servis.KozmeticarServis;
import rs.ac.metropolitan.it355.salon.servis.RecenzijaServis;

import java.util.List;

/**
 * Kontroler za recenzije klijenata o radu kozmetičara.
 */
@Controller
@RequestMapping("/recenzije")
public class RecenzijaKontroler {

    private final RecenzijaServis recenzijaServis;
    private final KozmeticarServis kozmeticarServis;

    public RecenzijaKontroler(RecenzijaServis recenzijaServis, KozmeticarServis kozmeticarServis) {
        this.recenzijaServis = recenzijaServis;
        this.kozmeticarServis = kozmeticarServis;
    }

    @ModelAttribute("sviKozmeticari")
    public List<Kozmeticar> sviKozmeticari() {
        return kozmeticarServis.sviKozmeticari();
    }

    /** Lista recenzija sa filterom po kozmetičaru. */
    @GetMapping
    public String lista(@RequestParam(required = false) Long kozmeticarId, Model model) {
        model.addAttribute("recenzije", recenzijaServis.poKozmeticaru(kozmeticarId));
        model.addAttribute("kozmeticarId", kozmeticarId);
        return "recenzije/lista";
    }

    @GetMapping("/nova")
    public String nova(Model model) {
        model.addAttribute("recenzija", new Recenzija());
        return "recenzije/forma";
    }

    @PostMapping("/sacuvaj")
    public String sacuvaj(@Valid @ModelAttribute("recenzija") Recenzija recenzija,
                          BindingResult rezultat, RedirectAttributes poruke) {
        if (recenzija.getKozmeticar().getId() == null) {
            rezultat.rejectValue("kozmeticar.id", "obavezno", "Izaberite kozmetičara.");
        }
        if (rezultat.hasErrors()) {
            return "recenzije/forma";
        }
        recenzijaServis.sacuvaj(recenzija);
        poruke.addFlashAttribute("uspeh", "Hvala na recenziji!");
        return "redirect:/recenzije";
    }

    @PostMapping("/obrisi/{id}")
    public String obrisi(@PathVariable Long id, RedirectAttributes poruke) {
        recenzijaServis.obrisi(id);
        poruke.addFlashAttribute("uspeh", "Recenzija je obrisana.");
        return "redirect:/recenzije";
    }
}
