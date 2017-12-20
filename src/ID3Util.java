import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.util.ArrayList;
import java.util.LinkedList;

public class ID3Util {


    //用于保存数据的内部类
    private class ID3Node{
        private ArrayList<Employee> employees;//当前集合包含的员工数据
        private int parentId;//父节点的id
        private int id;//该节点的ID
        private boolean [] visit = new boolean[5];
        ID3Node(ArrayList<Employee> employees,int parentId,boolean [] visit){
            this.employees = employees;
            this.parentId = parentId;
            this.visit = visit;
        }
        ID3Node(){

        }
        public int getParentId() {
            return parentId;
        }

        public void setParentId(int parentId) {
            this.parentId = parentId;
        }

        public ArrayList<Employee> getEmployees() {
            return employees;
        }

        public void setEmployees(ArrayList<Employee> employees) {
            this.employees = employees;
        }

        public boolean[] getVisit() {
            return visit;
        }

        public void setVisit(boolean[] visit) {
            for(int i=0;i<5;i++){
                this.visit[i]=visit[i];
            }
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }
    }
    public static void main(String[] args) {
//        ID3Util util = new ID3Util();
//        Double d1 = 2.0/5;
//        Double d2 = 3.0/5;
//        Double ans = d1*util.getLog2(d1)+d2*util.getLog2(d2);
//        ans = -ans;
//        DecimalFormat format = new DecimalFormat("0.000");
//        System.out.println(format.format(ans));
//        ID3Util util = new ID3Util();
//        ArrayList<Employee> employees = DataLoader.loadData(new File("employeeData.xml"));
//        int index = util.selectAttr(employees);
//        System.out.println(index);
//        ArrayList<ArrayList<Employee>> list = util.getSubsetWithPostType(employees);
//        while (true){
//
//        }
    }
    //空构造方法
    ID3Util(){

    }

    private DocumentBuilderFactory builderFactory = DocumentBuilderFactory.newInstance();
    private DocumentBuilder builder;
    private Document document;
    private int id;
    public void runIDThreeAlgorithm(ArrayList<Employee> employeeList){
        //初始化xml document
        try {
            builder = builderFactory.newDocumentBuilder();
            document = builder.newDocument();
        }catch (Exception e){
            e.printStackTrace();
        }
        id =1;
        buildRootNode();//创建根节点DecisionTree
        LinkedList<ID3Node> newQueue = new LinkedList<>();
        ID3Node node = new ID3Node(employeeList,1,new boolean[5]);//新增根节点
        node.setId(id++);
        newQueue.offer(node);
        //当队列不为空说明还有不能划分的集合
        while(newQueue.peek()!=null){
            //数据初始化
            ID3Node currentNode = newQueue.poll();//获取当前队首的元素
            ArrayList<Employee> currentSet=currentNode.getEmployees();
            int parentId = currentNode.getParentId();
            boolean [] visit = currentNode.getVisit();
            //判断是否叶节点
            if(isLeafNode(currentSet)){
                buildLeafNode(currentNode.getId(),currentSet);//在XML文档中建立一个叶子结点
                continue;
            }
            //已经没有可用属性
            if(!isMoreAttr(visit)){
                printError();
                return;
            }
            int index = selectAttr(currentSet,visit);
            //System.out.println(index); 测试用
            ArrayList<ArrayList<Employee>> subsets = getSubset(index,currentSet);
            //为各个子集新建一个子节点
            for (ArrayList<Employee> subset : subsets) {
                ID3Node tmpNode = new ID3Node();
                tmpNode.setEmployees(subset);
                tmpNode.setParentId(currentNode.getId());
                tmpNode.setId(id++);
                visit[index]=true;
                tmpNode.setVisit(visit);
                newQueue.offer(tmpNode);
            }
            buildNonLeafNode(subsets,currentNode.getId(),index);//在XML文档中生成一个非叶节点
        }
        printXMLTree();//输出决策树
        printSuccess();//输出成功信息

    }
    //输出树
    private void printXMLTree(){
        File file = new File("decisionTree.xml");
        try {
            TransformerFactory factory = TransformerFactory.newInstance();
            Transformer transformer = factory.newTransformer();
            transformer.setOutputProperty(OutputKeys.INDENT,"yes");
            transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "4");
            transformer.transform(new DOMSource(document),new StreamResult(file));
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }
    //构造根节点
    private void buildRootNode(){
        Element root = document.createElement("decisionTree");
        root.setAttribute("id",Integer.toString(id));
        root.setIdAttribute("id",true);
        document.appendChild(root);
    }
    //构造叶子节点
    private void buildLeafNode(int id,ArrayList<Employee> employees){
        Node node = document.getElementById(Integer.toString(id));
        String classType = Character.toString(employees.get(0).getClassType());
        node.setTextContent(classType);
    }
    //构造非叶节点
    private void buildNonLeafNode(ArrayList<ArrayList<Employee>> subsets,int currentId,int index){
        String tagName = getTagName(index);
        Node node = document.getElementById(Integer.toString(currentId));
        int tmpId = id - subsets.size();
        for(ArrayList<Employee> subset:subsets){
            String value = getValueByIndex(subset.get(0),index);
            Element elem = document.createElement(tagName);
            elem.setAttribute("value",value);
            elem.setAttribute("id",Integer.toString(tmpId++));
            elem.setIdAttribute("id",true);
            node.appendChild(elem);
        }
    }

