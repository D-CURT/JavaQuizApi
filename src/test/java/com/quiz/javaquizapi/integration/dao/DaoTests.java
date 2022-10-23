package com.quiz.javaquizapi.integration.dao;

import com.quiz.javaquizapi.dao.BaseRepository;
import com.quiz.javaquizapi.model.user.Providers;
import com.quiz.javaquizapi.model.user.Roles;
import com.quiz.javaquizapi.model.user.User;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.hibernate.SessionFactory;
import org.hibernate.stat.Statistics;
import org.junit.jupiter.api.Assertions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import javax.persistence.EntityManagerFactory;
import java.util.Optional;
import java.util.UUID;

@DataJpaTest
@SuppressWarnings("all")
@Getter(AccessLevel.PROTECTED)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public abstract class DaoTests<R extends BaseRepository> {

    protected static final String EXECUTED_QUERIES_MESSAGE = "Expected number of executed queries should be equal to %s";

    protected final User localUser;

    private Statistics statistics;

    {
        localUser = new User()
                .setUsername("username")
                .setPassword("password")
                .setDisplayName("displayName")
                .setRole(Roles.USER)
                .setProvider(Providers.LOCAL)
                .setEnabled(Boolean.TRUE);
        localUser.setCode(UUID.randomUUID().toString());
    }

    @Autowired
    private R repository;

    @Autowired
    private void initStatistics(EntityManagerFactory factory) {
        this.statistics = Optional.ofNullable(factory.unwrap(SessionFactory.class))
                .orElseThrow(() -> new IllegalStateException("Unable to load Hibernate session statistics"))
                .getStatistics();
    }

    protected void assertExecutedQueries() {
        assertExecutedQueries(ExecutedQueries.ONE);
    }

    protected void assertExecutedQueries(ExecutedQueries queryCount) {
        Assertions.assertEquals(queryCount.getCount(), statistics.getQueryExecutionCount(),
                EXECUTED_QUERIES_MESSAGE.formatted(queryCount));
        statistics.clear();
    }

    @RequiredArgsConstructor
    @Getter(AccessLevel.PRIVATE)
    protected enum ExecutedQueries {
        ONE(1);

        private final long count;

        @Override
        public String toString() {
            return String.valueOf(count);
        }
    }
}
