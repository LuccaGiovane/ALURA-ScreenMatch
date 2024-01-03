package br.com.alura.screenMatch.model;

public enum Categoria
{
    DOCUMENTARIO("Documentary", "Documentário"),
CURTA_METRAGEM("Short", "Curta-Metragem"),
DRAMA("Drama", "Drama"),
COMEDIA("Comedy", "Comédia"),
NOTICIAS("News", "Notícias"),
TALK_SHOW("Talk-Show", "Talk-Show"),
ROMANCE("Romance", "Romance"),
BIOGRAFIA("Biography", "Biografia"),
FANTASIA("Fantasy", "Fantasia"),
ANIMACAO("Animation", "Animação"),
HISTORIA("History", "História"),
FAMILIA("Family", "Família"),
FICCAO_CIENTIFICA("Sci-Fi", "Ficção Científica"),
AVENTURA("Adventure", "Aventura"),
MUSICA("Music", "Música"),
ACAO("Action", "Ação"),
HORROR("Horror", "Horror"),
MISTERIO("Mystery", "Mistério"),
MUSICAL("Musical", "Musical"),
REALITY_SHOW("Reality-TV", "Reality Show"),
ESPORTE("Sport", "Esporte"),
CRIME("Crime", "Crime"),
SUSPENSE("Thriller", "Suspense"),
GAME_SHOW("Game-Show", "Game Show"),
GUERRA("War", "Guerra"),
FAROESTE("Western", "Faroeste");


    private String categoriaOmdb;
    private String categoriaPortugues;

    Categoria(String categoriaOMDB, String categoriaPortugues)
    {
        this.categoriaOmdb = categoriaOMDB;
        this.categoriaPortugues = categoriaPortugues;
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

        public static Categoria fromPortugues(String text)
        {
            for (Categoria categoria : Categoria.values())
            {
                    if (categoria.categoriaPortugues.equalsIgnoreCase(text))
                    {
                            return categoria;
                    }
            }
            throw new IllegalArgumentException("Nenhuma categoria encontrada para a string fornecida: " + text);
    }


}
