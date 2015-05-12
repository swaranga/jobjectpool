package com.jobjectpool.pool.test;

import java.sql.Connection;

import com.jobjectpool.pool.Pool;
import com.jobjectpool.pool.PoolFactory;

public class Main {
    public static void main(String[] args) {
        Pool<Connection> pool = PoolFactory.newBoundedBlockingPool(10,
                new JDBCConnectionFactory("", "", "", ""),
                new JDBCConnectionValidator());

        // do whatever you like
    }
}
