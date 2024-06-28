package br.com.alura.screenmatch.principal;

import br.com.alura.screenmatch.model.*;
import br.com.alura.screenmatch.repository.SerieRepository;
import br.com.alura.screenmatch.service.ConsumoApi;
import br.com.alura.screenmatch.service.ConverteDados;

import java.util.*;
import java.util.stream.Collectors;

public class Principal {

    private Scanner leitura = new Scanner(System.in);
    private ConsumoApi consumo = new ConsumoApi();
    private ConverteDados conversor = new ConverteDados();
    private final String ENDERECO = "https://www.omdbapi.com/?t=";
    private final String API_KEY = "&apikey=6585022c";
    private List<DadosSerie> dadosSerie = new ArrayList<>();
    private SerieRepository repository;
    private List<Serie> series = new ArrayList<>();
    private Optional<Serie> serieBusca;

    public Principal(SerieRepository repository) {
        this.repository = repository;
    }

    public void exibeMenu() throws IllegalAccessException {
        var opcao = -1;

        while(opcao != 0) {
            var menu = """
                    1 - Buscar séries
                    2 - Buscar episódios
                    3 - Listar Series Buscadas 
                    4 - Buscar por genero 
                    5 - Buscar serie por titulo          
                    6 - Buscar series por ator  
                    7 - Buscar top 5 series 
                    8 - Buscar serie por caregoria
                    9 - Buscar serie por temporada e avaliacao
                    10 - Buscar episódos por trecho
                    11 - Top episódios por série
                    12 - Buscar por data
                    0 - Sair                                 
                    """;

            System.out.println(menu);
            opcao = leitura.nextInt();
            leitura.nextLine();

            switch (opcao) {
                case 1:
                    buscarSerieWeb();
                    break;
                case 2:
                    buscarEpisodioPorSerie();
                    break;
                case 0:
                    System.out.println("Saindo...");
                    break;
                case 3:
                    listarSeriresBuscadas();
                    break;
                case 4:
                    if(dadosSerie.isEmpty()) {
                        System.out.println("Erro! nenhuma seria buscada...");
                        break;
                    }
                    System.out.println("Digite o genero para buscar: ");
                    listarPorGenero(leitura.nextLine());
                    break;
                case 5:
                    buscarSeriePorTitulo();
                    break;
                case 6:
                    buscarSeriesPorAtor();
                    break;
                case 7:
                    buscarTop5Series();
                    break;
                case 8:
                    buscarSeriePorGenero();
                    break;
                case 9:
                    buscarSeriePorTemporadaEAvaliacao();
                    break;
                case 10:
                    buscarEpisodioPorTrecho();
                    break;
                case 11:
                    topEpisodiosPorSerie();
                    break;
                case 12:
                    buscarPorData();
                    break;
                default:
                    System.out.println("Opção inválida");
            }
        }
    }

    private void buscarPorData() {
        buscarSeriePorTitulo();

        if(serieBusca.isPresent()) {
            System.out.println("Escolha a data");
            var anoLancamento = leitura.nextInt();
            leitura.nextLine();

            List<Episodio> episodiosAno = repository.episodiosPorSerieEAno(anoLancamento, serieBusca.get());
            episodiosAno.forEach(System.out::println);
        }

    }

    private void topEpisodiosPorSerie() {
        buscarSeriePorTitulo();

        if (serieBusca.isPresent()) {
            Serie serie = serieBusca.get();
            List<Episodio> topEpisodios = repository.topEpisodiosPorSerie(serie);
            topEpisodios.forEach(e ->
                    System.out.printf("Série: %s Temporada %s - Episódio %s - %s Avaliação %s\n",
                            e.getSerie().getTitulo(), e.getTemporada(),
                            e.getNumeroEpisodio(), e.getTitulo(), e.getAvaliacao()));
        }
    }

    private void buscarEpisodioPorTrecho() {
        System.out.println("Digite o nome do episodio ");
        var nomeEpisodio = leitura.nextLine();

        List<Episodio> episodiosEncontrados = repository.episodiosPorTrecho(nomeEpisodio);
        episodiosEncontrados.forEach(e ->
                System.out.printf("Série: %s Temporada %s - Episódio %s - %s\n",
                        e.getSerie().getTitulo(), e.getTemporada(),
                        e.getNumeroEpisodio(), e.getTitulo()));
    }

