package com.reactivespring.routes;

import com.reactivespring.domain.Review;
import com.reactivespring.repository.ReviewReactiveRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.reactive.server.WebTestClient;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
@AutoConfigureWebTestClient
public class ReviewsIntgTest {

    @Autowired
    WebTestClient webTestClient;

    @Autowired
    ReviewReactiveRepository reviewReactiveRepository;

    static String REVIEWS_URL = "/v1/reviews";

    @BeforeEach
    void setUp() {
        var reviewsList = List.of(
                new Review(null, 1L, "Awesome Movie", 9.0),
                new Review(null, 1L, "Awesome Movie1", 9.0),
                new Review("abc", 2L, "Excellent Movie", 8.0));
        reviewReactiveRepository.saveAll(reviewsList)
                .blockLast();
    }

    @AfterEach
    void tearDown() {
        reviewReactiveRepository.deleteAll().block();
    }

    @Test
    void addReview() {
        // given
        var review = new Review(null, 1L, "Awesome Movie", 9.0);

        // then
        webTestClient
                .post()
                .uri(REVIEWS_URL)
                .bodyValue(review)
                .exchange()
                .expectStatus()
                .isCreated()
                .expectBody(Review.class)
                .consumeWith(reviewEntityExchangeResult -> {

                    var savedReview = reviewEntityExchangeResult.getResponseBody();
                    assert savedReview != null;
                    assert savedReview.getReviewId() != null;
                });
    }

    @Test
    void addReview_validation() {
        // given
        var review = new Review(null, null, "Awesome Movie", -9.0);

        // then
        webTestClient
                .post()
                .uri(REVIEWS_URL)
                .bodyValue(review)
                .exchange()
                .expectStatus()
                .isBadRequest();

    }

    @Test
    void getAllReviews() {

        webTestClient
                .get()
                .uri(REVIEWS_URL)
                .exchange()
                .expectStatus()
                .is2xxSuccessful()
                .expectBodyList(Review.class)
//                .hasSize(3);
                .value(reviews -> {
                    assertEquals(3, reviews.size());
                });
    }

    @Test
    void getReviewsByMovieInfoId() {

        // given
//        var uri = UriComponentsBuilder.fromUriString(REVIEWS_URL)
//                .queryParam("movieInfoId", "1")
//                .buildAndExpand().toUri();

        // when
        webTestClient
                .get()
//                .uri(uri)
                .uri(uriBuilder -> {
                    return uriBuilder.path(REVIEWS_URL)
                            .queryParam("movieInfoId", "1")
                            .build();
                })
                .exchange()
                .expectStatus()
                .is2xxSuccessful()
                .expectBodyList(Review.class)
                .hasSize(2);
    }

    @Test
    void updateReview() {
        // given
        var reviewId = "abc";
        var reviewUpdate = new Review(null, 2L, "Excellent Movie1", 8.5);

        // then
        webTestClient
                .put()
                .uri(REVIEWS_URL + "/{id}", reviewId)
                .bodyValue(reviewUpdate)
                .exchange()
                .expectStatus()
                .is2xxSuccessful()
                .expectBody(Review.class)
                .consumeWith(reviewEntityExchangeResult -> {

                    var updatedReview = reviewEntityExchangeResult.getResponseBody();
                    assert updatedReview != null;
                    assert updatedReview.getReviewId().equals("abc");
                    assertEquals("Excellent Movie1", updatedReview.getComment());
                    assertEquals(8.5D, updatedReview.getRating());
                });
    }

    @Test
    void deleteReview() {
        // given
        var reviewId = "abc";

        // then
        webTestClient
                .delete()
                .uri(REVIEWS_URL + "/{id}", reviewId)
                .exchange()
                .expectStatus()
                .isNoContent();
    }
}
