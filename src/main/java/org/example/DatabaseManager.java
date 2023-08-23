package org.example;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;


public class DatabaseManager {
    private static int userID = 1;
    private static int goodsID = 1;

    private static final String DB_URL = "jdbc:sqlite:dataBase.db";

    public boolean userRegister(String username, String password) {
        try {
            Connection connection = DriverManager.getConnection(DB_URL);
            
            PreparedStatement statement2 = connection.prepareStatement("SELECT * FROM USER WHERE USERACCOUNT = ?");
            statement2.setString(1, username);
            ResultSet resultSet = statement2.executeQuery();
            if(resultSet.next()){
                connection.close();
                return false;
            }

            PreparedStatement statement = connection.prepareStatement("INSERT INTO USER (ID, USERACCOUNT, PASSWORD, DEFAULTPASSWORD) VALUES (?, ?, ?, ?)");
            statement.setInt(1, userID++);
            statement.setString(2, username);
            statement.setString(3, password);
            statement.setString(4,username);
            statement.executeUpdate();
            connection.close();
            return true;
        } catch (SQLException e) {
            System.out.println("Failed to register user: " + e.getMessage());
        }
        return false;
    }

    public boolean userLogin(String username, String password) {
        
        try {
            Connection connection = DriverManager.getConnection(DB_URL);
            PreparedStatement statement = connection.prepareStatement("SELECT * FROM USER WHERE USERACCOUNT = ?");

            statement.setString(1, username);
            ResultSet resultSet = statement.executeQuery();
    
            if (resultSet.next()) {
                String storedPassword = resultSet.getString("PASSWORD");
                connection.close();
                if (password.equals(storedPassword)) return true;
                else return false;
            } else return false;
        } catch (SQLException e) {
            System.out.println("Failed to login: " + e.getMessage());
        }
        return false;
    }

    public boolean userpasswordChange(String username, String oldPassword, String newPassword) {
        try {
            Connection connection = DriverManager.getConnection(DB_URL);
            PreparedStatement statement = connection.prepareStatement("UPDATE USER set PASSWORD = ? WHERE USERACCOUNT = ? AND PASSWORD = ?");
            statement.setString(1, newPassword);
            statement.setString(2, username);
            statement.setString(3, oldPassword);
            int updateResult = statement.executeUpdate();
            connection.close();
            if(updateResult == 0) return false;
            else return true;
        } catch (SQLException e) {
            System.out.println("Failed to change password:" + e.getMessage());
            return false;
        }
    }

    public boolean userPasswordReset(String username) {
        try {
            Connection connection = DriverManager.getConnection(DB_URL);
            PreparedStatement statement = connection.prepareStatement("UPDATE USER SET PASSWORD = DEFAULTPASSWORD WHERE USERACCOUNT = ?");
            statement.setString(1, username);
            if(statement.executeUpdate() != 0) {
                connection.close();
                return true;
            }else return false;
        }catch(SQLException e) {
            System.out.println("Failed to reset password: " + e.getMessage());
            return false;
        }
    }

    public boolean allUserPasswordReset() {
        try {
            Connection connection = DriverManager.getConnection(DB_URL);
            PreparedStatement statement = connection.prepareStatement("SELECT USERACCOUNT FROM USER");
            PreparedStatement statement2 = connection.prepareStatement("UPDATE USER SET PASSWORD = DEFAULTPASSWORD WHERE USERACCOUNT = ?");
            ResultSet resultSet = statement.executeQuery();
            while(resultSet.next()) {
                statement2.setString(1, resultSet.getString("USERACCOUNT"));
                statement2.executeUpdate();
            }
            connection.close();
            return true;
        }catch(SQLException e) {
            System.out.println("Failed to reset all users' password: " + e.getMessage());
            return false;
        }
    }

