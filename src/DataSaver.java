
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.util.ArrayList;

/**
 * 该类用于保存数据
 */
public class DataSaver {
    public static void saveData(ArrayList<Employee> employeeList){
        /*

         */
        try{
            File file = new File("data/employeeData.xml");//指定保存文件的路径
            TransformerFactory factory = TransformerFactory.newInstance();
            Transformer transformer = factory.newTransformer();
            DocumentBuilderFactory builderFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = builderFactory.newDocumentBuilder();
            Document document = builder.newDocument();//完成document的初始化
            Element root = document.createElement("employeeList");//先生成根节点
            //将各条数据不断生成一个新的结点
            for (Employee e : employeeList) {
                Element employee = document.createElement("employee");
                Element id = document.createElement("id");
                id.setTextContent(Integer.toString(e.getId()));
                Element educationLevel = document.createElement("educationLevel");
                educationLevel.setTextContent(Character.toString(e.getEducationLevel()));
                Element sex = document.createElement("sex");
                sex.setTextContent(Character.toString(e.getSex()));
                Element englishLevel = document.createElement("englishLevel");
                englishLevel.setTextContent(Integer.toString(e.getEnglishLevel()));
                Element characterType = document.createElement("characterType");
                characterType.setTextContent(Integer.toString(e.getCharacterType()));
                Element postType = document.createElement("postType");
                postType.setTextContent(Integer.toString(e.getPostType()));
                Element classType = document.createElement("classType");
                classType.setTextContent(Character.toString(e.getClassType()));
                employee.appendChild(id);
                employee.appendChild(educationLevel);
                employee.appendChild(sex);
                employee.appendChild(englishLevel);
                employee.appendChild(characterType);
                employee.appendChild(postType);
                employee.appendChild(classType);
                root.appendChild(employee);
            }
            document.appendChild(root);//root是整个document的子节点
            transformer.setOutputProperty(OutputKeys.INDENT,"yes");//采用缩进
            transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "4");//缩进采用4个空格，即一个tab
            transformer.transform(new DOMSource(document),new StreamResult(file));

        }catch (Exception e){
            e.printStackTrace();
        }

    }
}
