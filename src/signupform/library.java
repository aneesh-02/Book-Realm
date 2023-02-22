package signupform;
import java.sql.ResultSetMetaData;


import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

import java.awt.Font;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;

import todolist.ResultSetMetaData;

import javax.swing.JTextField;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Vector;
import java.awt.event.ActionEvent;
import javax.swing.JTable;
import javax.swing.border.EtchedBorder;
import java.awt.Color;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.SystemColor;
import javax.swing.UIManager;

public class library extends JFrame {

	private JPanel contentPane;
	private JTextField BookName;
	private JTextField BookEdition;
	private JTextField BookPrice;
	private JTable table;
	private JTextField BookId;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					library frame = new library();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	
	// CODE STARTS HERE ///////////////////////////////////////////////////////////////////////////////////
	
	Connection con = null;
	
	
	public library() {
		
		con = (Connection)DB.dbconnect();
		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 1148, 592);
		contentPane = new JPanel();
		contentPane.setBackground(new Color(255, 228, 225));
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JLabel lblNewLabel = new JLabel("BOOK REALM");
		lblNewLabel.setFont(new Font("Times New Roman", Font.BOLD, 25));
		lblNewLabel.setBounds(476, 10, 193, 44);
		contentPane.add(lblNewLabel);
		
		JPanel panel = new JPanel();
		panel.setBorder(new TitledBorder(new EtchedBorder(EtchedBorder.LOWERED, new Color(255, 255, 255), new Color(160, 160, 160)), "Registration", TitledBorder.LEADING, TitledBorder.TOP, null, new Color(0, 0, 0)));
		panel.setBounds(41, 107, 377, 202);
		contentPane.add(panel);
		panel.setLayout(null);
		
		JLabel lblNewLabel_1 = new JLabel("Book Name");
		lblNewLabel_1.setFont(new Font("Tahoma", Font.PLAIN, 20));
		lblNewLabel_1.setBounds(10, 51, 111, 38);
		panel.add(lblNewLabel_1);
		
		JLabel lblNewLabel_1_1 = new JLabel("Book Edition");
		lblNewLabel_1_1.setFont(new Font("Tahoma", Font.PLAIN, 20));
		lblNewLabel_1_1.setBounds(10, 98, 111, 38);
		panel.add(lblNewLabel_1_1);
		
		JLabel lblNewLabel_1_2 = new JLabel("Book Price");
		lblNewLabel_1_2.setFont(new Font("Tahoma", Font.PLAIN, 20));
		lblNewLabel_1_2.setBounds(10, 146, 111, 38);
		panel.add(lblNewLabel_1_2);
		
		BookName = new JTextField();
		BookName.setFont(new Font("Tahoma", Font.PLAIN, 20));
		BookName.setBounds(131, 54, 236, 33);
		panel.add(BookName);
		BookName.setColumns(10);
		
		BookEdition = new JTextField();
		BookEdition.setFont(new Font("Tahoma", Font.PLAIN, 20));
		BookEdition.setColumns(10);
		BookEdition.setBounds(131, 101, 236, 33);
		panel.add(BookEdition);
		
		BookPrice = new JTextField();
		BookPrice.setFont(new Font("Tahoma", Font.PLAIN, 20));
		BookPrice.setColumns(10);
		BookPrice.setBounds(131, 151, 236, 33);
		panel.add(BookPrice);
		
		JLabel lblNewLabel_1_4 = new JLabel("REGISTRATION");
		lblNewLabel_1_4.setFont(new Font("Tahoma", Font.PLAIN, 20));
		lblNewLabel_1_4.setBounds(112, 10, 140, 38);
		panel.add(lblNewLabel_1_4);
		
