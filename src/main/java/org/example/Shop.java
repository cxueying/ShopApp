package org.example;
import java.util.Scanner;

public class Shop {
    Scanner scanner = null;
    DatabaseManager databaseManager = null;

    Shop(Scanner scanner, DatabaseManager databaseManager) {
        this.scanner = scanner;
        this.databaseManager = databaseManager;
    }

    public void run() {
        if(User.getUserState() == false) {
            System.out.println("当前状态未登录，请先登录");
            return;
        }

        boolean runFlag = true;
        String userInput = "";
        while(runFlag) {
            System.out.println("购物");
            System.out.println("1、将商品加入购物车");
            System.out.println("2、从购物车中移除商品");
            System.out.println("3、修改购物车中的商品");
            System.out.println("4、结账");
            System.out.println("5、查看购买历史");
            System.out.println("6、返回上一级");
            System.out.print("->");
            userInput = scanner.nextLine();

            switch(userInput) {
                case "1" : addGoodsToShoppingCart(); break;
                case "2" : removeGoodeFromShoppingCart(); break;
                case "3" : changeGoodsOnShoppingCart(); break;
                case "4" : checkout(); break;
                case "5" : inquirePurchaseHistory(); break;
                case "6" : runFlag = false; break;
                default  : System.out.println("输入错误，请重新输入"); break;
            }
        }
    }

    private void addGoodsToShoppingCart() {
        databaseManager.showAllGoods();
        System.out.print("请选择要添加商品ID：");
        int id = scanner.nextInt();
        System.out.print("请输入商品数量：");
        int quantity = scanner.nextInt();
        databaseManager.addGoodsToCart(id, User.getUserAccount(), quantity);
        databaseManager.showUserShoppingCart(User.getUserAccount());
        scanner.nextLine();
    }

    private void removeGoodeFromShoppingCart() {

    }

    private void changeGoodsOnShoppingCart() {

    }

    private void checkout() {

    }

    private void inquirePurchaseHistory() {

    }
}
