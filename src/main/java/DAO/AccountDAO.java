package DAO;

import Util.ConnectionUtil;
import Model.Account;

import java.sql.Statement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;


public class AccountDAO {
    /**
     * NOT SURE IF THIS IS NEEDED
     * Get account from account_id
     * @return Account
     */
    public Account getAccountByID(int accountID){
        Connection connection = ConnectionUtil.getConnection();
        try {
            String sql = "SELECT * FROM account WHERE account_id = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, accountID);
            
            // ResultSet to get the result from query
            ResultSet rs = preparedStatement.executeQuery();
            while(rs.next()){
                Account acc = new Account(
                        rs.getInt("account_id"),
                        rs.getString("username"),
                        rs.getString("password"));
                return acc;
            }
        } catch(SQLException e){
            System.out.println(e.getMessage());
        }
        return null;
    }

    /**
     * Get account by username
     * @return Account
     */
    public Account getAccountByUsername(String user){
        Connection connection = ConnectionUtil.getConnection();
        try {
            String sql = "SELECT * FROM account WHERE username = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, user);
            
            // ResultSet to get the result from query
            ResultSet rs = preparedStatement.executeQuery();
            while(rs.next()){
                Account acc = new Account(
                        rs.getInt("account_id"),
                        rs.getString("username"),
                        rs.getString("password"));
                return acc;
            }
        } catch(SQLException e){
            System.out.println(e.getMessage());
        }
        return null;
    }

    /**
     * Insert/Create an account for user registration.
     * @return Account Object, including its account_id
     */
    public Account insertAccount(Account acc){
        Connection connection = ConnectionUtil.getConnection();
        try {
            String sql = "INSERT INTO account (username, password) VALUES (?, ?)" ;

            // Statement.RETURN_GENERATED_KEYS retrieved from Flight Tracker Mini Project
            PreparedStatement preparedStatement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

            preparedStatement.setString(1, acc.getUsername());
            preparedStatement.setString(2, acc.getPassword());

            preparedStatement.executeUpdate();

            // Fetching generated keys. Retrieved from Flight Tracker Mini Project
            ResultSet generatedKeys = preparedStatement.getGeneratedKeys();
            if(generatedKeys.next()){
                int generatedId = generatedKeys.getInt(1);
                return new Account(generatedId, acc.getUsername(), acc.getPassword());
            } else {
                throw new SQLException("Creating account failed, no ID obtained.");
            }
        } catch(SQLException e) {
            System.out.println(e.getMessage());
        }
        return null;
    }

}

