package jp.ac.jec.cm0113.mywordbook;

public class Card {
    private String Chinese;
    private String japanese;
    private int id;

    public Card(String chinese, String japanese, int id) {
        this.japanese = japanese;
        Chinese = chinese;
        this.id = id;
    }

    public Card(String chinese, String japanese) {
        this.japanese = japanese;
        Chinese = chinese;
    }

    public String getJapanese() {
        return japanese;
    }

    public void setJapanese(String japanese) {
        this.japanese = japanese;
    }

    public String getChinese() {
        return Chinese;
    }

    public void setChinese(String chinese) {
        Chinese = chinese;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
