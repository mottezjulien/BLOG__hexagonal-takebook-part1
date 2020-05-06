package fr.jmottez.takebook.spring.facade.transport.objet;

public class ShelfDTO {

    private int id;
    private String label;
    private LibraryDTO library;

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }


    public void setLibrary(LibraryDTO library) {
        this.library = library;
    }

    public LibraryDTO getLibrary() {
        return library;
    }
}
