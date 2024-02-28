package dao;

import dataAccess.DataAccessException;

import java.sql.Connection;

public class AuthDAO {
    private Connection conn;
    private AuthDAO (){}
    public void setConnection(Connection c) throws DataAccessException {
        conn = c;
    }
}
