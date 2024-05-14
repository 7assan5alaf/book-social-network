package com.hks.book.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.hks.book.request.BookRequest;
import com.hks.book.response.BookResponse;
import com.hks.book.service.BookService;

import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("books")
@RequiredArgsConstructor
@Tag(name = "Book")
public class BookController {

	private final BookService bookService;

	@PostMapping("/add")
	public ResponseEntity<?> insertBook(@Valid @RequestBody BookRequest book, Authentication auth) {
		return ResponseEntity.ok(bookService.save(book, auth));
	}

	@GetMapping("/{id}")
	public ResponseEntity<BookResponse> findById(@PathVariable Long id) {
		return ResponseEntity.ok(bookService.findById(id));
	}

	@GetMapping("")
	public ResponseEntity<?> findAllBooks(@RequestParam(defaultValue = "0", required = false) int page,
			@RequestParam(defaultValue = "10", required = false) int size, Authentication auth) {
		return ResponseEntity.ok(bookService.findAllBooks(page, size, auth));
	}

	@GetMapping("/owner")
	public ResponseEntity<?> findAllBooksByOwner(
			@RequestParam(name = "page", defaultValue = "0", required = false) int page,
			@RequestParam(name = "size", defaultValue = "10", required = false) int size, Authentication auth) {
		return ResponseEntity.ok(bookService.findAllBooksByOwner(page, size, auth));
	}

	@GetMapping("/borrowed")
	public ResponseEntity<?> findAllBorrowedBookByOwner(
			@RequestParam(name = "page", defaultValue = "0", required = false) int page,
			@RequestParam(name = "size", defaultValue = "10", required = false) int size, Authentication auth) {
		return ResponseEntity.ok(bookService.findAllBorrowedBooksByOwner(page, size, auth));
	}

	@GetMapping("/returned")
	public ResponseEntity<?> findAllBooksByReturned(
			@RequestParam(name = "page", defaultValue = "0", required = false) int page,
			@RequestParam(name = "size", defaultValue = "10", required = false) int size, Authentication auth) {
		return ResponseEntity.ok(bookService.findAllBooksByReturned(page, size, auth));
	}

	@PatchMapping("/shareable/{bookId}")
	public ResponseEntity<Long> updateShareable(@PathVariable Long bookId, Authentication auth) {
		return ResponseEntity.ok(bookService.updateShareable(bookId, auth));
	}

	@PatchMapping("/archived/{bookId}")
	public ResponseEntity<Long> updateArchived(@PathVariable Long bookId, Authentication auth) {
		return ResponseEntity.ok(bookService.updateArchived(bookId, auth));
	}

	@PostMapping("/borrowed/{bookId}")
	public ResponseEntity<Long> borrowedBook(@PathVariable Long bookId, Authentication auth) {
		return ResponseEntity.ok(bookService.borrowBook(bookId, auth));
	}

	@PatchMapping("/borrowed/return/{bookId}")
	public ResponseEntity<Long> returnBorrowedBook(@PathVariable Long bookId, Authentication auth) {
		return ResponseEntity.ok(bookService.returnBorrowedBook(bookId, auth));
	}

	@PatchMapping("/borrowed/return-approve/{bookId}")
	public ResponseEntity<Long> returnApproveBorrowedBook(@PathVariable Long bookId, Authentication auth) {
		return ResponseEntity.ok(bookService.returnApproveBorrowedBook(bookId, auth));
	}

	@PostMapping("/upload/book-cover/{bookId}")
	public ResponseEntity<?> uploadFile(@PathVariable Long bookId, @Parameter @RequestPart("file") MultipartFile file,
			Authentication authentication) {
		bookService.uploadFile(bookId, file, authentication);

		return ResponseEntity.accepted().build();
	}

	@PutMapping("/edit/{bookId}")
	public ResponseEntity<?>updateBook(@PathVariable Long bookId,
			Authentication auth,@RequestBody BookRequest request ){
	return ResponseEntity.ok(bookService.updateBook(bookId,auth,request));	
	}
	
//	@DeleteMapping("/delete/{bookId}")
//	public ResponseEntity<?>deleteBook(@PathVariable Long bookId,Authentication auth){
//		return ResponseEntity.ok(bookService.deleteBook(bookId,auth));
//	}
	
	
}
