package br.com.alura.screenMatch.model;

public enum Categoria
{
    DOCUMENTARIO("Documentary"),
    CURTA_METRAGEM("Short"),
    DRAMA("Drama"),
    COMEDIA("Comedy"),
    NOTICIAS("News"),
    TALK_SHOW("Talk-Show"),
    ROMANCE("Romance"),
    BIOGRAFIA("Biography"),
    FANTASIA("Fantasy"),
    ANIMACAO("Animation"),
    HISTORIA("History"),
    FAMILIA("Family"),
    FICCAO_CIENTIFICA("Sci-Fi"),
    AVENTURA("Adventure"),
    MUSICA("Music"),
    ACAO("Action"),
    HORROR("Horror"),
    MISTERIO("Mystery"),
    MUSICAL("Musical"),
    REALITY_SHOW("Reality-TV"),
    ESPORTE("Sport"),
    CRIME("Crime"),
    SUSPENSE("Thriller"),
    GAME_SHOW("Game-Show"),
    GUERRA("War"),
    FAROESTE("Western");

    private String categoriaOmdb;

    Categoria(String categoriaOMDB)
    {
        this.categoriaOmdb = categoriaOMDB;
    }

    /*Basicamente esse método vai dinamicamente  interpretar o valor que veio textualmente do OMDB
     e transformar no enum*/
    public static Categoria fromString(String text)
    {
        for (Categoria categoria : Categoria.values())
        {
            if (categoria.categoriaOmdb.equalsIgnoreCase(text))
            {
                return categoria;
            }
        }
        throw new IllegalArgumentException("Nenhuma categoria encontrada para a string fornecida: " + text);
    }

}
