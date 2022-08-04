import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.opencsv.CSVReader;
import com.opencsv.bean.ColumnPositionMappingStrategy;
import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;
import org.w3c.dom.*;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class Main {
    private static List<Employee> list = new ArrayList<>();
    private static final String[] columnMapping = {"id", "firstName", "lastName", "country", "age"};

    public static void main(String[] args) throws IOException, ParserConfigurationException, SAXException {
        File file = new File("data.json");
        created(file);
        String fileName = "data.csv";
        List<Employee> list1 = parseCSV(columnMapping, fileName);
        String json = listToJson(list1);
        writeString(file, json);

        File fileJson = new File("data2.json");
        created(file);
        List<Employee> list2 = parseXML("data.xml");
        String json2 = listToJson(list2);
        writeString(fileJson, json2);

    }

    public static void created(File file) {
        try {
            boolean created = file.createNewFile();
            if (created) System.out.println("ок");
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    private static List<Employee> parseCSV(String[] mapping, String fileName) throws IOException {
        List<Employee> staff = null;
        try (CSVReader reader = new CSVReader(new FileReader(fileName))) {
            ColumnPositionMappingStrategy<Employee> strategy = new ColumnPositionMappingStrategy<>();
            strategy.setType(Employee.class);
            strategy.setColumnMapping(mapping[0], mapping[1], mapping[2], mapping[3], mapping[4]);
            CsvToBean<Employee> csv = new CsvToBeanBuilder<Employee>(reader)
                    .withMappingStrategy(strategy)
                    .build();
            staff = csv.parse();
            staff.forEach(System.out::println);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return staff;
    }

    private static String listToJson(List<Employee> listAll) {
        Type listType = new TypeToken<List<Employee>>() {
        }.getType();
        GsonBuilder builder = new GsonBuilder();
        Gson gson = builder.setPrettyPrinting().create();
        String gjon = gson.toJson(listAll, listType);
        return gjon;
    }

    public static void writeString(File file, String json) {
        try (FileWriter writer = new FileWriter(file)) {
            writer.write(json);
            writer.flush();
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    private static List<Employee> read(Node node) {
        NodeList nodeList = node.getChildNodes();
        List<Employee> employeeList = new ArrayList<>();
        for (int i = 0; i < nodeList.getLength(); i++) {
            Node node1 = nodeList.item(i);
            if (Node.ELEMENT_NODE == node1.getNodeType()) {
                Element element = (Element) node1;
                long id = Long.parseLong(element.getElementsByTagName(columnMapping[0]).item(0).getTextContent());

                String firstName = element.getElementsByTagName(columnMapping[1]).item(0).getTextContent();
                String lastName = element.getElementsByTagName(columnMapping[2]).item(0).getTextContent();
                String country = element.getElementsByTagName(columnMapping[3]).item(0).getTextContent();
                int age = Integer.parseInt(element.getElementsByTagName(columnMapping[4]).item(0).getTextContent());
                employeeList.add(new Employee(id, firstName, lastName, country, age));
            }
        }
        return employeeList;
    }

    public static List<Employee> parseXML(String filename) throws ParserConfigurationException, IOException, SAXException {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document doc = builder.parse(new File(filename));

        Node root = doc.getDocumentElement();
        return read(root);
    }
}


