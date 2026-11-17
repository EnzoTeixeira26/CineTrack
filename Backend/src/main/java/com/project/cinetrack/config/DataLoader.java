package com.project.cinetrack.config;

import com.project.cinetrack.modeloBD.Genero;
import com.project.cinetrack.modeloBD.Obra;
import com.project.cinetrack.repositories.GeneroRepository;
import com.project.cinetrack.repositories.ObraRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Configuration
public class DataLoader {

    @Bean
    CommandLineRunner initDatabase(GeneroRepository generoRepository,
                                   ObraRepository obraRepository) {
        return args -> {
            seedGeneros(generoRepository);
            seedObras(generoRepository, obraRepository);
        };
    }


    private void seedGeneros(GeneroRepository generoRepository) {
        List<Genero> padrao = List.of(
                criarGenero("Ação", "acao"),
                criarGenero("Comédia", "comedia"),
                criarGenero("Drama", "drama"),
                criarGenero("Terror", "terror"),
                criarGenero("Ficção Científica", "ficcao-cientifica"),
                criarGenero("Romance", "romance"),
                criarGenero("Documentário", "documentario"),
                criarGenero("Animação", "animacao")
                
        );

        int inseridos = 0;
        for (Genero g : padrao) {
            if (generoRepository.findBySlug(g.getSlug()).isEmpty()) {
                generoRepository.save(g);
                inseridos++;
            }
        }
        log(inseridos, "gênero(s)", "Seed de gêneros");
    }

    private Genero criarGenero(String nome, String slug) {
        Genero g = new Genero();
        g.setNome(nome);
        g.setSlug(slug);
        return g;
    }


