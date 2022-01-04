package com.reactivespring.controller;

import com.reactivespring.domain.MovieInfo;
import com.reactivespring.service.MoviesInfoService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDate;
import java.util.List;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

/**
 * Created by dmitri on 2022-01-03.
 */
@WebFluxTest(controllers = MoviesInfoController.class)
@AutoConfigureWebTestClient
public class MoviesInfoControllerUnitTest {

    @Autowired
    private WebTestClient webTestClient;

    @MockBean
    private MoviesInfoService moviesInfoServiceMock;

    static final String MOVIE_INFOS_URL = "/v1/movieinfos";

    private List<MovieInfo> movieInfos;

    @BeforeEach
    void setUp() {
        movieInfos = List.of(
                new MovieInfo(null, "Batman Begins",
                        2005, List.of("Christian Bale", "Michael Cane"), LocalDate.parse("2005-06-15")),
                new MovieInfo(null, "The Dark Knight",
                        2008, List.of("Christian Bale", "Heath Ledger"), LocalDate.parse("2008-07-18")),
                new MovieInfo("abc", "Dark Knight Rises",
                        2012, List.of("Christian Bale", "Tom Hardy"), LocalDate.parse("2012-07-20"))
        );
    }

    @Test
    void getAllMoviesInfo() {

        when(moviesInfoServiceMock.getAllMovieInfos()).thenReturn(Flux.fromIterable(movieInfos));

        webTestClient
                .get()
                .uri(MOVIE_INFOS_URL)
                .exchange()
                .expectStatus()
                .is2xxSuccessful()
                .expectBodyList(MovieInfo.class)
                .hasSize(3);
    }

    private MovieInfo getTestMovieInfo(String movieInfoId) {
        return movieInfos.stream()
                .filter(movieInfo -> movieInfoId.equalsIgnoreCase(movieInfo.getMovieInfoId()))
                .findFirst()
                .get();
    }

    @Test
    void getMovieInfoById() {
        // given
        var movieInfoId = "abc";
        var testMovieInfo = getTestMovieInfo(movieInfoId);

        when(moviesInfoServiceMock.getMovieInfoById(anyString())).thenReturn(Mono.just(testMovieInfo));

        webTestClient
                .get()
                .uri(MOVIE_INFOS_URL + "/{id}", movieInfoId)
                .exchange()
                .expectStatus()
                .is2xxSuccessful()
                .expectBody()
                .jsonPath("$.name").isEqualTo("Dark Knight Rises");
    }
}
