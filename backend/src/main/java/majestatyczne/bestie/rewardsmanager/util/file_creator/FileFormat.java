package majestatyczne.bestie.rewardsmanager.util.file_creator;

public enum FileFormat {

    PDF,

    XLSX;

    public static FileFormat fromString(String fileFormatString) {
        return switch (fileFormatString.toLowerCase()) {
            case "xlsx" -> XLSX;
            case "pdf" -> PDF;
            default -> throw new IllegalArgumentException(String.format("invalid file format: %s", fileFormatString));
        };
    }
}
