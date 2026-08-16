package rs.ac.metropolitan.it355.salon.model;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

/**
 * Kozmetičar koji izvršava uslugu u zakazanom terminu.
 */
public class Kozmeticar {

    private Long id;

    @NotBlank(message = "Ime je obavezno.")
    @Size(min = 2, max = 30, message = "Ime mora imati između 2 i 30 karaktera.")
    private String ime;

    @NotBlank(message = "Prezime je obavezno.")
    @Size(min = 2, max = 30, message = "Prezime mora imati između 2 i 30 karaktera.")
    private String prezime;

    @NotBlank(message = "Specijalnost je obavezna.")
    @Size(max = 60, message = "Specijalnost može imati najviše 60 karaktera.")
    private String specijalnost;

    @NotNull(message = "Ocena je obavezna.")
    @DecimalMin(value = "1.0", message = "Ocena ne može biti manja od 1.0.")
    @DecimalMax(value = "5.0", message = "Ocena ne može biti veća od 5.0.")
    private Double ocena;

    public Kozmeticar() {
    }

    public Kozmeticar(Long id, String ime, String prezime, String specijalnost, Double ocena) {
        this.id = id;
        this.ime = ime;
        this.prezime = prezime;
        this.specijalnost = specijalnost;
        this.ocena = ocena;
    }

    public String getPunoIme() {
        return ime + " " + prezime;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getIme() {
        return ime;
    }

    public void setIme(String ime) {
        this.ime = ime;
    }

    public String getPrezime() {
        return prezime;
    }

    public void setPrezime(String prezime) {
        this.prezime = prezime;
    }

    public String getSpecijalnost() {
        return specijalnost;
    }

    public void setSpecijalnost(String specijalnost) {
        this.specijalnost = specijalnost;
    }

    public Double getOcena() {
        return ocena;
    }

    public void setOcena(Double ocena) {
        this.ocena = ocena;
    }

    @Override
    public String toString() {
        return getPunoIme();
    }
}