    public void updataID(){//更新id
        try {
            Connection connection = DriverManager.getConnection(DB_URL);
            PreparedStatement statement = connection.prepareStatement("SELECT ID FROM USER");
            ResultSet resultSet = statement.executeQuery();
            int id = 0;
            while(resultSet.next()){
                id = resultSet.getInt("ID");
            }
            userID = ++id;

            id = 0;
            statement = connection.prepareStatement("SELECT ID FROM GOODS");
            resultSet = statement.executeQuery();
            while(resultSet.next()){
                id = resultSet.getInt("ID");
            }
            goodsID = ++id;

            connection.close();
        } catch(SQLException e) {
            System.out.println("Failed to updataID" + e.getMessage());
        }
    }

    public boolean showUserInfo(String username) {
        try {
            Connection connection = DriverManager.getConnection(DB_URL);
            PreparedStatement statement = connection.prepareStatement("SELECT * FROM USER WHERE USERACCOUNT = ?");
            statement.setString(1, username);
            ResultSet resultSet = statement.executeQuery();
            if(resultSet.next()){
                System.out.print("ID = " + resultSet.getInt("ID") + "\t");
                System.out.print("USERACCOUNT = " + resultSet.getString("USERACCOUNT") + "\t");
                System.out.println("PASSWORD = " + resultSet.getString("PASSWORD"));
                connection.close();
                return true;
            }else {
                connection.close();
                return false;
            }
        }catch(SQLException e) {
            System.out.println("Failed to show user infomatin: " + e.getMessage());
            return false;
        }
    }

    public void showAllUser() {
        try {
            Connection connection = DriverManager.getConnection(DB_URL);
            PreparedStatement statement = connection.prepareStatement("SELECT * FROM USER");
            ResultSet resultSet = statement.executeQuery();
            
            while(resultSet.next()) {
                System.out.print("ID = " + resultSet.getInt("ID") + "\t");
                System.out.print("USERACCOUNT = " + resultSet.getString("USERACCOUNT") + "\t");
                System.out.println("PASSWORD = " + resultSet.getString("PASSWORD"));
            }
            connection.close();
        } catch(SQLException e) {
            System.out.println("Failed to show all users : " + e.getMessage());
        }
    }

    public void addAdmin() {
        try {
            Connection connection = DriverManager.getConnection(DB_URL);
            PreparedStatement statement = connection.prepareStatement("INSERT INTO ADMIN (ID, ADMINACCOUNT, PASSWORD) VALUES (?, ?, ?)");
            statement.setInt(1, 1);
            statement.setString(2, "admin");
            statement.setString(3, "admin");
            statement.executeUpdate();
            connection.close();
        }catch(SQLException e) {
            //System.out.println("Failed to add admin: " + e.getMessage());
        }
    }

    public boolean adminLogin(String adminname, String password) {
        
        try {
            Connection connection = DriverManager.getConnection(DB_URL);
            PreparedStatement statement = connection.prepareStatement("SELECT * FROM ADMIN WHERE ADMINACCOUNT = ?");

            statement.setString(1, adminname);
            ResultSet resultSet = statement.executeQuery();
    
            if (resultSet.next()) {
                String storedPassword = resultSet.getString("PASSWORD");
                connection.close();
                if (password.equals(storedPassword)) return true;
                else return false;
            } else return false;
        } catch (SQLException e) {
            System.out.println("Failed to login: " + e.getMessage());
        }
        return false;
    }

    public void showAllAdmin() {
        try {
            Connection connection = DriverManager.getConnection(DB_URL);
            PreparedStatement statement = connection.prepareStatement("SELECT * FROM ADMIN");
            ResultSet resultSet = statement.executeQuery();
            
            while(resultSet.next()) {
                System.out.print("ID = " + resultSet.getInt("ID") + "\t");
                System.out.print("ADMINACCOUNT = " + resultSet.getString("ADMINACCOUNT") + "\t");
                System.out.println("PASSWORD = " + resultSet.getString("PASSWORD"));
            }
            connection.close();
        } catch(SQLException e) {
            System.out.println("Failed to show all admins : " + e.getMessage());
        }
    }

