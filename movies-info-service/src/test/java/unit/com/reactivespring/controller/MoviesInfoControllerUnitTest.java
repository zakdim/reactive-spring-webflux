package com.reactivespring.controller;

import com.reactivespring.domain.MovieInfo;
import com.reactivespring.service.MoviesInfoService;
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

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.Mockito.when;
import static org.junit.jupiter.api.Assertions.assertEquals;

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

    @Test
    void getAllMoviesInfo() {

        var movieInfos = List.of(
                new MovieInfo(null, "Batman Begins",
                        2005, List.of("Christian Bale", "Michael Cane"), LocalDate.parse("2005-06-15")),
                new MovieInfo(null, "The Dark Knight",
                        2008, List.of("Christian Bale", "Heath Ledger"), LocalDate.parse("2008-07-18")),
                new MovieInfo("abc", "Dark Knight Rises",
                        2012, List.of("Christian Bale", "Tom Hardy"), LocalDate.parse("2012-07-20"))
        );

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

    @Test
    void getMovieInfoById() {
        // given
        var movieInfoId = "abc";
        var movieInfo = new MovieInfo("abc", "Dark Knight Rises",
                2012, List.of("Christian Bale", "Tom Hardy"), LocalDate.parse("2012-07-20"));

        when(moviesInfoServiceMock.getMovieInfoById(anyString())).thenReturn(Mono.just(movieInfo));

        webTestClient
                .get()
                .uri(MOVIE_INFOS_URL + "/{id}", movieInfoId)
                .exchange()
                .expectStatus()
                .is2xxSuccessful()
//                .expectBody(MovieInfo.class)
//                .consumeWith(movieInfoEntityExchangeResult -> {
//                    var movieInfo = movieInfoEntityExchangeResult.getResponseBody();
//                    assertNotNull(movieInfo);
//                });
                .expectBody()
                .jsonPath("$.name").isEqualTo("Dark Knight Rises");
    }

    @Test
    void addMovieInfo() {
        // given
        var movieInfo = new MovieInfo(null, "Batman Begins1",
                2005, List.of("Christian Bale", "Michael Cane"), LocalDate.parse("2005-06-15"));

        when(moviesInfoServiceMock.addMovieInfo(isA(MovieInfo.class))).thenReturn(
                Mono.just(new MovieInfo("mockId", "Batman Begins1",
                        2005, List.of("Christian Bale", "Michael Cane"), LocalDate.parse("2005-06-15")))
        );

        webTestClient
                .post()
                .uri(MOVIE_INFOS_URL)
                .bodyValue(movieInfo)
                .exchange()
                .expectStatus()
                .isCreated()
                .expectBody(MovieInfo.class)
                .consumeWith(movieInfoEntityExchangeResult -> {

                    var savedMovieInfo = movieInfoEntityExchangeResult.getResponseBody();
                    assert savedMovieInfo != null;
                    assert savedMovieInfo.getMovieInfoId() != null;
                    assertEquals("mockId", savedMovieInfo.getMovieInfoId());
                });
    }

    @Test
    void addMovieInfo_validation() {
        // given
        var movieInfo = new MovieInfo(null, "",
                -2005, List.of("Christian Bale", "Michael Cane"), LocalDate.parse("2005-06-15"));

        when(moviesInfoServiceMock.addMovieInfo(isA(MovieInfo.class))).thenReturn(
                Mono.just(new MovieInfo("mockId", "Batman Begins1",
                        2005, List.of("Christian Bale", "Michael Cane"), LocalDate.parse("2005-06-15")))
        );

        webTestClient
                .post()
                .uri(MOVIE_INFOS_URL)
                .bodyValue(movieInfo)
                .exchange()
                .expectStatus()
                .isBadRequest()
                .expectBody(String.class)
                .consumeWith(stringEntityExchangeResult -> {
                    var responseBody = stringEntityExchangeResult.getResponseBody();
                    System.out.println("responseBody: " + responseBody);
                    assertNotNull(responseBody);
                })
//                .expectBody(MovieInfo.class)
//                .consumeWith(movieInfoEntityExchangeResult -> {
//
//                    var savedMovieInfo = movieInfoEntityExchangeResult.getResponseBody();
//                    assert savedMovieInfo != null;
//                    assert savedMovieInfo.getMovieInfoId() != null;
//                    assertEquals("mockId", savedMovieInfo.getMovieInfoId());
//                })
                ;
    }

    @Test
    void updateMovieInfo() {
        // given
        var movieInfoId = "abc";
        var movieInfo = new MovieInfo(null, "Dark Knight Rises1",
                2005, List.of("Christian Bale", "Michael Cane"), LocalDate.parse("2005-06-15"));
        // when
        when(moviesInfoServiceMock.updateMovieInfo(isA(MovieInfo.class), isA(String.class))).thenReturn(
                Mono.just(new MovieInfo(movieInfoId, "Dark Knight Rises1",
                        2005, List.of("Christian Bale", "Michael Cane"), LocalDate.parse("2005-06-15")))
        );

        webTestClient
                .put()
                .uri(MOVIE_INFOS_URL + "/{id}", movieInfoId)
                .bodyValue(movieInfo)
                .exchange()
                .expectStatus()
                .is2xxSuccessful()
                .expectBody(MovieInfo.class)
                .consumeWith(movieInfoEntityExchangeResult -> {

                    var updatedMovieInfo = movieInfoEntityExchangeResult.getResponseBody();
                    assert updatedMovieInfo != null;
                    assert updatedMovieInfo.getMovieInfoId() != null;
                    assertEquals("Dark Knight Rises1", updatedMovieInfo.getName());
                });

        // then
    }

    @Test
    void deleteMovieInfo() {
        // given
        var movieInfoId = "abc";

        // when
        when(moviesInfoServiceMock.deleteMovieInfo(anyString())).thenReturn(Mono.empty());

        webTestClient
                .delete()
                .uri(MOVIE_INFOS_URL + "/{id}", movieInfoId)
                .exchange()
                .expectStatus()
                .isNoContent()
                .expectBody()
                .isEmpty();
    }
}
