import org.w3c.dom.Document;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;
import java.util.ArrayList;

/**
 * 该类用户加载员工数据
 */
public class DataLoader {
    //load data from file
    public static ArrayList<Employee> loadData(File datafile){
        /*

         */
        ArrayList<Employee> employeeList = new ArrayList<>();//要返回的ArrayList
        try{
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder= factory.newDocumentBuilder();
            Document document = builder.parse(datafile);//将对应的XML文件转化为Document
            NodeList nodeList = document.getElementsByTagName("employee");//找到employee的Tag
            for(int i=0;i<nodeList.getLength();i++){
                Employee e = new Employee();//新建Employee
                //以下都是赋值
                e.setId(Integer.parseInt(document.getElementsByTagName("id").item(i).getFirstChild().getNodeValue()));
                e.setEducationLevel(document.getElementsByTagName("educationLevel").item(i).getFirstChild().getNodeValue().charAt(0));
                e.setSex(document.getElementsByTagName("sex").item(i).getFirstChild().getNodeValue().charAt(0));
                e.setEnglishLevel(Integer.parseInt(document.getElementsByTagName("englishLevel").item(i).getFirstChild().getNodeValue()));
                e.setCharacterType(Integer.parseInt(document.getElementsByTagName("characterType").item(i).getFirstChild().getNodeValue()));
                e.setPostType(Integer.parseInt(document.getElementsByTagName("postType").item(i).getFirstChild().getNodeValue()));
                e.setClassType(document.getElementsByTagName("classType").item(i).getFirstChild().getNodeValue().charAt(0));
                employeeList.add(e);
            }

        }catch (Exception e){
               e.printStackTrace();
        }

        return employeeList;//返回结果

    }

}
