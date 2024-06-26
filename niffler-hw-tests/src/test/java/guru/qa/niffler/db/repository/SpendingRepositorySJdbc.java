package guru.qa.niffler.db.repository;

import guru.qa.niffler.db.DataSourceProvider;
import guru.qa.niffler.db.Database;
import guru.qa.niffler.db.model.CategoryEntity;
import guru.qa.niffler.db.model.SpendEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;

import java.sql.PreparedStatement;
import java.util.UUID;

public class SpendingRepositorySJdbc implements SpendingRepository {

    private final JdbcTemplate spendTemplate;

    public SpendingRepositorySJdbc() {
        this.spendTemplate = new JdbcTemplate(DataSourceProvider.INSTANCE.dataSource(Database.SPEND));
    }

    @Override
    public CategoryEntity createCategory(CategoryEntity category) {
        KeyHolder kh = new GeneratedKeyHolder();

        spendTemplate.update(
                con -> {
                    PreparedStatement ps = con.prepareStatement(
                            "INSERT INTO category " +
                                    "(category, username) " +
                                    "VALUES(?, ?);"
                            , PreparedStatement.RETURN_GENERATED_KEYS
                    );

                    ps.setString(1, category.getCategory());
                    ps.setString(2, category.getUsername());

                    return ps;
                }, kh
        );

        category.setId((UUID) kh.getKeys().get("id"));

        return category;
    }

    @Override
    public SpendEntity createSpending(SpendEntity spend) {
        spend.getCategory().setId(UUID.fromString(getCategoryId(spend.getCategory().getCategory(), spend.getUsername())));

        KeyHolder kh = new GeneratedKeyHolder();

        spendTemplate.update(
                con -> {
                    PreparedStatement ps = con.prepareStatement(
                            "INSERT INTO spend " +
                                    "(username, spend_date, currency, amount, description, category_id) " +
                                    "VALUES(?, ?, ?, ?, ?, ?::uuid);"
                            , PreparedStatement.RETURN_GENERATED_KEYS
                    );

                    ps.setString(1, spend.getUsername());
                    ps.setDate(2, new java.sql.Date(spend.getSpendDate().getTime()));
                    ps.setString(3, spend.getCurrency().toString());
                    ps.setDouble(4, spend.getAmount());
                    ps.setString(5, spend.getDescription());
                    ps.setString(6, spend.getCategory().getId().toString());

                    return ps;
                }, kh
        );

        spend.setId((UUID) kh.getKeys().get("id"));

        return spend;
    }

    @Override
    public String getCategoryId(String category, String username) {
        return spendTemplate.queryForObject("SELECT id FROM category WHERE category=? AND username=?"
                , new Object[]{category, username}, String.class);
    }

}
