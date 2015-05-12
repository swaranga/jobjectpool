package com.jobjectpool.pool.test;

import java.sql.Connection;
import java.sql.SQLException;

import com.jobjectpool.pool.Pool.Validator;

public final class JDBCConnectionValidator implements Validator<Connection> {
    public boolean isValid(Connection con) {
        if (con == null) {
            return false;
        }

        try {
            return !con.isClosed();
        } catch (SQLException se) {
            return false;
        }
    }

    public void invalidate(Connection con) {
        try {
            con.close();
        } catch (SQLException se) {

        }
    }
}
