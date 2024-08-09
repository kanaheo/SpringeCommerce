package com.shopme.admin;

import org.springframework.data.repository.PagingAndSortingRepository;

import com.shopme.common.entity.Category;

public interface CategoryRepository extends PagingAndSortingRepository<Category, Integer > {

}
