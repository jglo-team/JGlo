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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getId() {
        return id;
    }

    @Override
    public String toString() {
        return name;
    }


}