    private String getValueByIndex(Employee e,int index){
        switch (index){
            case 0:
                return Character.toString(e.getEducationLevel());
            case 1:
                return Character.toString(e.getSex());
            case 2:
                return Integer.toString(e.getEnglishLevel());
            case 3:
                return Integer.toString(e.getCharacterType());
            case 4:
                return Integer.toString(e.getPostType());
        }
        return null;
    }
    //获取节点的Tag
    private String getTagName(int index){
        switch (index){
            case -1:
                return "decisionTree";
            case 0:
                return "educationLevel";
            case 1:
                return "sex";
            case 2:
                return "englishLevel";
            case 3:
                return "characterType";
            case 4:
                return "postType";
            default:
                return "error";
        }
    }
    //打印成功信息
    private void printSuccess(){
        System.out.println("build decision tree successfully.");
        System.out.println();
    }
    //打印错误信息
    private void printError(){
        System.out.println("Error! can not build Decision tree.");
        System.out.println();
    }
    //判断有没有可用属性
    private boolean isMoreAttr(boolean [] visit){
        for(int i=0;i<4;i++){
            if(!visit[i]){
                return true;
            }
        }
        return  false;
    }
    //判断是否叶子节点
    private boolean isLeafNode(ArrayList<Employee> employees){
        char classType = employees.get(0).getClassType();
        for(Employee e:employees){
            if(e.getClassType()!=classType){
                return false;
            }
        }
        return true;
    }
    //选择划分属性
    private int selectAttr(ArrayList<Employee> employeeList,boolean visit[]){
        int index = -2;
        Double minInformationEntropy = 100.00;
        Double tmpInformation;
        for(int i=0;i<5;i++){
            if(visit[i]) continue;
            tmpInformation = countAverageInformation(i,employeeList);
//            System.out.println(tmpInformation);
            if(minInformationEntropy > tmpInformation){
                minInformationEntropy = tmpInformation;
                index = i;
            }
        }
        return index;
    }
    //计算某一属性值的平均信息量
    private Double countAverageInformation(int index,ArrayList<Employee> employeeList){
        double elemNum = employeeList.size();
        Double avgInformation = 0.0;
        ArrayList<ArrayList<Employee>> subsetOfEmList  = new ArrayList<>();//用于保存所有子集
        subsetOfEmList = getSubset(index,employeeList);
        Double [] entropy = new Double[3];
        for(int i=0;i<subsetOfEmList.size();i++){
            entropy[i] = countEntropy(subsetOfEmList.get(i));
//            System.out.println(entropy[i]);
//            System.out.println();
            avgInformation += (subsetOfEmList.get(i).size()/elemNum)*entropy[i];
        }

        return avgInformation;
    }
    //计算子集的熵
    private Double countEntropy(ArrayList<Employee> subset){
        int classY=0,classN=0;
        double elemNum = subset.size();
        for (Employee e : subset) {
            if(e.getClassType()=='N') classN++;
            else if(e.getClassType()=='Y') classY++;
        }
        Double entropy = 0.0;
        if(classY==0||classN==0){
            return entropy;
        }
        entropy+=(classY/elemNum)*getLog2(classY/elemNum);
        entropy+=(classN/elemNum)*getLog2(classN/elemNum);
        entropy= -entropy;
        return entropy;
    }
    //获取子集
    private ArrayList<ArrayList<Employee>> getSubset(int index,ArrayList<Employee> employeeList){
        switch (index){
            case 0:return getSubsetWithEducationLevel(employeeList);
            case 1:return getSubsetWithSex(employeeList);
            case 2:return getSubsetWithEnglishLevel(employeeList);
            case 3:return getSubsetWithCharacterType(employeeList);
            case 4:return getSubsetWithPostType(employeeList);
            default:
                return null;
        }
    }
    //以教育水平划分子集
    private ArrayList<ArrayList<Employee>>  getSubsetWithEducationLevel(ArrayList<Employee> employeeArrayList){
        ArrayList<ArrayList<Employee>> subset = new ArrayList<>();
        ArrayList<Employee> subsetB = new ArrayList<>();
        ArrayList<Employee> subsetC = new ArrayList<>();
        ArrayList<Employee> subsetM = new ArrayList<>();
        for (Employee e : employeeArrayList) {
            switch (e.getEducationLevel()){
                case 'B':
                    subsetB.add(e);
                    break;
                case 'C':
                    subsetC.add(e);
                    break;
                case 'M':
                    subsetM.add(e);
                    break;
                default:
                    break;
            }
        }
        if(subsetB.size()!=0) subset.add(subsetB);
        if(subsetC.size()!=0) subset.add(subsetC);
        if(subsetM.size()!=0) subset.add(subsetM);
        return subset;
    }
    //以性别划分子集
    private ArrayList<ArrayList<Employee>> getSubsetWithSex(ArrayList<Employee> employees){
        ArrayList<ArrayList<Employee>> subset = new ArrayList<>();
        ArrayList<Employee> subsetM = new ArrayList<>();
        ArrayList<Employee> subsetF = new ArrayList<>();
        for (Employee e : employees) {
            switch (e.getSex()){
                case 'M':
                    subsetM.add(e);
                    break;
                case 'F':
                    subsetF.add(e);
                    break;
                default:
                    break;
            }
        }
        if (subsetM.size()!=0) subset.add(subsetM);
        if (subsetF.size()!=0) subset.add(subsetF);
        return subset;
    }
    //以英语水平划分子集
    private ArrayList<ArrayList<Employee>> getSubsetWithEnglishLevel(ArrayList<Employee> employees){
        ArrayList<ArrayList<Employee>> subset = new ArrayList<>();
        ArrayList<Employee> subset2 = new ArrayList<>();
        ArrayList<Employee> subset4 = new ArrayList<>();
        ArrayList<Employee> subset6 = new ArrayList<>();
        for (Employee e : employees){
            switch (e.getEnglishLevel()){
                case 2:
                    subset2.add(e);
                    break;
                case 4:
                    subset4.add(e);
                    break;
                case 6:
                    subset6.add(e);
                    break;
                 default:
                     break;
            }
        }
        if(subset2.size()!=0) subset.add(subset2);
        if(subset4.size()!=0) subset.add(subset4);
        if(subset6.size()!=0) subset.add(subset6);
        return subset;
    }
    //以性格划分子集
    private ArrayList<ArrayList<Employee>> getSubsetWithCharacterType(ArrayList<Employee> employees){
        ArrayList<ArrayList<Employee>> subset = new ArrayList<>();
        ArrayList<Employee> subset1 = new ArrayList<>();
        ArrayList<Employee> subset2 = new ArrayList<>();
        ArrayList<Employee> subset3 = new ArrayList<>();
        for (Employee e : employees) {
            switch (e.getCharacterType()){
                case 1:
                    subset1.add(e);
                    break;
                case 2:
                    subset2.add(e);
                    break;
                case 3:
                    subset3.add(e);
                    break;
                default:
                    break;
            }
        }
        if(subset1.size()!=0)subset.add(subset1);
        if(subset2.size()!=0)subset.add(subset2);
        if(subset3.size()!=0)subset.add(subset3);
        return subset;
    }
    //以岗位划分子集
    private ArrayList<ArrayList<Employee>> getSubsetWithPostType(ArrayList<Employee> employees){
        ArrayList<ArrayList<Employee>> subset = new ArrayList<>();
        ArrayList<Employee> subset1 = new ArrayList<>();
        ArrayList<Employee> subset2 = new ArrayList<>();
        ArrayList<Employee> subset3 = new ArrayList<>();
        for (Employee e : employees) {
            switch (e.getPostType()){
                case 1:
                    subset1.add(e);
                    break;
                case 2:
                    subset2.add(e);
                    break;
                case 3:
                    subset3.add(e);
                    break;
            }
        }
        if(subset1.size()!=0)subset.add(subset1);
        if(subset2.size()!=0)subset.add(subset2);
        if(subset3.size()!=0)subset.add(subset3);
        return subset;
    }
    //获得以2为底的对数
    private Double getLog2(Double value){
        return Math.log(value)/Math.log(2);
    }
}
