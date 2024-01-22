package majestatyczne.bestie.frontend;

public class Constants {

//    FXML
    public static final String FXML_HOME_PAGE_RESOURCE = "FXML/home-page.fxml";

    public static final String FXML_QUIZ_PAGE_RESOURCE = "FXML/quiz-page.fxml";

    public static final String FXML_GLOBAL_SETTINGS_RESOURCE = "FXML/global-settings-page.fxml";

    public static final String FXML_QUIZ_SETTINGS_RESOURCE = "FXML/quiz-settings-page.fxml";

    public static final String FXML_EXPORT_POPUP_RESOURCE = "FXML/export-popup.fxml";

    public static final String FXML_STATS_PAGE_RESOURCE = "FXML/stats-page.fxml";

    public static final String FXML_ANSWERS_PAGE_RESOURCE = "FXML/answers-page.fxml";

//    ICONS
    public static final String APPLICATION_ICON_RESOURCE = "icons/owl-bear.png";

    public static final String SETTINGS_ICON_RESOURCE = "icons/settings.png";

    public static final String BACK_ICON_RESOURCE = "icons/left-arrow.png";

//    SCENE PARAMETERS
    public static final int SCENE_WIDTH = 1080;

    public static final int SCENE_HEIGHT = 720;

    public static final String STAGE_TITLE = "Rewards Manager";

    public static final String DEFAULT_UPLOAD_DIRECTORY = "/Downloads";

    public static final String FILE_UPLOAD_ACCEPTED_TITLE = "Przesłanie pliku powiodło się!";

    public static final String FILE_UPLOAD_ERROR_TITLE = "Przesłanie pliku nie powiodło się!";

//    CONSTANT LABELS

    public static final String QUIZ_MAX_SCORE_INFO = "Punkty do uzyskania: ";

    public static final String NEW_REWARD_CATEGORY_PROMPT = "Nazwa nowej kategorii";

    public static final String NEW_REWARD_NAME_PROMPT = "Nazwa nowej nagrody";

    public static final String NEW_REWARD_DESCRIPTION_PROMPT = "Opis nowej nagrody";

    public static final String PERCENTAGE_REWARD_STRATEGY_DESCRIPTION = "X% najlepszych (maksymalny wynik i najszybciej) - kategoria1, reszta - kategoria2";

    public static final String SCORE_REWARD_STRATEGY_DESCRIPTION = "X poprawnych odpowiedzi - kategoria1, X-1 poprawnych - kategoria2, ...";

    public static final String PERCENTAGE_STRATEGY_PARAMETER_COLUMN_NAME = "Procent (0-100)";

    public static final String SCORE_STRATEGY_PARAMETER_COLUMN_NAME = "Liczba poprawnych odpowiedzi";

    public static final String REWARD_CATEGORY_CHOICE_BOX_NO_CATEGORY = "brak kategorii";

    public static final String REWARD_CHOICE_BOX_NO_REWARD = "brak nagrody";

    public static final String EXPORT_POPUP_WINDOW_TITLE = "Eksport pliku";

    public static final String DELETE_BUTTON_TEXT = "Usuń";

//    ALERTS
    public static final String UPDATE_REWARDS_INFO = "Zmiany w nagrodach zostały wprowadzone!";
    public static final String UPDATE_REWARDS_ERROR_TITLE = "Zmiany w nagrodach nie zostały wprowadzone!";

    public static final String UPDATE_REWARD_CATEGORIES_INFO = "Zmiany w kategoriach nagród zostały wprowadzone!";
    public static final String UPDATE_REWARD_CATEGORIES_ERROR_TITLE = "Zmiany w kategoriach nagród nie zostały wprowadzone!";

    public static final String ADD_REWARD_CATEGORY_INFO = "Nowa kategoria została dodana!";

    public static final String ADD_REWARD_CATEGORY_ERROR_TITLE = "Nie udało się dodać nowej kategorii!";

    public static final String ADD_REWARD_CATEGORY_EMPTY_WARNING = "Nie zostawiaj nazwy kategorii pustej";

    public static final String UPDATE_REWARD_CATEGORY_EMPTY_ERROR = "Co najmniej jedna z nazw kategorii jest pusta. Zmiany w kategoriach nagród nie zostaną wprowadzone!";

    public static final String ADD_REWARD_EMPTY_WARNING = "Nie zostawiaj nazwy nagrody pustej.";

    public static final String ADD_REWARD_INFO = "Nowa nagroda została dodana!";

    public static final String ADD_REWARD_ERROR_TITLE = "Nie udało się dodać nowej nagrody!";

    public static final String DELETE_QUIZ_INFO = "Wybrany quiz został usunięty!";

    public static final String DELETE_QUIZ_ERROR = "Nie udało się usunąć wybranego quizu!";

    public static final String DELETE_REWARD_INFO = "Wybrana nagroda została usunięta!";

    public static final String DELETE_REWARD_ERROR = "Nie udało się usunąć wybranej nagrody!";

    public static final String DELETE_REWARD_CATEGORY_INFO = "Wybrana kategoria nagrody została usunięta!";

    public static final String DELETE_REWARD_CATEGORY_ERROR = "Nie udało się usunąć wybranej kategorii nagrody!";

    public static final String ADD_STRATEGY_INFO = "Nowa strategia została dodana i zastosowana!";

    public static final String ADD_STRATEGY_ERROR = "Nie udało się dodać nowej strategii!";

    public static final String UPDATE_STRATEGY_INFO = "Strategia została zaktualizowana i zastosowana!";

    public static final String UPDATE_STRATEGY_ERROR = "Nie udało się zaktualizować strategii!";

    public static final String PERCENTAGE_STRATEGY_PARAMETER_OUT_OF_BOUND_WARNING = "Parametr musi mieć wartość między 0 a 100!";

    public static final String PERCENTAGE_STRATEGY_PARAMETER_SUM_OVER_100_WARNING = "Suma procentów nie może przekraczać 100!";

    public static final String SCORE_STRATEGY_PARAMETER_OUT_OF_BOUND_WARNING = "Każdy z parametrów musi być z zakresu od 0 do maksymalnej liczby punktów z quizu";

    public static final String SCORE_STRATEGY_PARAMETER_DUPLICATE_WARNING = "Parametry oznaczające liczbę uzyskanych punktów nie mogą się powtarzać!";

    public static final String EXPORT_FILE_ERROR = "Nie udało się wyeksportować wyników do pliku!";

    public static final String EXPORT_FILE_INFO = "Plik został wyeksportowany poprawnie i zapisany na dysku w folderze Pobrane!";

    public static final String SAVE_EXPORT_FILE_EXCEPTION = "Nie udało się zapisać otrzymanego pliku na dysku!";

    // general alert texts
    public static final String ALERT_WARNING_TITLE = "Uwaga";

    public static final String ALERT_CONFIRMATION_TITLE = "Potwierdzenie";

    public static final String ALERT_ERROR_TITLE = "Błąd";

    public static final String ALERT_ERROR_CODE = "Kod błędu: ";

//    STRATEGIES PARAMETERS
    public static final int PERCENTAGE_STRATEGY_PARAMETERS_NUMBER = 2;

//    RETROFIT CONSTANTS
    public static final String BASE_SERVER_URL = "http://localhost:8080/";

    public static final String JSON_DATE_FORMAT = "yyyy-MM-dd'T'HH:mm:ss";
}
