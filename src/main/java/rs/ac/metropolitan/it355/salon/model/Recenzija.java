package rs.ac.metropolitan.it355.salon.model;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;

/**
 * Recenzija koju klijent ostavlja za rad određenog kozmetičara.
 * Prosečna ocena svih recenzija koristi se za ažuriranje ocene kozmetičara.
 */
public class Recenzija {

    private Long id;

    @NotBlank(message = "Ime autora je obavezno.")
    @Size(min = 2, max = 40, message = "Ime autora mora imati između 2 i 40 karaktera.")
    private String autor;

    @NotNull(message = "Ocena je obavezna.")
    @Min(value = 1, message = "Najmanja ocena je 1.")
    @Max(value = 5, message = "Najveća ocena je 5.")
    private Integer ocena;

    @NotBlank(message = "Komentar je obavezan.")
    @Size(min = 5, max = 300, message = "Komentar mora imati između 5 i 300 karaktera.")
    private String komentar;

    private LocalDate datum;

    private Kozmeticar kozmeticar = new Kozmeticar();

    public Recenzija() {
    }

    public Recenzija(Long id, String autor, Integer ocena, String komentar, LocalDate datum, Kozmeticar kozmeticar) {
        this.id = id;
        this.autor = autor;
        this.ocena = ocena;
        this.komentar = komentar;
        this.datum = datum;
        this.kozmeticar = kozmeticar;
    }

    /** Pomoćna metoda za prikaz ocene zvezdicama u pogledu. */
    public String getZvezdice() {
        if (ocena == null) {
            return "";
        }
        return "★".repeat(ocena) + "☆".repeat(5 - ocena);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAutor() {
        return autor;
    }

    public void setAutor(String autor) {
        this.autor = autor;
    }

    public Integer getOcena() {
        return ocena;
    }

    public void setOcena(Integer ocena) {
        this.ocena = ocena;
    }

    public String getKomentar() {
        return komentar;
    }

    public void setKomentar(String komentar) {
        this.komentar = komentar;
    }

    public LocalDate getDatum() {
        return datum;
    }

    public void setDatum(LocalDate datum) {
        this.datum = datum;
    }

    public Kozmeticar getKozmeticar() {
        return kozmeticar;
    }

    public void setKozmeticar(Kozmeticar kozmeticar) {
        this.kozmeticar = kozmeticar;
    }
}
