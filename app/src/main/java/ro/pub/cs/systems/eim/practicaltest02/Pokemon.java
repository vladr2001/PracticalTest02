package ro.pub.cs.systems.eim.practicaltest02;

import java.util.ArrayList;

public class Pokemon {
    String name;
    String url;
    ArrayList<String> abilities;
    ArrayList<String> types;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public ArrayList<String> getAbilities() {
        return abilities;
    }

    public void setAbilities(ArrayList<String> abilities) {
        this.abilities = abilities;
    }

    public ArrayList<String> getTypes() {
        return types;
    }

    public void setTypes(ArrayList<String> types) {
        this.types = types;
    }

    public Pokemon(String name, String url, ArrayList<String> abilities, ArrayList<String> types) {
        this.name = name;
        this.url = url;
        this.abilities = abilities;
        this.types = types;
    }
}
