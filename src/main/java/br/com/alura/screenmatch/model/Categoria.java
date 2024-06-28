package br.com.alura.screenmatch.model;

public enum Categoria {

    ACAO("Action", "Ação"),
    ROMANCE("Romance", "Romance"),
    COMEDIA("Comedy", "Comédia"),
    DRAMA("Drama", "Drama"),
    CRIME("Crime", "Crime");

    private String categoriaOmd;

    private String categoriaPortugues;

    Categoria (String categoriaOmd, String categoriaEmPortugues) {
        this.categoriaOmd = categoriaOmd;
        this.categoriaPortugues = categoriaEmPortugues;
    }

    public static Categoria fromString(String text) throws IllegalAccessException {
        for ( Categoria categoria : Categoria.values() ) {
            if(categoria.categoriaOmd.equalsIgnoreCase(text)) {
                return categoria;
            }
        }
        throw new IllegalAccessException("Nenhuma categoria encontrada para a string: " + text);
    }

    public static Categoria fromPortugues(String text) throws IllegalAccessException {
        for ( Categoria categoria : Categoria.values() ) {
            if(categoria.categoriaPortugues.equalsIgnoreCase(text)) {
                return categoria;
            }
        }
        throw new IllegalAccessException("Nenhuma categoria encontrada para a string: " + text);
    }
}
