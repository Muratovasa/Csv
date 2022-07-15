import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;
import com.opencsv.bean.ColumnPositionMappingStrategy;
import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;
import com.opencsv.exceptions.CsvValidationException;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.swing.text.AttributeSet;
import javax.swing.text.Element;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Main {
    public static List<Employee> list=new ArrayList<>();
    public static void main(String[] args) throws IOException, ParserConfigurationException, SAXException {
        String[] columnMapping = {"id","firstName","lastName","country","age"};
       String[] name1="1,John,Smith,USA,25".split(",");
       String[] name2="2,Inav,Petrov,RU,23".split(",");
       File file=new File("data.csv");
       writeAdd(name1,file);
       writeAdd(name2,file);
       String fileName="data.csv";
        List<Employee> list1=parseCSV(columnMapping, fileName);

        List<Employee> list2 = parseXML("data.xml");

    }

    private static void writeAdd(String[] names, File file) throws IOException{
        try(CSVWriter writer=new CSVWriter(new FileWriter(file,true))) {
            writer.writeNext(names);
        } catch (IOException e){
            e.printStackTrace();
        }
    }

    private static List<Employee> parseCSV(String[] columnMapping, String fileName)throws IOException{
        try(CSVReader reader=new CSVReader(new FileReader(fileName))) {
            ColumnPositionMappingStrategy<Employee> strategy=new ColumnPositionMappingStrategy<>();
            strategy.setType(Employee.class);
            strategy.setColumnMapping(columnMapping);
            CsvToBean<Employee> csv =new CsvToBeanBuilder<Employee>(reader)
                    .withMappingStrategy(strategy)
                    .build();
            List<Employee> staff=csv.parse();
            staff.forEach(System.out::println);
        } catch (IOException e){
            e.printStackTrace();
        }
        return list;
    }
/*
private static String  listToJson(List<Employee> listAll){
    Type listType = new TypeToken<List<Employee>>() {}.getType();
    GsonBuilder builder=new GsonBuilder();
    Gson gson= builder.create();
    String gjon= gson.toJson(listAll,listType);
    return gjon;
}
*/
private static List<Employee> parseXML(String list2) throws IOException, SAXException, ParserConfigurationException {
    DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
    DocumentBuilder builder= factory.newDocumentBuilder();
    Document doc= builder.parse(new File("data.xml"));

    Node root= doc.getDocumentElement();
    System.out.println(root.getNodeName());
    read(root);
return list;
}

private static void read(Node node){
    NodeList nodeList=node.getChildNodes();
    for (int i=0;i<nodeList.getLength(); i++){
        Node node1=nodeList.item(i);
        if (Node.ELEMENT_NODE==node1.getNodeType()){
            System.out.println("текущий узел"+node1.getNodeName());
            Element element=(Element) node1;
            NamedNodeMap map= element.getAttributes();
            for (int a=0;a<map.getLength();a++){
                String attName=map.item(a).getNodeName();
                String attValue=map.item(a).getNodeValue();
                System.out.println("атрибут: "+attName+" значение: "+attValue);
            }
            read(node1);
        }
    }
}
}
