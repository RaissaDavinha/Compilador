import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import javax.swing.JFrame;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JMenuBar;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.ScrollPaneConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.filechooser.FileSystemView;
import javax.swing.JList;
import javax.swing.JScrollPane;
import java.awt.Font;
import java.awt.BorderLayout;
import javax.swing.JTextPane;
import javax.swing.JProgressBar;
import javax.swing.JLabel;
import java.awt.Color;
import javax.swing.SwingConstants;
import javax.swing.AbstractListModel;
import javax.swing.border.LineBorder;
import javax.swing.ListSelectionModel;

@SuppressWarnings("serial")
public class IDE extends JFrame{
	private JPanel contentPane;
	String archive = null;
	private JTextArea textArea = new JTextArea();
	private JList<Integer> list = new JList<Integer>();
	private JMenuBar menuBar = new JMenuBar();
	private JMenuItem mntmNovo = new JMenuItem("Novo");
	private static JTextPane lblConsole = new JTextPane();
	private String actualArchive = FileSystemView.getFileSystemView().getHomeDirectory().toString();
	private int index = 0;
	DefaultListModel<Integer> model = new DefaultListModel<Integer>();

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
		
		//menu
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
		list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		list.setBorder(new LineBorder(new Color(0, 0, 0)));
		
		//linhas do codigo
		list.setFont(new Font("Dialog", Font.BOLD, 12));
		pContainer.add(list, BorderLayout.WEST);
		model.addElement(index);
		list.setModel(model);
		
		//area de escrever o codigo
		textArea.addKeyListener((KeyListener) new KeyListener() {  
			@Override
			public void keyPressed(KeyEvent e) {
	            System.out.println("test");
	            if(e.getKeyCode() == KeyEvent.VK_ENTER) {
	            	index++;
	            	model.addElement(index);
	       			list.setModel(model);
	                System.out.println("enter is pressed");
	            }
			}
			
			@Override
			public void keyReleased(KeyEvent arg0) {}

			@Override
			public void keyTyped(KeyEvent arg0) {}
		});
		textArea.setFont(new Font("Dialog", Font.PLAIN, 15));
		pContainer.add(textArea, BorderLayout.CENTER);
		textArea.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
		
		JScrollPane scroll = new JScrollPane(pContainer);
	    scroll.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
	    scroll.setBounds(12, 33, 631, 353);
	    contentPane.add(scroll);
	    
	    //console
	    JScrollPane scrollPane = new JScrollPane();
	    scrollPane.setBounds(12, 398, 631, 160);
	    contentPane.add(scrollPane);
	    lblConsole.setText("Console");
	    lblConsole.setToolTipText("");
	    lblConsole.setForeground(new Color(255, 0, 0));
	    lblConsole.setFont(new Font("Arial", Font.BOLD, 12));
	    scrollPane.setViewportView(lblConsole);
	}
	
	private class botaoNovo implements ActionListener {
		public void actionPerformed(ActionEvent arg0) {
			JFileChooser j = new JFileChooser(FileSystemView.getFileSystemView().getHomeDirectory());
			int r = j.showOpenDialog(null);
			 
			 if (r == JFileChooser.APPROVE_OPTION) {
				 if(!j.getSelectedFile().exists()) {
					 createFile(j.getSelectedFile().getAbsolutePath());
					 actualArchive = j.getSelectedFile().getAbsolutePath();
					 JOptionPane.showMessageDialog(contentPane, "Arquivo criado com sucesso!");
					 showFile(j.getSelectedFile().getAbsolutePath());
				 }
			 } else {
				 //arquivo ja existe
				 JOptionPane.showMessageDialog(contentPane, "Arquivo com esse nome ja existente");
			 }
			
		}
	}
	
	private class botaoSalvar implements ActionListener {
		public void actionPerformed(ActionEvent arg0) {
			JFileChooser j = new JFileChooser(FileSystemView.getFileSystemView().getHomeDirectory());
			if (actualArchive != null) {
				j = new JFileChooser(actualArchive);
			}
            int r = j.showSaveDialog(null);
            if (r == JFileChooser.APPROVE_OPTION){ 
            	actualArchive = j.getSelectedFile().getAbsolutePath();
            	if(!j.getSelectedFile().exists()) {
            		createFile(actualArchive);
				} 
            	PrintWriter writer;
				try {
					writer = new PrintWriter(actualArchive);
					writer.println(textArea.getText());
					writer.close();
				} catch (FileNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
                //show success
            	JOptionPane.showMessageDialog(contentPane, "Arquivo salvo com sucesso!");
            } else {
            	JOptionPane.showMessageDialog(null, "Algum erro ocorreu ao tentar salvar", "Erro", JOptionPane.ERROR_MESSAGE);
            }
		}
	}
	
	private class botaoImportar implements ActionListener {
		public void actionPerformed(ActionEvent arg0) {
			JFileChooser j = new JFileChooser(FileSystemView.getFileSystemView().getHomeDirectory());
			if (actualArchive != null) {
				j = new JFileChooser(actualArchive);
			}
			int r = j.showOpenDialog(null);
			 
			if (r == JFileChooser.APPROVE_OPTION) {
				actualArchive = j.getSelectedFile().getAbsolutePath();
				
				if(j.getSelectedFile().exists()) {
					showFile(actualArchive);
				} else {
					createFile(actualArchive);
					showFile(actualArchive);
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
			if(actualArchive != null) {
				try {
					PrintWriter writer;
					writer = new PrintWriter(actualArchive);
					writer.println(textArea.getText());
					writer.close();
					SintaticMain.sintaticMain(actualArchive);
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
			} else {
				//mostrar erro caso contrario 
				JOptionPane.showMessageDialog(null, "Arquivo nao encontrado ou vazio", "Erro", JOptionPane.ERROR_MESSAGE);
				
			}
		}
	}
	
	private void showFile(String archive) {
        System.out.println(archive);
        list.setModel(model);
        FileReader fileReader;
		try {
			fileReader = new FileReader(archive);
			BufferedReader reader = new BufferedReader(fileReader);
            String fileContent = "";
            int fileContentIndex = 0;
       		String auxContent = reader.readLine();
       		model.clear();
       		
       		while (auxContent != null) {
       			fileContent += auxContent;
       			fileContent += '\n';
       			auxContent = reader.readLine();
       			model.addElement(fileContentIndex);
       			list.setModel(model);
       			fileContentIndex++;
       		}
       		index = fileContentIndex;
       		fileReader.close();
       		textArea.setText(fileContent);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	}
	
	private void createFile(String name) {
		File file = new File(name);
	    try {
	    	if(file.createNewFile()){
	    		System.out.println("File Created");
			}else System.out.println("File already exists in the directory");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static void sendToConsole(String str) {
		lblConsole.setText(lblConsole.getText() + "\n" + str);
	}
}
