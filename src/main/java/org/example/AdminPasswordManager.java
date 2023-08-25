package org.example;

import java.util.Scanner;

public class AdminPasswordManager {
    Scanner scanner = null;
    DatabaseManager databaseManager = null;

    public AdminPasswordManager(Scanner scanner, DatabaseManager databaseManager) {
        this.scanner = scanner;
        this.databaseManager = databaseManager;
    }

    public void run() {
        if(Admin.getAdminState() == false) {
            System.out.println("当前状态未登录，请先登录");
            System.out.print("键入Enter继续");
            scanner.nextLine();
            return;
        }
        String adminInput = "";
        boolean runFlag = true;
        while(runFlag) {
            System.out.println("密码管理");
            System.out.println("请选择操作");
            System.out.println("1、修改密码");
            System.out.println("2、重置用户密码");
            System.out.println("3、退出");
            System.out.print("->");
            adminInput = scanner.nextLine();
            switch(adminInput) {
                case "1" : adminPasswordChange(); runFlag = false; break;
                case "2" : adminResetUserPassword(); runFlag = false; break;
                case "3" : runFlag = false; break;
                default  : System.out.println("输入错误，请重新输入"); break;
            }
        }
    }
    
    private void adminPasswordChange(){
        System.out.println("修改密码");
        System.out.print("原密码：");
        String oldPassword = scanner.nextLine();
        System.out.print("新密码：");
        String newPassword = scanner.nextLine();
        
        boolean success = databaseManager.adminPasswordChange(Admin.getAdminAccount(), oldPassword, newPassword);
        
        if(success) {
            System.out.println("密码修改成功");
            System.out.print("键入Enter继续");
            scanner.nextLine();
        } else {
            System.out.println("密码修改失败");
            System.out.print("键入Enter继续");
            scanner.nextLine();
        }
    }

    private void adminResetUserPassword() {
        String userInput = "";
        boolean runFlag = true;
        while(runFlag) {
            System.out.println("用户密码管理");
            System.out.println("请选择操作");
            System.out.println("1、重置指定用户密码");
            System.out.println("2、重置所有用户密码");
            System.out.println("3、退出");
            System.out.print("->");
            userInput = scanner.nextLine();

            switch(userInput) {
                case "1" : resetSpecifyUserPassword(); runFlag = false; break;
                case "2" : resetAllUserPassword(); runFlag = false; break;
                case "3" : runFlag = false; break;
                default  : System.out.println("输入错误，请重新输入\n");
            }
        }
    }

    private void resetSpecifyUserPassword() {
        boolean runFlag = true;
        String username = "";
        String adminInput = "";
        while(runFlag) {
            System.out.println("重置用户密码");
            System.out.print("用户名：");

            while(scanner.hasNext("\\n")) scanner.next();
            username = scanner.nextLine();

            if(databaseManager.fineUser(username)) {
                while(true) {
                    System.out.println("是否重置该用户的密码(Y/N)：" + username);
                    System.out.print("->");
                    adminInput = scanner.nextLine();
                    if(adminInput.equals("y") || adminInput.equals("Y")) {
                        if(databaseManager.userPasswordReset(username)) {
                            System.out.println("用户密码重置成功");
                            System.out.print("键入Enter继续");
                            scanner.nextLine();
                            runFlag = false;
                            break;
                        }
                    } else if(adminInput.equals("n") || adminInput.equals("N")){
                        System.out.println("操作取消");
                        System.out.print("键入Enter继续");
                        scanner.nextLine();
                        runFlag = false;
                        break;
                    } else {
                        System.out.println("输入错误，请重新输入");
                    }
                }
            } else {
                System.out.println("该用户不存在，是否继续(Y/N)");
                while(true) {
                    System.out.print("->");
                    while(scanner.hasNext("\\n")) scanner.next();
                    adminInput = scanner.nextLine();
                    if(adminInput.equals("y") || adminInput.equals("Y")) {
                        break;
                    } else if(adminInput.equals("n") || adminInput.equals("N")){
                        runFlag = false;
                        break;
                    } else {
                        System.out.println("输入错误，请重新输入");
                    }
                }
            }
            // boolean success = databaseManager.userPasswordReset(username);
            // if(success) {
            //     System.out.println("用户密码重置成功！");
            //     System.out.print("键入Enter继续");
            //     scanner.nextLine();
            //     runFlag = false;
            // }else {
            //     System.out.println("该用户不存在，是否继续（Y/N）？");
            //     System.out.print("->");
            //     String adminInput = scanner.nextLine();
            //     if(adminInput.equals("y") || adminInput.equals("Y")){
            //         continue;
            //     }else {
            //         System.out.println("操作取消");
            //         System.out.print("键入Enter继续");
            //         scanner.nextLine();
            //         runFlag = false;
            //     }
            // }
        }
    }

    private void resetAllUserPassword() {
        System.out.println("是否用重置所有用户密码（Y/N）？");
        System.out.print("->");
        String adminInput = scanner.nextLine();
        if(adminInput.equals("y") || adminInput.equals("Y")) {
            boolean success = databaseManager.allUserPasswordReset();
            if(success) {
                System.out.println("操作成功");
                System.out.print("键入Enter继续");
                scanner.nextLine();
            }else {
                System.out.println("操作失败");
                System.out.print("键入Enter继续");
                scanner.nextLine();
            }
        }else {
            System.out.println("操作取消");
            System.out.print("键入Enter继续");
            scanner.nextLine();
        }
    }
}