    public boolean adminPasswordChange(String adminname, String oldPassword, String newPassword) {
        try {
            Connection connection = DriverManager.getConnection(DB_URL);
            PreparedStatement statement = connection.prepareStatement("UPDATE ADMIN set PASSWORD = ? WHERE ADMINACCOUNT = ? AND PASSWORD = ?");
            statement.setString(1, newPassword);
            statement.setString(2, adminname);
            statement.setString(3, oldPassword);
            int updateResult = statement.executeUpdate();
            connection.close();
            if(updateResult == 0) return false;
            else return true;
        } catch (SQLException e) {
            System.out.println("Failed to change password:" + e.getMessage());
            return false;
        }
    }

    public boolean deleteUserInfo(String username) {
        try {
            Connection connection = DriverManager.getConnection(DB_URL);
            PreparedStatement statement = connection.prepareStatement("DELETE FROM USER WHERE USERACCOUNT = ?");
            statement.setString(1, username);
            int updateResult = statement.executeUpdate();
            connection.close();
            if(updateResult == 0) return false;
            else return true;
        }catch(SQLException e) {
            System.out.println("Failed to delete user infomation: " + e.getMessage());
            return false;
        }
    }

    public void showAllGoods() {
        try {
            Connection connection = DriverManager.getConnection(DB_URL);
            PreparedStatement statement = connection.prepareStatement("SELECT * FROM GOODS");
            ResultSet resultSet = statement.executeQuery();
            while(resultSet.next()) {
                System.out.println("ID = " + resultSet.getString("ID"));
                System.out.println("NAME = " + resultSet.getString("NAME"));
                System.out.println("PRICE = " + resultSet.getString("PRICE"));
                System.out.println("QUANTITY = " + resultSet.getString("QUANTITY"));
            }
            connection.close();
        } catch (SQLException e) {
            System.out.println("Failed to show all goods intomation: " + e.getMessage());
        }
    } 

    public boolean addGoods(String name, double price, int quantity) {
        try {
            Connection connection = DriverManager.getConnection(DB_URL);
            PreparedStatement statement = connection.prepareStatement("INSERT INTO GOODS (ID, NAME, PRICE, QUANTITY) VALUES (?, ?, ?, ?)");
            statement.setInt(1, goodsID++);
            statement.setString(2, name);
            statement.setDouble(3, price);
            statement.setInt(4, quantity);
            statement.executeUpdate();
            connection.close();
            return true;
        } catch (SQLException e) {
            System.out.println("Failed to add goods: " + e.getMessage());
            return false;
        }
    }

    public boolean inquireGoods(String name) {
        try {
            Connection connection = DriverManager.getConnection(DB_URL);
            PreparedStatement statement = connection.prepareStatement("SELECT * FROM GOODS WHERE NAME = ?");
            statement.setString(1, name);
            ResultSet resultSet = statement.executeQuery();
            if(resultSet.next()){
                System.out.println("ID = " + resultSet.getInt("ID"));
                System.out.println("NAME = " + resultSet.getString("NAME"));
                System.out.println("PRICE = " + resultSet.getDouble("PRICE"));
                System.out.println("QUANTITY = " + resultSet.getInt("QUANTITY"));
                while(resultSet.next()) {
                    System.out.println("ID = " + resultSet.getInt("ID"));
                    System.out.println("NAME = " + resultSet.getString("NAME"));
                    System.out.println("PRICE = " + resultSet.getDouble("PRICE"));
                    System.out.println("QUANTITY = " + resultSet.getInt("QUANTITY"));
                }
                connection.close();
                return true;
            }else{
                connection.close();
                return false;
            }
        } catch (SQLException e) {
            System.out.println("Failed to inquire goods info: " + e.getMessage());
            return false;
        }
    }

    public boolean deleteGoods(int ID) {
        try {
            Connection connection = DriverManager.getConnection(DB_URL);
            PreparedStatement statement = connection.prepareStatement("DELETE FROM GOODS WHERE ID = ?");
            statement.setInt(1, ID);
            int deleteResult = statement.executeUpdate();
            connection.close();
            if(deleteResult == 0) return false;
            else return true;
        } catch (SQLException e) {
            System.out.println("Failed to delete goods infomation: " + e.getMessage());
            return false;
        }
    } 

