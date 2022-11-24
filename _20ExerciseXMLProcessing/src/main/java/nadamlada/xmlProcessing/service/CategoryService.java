package nadamlada.xmlProcessing.service;

import nadamlada.xmlProcessing.model.dto.CategorySeedDto;
import nadamlada.xmlProcessing.model.entity.Category;

import java.util.List;
import java.util.Set;

public interface CategoryService {
    void seedCategories(List<CategorySeedDto> categories);

    long getEntityCount();

    Set<Category> getRandomCategories();
}