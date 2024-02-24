
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.*;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.concurrent.locks.StampedLock;

public class server {

    static ArrayList<my_files> myFiles = new ArrayList<>();

    private static String getFileExtension(String filename) {
        my_files dummyFile = new my_files(0, filename, null, null);
        return dummyFile.getFileExtension();
    }

    public static void main(String[] args) {
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

        try (ServerSocket serverSocket = new ServerSocket(1234)) {
            while (true) {
                Socket socket = serverSocket.accept();
                handleClient(socket, jPanel);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static int fileId = 0; // Add a global variable to keep track of fileId

    private static void handleClient(Socket socket, JPanel jPanel) {
        try (DataInputStream dataInputStream = new DataInputStream(socket.getInputStream())) {
            int fileNameLength = dataInputStream.readInt();

            if (fileNameLength > 0) {
                byte[] fileNameBytes = new byte[fileNameLength];
                dataInputStream.readFully(fileNameBytes, 0, fileNameBytes.length);
                String fileName = new String(fileNameBytes);

                int fileContentLength = dataInputStream.readInt();

                if (fileContentLength > 0) {
                    byte[] fileContentBytes = new byte[fileContentLength];
                    dataInputStream.readFully(fileContentBytes, 0, fileContentLength);

                    JPanel jpFileRow = new JPanel();
                    jpFileRow.setLayout(new BoxLayout(jpFileRow, BoxLayout.Y_AXIS));

                    JLabel jlFileName = new JLabel(fileName);
                    jlFileName.setFont(new Font("Arial", Font.BOLD, 20));
                    jlFileName.setBorder(new EmptyBorder(10, 0, 0, 0));

                    if (getFileExtension(fileName).equalsIgnoreCase("txt")) {
                        jpFileRow.setName(String.valueOf(fileId));
                        jpFileRow.addMouseListener(getMyMouseListener());

                        jpFileRow.add(jlFileName);
                        jPanel.add(jpFileRow);
                        jPanel.validate();
                    } else {
                        // Increment fileId for non-txt files
                        jpFileRow.setName(String.valueOf(fileId));
                        jpFileRow.addMouseListener(getMyMouseListener());

                        jpFileRow.add(jlFileName);
                        jPanel.add(jpFileRow);
                        jPanel.validate();
                    }

                    myFiles.add(new my_files(fileId, fileName, fileContentBytes, getFileExtension(fileName)));
                    fileId++;
                }
            }
        } catch (IOException error) {
            error.printStackTrace();
        }
    }


    public static MouseListener getMyMouseListener() {
        return new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                JPanel jPanel = (JPanel) e.getSource();

                int fileId = Integer.parseInt(jPanel.getName());

                for (my_files myFile : myFiles) {
                    if (myFile.getId() == fileId) {
                        JFrame jfPreview = createFrame(myFile.getName(), myFile.getData(), myFile.getFileExtension());
                        jfPreview.setVisible(true);
                    }
                }
            }
        };
    }


    public static JFrame createFrame(String fileName, byte[] fileData, String fileExtension) {
        JFrame jFrame = new JFrame("Group 4 File Downloader");
        JFrame jFrame1 = jFrame;
        jFrame1.setSize(400, 400);

        JPanel jPanel = new JPanel();
        jPanel.setLayout(new BoxLayout(jPanel, BoxLayout.Y_AXIS));

        JLabel jTitle = new JLabel("Group 4 File Downloader");
        jTitle.setAlignmentX(Component.CENTER_ALIGNMENT);
        jTitle.setFont(new Font("Arial", Font.BOLD, 25));
        jTitle.setBorder(new EmptyBorder(20, 0, 10, 0));

        JLabel jlPrompt = new JLabel("Are you sure you want to download " + fileName);
        jlPrompt.setFont(new Font("Arial", Font.BOLD, 20));
        jlPrompt.setBorder(new EmptyBorder(20, 0, 10, 0));
        jlPrompt.setAlignmentX(Component.CENTER_ALIGNMENT);

        JButton jbYes = new JButton("Yes");
        jbYes.setPreferredSize(new Dimension(150, 75));
        jbYes.setFont(new Font("Arial", Font.BOLD, 20));

        JButton jbNo = new JButton("No");
        jbNo.setPreferredSize(new Dimension(150, 75));
        jbNo.setFont(new Font("Arial", Font.BOLD, 20));

        JLabel jlFileContent = new JLabel();
        jlFileContent.setAlignmentX(Component.CENTER_ALIGNMENT);

        JPanel jpButtons = new JPanel();
        jpButtons.setBorder(new EmptyBorder(20, 0, 10, 0));
        jpButtons.add(jbYes);
        jpButtons.add(jbNo);

        if (fileExtension.equalsIgnoreCase("txt")) {
            jlFileContent.setText("<html>" + new String(fileData) + "</html>");
        } else {
            jlFileContent.setIcon(new ImageIcon(fileData));
        }

        jbYes.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                File fileDownload = new File(fileName);

                try {
                    FileOutputStream fileOutputStream = new FileOutputStream(fileDownload);

                    fileOutputStream.write(fileData);
                    fileOutputStream.close();

                    jFrame1.dispose();
                } catch (IOException error) {
                    error.printStackTrace();
                }
            }
        });

        jbNo.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                jFrame1.dispose();
            }
        });

        jPanel.add(jTitle);
        jPanel.add(jlPrompt);
        jPanel.add(jlFileContent);
        jPanel.add(jpButtons);

        jFrame1.add(jPanel);

        return jFrame1;
    }
}
