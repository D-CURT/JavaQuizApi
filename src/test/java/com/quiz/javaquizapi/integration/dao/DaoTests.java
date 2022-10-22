package com.quiz.javaquizapi.integration.dao;

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

@DataJpaTest
@Getter(AccessLevel.PROTECTED)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public abstract class DaoTests {

    public static final String EXECUTED_QUERIES_MESSAGE = "Expected number of executed queries should be equal to %s";
    private Statistics statistics;

    @Autowired
    public void someService(EntityManagerFactory factory) {
        this.statistics = factory.unwrap(SessionFactory.class).getStatistics();
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
        ONE(1L);

        private final Long count;

        @Override
        public String toString() {
            return count.toString();
        }
    }
}
