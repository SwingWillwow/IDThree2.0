import java.io.File;
import java.util.ArrayList;
import java.util.Scanner;

public class IDThree {
    public static void main(String[] args) {
        File file = new File("data/employeeData.xml");
        //load data from file
        ArrayList<Employee> employeeList = DataLoader.loadData(file);
        runningProgram(employeeList);
        //after operations save data toFile
        DataSaver.saveData(employeeList);
    }

    //主程序
    private static void runningProgram(ArrayList<Employee> employeeList){
        while(true){
            char c='a';
            Scanner scanner = new Scanner(System.in);
            showMenu();
            String inputStr = scanner.next();
            if(inputStr.length()>1){
                System.out.println("error input please input again.");
                continue;
            }
            //get c
            c=inputStr.charAt(0);
            if(c=='#') break;
            switch (c){
                case 'a':
                    employeeList=addEmployee(employeeList);
                    break;
                case 'd':
                    int id=0;
                    id = getIdToDelete(employeeList);
                    employeeList=deleteEmployee(employeeList,id);
                    break;
                case 's':
                    showEmployees(employeeList);
                    break;
                case 'r':
                    new ID3Util().runIDThreeAlgorithm(employeeList);
                    new RulePrinter().print();
                    break;
                default:
                    c='c';//c for continue;
                    break;
            }
            if(c=='c'){
                System.out.println("error input please input again.");
            }
        }
    }

    private static void showMenu() {
        System.out.println("please input a char to decide what to do next:");
        System.out.println("input a(means add) to add a new employee.");
        System.out.println("input d(means delete) to delete an employee form the data source.");
        System.out.println("input s(means show) to show all the employee in the list.");
        System.out.println("input r(means run) to run the program to get the decision-making tree.");
    }


    //add a new Employee to the list
    private static ArrayList<Employee> addEmployee(ArrayList<Employee> employeeList){
        Employee e = new Employee();
        e.setId(getNewId(employeeList));
        e.setEducationLevel(getNewEducationLevel());
        e.setSex(getNewSex());
        e.setEnglishLevel(getNewEnglishLevel());
        e.setCharacterType(getNewCharacterType());
        e.setPostType(getNewPostType());
        e.setClassType(getNewClassType());
        employeeList.add(e);
        return employeeList;
    }
    //下面全是用于新增数据时保证健壮性的一些方法
    private static int getNewId(ArrayList<Employee> employees){
        System.out.println("please input the id of the new Employee.");
        int id = getIntSafe();
        if(isIdExist(employees,id)){
            System.out.println("id exist! please input again");
            id = getNewId(employees);

        }
        return id;
    }
    private static char getNewEducationLevel(){
        System.out.println("please input the education level of the new Employee.");
        char level = getCharSafe();
        if(!isEduValid(level)){
            System.out.println("invalid education level. please input again!");
            level = getNewEducationLevel();
        }
        return level;
    }
    private static boolean isEduValid(char edu){
        return edu == 'M' || edu == 'B' || edu == 'C';
    }
    private static char getNewSex(){
        System.out.println("please input the sex of the new employee.");
        char sex = getCharSafe();
        if(!isSexValid(sex)){
            System.out.println("invalid sex. please input agagin!");
            sex = getNewSex();
        }
        return sex;
    }
    private static boolean isSexValid(char sex){
        return sex=='M'||sex=='F';
    }
    private static int getNewEnglishLevel(){
        System.out.println("please input the english value of the new employee.");
        int level = getIntSafe();
        if(!isEngSafe(level)){
            System.out.println("invalid english level. please input again");
            level = getNewEnglishLevel();
        }
        return level;
    }
    private static boolean isEngSafe(int eng){
        return eng==2||eng==4||eng==6;
    }
    private static int getNewCharacterType(){
        System.out.println("please input the character type of the new employee.");
        int type = getIntSafe();
        if(!isTypeSafe(type)){
            System.out.println("invalid character type. please input again!");
            type = getNewCharacterType();
        }
        return type;
    }
    private static boolean isTypeSafe(int type){
        return type==1||type==2||type==3;
    }
    private static int getNewPostType(){
        System.out.println("please input the post type of the new employee.");
        int type = getIntSafe();
        if(!isTypeSafe(type)){
            System.out.println("invalid character type. please input again!");
            type = getNewPostType();
        }
        return type;
    }
    private static char getNewClassType(){
        System.out.println("please input the class type of the new employee.");
        char type = getCharSafe();
        if(!isClassTypeSafe(type)){
            System.out.println("invalid class type. please input again.");
            type = getNewClassType();
        }
        return type;
    }
    private static boolean isClassTypeSafe(char type){
        return type =='N'||type=='Y';
    }
    private static int getIntSafe(){
        Scanner scanner = new Scanner(System.in);
        String input = scanner.next();
        int ret;
        try {
            ret = Integer.parseInt(input);
        }
        catch (Exception e){
            System.out.println("please input Integer");
            ret = getIntSafe();
        }
        return ret;
    }
    private static char getCharSafe(){
        Scanner scanner = new Scanner(System.in);
        String input = scanner.next();
        char ret;
        if(input.length()!=1){
            System.out.println("please input character.");
            ret=getCharSafe();
        }
        else {
            ret = input.charAt(0);
        }
        return ret;
    }

    //从列表中删除一个数据
    private static ArrayList<Employee> deleteEmployee(ArrayList<Employee> employeeList, int id){
        for(Employee e : employeeList){
            if(e.getId()==id){
                employeeList.remove(e);
                break;
            }
        }
        return employeeList;
    }
    //显示列表中的所有数据
    private static void showEmployees(ArrayList<Employee> employeeList){
        for (Employee anEmployeeList : employeeList) {
            showEmployee(anEmployeeList);
        }
    }
    //显示一个数据
    private static void showEmployee(Employee e){
        System.out.println("Id:"+e.getId());
        System.out.println("EducationLevel:"+e.getEducationLevel());
        System.out.println("Sex:"+e.getSex());
        System.out.println("EnglishLevel:"+e.getEnglishLevel());
        System.out.println("CharacterType:"+e.getCharacterType());
        System.out.println("PostType:"+e.getPostType());
        System.out.println("ClassType:"+e.getClassType());
        System.out.println();
    }
    //获取要删除用户的id
    private static int getIdToDelete(ArrayList<Employee> employees){
        System.out.println("please input id:");
        Scanner scanner = new Scanner(System.in);
        String input = scanner.next();
        int id;
        try {
            id = Integer.parseInt(input);
        }
        catch (Exception e){
            System.out.println("please input Integer");
            id = getIdToDelete(employees);
        }
        if(!isIdExist(employees,id)){
            System.out.println("employee with this id not exist!");
            System.out.println("please input again.");
            id = getIdToDelete(employees);
        }
        return id;

    }
    //判断ID是否存在
    private static boolean isIdExist(ArrayList<Employee> employees, int id){
        for (Employee e: employees){
            if( e.getId()==id) return true;
        }
        return false;
    }
}
