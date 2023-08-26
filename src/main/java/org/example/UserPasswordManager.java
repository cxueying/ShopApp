package org.example;
import java.util.Scanner;

public class UserPasswordManager {
    private Scanner scanner = null;
    private DatabaseManager databaseManager = null;

    public UserPasswordManager(Scanner scanner, DatabaseManager databaseManager) {
        this.scanner = scanner;
        this.databaseManager = databaseManager;
    }

    public void run() {
        if(User.getUserState() == false){
            System.out.println("当前状态未登录，请先登录");
            return;
        }
        String userInput = "";
        boolean runFlag = true;
        while(runFlag) {
            System.out.println("密码管理");
            System.out.println("请选择操作");
            System.out.println("1、修改密码");
            System.out.println("2、重置密码");
            System.out.println("3、退出");
            System.out.print("->");
            userInput = scanner.nextLine();
            switch(userInput) {
                case "1" : userPasswordChange(); runFlag = false; break;
                case "2" : userPasswordReset(); runFlag = false; break;
                case "3" : runFlag = false; break;
                default  : System.out.println("输入错误，请重新输入\n"); break;
            }
        }
    }

    private void userPasswordChange() {
        System.out.println("修改密码");
        System.out.print("原密码：");
        String oldPassword = scanner.nextLine();
        System.out.print("新密码：");
        String newPassword = scanner.nextLine();
        
        boolean success = databaseManager.userPasswordChange(User.getUserAccount(), oldPassword, newPassword);
        
        if(success) {
            System.out.println("密码修改成功!");
            System.out.print("键入Enter继续");
            scanner.nextLine();
        } else {
            System.out.println("密码修改失败! ");
            System.out.print("键入Enter继续");
            scanner.nextLine();
        }
    }

    private void userPasswordReset() {
        if(User.getUserState() == false){
            System.out.println("当前状态未登录，请先登录");
            return;
        }
        boolean runFlag = true;
        while(runFlag){
            System.out.println("是否需要重置密码（Y/N）");
            System.out.print("->");
            String userInput = scanner.nextLine();
            switch(userInput) {
                case "y" :
                case "Y" : {
                    if(databaseManager.userPasswordReset(User.getUserAccount())) {
                        System.out.println("密码重置成功！");
                        System.out.print("键入Enter键继续");
                        scanner.nextLine();
                    } else {
                        System.out.println("密码重置失败！请联系管理员处理");
                        System.out.print("键入Enter键继续");
                        scanner.nextLine();
                    }
                    runFlag = false;
                    break;
                }
                case "n" :
                case "N" : {
                    System.out.println("操作取消");
                    System.out.print("键入Enter键继续");
                    scanner.nextLine();
                    runFlag = false;
                    break;
                }
                default  : System.out.println("输入错误，请重新输入");break;
            }
        }
    }
}
