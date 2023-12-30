package br.com.alura.screenMatch.principal;

import br.com.alura.screenMatch.model.*;
import br.com.alura.screenMatch.repository.SerieRepository;
import br.com.alura.screenMatch.service.ConsumoApi;
import br.com.alura.screenMatch.service.ConverteDados;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

public class Principal
{
    ConsumoApi consumoApi = new ConsumoApi();
    ConverteDados conversor = new ConverteDados();
    private Scanner input = new Scanner(System.in);
    private List<DadosSerie> dadosSeries = new ArrayList<>();
    private final String ENDERECO = "https://www.omdbapi.com/?t=";
    private final String API_KEY  = "&apikey=18ae168a";
    private SerieRepository repositorio;

    public Principal(SerieRepository repositorio)
    {
        this.repositorio = repositorio;
    }

    public void exibeMenu()
    {
        var opcao = -1;


        var apresentacao =
                """
                ******************
                * OMDB CONSULTOR *
                ******************
                """;
        System.out.println(apresentacao);

        while (opcao != 0)
        {
            var menu =
                """
                \n\n[1] Buscar séries
                [2] Buscar episódios
                [3] Listar séries buscadas
                [0] Sair
                Escolha uma das opções:                                 
                """;

            System.out.println(menu);
            opcao = input.nextInt();
            input.nextLine();

            switch (opcao)
            {
                case 1:
                    buscarSerieWeb();
                    break;

                case 2:
                    buscarEpisodioPorSerie();
                    break;

                case 3:
                    listarSeriesBuscadas();
                    break;

                case 0:
                    System.out.println("Saindo...");
                    break;

                default:
                    System.out.println("Opção inválida");
            }
        }
    }

     private void buscarSerieWeb()
     {
        DadosSerie dados = getDadosSerie();
        Serie serie = new Serie (dados);
        repositorio.save(serie);
        System.out.println(dados);
    }

    private DadosSerie getDadosSerie()
    {
        System.out.println("Digite o nome da série para busca");
        var nomeSerie = input.nextLine();
        var json = consumoApi.obterDados(ENDERECO + nomeSerie.replace(" ", "+") + API_KEY);
        DadosSerie dados = conversor.obterDados(json, DadosSerie.class);
        return dados;
    }

    private void buscarEpisodioPorSerie()
    {
        DadosSerie dadosSerie = getDadosSerie();
        List<DadosTemporada> temporadas = new ArrayList<>();

        for (int i = 1; i <= dadosSerie.totalTemporadas(); i++) {
            var json = consumoApi.obterDados(ENDERECO + dadosSerie.titulo().replace(" ", "+") + "&season=" + i + API_KEY);
            DadosTemporada dadosTemporada = conversor.obterDados(json, DadosTemporada.class);
            temporadas.add(dadosTemporada);
        }
        temporadas.forEach(System.out::println);
    }

    private void listarSeriesBuscadas()
    {
        List<Serie> series = repositorio.findAll();//busca quem esta cadastrado no banco

        series.stream()
                .sorted(Comparator.comparing(Serie::getGenero))//ordena as series por genero
                .forEach(System.out::println);//uma vez ordenado as series, mostra todas
    }
}
