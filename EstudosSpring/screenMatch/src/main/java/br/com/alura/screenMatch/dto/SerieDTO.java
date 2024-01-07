package br.com.alura.screenMatch.dto;

import br.com.alura.screenMatch.model.Categoria;

public record SerieDTO( long id,
                        String titulo,
                        Integer totalTemporadas,
                        Double avaliacao,
                        Categoria genero,
                        String atores,
                        String poster,
                        String sinopse) {
}