		JButton btnNewButton = new JButton("ADD");
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) 
			{
				try
				{
					//adding to backend database
					String  nameString = BookName.getText();
					String  editionString = BookEdition.getText();
					String  priceString = BookPrice.getText();
					
					PreparedStatement pst = (PreparedStatement)con.prepareStatement("insert into book(Name,Edition,Price) values(?,?,?)");
					
					pst.setString(1,nameString);
					pst.setString(2,editionString);
					pst.setString(3,priceString);
					 
					pst.executeUpdate();
					
					JOptionPane.showMessageDialog(null, "New book added");
					BookName.setText("");
					BookEdition.setText("");
					BookPrice.setText("");
					
					// displaying on frontend table from backend DB
					int a;
					PreparedStatement pst1 = (PreparedStatement)con.prepareStatement("select * from book");
					ResultSet rs = pst1.executeQuery();
					ResultSetMetaData rd = (ResultSetMetaData) rs.getMetaData();
					a=rd.getColumnCount();
					DefaultTableModel df = (DefaultTableModel)table.getModel();
					df.setRowCount(0);
					
					while(rs.next())
					{
						Vector v2 = new Vector();
						for(int i=1;i<a;i++)
						{
							v2.add(rs.getString("Id"));
							v2.add(rs.getString("Name"));
							v2.add(rs.getString("Edition"));
							v2.add(rs.getString("Price"));
						}
						df.addRow(v2); 
					}

				}
				catch (Exception e1)
				{
					e1.printStackTrace();
				}
			}
		});
		btnNewButton.setFont(new Font("Tahoma", Font.PLAIN, 20));
		btnNewButton.setBounds(41, 319, 119, 44);
		contentPane.add(btnNewButton);
		
		JButton btnEdit = new JButton("EDIT");
		btnEdit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				DefaultTableModel df = (DefaultTableModel)table.getModel();
				int s = table.getSelectedRow();
				int id = Integer.parseInt(df.getValueAt(s,0).toString());
				
				try
				{
					String  nameString = BookName.getText();
					String  editionString = BookEdition.getText();
					String  priceString = BookPrice.getText();
					
					PreparedStatement pst = con.prepareStatement("update book set Name=?, Edition=?, Price=? where id=?");
					
					pst.setString(1,nameString);
					pst.setString(2,editionString);
					pst.setString(3,priceString);
					pst.setInt(4,id);
					 
					pst.executeUpdate();
					
					JOptionPane.showMessageDialog(null, "list Updated");
					BookName.setText("");
					BookEdition.setText("");
					BookPrice.setText("");
					
					// displaying on frontend table from backend DB
					int a;
					PreparedStatement pst1 = (PreparedStatement)con.prepareStatement("select * from book");
					ResultSet rs = pst1.executeQuery();
					ResultSetMetaData rd = (ResultSetMetaData) rs.getMetaData();
					a=rd.getColumnCount();
					DefaultTableModel df1 = (DefaultTableModel)table.getModel();
					df1.setRowCount(0);
					
					while(rs.next())
					{
						Vector v2 = new Vector();
						for(int i=1;i<a;i++)
						{
							v2.add(rs.getString("Id"));
							v2.add(rs.getString("Name"));
							v2.add(rs.getString("Edition"));
							v2.add(rs.getString("Price"));
						}
						df.addRow(v2); 
					}

				}
				
				catch (Exception e1)
				{
					e1.printStackTrace();
				}
					
			}
		});
		btnEdit.setFont(new Font("Tahoma", Font.PLAIN, 20));
		btnEdit.setBounds(170, 319, 119, 44);
		contentPane.add(btnEdit);
		
		JButton btnDelete = new JButton("DELETE");
		btnDelete.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
