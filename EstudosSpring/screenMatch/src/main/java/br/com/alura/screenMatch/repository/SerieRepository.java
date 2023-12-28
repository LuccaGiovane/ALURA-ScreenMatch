package br.com.alura.screenMatch.repository;

import br.com.alura.screenMatch.model.Serie;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SerieRepository extends JpaRepository<Serie, Long>
{

}
