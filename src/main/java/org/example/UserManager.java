package org.example;

import java.util.Scanner;

public class UserManager {
    Scanner scanner = null;
    DatabaseManager databaseManager = null;

    UserManager(Scanner scanner, DatabaseManager databaseManager) {
        this.scanner = scanner;
        this.databaseManager = databaseManager;
    }

    public void run() {
        if(Admin.getAdminState() == false) {
            System.out.println("当前状态未登录，请先登录");
            System.out.print("键入任意键继续");
            scanner.nextLine();
            return;
        }
        boolean runFlag = true;
        String adminInput = "";
        while(runFlag) {
            System.out.println("客户管理");
            System.out.println("1、列出所有客户信息");
            System.out.println("2、删除客户信息");
            System.out.println("3、查询客户信息");
            System.out.println("4、返回上一级");
            System.out.print("->");
            adminInput = scanner.nextLine();
            
            switch(adminInput) {
                case "1" : showAllUserInfo(); break;
                case "2" : deleteUserInfo(); break;
                case "3" : inquireUserInfo(); break;
                case "4" : runFlag = false; break;
                default  : System.out.println("输入错误，请重新输入\n"); break;
            }
        }
    }

    private void showAllUserInfo() {
        databaseManager.showAllUser();
    }

    private void deleteUserInfo() {
        String username = "";
        String adminInput = "";
        boolean runFlag = true;
        while(runFlag) {
            System.out.print("请输入用户名：");
            username = scanner.nextLine();
            if(databaseManager.findUser(username)){
                databaseManager.showUserInfo(username);
                System.out.println("是否要删除该用户信息（Y/N）?");
                while(true) {
                    System.out.print("->");
                    adminInput = scanner.nextLine();
                    if(adminInput.equals("y") || adminInput.equals("Y")){
                        boolean success = databaseManager.deleteUserInfo(username);
                        if(success) {
                            System.out.println("删除成功");
                            System.out.print("键入任意键继续");
                            scanner.nextLine();
                            runFlag = false;
                            break;
                        } else {
                            System.out.println("删除失败");
                            System.out.print("键入任意键继续");
                            scanner.nextLine();
                            runFlag = false;
                            break;
                        }
                    }else if(adminInput.equals("n") || adminInput.equals("N")){
                        System.out.println("操作取消");
                        System.out.print("键入任意键继续");
                        scanner.nextLine();
                        runFlag = false;
                        break;
                    } else {
                        System.out.println("输入错误，请重新输入");
                    }
                }
            }else {
                System.out.println("操作失败，该用户不存在！");
                System.out.println("是否继续(Y/N)");
                while(true) {
                    System.out.print("->");
                    while(scanner.hasNext("\\n")) scanner.next();
                    adminInput = scanner.nextLine();
                    if(adminInput.equals("y") || adminInput.equals("Y")){
                        break;
                    }else if(adminInput.equals("n") || adminInput.equals("N")){
                        runFlag = false;
                        break;
                    } else {
                        System.out.println("输入错误，请重新输入");
                    }
                }
                
            }
        }
    }

    private void inquireUserInfo(){
        // System.out.print("请输入用户名：");
        // String username = scanner.nextLine();
        // if(databaseManager.showUserInfo(username));
        // else System.out.println("查询失败，该用户不存在！");
        
        String username = "";
        String adminInput = "";
        boolean runFlag = true;
        while(runFlag) {
            System.out.print("请输入要查询的用户名：");
            while(scanner.hasNext("\\n")) scanner.next();
            username = scanner.nextLine();
            if(databaseManager.showUserInfo(username)) {
                System.out.print("键入任意键继续");
                scanner.nextLine();
                runFlag = false;
            } else {
                System.out.println("查询失败，该用户不存在");
                System.out.println("是否继续查询(Y/N)");
                while(true) {
                    System.out.print("->");
                    while(scanner.hasNext("\\n")) scanner.next();
                    adminInput = scanner.nextLine();
                    if(adminInput.equals("y") || adminInput.equals("Y")){
                        break;
                    }else if(adminInput.equals("n") || adminInput.equals("N")){
                        runFlag = false;
                        break;
                    } else {
                        System.out.println("输入错误，请重新输入");
                    }
                }
            }
        }
    }
}