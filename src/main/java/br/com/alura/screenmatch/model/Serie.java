package br.com.alura.screenmatch.model;

import br.com.alura.screenmatch.service.traducao.MyMemoryApiConnector;
import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Entity
@Table(name = "series")
public class Serie {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @Column(unique = true)
    private String titulo;
    private Integer totalTemporadas;
    private Double avaliacao;
    @Enumerated(EnumType.STRING)
    private Categoria genero;
    private String sinopse;
    private String image;
    private String atores;

    @OneToMany(mappedBy = "serie", cascade = CascadeType.ALL, fetch = FetchType.EAGER) // estabelecendo mapeamento de relação entre entidades no DB aqui no caso (OneToMany), ou seja, um para muitos
    private List<Episodio> episodios = new ArrayList<>();

    public Serie() {}

    public Serie(DadosSerie dadosSerie) throws IllegalAccessException {
        this.titulo = dadosSerie.titulo();
        this.totalTemporadas = dadosSerie.totalTemporadas();
        this.avaliacao = Optional.of(Double.valueOf(dadosSerie.avaliacao())).orElse(.0);
        this.genero = Categoria.fromString(dadosSerie.genero().split(",")[0].trim());
        this.sinopse = MyMemoryApiConnector.pegaTraducao(dadosSerie.sinopse()).trim();
        this.image = dadosSerie.image();
        this.atores = dadosSerie.atores();
    }

    public List<Episodio> getEpisodios() {
        return episodios;
    }

    public void setEpisodios(List<Episodio> episodios) {
        episodios.forEach(e-> e.setSerie(this));
        this.episodios = episodios;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public Integer getTotalTemporadas() {
        return totalTemporadas;
    }

    public void setTotalTemporadas(Integer totalTemporadas) {
        this.totalTemporadas = totalTemporadas;
    }

    public Double getAvaliacao() {
        return avaliacao;
    }

    public void setAvaliacao(Double avaliacao) {
        this.avaliacao = avaliacao;
    }

    public Categoria getGenero() {
        return genero;
    }

    public void setGenero(Categoria genero) {
        this.genero = genero;
    }

    public String getSinopse() {
        return sinopse;
    }

    public void setSinopse(String sinopse) {
        this.sinopse = sinopse;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getAtores() {
        return atores;
    }

    public void setAtores(String atores) {
        this.atores = atores;
    }

    @Override
    public String toString() {
        return "\n" + "titulo: " + this.getTitulo() + '\n' +
                "Total de Temporadas: " + this.getTotalTemporadas() + '\n' +
                "Avaliacao: " + this.getAvaliacao() + '\n' +
                "Genero: " + this.getGenero() + '\n' +
                "Sinopse: " + this.getSinopse() + '\n' +
                "Image: " + this.getImage() + '\n' +
                "Atores: " + this.getAtores() + '\n' +
                "Episodios: " + this.getEpisodios() + '\n';
    }

}
