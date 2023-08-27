package org.example;
import java.util.Scanner;
import java.util.regex.Pattern;

public class UserPasswordManager {
    private Scanner scanner = null;
    private DatabaseManager databaseManager = null;

    public UserPasswordManager(Scanner scanner, DatabaseManager databaseManager) {
        this.scanner = scanner;
        this.databaseManager = databaseManager;
    }

    public void run() {
        String userInput = "";
        boolean runFlag = true;
        while(runFlag) {
            System.out.println("\n\n*******************************************");
            System.out.println("***************购物管理系统****************");
            System.out.println("*****************密码管理******************");
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
        if(User.getUserState() == false){
            System.out.println("当前状态未登录，请先登录");
            System.out.print("键入Enter键继续");
            scanner.nextLine();
            return;
        }
        System.out.println("修改密码");
        System.out.println("原密码");
        System.out.print("->");
        String oldPassword = scanner.nextLine();
        System.out.print("新密码：");
        String newPassword = "";
        while(true) {
            System.out.print("->");
            newPassword = scanner.nextLine();
            if(Pattern.compile("^(?![A-Za-z0-9]+$)(?![a-z0-9#?!@$%^&*-.]+$)(?![A-Za-z#?!@$%^&*-.]+$)(?![A-Z0-9#?!@$%^&*-.]+$)[a-zA-Z0-9#?!@$%^&*-.]{8,16}$").matcher(newPassword).matches()) break;
            System.out.println("新密码格式错误，请重新输入");
        }
        
        boolean success = databaseManager.userPasswordChange(User.getUserAccount(), oldPassword, newPassword);
        
        if(success) {
            System.out.println("密码修改成功!");
            databaseManager.passwordWrongTimesReset(User.getUserAccount());
            System.out.print("键入Enter继续");
            scanner.nextLine();
        } else {
            System.out.println("密码修改失败! ");
            System.out.print("键入Enter继续");
            scanner.nextLine();
        }
    }

    private void userPasswordReset() {
        boolean runFlag = true;
        while(runFlag) {
            System.out.println("密码重置");
            String userAccount = "";
            if(User.getUserState()) {
                userAccount = User.getUserAccount();
            } else {
                System.out.println("请输入用户名");
                System.out.print("->");
                userAccount = scanner.nextLine();
            }
            if(databaseManager.findUser(userAccount)) {
                String newPassword = databaseManager.userPasswordReset(userAccount);
                if(!newPassword.equals("error") && !newPassword.equals("fail")) {
                    if(sendNewPasswordToUserEmail(userAccount, newPassword)) {
                        System.out.println("密码重置成功，新密码已发送至用户邮箱");
                    }else {
                        System.out.println("密码重置失败");
                    }
                } else {
                    System.out.println("发生错误，操作失败");
                }
                runFlag = false;
                System.out.println("键入Enter键继续");
                scanner.nextLine();
            } else {
                System.out.println("重置密码失败，请检查用户名是否正确");
                System.out.println("是否继续(Y/N)");
                while(true) {
                    System.out.print("->");
                    String userInput = scanner.nextLine();
                    if(userInput.equals("y") || userInput.equals("Y")) {
                        break;
                    } else if(userInput.equals("n") || userInput.equals("N")) {
                        runFlag = false;
                        break;
                    } else {
                        System.out.println("输入错误，请重新输入");
                    }
                }
            }
        }
    }

    private boolean sendNewPasswordToUserEmail(String userAccount, String newPassword) {
        System.out.println("您账户的新密码为：" + newPassword);
        return true;
    }

    
}