    private void seedObras(GeneroRepository generoRepository,
                           ObraRepository obraRepository) {

        Genero acao    = generoRepository.findBySlug("acao").orElse(null);
        Genero comedia = generoRepository.findBySlug("comedia").orElse(null);
        Genero drama   = generoRepository.findBySlug("drama").orElse(null);
        Genero terror  = generoRepository.findBySlug("terror").orElse(null);
        Genero ficcao  = generoRepository.findBySlug("ficcao-cientifica").orElse(null);
        Genero romance = generoRepository.findBySlug("romance").orElse(null);

        List<Obra> obras = List.of(
                criarObra("La La Land: Cantando Estações", "La La Land", "FILME",
                        "Mia, uma aspirante a atriz, e Sebastian, um músico de jazz, se apaixonam em Los Angeles, mas o sucesso profissional ameaça destruir o relacionamento dos dois.",
                        LocalDate.of(2016, 12, 9),
                        "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcSdYnBTgj1n0vQexYwHn4He35lCEXhEVRPYri20oXHd6bQqKT7EGhEhwx0&s=10",
                        "https://m.media-amazon.com/images/S/pv-target-images/a0f75a1dc5f67043f904ed772fd6c9c4ac443c434628276ffc73f013e7becd39.jpg",
                        129, null, 313369, generos(terror, drama, comedia)),

                criarObra("500 Dias com Ela", "(500) Days of Summer", "FILME",
                        "Tom, um romântico incorrigível, se apaixona por Summer, uma garota que não acredita em amor. Quando ela termina o relacionamento, ele revisita os 500 dias que passaram juntos.",
                        LocalDate.of(2009, 7, 17),
                        "https://blogfca.pucminas.br/ccm/wp-content/uploads/2025/02/WhatsApp-Image-2025-02-12-at-15.44.41-716x1024.jpeg", // fallback seguro
                        "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcRzqP8Z5XWf9YeEFGiXDiL3hnY-FwXt03l_h9enqWrd3nhGaUewF8uydAnW&s=10",
                        95, null, 19913, generos(terror, comedia, drama)),

                criarObra("Blue Valentine", "Blue Valentine", "FILME",
                        "Dean e Cindy tentam salvar um casamento em ruínas enquanto flashbacks mostram o início apaixonado do relacionamento e o desgaste que os levou ao divórcio.",
                        LocalDate.of(2010, 12, 29),
                        "https://m.media-amazon.com/images/S/pv-target-images/05ca59a46cc7c5768f3b7f5c99dd5e909d0c01bf5383e595348087855b64d22b.jpg", // fallback
                        "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcTwKrg8I24GgcCqyn6yJ1KMV656qA2plhGCElCAC_tMRFmpxndsVOl1uuM&s=10",
                        112, null, 46705, generos(terror, drama, romance)),

                criarObra("Brilho Eterno de uma Mente sem Lembranças", "Eternal Sunshine of the Spotless Mind", "FILME",
                        "Joel descobre que sua ex-namorada Clementine apagou todas as memórias dele. Desesperado, ele decide fazer o mesmo, mas durante o procedimento percebe que ainda a ama.",
                        LocalDate.of(2004, 3, 19),
                        "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcQeekle5wwpCZXqJZ1zPU1JtUluGFzA0_vpZ3hV1a9uwbgUGyPkG0c8L5nU0VU232-xLLSm&s=10",
                        "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcSpB983tcAJ8ZJs-9AkHibDqqo_TY9SvFdc5TyvrFMTbQ&s=10",
                        108, null, 38, generos(terror, ficcao, drama)),


                criarObra("O Iluminado", "The Shining", "FILME",
                        "Jack Torrance aceita um emprego como zelador de um hotel isolado nas montanhas, onde forças sobrenaturais começam a afetar sua sanidade e a de sua família.",
                        LocalDate.of(1980, 5, 23),
                        "https://image.tmdb.org/t/p/w500/b6ko0IKC8MdYBBPkkA1aBPLe2yz.jpg",
                        "https://image.tmdb.org/t/p/w1280/9fgh3Ns1iRzlQNYuJyK0ARQZU7w.jpg",
                        146, null, 694, generos(terror, drama)),

                criarObra("O Exorcista", "The Exorcist", "FILME",
                        "Quando uma garota de 12 anos é possuída por uma entidade demoníaca, dois padres tentam realizar um exorcismo para salvá-la.",
                        LocalDate.of(1973, 12, 26),
                        "https://image.tmdb.org/t/p/w500/4ucLGcXVVSVnsfkGtbLY4XAius8.jpg",
                        "https://image.tmdb.org/t/p/w1280/9fgh3Ns1iRzlQNYuJyK0ARQZU7w.jpg",
                        122, null, 9552, generos(terror)),

                criarObra("Hereditário", "Hereditary", "FILME",
                        "Após a morte da matriarca da família Leigh, segredos perturbadores sobre a herança familiar vêm à tona e colocam todos em perigo.",
                        LocalDate.of(2018, 6, 8),
                        "https://image.tmdb.org/t/p/w500/4GFPuL14eXi66V96xBWY73Y9PfR.jpg",
                        "https://image.tmdb.org/t/p/w1280/pS9Aub8MPyQbENblGD8mHeaqMuv.jpg",
                        127, null, 493922, generos(terror, drama)),

                criarObra("Corra!", "Get Out", "FILME",
                        "Um jovem negro visita a família da namorada branca em uma mansão isolada e descobre uma verdade aterrorizante sobre seus anfitriões.",
                        LocalDate.of(2017, 2, 24),
                        "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcRWpFo8WZSfMrl5IQ55_oj32qDIsMMAIz6DpZeam-O-vH3NHGXMD7g-mlO8&s=10", // fallback
                        "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcSFkvgSTiJkoLoJMUQ1QpP8rPgA6z_0hzOzJ3QEus0wGg&s=10",
                        104, null, 419430, generos(terror, drama)),

                criarObra("A Bruxa", "The Witch", "FILME",
                        "Na Nova Inglaterra do século XVII, uma família puritana é expulsa da comunidade e se instala à beira de uma floresta, onde forças malignas começam a assombrá-los.",
                        LocalDate.of(2016, 2, 19),
                        "https://br.web.img3.acsta.net/pictures/16/02/02/11/51/346769.jpg", // fallback
                        "https://rollingstone.com.br/wp-content/uploads/anya_taylor-joy_em_a_bruxa_foto_divulgacao__a24__robert_eggers_foto_jeff_spicergetty_images.jpg",
                        92, null, 196396, generos(terror, drama)),

                criarObra("Alien: Romulus", "Alien: Romulus", "FILME",
                        "Um grupo de jovens colonos espaciais explora uma estação abandonada e se depara com a forma de vida mais aterrorizante do universo.",
                        LocalDate.of(2024, 8, 16),
                        "https://image.tmdb.org/t/p/w500/m2zXTuNPkywdYLyWlVyJZW2QOJH.jpg", // fallback
                        "https://image.tmdb.org/t/p/w1280/9fgh3Ns1iRzlQNYuJyK0ARQZU7w.jpg",
                        119, null, 945961, generos(terror, ficcao)),


                criarObra("Batman", "The Batman", "FILME",
                        "Quando um assassino sádico começa a assassinar figuras políticas importantes de Gotham City, Batman é forçado a investigar a corrupção oculta na cidade.",
                        LocalDate.of(2022, 3, 3),
                        "https://image.tmdb.org/t/p/w500/8UlWHLMpgZm9bx6QYh0NFoq67TZ.jpg",
                        "https://image.tmdb.org/t/p/w1280/b0PlSFdDwbyK0cf5RxwDpaOJQvQ.jpg",
                        176, null, 414906, generos(acao, drama)),

                criarObra("Coringa", "Joker", "FILME",
                        "Arthur Fleck, um comediante fracassado, luta para encontrar seu lugar na sociedade e acaba se transformando no vilão Coringa.",
                        LocalDate.of(2019, 10, 3),
                        "https://image.tmdb.org/t/p/w500/qJ2tW6WMUDux911r6m7haRef0WH.jpg",
                        "https://image.tmdb.org/t/p/w1280/n6bUvigpRFqSwmPp1m2YADdbRBc.jpg",
                        122, null, 475557, generos(drama)),

                criarObra("Clube da Luta", "Fight Club", "FILME",
                        "Um funcionário de escritório insone e um vendedor de sabonetes desiludido formam um clube de luta clandestino que cresce para algo muito maior. Lembre-se, você não fala sobre o ***** ** ****",
                        LocalDate.of(1999, 10, 15),
                        "https://image.tmdb.org/t/p/w500/pB8BM7pdSp6B6Ih7QZ4DrQ3PmJK.jpg",
                        "https://image.tmdb.org/t/p/w1280/hZkgoQYus5vegHoetLkCJzb17zJ.jpg",
                        139, null, 550, generos(drama)),

                criarObra("Duna", "Dune", "FILME",
                        "Paul Atreides viaja para o planeta mais perigoso do universo para garantir o futuro de sua família e seu povo.",
                        LocalDate.of(2021, 10, 22),
                        "https://image.tmdb.org/t/p/w500/1E5baAaEse26fej7uHcjOgEE2t2.jpg",
                        "https://image.tmdb.org/t/p/w1280/jYEW5xZkZk2WTrdbMGAPFuBqbDc.jpg",
                        155, null, 438631, generos(ficcao, drama)),

                criarObra("O Poderoso Chefão", "The Godfather", "FILME",
                        "A história da família Corleone, uma das mais poderosas famílias do crime organizado de Nova York.",
                        LocalDate.of(1972, 3, 14),
                        "https://image.tmdb.org/t/p/w500/3bhkrj58Vtu7enYsRolD1fZdja1.jpg",
                        "https://image.tmdb.org/t/p/w1280/tSPT36ZKlP2Wg7Msx5hH2nQOhw0.jpg",
                        175, null, 238, generos(drama)),

                criarObra("Parasita", "기생충", "FILME",
                        "A família Ki-taek se infiltra na vida da rica família Park, mas um segredo chocante ameaça seu esquema.",
                        LocalDate.of(2019, 5, 30),
                        "https://image.tmdb.org/t/p/w500/2LqaLgk4Z226KkgPJuiOQ58wvrm.jpg",
                        "https://image.tmdb.org/t/p/w1280/TU9NIjwzjoKPwQHoHshkFcQUCG.jpg",
                        132, null, 496243, generos(drama, comedia)),

                criarObra("Interestelar", "Interstellar", "FILME",
                        "Um grupo de exploradores viaja por um buraco de minhoca em busca de um novo lar para a humanidade.",
                        LocalDate.of(2014, 11, 6),
                        "https://image.tmdb.org/t/p/w500/gEU2QniE6E77NI6lCU6MxlNBvIx.jpg",
                        "https://image.tmdb.org/t/p/w1280/xJHokMbljvjADYdit5fK5VQsXEG.jpg",
                        169, null, 157336, generos(ficcao, drama)),

                criarObra("A Origem", "Inception", "FILME",
                        "Um ladrão que rouba segredos corporativos através de sonhos recebe a tarefa de plantar uma ideia na mente de um CEO.",
                        LocalDate.of(2010, 7, 16),
                        "https://image.tmdb.org/t/p/w500/9gk7adHYeDvHkCSEqAvQNLV5Uge.jpg",
                        "https://image.tmdb.org/t/p/w1280/s3TBrRGB1iav7gFOCNx3H31MoES.jpg",
                        148, null, 27205, generos(ficcao, acao)),

                criarObra("Matrix", "The Matrix", "FILME",
                        "Um hacker descobre que a realidade é uma simulação criada por máquinas e se junta à rebelião.",
                        LocalDate.of(1999, 5, 21),
                        "https://image.tmdb.org/t/p/w500/f89U3ADr1oiB1s9GkdPOEpXUk5H.jpg",
                        "https://image.tmdb.org/t/p/w1280/fNG7i7RqMErkcqhohV2a6cV1Ehy.jpg",
                        136, null, 603, generos(ficcao, acao)),

                criarObra("Vingadores: Ultimato", "Avengers: Endgame", "FILME",
                        "Os Vingadores restantes se reúnem para reverter o estalo de Thanos e restaurar o universo.",
                        LocalDate.of(2019, 4, 25),
                        "https://image.tmdb.org/t/p/w500/or06FN3Dka5tukK1e9sl16pB3iy.jpg",
                        "https://image.tmdb.org/t/p/w1280/7RyHsO4yDXtBv1zUU3mTpHeQ0d5.jpg",
                        181, null, 299534, generos(acao, ficcao)),

                criarObra("Whiplash: Em Busca da Perfeição", "Whiplash", "FILME",
                        "Um jovem baterista é levado ao limite por um professor abusivo que fará de tudo para realizar seu potencial.",
                        LocalDate.of(2014, 10, 10),
                        "https://image.tmdb.org/t/p/w500/7fn624j5lj3xTme2SgiLCeuedmO.jpg",
                        "https://image.tmdb.org/t/p/w1280/6bbZ6XyvgfjhQwbplnUh1LSj1ky.jpg",
                        106, null, 244786, generos(drama)),


                criarObra("Dexter", "Dexter", "SERIE",
                        "Dexter Morgan é um analista forense da polícia de Miami que leva uma vida secreta como serial killer, caçando criminosos que escaparam da justiça.",
                        LocalDate.of(2006, 10, 1),
                        "https://image.tmdb.org/t/p/w500/58H6Ctze1nnpS0s9vPmAAzPcipR.jpg",
                        "https://image.tmdb.org/t/p/w1280/fIKc2cR1GglarzChMAb4BOP1qHP.jpg",
                        null, 8, 1405, generos(drama, terror)),

                criarObra("Dexter: New Blood", "Dexter: New Blood", "SERIE",
                        "Dez anos após desaparecer no olho do furacão Laura, Dexter vive sob nome falso na pequena cidade de Iron Lake, Nova York.",
                        LocalDate.of(2021, 11, 7),
                        "https://m.media-amazon.com/images/M/MV5BZGY4YjY5MGItNzM2NS00Y2M1LWFkYTYtZjI0ZDk3NTE0NmE0XkEyXkFqcGc@._V1_.jpg", // fallback
                        "https://m.media-amazon.com/images/M/MV5BMTM3MmFlNmUtNTUzNy00YjQ4LWJkZjctMGU0YTg4ZGNiOWIwXkEyXkFqcGc@._V1_.jpg",
                        null, 1, 131927, generos(drama, terror)),

                criarObra("Dexter: Original Sin", "Dexter: Original Sin", "SERIE",
                        "Em 1991, em Miami, o jovem Dexter Morgan passa de estudante a assassino em série vingativo, com a orientação de seu pai, Harry.",
                        LocalDate.of(2024, 12, 13),
                        "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcSU0srloc5tJ_8rHh-3idw-FBEp9K0VR6yvFlRjKBrk0g&s=10", // fallback
                        "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcQ2TZiTD8I0y7gKQJOWajDawjSOPb_bdBT6OujPNXDZunu3cAeja-AUao4&s=10",
                        null, 1, 219937, generos(drama, terror)),

                criarObra("Dexter: Resurrection", "Dexter: Resurrection", "SERIE",
                        "Dexter Morgan acorda de um coma e parte para Nova York em busca de seu filho Harrison, enquanto seu passado o alcança.",
                        LocalDate.of(2025, 7, 11),
                        "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcQDYqNZO25YTM_fCkvIvTsllJVtcfNrJMkCGy2NkWcfyE_BoLYxzyznZ4-5&s=10", // fallback
                        "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcQqn8DYolVTuLMM47VyF3qRRa0HzquL3aSi3WbCY1dbhcisZUvOqYLru1bY&s=10",
                        null, 1, 259909, generos(drama, terror)),

                criarObra("Prison Break", "Prison Break", "SERIE",
                        "Michael Scofield se infiltra na prisão onde seu irmão está no corredor da morte para ajudá-lo a escapar.",
                        LocalDate.of(2005, 8, 29),
                        "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcSUZaAnWPCNK7Kwm6hDd1svQmIibnT3FSGl1CheGKJTFQ&s=10", // fallback
                        "https://m.media-amazon.com/images/S/pv-target-images/06a81368395186e55ef7e435b0967d732a1bbd7572e111d533a3e554c4c7e8b9._SX1080_FMjpg_.jpg",
                        null, 5, 2288, generos(acao, drama)),

                criarObra("Breaking Bad", "Breaking Bad", "SERIE",
                        "Um professor de química diagnosticado com câncer decide produzir e vender metanfetamina para garantir o futuro da família.",
                        LocalDate.of(2008, 1, 20),
                        "https://image.tmdb.org/t/p/w500/ggFHVNu6YYI5L9pCfOacjizRGt.jpg",
                        "https://image.tmdb.org/t/p/w1280/tsRy63Mu5cu8etL1X7ZLyf7UP1M.jpg",
                        null, 5, 1396, generos(drama)),

                criarObra("Stranger Things", "Stranger Things", "SERIE",
                        "Quando um menino desaparece, uma pequena cidade descobre um mistério envolvendo experimentos secretos e forças sobrenaturais.",
                        LocalDate.of(2016, 7, 15),
                        "https://image.tmdb.org/t/p/w500/49WJfeN0moxb9IPfGn8AIqMGskD.jpg",
                        "https://image.tmdb.org/t/p/w1280/56v2KjBlU4XaOv9rVYEQypROD7P.jpg",
                        null, 4, 66732, generos(terror, ficcao)),

                criarObra("Round 6", "오징어 게임", "SERIE",
                        "Centenas de pessoas com dificuldades financeiras aceitam competir em jogos infantis por um prêmio tentador, mas fatal.",
                        LocalDate.of(2021, 9, 17),
                        "https://image.tmdb.org/t/p/w500/dDlEmu3EZ0Pgg93K2SVNLCjCSvE.jpg",
                        "https://image.tmdb.org/t/p/w1280/oaGvjB0DvdhXhOAuADfHb261ZHa.jpg",
                        null, 2, 93405, generos(drama, terror)),

                criarObra("The Last of Us", "The Last of Us", "SERIE",
                        "Vinte anos após a destruição da civilização, Joel é contratado para contrabandear Ellie para fora de uma zona de quarentena.",
                        LocalDate.of(2023, 1, 15),
                        "https://image.tmdb.org/t/p/w500/uKvVjHNqB5VmOrdxqAt2F7J78ED.jpg",
                        "https://image.tmdb.org/t/p/w1280/uDgy6hyPd82kOHh6I95FLtLnj6p.jpg",
                        null, 1, 100088, generos(drama, terror)),

                criarObra("Game of Thrones", "Game of Thrones", "SERIE",
                        "Nove famílias nobres lutam pelo controle das terras míticas de Westeros enquanto um antigo inimigo retorna.",
                        LocalDate.of(2011, 4, 17),
                        "https://image.tmdb.org/t/p/w500/1XS1oqL89opfnbLl8WnZY1O1uJx.jpg",
                        "https://image.tmdb.org/t/p/w1280/2OMB0ynKlyIenMJWI2Dy9IWT4c.jpg",
                        null, 8, 1399, generos(drama))
        );

        int inseridos = 0;
        for (Obra obra : obras) {
            if (obra.getTmdbId() != null
                    && obraRepository.existsByTmdbId(obra.getTmdbId())) {
                continue;
            }
            obraRepository.save(obra);
            inseridos++;
        }
        log(inseridos, "obra(s)", "Seed de obras");
    }

