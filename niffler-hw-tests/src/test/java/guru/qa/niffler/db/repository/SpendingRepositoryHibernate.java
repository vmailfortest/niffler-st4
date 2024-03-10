package guru.qa.niffler.db.repository;

import guru.qa.niffler.db.EmfProvider;
import guru.qa.niffler.db.jpa.JpaService;
import guru.qa.niffler.db.model.CategoryEntity;
import guru.qa.niffler.db.model.SpendEntity;

import java.util.Map;
import java.util.UUID;

import static guru.qa.niffler.db.Database.SPEND;

public class SpendingRepositoryHibernate extends JpaService implements SpendingRepository {

    public SpendingRepositoryHibernate() {
        super(
                Map.of(
                        SPEND, EmfProvider.INSTANCE.emf(SPEND).createEntityManager()
                )
        );
    }

    @Override
    public CategoryEntity createCategory(CategoryEntity category) {

        persist(SPEND, category);

        return category;
    }

    @Override
    public SpendEntity createSpending(SpendEntity spend) {

        spend.getCategory().setId(UUID.fromString(getCategoryId(spend.getCategory().getCategory(), spend.getUsername())));

        persist(SPEND, spend);

        return spend;
    }

    @Override
    public String getCategoryId(String category, String username) {
        return entityManager(SPEND).createQuery(
                        "SELECT c.id FROM CategoryEntity c WHERE c.category=:category AND c.username=:username"
                        , UUID.class)
                .setParameter("category", category)
                .setParameter("username", username)
                .getSingleResult().toString();
    }
}
