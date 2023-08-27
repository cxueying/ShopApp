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
            System.out.println("5、查询商品信息");
            System.out.println("6、返回上一级");
            System.out.print("->");

            while(!scanner.hasNextInt()) scanner.next();
            userInput = scanner.nextInt();
            scanner.nextLine();

            switch(userInput) {
                case 1 : showAllGoodsInfo(); break;
                case 2 : addGoods(); break;
                case 3 : changeGoodsInfo(); break;
                case 4 : deleteGoods(); break;
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

    private void addGoods() {
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
            if(databaseManager.findGoods(ID)){
                while(run2Flag) {
                    databaseManager.showGoodsInfo(ID);
                    System.out.println("请选择要修改的信息");
                    System.out.println("1、商品编号");
                    System.out.println("2、商品名称");
                    System.out.println("3、生产厂家");
                    System.out.println("4、生产日期");
                    System.out.println("5、型号");
                    System.out.println("6、进货价");
                    System.out.println("7、零售价格");
                    System.out.println("8、数量");
                    System.out.println("9、退出");
                    System.out.print("->");
                    while(!scanner.hasNextInt()) scanner.next();
                    int choice = scanner.nextInt();
                    scanner.nextLine();
                    switch(choice) {
                        case 1 : changeGoodsID(ID); break;
                        case 2 : changeGoodsName(ID); break;
                        case 3 : changeGoodsManufacturer(ID); break;
                        case 4 : changeGoodsManufactureData(ID); break;
                        case 5 : changeGoodsModel(ID); break;
                        case 6 : changeGoodsRestockingPrice(ID); break;
                        case 7 : changeGoodsRetailPrice(ID); break;
                        case 8 : changeGoodsQuantity(ID); break;
                        case 9 : run2Flag = false; runFlag = false; break;
                        default: System.out.println("输入错误，请重新输入"); break;
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

    private void changeGoodsID(int ID) {
        System.out.print("请输入修改后的商品编号：");
        while(!scanner.hasNextInt()) scanner.next();
        int newID = scanner.nextInt();
        scanner.nextLine();

        System.out.println("修改后的商品编号为：" + newID);
        System.out.println("是否修改(Y/N)");

        while(true) {
            System.out.print("->");
            String adminInput = scanner.nextLine();
            if(adminInput.equals("y") || adminInput.equals("Y")) {
                if(databaseManager.changeGoodsID(ID, newID)) {
                    System.out.println("修改成功！");
                } else {
                    System.out.println("修改失败！请检查商品编号是否重复！");
                }
                System.out.print("键入Enter键继续");
                scanner.nextLine();
                break;
            } else if (adminInput.equals("n") || adminInput.equals("N")) {
                System.out.println("操作取消");
                System.out.print("键入Enter键继续");
                scanner.nextLine();
                break;
            } else {
                System.out.println("输入有误，请重新输入");
            }
        }
    }

    private void changeGoodsName(int ID) {
        System.out.print("请输入修改后的商品名称：");
        String newName = scanner.nextLine();

        System.out.println("修改后的商品名称为：" + newName);
        System.out.println("是否修改(Y/N)");

        while(true) {
            System.out.print("->");
            String adminInput = scanner.nextLine();
            if(adminInput.equals("y") || adminInput.equals("Y")) {
                if(databaseManager.changeGoodsName(ID, newName)) {
                    System.out.println("修改成功！");
                } else {
                    System.out.println("修改失败！");
                }
                System.out.print("键入Enter键继续");
                scanner.nextLine();
                break;
            } else if (adminInput.equals("n") || adminInput.equals("N")) {
                System.out.println("操作取消");
                System.out.print("键入Enter键继续");
                scanner.nextLine();
                break;
            } else {
                System.out.println("输入有误，请重新输入");
            }
        }
    }

    private void changeGoodsManufacturer(int ID) {
        System.out.print("请输入修改后的生产厂家：");
        String newManufacturer = scanner.nextLine();

        System.out.println("修改后的生产厂家为：" + newManufacturer);
        System.out.println("是否修改(Y/N)");

        while(true) {
            System.out.print("->");
            String adminInput = scanner.nextLine();
            if(adminInput.equals("y") || adminInput.equals("Y")) {
                if(databaseManager.changeGoodsManufacturer(ID, newManufacturer)) {
                    System.out.println("修改成功！");
                } else {
                    System.out.println("修改失败！");
                }
                System.out.print("键入Enter键继续");
                scanner.nextLine();
                break;
            } else if (adminInput.equals("n") || adminInput.equals("N")) {
                System.out.println("操作取消");
                System.out.print("键入Enter键继续");
                scanner.nextLine();
                break;
            } else {
                System.out.println("输入有误，请重新输入");
            }
        }
    }

    private void changeGoodsManufactureData(int ID) {
        System.out.print("请输入修改后的生产日期：");
        String newManufactureData = scanner.nextLine();

        System.out.println("修改后的生产日期为：" + newManufactureData);
        System.out.println("是否修改(Y/N)");

        while(true) {
            System.out.print("->");
            String adminInput = scanner.nextLine();
            if(adminInput.equals("y") || adminInput.equals("Y")) {
                if(databaseManager.changeGoodsManufactureData(ID, newManufactureData)) {
                    System.out.println("修改成功！");
                } else {
                    System.out.println("修改失败！");
                }
                System.out.print("键入Enter键继续");
                scanner.nextLine();
                break;
            } else if (adminInput.equals("n") || adminInput.equals("N")) {
                System.out.println("操作取消");
                System.out.print("键入Enter键继续");
                scanner.nextLine();
                break;
            } else {
                System.out.println("输入有误，请重新输入");
            }
        }
    }

    private void changeGoodsModel(int ID) {
        System.out.print("请输入修改后的型号：");
        String newModel = scanner.nextLine();

        System.out.println("修改后的型号为：" + newModel);
        System.out.println("是否修改(Y/N)");

        while(true) {
            System.out.print("->");
            String adminInput = scanner.nextLine();
            if(adminInput.equals("y") || adminInput.equals("Y")) {
                if(databaseManager.changeGoodsModel(ID, newModel)) {
                    System.out.println("修改成功！");
                } else {
                    System.out.println("修改失败！");
                }
                System.out.print("键入Enter键继续");
                scanner.nextLine();
                break;
            } else if (adminInput.equals("n") || adminInput.equals("N")) {
                System.out.println("操作取消");
                System.out.print("键入Enter键继续");
                scanner.nextLine();
                break;
            } else {
                System.out.println("输入有误，请重新输入");
            }
        }
    }

    private void changeGoodsRestockingPrice(int ID) {
        System.out.print("请输入修改后的进货价：");
        while(!scanner.hasNextDouble()) scanner.next();
        double newRestockingPrice = scanner.nextDouble();
        scanner.nextLine();

        System.out.println("修改后的进货价为：" + newRestockingPrice);
        System.out.println("是否修改(Y/N)");

        while(true) {
            System.out.print("->");
            String adminInput = scanner.nextLine();
            if(adminInput.equals("y") || adminInput.equals("Y")) {
                if(databaseManager.changeGoodsRestockingPrice(ID, newRestockingPrice)) {
                    System.out.println("修改成功！");
                } else {
                    System.out.println("修改失败！");
                }
                System.out.print("键入Enter键继续");
                scanner.nextLine();
                break;
            } else if (adminInput.equals("n") || adminInput.equals("N")) {
                System.out.println("操作取消");
                System.out.print("键入Enter键继续");
                scanner.nextLine();
                break;
            } else {
                System.out.println("输入有误，请重新输入");
            }
        }
    }

    private void changeGoodsRetailPrice(int ID) {
        System.out.print("请输入修改后的零售价格：");
        while(!scanner.hasNextDouble()) scanner.next();
        double newRetailPrice = scanner.nextDouble();
        scanner.nextLine();

        System.out.println("修改后的零售价格为：" + newRetailPrice);
        System.out.println("是否修改(Y/N)");

        while(true) {
            System.out.print("->");
            String adminInput = scanner.nextLine();
            if(adminInput.equals("y") || adminInput.equals("Y")) {
                if(databaseManager.changeGoodsRetailPrice(ID, newRetailPrice)) {
                    System.out.println("修改成功！");
                } else {
                    System.out.println("修改失败！");
                }
                System.out.print("键入Enter键继续");
                scanner.nextLine();
                break;
            } else if (adminInput.equals("n") || adminInput.equals("N")) {
                System.out.println("操作取消");
                System.out.print("键入Enter键继续");
                scanner.nextLine();
                break;
            } else {
                System.out.println("输入有误，请重新输入");
            }
        }
    }

    private void changeGoodsQuantity(int ID) {
        System.out.print("请输入修改后的商品数量：");
        while(!scanner.hasNextInt()) scanner.next();
        int newQuantity = scanner.nextInt();
        scanner.nextLine();

        System.out.println("修改后的商品数量为：" + newQuantity);
        System.out.println("是否修改(Y/N)");

        while(true) {
            System.out.print("->");
            String adminInput = scanner.nextLine();
            if(adminInput.equals("y") || adminInput.equals("Y")) {
                if(databaseManager.changeGoodsQuantity(ID, newQuantity)) {
                    System.out.println("修改成功！");
                } else {
                    System.out.println("修改失败！");
                }
                System.out.print("键入Enter键继续");
                scanner.nextLine();
                break;
            } else if (adminInput.equals("n") || adminInput.equals("N")) {
                System.out.println("操作取消");
                System.out.print("键入Enter键继续");
                scanner.nextLine();
                break;
            } else {
                System.out.println("输入有误，请重新输入");
            }
        }
    }

    private void deleteGoods() {
        System.out.print("请输入要删除商品ID：");
        while(!scanner.hasNextInt()) scanner.next();
        int ID = scanner.nextInt();
        scanner.nextLine();

        if(databaseManager.findGoods(ID)) {
            System.out.println("删除后无法恢复，是否要删除该商品(Y/N)");
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
        boolean runFlag = true;
        while(runFlag) {
            System.out.println("请选择查询方式");
            System.out.println("1、商品名称");
            System.out.println("2、生产厂家");
            System.out.println("3、零售价格");
            System.out.println("4、取消");
            System.out.print("->");
            while(!scanner.hasNextInt()) scanner.next();
            int choice = scanner.nextInt();
            scanner.nextLine();

            switch(choice) {
                case 1 : inquireGoodsInfoByName(); break;
                case 2 : inquireGoodsInfoByManufacturer(); break;
                case 3 : inquireGoodsInfoByRetailPriceO(); break;
                case 4 : runFlag = false; break;
                default: System.out.println("输入错误，请重新输入"); break;
            }
        }
    }

    private void inquireGoodsInfoByName() {
        boolean runFlag = true;
        while(runFlag) {
            System.out.print("请输入商品名称：");
            String name = scanner.nextLine();
            if(databaseManager.inquireGoodsInfoByName(name)) {
                System.out.println("查询成功");
                System.out.print("键入Enter键继续");
                scanner.nextLine();
                runFlag = false;
            } else {
                System.out.println("无相关查询结果，是否重新查询(Y/N)");
                while(true) {
                    System.out.print("->");
                    String adminInput = scanner.nextLine();
                    if(adminInput.equals("y") || adminInput.equals("Y")) {
                        break;
                    } else if(adminInput.equals("n") || adminInput.equals("N")) {
                        runFlag = false;
                        break;
                    } else {
                        System.out.println("输入错误，请重新输入");
                    }
                }
                
            }
            
        }
    }

    private void inquireGoodsInfoByManufacturer() {
        boolean runFlag = true;
        while(runFlag) {
            System.out.print("请输入生产厂家：");
            String manufacturer = scanner.nextLine();
            if(databaseManager.inquireGoodsInfoByManufacturer(manufacturer)) {
                System.out.println("查询成功");
                System.out.print("键入Enter键继续");
                scanner.nextLine();
                runFlag = false;
            } else {
                System.out.println("无相关查询结果，是否重新查询(Y/N)");
                while(true) {
                    System.out.print("->");
                    String adminInput = scanner.nextLine();
                    if(adminInput.equals("y") || adminInput.equals("Y")) {
                        break;
                    } else if(adminInput.equals("n") || adminInput.equals("N")) {
                        runFlag = false;
                        break;
                    } else {
                        System.out.println("输入错误，请重新输入");
                    }
                }
                
            }
            
        }
    }

    private void inquireGoodsInfoByRetailPriceO() {
        boolean runFlag = true;
        while(runFlag) {
            System.out.print("请输入价格：");
            while(!scanner.hasNextDouble()) scanner.next();
            double retailPrice = scanner.nextDouble();
            scanner.nextLine();

            if(databaseManager.inquireGoodsInfoByRetailPriceO(retailPrice)) {
                System.out.println("查询成功");
                System.out.print("键入Enter键继续");
                scanner.nextLine();
                runFlag = false;
            } else {
                System.out.println("无相关查询结果，是否重新查询(Y/N)");
                while(true) {
                    System.out.print("->");
                    String adminInput = scanner.nextLine();
                    if(adminInput.equals("y") || adminInput.equals("Y")) {
                        break;
                    } else if(adminInput.equals("n") || adminInput.equals("N")) {
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
