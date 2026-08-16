package rs.ac.metropolitan.it355.salon.model;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

/**
 * Klijent salona, odnosno osoba koja zakazuje termin.
 */
public class Klijent {

    private Long id;

    @NotBlank(message = "Ime je obavezno.")
    @Size(min = 2, max = 30, message = "Ime mora imati između 2 i 30 karaktera.")
    private String ime;

    @NotBlank(message = "Prezime je obavezno.")
    @Size(min = 2, max = 30, message = "Prezime mora imati između 2 i 30 karaktera.")
    private String prezime;

    @NotBlank(message = "Telefon je obavezan.")
    @Pattern(regexp = "^0[0-9]{8,9}$", message = "Telefon mora biti u formatu 06xxxxxxx (9 ili 10 cifara).")
    private String telefon;

    @NotBlank(message = "Email je obavezan.")
    @Email(message = "Email adresa nije ispravna.")
    private String email;

    public Klijent() {
    }

    public Klijent(Long id, String ime, String prezime, String telefon, String email) {
        this.id = id;
        this.ime = ime;
        this.prezime = prezime;
        this.telefon = telefon;
        this.email = email;
    }

    /** Pomoćna metoda za prikaz punog imena u tabelama i padajućim listama. */
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

    public String getTelefon() {
        return telefon;
    }

    public void setTelefon(String telefon) {
        this.telefon = telefon;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public String toString() {
        return getPunoIme();
    }
}
