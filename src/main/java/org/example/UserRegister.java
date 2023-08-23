package org.example;

import java.util.Scanner;

public class UserRegister{

    private Scanner scanner = null;
    private DatabaseManager userManager = null;

    public UserRegister(Scanner scanner, DatabaseManager userManager) {
        this.scanner = scanner;
        this.userManager = userManager;
    }

    public void run() {
        boolean runFlag = true;

        while (runFlag) {
            System.out.print("请输入用户名:");
            String username = this.scanner.nextLine();

            System.out.print("请输入密码:");
            String password = this.scanner.nextLine();

            boolean success = this.userManager.userRegister(username, password);
            
            if(success){
                System.out.println("注册成功");
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
