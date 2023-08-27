package org.example;

import java.util.Scanner;

public class Admin {
    private Scanner scanner = null;
    private DatabaseManager databaseManager = null;

    public Admin(Scanner scanner, DatabaseManager databaseManager){
        this.scanner = scanner;
        this.databaseManager = databaseManager;
    }

    private static boolean adminState = false;
    private static String adminAccount = "";
    public void run() {
        String adminInput = "";
        boolean runFlag = true;
        while(runFlag){
            //databaseManager.showAllAdmin();
            System.out.println("\n\n*******************************************");
            System.out.println("***************购物管理系统****************");
            System.out.println("*******************************************");
            System.out.println("当前身份：管理员");
            System.out.print("当前状态：");
            if(adminState) System.out.println("已登录");
            else System.out.println("未登录");
            if(!adminAccount.equals(""))
            System.out.println("当前管理员：" + adminAccount);
            System.out.println("\n请选择操作");
            System.out.println("1、登录");
            System.out.println("2、密码管理");
            System.out.println("3、客户管理");
            System.out.println("4、商品管理");
            System.out.println("5、退出登录");
            System.out.println("6、返回上一级");
            System.out.print("->");

            while(scanner.hasNext("\\n")) scanner.next();
            adminInput = scanner.nextLine();

            switch(adminInput){
                case "1" : Login login = new Login(scanner, databaseManager); login.run("admin"); break;
                case "2" : AdminPasswordManager adminPasswordManager = new AdminPasswordManager(scanner, databaseManager); adminPasswordManager.run(); break;
                case "3" : UserManager userManager = new UserManager(scanner, databaseManager); userManager.run(); break;
                case "4" : GoodsManager goodsManager = new GoodsManager(scanner, databaseManager); goodsManager.run(); break;
                case "5" : adminState = false; adminAccount = ""; break;
                case "6" : runFlag = false; adminState = false; adminAccount = ""; break;
                default  : System.out.println("输入错误，请重新输入\n"); break;
                
            }
        }
    }
    
    public static void setAdminState(boolean state) {
        adminState = state;
    }

    public static boolean getAdminState() {
        return adminState;
    }

    public static void setAdminAccount(String account) {
        adminAccount = account;
    }

    public static String getAdminAccount() {
        return adminAccount;
    }
}
