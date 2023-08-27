package org.example;
import java.util.Scanner;

public class User{
    private Scanner scanner = null;
    private DatabaseManager databaseManager = null;

    public User(Scanner scanner, DatabaseManager databaseManager) {
        this.scanner = scanner;
        this.databaseManager = databaseManager;
    }
    
    private static boolean userState = false;
    private static String userAccount = "";

    public void run() {
        String userInput = "";
        boolean runFlag = true;
        while(runFlag){
            //databaseManager.showAllUser();//********************** */
            System.out.println("\n\n*******************************************");
            System.out.println("***************购物管理系统****************");
            System.out.println("*******************************************");
            System.out.println("当前身份：用户");
            System.out.print("当前状态：");
            if(userState) System.out.println("已登录");
            else System.out.println("未登录");
            if(!userAccount.equals(""))
            System.out.println("当前用户：" + userAccount);
            System.out.println("\n请选择操作");
            System.out.println("1、注册");
            System.out.println("2、登录");
            System.out.println("3、密码管理");
            System.out.println("4、购物");
            System.out.println("5、退出登录");
            System.out.println("6、返回上一级");
            System.out.print("->");
            userInput = scanner.nextLine();

            switch(userInput){
                case "1" : UserRegister userRegister = new UserRegister(scanner, databaseManager); userRegister.run(); break;
                case "2" : Login login = new Login(scanner, databaseManager); login.run("user"); break;
                case "3" : UserPasswordManager userPasswordManager = new UserPasswordManager(scanner, databaseManager); userPasswordManager.run(); break;
                case "4" : Shop shop = new Shop(scanner, databaseManager); shop.run(); break;
                case "5" : userState = false; userAccount = ""; System.out.println("退出成功\n"); break;
                case "6" : userState = false; userAccount = ""; runFlag = false; break;
                default  : System.out.println("输入错误，请重新输入\n"); break;
            }
        }
    }
    
    public static boolean getUserState() {
        return userState;
    }

    public static void setUserState(boolean state) {
        userState = state;
    }

    public static String getUserAccount() {
        return userAccount;
    }

    public static void setUserAccount(String account) {
        userAccount = account;
    }
}