package org.example;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {

        DatabaseInitializer databaseInitializer = new DatabaseInitializer();
        databaseInitializer.initializeDatabase();

        DatabaseManager dataBaseManager = new DatabaseManager();
        dataBaseManager.addAdmin();
        dataBaseManager.updataID();
        
        Scanner scanner = new Scanner(System.in);

        String userInput = "";

        boolean mainRunFlag = true;
        while(mainRunFlag){
            System.out.println("\n\n*******************************************");
            System.out.println("***************购物管理系统****************");
            System.out.println("*******************************************");
            System.out.println("请选择你的身份");
            System.out.println("1、管理员");
            System.out.println("2、用户");
            System.out.println("3、退出");
            System.out.print("->");

            userInput = scanner.nextLine();

            switch(userInput){
                case "1" : Admin admin = new Admin(scanner, dataBaseManager); admin.run(); break;
                case "2" : User user = new User(scanner, dataBaseManager); user.run(); break;
                case "3" : System.out.println("程序退出中"); mainRunFlag = false; break;
                default  : System.out.println("输入错误，请重新输入\n"); break;
            }
        }
        scanner.close();
        System.out.println("Done.");
    }
}