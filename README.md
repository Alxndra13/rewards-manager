
# Technologie obiektowe projekt - grupa Majestatyczne Bestie


## Instrukcja uruchomienia aplikacji
Root directory projektu zawiera foldery backend i frontend, zawierające odpowiednio dwie aplikacje.

### Backend
W folderze backend znajduje się aplikacja spring boot implementująca stronę serwerową aplikacji. W celu uruchomienia serwera, wystarczy uruchomić RewardsManagerApplication.

W projekcie została wykorzystana plikowa baza danych H2. Zawartość bazy danych można przeglądać po zatrzymaniu aplikacji spring boot
#### Dostęp do bazy przez IDE
1. Zakładka Database w IDE
2. Data Source from Path
3. Odpowiednia ścieżka do pliku: 
```../backend/resources/db-rewards-manager.mv.db```
4. Driver H2
5. User: **username**
6. Password: **password**

#### Konsola w przeglądarce po uruchomieniu aplikacji
1. Uruchomienie aplikacji serwerowej
2. W przeglądarce URL:
[http://localhost:8080/h2-console](http://localhost:8080/h2-console)
3. Username i Password analogicznie.

<u>Uwaga:</u> W pliku backend/resources/application.properties, można skonfigurować inne ustawienia dla bazy danych, np. zmienić username i password.

### Frontend
Po uruchomieniu aplikacji serwerowej, należy w drugim oknie otworzyć folder frontend, który zawiera aplikację desktopową JavaFx.
1. Zakładka Gradle w IDE
2. frontend ->application -> run
3. Należy z pomocą tej opcji uruchamiać aplikację bądź zapisać ten sposób do swoich konfiguracji.

## Historia zmian
### Milestone 1 (do 15.10.2023)
- Konfiguracja backendowej aplikacji -> połączenie z bazą embedded h2
	- Stworzenie modeli bazodanowych
	- Stworzenie warstw persystencji:
		- Warstwy repository  z wykorzystaniem JPARepository
		- Warstwy service
	- Stworzenie parsera:
		- Serwer przyjmuje plik od klienta
		- W przypadku poprawnego pliku, akceptuje go, dodając nowy quiz do bazy danych wraz z powiązanymi informacjami
		- Odrzuca niepoprawne pliki
	- Wystawienie endpointów:
		- /upload-file - służący do obsłużenia zapytania POST z plikiem xlsx
		- /quiz/all - obsługujący zapytanie GET, zwracającego wszystkie quizy z pominięciem wyników poszczególnych quizów
		- /quiz/{quizId} -> zwracający quiz dla danego id
		- /results/by_quiz_id/{quizId} -> zwracający wszystkie wyniki dla danego id quizu
	- Stworzenie testów, sprawdzających działanie logiki parsowania plików xlsx
-  Konfiguracja aplikacji desktopowej -> stworzenie aplikacji JavaFX
	-  Stworzenie widoków:
		- Strony głównej - home page - z której możemy wyświetlić wszystkie quizy, oraz importować plik z urządzenia
		- Quizu - quiz page - pokazująca wyniki danego quizu
		- Oba widoki składają się na pliki fxml i odpowiadające im kontrolery
	- Z użyciem biblioteki retrofit wysyłamy odpowiednie zapytania do serwera 
		- Odpowiednio z endpointami po stronie serwerowej: GET listy quizów, GET wyników dla danego quizu, POST przesłania pliku do zaimportowania do aplikacji
	- Stworzenie modeli przechwytujących dane z serwera
	- Stworzenie modeli widokowych, potrzebnych do wyświetlenia przechwyconych danych po stronie klienta

### Milestone 2 (do 5.01.2024)
- Dodane funkcjonalności:
	- Automatyczny import nagród z arkusza
	- Możliwość definiowania i przydzielania kategorii nagród
	- Wyliczanie nagród na podstawie dwóch strategii -> punktowej i procentowej
	- Poprawiony wygląd 
	- Możliwość przydzielania strategii oraz jego parametrów dla danego arkusza
	- Nowe endpointy
	- Zostały rozszerzone modele bazodanowe
	- Package reward_selection_strategy zawiera implementacje algorytmów strategii
	- Frontend:
		- Nowe widoki (pliki FXML):
			- global-settings-page -> konfiguracja kategorii na stronie głównej 
			- quiz-settings-page -> konfiguracja strategii dla danego quizu (otwierana przez zębatke w widoku quiz-page)
		- Nowe modele
		- Kontrolery obsługujące nowe widoki

### Milestone 3 (do 19.01.2024)
- Dodane funkcjonalności:
	- Eksportowanie wyników quizu do pliku xlsx lub pdf, z możliwością rozszerzeń
	- Poprawienie logiki działania strategii, zgodnie z wymaganiami
	- Statystyki dla danego quizu:
		- Statystyka % poprawnych odpowiedzi dla każdego pytania
		- Statystyka rozkładu odpowiedzi dla konkretnego pytania (po kliknięciu na pytanie, pojawia się wykres ze statystyką odpowiedzi na to pytanie)
- Backend
    - Nowe modele + DTO - Question i Answer, do przechowywania pytań i odpowiedzi z danego quizu
    - Nowe endpointy
		- eksport wyników quizu do pliku
		- GET listy pytań dla danego quizId
    - Klasy i logika obsługujące stworzenie pliku z wynikami quizu w odpowiednim formacie (pakiet util/file_creator)
    - Poprawki w parserze - dodano parsowanie pytań i odpowiedzi
- Frontend:
	- Nowe widoki (pliki FXML):
		- export-popup - okienko konfiguracji eksportu do pliku (z wyborem pożądanego formatu)
		- stats-page - główna strona statystyk dla danego quizu - informacje o % poprawnych odpowiedzi dla każdego z pytań
		- answers-page - otwierające statystykę rozkładu odpowiedzi dla konkretnego pytania z tabeli w stats-page, w formie wykresu słupkowego, z oznaczeniem kolorystycznym poprawnej odpowiedzi
	- Kontrolery obsługujące nowe widoki
	- Nowe modele
	- Odpowiadające backendowi zapytania z użyciem biblioteki retrofit
	- Dopracowanie strony wizualnej

<u>Uwaga:</u> Logiem aplikacji jest ikonka majestatycznej bestii :)
Źródło: https://github.com/Soamid/obiektowe-lab/blob/master/img/owlbear7.png