    public boolean changeGoodsName(int ID, String name) {
        try {
            Connection connection = DriverManager.getConnection(DB_URL);
            PreparedStatement statement = connection.prepareStatement("UPDATE GOODS SET NAME = ? WHERE ID = ?");
            statement.setString(1, name);
            statement.setInt(2, ID);
            int changeNameResult = statement.executeUpdate();
            connection.close();
            if(changeNameResult == 0) return false;
            else return true;
        } catch (SQLException e) {
            System.out.println("Failed to change goods name: " + e.getMessage());
            return false;
        }
    }

    public boolean changeGoodsPrice(int ID, double price) {
        try {
            Connection connection = DriverManager.getConnection(DB_URL);
            PreparedStatement statement = connection.prepareStatement("UPDATE GOODS SET PRICE = ? WHERE ID = ?");
            statement.setDouble(1, price);
            statement.setInt(2, ID);
            int changeNameResult = statement.executeUpdate();
            connection.close();
            if(changeNameResult == 0) return false;
            else return true;
        } catch (SQLException e) {
            System.out.println("Failed to change goods price: " + e.getMessage());
            return false;
        }
    }

    public boolean changeGoodsQuantity(int id, int quantity) {
        try {
            Connection connection = DriverManager.getConnection(DB_URL);
            PreparedStatement statement = connection.prepareStatement("UPDATE GOODS SET QUANTITY = ? WHERE ID = ?");
            statement.setInt(1, quantity);
            statement.setInt(2, id);
            int changeNameResult = statement.executeUpdate();
            connection.close();
            if(changeNameResult == 0) return false;
            else return true;
        } catch (SQLException e) {
            System.out.println("Failed to change goods name: " + e.getMessage());
            return false;
        }
    }

    public boolean addGoodsToCart(int id, String username, int quantity) {
        try {
            Connection connection = DriverManager.getConnection(DB_URL);
            PreparedStatement statement = connection.prepareStatement("SELECT * FROM GOODS WHERE ID = ?");
            statement.setInt(1, id);
            ResultSet resultSet = statement.executeQuery();
            PreparedStatement statement2 = connection.prepareStatement("INSERT INTO SHOPPINGCART (USERACCOUNT, ID, GOODSNAME, PRICE, QUANTITY) VALUES (?, ?, ?, ?, ?)");
            if(resultSet.next()) {
                statement2.setString(1, username);
                statement2.setInt(2,id);
                statement2.setString(3, resultSet.getString("NAME"));
                statement2.setDouble(4, resultSet.getDouble("PRICE"));
                statement2.setInt(5, quantity);
                statement2.executeUpdate();
                connection.close();
                return true;
            }else {
                connection.close();
                return false;
            }
            
        } catch (SQLException e) {
            System.out.println("Failed to add goods to shopping cart: " + e.getMessage());
            return false;
        }
    }

    public boolean showUserShoppingCart(String username) {
        try {
            Connection connection = DriverManager.getConnection(DB_URL);
            PreparedStatement statement = connection.prepareStatement("SELECT * FROM SHOPPINGCART WHERE USERACCOUNT = ?");
            statement.setString(1, username);
            ResultSet resultSet = statement.executeQuery();
            while(resultSet.next()) {
                System.out.println("ID = " + resultSet.getInt("ID"));
                System.out.println("GOODSNAME = " + resultSet.getString("GOODSNAME"));
                System.out.println("PRICE = " + resultSet.getDouble("PRICE"));
                System.out.println("QUANTITY = " + resultSet.getInt("QUANTITY"));
            }
            connection.close();
            return true;
        } catch (SQLException e) {
            System.out.println("Failed to show user shopping cart" + e.getMessage());
            return false;
        }
    }
}
