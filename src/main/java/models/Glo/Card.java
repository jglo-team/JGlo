package models.Glo;

public class Card {
    private String id;
  //  private String description;
    private String name;

    public Card(String id, String name) {
        this.id = id;
    //    this.description = description;
        this.name = name;
    }

    @Override
    public String toString() {
        return name;
    }
}
