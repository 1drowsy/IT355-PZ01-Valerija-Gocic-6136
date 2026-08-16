package rs.ac.metropolitan.it355.salon.model;

/**
 * Kategorija kojoj pripada usluga salona.
 * Nabrajanje (enum) je korišćeno zato što je skup kategorija unapred poznat i ne menja se.
 */
public enum KategorijaUsluge {

    LICE("Nega lica"),
    TELO("Nega tela"),
    NOKTI("Manikir i pedikir"),
    DEPILACIJA("Depilacija"),
    SMINKANJE("Šminkanje");

    /** Naziv koji se prikazuje korisniku u pogledima. */
    private final String naziv;

    KategorijaUsluge(String naziv) {
        this.naziv = naziv;
    }

    public String getNaziv() {
        return naziv;
    }
}
