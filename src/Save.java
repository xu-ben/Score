import java.io.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.sql.*;

public class Save extends JFrame implements ActionListener{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	JTextField fieldDbPath = new JTextField();
	JButton buttonSelectDbPath = new JButton("选择");
	JButton buttonLinkAccess = new JButton("连接Access数据库");
	JButton buttonLinkMySQL = new JButton("连接MySQL数据库");
	JButton buttonSave = new JButton("保存程序");
	JButton buttonScoreSingle = new JButton("统计单次成绩");
	JButton buttonScoreMulti = new JButton("计算多次成绩");
	JButton buttonStandard = new JButton("生成评分标准");
	JTextArea areaTitle = new JTextArea();
	JTextArea areaUser = new JTextArea();
	JTextArea areaRankText = new JTextArea();
	JTextArea areaTestMark = new JTextArea();
	JTextArea areaTemplate = new JTextArea();
	JTextArea areaStandard = new JTextArea();
	
	Save(){
		Container con = this.getContentPane();
		con.setLayout(new BorderLayout(20, 20));
		JPanel panelControl = new JPanel();
		panelControl.setLayout(new GridLayout(2, 1, 0, 10));
		JPanel panelDatabase = new JPanel();
		panelDatabase.setLayout(new BorderLayout());
		panelDatabase.add(new JLabel("Access数据库路径："), BorderLayout.WEST);
		panelDatabase.add(fieldDbPath, BorderLayout.CENTER);
		JPanel panelDbOper = new JPanel();
		panelDbOper.setLayout(new BorderLayout());
		panelDbOper.add(buttonSelectDbPath, BorderLayout.WEST);
		panelDbOper.add(buttonLinkAccess, BorderLayout.CENTER);
		panelDatabase.add(panelDbOper, BorderLayout.EAST);
		panelControl.add(panelDatabase);
		JPanel panelOper = new JPanel();
		panelOper.setLayout(new GridLayout(1, 5, 15, 0));
		panelOper.add(buttonLinkMySQL);
		panelOper.add(buttonSave);
		panelOper.add(buttonScoreSingle);
		panelOper.add(buttonScoreMulti);
		panelOper.add(buttonStandard);
		panelControl.add(panelOper);
		con.add(panelControl, BorderLayout.NORTH);
		JPanel panelContent = new JPanel();
		panelContent.setLayout(new GridLayout(3, 2, 20, 20));
		JPanel panelTitle = new JPanel();
		panelTitle.setLayout(new BorderLayout());
		panelTitle.add(new JLabel("                    题目号"), BorderLayout.NORTH);
		panelTitle.add(new JScrollPane(areaTitle), BorderLayout.CENTER);
		panelContent.add(panelTitle);
		JPanel panelUser = new JPanel();
		panelUser.setLayout(new BorderLayout());
		panelUser.add(new JLabel("                    用户名"), BorderLayout.NORTH);
		panelUser.add(new JScrollPane(areaUser));
		panelContent.add(panelUser);
		
		JPanel panelRankText = new JPanel();
		panelRankText.setLayout(new BorderLayout());
		panelRankText.add(new JLabel("                  名次文本："), BorderLayout.NORTH);
		panelRankText.add(new JScrollPane(areaRankText), BorderLayout.CENTER);
		panelContent.add(panelRankText);
		JPanel panelTestMark = new JPanel();
		panelTestMark.setLayout(new BorderLayout());
		panelTestMark.add(new JLabel("                  第几次实验："), BorderLayout.NORTH);
		panelTestMark.add(new JScrollPane(areaTestMark), BorderLayout.CENTER);
		panelContent.add(panelTestMark);
		
		JPanel panelTemplate = new JPanel();
		panelTemplate.setLayout(new BorderLayout());
		panelTemplate.add(new JLabel("                  评分模板："), BorderLayout.NORTH);
		panelTemplate.add(new JScrollPane(areaTemplate), BorderLayout.CENTER);
		panelContent.add(panelTemplate);
		JPanel panelStandard = new JPanel();
		panelStandard.setLayout(new BorderLayout());
		panelStandard.add(new JLabel("                  评分标准："), BorderLayout.NORTH);
		panelStandard.add(new JScrollPane(areaStandard), BorderLayout.CENTER);
		panelContent.add(panelStandard);
		con.add(panelContent, BorderLayout.CENTER);
		
		buttonSelectDbPath.addActionListener(this);
		buttonLinkAccess.addActionListener(this);
		buttonLinkMySQL.addActionListener(this);
		buttonSave.addActionListener(this);
		buttonScoreSingle.addActionListener(this);
		buttonScoreMulti.addActionListener(this);
		buttonStandard.addActionListener(this);
		
		this.setBounds(30, 30, 770, 700);
		this.setTitle("ACM竞赛源代码保存和成绩计算系统");
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setVisible(true);
		this.setResizable(false);
	}
	public void actionPerformed(ActionEvent ae){
		Object obj = ae.getSource();
		if (obj == buttonSelectDbPath){
			class MyFileChooser extends JFileChooser{
				/**
				 * 
				 */
				private static final long serialVersionUID = 1L;
				public boolean accept(File f){
					if (f.isDirectory() || f.getName().endsWith(".mdb"))
						return true;
					else
						return false;
				}
				public MyFileChooser(String s){
					super(s);
				}
			}
			MyFileChooser fc = new MyFileChooser(fieldDbPath.getText().trim());
			fc.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
			int result = fc.showOpenDialog(this);
			if (result == JFileChooser.APPROVE_OPTION){
				File file = fc.getSelectedFile();
				String path = file.getAbsolutePath();
				fieldDbPath.setText(path);
			}
			return;
		}
		if (obj == buttonLinkAccess){
			String accessPath = fieldDbPath.getText().trim();
			if (accessPath.length() == 0){
				JOptionPane.showMessageDialog(this, "请输入Access数据库路径！");
				return;
			}
			DBOperator.connectAccess(accessPath);
			JOptionPane.showMessageDialog(this, "Access数据库已连接");
		}
		if (obj == buttonLinkMySQL){
			DBOperator.connectMySQL();
			JOptionPane.showMessageDialog(this, "MySQL数据库已连接");
		}
		if (obj == buttonSave){
			if (DBOperator.stmtAccess == null){
				JOptionPane.showMessageDialog(this, "请连接Access数据库");
				return;
			}
			if (DBOperator.stmtMySQL == null){
				JOptionPane.showMessageDialog(this, "请连接MySQL数据库");
				return;
			}
			String strTitle = areaTitle.getText().trim();
			String[] astrTitle = strTitle.split("\n");
			int titleLen = astrTitle.length;
			for (int i=0; i<titleLen; i++)
				astrTitle[i] = astrTitle[i].trim();
			String strUser = areaUser.getText().trim();
			String[] astrUser = strUser.split("\n");
			int userLen = astrUser.length;
			for (int i=0; i<userLen; i++)
				astrUser[i] = astrUser[i].trim();
			String tableName = "program";
			if (tableName.length() == 0){
				JOptionPane.showMessageDialog(this, "请输入表名");
				return;
			}
			for (int i=0; i<userLen; i++){
				insertOneUserProgram(astrUser[i], astrTitle, "program", i);
			}
			try{
				DBOperator.stmtAccess.execute("insert into program (mark) values(9999)");
			}catch(SQLException sqle){
				
			}
			JOptionPane.showMessageDialog(this, "程序已保存！");
		}
		if (obj == buttonScoreSingle){
			String pile = this.areaRankText.getText();
			if (pile.length() == 0){
				JOptionPane.showMessageDialog(this, "请输入名次文本");
				return;
			}
			String standardLine = this.areaTemplate.getText().trim();
			if (standardLine.length() == 0){
				JOptionPane.showMessageDialog(this, "请输入单行评分模板");
				return;
			}
			String userText = this.areaUser.getText().trim();
			if (userText.length() == 0){
				JOptionPane.showMessageDialog(this, "请输入带n的学号");
				return;
			}
			String strWhich = this.areaTestMark.getText().trim();
			if (strWhich.length() == 0){
				JOptionPane.showMessageDialog(this, "请输入这是哪次实验，从1计数");
				return;
			}
			/*
			String[] users = userText.split("\n");
			for (int i=0; i<users.length; i++)
				users[i] = users[i].trim();
			Quaternion[] aQuaternion = Quaternion.findQuaternionGroup(pile, users);
			Quaternion.dealWithQuaternion(aQuaternion, standardLine);
			if (DBOperator.stmtAccess == null){
				JOptionPane.showMessageDialog(this, "请连接Access数据库！");
				return;
			}
			for (int i=0; i<users.length; i++){
				try{
					String column = "test_" + strWhich;
					String sql = "update score set " + column + " = " + aQuaternion[i].score + " ";
					sql += "where student_num = " + "'" + aQuaternion[i].userName + "'";
					DBOperator.stmtAccess.executeUpdate(sql);
				}
				catch(SQLException sqle){
					JOptionPane.showMessageDialog(this, "向数据库中保存数据时出错！");
					return;
				}
			}
			JOptionPane.showMessageDialog(this, "此次实验成绩已经存入数据库中!");
			*/
			boolean result = this.createScoreSingle(pile, standardLine, userText, Integer.parseInt(strWhich));
			JOptionPane.showMessageDialog(this, "" + result);
		}
		if (obj == buttonScoreMulti){
			String strUsers = this.areaUser.getText().trim();
			if (strUsers.length() == 0){
				JOptionPane.showMessageDialog(this, "请输入用户列表");
				return;
			}
			String strTemplate = this.areaTemplate.getText().trim();
			if (strTemplate.length() == 0){
				JOptionPane.showMessageDialog(this, "请输入多条评分模板");
				return;
			}
			String strRankText = this.areaRankText.getText().trim();
			if (strRankText.length() == 0){
				JOptionPane.showMessageDialog(this, "请输入多个排名文本");
				return;
			}
			String[] aPile = strRankText.split("##########");
			String[] aLine = strTemplate.split("\n");
			for (int i=0; i<aPile.length; i++){
				boolean result = this.createScoreSingle(aPile[i], aLine[i], strUsers, (i+1));
				if (!result){
					JOptionPane.showMessageDialog(this, "第 " + (i+1) + " 次实验分数保存失败");
					return;
				}
			}
			JOptionPane.showMessageDialog(this, "成绩已计算，并且保存成功");
		}
		if (obj == this.buttonStandard){
			String standardTemplate = this.areaTemplate.getText().trim();
			if (standardTemplate.length() == 0){
				JOptionPane.showMessageDialog(this, "请输入评分模板");
				return;
			}
			String[] astrTemplate = standardTemplate.split("\n");
			for (int i=0; i<astrTemplate.length; i++)
				astrTemplate[i] = astrTemplate[i].trim();
			String result = "";
			for (int i=0; i<astrTemplate.length; i++)
				result += Quaternion.createStandardSingle(i+1, astrTemplate[i]);
			this.areaStandard.setText(result);
		}
	}
	public boolean createScoreSingle(String strPile, String standardLine, String userText, int which){
		strPile = "  " + strPile;
		String[] users = userText.split("\n");
		for (int i=0; i<users.length; i++)
			users[i] = users[i].trim();
		Quaternion[] aQuaternion = Quaternion.findQuaternionGroup(strPile, users);
		Quaternion.dealWithQuaternion(aQuaternion, standardLine);
		if (DBOperator.stmtAccess == null){
			JOptionPane.showMessageDialog(this, "请连接Access数据库！");
			return false;
		}
		for (int i=0; i<users.length; i++){
			try{
				String column = "test_" + which;
				String sql = "update score set " + column + " = " + aQuaternion[i].score + " ";
				sql += "where student_num = " + "'" + aQuaternion[i].userName + "'";
				DBOperator.stmtAccess.executeUpdate(sql);
			}
			catch(SQLException sqle){
				JOptionPane.showMessageDialog(this, "向数据库中保存数据时出错！");
				return false;
			}
		}
		return true;
	}
	public static void insertOneUserProgram(String userName, String[] aTitle, String tableName, int mark){
		String[] astrProgram = new String[aTitle.length];
		for (int i=0; i<aTitle.length; i++){
			astrProgram[i] = "No Solution";
		}
		try{
			for (int i=0; i<aTitle.length; i++){
				String strQuery = "select solution_id from solution where user_id = '";
				strQuery += userName;
				strQuery += "' and problem_id = '";
				strQuery += aTitle[i];
				strQuery += "' order by solution_id desc";
				ResultSet rs = DBOperator.stmtMySQL.executeQuery(strQuery);
				rs.beforeFirst();
				if (!rs.isBeforeFirst())
					continue;
				rs.next();
				String solutionId = rs.getString(1);
				rs.close();
				strQuery = "select uncompress(source) as code from source_code where solution_id = " + solutionId;
				rs = DBOperator.stmtMySQL.executeQuery(strQuery);
				rs.beforeFirst();
				rs.next();
				astrProgram[i] = rs.getString(1);

			}
			String strMark = "" + mark;
			String strInsert = "insert into " + tableName + " (mark, user_id, ";
			for (int i=0; i<aTitle.length; i++){
				String numColumn = "" + i;
				if (numColumn.length() == 1)
					numColumn = "p0" + numColumn;
				else
					numColumn = "p" + numColumn;
				strInsert += numColumn;
				if (i != aTitle.length - 1)
					strInsert += ",";
			}
			strInsert += ") values ('" + strMark + "', '" + userName + "', ";
			for (int i=0; i<aTitle.length; i++){
				astrProgram[i] = astrProgram[i].replaceAll("'", "|SingleQuote|");
				astrProgram[i] = astrProgram[i].replaceAll("\"", "|DoubleQuote|");
			}
			for (int i=0; i<aTitle.length; i++){
				strInsert += "'" +  astrProgram[i] + "'";
				if (i != aTitle.length-1)
					strInsert += ",";
			}
			strInsert += ")";
			DBOperator.stmtAccess.execute(strInsert);
		}catch(SQLException sqle){
			System.out.println("MySQL: " + sqle);
		}
	}
	public static void main(String[] args){
		new Save();
	}
}