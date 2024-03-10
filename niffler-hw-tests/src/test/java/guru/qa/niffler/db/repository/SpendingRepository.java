package guru.qa.niffler.db.repository;

import guru.qa.niffler.db.model.CategoryEntity;
import guru.qa.niffler.db.model.SpendEntity;

public interface SpendingRepository {

    CategoryEntity createCategory(CategoryEntity category);

    SpendEntity createSpending(SpendEntity spend);

    String getCategoryId(String category, String username);
}
