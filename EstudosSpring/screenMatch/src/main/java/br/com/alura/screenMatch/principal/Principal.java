package br.com.alura.screenMatch.principal;

import br.com.alura.screenMatch.model.DadosEpisodio;
import br.com.alura.screenMatch.model.DadosSerie;
import br.com.alura.screenMatch.model.DadosTemporada;
import br.com.alura.screenMatch.model.Episodio;
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
    private final String ENDERECO = "https://www.omdbapi.com/?t=";
    private final String API_KEY  = "&apikey=18ae168a";

    public void exibeMenu()
    {
        var menu = """
                ******************
                * IMDB CONSULTOR *
                ******************
                
                [1] Buscar séries
                [2] Buscar episódios
                [3] Sair
                Escolha uma das opções:                                 
                """;

        System.out.println(menu);
        var opcao = input.nextInt();
        input.nextLine();

        switch (opcao)
        {
            case 1:
                buscarSerieWeb();
                break;

            case 2:
                buscarEpisodioPorSerie();
                break;

            case 0:
                System.out.println("Saindo...");
                break;

            default:
                System.out.println("Opção inválida");
        }

    }

     private void buscarSerieWeb()
     {
        DadosSerie dados = getDadosSerie();
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
}
