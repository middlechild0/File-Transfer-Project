package filesharing;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JTextField;
import javax.swing.JLabel;
import java.awt.Font;
import javax.swing.JButton;

public class ClientSide {

	private JFrame frame;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					ClientSide window = new ClientSide();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public ClientSide() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setBounds(100, 100, 450, 300);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		
		JLabel filesender = new JLabel("FILE SENDING");
		filesender.setFont(new Font("Times New Roman", Font.BOLD, 17));
		filesender.setBounds(155, 11, 124, 21);
		frame.getContentPane().add(filesender);
		
		JLabel share = new JLabel("Please select a file to share::");
		share.setFont(new Font("Times New Roman", Font.BOLD, 16));
		share.setBounds(123, 43, 219, 37);
		frame.getContentPane().add(share);
		
		JButton btnsend = new JButton("Send File");
		btnsend.setBounds(61, 113, 101, 37);
		frame.getContentPane().add(btnsend);
		
		JButton btnchoose = new JButton("Choose File:");
		btnchoose.setBounds(262, 113, 94, 37);
		frame.getContentPane().add(btnchoose);
	}
}
