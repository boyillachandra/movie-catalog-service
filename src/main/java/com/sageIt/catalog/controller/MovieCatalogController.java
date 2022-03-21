package com.sageIt.catalog.controller;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;

import com.sageIt.catalog.constants.Constants;
import com.sageIt.catalog.models.CatalogItem;
import com.sageIt.catalog.models.Movie;
import com.sageIt.catalog.models.UserRating;

@RestController
@RequestMapping("/catalog")
public class MovieCatalogController {

	@Autowired
	private RestTemplate restTemplate;
	

	@Autowired
	private WebClient.Builder webClientBuilder;

	@RequestMapping("/{userId}")
	public List<CatalogItem> getCatalog(@PathVariable String userId) {
		System.out.println("MovieCatalogController ::: getCatalog() started");
		/*
		 * WebClient.Builder builder = WebClient.builder(); List<Rating> ratingList =
		 * Arrays.asList(new Rating("123",4),new Rating("5678", 3)); UserRating
		 * userRating =
		 * restTemplate.getForObject("http://localhost:9003/ratingsdata/users/3",
		 * UserRating.class );
		 */

		UserRating userRating = restTemplate.getForObject(Constants.RATING_SERVICE_ENDPOINT + "users/" + userId,
				UserRating.class);
		return userRating.getRatings().stream().map(rating -> {
			/*
			 * Movie movie = webClientBuilder.build() .get()
			 * .uri("http://localhost:8082/movies/"+rating.getMovieId()) .retrieve()
			 * .bodyToMono(Movie.class) .block();
			 */

			Movie movie = restTemplate.getForObject(Constants.MOVIE_SERVICE_ENDPOINT + rating.getMovieId(), Movie.class);
			return new CatalogItem(movie.getName(), movie.getDescription(), rating.getRating());
		}).collect(Collectors.toList());

	}
}