    private void buscarSeriePorTemporadaEAvaliacao() {
        System.out.println("Quantas temporadas deseja?");
       var quantidadeDeTemporadas = leitura.nextInt();
        System.out.println("Filtrar por qual avaliacao?");
        var avaliacao = leitura.nextDouble();

        List<Serie> seriesParaMaratonar = repository.seriesPorTemporadaEAvaliacao(quantidadeDeTemporadas, avaliacao);
        System.out.println(seriesParaMaratonar);

    }

    private void buscarSeriePorGenero() throws IllegalAccessException {
        System.out.println("Deseja buscar séries de que categoria/genero?");
        var nomeDoGenero = leitura.nextLine();
        Categoria categoria = Categoria.fromPortugues(nomeDoGenero);

        List<Serie> seriesPorCategoria = repository.findByGenero(categoria);
        System.out.println("Series da categoria" + nomeDoGenero);
        System.out.println(seriesPorCategoria);
    }

    private void buscarSerieWeb() throws IllegalAccessException {
        DadosSerie dados = getDadosSerie(); // buscando serie na api
        Serie serie = new Serie(dados); // criando uma instancia(objeto) serie da qual foi representada pelo record e agora sendo criada como objeto manipulavel
        repository.save(serie);

        System.out.println(dados);
    }

    private DadosSerie getDadosSerie() {
        System.out.println("Digite o nome da série para busca");
        var nomeSerie = leitura.nextLine();
        var json = consumo.obterDados(ENDERECO + nomeSerie.replace(" ", "+") + API_KEY);
        DadosSerie dados = conversor.obterDados(json, DadosSerie.class);
        return dados;
    }

    private void buscarEpisodioPorSerie(){
        listarSeriresBuscadas();
        System.out.println("Digite o nome da serie para buscar os episodios");
        var nomeSerie = leitura.nextLine();

        Optional<Serie> serie = repository.findByTituloContainingIgnoreCase(nomeSerie);

        if(serie.isPresent()) {
            var serieEncontrada = serie.get();
            List<DadosTemporada> temporadas = new ArrayList<>();

            for (int i = 1; i <= serieEncontrada.getTotalTemporadas(); i++) {
                var json = consumo.obterDados(ENDERECO + serieEncontrada.getTitulo().replace(" ", "+") + "&season=" + i + API_KEY);
                DadosTemporada dadosTemporada = conversor.obterDados(json, DadosTemporada.class);
                temporadas.add(dadosTemporada);
            }
            temporadas.forEach(System.out::println);

            List<Episodio> episodios = temporadas.stream()
                    .flatMap(d -> d.episodios().stream()
                            .map(e -> new Episodio(d.numero(), e)))
                    .collect(Collectors.toList());

            serieEncontrada.setEpisodios(episodios);
            repository.save(serieEncontrada);
        } else {
            System.out.println("Serie não encontrada!");
        }
    }

    private void listarSeriresBuscadas() {
        series = repository.findAll();
        series.stream()
                .sorted(Comparator.comparing(Serie::getGenero))
                .forEach(System.out::println);
    }

    private void listarPorGenero(String genero) {
        dadosSerie.stream()
                .filter( d -> d.genero().toLowerCase().contains(genero.toLowerCase()))
                .forEach(System.out::println);
    }

    private void buscarSeriePorTitulo() {
        System.out.println("Digite o nome da serie para buscar os episodios");
        var nomeSerie = leitura.nextLine();

        serieBusca = repository.findByTituloContainingIgnoreCase(nomeSerie);

        if(serieBusca.isPresent()) {
            System.out.println("Dados da série: " + serieBusca.get());
        } else {
            System.out.println("Serie não encontrada");
        }
    }

    private void buscarSeriesPorAtor(){
        System.out.println("Qual o nome para a busca?");
        var nomeDoAtor = leitura.nextLine();
        System.out.println("Qual a avaliacao da serie?");
        var avaliacao = leitura.nextDouble();

        List<Serie> seriesEcontradas =  repository.findByAtoresContainingIgnoreCaseAndAvaliacaoGreaterThanEqual(nomeDoAtor, avaliacao);
        System.out.println(seriesEcontradas);
    }

    private void buscarTop5Series() {
        List<Serie> seriesTop = repository.findTop5ByOrderByAvaliacaoDesc();
        System.out.println(seriesTop);
    }
}