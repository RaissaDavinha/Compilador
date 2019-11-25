import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import javax.swing.JFrame;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JMenuBar;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.ScrollPaneConstants;
import javax.swing.SwingUtilities;
import javax.swing.border.EmptyBorder;
import javax.swing.filechooser.FileSystemView;
import javax.swing.JList;
import javax.swing.JScrollPane;
import java.awt.Font;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.awt.BorderLayout;

@SuppressWarnings("serial")
public class IDE extends JFrame{
	private JPanel contentPane;
	private JFrame frame;
	String archive = null;
	private JTextArea textArea = new JTextArea();
	private JList<Integer> list = new JList<Integer>();
	private JMenuBar menuBar = new JMenuBar();
	private JMenuItem mntmNovo = new JMenuItem("Novo");

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					IDE frame = new IDE();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public IDE() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		setResizable(false);
		setTitle("IDE");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 655, 600);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		menuBar.setBounds(12, 5, 65, 21);
		contentPane.add(menuBar);
		
		JMenu mnSalvar = new JMenu("Menu");
		menuBar.add(mnSalvar);
		
		mnSalvar.add(mntmNovo);
		botaoNovo btnNovo = new botaoNovo();
		mntmNovo.addActionListener(btnNovo);
		
		JMenuItem mntmSalvar = new JMenuItem("Salvar");
		mnSalvar.add(mntmSalvar);
		botaoSalvar btnSalvar = new botaoSalvar();
		mntmSalvar.addActionListener(btnSalvar);
		
		JMenuItem mntmImportar = new JMenuItem("Importar");
		mnSalvar.add(mntmImportar);
		botaoImportar btnImportar = new botaoImportar();
		mntmImportar.addActionListener(btnImportar);
		
		JMenuItem mntmSair = new JMenuItem("Sair");
		mnSalvar.add(mntmSair);
		botaoSair btnSair = new botaoSair();
		mntmSair.addActionListener(btnSair);
		
		JButton btnCompilar = new JButton("Compilar");
		btnCompilar.setBounds(542, 5, 101, 21);
		contentPane.add(btnCompilar);
		botaoCompilar botaoCompilar = new botaoCompilar();
		btnCompilar.addActionListener(botaoCompilar);
		
		JPanel pContainer = new JPanel();
		pContainer.setLayout(new BorderLayout(0, 0));
		
		list.setFont(new Font("Dialog", Font.PLAIN, 12));
		pContainer.add(list, BorderLayout.WEST);
		
		textArea.setFont(new Font("Dialog", Font.PLAIN, 14));
		pContainer.add(textArea, BorderLayout.CENTER);
		
		JScrollPane scroll = new JScrollPane(pContainer);
	    scroll.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
	    scroll.setBounds(12, 33, 631, 504);
	    contentPane.add(scroll);
	}
	
	private class botaoNovo implements ActionListener {
		public void actionPerformed(ActionEvent arg0) {
			
		}
	}
	
	private class botaoSalvar implements ActionListener {
		public void actionPerformed(ActionEvent arg0) {
			JFileChooser j = new JFileChooser(FileSystemView.getFileSystemView().getHomeDirectory()); 
            int r = j.showSaveDialog(null); 
  
            if (r == JFileChooser.APPROVE_OPTION){ 
                //show success
            }
		}
	}
	
	private class botaoImportar implements ActionListener {
		public void actionPerformed(ActionEvent arg0) {
			DefaultListModel<Integer> model = new DefaultListModel<Integer>();
			JFileChooser j = new JFileChooser("oi.txt");  
			 int r = j.showOpenDialog(null);
			 
			 if (r == JFileChooser.APPROVE_OPTION) 
	            {
	                archive = j.getSelectedFile().getAbsolutePath();
	                System.out.println(archive);
	                FileReader fileReader;
					try {
						fileReader = new FileReader(archive);
						BufferedReader reader = new BufferedReader(fileReader);
		                String fileContent = "";
		                int fileContentIndex = 0;
		        		String auxContent = reader.readLine();
		        		
		        		while (auxContent != null) {
		        			fileContent += auxContent;
		        			fileContent += '\n';
		        			auxContent = reader.readLine();
		        			model.addElement(fileContentIndex);
		        			list.setModel(model);
		        			fileContentIndex++;
		        		}
		        		fileReader.close();
		        		textArea.setText(fileContent);
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
	            } 
		}
	}
	
	private class botaoSair implements ActionListener {
		public void actionPerformed(ActionEvent arg0) {
			System.exit(EXIT_ON_CLOSE);
		}
	}
	
	private class botaoCompilar implements ActionListener {
		public void actionPerformed(ActionEvent arg0) {
			if(archive != null) {
				try {
					SintaticMain.sintaticMain(archive);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (LexicoException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (SintaticoException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			} //mostrar erro caso contrario 
		}
	}
}