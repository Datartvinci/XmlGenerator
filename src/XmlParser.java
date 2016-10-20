import org.w3c.dom.Document;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.*;
import java.util.Stack;

/**
 * Created by John on 2016/10/20.
 */
public class XmlParser {
    XmlParser xmlParser;

    public XmlParser() {
        xmlParser = this;
    }


    public Node readFromXml(String fileName) {
        Node root = null;
        if (fileName == null || !fileName.endsWith(".xml")) return null;
        BufferedReader bufferedReader = null;
        FileInputStream fileInputStream = null;
        try {
            fileInputStream = new FileInputStream(fileName);
            bufferedReader = new BufferedReader(new InputStreamReader(new FileInputStream(fileName)));
            String headLine = bufferedReader.readLine();
            int encodeStart=headLine.indexOf("encoding=\"")+10;
            int encodeEnd=headLine.indexOf("\"",encodeStart);
            String charSet=headLine.substring(encodeStart,encodeEnd);
            bufferedReader.close();
            bufferedReader=new BufferedReader(new InputStreamReader(new FileInputStream(fileName),charSet));
            StringBuilder sb = new StringBuilder();
            int length = 0;
            byte[] buf = new byte[1024];  //建立缓存数组，缓存数组的大小一般都是1024的整数倍，理论上越大效率越好
            while ((length = fileInputStream.read(buf)) != -1) {
                String str = new String(buf, 0, length);
                sb.append(str);
            }
            String str = sb.toString();
            Stack<Node> stack = new Stack<>();
            if (!str.contains("<?") || !str.contains("?>")) return null;
            for (int i = str.indexOf("?>") + 3; i != -1 && i < str.length(); ++i) {
                int start = str.indexOf("<", i) + 1;
                int end = str.indexOf(">", i);
                i = end;
                if (i == -1) break;
                if (start < end) {
                    String nodeTag = str.substring(start, end);
                    if (!nodeTag.contains("/")) {
                        String[] strings = nodeTag.split(" ");
                        nodeTag = strings[0];
                        Node node = new Node(nodeTag);
                        for (int j = 1; j < strings.length; ++j) {
                            String[] pair = strings[1].split("=");
                            node.setAttributes(pair[0], pair[1].substring(1, pair[1].length() - 1));
                        }

                        if (!stack.isEmpty()) {
                            stack.peek().appendChild(node);
                        }
                        stack.push(node);
                        int nextStart = str.indexOf("</", end + 1);
                        int nextEnd = str.indexOf(">", nextStart);
                        if (nextEnd > nextStart || nextEnd != -1) {
                            if (str.substring(nextStart + 2, nextEnd).equals(nodeTag)) {
                                node.setTextContent(str.substring(end + 1, nextStart));
                            }
                        }
                        System.out.println(nodeTag);
                    } else {
                        if (!stack.isEmpty())
                            root = stack.pop();
                    }

                }
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        finally {
            if(bufferedReader!=null) try {
                bufferedReader.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            if(fileInputStream!=null) try {
                fileInputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
        return root;
    }
}
