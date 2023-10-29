package com.quiz.javaquizapi.integration.dao;

import com.quiz.javaquizapi.ApiTests;
import com.quiz.javaquizapi.dao.BaseRepository;
import jakarta.persistence.EntityManagerFactory;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.hibernate.SessionFactory;
import org.hibernate.stat.Statistics;
import org.junit.jupiter.api.Assertions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.DirtiesContext;

import java.util.Optional;

@DataJpaTest
@SuppressWarnings("all")
@Getter(AccessLevel.PROTECTED)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_CLASS)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public abstract class DaoTests<R extends BaseRepository> extends ApiTests {
    protected static final String EXECUTED_QUERIES_MESSAGE = "Expected number of executed queries should be equal to %s";

    private Statistics statistics;

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
        ONE(1),
        TWO(2),
        THREE(3);

        private final long count;

        @Override
        public String toString() {
            return String.valueOf(count);
        }
    }
}
