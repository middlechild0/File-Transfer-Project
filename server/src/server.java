import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.io.DataInputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class server {
    static ArrayList<my_files> myFiles = new ArrayList<>();

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame jFrame = new JFrame("Group 4 server");
            jFrame.setSize(400, 400);
            jFrame.setLayout(new BoxLayout(jFrame.getContentPane(), BoxLayout.Y_AXIS));
            jFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

            JPanel jPanel = new JPanel();
            jPanel.setLayout(new BoxLayout(jPanel, BoxLayout.Y_AXIS));

            JScrollPane jScrollPane = new JScrollPane(jPanel);
            jScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);

            JLabel jlTitle = new JLabel("Group 4 File Receiver");
            jlTitle.setFont(new Font("Arial", Font.BOLD, 25));
            jlTitle.setBorder(new EmptyBorder(20, 0, 10, 0));
            jlTitle.setAlignmentX(Component.CENTER_ALIGNMENT);

            jFrame.add(jlTitle);
            jFrame.add(jScrollPane);
            jFrame.setVisible(true);


            ServerSocket serverSocket = new ServerSocket(1234);

            while (true) {

                    try {

                        Socket socket = serverSocket.accept();

                        DataInputStream dataInputStream = new DataInputStream(socket.getInputStream());

                        int fileNameLength = dataInputStream.readInt();

                        if(fileNameLength > 0) {
                            byte[] fileNameBytes = new byte[fileNameLength];
                            dataInputStream.readFully(fileNameBytes, 0, fileNameBytes.length);
                            String fileName = new String(fileNameBytes);

                            int fileContentLength = dataInputStream.readInt();

                            if (fileContentLength > 0){
                                byte[] fileContentBytes = new byte[fileContentLength];
                                dataInputStream.readFully(fileContentBytes,0,fileContentLength);

                                JPanel jpFileRow = new JPanel();
                                jpFileRow.setLayout(new BoxLayout(jpFileRow, BoxLayout.Y_AXIS));

                                JLabel jlFileName = new JLabel(fileName);
                                jlFileName.setFont(new Font("Arial",Font.BOLD,20));
                                jlFileName.setBorder(new EmptyBorder( 10, 0, 0));

                                if (getFileExtension(fileName).equalsIgnoreCase("txt")){
                                        jpFileRow.setName(String.valueOf(field));
                                    }

                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
            }

    }


            public static String getFileExtension (string filename) {
                int lastDotIndex = filename.lastIndexOf(".");

                if (lastDotIndex >= 0 && lastDotIndex < filename.length() - 1) {
                    return filename.substring(lastDotIndex + 1);
                } else {
                    return "No extension found";
                }
            }
;