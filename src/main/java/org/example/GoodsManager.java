package org.example;
import java.util.Scanner;

public class GoodsManager {
    Scanner scanner = null;
    DatabaseManager databaseManager = null;

    GoodsManager(Scanner scanner, DatabaseManager databaseManager) {
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

        boolean runFlag = true;
        int userInput = -1;
        while(runFlag) {
            System.out.println("\n\n*******************************************");
            System.out.println("***************购物管理系统****************");
            System.out.println("*****************商品管理******************");
            System.out.println("1、列出所有商品信息");
            System.out.println("2、添加商品");
            System.out.println("3、修改商品信息");
            System.out.println("4、删除商品信息");
            System.out.println("5、查看商品信息");
            System.out.println("6、返回上一级");
            System.out.print("->");

            while(!scanner.hasNextInt()) scanner.next();
            userInput = scanner.nextInt();
            scanner.nextLine();

            switch(userInput) {
                case 1 : showAllGoodsInfo(); break;
                case 2 : addGoodsInfo(); break;
                case 3 : changeGoodsInfo(); break;
                case 4 : deleteGoodsInfo(); break;
                case 5 : inquireGoodsInfo(); break;
                case 6 : runFlag = false; break;
                default: System.out.println("输入错误，请重新输入"); break;
            }
        }
    }
    
    private void showAllGoodsInfo() {
        System.out.println("商品信息");
        databaseManager.showAllGoods();
    }

