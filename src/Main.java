/**
 * Created by John on 2016/10/20.
 */
public class Main {
    public static void main(String[] args) {
        Node root = new Node("CategoryList");
        Node child = new Node("Category");
        child.setAttributes("ID", "01");
        Node child1 = new Node("MainCategory");
        child1.setTextContent("XML");
        Node child2 = new Node("Description");
        child2.setTextContent("This is a list my XML articles.");
        Node child3 = new Node("Active");
        child3.setTextContent("true");
        root.appendChild(child);
        child.appendChild(child1);
        child.appendChild(child2);
        child.appendChild(child3);

        //      root.deleteChild(child3);
        //      root.printCurrentNode();
        //    new XmlBuilder().from(root).setCharSet("utf-8").setFileName("my").buildXml();
        Node nodeFromXml = Node.addFromXml("my.xml");
        nodeFromXml.printCurrentNode();

        Node rootNode=new Node("root");
        rootNode.addChildFromXml("my.xml");
        rootNode.printCurrentNode();
    }
}
