package org.example;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.TimeZone;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;


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
            PreparedStatement statement = connection.prepareStatement("SELECT * FROM SHOPPINGCART WHERE ID = ?");
            statement.setInt(1, id);
            ResultSet resultSet = statement.executeQuery();
            if(resultSet.next()) {
                statement = connection.prepareStatement("UPDATE SHOPPINGCART SET QUANTITY = ? WHERE ID = ?");
                statement.setInt(1, resultSet.getInt("QUANTITY") + quantity);
                statement.setInt(2, id);
                statement.executeUpdate();
                connection.close();
                return true;
            }else {
                PreparedStatement statement2 = connection.prepareStatement("SELECT * FROM GOODS WHERE ID = ?");
                statement2.setInt(1, id);
                resultSet = statement2.executeQuery();
                statement2 = connection.prepareStatement("INSERT INTO SHOPPINGCART (USERACCOUNT, ID, GOODSNAME, PRICE, QUANTITY) VALUES (?, ?, ?, ?, ?)");
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
            System.out.println("Failed to show user shopping cart:" + e.getMessage());
            return false;
        }
    }

    public boolean deleteUserGoodFromCart(int id) {
        try {
            Connection connection = DriverManager.getConnection(DB_URL);
            PreparedStatement statement = connection.prepareStatement("DELETE FROM SHOPPINGCART WHERE ID = ?");
            statement.setInt(1, id);
            int updateResult = statement.executeUpdate();
            connection.close();
            if(updateResult != 0) return true;
            else return false;
        } catch (Exception e) {
            System.out.println("Failed to delete user's goods from shopping cart: " + e.getMessage());
            return false;
        }
    }

    public boolean showUserGoods(int id) {
        try {
            Connection connection = DriverManager.getConnection(DB_URL);
            PreparedStatement statement = connection.prepareStatement("SELECT * FROM SHOPPINGCART WHERE ID = ?");
            statement.setInt(1, id);
            ResultSet resultSet = statement.executeQuery();
            if(resultSet.next()) {
                System.out.println("ID = " + resultSet.getInt("ID") + " PRICE = " + resultSet.getDouble("PRICE") + " QUANTITY = " + resultSet.getInt("QUANTITY"));
                connection.close();
                return true;
            } else {
                connection.close();
                return false;
            }
        } catch (Exception e) {
            System.out.println("Failed to show user's goods on shopping cart: " + e.getMessage());
            return false;
        }
    }

    public boolean changeUserGoodsQuantity(int id, int newQuantity) {
        try {
            Connection connection = DriverManager.getConnection(DB_URL);
            PreparedStatement statement = connection.prepareStatement("UPDATE SHOPPINGCART SET QUANTITY = ? WHERE ID = ?");
            statement.setInt(1, newQuantity);
            statement.setInt(2, id);
            int updateResult = statement.executeUpdate();
            connection.close();
            if(updateResult != 0) return true;
            else return false;
        } catch (Exception e) {
            System.out.println("Failed to change quantity: " + e.getMessage());
            return false;
        }
    }

    public double checkout(String userAccount) {//实现计算总价、更新商品信息、添加购物信息到历史记录、清空用户购物车
        try {
            double price = 0;
            Connection connection = DriverManager.getConnection(DB_URL);
            PreparedStatement cartStatement = connection.prepareStatement("SELECT * FROM SHOPPINGCART WHERE USERACCOUNT = ?");//购物车
            PreparedStatement goodsStatement = connection.prepareStatement("SELECT * FROM GOODS WHERE ID = ?");//商品
            cartStatement.setString(1, userAccount);
            ResultSet cartResultSet = cartStatement.executeQuery();
            ResultSet goodsResultSet;
            while(cartResultSet.next()) {//计算总价格，判断购物车商品数量是否大于库存数量，大于则返回-2
                goodsStatement.setInt(1, cartResultSet.getInt("ID"));
                goodsResultSet = goodsStatement.executeQuery();
                if(cartResultSet.getInt("QUANTITY") > goodsResultSet.getInt("QUANTITY")) {//用户购物车商品数量大于货物数量
                    price = -2;
                    break;
                }
                price += cartResultSet.getDouble("PRICE") * cartResultSet.getInt("QUANTITY");
            }

            //获取东八区时间
            Date date = new Date();
            TimeZone timeZone = TimeZone.getTimeZone("Asia/Shanghai");
            DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
            dateFormat.setTimeZone(timeZone);
            String time = dateFormat.format(date);

            if(price != -2) {
                PreparedStatement goodsUpdateStatement = connection.prepareStatement("UPDATE GOODS SET QUANTITY = ? WHERE ID = ?");//更新商品数量
                PreparedStatement addToShopHistoryStatement = connection.prepareStatement("INSERT INTO SHOPHISTORY (TIME, USERACCOUNT, ID, GOODSNAME, QUANTITY, PRICE) VALUES (?, ?, ?, ?, ?, ?)");//将购入信息加入表SHOPHISTORY
                PreparedStatement deleteStatement = connection.prepareStatement("DELETE FROM SHOPPINGCART WHERE ID = ?");//删除购物车中的商品

                cartResultSet = cartStatement.executeQuery();

                while(cartResultSet.next()) {
                    //更新商品信息
                    goodsStatement.setInt(1, cartResultSet.getInt("ID"));
                    goodsResultSet = goodsStatement.executeQuery();
                    goodsUpdateStatement.setInt(1, goodsResultSet.getInt("QUANTITY") - cartResultSet.getInt("QUANTITY"));
                    goodsUpdateStatement.setInt(2, cartResultSet.getInt("ID"));
                    goodsUpdateStatement.executeUpdate();

                    //将购入信息加入表SHOPHISTORY
                    addToShopHistoryStatement.setString(1, time);
                    addToShopHistoryStatement.setString(2, userAccount);
                    addToShopHistoryStatement.setInt(3, cartResultSet.getInt("ID"));
                    addToShopHistoryStatement.setString(4, cartResultSet.getString("GOODSNAME"));
                    addToShopHistoryStatement.setInt(5, cartResultSet.getInt("QUANTITY"));
                    addToShopHistoryStatement.setDouble(6, cartResultSet.getDouble("PRICE"));
                    addToShopHistoryStatement.executeUpdate();

                    //删除购物车中的商品
                    deleteStatement.setInt(1, cartResultSet.getInt("ID"));
                    deleteStatement.executeUpdate();
                }
            }
            connection.close();
            return price;
        } catch (Exception e) {
            System.out.println("Failed to check out: " + e.getMessage());
            return -1;
        }
    }

    public boolean showUserShopHistory(String userAccount) {
        try {
            Connection connection = DriverManager.getConnection(DB_URL);
            PreparedStatement statement = connection.prepareStatement("SELECT * FROM SHOPHISTORY WHERE USERACCOUNT = ?");
            statement.setString(1, userAccount);
            ResultSet resultSet = statement.executeQuery();
            System.out.printf("%-5s %-18s %-8s    %-8s       %-4s\n", "ID", "GoodsName", "Quantity", "Price", "Time");
            while(resultSet.next()) {
                System.out.printf("%-5d %-18s %-8d    %-8.2f %-18s\n", 
                    resultSet.getInt("ID"),
                    resultSet.getString("GOODSNAME"),
                    resultSet.getInt("QUANTITY"), resultSet.getDouble("PRICE"),
                    resultSet.getString("TIME")
                );
            }
            return true;
        } catch (Exception e) {
            System.out.println("Failed to show user's shop history: " + e.getMessage());
            return false;
        }
    }
}
