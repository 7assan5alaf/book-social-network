package com.hks.book.service;

import java.util.List;
import java.util.Objects;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.hks.book.entity.Book;
import com.hks.book.entity.BookTransactionHistory;
import com.hks.book.entity.User;
import com.hks.book.exception.OperationPermittedException;
import com.hks.book.exception.RecordNotFound;
import com.hks.book.file.FileStorageService;
import com.hks.book.mapper.BookMapper;
import com.hks.book.repository.BookRepo;
import com.hks.book.repository.BookTransactionRepo;
import com.hks.book.request.BookRequest;
import com.hks.book.request.BookSpecification;
import com.hks.book.response.BookResponse;
import com.hks.book.response.BorrowedBookResponse;
import com.hks.book.response.PageResponse;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class BookService {

	private final BookRepo bookRepo;
	private final BookMapper mapper;
	private final BookTransactionRepo transactionRepo;
	private final FileStorageService fileStorageService;

	public Long save(BookRequest request, Authentication authentication) {
		Book book = mapper.toBook(request);
		User user = (User) authentication.getPrincipal();
		book.setOwner(user);
		bookRepo.save(book);
		return book.getId();
	}

	public BookResponse findById(Long id) {
		Book book = bookRepo.findById(id).orElseThrow(() -> new RecordNotFound("book is not found"));
		return mapper.toBookResponse(book);
	}

	public PageResponse<BookResponse> findAllBooks(int page, int size, Authentication auth) {
		User user = (User) auth.getPrincipal();
		Pageable pageable = PageRequest.of(page, size, Sort.by("createdDate").descending());
		Page<Book> book = bookRepo.findAllDisplayableBooks(pageable, user.getId());

		List<BookResponse> responses = book.stream().map(mapper::toBookResponse).toList();
		return new PageResponse<BookResponse>(responses, book.getNumber(),
				book.getSize(), book.getTotalElements(),
				book.getTotalPages(), book.isFirst(), book.isLast());

	}

	public PageResponse<BookResponse> findAllBooksByOwner(int page, int size, Authentication auth) {
		User user = (User) auth.getPrincipal();
		Pageable pageable = PageRequest.of(page, size, Sort.by("createdDate").descending());
		Page<Book> book = bookRepo.findAll(BookSpecification.withOwnerId(user.getId()), pageable);
		List<BookResponse> responses = book.stream().map(mapper::toBookResponse).toList();
		return new PageResponse<BookResponse>(responses, book.getNumber(), book.getSize(), book.getTotalElements(),
				book.getTotalPages(), book.isFirst(), book.isLast());
	}

	public PageResponse<BorrowedBookResponse> findAllBorrowedBooksByOwner(int page, int size, Authentication auth) {
		User user = (User) auth.getPrincipal();
		Pageable pageable = PageRequest.of(page, size, Sort.by("createdDate").descending());
		Page<BookTransactionHistory> book = transactionRepo.findAllBorrowedBooksByOwnerId(pageable, user.getId());
		List<BorrowedBookResponse> responses = book.stream().map(mapper::toBorrowedBookResponse).toList();
		return new PageResponse<BorrowedBookResponse>(responses, book.getNumber(), book.getSize(),
				book.getTotalElements(), book.getTotalPages(), book.isFirst(), book.isLast());
	}

	public PageResponse<?> findAllBooksByReturned(int page, int size, Authentication auth) {
		User user = (User) auth.getPrincipal();
		Pageable pageable = PageRequest.of(page, size, Sort.by("createdDate").descending());
		Page<BookTransactionHistory> book = transactionRepo.findAllBooksByReturned(pageable, user.getId());
		List<BorrowedBookResponse> responses = book.stream().map(mapper::toBorrowedBookResponse).toList();
		return new PageResponse<BorrowedBookResponse>(responses, book.getNumber(), book.getSize(),
				book.getTotalElements(), book.getTotalPages(), book.isFirst(), book.isLast());
	}

	public Long updateShareable(Long bookId, Authentication auth) {
		Book book = bookRepo.findById(bookId).orElseThrow(() -> new RecordNotFound("no found book id " + bookId));
		User user = (User) auth.getPrincipal();
		if (!Objects.equals(book.getOwner().getId(), user.getId())) {
			throw new OperationPermittedException("You can not update other book shareable state");
		}
		book.setShareable(!book.isShareable());
		bookRepo.save(book);
		return bookId;
	}

	public Long updateArchived(Long bookId, Authentication auth) {
		Book book = bookRepo.findById(bookId).orElseThrow(() -> new RecordNotFound("no found book id " + bookId));
		User user = (User) auth.getPrincipal();
		if (!Objects.equals(book.getOwner().getId(), user.getId())) {
			throw new OperationPermittedException("You can not update other book archived state");
		}
		book.setArchived(!book.isArchived());
		bookRepo.save(book);
		return bookId;
	}

	public Long borrowBook(Long bookId, Authentication auth) {
		Book book = bookRepo.findById(bookId).orElseThrow(() -> new RecordNotFound("no found book id " + bookId));
		User user = (User) auth.getPrincipal();
		if (book.isArchived() || !book.isShareable())
			throw new OperationPermittedException(
					"The requested book cannot be borrowed since it is archived or not shareable");
		if (Objects.equals(book.getOwner().getId(), user.getId())) {
			throw new OperationPermittedException("You can not borrowed your own book");
		}
		final boolean isArradyBorrowed = transactionRepo.isAlreadyBorrowed(bookId, user.getId());
		if (isArradyBorrowed) {
			throw new OperationPermittedException("The request book is allready borrowed");
		}
		BookTransactionHistory history = BookTransactionHistory.builder().book(book).user(user).returned(false)
				.returnedApproved(false).build();
		transactionRepo.save(history);

		return bookId;
	}

	public Long returnBorrowedBook(Long bookId, Authentication auth) {
		Book book = bookRepo.findById(bookId).orElseThrow(() -> new RecordNotFound("no found book id " + bookId));
		User user = (User) auth.getPrincipal();
		if (book.isArchived() || !book.isShareable())
			throw new OperationPermittedException(
					"The requested book cannot be borrowed since it is archived or not shareable");
		if (Objects.equals(book.getOwner().getId(), user.getId())) {
			throw new OperationPermittedException("You can not borrowed or returned  your own book");
		}

		BookTransactionHistory transactionHistory = transactionRepo.returnBorrowedBook(bookId, user.getId())
				.orElseThrow(() -> new OperationPermittedException("You did not borrowed this book"));
		transactionHistory.setReturned(true);
		return transactionRepo.save(transactionHistory).getId();
	}

	public Long returnApproveBorrowedBook(Long bookId, Authentication auth) {
		Book book = bookRepo.findById(bookId).orElseThrow(() -> new RecordNotFound("no found book id " + bookId));
		User user = (User) auth.getPrincipal();
		if (book.isArchived() || !book.isShareable())
			throw new OperationPermittedException(
					"The requested book cannot be borrowed since it is archived or not shareable");
		if (Objects.equals(book.getOwner().getId(), user.getId())) {
			throw new OperationPermittedException("You can not borrowed or returned  your own book");
		}

		BookTransactionHistory transactionHistory = transactionRepo.returnApproveBorrowedBook(bookId, user.getId())
				.orElseThrow(() -> new OperationPermittedException(
						"The book is not returned yet. You can not approve its returned "));
		transactionHistory.setReturnedApproved(true);
		return transactionRepo.save(transactionHistory).getId();
	}

	public void uploadFile(Long bookId, MultipartFile file, Authentication auth) {
		Book book = bookRepo.findById(bookId).orElseThrow(() -> new RecordNotFound("no found book id " + bookId));
		User user = (User) auth.getPrincipal();
		var cover=fileStorageService.saveFile(file,user.getId());
		book.setBookCover(cover);
		bookRepo.save(book);

	}

	public BookResponse updateBook(Long bookId, Authentication auth, BookRequest request) {
		Book book=bookRepo.findById(bookId).orElseThrow(()->new RecordNotFound("no found book id : "+bookId));
		User user = (User) auth.getPrincipal();
		if (!Objects.equals(book.getOwner().getId(), user.getId())) {
			throw new OperationPermittedException("You can not update or edit  this book");
		}
		book.setIsbn(request.getIsbn());
		book.setAuthorName(request.getAuthorName());
		book.setTitle(request.getTitle());
        book.setSynopsis(request.getSynopsis());
        bookRepo.save(book);
		BookResponse response=mapper.toBookUpdateResponse(book);
		return response;
	}

//	public String deleteBook(Long bookId, Authentication auth) {
//		Book book=bookRepo.findById(bookId).orElseThrow(()->new RecordNotFound("no found book id : "+bookId));
//		 transactionRepo.deleteByBookId(bookId);
//		
//		User user = (User) auth.getPrincipal();
//		if (!Objects.equals(book.getOwner().getId(), user.getId())) {
//			throw new OperationPermittedException("You can not delete or remove  this book");
//		}
//		if(!FileUtils.deleteFileFromLoction(book.getBookCover())) {
//			bookRepo.delete(book);
//			throw new RecordNotFound("the cover not found  and delete successfully");
//		}
//		bookRepo.delete(book);
//		return "delete book successfully";
//	}

}
