package org.server.dao;

import org.server.exceptions.DaoException;

import java.math.BigDecimal;
import java.sql.SQLException;

public interface Dao<T> {
    void saveRecord(T record) throws DaoException;

    BigDecimal getSumRecords() throws SQLException;

    String getDescription(Integer recordId) throws SQLException;

    String getDescription(String firstname, String secondName, String middleName) throws SQLException;

    BigDecimal getSumRecordsOfCandidate(Integer candidateId) throws SQLException;

}
