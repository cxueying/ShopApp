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

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import java.util.Random;


public class DatabaseManager {
    private static int userID = 1;
    

    private static final String DB_URL = "jdbc:sqlite:dataBase.db";

    private static String md5(String data) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] md5 = md.digest(data.getBytes(StandardCharsets.UTF_8));
            StringBuilder sb = new StringBuilder();
            for(byte b: md5) {
                sb.append(String.format("%02x", b));
            }
            return sb.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return "";
    }

    public boolean userRegister(String userAccount, String password, String phoneNumber, String email) {
        try {
            Connection connection = DriverManager.getConnection(DB_URL);
            
            PreparedStatement statement2 = connection.prepareStatement("SELECT * FROM USER WHERE USERACCOUNT = ?");
            ResultSet resultSet = statement2.executeQuery();
            if(resultSet.next()){
                connection.close();
                return false;
            }

            Date date = new Date();
            TimeZone timeZone = TimeZone.getTimeZone("Asia/Shanghai");
            DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
            dateFormat.setTimeZone(timeZone);
            String time = dateFormat.format(date);

            PreparedStatement statement = connection.prepareStatement("INSERT INTO USER (ID, USERACCOUNT, LEVEL, REGISTERTIME, TOTALCOST, PHONENUMBER, EMAIL, PASSWORD, STATE, PASSWORDWRONGTIMES) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");
            statement.setInt(1, userID++);
            statement.setString(2, userAccount);
            statement.setString(3, "铜牌");
            statement.setString(4,time);
            statement.setDouble(5, 0.0);
            statement.setString(6, phoneNumber);
            statement.setString(7, email);
            statement.setString(8, md5(password));
            statement.setString(9, "正常");
            statement.setInt(10, 0);
            statement.executeUpdate();
            connection.close();
            return true;
        } catch (SQLException e) {
            System.out.println("Failed to register user: " + e.getMessage());
        }
        return false;
    }

    public boolean wrongPassword(String userAccount) {
        try {
            Connection connection = DriverManager.getConnection(DB_URL);
            PreparedStatement statement = connection.prepareStatement("UPDATE USER SET PASSWORDWRONGTIMES = ((SELECT PASSWORDWRONGTIMES FROM USER WHERE USERACCOUNT = ?) + 1) WHERE USERACCOUNT = ?");
            statement.setString(1, userAccount);
            statement.setString(2,userAccount);
            int updateResult = statement.executeUpdate();
            connection.close();
            if(updateResult != 0) return true;
            else return false;
        } catch (Exception e) {
            System.out.println("Failed to add password wrong times: " + e.getMessage());
            return false;
        }
    }

    public int passwordWrongTimes(String userAccount) {
        try {
            Connection connection = DriverManager.getConnection(DB_URL);
            PreparedStatement statement = connection.prepareStatement("SELECT PASSWORDWRONGTIMES FROM USER WHERE USERACCOUNT = ?");
            statement.setString(1, userAccount);
            ResultSet resultSet = statement.executeQuery();
            if(resultSet.next()) {
                int wrongTimes = resultSet.getInt("PASSWORDWRONGTIMES");
                connection.close();
                return wrongTimes;
            }else {
                connection.close();
                return -1;
            }
        } catch (Exception e) {
            System.out.println("Failed to inquire password wrong times:" + e.getMessage());
            return -2;
        }
    }

    public boolean passwordWrongTimesReset(String userAccount) {
        try {
            Connection connection = DriverManager.getConnection(DB_URL);
            PreparedStatement statement = connection.prepareStatement("UPDATE USER SET PASSWORDWRONGTIMES = 0 WHERE USERACCOUNT = ?");
            statement.setString(1, userAccount);
            int updateResult = statement.executeUpdate();
            connection.close();
            if(updateResult != 0) return true;
            else return false;
        } catch (Exception e) {
            System.out.println("Failed to reset password wrong times: " + e.getMessage());
            return false;
        }
    }

    public boolean lockUser(String userAccount) {
        try {
            Connection connection = DriverManager.getConnection(DB_URL);
            PreparedStatement statement = connection.prepareStatement("UPDATE USER SET STATE = ? WHERE USERACCOUNT = ?");
            statement.setString(1, "上锁");
            statement.setString(2, userAccount);
            connection.close();
            return true;
        } catch (Exception e) {
            System.out.println("Failed to lock user account: " + e.getMessage());
            return false;
        }
    }

    public boolean userLogin(String username, String password) {
        
        try {
            Connection connection = DriverManager.getConnection(DB_URL);
            PreparedStatement statement = connection.prepareStatement("SELECT * FROM USER WHERE USERACCOUNT = ?");

            statement.setString(1, username);
            ResultSet resultSet = statement.executeQuery();
            if(resultSet.getString("STATE").equals("上锁")) {
                connection.close();
                System.out.println("该账户已上锁，请重置密码");
                return false;
            }
            if (resultSet.next()) {
                String storedPassword = resultSet.getString("PASSWORD");
                connection.close();
                if (md5(password).equals(storedPassword)) return true;
                else return false;
            } else return false;
        } catch (SQLException e) {
            System.out.println("Failed to login: " + e.getMessage());
        }
        return false;
    }

    public boolean findUser(String userAccount) {
        try {
            Connection connection = DriverManager.getConnection(DB_URL);
            PreparedStatement statement = connection.prepareStatement("SELECT * FROM USER WHERE USERACCOUNT = ?");
            statement.setString(1, userAccount);
            ResultSet resultSet = statement.executeQuery();
            if(resultSet.next()) {
                connection.close();
                return true;
            } else {
                connection.close();
                return false;
            }
        } catch (Exception e) {
            System.out.println("Failed to find user: " + e.getMessage());
            return false;
        }
    }

    public boolean userPasswordChange(String username, String oldPassword, String newPassword) {
        try {
            Connection connection = DriverManager.getConnection(DB_URL);
            PreparedStatement statement = connection.prepareStatement("UPDATE USER set PASSWORD = ? WHERE USERACCOUNT = ? AND PASSWORD = ?");
            statement.setString(1, md5(newPassword));
            statement.setString(2, username);
            statement.setString(3, md5(oldPassword));
            int updateResult = statement.executeUpdate();
            connection.close();
            if(updateResult == 0) return false;
            else return true;
        } catch (SQLException e) {
            System.out.println("Failed to change password:" + e.getMessage());
            return false;
        }
    }

    public String userPasswordReset(String userAccount) {
        String newPassword = getRandomPassword(10);
        try {
            Connection connection = DriverManager.getConnection(DB_URL);
            PreparedStatement statement = connection.prepareStatement("UPDATE USER SET PASSWORD = ? WHERE USERACCOUNT = ?");
            statement.setString(1, md5(newPassword));
            statement.setString(2, userAccount);
            int updateResult = statement.executeUpdate();
            connection.close();
            if(updateResult != 0) return newPassword;
            else return "fail";
        } catch (Exception e) {
            System.out.println("Failed to reset user password: " + e.getMessage());
            return "error";
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
                System.out.printf("%5s\t%12s\t%8s\t%18s\t%9s\t%13s\t%18s\n", "ID", "用户名", "用户级别", "注册时间", "累计消费", "手机号", "邮箱");
                System.out.printf("%5d\t%12s\t%8s\t%18s\t%9.2f\t%13s\t%18s\n", 
                    resultSet.getInt("ID"),
                    resultSet.getString("USERACCOUNT"),
                    resultSet.getString("LEVEL"),
                    resultSet.getString("REGISTERTIME"),
                    resultSet.getDouble("TOTALCOST"),
                    resultSet.getString("PHONENUMBER"),
                    resultSet.getString("EMAIL")
                );
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
            
            System.out.printf("%5s\t%12s\t%8s\t%18s\t%9s\t%13s\t%18s\n", "ID", "用户名", "用户级别", "注册时间", "累计消费", "手机号", "邮箱");
            while(resultSet.next()) {
                System.out.printf("%5d\t%12s\t%8s\t%18s\t%9.2f\t%13s\t%18s\n", 
                    resultSet.getInt("ID"),
                    resultSet.getString("USERACCOUNT"),
                    resultSet.getString("LEVEL"),
                    resultSet.getString("REGISTERTIME"),
                    resultSet.getDouble("TOTALCOST"),
                    resultSet.getString("PHONENUMBER"),
                    resultSet.getString("EMAIL")
                );
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
            statement.setString(3, md5("ynuinfo#777"));
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
                if (md5(password).equals(storedPassword)) return true;
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
            statement.setString(1, md5(newPassword));
            statement.setString(2, adminname);
            statement.setString(3, md5(oldPassword));
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

            statement = connection.prepareStatement("DELETE FROM SHOPPINGCART WHERE USERACCOUNT = ?");
            statement.setString(1, username);
            statement.executeUpdate();

            statement = connection.prepareStatement("DELETE FROM SHOPHISTORY WHERE USERACCOUNT = ?");
            statement.setString(1, username);
            statement.executeUpdate();

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
            System.out.printf("%-12s%-12s  %-12s  %-10s  %-12s  %-8s  %-8s  %-8s\n", 
                "编号", "名称", "生产厂家", "生产日期", "型号", "进货价", "零售价格", "数量"
            );
            while(resultSet.next()) {
                System.out.printf("%-12d  %-12s  %-12s  %-10s  %-12s  %8.2f  %8.2f  %8d\n", 
                    resultSet.getInt("ID"),
                    resultSet.getString("NAME"),
                    resultSet.getString("MANUFACTURER"),
                    resultSet.getString("MANUFACTUREDATA"),
                    resultSet.getString("MODEL"),
                    resultSet.getDouble("RESTOCKINGPRICE"),
                    resultSet.getDouble("RETAILPRICE"),
                    resultSet.getInt("QUANTITY")
                );
                
            }
            connection.close();
        } catch (SQLException e) {
            System.out.println("Failed to show all goods intomation: " + e.getMessage());
        }
    } 

    public boolean findGoods(int ID) {
        try {
            Connection connection = DriverManager.getConnection(DB_URL);
            PreparedStatement statement = connection.prepareStatement("SELECT * FROM GOODS WHERE ID = ?");
            statement.setInt(1, ID);
            ResultSet resultSet = statement.executeQuery();
            if(resultSet.next()) {
                connection.close();
                return true;
            } else {
                connection.close();
                return false;
            }
        } catch (Exception e) {
            System.out.println("Failed to find goods: " + e.getMessage());
            return false;
        }
    }

    public boolean addGoods(
        int ID, String name, String manufacturer, 
        String manufactureData, String model, Double restockingPrice,
        Double retailPrice, int quantity
        ) {
        try {
            Connection connection = DriverManager.getConnection(DB_URL);
            PreparedStatement statement = connection.prepareStatement("INSERT INTO GOODS (ID, NAME, MANUFACTURER, MANUFACTUREDATA, MODEL, RESTOCKINGPRICE, RETAILPRICE, QUANTITY) VALUES (?, ?, ?, ?, ?, ?, ?, ?)");
            statement.setInt(1, ID);
            statement.setString(2, name);
            statement.setString(3, manufacturer);
            statement.setString(4, manufactureData);
            statement.setString(5, model);
            statement.setDouble(6, restockingPrice);
            statement.setDouble(7, retailPrice);
            statement.setInt(8, quantity);
            statement.executeUpdate();
            connection.close();
            return true;
        } catch (SQLException e) {
            System.out.println("Failed to add goods: " + e.getMessage());
            return false;
        }
    }

    public boolean showGoodsInfo(int ID) {
        try {
            Connection connection = DriverManager.getConnection(DB_URL);
            PreparedStatement statement = connection.prepareStatement("SELECT * FROM GOODS WHERE ID = ?");
            statement.setInt(1, ID);
            ResultSet resultSet = statement.executeQuery();
            System.out.printf("%-12s%-12s  %-12s  %-10s  %-12s  %-8s  %-8s  %-8s\n", 
                "编号", "名称", "生产厂家", "生产日期", "型号", "进货价", "零售价格", "数量"
            );
            if(resultSet.next()) {
                System.out.printf("%-12d  %-12s  %-12s  %-10s  %-12s  %-8.2f  %-8.2f  %-8d\n", 
                    resultSet.getInt("ID"),
                    resultSet.getString("NAME"),
                    resultSet.getString("MANUFACTURER"),
                    resultSet.getString("MANUFACTUREDATA"),
                    resultSet.getString("MODEL"),
                    resultSet.getDouble("RESTOCKINGPRICE"),
                    resultSet.getDouble("RETAILPRICE"),
                    resultSet.getInt("QUANTITY")
                );
                connection.close();
                return true;
            } else {
                connection.close();
                return false;
            }
        } catch (Exception e) {
            System.out.println("Failed to show goods info: " + e.getMessage());
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

    public boolean changeGoodsID(int ID, int newID) {
        try {
            Connection connection = DriverManager.getConnection(DB_URL);
            PreparedStatement statement = connection.prepareStatement("UPDATE GOODS SET ID = ? WHERE ID = ?");
            statement.setInt(1, newID);
            statement.setInt(2, ID);
            int updataResult = statement.executeUpdate();
            connection.close();
            if(updataResult != 0) return true;
            else return false;
        } catch (Exception e) {
            System.out.println("Failed to chang goods ID: " + e.getMessage());
            return false;
        }
    }

    public boolean changeGoodsName(int ID, String newName) {
        try {
            Connection connection = DriverManager.getConnection(DB_URL);
            PreparedStatement statement = connection.prepareStatement("UPDATE GOODS SET NAME = ? WHERE ID = ?");
            statement.setString(1, newName);
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

    public boolean changeGoodsManufacturer(int ID, String newManufacturer) {
        try {
            Connection connection = DriverManager.getConnection(DB_URL);
            PreparedStatement statement = connection.prepareStatement("UPDATE GOODS SET MANUFACTURER = ?WHERE ID = ?");
            statement.setString(1, newManufacturer);
            statement.setInt(2, ID);
            int updataResult = statement.executeUpdate();
            connection.close();
            if(updataResult != 0) return true;
            else return false;
        } catch (Exception e) {
            System.out.println("Failed to change goods manufacturer: " + e.getMessage());
            return false;
        }
    }

    public boolean changeGoodsManufactureData(int ID, String newManufactureData) {
        try {
            Connection connection = DriverManager.getConnection(DB_URL);
            PreparedStatement statement = connection.prepareStatement("UPDATE GOODS SET MANUFACTUREDATA = ? WHERE ID = ?");
            statement.setString(1, newManufactureData);
            statement.setInt(2, ID);
            int updataResult = statement.executeUpdate();
            connection.close();
            if(updataResult != 0) return true;
            else return false;
        } catch (Exception e) {
            System.out.println("Failed to change goods manufacture data: " + e.getMessage());
            return false;
        }
    }

    public boolean changeGoodsModel(int ID, String newModel) {
        try {
            Connection connection = DriverManager.getConnection(DB_URL);
            PreparedStatement statement = connection.prepareStatement("UPDATE GOODS SET MODEL = ? WHERE ID = ?");
            statement.setString(1, newModel);
            statement.setInt(2, ID);
            int updataResult = statement.executeUpdate();;
            connection.close();
            if(updataResult != 0) return true;
            else return false;
        } catch (Exception e) {
            System.out.println("Failed to change goods model: " + e.getMessage());
            return false;
        }
    }

    public boolean changeGoodsRestockingPrice(int ID, double newRestockingPrice) {
        try {
            Connection connection = DriverManager.getConnection(DB_URL);
            PreparedStatement statement = connection.prepareStatement("UPDATE GOODS SET RESTOCKINGPRICE = ? WHERE ID = ?");
            statement.setDouble(1, newRestockingPrice);
            statement.setInt(2, ID);
            int updataResult = statement.executeUpdate();
            connection.close();
            if(updataResult != 0) return true;
            else return false;
        } catch (Exception e) {
            System.out.println("Failed to change goods restocking price: " + e.getMessage());
            return false;
        }
    }

    public boolean changeGoodsRetailPrice(int ID, double newRetailPrice) {
        try {
            Connection connection = DriverManager.getConnection(DB_URL);
            PreparedStatement statement = connection.prepareStatement("UPDATE GOODS SET RETAILPRICE = ? WHERE ID = ?");
            statement.setDouble(1, newRetailPrice);
            statement.setInt(2, ID);
            int updataResult = statement.executeUpdate();
            connection.close();
            if(updataResult != 0) return true;
            else return false;
        } catch (Exception e) {
            System.out.println("Failed to change goods retail price: " + e.getMessage());
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

    public boolean inquireGoodsInfoByName(String name) {
        try {
            Connection connection = DriverManager.getConnection(DB_URL);
            PreparedStatement statement = connection.prepareStatement("SELECT * FROM GOODS WHERE NAME = ?");
            statement.setString(1, name);
            ResultSet resultSet = statement.executeQuery();
            boolean findResult = false;
            boolean firstPrint = true;
            while(resultSet.next()) {
                findResult = true;
                if(firstPrint) {
                    System.out.printf("%12s\t%12s\t%12s\t%12s\t%12s\t%8s\t%8s\t%8s\n", 
                        " 编号", "名称", "生产厂家", "生产日期", "型号", "进货价", "零售价格", "数量"
                    );
                    firstPrint = false;
                }
                System.out.printf("%12s\t%12s\t%12s\t%12s\t%12s\t%8s\t%8s\t%8s\n", 
                    resultSet.getInt("ID"),
                    resultSet.getString("NAME"),
                    resultSet.getString("MANUFACTURER"),
                    resultSet.getString("MANUFACTUREDATA"),
                    resultSet.getString("MODEL"),
                    resultSet.getDouble("RESTOCKINGPRICE"),
                    resultSet.getDouble("RETAILPRICE"),
                    resultSet.getInt("QUANTITY")
                );
            }
            return findResult;
        } catch (Exception e) {
            System.out.println("Failed to inquire goods info by name: " + e.getMessage());
            return false;
        }
    }

    public boolean inquireGoodsInfoByManufacturer(String manufacturer) {
        try {
            Connection connection = DriverManager.getConnection(DB_URL);
            PreparedStatement statement = connection.prepareStatement("SELECT * FROM GOODS WHERE MANUFACTURER = ?");
            statement.setString(1, manufacturer);
            ResultSet resultSet = statement.executeQuery();
            boolean findResult = false;
            boolean firstPrint = true;
            while(resultSet.next()) {
                findResult = true;
                if(firstPrint) {
                    System.out.printf("%12s\t%12s\t%12s\t%12s\t%12s\t%8s\t%8s\t%8s\n", 
                        " 编号", "名称", "生产厂家", "生产日期", "型号", "进货价", "零售价格", "数量"
                    );
                    firstPrint = false;
                }
                System.out.printf("%12s\t%12s\t%12s\t%12s\t%12s\t%2s\t%8s\t%8s\n", 
                    resultSet.getInt("ID"),
                    resultSet.getString("NAME"),
                    resultSet.getString("MANUFACTURER"),
                    resultSet.getString("MANUFACTUREDATA"),
                    resultSet.getString("MODEL"),
                    resultSet.getDouble("RESTOCKINGPRICE"),
                    resultSet.getDouble("RETAILPRICE"),
                    resultSet.getInt("QUANTITY")
                );
            }
            return findResult;
        } catch (Exception e) {
            System.out.println("Failed to inquire goods info by name: " + e.getMessage());
            return false;
        }
    }

    public boolean inquireGoodsInfoByRetailPriceO(double retailPrice) {
        try {
            Connection connection = DriverManager.getConnection(DB_URL);
            PreparedStatement statement = connection.prepareStatement("SELECT * FROM GOODS WHERE RETAILPRICE > ?");
            statement.setDouble(1, retailPrice);
            ResultSet resultSet = statement.executeQuery();
            boolean findResult = false;
            boolean firstPrint = true;
            while(resultSet.next()) {
                findResult = true;
                if(firstPrint) {
                    System.out.printf("%12s\t%12s\t%12s\t%12s\t%12s\t%8s\t%8s\t%8s\n", 
                        " 编号", "名称", "生产厂家", "生产日期", "型号", "进货价", "零售价格", "数量"
                    );
                    firstPrint = false;
                }
                System.out.printf("%12s\t%12s\t%12s\t%12s\t%12s\t%8s\t%8s\t%8s\n", 
                    resultSet.getInt("ID"),
                    resultSet.getString("NAME"),
                    resultSet.getString("MANUFACTURER"),
                    resultSet.getString("MANUFACTUREDATA"),
                    resultSet.getString("MODEL"),
                    resultSet.getDouble("RESTOCKINGPRICE"),
                    resultSet.getDouble("RETAILPRICE"),
                    resultSet.getInt("QUANTITY")
                );
            }
            return findResult;
        } catch (Exception e) {
            System.out.println("Failed to inquire goods info by name: " + e.getMessage());
            return false;
        }
    }

    public boolean addGoodsToCart(int id, String userAccount, int quantity) {
        try {
            Connection connection = DriverManager.getConnection(DB_URL);
            PreparedStatement statement = connection.prepareStatement("SELECT * FROM SHOPPINGCART WHERE ID = ? AND USERACCOUNT = ?");
            statement.setInt(1, id);
            statement.setString(2, userAccount);
            ResultSet resultSet = statement.executeQuery();
            if(resultSet.next()) {//购物车中已经存在该商品
                statement = connection.prepareStatement("UPDATE SHOPPINGCART SET QUANTITY = ? WHERE ID = ? AND USERACCOUNT = ?");
                statement.setInt(1, resultSet.getInt("QUANTITY") + quantity);
                statement.setInt(2, id);
                statement.setString(3, userAccount);
                statement.executeUpdate();
                connection.close();
                return true;
            }else {//购物车中未存在该商品
                PreparedStatement statement2 = connection.prepareStatement("SELECT * FROM GOODS WHERE ID = ?");
                statement2.setInt(1, id);
                resultSet = statement2.executeQuery();
                statement2 = connection.prepareStatement("INSERT INTO SHOPPINGCART (USERACCOUNT, ID, GOODSNAME, PRICE, QUANTITY) VALUES (?, ?, ?, ?, ?)");
                if(resultSet.next()) {
                    statement2.setString(1, userAccount);
                    statement2.setInt(2,id);
                    statement2.setString(3, resultSet.getString("NAME"));
                    statement2.setDouble(4, resultSet.getDouble("RETAILPRICE"));
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

    public boolean findUserGoods(int ID) {
        try {
            Connection connection = DriverManager.getConnection(DB_URL);
            PreparedStatement statement = connection.prepareStatement("SELECT * FROM SHOPPINGCART WHERE ID = ?");
            statement.setInt(1, ID);
            ResultSet resultSet = statement.executeQuery();
            if(resultSet.next()) {
                connection.close();
                return true;
            } else {
                connection.close();
                return false;
            }
        } catch (Exception e) {
            System.out.println("Failed to find goods on user's shopping cart: " + e.getMessage());
            return false;
        }
    }

    public boolean showUserShoppingCart(String userAccount) {
        try {
            Connection connection = DriverManager.getConnection(DB_URL);
            PreparedStatement statement = connection.prepareStatement("SELECT * FROM SHOPPINGCART WHERE USERACCOUNT = ?");
            statement.setString(1, userAccount);
            ResultSet resultSet = statement.executeQuery();
            System.out.printf("%-5s\t%-15s\t%-8s\t%8s\n", "ID", "GOODSNAME", "QUANTITY", "PRICE");
            while(resultSet.next()) {
                System.out.printf("%-5d\t%-15s\t%8d\t%8.2f\n",
                    resultSet.getInt("ID"),
                    resultSet.getString("GOODSNAME"),
                    resultSet.getInt("QUANTITY"),
                    resultSet.getDouble("PRICE")
                );
            }
            connection.close();
            return true;
        } catch (SQLException e) {
            System.out.println("Failed to show user shopping cart:" + e.getMessage());
            return false;
        }
    }

    public boolean deleteUserGoodFromCart(int id, String userAccount) {
        try {
            Connection connection = DriverManager.getConnection(DB_URL);
            PreparedStatement statement = connection.prepareStatement("DELETE FROM SHOPPINGCART WHERE ID = ? AND USERACCOUNT = ?");
            statement.setInt(1, id);
            statement.setString(2, userAccount);
            int updateResult = statement.executeUpdate();
            connection.close();
            if(updateResult != 0) return true;
            else return false;
        } catch (Exception e) {
            System.out.println("Failed to delete user's goods from shopping cart: " + e.getMessage());
            return false;
        }
    }

    public boolean changeUserGoodsQuantity(int id, String userAccount ,int newQuantity) {
        try {
            Connection connection = DriverManager.getConnection(DB_URL);
            PreparedStatement statement = connection.prepareStatement("UPDATE SHOPPINGCART SET QUANTITY = ? WHERE ID = ? AND USERACCOUNT = ?");
            statement.setInt(1, newQuantity);
            statement.setInt(2, id);
            statement.setString(3, userAccount);
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
                PreparedStatement addPriceTotalCostStatement = connection.prepareStatement("UPDATE USER SET TOTALCOST = ((SELECT TOTALCOST FROM USER WHERE USERACCOUNT = ?) + ?) WHERE USERACCOUNT = ?");
                addPriceTotalCostStatement.setDouble(2, price);
                addPriceTotalCostStatement.setString(1, userAccount);
                addPriceTotalCostStatement.setString(3, userAccount);
                addPriceTotalCostStatement.executeUpdate();
            }
            connection.close();
            return price;
        } catch (Exception e) {
            System.out.println("Failed to check out: " + e.getMessage());
            return -1;
        }
    }

    public boolean userShoppingCartEmpty(String userAccount) {
        try {
            Connection connection = DriverManager.getConnection(DB_URL);
            PreparedStatement statement = connection.prepareStatement("SELECT * FROM SHOPPINGCART WHERE USERACCOUNT = ?");
            statement.setString(1, userAccount);
            ResultSet resultSet = statement.executeQuery();
            if(resultSet.next()) {
                connection.close();
                return true;
            } else {
                connection.close();
                return false;
            }
        } catch (Exception e) {
            System.out.println("Failed to inquire the user shopping cart situation: " + e.getMessage());
            return false;
        }
    }

    public boolean showUserShopHistory(String userAccount) {
        try {
            Connection connection = DriverManager.getConnection(DB_URL);
            PreparedStatement statement = connection.prepareStatement("SELECT * FROM SHOPHISTORY WHERE USERACCOUNT = ?");
            statement.setString(1, userAccount);
            ResultSet resultSet = statement.executeQuery();
            System.out.printf("%-5s %-18s %-8s    %-8s       %-4s\n", "编号", "商品名称", "数量", "价格", "时间");
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

    //返回随机产生的8位数
    private String getRandomPassword(int len) {
        String result= this.makeRandomPassword(len);
        if (result.matches(".*[a-z]{1,}.*") && result.matches(".*[A-Z]{1,}.*") && result.matches(".*\\d{1,}.*") && result.matches(".*[~!@#$%^&*\\.?]{1,}.*")) {
            return result;
        }
        result = makeRandomPassword(len);
        return result;
    }

    //产生8位随机数
    private String makeRandomPassword(int len){
        char charr[] = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890~!@#$%^&*.?".toCharArray();
        StringBuilder sb = new StringBuilder();
        Random r = new Random();
        for (int x = 0; x < len; ++x) {
            sb.append(charr[r.nextInt(charr.length)]);
        }
        return sb.toString();
    }
}
