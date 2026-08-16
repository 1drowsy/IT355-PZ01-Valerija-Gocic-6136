package rs.ac.metropolitan.it355.salon.model;

/**
 * Status u kojem se termin trenutno nalazi.
 * Koristi se i za filtriranje liste termina.
 */
public enum StatusTermina {

    ZAKAZAN("Zakazan"),
    ZAVRSEN("Završen"),
    OTKAZAN("Otkazan");

    private final String naziv;

    StatusTermina(String naziv) {
        this.naziv = naziv;
    }

    public String getNaziv() {
        return naziv;
    }
}
