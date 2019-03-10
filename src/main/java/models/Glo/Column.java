package models.Glo;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;

public class Column {
    String name;
    String id;

    List<Card> cards;

    public Column() {
        this.cards = new LinkedList<>();
    }

    public Column(String name, String id) {
        this();
        this.name = name;
        this.id = id;

    }

    public void setName(String name) {
        this.name = name;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void addCard(Card card) { this.cards.add(card); }

    public void setCards(List<Card> cards) {
        this.cards = cards;
    }

    public List<Card> getCards() {
        return cards;
    }

    @Override
    public String toString() {
        return this.name;
    }
}
