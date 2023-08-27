package org.example;

import java.util.Scanner;
import java.util.regex.Pattern;

public class UserRegister{

    private Scanner scanner = null;
    private DatabaseManager databaseManager = null;

    public UserRegister(Scanner scanner, DatabaseManager databaseManager) {
        this.scanner = scanner;
        this.databaseManager = databaseManager;
    }

    public void run() {
        boolean runFlag = true;

        while (runFlag) {
            System.out.println("\n\n*******************************************");
            System.out.println("***************购物管理系统****************");
            System.out.println("*******************注册********************");
            System.out.println("请输入用户名");
            String userAccount = "";
            while(true) {
                System.out.print("->");
                userAccount = scanner.nextLine();
                if(Pattern.compile("^[a-zA-Z][a-zA-Z0-9_]{4,15}$").matcher(userAccount).matches()) break;
                System.out.println("用户名长度需要在5-16位，且只能使用字母、数字、下划线，必须以字母开头");
                System.out.println("请重新输入");
            }

            System.out.println("请输入密码:");
            String password = "";
            while(true) {
                System.out.print("->");
                password = scanner.nextLine();
                if(Pattern.compile("^(?![A-Za-z0-9]+$)(?![a-z0-9#?!@$%^&*-.]+$)(?![A-Za-z#?!@$%^&*-.]+$)(?![A-Z0-9#?!@$%^&*-.]+$)[a-zA-Z0-9#?!@$%^&*-.]{8,16}$").matcher(password).matches()) break;
                System.out.println("密码长度需要在8-16位，必须是大小写字母、数字以及特殊符号的组合");
                System.out.println("请重新输入");
            }

            System.out.println("请输入手机号");
            String phoneNumber = "";
            while(true) {
                System.out.print("->");
                phoneNumber = scanner.nextLine();
                if(Pattern.compile("^(1[0-9])\\d{9}$").matcher(phoneNumber).matches()) break;
                System.out.println("手机号必须以1开头，并且长度为11位");
                System.out.println("请重新输入");
            }

            System.out.println("请输入邮箱");
            String email = "";
            while(true) {
                System.out.print("->");
                email = scanner.nextLine();
                if(Pattern.compile("^\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*$").matcher(email).matches()) break;
                System.out.println("请输入正确的邮箱地址");
                System.out.println("请重新输入");
            }

            boolean success = databaseManager.userRegister(userAccount, password, phoneNumber, email);
            
            if(success){
                System.out.println("注册成功");
                System.out.print("键入Enter键继续");
                scanner.nextLine();
                break;
            }else{
                System.out.println("该用户已经存在");
                System.out.println("注册失败");
                boolean continueFlag = true;
                while(continueFlag){
                    System.out.println("是否需要重新注册（Y/N）");
                    System.out.print("->");
                    String userInput = scanner.nextLine();
                    switch(userInput){
                        case "y" :
                        case "Y" : {
                            continueFlag = false;
                            break;
                        }
                        case "n" :
                        case "N" : {
                            continueFlag = false;
                            runFlag = false;
                            break;
                        }
                        default  : {
                            System.out.println("输入错误，请重新输入");
                        }
                    }
                }
            }
        }
    }
}
