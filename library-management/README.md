# Zarzadzanie Biblioteka

Ten moduł to aplikacja Spring Boot, która udostępnia prosty system biblioteczny online. Pokazuje rejestrację użytkowników, logowanie oraz podstawowy katalog książek.

## Uruchamianie aplikacji

Upewnij się, że masz zainstalowaną Javę 17 oraz Mavena. Z tego katalogu uruchom:

```bash
mvn spring-boot:run
```

Domyślnie aplikacja łączy się z bazą danych PostgreSQL `librarydb` na `localhost`. W razie potrzeby dostosuj ustawienia w pliku `src/main/resources/application.yml`.

## Schemat bazy danych

Schemat jest inicjalizowany automatycznie z `src/main/resources/schema.sql` podczas startu. Skrypt tworzy niezbędne tabele i dodaje kilka przykładowych książek.

## Dane demo

- **Admin:** `Fabian` / `Fabian123`
- **Użytkownik:** `Anna` / `Anna123`
