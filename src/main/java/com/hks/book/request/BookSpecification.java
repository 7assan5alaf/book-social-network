package com.hks.book.request;

import org.springframework.data.jpa.domain.Specification;

import com.hks.book.entity.Book;


public class BookSpecification {
	
	public static Specification<Book>withOwnerId(Long id){
		return (root,query,cri)->cri.equal(root.get("owner").get("id"), id);
	}

}