//				dispose();
				DefaultTableModel df = (DefaultTableModel)table.getModel();
				
				int s = table.getSelectedRow();
				int id = Integer.parseInt(df.getValueAt(s,0).toString());
				try
				{
					PreparedStatement pst = con.prepareStatement("DELETE FROM book WHERE id=?");
					pst.setInt(1,id);
					pst.executeUpdate();
					
					JOptionPane.showMessageDialog(null, "Book deleted");
					BookName.setText("");
					BookEdition.setText("");
					BookPrice.setText("");
					
					int a;
					pst = (PreparedStatement)con.prepareStatement("select * from book");
					ResultSet rs = pst.executeQuery();
					ResultSetMetaData rd = (ResultSetMetaData) rs.getMetaData();
					a=rd.getColumnCount();
					DefaultTableModel df1 = (DefaultTableModel)table.getModel();
					df1.setRowCount(0);
					
					while(rs.next())
					{
						Vector v2 = new Vector();
						for(int i=1;i<a;i++)
						{
							v2.add(rs.getString("Id"));
							v2.add(rs.getString("Name"));
							v2.add(rs.getString("Edition"));
							v2.add(rs.getString("Price"));
						}
						df.addRow(v2); 
					}
					
				}
				catch (Exception e1)
				{
					e1.printStackTrace();
				}
			}
		});
		btnDelete.setFont(new Font("Tahoma", Font.PLAIN, 20));
		btnDelete.setBounds(299, 319, 119, 44);
		contentPane.add(btnDelete);
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(443, 103, 681, 434);
		contentPane.add(scrollPane);
		
		
		table = new JTable();
		table.setFont(new Font("Tahoma", Font.PLAIN, 12));
		table.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				
					DefaultTableModel df = (DefaultTableModel)table.getModel();
					int selected = table.getSelectedRow();
					int id = Integer.parseInt(df.getValueAt(selected, 0).toString());
					BookName.setText(df.getValueAt(selected, 1).toString());
					BookEdition.setText(df.getValueAt(selected, 2).toString());
					BookPrice.setText(df.getValueAt(selected, 3).toString());
					btnNewButton.setEnabled(false);
				
			}
		});
		scrollPane.setViewportView(table);
		table.setModel(new DefaultTableModel(
			new Object[][] {
			},
			new String[] {
				"ID", "Name", "Edition", "Price"
			}
		));
		
		JPanel panel_1 = new JPanel();
		panel_1.setLayout(null);
		panel_1.setBorder(new TitledBorder(new EtchedBorder(EtchedBorder.LOWERED, new Color(255, 255, 255), new Color(160, 160, 160)), "Search", TitledBorder.LEADING, TitledBorder.TOP, null, new Color(0, 0, 0)));
		panel_1.setBounds(41, 373, 377, 110);
		contentPane.add(panel_1);
		
		JLabel lblNewLabel_1_3 = new JLabel("Book ID");
		lblNewLabel_1_3.setFont(new Font("Tahoma", Font.PLAIN, 20));
		lblNewLabel_1_3.setBounds(10, 53, 97, 38);
		panel_1.add(lblNewLabel_1_3);
		
		BookId = new JTextField();
		BookId.setFont(new Font("Tahoma", Font.PLAIN, 20));
		BookId.setColumns(10);
		BookId.setBounds(117, 58, 250, 33);
		panel_1.add(BookId);
		
		JLabel lblNewLabel_1_3_1 = new JLabel("SEARCH ");
		lblNewLabel_1_3_1.setFont(new Font("Tahoma", Font.PLAIN, 20));
		lblNewLabel_1_3_1.setBounds(148, 10, 79, 38);
		panel_1.add(lblNewLabel_1_3_1);
		
		JButton btnSearch = new JButton("SEARCH");
		btnSearch.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				try 
				{
					int id = Integer.parseInt(BookId.getText());
					
		            // Prepare the SQL query to select book details for the given ID
		            PreparedStatement pst = con.prepareStatement("SELECT * FROM book WHERE id=?");
		            pst.setInt(1, id);
		            ResultSet rs = pst.executeQuery();

		            if (rs.next()) {
		                // If a matching book is found, display its details in the form
		                BookName.setText(rs.getString("Name"));
		                BookEdition.setText(rs.getString("Edition"));
		                BookPrice.setText(rs.getString("Price"));
		            } else {
		                // If no matching book is found, display an error message
		                JOptionPane.showMessageDialog(null, "No book found with ID " + id);
		            }
		
				}
				catch (Exception e1)
				{
					e1.printStackTrace();
				}
				
			}
		});
		btnSearch.setFont(new Font("Tahoma", Font.PLAIN, 20));
		btnSearch.setBounds(41, 493, 119, 44);
		contentPane.add(btnSearch);
		
		JButton btnClear_1 = new JButton("EXIT");
		btnClear_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				dispose();
			}
		});
		btnClear_1.setFont(new Font("Tahoma", Font.PLAIN, 20));
		btnClear_1.setBounds(299, 493, 119, 44);
		contentPane.add(btnClear_1);
		
		JLabel lblNewLabel_2 = new JLabel("Books Databse Management System");
		lblNewLabel_2.setFont(new Font("Tahoma", Font.PLAIN, 20));
		lblNewLabel_2.setBounds(415, 53, 329, 33);
		contentPane.add(lblNewLabel_2);
	}
}
