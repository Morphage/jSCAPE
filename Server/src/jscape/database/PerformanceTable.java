/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jscape.database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 *
 * @author achantreau
 */
public class PerformanceTable {

    private static final String TABLE_NAME = "performance";

    private static final String LOGIN_NAME = "login_name";
    private static final String EXERCISE_CATEGORY = "exercise_category";
    private static final String EXERCISES_ANSWERED = "exercises_answered";
    private static final String CORRECT_ANSWERS = "correct_answers";
    private static final String WRONG_ANSWERS = "wrong_answers";

    public static ArrayList<String> getPerformanceStats(String loginName) {
        PreparedStatement ps = null;
        Connection connection = Database.getConnection();
        ResultSet resultSet;

        ArrayList<String> performanceData = new ArrayList<>();

        try {
            String query = "SELECT " + EXERCISE_CATEGORY + "," + EXERCISES_ANSWERED + ","
                    + CORRECT_ANSWERS + "," + WRONG_ANSWERS + " FROM " + TABLE_NAME + " WHERE "
                    + LOGIN_NAME + " = ?";
            ps = connection.prepareStatement(query);
            ps.setString(1, loginName);
            resultSet = ps.executeQuery();

            while (resultSet.next()) {
                performanceData.add(resultSet.getString(EXERCISE_CATEGORY));
                performanceData.add("" + resultSet.getInt(EXERCISES_ANSWERED));
                performanceData.add("" + resultSet.getInt(CORRECT_ANSWERS));
                performanceData.add("" + resultSet.getInt(WRONG_ANSWERS));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (ps != null) {
                    ps.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        return performanceData;
    }
    
    public static void updatePerformanceStats(String loginName, String exerciseCategory,
            boolean isCorrectAnswer) {
        PreparedStatement ps = null;
        Connection connection = Database.getConnection();
        
        try {
            String updateColumn = isCorrectAnswer ? CORRECT_ANSWERS : WRONG_ANSWERS;
            
            String query = "UPDATE " + TABLE_NAME + " SET (" + EXERCISES_ANSWERED
                    + "," + updateColumn + ") = (" + EXERCISES_ANSWERED + "+?,"
                    + updateColumn + "+?) WHERE " + LOGIN_NAME + " = ?"
                    + " AND " + EXERCISE_CATEGORY + " = ?";
            
            ps = connection.prepareStatement(query);
            ps.setInt(1, 1);
            ps.setInt(2, 1);
            ps.setString(3, loginName);
            ps.setString(4, exerciseCategory);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (ps != null) {
                    ps.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}