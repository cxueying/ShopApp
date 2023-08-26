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
            if(databaseManager.userShoppingCartEmpty(User.getUserAccount())) {
                System.out.println("购物车");
                databaseManager.showUserShoppingCart(User.getUserAccount());
            }
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
        System.out.println("商品列表");
        databaseManager.showAllGoods();

        String userInput = "";
        boolean runFlag = true;
        while(runFlag) {
            System.out.print("请选择要添加商品ID：");
            while(!scanner.hasNextInt()) scanner.next();
            int ID = scanner.nextInt();
            scanner.nextLine();

            if(databaseManager.findGoods(ID)) {
                System.out.print("请输入商品数量：");
                while(!scanner.hasNextInt()) scanner.next();
                int quantity = scanner.nextInt();
                scanner.nextLine();

                if(databaseManager.addGoodsToCart(ID, User.getUserAccount(), quantity)) {
                    System.out.println("商品加入购物车成功");
                    System.out.println("是否继续添加商品到购物车(Y/N)");
                    while(true) {
                        System.out.print("->");
                        userInput = scanner.nextLine();
                        if(userInput.equals("y") || userInput.equals("Y")) {
                            break;
                        } else if(userInput.equals("n") || userInput.equals("N")) {
                            runFlag = false;
                            break;
                        } else {
                            System.out.println("输入错误，请重新输入");
                        }
                    }
                } else {
                    System.out.println("商品加入购物车失败！请联系管理员");
                    runFlag = false;
                    System.out.print("键入Enter继续");
                    scanner.nextLine();
                }
            } else {
                System.out.println("ID为 " + ID + " 的商品不存在，是否重新选择(Y/N)");
                while(true) {
                    System.out.print("->");
                    userInput = scanner.nextLine();
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

    private void removeGoodeFromShoppingCart() {
        String userInput = "";
        boolean runFlag = true;
        while(runFlag) {
            System.out.println("请选择要删除的商品ID");
            System.out.print("->");
            while(!scanner.hasNextInt()) scanner.next();
            int ID = scanner.nextInt();
            scanner.nextLine();

            if(databaseManager.findUserGoods(ID)) {
                System.out.println("是否删除ID为 " + ID + " 的商品(Y/N)");
                while(true) {
                    System.out.print("->");
                    userInput = scanner.nextLine();
                    if(userInput.equals("y") || userInput.equals("Y")) {
                        if(databaseManager.deleteUserGoodFromCart(ID, User.getUserAccount())) {
                            System.out.println("删除成功！");
                            System.out.print("键入Enter键继续");
                            scanner.nextLine();
                            runFlag = false;
                            break;
                        } else {
                            System.out.println("删除失败！请联系管理员处理");
                            System.out.print("键入Enter键继续");
                            scanner.nextLine();
                            runFlag = false;
                            break;
                        }
                    } else if(userInput.equals("n") || userInput.equals("N")) {
                        System.out.println("操作取消");
                        System.out.print("键入Enter键继续");
                        scanner.nextLine();
                        runFlag = false;
                        break;
                    } else {
                        System.out.println("输入错误，请重新输入");
                    }
                }
            } else {
                System.out.println("所输入ID为 " + ID+ " 的商品不存在购物车中，是否继续(Y/N)");
                while(true) {
                    System.out.print("->");
                    userInput = scanner.nextLine();
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

    private void changeGoodsOnShoppingCart() {
        String userInput = "";
        boolean runFlag = true;
        while(runFlag) {
            System.out.print("请输入商品ID：");
            while(!scanner.hasNextInt()) scanner.next();
            int ID = scanner.nextInt();
            scanner.nextLine();
            
            if(databaseManager.findUserGoods(ID)) {
                System.out.println("请选择要修改的信息：");
                System.out.println("1、修改商品数量");
                System.out.println(("2、取消"));
                boolean run2Flag = true;
                while(run2Flag) {
                    System.out.print("->");
                    while(!scanner.hasNextInt()) scanner.next();
                    int choice = scanner.nextInt();
                    scanner.nextLine();

                    switch(choice) {
                        case 1 : {
                            System.out.print("请输入新的数量：");
                            while(!scanner.hasNextInt()) scanner.next();
                            int newQuantity = scanner.nextInt();
                            scanner.nextLine();

                            if(databaseManager.changeUserGoodsQuantity(ID, User.getUserAccount(), newQuantity)) {
                                System.out.println("修改成功");
                                System.out.print("键入Enter键继续");
                                scanner.nextLine();
                                

                            } else {
                                System.out.println("修改失败");
                                System.out.print("键入Enter键继续");
                                scanner.nextLine();
                            }
                            run2Flag = false;
                            runFlag = false;
                            break;
                        }
                        case 2 : {
                            System.out.println(("操作取消!"));
                            System.out.print("键入Enter键继续");
                            scanner.nextLine();
                            run2Flag = false;
                            runFlag = false;
                            break;
                        }
                        default: {
                            System.out.println("输入错误，请重新输入");
                        }
                    }
                }
            } else {
                System.out.println("ID为 " + ID + " 的商品不存在购物车中，是否继续(Y/N)");
                System.out.print("->");
                userInput = scanner.nextLine();
                if(userInput.equals("y") || userInput.equals("Y")) {
                    
                } else if(userInput.equals("n") || userInput.equals("N")) {
                    System.out.println("操作取消");
                    System.out.print("键入Enter键继续");
                    scanner.nextLine();
                    runFlag = false;
                    break;
                } else {
                    System.out.println("输入错误，请重新输入");
                }
            }
        }
    }

    private void checkout() {
        double price = databaseManager.checkout(User.getUserAccount());
        if(price == -2) {
            System.out.println("商品库存不足，结账失败");
            System.out.print("键入Enter键继续");
            scanner.nextLine();
        } else {
            System.out.println("总价格：" + price);
            System.out.println("交易成功");
            System.out.print("键入Enter键继续");
            scanner.nextLine();
        }
        
    }

    private void inquirePurchaseHistory() {
        databaseManager.showUserShopHistory(User.getUserAccount());
        System.out.print("键入Enter键继续");
        scanner.nextLine();
    }
}
