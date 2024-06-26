package br.com.alura.screenmatch.model;

public enum Linguagem {
    PORTUGUES("pt"),
    INGLES("en");

    public String silgaLinguagem;

    Linguagem(String lang) {
        this.silgaLinguagem = lang;
    }

    public static Linguagem getLang(String lang) throws NoSuchFieldException {
        for (Linguagem self : Linguagem.values()) {
            if(self.silgaLinguagem.equalsIgnoreCase(lang)) {
                return self;
            }
        }
        throw new NoSuchFieldException("Língua não encontrada!");
    }
}
