package com.hks.book.mapper;

import org.springframework.stereotype.Service;

import com.hks.book.entity.Book;
import com.hks.book.entity.BookTransactionHistory;
import com.hks.book.file.FileUtils;
import com.hks.book.request.BookRequest;
import com.hks.book.response.BookResponse;
import com.hks.book.response.BorrowedBookResponse;

@Service
public class BookMapper {

	public Book toBook(BookRequest request) {
		return Book.builder().archived(false).authorName(request.getAuthorName()).isbn(request.getIsbn())
				.shareable(request.isShareable()).title(request.getTitle()).synopsis(request.getSynopsis()).build();
	}

	public BookResponse toBookResponse(Book book) {
		return BookResponse.builder().archived(book.isArchived()).authorName(book.getAuthorName()).isbn(book.getIsbn())
				.owner(book.getOwner().getFullName()).shareable(book.isShareable()).title(book.getTitle())
				.synopsis(book.getSynopsis()).rate(book.getRate())
				.cover(FileUtils.readFileFromLocatin(book.getBookCover())).build();
	}

	public BorrowedBookResponse toBorrowedBookResponse(BookTransactionHistory history) {
		return BorrowedBookResponse.builder().authorName(history.getBook().getAuthorName())
				.isbn(history.getBook().getIsbn()).owner(history.getUser().getFullName())
				.title(history.getBook().getTitle()).rate(history.getBook().getRate())
				.returnApproved(history.isReturnedApproved()).returned(history.isReturned()).build();
	}
	public BookResponse toBookUpdateResponse(Book request) {
		return BookResponse.builder().authorName(request.getAuthorName()).isbn(request.getIsbn())
				.title(request.getTitle())
				.owner(request.getOwner().getFullName())
				.cover(FileUtils.readFileFromLocatin(request.getBookCover()))
				.rate(request.getRate())
				.synopsis(request.getSynopsis()).build();
	}

}
