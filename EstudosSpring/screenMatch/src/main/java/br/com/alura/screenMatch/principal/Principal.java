package br.com.alura.screenMatch.principal;

import br.com.alura.screenMatch.model.*;
import br.com.alura.screenMatch.repository.SerieRepository;
import br.com.alura.screenMatch.service.ConsumoApi;
import br.com.alura.screenMatch.service.ConverteDados;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

public class Principal {
    ConsumoApi consumoApi = new ConsumoApi();
    ConverteDados conversor = new ConverteDados();
    private Scanner input = new Scanner(System.in);
    private List<DadosSerie> dadosSeries = new ArrayList<>();
    private final String ENDERECO = "https://www.omdbapi.com/?t=";
    private final String API_KEY = "&apikey=18ae168a";
    private SerieRepository repositorio;
    private List<Serie> series = new ArrayList<>();
    private Optional<Serie> serieBusca;

    public Principal(SerieRepository repositorio) {
        this.repositorio = repositorio;
    }

    public void exibeMenu() {
        var opcao = -1;


        var apresentacao =
                """
                        ******************
                        * OMDB CONSULTOR *
                        ******************
                        """;
        System.out.println(apresentacao);

        while (opcao != 0) {
            var menu =
                    """
                            \n\n[1] Buscar séries
                            [2] Buscar episódios
                            [3] Listar séries buscadas
                            [4] Buscar série por título
                            [5] Buscar série(s) por ator
                            [6] Top 5 melhores series
                            [7] Buscar série por gênero
                            [8] Buscar série por número de temporadas e avaliação
                            [9] Buscar episódio por trecho
                            [10] Top 5 melhores episódios de uma série
                            [11] Buscar episódios a partir de uma data
                            [0] Sair
                            Escolha uma das opções:                                 
                            """;

            System.out.println(menu);
            opcao = input.nextInt();
            input.nextLine();

            switch (opcao) {
                case 1:
                    buscarSerieWeb();
                    break;

                case 2:
                    buscarEpisodioPorSerie();
                    break;

                case 3:
                    listarSeriesBuscadas();
                    break;

                case 4:
                    buscarSeriePorTitulo();
                    break;

                case 5:
                    buscarSeriePorAtor();
                    break;

                case 6:
                    buscarTop5Series();
                    break;

                case 7:
                    buscarSeriePorGenero();
                    break;

                case 8:
                    buscarSeriePorTemporadasEAvaliacao();
                    break;

                case 9:
                    buscarEpisodioPorTrecho();
                    break;

                case 10:
                    buscarTop5Episodios();
                    break;

                case 11:
                    buscarEpisodioPosData();
                    break;

                case 0:
                    System.out.println("Saindo...");
                    break;

                default:
                    System.out.println("Opção inválida");
            }
        }
    }

    private void buscarSerieWeb() {
        DadosSerie dados = getDadosSerie();
        Serie serie = new Serie(dados);
        repositorio.save(serie);
        System.out.println(dados);
    }

    private DadosSerie getDadosSerie() {
        System.out.println("Digite o nome da série para busca");
        var nomeSerie = input.nextLine();
        var json = consumoApi.obterDados(ENDERECO + nomeSerie.replace(" ", "+") + API_KEY);
        DadosSerie dados = conversor.obterDados(json, DadosSerie.class);
        return dados;
    }

    private void buscarEpisodioPorSerie() {
        listarSeriesBuscadas();
        System.out.println("Escolha uma serie pelo nome:");
        var nomeSerie = input.nextLine();

        Optional<Serie> serie = series.stream()
                .filter(s -> s.getTitulo().toLowerCase().contains(nomeSerie.toLowerCase()))
                .findFirst();

        if (serie.isPresent()) {
            List<DadosTemporada> temporadas = new ArrayList<>();
            var serieEncontrada = serie.get();

            for (int i = 1; i <= serieEncontrada.getTotalTemporadas(); i++) {
                var json = consumoApi.obterDados(ENDERECO + serieEncontrada.getTitulo().replace(" ", "+") + "&season=" + i + API_KEY);
                DadosTemporada dadosTemporada = conversor.obterDados(json, DadosTemporada.class);
                temporadas.add(dadosTemporada);
            }
            temporadas.forEach(System.out::println);

            List<Episodio> episodios = temporadas.stream()
                    .flatMap(d -> d.episodios().stream()
                            .map(e -> new Episodio(d.numero(), e)))
                    .collect(Collectors.toList());

            serieEncontrada.setEpisodios(episodios);

            repositorio.save(serieEncontrada);
        } else {
            System.out.println("Série não encontrada.");
        }
    }

