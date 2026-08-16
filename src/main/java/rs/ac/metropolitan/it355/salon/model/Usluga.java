package rs.ac.metropolitan.it355.salon.model;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

/**
 * Usluga koju salon nudi (npr. tretman lica, manikir...).
 */
public class Usluga {

    private Long id;

    @NotBlank(message = "Naziv usluge je obavezan.")
    @Size(min = 3, max = 50, message = "Naziv mora imati između 3 i 50 karaktera.")
    private String naziv;

    @NotBlank(message = "Opis usluge je obavezan.")
    @Size(max = 200, message = "Opis može imati najviše 200 karaktera.")
    private String opis;

    @NotNull(message = "Trajanje je obavezno.")
    @Min(value = 15, message = "Usluga mora trajati najmanje 15 minuta.")
    @Max(value = 300, message = "Usluga može trajati najviše 300 minuta.")
    private Integer trajanjeUMinutima;

    @NotNull(message = "Cena je obavezna.")
    @Min(value = 100, message = "Cena mora biti najmanje 100 RSD.")
    @Max(value = 100000, message = "Cena može biti najviše 100000 RSD.")
    private Double cena;

    @NotNull(message = "Kategorija je obavezna.")
    private KategorijaUsluge kategorija;

    public Usluga() {
    }

    public Usluga(Long id, String naziv, String opis, Integer trajanjeUMinutima, Double cena, KategorijaUsluge kategorija) {
        this.id = id;
        this.naziv = naziv;
        this.opis = opis;
        this.trajanjeUMinutima = trajanjeUMinutima;
        this.cena = cena;
        this.kategorija = kategorija;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNaziv() {
        return naziv;
    }

    public void setNaziv(String naziv) {
        this.naziv = naziv;
    }

    public String getOpis() {
        return opis;
    }

    public void setOpis(String opis) {
        this.opis = opis;
    }

    public Integer getTrajanjeUMinutima() {
        return trajanjeUMinutima;
    }

    public void setTrajanjeUMinutima(Integer trajanjeUMinutima) {
        this.trajanjeUMinutima = trajanjeUMinutima;
    }

    public Double getCena() {
        return cena;
    }

    public void setCena(Double cena) {
        this.cena = cena;
    }

    public KategorijaUsluge getKategorija() {
        return kategorija;
    }

    public void setKategorija(KategorijaUsluge kategorija) {
        this.kategorija = kategorija;
    }

    @Override
    public String toString() {
        return naziv;
    }
}
