package rs.ac.metropolitan.it355.salon.model;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

/**
 * Zakazani termin - centralni entitet aplikacije.
 * Povezuje klijenta, uslugu i kozmetičara u određenom datumu i vremenu.
 */
public class Termin {

    private Long id;

    @NotNull(message = "Datum i vreme su obavezni.")
    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm")
    private LocalDateTime datumIVreme;

    /**
     * Veze ka ostalim modelima. U formi se popunjava samo ugnežđeni id
     * (npr. th:field="*{klijent.id}"), a servisni sloj zatim učitava ceo objekat.
     */
    private Klijent klijent = new Klijent();

    private Usluga usluga = new Usluga();

    private Kozmeticar kozmeticar = new Kozmeticar();

    @NotNull(message = "Status termina je obavezan.")
    private StatusTermina status = StatusTermina.ZAKAZAN;

    @Size(max = 200, message = "Napomena može imati najviše 200 karaktera.")
    private String napomena;

    public Termin() {
    }

    public Termin(Long id, LocalDateTime datumIVreme, Klijent klijent, Usluga usluga,
                  Kozmeticar kozmeticar, StatusTermina status, String napomena) {
        this.id = id;
        this.datumIVreme = datumIVreme;
        this.klijent = klijent;
        this.usluga = usluga;
        this.kozmeticar = kozmeticar;
        this.status = status;
        this.napomena = napomena;
    }

    /**
     * Vreme završetka termina, izračunato na osnovu trajanja izabrane usluge.
     * Koristi se za proveru da li se dva termina istog kozmetičara preklapaju.
     */
    public LocalDateTime getKrajTermina() {
        if (datumIVreme == null || usluga == null || usluga.getTrajanjeUMinutima() == null) {
            return datumIVreme;
        }
        return datumIVreme.plusMinutes(usluga.getTrajanjeUMinutima());
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDateTime getDatumIVreme() {
        return datumIVreme;
    }

    public void setDatumIVreme(LocalDateTime datumIVreme) {
        this.datumIVreme = datumIVreme;
    }

    public Klijent getKlijent() {
        return klijent;
    }

    public void setKlijent(Klijent klijent) {
        this.klijent = klijent;
    }

    public Usluga getUsluga() {
        return usluga;
    }

    public void setUsluga(Usluga usluga) {
        this.usluga = usluga;
    }

    public Kozmeticar getKozmeticar() {
        return kozmeticar;
    }

    public void setKozmeticar(Kozmeticar kozmeticar) {
        this.kozmeticar = kozmeticar;
    }

    public StatusTermina getStatus() {
        return status;
    }

    public void setStatus(StatusTermina status) {
        this.status = status;
    }

    public String getNapomena() {
        return napomena;
    }

    public void setNapomena(String napomena) {
        this.napomena = napomena;
    }
}
