package com.hks.book.repository;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.hks.book.entity.BookTransactionHistory;

@Repository
public interface BookTransactionRepo extends JpaRepository<BookTransactionHistory, Long> {

	@Query("""
			select borrowed
			from BookTransactionHistory borrowed
			where borrowed.user.id=:id
			""")

	Page<BookTransactionHistory> findAllBorrowedBooksByOwnerId(Pageable pageable, Long id);

	@Query("""
			select borrowed
			from BookTransactionHistory borrowed
			where borrowed.book.owner.id=:id
			""")
	Page<BookTransactionHistory> findAllBooksByReturned(Pageable pageable, Long id);

	@Query("""
			 SELECT
			      (COUNT (*) > 0) AS isBorrowed
			      FROM BookTransactionHistory bookTransactionHistory
			      WHERE bookTransactionHistory.user.id=:id
			      AND bookTransactionHistory.book.id=:bookId
			      AND bookTransactionHistory.returnedApproved = false
			""")
	boolean isAlreadyBorrowed(Long bookId, Long id);

	@Query("""
			select borrowed
			from BookTransactionHistory borrowed
			where borrowed.user.id=:id
			and borrowed.book.id=:bookId
			and borrowed.returned=false
		    and borrowed.returnedApproved=false
			""")

	Optional<BookTransactionHistory> returnBorrowedBook(Long bookId, Long id);
	@Query("""
			select borrowed
			from BookTransactionHistory borrowed
			where borrowed.book.owner.id=:id
			and borrowed.book.id=:bookId
			and borrowed.returned=true
		    and borrowed.returnedApproved=false
			""")
	Optional<BookTransactionHistory> returnApproveBorrowedBook(Long bookId, Long id);
	
//	@Query("""
//			delete t 
//			from BookTransactionHistory t
//			where t.book.id=:bookId
//			""")
//	  void deleteByBookId(Long bookId);

}
