package org.example;

import java.util.Scanner;

public class Login {

    private Scanner scanner = null;
    private DatabaseManager databaseManager = null;

    public Login(Scanner scanner, DatabaseManager databaseManager) {
        this.scanner = scanner;
        this.databaseManager = databaseManager;
    }

    public boolean run(String identity) {
        System.out.println("\n\n*******************************************");
        System.out.println("***************购物管理系统****************");
        System.out.println("*******************登录********************");
        if(User.getUserState() == true || Admin.getAdminState() == true) {
            System.out.println("当前已登录，请先退出登录");
            System.out.print("键入Enter键继续");
            scanner.nextLine();
            return false;
        }

        String account = "";
        String password = "";
        String input = "";

        boolean runFlag = true;
        while(runFlag){
            System.out.print("用户名：");
            account = scanner.nextLine();
            System.out.print("密码：");
            password = scanner.nextLine();
            
            boolean success = false;
            if(identity.equals("user")) {
                success = databaseManager.userLogin(account, password);
                if(success) {
                    User.setUserAccount(account);
                    User.setUserState(true);
                }
            }else{
                success = databaseManager.adminLogin(account, password);
                if(success) {
                    Admin.setAdminAccount(account);
                    Admin.setAdminState(true);
                }
            }
            if(success){
                System.out.println("登录成功");
                return true;
            }else {
                System.out.println("登录失败！！！");
                boolean continueFlag = true;
                while(continueFlag){
                    System.out.println("请选择操作");
                    System.out.println("1、重新登录");
                    System.out.println("2、退出");
                    System.out.print("->");
                    input = scanner.nextLine();
                    switch(input){
                        case "1" : {
                            continueFlag = false;
                            break;
                        }
                        case "2" : {
                            runFlag = false;
                            continueFlag = false;
                            break;
                        }
                        default  : {
                            System.out.println("输入错误，请重新输入");
                        }
                    }
                }
            }
        }
        return false;
    }
}
