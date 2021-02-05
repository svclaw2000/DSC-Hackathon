package goorum.goorum.service;

import goorum.goorum.domain.Category;

import java.util.List;

public interface CategoryService {
    List<Category> getList();

    boolean addCategory(String category);
}