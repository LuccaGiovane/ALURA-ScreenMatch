//package br.com.alura.screenMatch;
//
//import br.com.alura.screenMatch.principal.Principal;
//import br.com.alura.screenMatch.repository.SerieRepository;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.CommandLineRunner;
//import org.springframework.boot.SpringApplication;
//import org.springframework.boot.autoconfigure.SpringBootApplication;
//
//@SpringBootApplication
//public class ScreenMatchApplicationSemWeb implements CommandLineRunner
//{
//	@Autowired
//	private SerieRepository repositorio;
//	public static void main(String[] args)
//	{
//		SpringApplication.run(ScreenMatchApplicationSemWeb.class, args);
//	}
//
//	@Override
//	public void run(String... args) throws Exception
//	{
//		Principal principal = new Principal(repositorio);
//
//		principal.exibeMenu();
//
//	}
//}
