package br.com.alura.screenMatch.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SerieController
{

    @GetMapping("/series")
    public String obterSeries()
    {
        return "Aqui serão listadas as séries";
    }

}
