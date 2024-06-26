package br.com.alura.screenmatch.model;

public enum Categoria {

    ACAO("Action"),
    ROMANCE("Romance"),
    COMEDIA("Comedy"),
    DRAMA("Drama"),
    CRIME("Crime");

    private String categoriaOmd;

    Categoria (String categoriaOmd) {
        this.categoriaOmd = categoriaOmd;
    }

    public static Categoria fromString(String text) throws IllegalAccessException {
        for ( Categoria categoria : Categoria.values() ) {
            if(categoria.categoriaOmd.equalsIgnoreCase(text)) {
                return categoria;
            }
        }
        throw new IllegalAccessException("Nenhuma categoria encontrada para a string: " + text);
    }
}
