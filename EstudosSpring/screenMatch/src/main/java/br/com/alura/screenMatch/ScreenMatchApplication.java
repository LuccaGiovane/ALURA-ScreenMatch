package br.com.alura.screenMatch;

import br.com.alura.screenMatch.model.DadosEpisodio;
import br.com.alura.screenMatch.model.DadosSerie;
import br.com.alura.screenMatch.model.DadosTemporada;
import br.com.alura.screenMatch.service.ConsumoApi;
import br.com.alura.screenMatch.service.ConverteDados;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.ArrayList;
import java.util.List;

@SpringBootApplication
public class ScreenMatchApplication implements CommandLineRunner
{

	public static void main(String[] args)
	{
		SpringApplication.run(ScreenMatchApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception
	{
		ConverteDados conversor = new ConverteDados();
		var consumoApi = new ConsumoApi();
		var json = consumoApi.obterDados("https://www.omdbapi.com/?t=stranger+things+&apikey=18ae168a");

		System.out.println(json);

		DadosSerie dados = conversor.obterDados(json, DadosSerie.class);
		System.out.println(dados);

		json = consumoApi.obterDados("https://www.omdbapi.com/?t=stranger+things+&season=1&episode=3&apikey=18ae168a");
		DadosEpisodio dadosEpisodeo = conversor.obterDados(json, DadosEpisodio.class);
		System.out.println(dadosEpisodeo);

		List<DadosTemporada> temporadas = new ArrayList<>();

		for(int i=1; i <= dados.totalTemporadas(); i++)
		{
			json = consumoApi.obterDados("https://www.omdbapi.com/?t=stranger+things+&season=" +i+ "&episode=3&apikey=18ae168a");
			DadosTemporada dadosTemporada = conversor.obterDados(json, DadosTemporada.class);

			temporadas.add(dadosTemporada);
		}

		temporadas.forEach(System.out::println);
	}
}