    private Obra criarObra(String titulo, String tituloOriginal, String tipo,
                           String sinopse, LocalDate dataLancamento,
                           String posterUrl, String backdropUrl,
                           Integer duracaoMinutos, Integer numeroTemporadas,
                           Integer tmdbId, Set<Genero> generos) {
        Obra o = new Obra();
        o.setTitulo(titulo);
        o.setTituloOriginal(tituloOriginal);
        o.setTipo(tipo);
        o.setSinopse(sinopse);
        o.setDataLancamento(dataLancamento);
        o.setPosterUrl(posterUrl);
        o.setBackdropUrl(backdropUrl);
        o.setDuracaoMinutos(duracaoMinutos);
        o.setNumeroTemporadas(numeroTemporadas);
        o.setTmdbId(tmdbId);
        o.setNotaMedia(BigDecimal.ZERO);
        o.setGeneros(generos);
        return o;
    }

    private Set<Genero> generos(Genero... generos) {
        Set<Genero> set = new HashSet<>();
        for (Genero g : generos) {
            if (g != null) set.add(g);
        }
        return set;
    }

    private void log(int inseridos, String unidade, String prefixo) {
        if (inseridos > 0) {
            System.out.println(prefixo + ": " + inseridos + " novo(s) " + unidade + " inserido(s).");
        } else {
            System.out.println(prefixo + ": nada a fazer (tudo já existe).");
        }
    }
}