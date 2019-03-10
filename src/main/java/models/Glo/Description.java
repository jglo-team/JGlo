package models.Glo;

public class Description {
    private String text;

    public Description(String text) {
        this.text = text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getText() {
        return text;
    }

    @Override
    public String toString() {
        return "{\"" +
                "\"text\":\"" +
                text +
                "\"}";
    }
}
