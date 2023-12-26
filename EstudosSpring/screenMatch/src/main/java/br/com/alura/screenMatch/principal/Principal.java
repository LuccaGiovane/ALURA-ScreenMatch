package br.com.alura.screenMatch.principal;

import br.com.alura.screenMatch.model.DadosEpisodio;
import br.com.alura.screenMatch.model.DadosSerie;
import br.com.alura.screenMatch.model.DadosTemporada;
import br.com.alura.screenMatch.model.Episodio;
import br.com.alura.screenMatch.service.ConsumoApi;
import br.com.alura.screenMatch.service.ConverteDados;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Scanner;
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
        List<DadosTemporada> temporadas = new ArrayList<>();
        System.out.println("Digite o nome da serie para a busca:");
        var nomeSerie = input.nextLine();

		var json = consumoApi.obterDados(ENDERECO + nomeSerie.replace(" ", "+") +API_KEY);
        DadosSerie dados = conversor.obterDados(json, DadosSerie.class);

        System.out.println(dados);


		for(int i=1; i <= dados.totalTemporadas(); i++)
		{
			json = consumoApi.obterDados(ENDERECO + nomeSerie.replace(" ", "+") + "&season=" + i +API_KEY);
			DadosTemporada dadosTemporada = conversor.obterDados(json, DadosTemporada.class);

			temporadas.add(dadosTemporada);
		}
		temporadas.forEach(System.out::println);

        //Basicamente essa linha abaixo substitui 2 fors aninhados (Exemplo abaixo da linha)
        temporadas.forEach(t -> t.episodios().forEach(e -> System.out.println(e.titulo())));
        /*
        for(int i=0; i < dados.totalTemporadas(); i++)
        {
            List<DadosEpisodio> episodioTemporada = temporadas.get(i).episodios();
            for(int j=0; j < episodioTemporada.size(); j++)
            {
                System.out.println(episodioTemporada.get(j).titulo());
            }
        }
        */


        List<DadosEpisodio> dadosEpisodios = temporadas.stream().flatMap(t -> t.episodios().stream())
                .collect(Collectors.toList());

        System.out.println("[Top 5 Melhores Episodios]");
        dadosEpisodios.stream()
                .filter(e -> !e.avaliacao().equalsIgnoreCase("N/A"))
                .sorted(Comparator.comparing(DadosEpisodio::avaliacao).reversed())
                .limit(5)
                .forEach(System.out::println);

        List<Episodio> episodios = temporadas.stream().flatMap(t -> t.episodios().stream()
                .map(d -> new Episodio(t.numero(), d))).collect(Collectors.toList());

        episodios.forEach(System.out::println);

        System.out.println("A partir de qual ano deseja ver os episodios:");
        var ano = input.nextInt();
        input.nextLine();

        LocalDate dataBusca = LocalDate.of(ano, 1, 1);
        DateTimeFormatter formatador = DateTimeFormatter.ofPattern("dd/MM/yyyy");

        episodios.stream()
                .filter(e -> e.getDataLancamento() != null && e.getDataLancamento().isAfter(dataBusca))
                .forEach(e -> System.out.println(
                        "Temporada: " + e.getTemporada() +
                        " Episodio: " + e.getTitulo() +
                        " Data Lançamento: " + e.getDataLancamento().format(formatador)
                ));

    }
}
