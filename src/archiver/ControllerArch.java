package archiver;

import javafx.application.HostServices;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.FileChooser;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.MalformedURLException;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ControllerArch  {

    String text = readUsingFiles("one.txt");
    @FXML
    private Label fi;

    @FXML
    private Label of;

    @FXML
    private Button f1;

    @FXML
    private Button f2;

    @FXML
    private Label result1;

    @FXML
    private Label result11;

    @FXML
    private Label result2;

    @FXML
    private Label err;

    @FXML
    private Label l;

    List<Integer> codLZW;
    TreeMap<Character, String> codes;
    NodeTree tree;
    byte[] bs;

    public ControllerArch() throws IOException {
    }

    @FXML
    void open(ActionEvent event) {
        FileChooser fc = new FileChooser();
        File file = fc.showOpenDialog(null);
        String m;
        m = file.toURI().toString();
        of.setText(m);
    }

    //кодирование файла
    @FXML
    void encodingFile(ActionEvent event) throws IOException {
        File f = new File(of.getText());
        if (getFileExtension(f.getName()).equals("bmp")) {
            File f1 = new File(f.getName());
            BufferedImage img = ImageIO.read(f1);
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ImageIO.write(img, "bmp", baos);
            bs = baos.toByteArray();

            File outFile0 = new File("one.txt");
            FileWriter fileWriter0 = new FileWriter(outFile0, true);

            String str = new String(bs);
            fileWriter0.write(str);
            fileWriter0.close();
            text = readUsingFiles("one.txt");
        }
        else if (getFileExtension(f.getName()).equals("txt")) {
            text = readUsingFiles(f.getName()); //считывание из файла
        }
        TreeMap<Character, Integer> frequencies = countFrequency(text); //частота каждого символа в тексте
        File source = new File(f.getName());
        File dest = new File("D:\\university\\1\\i.bmp");
        if (dest.exists())
            dest.delete();
        Files.copy(source.toPath(), dest.toPath());
        File outFile0 = new File("D:\\university\\3\\1sem\\Alg\\lab1\\frequency.txt");
        FileWriter fileWriter0 = new FileWriter(outFile0, true);
        for (Map.Entry<Character, Integer> entry : frequencies.entrySet()) {
            fileWriter0.write(entry.getKey() + " " + entry.getValue() + "\n");
        }
        fileWriter0.close();
        ArrayList<NodeTree> codeTreeNodes = new ArrayList<>(); // генерация списка листьев дерева
        for (Character s : frequencies.keySet()) {
            codeTreeNodes.add(new NodeTree(s, frequencies.get(s)));
        }
        ArrayList<NodeTree> nodeTrees = new ArrayList<>();
        for (Map.Entry<Character, Integer> item : frequencies.entrySet()) {
            nodeTrees.add(new NodeTree(item.getKey(), item.getValue(), null, null));
        }
        tree = huffman(codeTreeNodes);
        codes = new TreeMap<>();
        for (Character s : frequencies.keySet()) {
            codes.put(s, tree.getCodeForCharacter(s, ""));
        }
        // Замена сиволов в тексте соответствующими кодами
        ArrayList cd = new ArrayList();
        for (int i = 0; i < text.length(); i++) {
            cd.add(codes.get(text.charAt(i)));
        }
        //запись в файл
        File outFile1 = new File("D:\\university\\3\\1sem\\Alg\\lab1\\ar1.txt");
        FileWriter fileWriter1 = new FileWriter(outFile1, true);
        for (var i : cd) fileWriter1.write(i + " ");
        fileWriter1.close();

        //LZW
        String text1 = readUsingFiles("ar1.txt"); //считывание из файла

        codLZW = codingLZW(text1);

        File outFile2 = new File("D:\\university\\3\\1sem\\Alg\\lab1\\encoding.txt");
        FileWriter fileWriter2 = new FileWriter(outFile2, true);

        for (Integer i : codLZW) fileWriter2.write(i + " ");

        fileWriter2.close();

        String text2 = readUsingFiles("encoding.txt");
        result1.setText("Размер исходного файла: " + text.getBytes().length + " байт");
        result11.setText("Размер сжатого файла: " + text2.getBytes().length + " байт");
    }
    //расшифровка файла
    @FXML
    void decodingFile(ActionEvent event) throws IOException {
        String str = null;
        List<Integer> codLZW1 = new ArrayList<>();
        BufferedReader br = new BufferedReader(new FileReader("encoding.txt"));
        while ((str = br.readLine()) != null) {
            String[] newWords = str.split(" ");
            codLZW1 = new ArrayList<>(codLZW1.size() + newWords.length);
            for (String newWord : newWords) {
                codLZW1.add(Integer.parseInt(newWord));
            }
        }
        br.close();
        String decodLZW = decodingLZW(codLZW1);
        StringBuilder dl = new StringBuilder();
        String[] decodLZW1 = decodLZW.split(" ");
        for (String d : decodLZW1)
            dl.append(d);

        str = null;
        TreeMap<Character, Integer> frequencies = new TreeMap<>();
        BufferedReader br1 = new BufferedReader(new FileReader("frequency.txt"));
        while ((str = br1.readLine()) != null) {
            String[] newWords = str.split(" ");
            for (int i = 0; i < 1; i++) {
                frequencies.put(newWords[i].charAt(0), Integer.parseInt(newWords[i + 1]));
            }
        }
        br1.close();

        ArrayList<NodeTree> codeTreeNodes = new ArrayList<>();
        for (Character s : frequencies.keySet()) {
            codeTreeNodes.add(new NodeTree(s, frequencies.get(s)));
        }

        ArrayList<NodeTree> nodeTrees = new ArrayList<>();
        for (Map.Entry<Character, Integer> item : frequencies.entrySet()) {
            nodeTrees.add(new NodeTree(item.getKey(), item.getValue(), null, null));
        }
        gh();
        tree = huffman(codeTreeNodes);
        codes = new TreeMap<>();
        for (Character s : frequencies.keySet()) {
            codes.put(s, tree.getCodeForCharacter(s, ""));
        }

        ArrayList cd = new ArrayList();
        for (int i = 0; i < text.length(); i++) {
            cd.add(codes.get(text.charAt(i)));
        }

        String decoded = huffmanDecode(dl, tree);
        StringBuilder de = new StringBuilder();
        String[] dd = decoded.split(" ");
        for (String d : dd)
            de.append(d);
        String a = de.toString();

        File outFile = new File("D:\\university\\3\\1sem\\Alg\\lab1\\decoding.txt");
        FileWriter fileWriter = new FileWriter(outFile, true);
        fileWriter.write(a);
        fileWriter.close();
        String decodingText = readUsingFiles("decoding.txt");
        result2.setText("Размер расшифрованного файла: " + decodingText.getBytes().length + " байт");
        File inputFile = new File("decoding.txt");
        FileInputStream fs = new FileInputStream(String.valueOf(Paths.get(inputFile.getAbsolutePath())));
        byte[] buffer = new byte[fs.available()];
        fs.read(buffer, 0, fs.available());
        File outFile2 = new File("i.bmp");

        l.setText("был создан i.bmp");
    }

    @FXML
    void clear(ActionEvent event) throws FileNotFoundException {
        PrintWriter writer = new PrintWriter("ar1.txt");
        writer.print("");
        writer.close();

        PrintWriter writer1 = new PrintWriter("decoding.txt");
        writer1.print("");
        writer1.close();

        PrintWriter writer2 = new PrintWriter("encoding.txt");
        writer2.print("");
        writer2.close();

        PrintWriter writer3 = new PrintWriter("frequency.txt");
        writer3.print("");
        writer3.close();

        PrintWriter writer4 = new PrintWriter("one.txt");
        writer4.print("");
        writer4.close();


        result1.setText("");
        result11.setText("");
        result2.setText("");
        of.setText("");
        err.setText("");
        l.setText("");
    }

    //определение формата файла
    private static String getFileExtension(String fullName) {
        String fileName = new File(fullName).getName();
        int dotIndex = fileName.lastIndexOf('.');
        return (dotIndex == -1) ? "" : fileName.substring(dotIndex + 1);
    }

    //чтение из файла
    private static String readUsingFiles(String fileName) throws IOException {
        return new String(Files.readAllBytes(Paths.get(fileName)));
    }

    //вычисление частоты
    private static TreeMap<Character, Integer> countFrequency(String text) {
        TreeMap<Character, Integer> map = new TreeMap<>();
        for (int i = 0; i < text.length(); i++) {
            Character c = text.charAt(i);
            Integer count = map.get(c);
            map.put(c, count != null ? count + 1 : 1);
        }
        return map;
    }

    //Хаффман кодирование
    private static NodeTree huffman(ArrayList<NodeTree> codeTreeNodes) {
        while (codeTreeNodes.size() > 1) {
            Collections.sort(codeTreeNodes);
            NodeTree left = codeTreeNodes.remove(codeTreeNodes.size() - 1);
            NodeTree right = codeTreeNodes.remove(codeTreeNodes.size() - 1);

            NodeTree parent = new NodeTree(null, right.weight + left.weight, left, right);
            codeTreeNodes.add(parent);
        }
        return codeTreeNodes.get(0);
    }

    //LZW кодирование
    public static List<Integer> codingLZW(String text) {
        // создания словаря
        int dictSize = 256;
        Map<String,Integer> dictionary = new HashMap<>();
        for (int i = 0; i < 256; i++)
            dictionary.put("" + (char)i, i);

        String w = "";
        List<Integer> result = new ArrayList<>();
        for (char c : text.toCharArray()) {
            String wc = w + c;
            if (dictionary.containsKey(wc)) //если есть заданый ключ
                w = wc;
            else {
                result.add(dictionary.get(w));
                dictionary.put(wc, dictSize++);
                w = "" + c;
            }
        }
        if (!w.equals(""))
            result.add(dictionary.get(w));

        return result;
    }

    //LZW декодирование
    public static String decodingLZW(List<Integer> cod) {
        int dictSize = 256;
        Map<Integer,String> dictionary = new HashMap<>();
        for (int i = 0; i < 256; i++)
            dictionary.put(i, "" + (char)i);

        String w = "" + (char)(int)cod.remove(0);
        StringBuilder result = new StringBuilder(w);
        for (int k : cod) {
            String entry;
            if (dictionary.containsKey(k))
                entry = dictionary.get(k);
            else if (k == dictSize)
                entry = w + w.charAt(0);
            else
                throw new IllegalArgumentException("Bad compressed k: " + k); //был передан не правильный аргумент

            result.append(entry);
            dictionary.put(dictSize++, w + entry.charAt(0));
            w = entry;
        }
        return result.toString();
    }

    //Хаффман декодирование
    private static String huffmanDecode(StringBuilder encoded, NodeTree tree) {
        StringBuilder decoded = new StringBuilder(); // для накапливание расшифрованных данных

        NodeTree node = tree; //хранение текущих вызовов для передвижения по дереву (изначально = корневому узлу)
        for (int i = 0; i < encoded.length(); i++) {
            node = encoded.charAt(i) == '0' ? node.left : node.right; // если ноль - налево, иначе направо
            if (node.symbol != null) { //когда дошли до символа
                decoded.append(node.symbol);
                node = tree;
            }
        }
        return decoded.toString();
    }

    //кнопки для открытия файла

    public void gh1(HostServices hostServices){
        f1.setOnAction(actionEvent -> {
            File fa = new File("encoding.txt");
            try {
                hostServices.showDocument(fa.toURI().toURL().toExternalForm());
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
        });

    }
    public void gh() throws IOException {
        File source = new File("D:\\university\\1\\i.bmp");
        File dest = new File("i.bmp");
        if (dest.exists())
            dest.delete();

        Files.copy(source.toPath(), dest.toPath());

    }
    public void gh2(HostServices hostServices) {
        f2.setOnAction(actionEvent -> {
            File fa = new File("decoding.txt");
            try {
                hostServices.showDocument(fa.toURI().toURL().toExternalForm());
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
        });
    }


}

