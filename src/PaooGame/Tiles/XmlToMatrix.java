package PaooGame.Tiles;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import java.io.File;
///Clasa XmlToMatrix este responsabilă pentru citirea unui fișier XML care conține date despre un layer de tile-uri și transformarea acestora într-o matrice de ID-uri.
/// Această matrice poate fi utilizată ulterior pentru a construi o hartă a jocului, unde fiecare ID corespunde unui tip specific de tile.
/// Aceasta clasa este pentru usurarea editarii hartilor ,deoarece am realizat-o intr-un editor grafic special numit Tiled.
public class XmlToMatrix {

    public static int [][] GetIdMatrix(File xmlFile,int height, int width) {
        try {
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(xmlFile);
            doc.getDocumentElement().normalize();

            Node layerNode = doc.getElementsByTagName("layer").item(0); // Get the first layer
            if (layerNode.getNodeType() == Node.ELEMENT_NODE) {
                Element layerElement = (Element) layerNode;
                String csvData = layerElement.getElementsByTagName("data").item(0).getTextContent();
                String[] rows = csvData.split("\n");
                int[][] matrix = new int[rows.length][];
                for (int i = 0; i < rows.length; i++) {
                    String[] elements = rows[i].split(",");
                    matrix[i] = new int[elements.length];
                    for (int j = 0; j < elements.length; j++) {
                        String element = elements[j].trim(); // Trim whitespace
                        if (!element.isEmpty()) { // Check if the string is not empty
                            try {
                                matrix[i][j] = Integer.parseInt(element);
                            } catch (NumberFormatException e) {
                                System.err.println("Error parsing element: " + element);
                                matrix[i][j] = 0; // Or handle the error as needed
                            }
                        } else {
                            matrix[i][j] = 0; // Handle empty string case
                        }
                    }
                }
                int[][] matRet = new int[height][width];
                //int[][] matRet2 = new int[34][70];
                for(int i = 0; i <height; i++)
                {
                    for(int j = 0; j < width; j++)
                    {
                        matRet[i][j] = matrix[i+1][j];
                    }
                }
                return matRet;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}