    private void addGoodsInfo() {
        boolean runFlag = true;
        String adminInput = "";
        int ID = -1;
        String name = "";
        String manufacturer = "";
        String manufactureData = "";
        String model = "";
        double restockingPrice = 0.0;
        double retailPrice = 0.0;
        int quantity = 0;
        while(runFlag) {
            System.out.println("添加商品");
            System.out.print("商品编号：");
            while(!scanner.hasNextInt()) scanner.next();
            ID = scanner.nextInt();
            scanner.nextLine();

            System.out.print("商品名称：");
            name = scanner.nextLine();

            System.out.print("生产厂家：");
            manufacturer = scanner.nextLine();

            System.out.print("生产日期：");
            manufactureData = scanner.nextLine();

            System.out.print("型号：");
            model = scanner.nextLine();

            System.out.print("进货价：");
            while(!scanner.hasNextDouble()) scanner.next();
            restockingPrice = scanner.nextDouble();
            scanner.nextLine();

            System.out.print("零售价：");
            while(!scanner.hasNextDouble()) scanner.next();
            retailPrice = scanner.nextDouble();
            scanner.nextLine();

            System.out.print("数量：");
            while(!scanner.hasNextInt()) scanner.next();
            quantity = scanner.nextInt();
            scanner.nextLine();

            boolean success = databaseManager.addGoods(ID,name,manufacturer,manufactureData,model,restockingPrice,retailPrice,quantity);
            if(success) System.out.println("添加商品成功");
            else System.out.println("添加商品失败");
            System.out.println("是否继续添加商品(Y/N)");
            while(true) { 
                System.out.print("->");
                while(scanner.hasNext("\\n")) scanner.next();
                scanner.nextLine();
                adminInput = scanner.nextLine();
                if(adminInput.equals("y") || adminInput.equals("Y")) {
                    break;
                } else if(adminInput.equals("n") || adminInput.equals("N")) {
                    System.out.println("操作结束");
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

    private void changeGoodsInfo() {
        boolean runFlag = true;
        while(runFlag) {
            System.out.print("请输入要修改信息的商品ID：");
            while(!scanner.hasNextInt()) scanner.next();
            int ID = scanner.nextInt();
            scanner.nextLine();

            boolean run2Flag = true;
            if(databaseManager.showGoodsInfo(ID)){
                System.out.println("请选择要修改的信息");
                System.out.println("1、商品名称");
                System.out.println("2、价格");
                System.out.println("3、数量");
                System.out.println("4、取消");
                while(run2Flag) {
                    System.out.print("->");
                    while(!scanner.hasNextInt()) scanner.next();
                    int choice = scanner.nextInt();
                    scanner.nextLine();
                    switch(choice) {
                        case 1 : {
                            System.out.print("请输入商品名称：");
                            String newName = scanner.nextLine();
                            if(databaseManager.changeGoodsName(ID, newName)) {
                                System.out.println("操作成功");
                                System.out.print("键入Enter继续");
                                scanner.nextLine();
                            } else {
                                System.out.println("操作失败");
                                System.out.print("键入Enter继续");
                                scanner.nextLine();
                            }
                            run2Flag = false;
                            runFlag = false;
                            break;
                        }
                        case 2 : {
                            System.out.print("请输入商品价格：");
                            double newPrice = scanner.nextDouble();
                            scanner.nextLine();
                            if(databaseManager.changeGoodsPrice(ID, newPrice)) {
                                System.out.println("操作成功");
                                System.out.print("键入Enter继续");
                                scanner.nextLine();
                            } else {
                                System.out.println("操作失败");
                                System.out.print("键入Enter继续");
                                scanner.nextLine();
                            }
                            run2Flag = false;
                            runFlag = false;
                            break;
                        }
                        case 3 : {
                            System.out.print("请输入商品数量：");
                            int newQuantity = scanner.nextInt();
                            scanner.nextLine();
                            if(databaseManager.changeGoodsQuantity(ID, newQuantity)) {
                                System.out.println("操作成功");
                                System.out.print("键入Enter继续");
                                scanner.nextLine();
                            } else {
                                System.out.println("操作失败");
                                System.out.print("键入Enter继续");
                                scanner.nextLine();
                            }
                            run2Flag = false;
                            runFlag = false;
                            break;
                        }
                        case 4 : {
                            System.out.println("操作取消");
                            System.out.print("键入Enter继续");
                            scanner.nextLine();
                            run2Flag = false;
                            runFlag = false;
                            break;
                        }
                        default  : {
                            System.out.println("输入错误");
                            System.out.println("请重新输入");
                            scanner.nextLine();
                            break;
                        }
                    }
                }
            } else {
                String adminInput;
                System.out.println("id为" + ID + "的商品不存在，是否继续(Y/N)");
                while(true) {
                    System.out.print("->");
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
        }
    }

    private void deleteGoodsInfo() {
        System.out.print("请输入要删除商品ID：");
        while(!scanner.hasNextInt()) scanner.next();
        int ID = scanner.nextInt();
        scanner.nextLine();

        if(databaseManager.showGoodsInfo(ID)) {
            System.out.println("是否要删除该商品(Y/N)");
            String adminInput = "";
            while(true) {
                System.out.print("->");
                adminInput = scanner.nextLine();
                if(adminInput.equals("y") || adminInput.equals("Y")) {
                    if(databaseManager.deleteGoods(ID)){
                        System.out.println("删除商品成功");
                        System.out.print("键入Enter键继续");
                        scanner.nextLine();
                    }else {
                        System.out.println("删除商品失败");
                        System.out.print("键入Enter键继续");
                        scanner.nextLine();
                    }
                    break;
                } else if(adminInput.equals("n") || adminInput.equals("N")){
                    System.out.println("操作取消");
                    System.out.print("键入Enter键继续");
                    scanner.nextLine();
                    break;
                } else {
                    System.out.println("输入错误，请重新输入");
                }
            }
        }else {
            System.out.println("该商品不存在！");
            System.out.print("键入Enter继续");
            scanner.nextLine();
        }
        
    }

    private void inquireGoodsInfo() {
        System.out.print("请输入要查询商品ID：");
        while(!scanner.hasNextInt()) scanner.next();
        int ID = scanner.nextInt();
        scanner.nextLine();
        if(!databaseManager.showGoodsInfo(ID)) {
            System.out.println("查询失败,该ID的商品不存在");
        }
        System.out.print("键入Enter键继续");
        scanner.nextLine();
    }
}
