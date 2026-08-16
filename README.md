# Sistem za rezervaciju termina u kozmetičkom salonu

Spring Boot MVC web aplikacija za vođenje evidencije termina, usluga, klijenata,
kozmetičara i recenzija u kozmetičkom salonu „Bella Vita". Podaci se, umesto u bazi,
čuvaju u **application scope-u** (in-memory skladište).

---

## Tehnologije

| Tehnologija | Verzija | Uloga |
|---|---|---|
| Java | 21 | Programski jezik |
| Spring Boot | 3.3.5 | Osnovni radni okvir |
| Spring MVC | 6.x | Kontrolerski sloj, mapiranje zahteva |
| Thymeleaf | 3.1 | Nivo pogleda (HTML šabloni) |
| Jakarta Bean Validation | 3.x | Validacija formi |
| CSS | – | Ručno pisani stil (`static/css/style.css`) |
| Maven | 3.9.9 (wrapper) | Build alat |

---

## Pokretanje

Potrebni su **JDK 21** i internet konekcija pri prvom pokretanju (Maven preuzima biblioteke).

```bash
# Windows
mvnw.cmd spring-boot:run

# Linux / macOS
./mvnw spring-boot:run
```

Aplikacija se pokreće na adresi **http://localhost:8080**.

Pokretanje testova:

```bash
mvnw.cmd test
```

Pravljenje izvršnog JAR fajla:

```bash
mvnw.cmd clean package
java -jar target/kozmeticki-salon.jar
```

---

## Funkcionalnosti

- **Termini** – kompletan CRUD: zakazivanje, izmena, otkazivanje (promena statusa) i brisanje.
- **Usluge** – kompletan CRUD uz filtriranje po kategoriji i pretragu po nazivu.
- **Klijenti** – kompletan CRUD uz pretragu po imenu, telefonu i email adresi.
- **Kozmetičari** – kompletan CRUD i prikaz prosečne ocene.
- **Recenzije** – dodavanje, brisanje i filtriranje po kozmetičaru; ocena kozmetičara se
  automatski usklađuje sa prosekom recenzija.
- **Validacija** – anotacije Bean Validation na modelima + poslovna pravila u servisnom sloju
  (radno vreme salona, zabrana zakazivanja u prošlosti, provera preklapanja termina).

---

## Struktura projekta

```
IT355-PZ01-Valerija-Gocic-6136/
├── pom.xml
├── mvnw / mvnw.cmd
├── README.md
├── DOKUMENTACIJA.md
├── PLAN-COMMITOVA.md
└── src/
    ├── main/
    │   ├── java/rs/ac/metropolitan/it355/salon/
    │   │   ├── SalonApplication.java
    │   │   ├── model/
    │   │   ├── skladiste/
    │   │   ├── servis/
    │   │   └── kontroler/
    │   └── resources/
    │       ├── application.properties
    │       ├── static/css/style.css
    │       └── templates/
    └── test/java/rs/ac/metropolitan/it355/salon/
```

## Autor

**Valerija Gocić**, indeks **6136**
Univerzitet Metropolitan, Fakultet informacionih tehnologija
Predmet: IT355 - Web sistemi 2, školska 2025/2026. godina
