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
            databaseManager.showUserShoppingCart(User.getUserAccount());
            System.out.println("购物");
            System.out.println("1、将商品加入购物车");
            System.out.println("2、从购物车中移除商品");
            System.out.println("3、修改购物车中的商品");
            System.out.println("4、结账");
            System.out.println("5、查看购买历史");
            System.out.println("6、返回上一级");
            System.out.print("->");

            while(scanner.hasNext("\\n")) {
                scanner.next();
            }
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
    }

    private void removeGoodeFromShoppingCart() {
        databaseManager.showUserShoppingCart(User.getUserAccount());
        System.out.print("请选择要删除的商品ID：");
        int id = scanner.nextInt();
        if(databaseManager.deleteUserGoodFromCart(id, User.getUserAccount())) {
            System.out.println("删除成功");
        } else {
            System.out.println("操作失败，购物车中不存在该商品");
        }
        
    }

    private void changeGoodsOnShoppingCart() {
        databaseManager.showUserShoppingCart(User.getUserAccount());
        System.out.print("请输入商品id：");
        int id = scanner.nextInt();
        if(databaseManager.showUserGoods(id, User.getUserAccount())) {
            System.out.println("请选择要修改的信息：");
            System.out.println("1、修改商品数量");
            System.out.println(("2、取消"));
            System.out.print("->");
            while(!scanner.hasNextInt()) {
                scanner.next();
            }
            int choice = scanner.nextInt();
            switch(choice) {
                case 1 : {
                    System.out.print("请输入新的数量：");
                    while(!scanner.hasNextInt()) {
                        scanner.next();
                    }
                    int newQuantity = scanner.nextInt();
                    if(databaseManager.changeUserGoodsQuantity(id, User.getUserAccount(), newQuantity)) {
                        System.out.println("修改成功");
                    } else {
                        System.out.println("修改失败");
                    }
                    break;
                }
                case 2 : System.out.println(("操作取消!"));break;
                default: return;
            }
        } else {
            System.out.println("输入id不存在购物车中，操作取消");
        }
    }

    private void checkout() {
        databaseManager.showUserShoppingCart(User.getUserAccount());
        double price = databaseManager.checkout(User.getUserAccount());
        if(price == -2) {
            System.out.println("商品库存不足，结账失败");
        } else {
            System.out.println("总价格：" + price);
            System.out.println("交易成功");
        }
        
    }

    private void inquirePurchaseHistory() {
        databaseManager.showUserShopHistory(User.getUserAccount());
    }
}
