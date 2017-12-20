import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;

public class RulePrinter {
    //输出一般规则集
    public void print(){
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document document = builder.parse(new File("decisionTree.xml"));
            NodeList list = document.getChildNodes().item(0).getChildNodes();
            printXMLTree(list,"if ");
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }
    //根据决策树打印一般规则集
    private void printXMLTree(NodeList root,String rule){
        for(int i=0;i<root.getLength();i++){
            String tagName;
            String value;
            String content;
            if(root.item(i) instanceof Element){
                tagName = root.item(i).getNodeName();
                value = ((Element) root.item(i)).getAttribute("value");
                content = root.item(i).getTextContent();
                if(content.equals("N")||content.equals("Y")){
                    String newRule;
                    if(rule.equals("if ")){
                        newRule= rule + tagName+" = " +value + " then "+root.item(i).getTextContent();
                    }
                    else {
                        newRule = rule + " and " + tagName + " = " + value+" then "+root.item(i).getTextContent();
                    }
                    System.out.println(newRule);
                }
                else {
                    if(rule.equals("if ")){
                        printXMLTree(root.item(i).getChildNodes(), rule + tagName + " = " + value);
                    }
                    else {
                        printXMLTree(root.item(i).getChildNodes(), rule + " and " + tagName + " = " + value);
                    }
                }
            }
        }
    }
}
