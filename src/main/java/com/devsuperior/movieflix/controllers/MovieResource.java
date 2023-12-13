package com.devsuperior.movieflix.controllers;

import com.devsuperior.movieflix.dto.MovieCardDTO;
import com.devsuperior.movieflix.dto.MovieDetailsDTO;
import com.devsuperior.movieflix.dto.ReviewDTO;
import com.devsuperior.movieflix.entities.User;
import com.devsuperior.movieflix.services.AuthService;
import com.devsuperior.movieflix.services.MovieService;
import com.devsuperior.movieflix.services.exceptions.UnauthorizedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping(value = "/movies")
public class MovieResource {

	@Autowired
	private AuthService authService;
	@Autowired
	private MovieService service;

	@GetMapping(value = "/{id}")
	public ResponseEntity<MovieDetailsDTO> findById(@PathVariable Long id) {
		verifyUser();
    	MovieDetailsDTO dto = service.findById(id);
		return ResponseEntity.ok().body(dto);
	}
	@GetMapping
	public ResponseEntity<Page<MovieCardDTO>> findByGenre(
			@RequestParam(name = "genreId", defaultValue = "0") Long genreId,
			Pageable pageable) {
		verifyUser();
		Page<MovieCardDTO> page = service.findByGenre(genreId, pageable);
		return ResponseEntity.ok().body(page);
	}

	@GetMapping(value = "/{id}/reviews")
	public ResponseEntity<List<ReviewDTO>> findReviews(@PathVariable Long id) {
		verifyUser();
		List<ReviewDTO> page = service.findReview(id);
		return ResponseEntity.ok().body(page);
	}

	private void verifyUser() {
		Optional<User> user = Optional.ofNullable(authService.authenticated());
		User entity = user.orElseThrow(() -> new UnauthorizedException("Invalid User"));
	}
}
