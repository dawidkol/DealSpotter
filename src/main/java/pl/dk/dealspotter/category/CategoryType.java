package pl.dk.dealspotter.category;

public enum CategoryType {
    All("Wszystkie kategorie"),
    ELECTRONICS("Elektronika"),
    FASHION("Moda"),
    HOUSE_AND_GARDEN("Dom i ogród"),
    SUPERMARKET("Supermarket"),
    CHILD("Dziecko"),
    BEAUTY("Uroda"),
    HEALTH("Zdrowie"),
    CULTURE_AND_ENTERTAINMENT("Kultura i rozrywka"),
    SPORT_AND_TOURISM("Sport i turystyka"),
    AUTOMOTIVE("Motoryzacja"),
    REAL_ESTATE("Nieruchomości"),
    OTHER("Inne");



    private String description;

    CategoryType(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
