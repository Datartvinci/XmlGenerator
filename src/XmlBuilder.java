import java.io.*;
import java.util.Map;

/**
 * Created by John on 2016/10/20.
 */
public class XmlBuilder {
    XmlBuilder xmlBuilder;
    Node root = null;
    String charSet;
    private String fileName = null;

    public XmlBuilder() {
        charSet = "utf-8";
        xmlBuilder = this;
    }

    public XmlBuilder from(Node root) {
        xmlBuilder.root = root;
        return xmlBuilder;
    }

    public XmlBuilder setCharSet(String charSet) {
        this.charSet = charSet;
        return xmlBuilder;
    }

    public XmlBuilder setFileName(String fileName) {
        this.fileName = fileName;
        return xmlBuilder;
    }

    public void buildXml() {
        if (root == null) return;
        if (fileName == null) fileName = root.getTag() + ".xml";
        if (!fileName.endsWith(".xml")) fileName += ".xml";
        FileOutputStream fileOutputStream = null;
        PrintStream printStream = null;
        try {
            fileOutputStream = new FileOutputStream(fileName);
            printStream = new PrintStream(fileOutputStream);
            StringBuilder sb = new StringBuilder();
            String headLine = "<?xml version=\"1.0\" encoding=\"" + charSet + "\"?>\n";
            sb.append(headLine);
            printXml(root, sb);
            printStream.println(new String(sb.toString().getBytes(), charSet));
            System.out.println(sb.toString());

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } finally {
            if (printStream != null)
                printStream.close();
            if (fileOutputStream != null)
                try {
                    fileOutputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
        }
    }

    private void printXml(Node node, StringBuilder sb) {
        if (node != null) {
            sb.append("<" + node.getTag());
            Map<String, String> map = node.getAttributes();
            if (map != null) {
                map.forEach((key, value) -> sb.append(" " + key + "=\"" + value + "\""));
            }
            sb.append(">");
            if (node.getTextContent() != null) {
                sb.append(node.getTextContent());
            } else if (node.getChildren() != null) {
                sb.append("\n");
                for (Node child : node.getChildren()) {
                    printXml(child, sb);
                }
                sb.append("\n");
            }
            sb.append("</" + node.getTag() + ">");
        }
    }
}
