package com.reactivespring.repository;

import com.reactivespring.domain.MovieInfo;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.test.context.ActiveProfiles;
import reactor.test.StepVerifier;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@DataMongoTest
@ActiveProfiles("test")
class MovieInfoRepositoryIntgTest {

    @Autowired
    MovieInfoRepository movieInfoRepository;

    @BeforeEach
    void setUp() {
        var movieInfos = List.of(
                new MovieInfo(null, "Batman Begins",
                        2005, List.of("Christian Bale", "Michael Cane"), LocalDate.parse("2005-06-15")),
                new MovieInfo(null, "The Dark Knight",
                        2008, List.of("Christian Bale", "Heath Ledger"), LocalDate.parse("2008-07-18")),
                new MovieInfo("abc", "Dark Knight Rises",
                        2012, List.of("Christian Bale", "Tom Hardy"), LocalDate.parse("2012-07-20"))
                );
        movieInfoRepository.saveAll(movieInfos)
                .blockLast();
    }

    @AfterEach
    void tearDown() {
        movieInfoRepository.deleteAll().block();
    }

    @Test
    void findAll() {
        // given

        // when
        var moviesInfoFlux = movieInfoRepository.findAll().log();

        // then
        StepVerifier.create(moviesInfoFlux)
                .expectNextCount(3)
                .verifyComplete();
    }

    @Test
    void findById() {
        // given

        // when
        var movieInfoMono = movieInfoRepository.findById("abc").log();

        // then
        StepVerifier.create(movieInfoMono)
//                .expectNextCount(1)
                .assertNext(movieInfo -> {
                    assertEquals("Dark Knight Rises", movieInfo.getName());
                })
                .verifyComplete();
    }

    @Test
    void saveMovieInfo() {
        // given
        var movieInfo = new MovieInfo(null, "Joker",
                2019, List.of("Joaquin Phoenix", "Robert De Niro"), LocalDate.parse("2019-10-04"));

        // when
        var savedMovieInfo = movieInfoRepository.save(movieInfo).log();

        // then
        StepVerifier.create(savedMovieInfo)
//                .expectNextCount(1)
                .assertNext(m -> {
                    assertNotNull(m.getMovieInfoId());
                    assertEquals("Joker", m.getName());
                })
                .verifyComplete();
    }

    @Test
    void updateMovieInfo() {
        // given
        var movieInfo = movieInfoRepository.findById("abc").block();
        movieInfo.setYear(2021);

        // when
        var updatedMovieInfo = movieInfoRepository.save(movieInfo).log();

        // then
        StepVerifier.create(updatedMovieInfo)
                .assertNext(m -> {
                    assertEquals(2021, m.getYear());
                })
                .verifyComplete();
    }

    @Test
    void deleteMovieInfo() {
        // given

        // when
        movieInfoRepository.deleteById("abc").block();
        var moviesInfoFlux = movieInfoRepository.findAll().log();

        // then
        StepVerifier.create(moviesInfoFlux)
                .expectNextCount(2)
                .verifyComplete();
    }
}