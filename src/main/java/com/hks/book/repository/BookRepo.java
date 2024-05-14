package com.hks.book.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.hks.book.entity.Book;

@Repository
public interface BookRepo extends JpaRepository<Book,Long> ,JpaSpecificationExecutor<Book>{

	@Query("""
			select book 
			from Book book
			where book.archived=false
			and book.shareable=true
			and book.owner.id!=:id
			""")
	Page<Book> findAllDisplayableBooks(Pageable pageable, Long id);

}