    private void listarSeriesBuscadas() {
        series = repositorio.findAll();//busca quem esta cadastrado no banco

        series.stream()
                .sorted(Comparator.comparing(Serie::getGenero))//ordena as series por genero
                .forEach(System.out::println);//uma vez ordenado as series, mostra todas
    }

    private void buscarSeriePorTitulo() {
        System.out.println("Escolha uma serie pelo nome:");
        var nomeSerie = input.nextLine();

        serieBusca = repositorio.findByTituloContainingIgnoreCase(nomeSerie);

        if (serieBusca.isPresent()) {
            System.out.println("Dados da série " + serieBusca.get());
        } else {
            System.out.println("Série não encontrada.");
        }
    }

    private void buscarSeriePorAtor() {
        System.out.println("Informe o nome do ator para a busca:");
        var nomeAtor = input.nextLine();

        System.out.println("Avaliações a partir de qual valor: ");
        var avaliacao = input.nextDouble();

        List<Serie> seriesEncontradas = repositorio.findByAtoresContainingIgnoreCaseAndAvaliacaoGreaterThanEqual(nomeAtor, avaliacao);

        System.out.println("Série(s) em que " + nomeAtor + " trabalhou:");
        seriesEncontradas.forEach(s ->
                System.out.println(s.getTitulo() + " - avaliação[" + s.getAvaliacao() + "]"));

    }

    private void buscarTop5Series() {
        List<Serie> seriesTop = repositorio.findTop5ByOrderByAvaliacaoDesc();

        System.out.println("Melhores series:");
        seriesTop.forEach(s ->
                System.out.println(s.getTitulo() + " - avaliação[" + s.getAvaliacao() + "]"));
    }

    private void buscarSeriePorGenero() {
        System.out.println("Informe o gênero:");
        var nomeGenero = input.nextLine();

        Categoria categoria = Categoria.fromPortugues(nomeGenero);
        List<Serie> seriesPorGenero = repositorio.findByGenero(categoria);

        System.out.println("Séries do gênero [" + nomeGenero + "]");
        seriesPorGenero.forEach(s ->
                System.out.println(s.getTitulo()));
    }

    private void buscarSeriePorTemporadasEAvaliacao() {
        System.out.println("Informe o número máximo de temporadas");
        var maximoTemporadas = input.nextInt();
        System.out.println("Informe o valor mínimo de avaliações");
        var minimoAvaliacoes = input.nextDouble();

        List<Serie> seriesTempAvaliacao = repositorio.seriePorTemporadaEAvaliacao(maximoTemporadas, minimoAvaliacoes);

        seriesTempAvaliacao.forEach(s ->
                System.out.println(s.getTitulo() + " - temporadas[" + s.getTotalTemporadas() +
                        "] - avaliação[" + s.getAvaliacao() + "]"));
    }

    private void buscarEpisodioPorTrecho() {
        System.out.println("Informe o nome do episódio para a busca:");
        var trechoEpisodio = input.nextLine();

        List<Episodio> episodiosEncontrados = repositorio.episodiosPorTrecho(trechoEpisodio);

        episodiosEncontrados.forEach(e ->
                System.out.printf("Série[%s] Temporada[%s] Episódio[%s - %s]\n", e.getSerie().getTitulo()
                        , e.getTemporada(), e.getNumero(), e.getTitulo()));
    }

    private void buscarTop5Episodios()
    {
        buscarSeriePorTitulo();
        if (serieBusca.isPresent())
        {
            Serie serie = serieBusca.get();
            List<Episodio> topEpisodios = repositorio.topEpisodiosPorSerie(serie);

            topEpisodios.forEach(e ->
                    System.out.printf("Série[%s] Temporada[%s] Avaliação[%s] Episódio[%s - %s]\n",
                            e.getSerie().getTitulo(), e.getTemporada(), e.getAvaliacao(),
                            e.getNumero(), e.getTitulo()));
        }
    }

    private void buscarEpisodioPosData()
    {
        buscarSeriePorTitulo();
        if(serieBusca.isPresent())
        {
            Serie serie = serieBusca.get();
            System.out.println("Insira o ano de lançamento:");
            var anoLancamento = input.nextInt();
            input.nextLine();

            List<Episodio> episodiosAno = repositorio.episodiosPorSerieEAno(serie ,anoLancamento);
            episodiosAno.forEach(System.out::println);
        }
    }
}

