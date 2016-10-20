import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * Created by John on 2016/10/19.
 */
public class Node {
    private Node parent = null;
    private List<Node> children = null;
    private String tag;
    private String textContent = null;
    private HashMap<String, String> attributes = null;

    public Node(String tag) {
        this.setTag(tag);
    }

    public List<Node> getChildren() {
        return children;
    }

    public List<Node> getNodesByTag(String tag) {
        if (tag == null) return null;
        List<Node> list = new LinkedList<>();
        if (tag == this.tag) {
            list.add(this);
            return list;
        } else {
            LinkedList<Node> queue = new LinkedList<>();
            getChildren().forEach(queue::add);
            while (!queue.isEmpty()) {
                Node child = queue.poll();
                if (child.getTag().equals(tag))
                    list.add(child);
                if (list.isEmpty() && child.getChildren() != null)
                    child.getChildren().forEach(queue::add);
            }
        }
        return list;

    }

    public void setAttributes(String key, String value) {
        if (getAttributes() == null) attributes = new HashMap<>();
        if (key != null)
            getAttributes().put(key, value);
    }

    public Map<String, String> getAttributes() {
        if (attributes == null) return null;
        return attributes;
    }

    public String getAttribute(String key) {
        if (key == null || getAttributes() == null) return null;
        return getAttributes().get(key);
    }

    public void setTextContent(String textContent) {
        if (getChildren() == null)
            this.textContent = textContent;
        else
            System.err.println("有了子元素就不能设置文字内容了吧");
    }

    public String getTextContent() {
        return textContent;
    }

    public void appendChild(Node child) {
        if (getTextContent() == null) {
            if (getChildren() == null) setChildren(new LinkedList<>());
            getChildren().add(child);
            child.setParent(this);
        } else
            System.err.println("有了文字内容就不能设置子元素了吧");


    }

    public void deleteChild(Node child) {
        if (getChildren() != null || child != null || child.getTag() != null || !child.getTag().equals("")) {
            getNodesByTag(child.getTag()).forEach(node -> node.parent.getChildren().remove(node));
        }
    }
    public void deleteChild(String tag) {
        if (getChildren() != null || tag != null ||  !tag.equals("")) {
            getNodesByTag(tag).forEach(node -> node.parent.getChildren().remove(node));
        }
    }
    public Node getParent() {
        return parent;
    }

    public void setParent(Node parent) {
        this.parent = parent;
    }

    public void setChildren(List<Node> children) {
        this.children = children;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public void printCurrentNode() {
        new XmlBuilder()
                .from(this)
                .setCharSet("utf-8")
                .setFileName(this.getTag() + ".xml")
                .buildXml();
    }


    public void printNode(String tag) {
        if (tag == null || tag.equals("")) return;
        getNodesByTag(tag).forEach(node -> new XmlBuilder()
                .from(node)
                .setCharSet("utf-8")
                .setFileName(node.getTag() + ".xml")
                .buildXml());

    }
    public static  Node addFromXml(String fileName){
        return new XmlParser().readFromXml(fileName);
    }
    public void addChildFromXml(String fileName){
        Node child=new XmlParser().readFromXml(fileName);
        if(child.getTag()==getTag())return;
        List<Node>list=child.getNodesByTag(getTag());
        if(list==null||list.isEmpty()){
            appendChild(child);
        }
    }
}
