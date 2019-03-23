package Repo;

import Domain.Student;
import Validator.*;
import org.w3c.dom.*;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.IOException;

public class StudentXMLFileRepo extends StudentRepo {
    String fName;
    public StudentXMLFileRepo (Validator<Student> v,String fName){
        super(v);
        this.fName = fName;
        load();
    }

    private void load(){
        try {
            Document docXml = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(fName);
            Element root = docXml.getDocumentElement();
            NodeList list = root.getChildNodes();
            for(int i = 0; i < list.getLength(); i++) {
                Node node = list.item(i);
                if(node.getNodeType() == Element.ELEMENT_NODE) {
                    try {
                        super.save(studentFromElement((Element)node));
                    } catch (ValidationException e) {
                        e.printStackTrace();
                    }
                }
            }

        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void write(){
        try {
            Document docXml = DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument();
            Element root = docXml.createElement("Students");
            docXml.appendChild(root);

            elems.values().forEach(s -> root.appendChild(elementFromStudent(s, docXml)));
            Transformer transXml = TransformerFactory.newInstance().newTransformer();
            transXml.transform(new DOMSource(docXml), new StreamResult(fName));
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        } catch (TransformerConfigurationException e) {
            e.printStackTrace();
        } catch (TransformerException e) {
            e.printStackTrace();
        }

    }

    private Student studentFromElement(Element e){
        Integer id = Integer.valueOf(e.getAttribute("ID"));
        Integer grupa = Integer.valueOf(e.getElementsByTagName("Grupa").item(0).getTextContent());
        String nume = e.getAttribute("Nume");
        String email = e.getElementsByTagName("email").item(0).getTextContent();
        return new Student(id,nume,grupa,email);
    }

    private Element createElement(Document docXml, String tag, String value) {
        Element el = docXml.createElement(tag);
        el.setTextContent(value);
        return el;
    }

    private Element elementFromStudent(Student s,Document docXML){
        Element e = docXML.createElement("Student");
        e.setAttribute("ID",s.getID().toString());
        e.setAttribute("Nume",s.getNume());
        e.appendChild(createElement(docXML,"Grupa",s.getGrupa().toString()));
        e.appendChild(createElement(docXML,"email",s.getEmail()));

        return e;
    }

    @Override
    public Student save(Student s){
        Student ret = super.save(s);
        write();
        return ret;
    }

    @Override
    public Student delete(Integer i){
        Student ret = super.delete(i);
        write();
        return ret;
    }

    @Override
    public Student update(Student s){
        Student ret = super.update(s);
        write();
        return ret;
    }
}
