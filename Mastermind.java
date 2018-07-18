import java.util.Scanner;
import java.util.ArrayList;
import java.util.Iterator;
import java.io.*;
import javax.swing.*;
import java.awt.*;
import javax.swing.event.*;
import java.awt.event.*;
import javax.swing.table.DefaultTableModel;
import java.sql.*;

interface GuardarPartida {
	public boolean guardar(Partida partida, int loadgameid);	
}

class Database {
	
	public Database() {
		
	}
	
	public void mostraSQLException(SQLException ex) {
		ex.printStackTrace(System.err);
		System.err.println("SQLState: " + ex.getSQLState());
		System.out.println("Error Code: " + ex.getErrorCode());	
		System.out.println("Message: " + ex.getMessage());	
		Throwable t = ex.getCause();
		while(t != null) {
			System.out.println("Cause: " + t);
			t = t.getCause();
		}
	}
	
	public void createDatabase() {
		String url = "jdbc:mysql://localhost:3306/";
		String user = "root";
		String pwd = "";	
		String cad0 = "create database if not exists Mastermind";
		String cad1 = "use Mastermind";
		String cad2 = "create table if not exists partides(" +
							   "id int not null auto_increment primary key, " + 
							   "random_num varchar(5) not null, " + 
							   "acabat boolean not null)";
		String cad3 = "create table if not exists tirades (" +
							  "id_partida int not null," +
							  "num_tirada varchar(5) not null," +
							  "BP int not null," +
							  "MP int not null," +
							  "FOREIGN KEY (id_partida) REFERENCES partides(id)" +
							  ")";
		Connection con = null;
		Statement st = null;
		
		try {
			Class.forName("com.mysql.jdbc.Driver");
			con = DriverManager.getConnection(url,user,pwd);
			st = con.createStatement();
			System.out.println("Creacio BD: " + st.executeUpdate(cad0));
			System.out.println("Use BD: " + st.executeUpdate(cad1));
			System.out.println("Crear taula partides: " + st.executeUpdate(cad2));
			System.out.println("Crear taula tirades: " + st.executeUpdate(cad3));
			if(con !=null) con.close();
		}
		catch(ClassNotFoundException e) {
			e.printStackTrace();
		}
		catch(SQLException e) {
			mostraSQLException(e);
		}
		finally{}
		
	}
	
	public boolean fillLoadedGames(Partida partida, Tirada tirada, DefaultTableModel model, int loadgameid) {
		String url = "jdbc:mysql://localhost:3306/";
		String user = "root";
		String pwd = "";	
		String cad0 = "use Mastermind";
		String cad1 ="select random_num from partides where id = " + loadgameid;
		String cad2 ="select num_tirada, BP, MP from tirades where id_partida = " + loadgameid;
		Connection con = null;
		Statement st = null;
		ResultSet rs;
		try {
			Class.forName("com.mysql.jdbc.Driver");
			con = DriverManager.getConnection(url,user,pwd);
			st = con.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
			System.out.println("Seleccionar BD: " + st.executeUpdate(cad0));
			rs =  st.executeQuery(cad1);
			while(rs.next()) {
				partida.setRandomnum(rs.getString(1));
			}
			rs = st.executeQuery(cad2);
			while(rs.next()) {
				Object[] newRow = {rs.getString(1), rs.getInt(2), rs.getInt(3), null};
				model.addRow(newRow);
			}
			if(con !=null) con.close();
		}
		catch(ClassNotFoundException e) {
			e.printStackTrace();
		}
		catch(SQLException e) {
			mostraSQLException(e);
		}
		finally{}
		
		return true;
	}
	
	public void loadGames(DefaultTableModel model) {
		String url = "jdbc:mysql://localhost:3306/";
		String user = "root";
		String pwd = "";	
		String cad0 = "use Mastermind";
		String cad1 = "select * from partides where acabat = 0";
		Connection con = null;
		Statement st = null;
		ResultSet rs;
		try {
			Class.forName("com.mysql.jdbc.Driver");
			con = DriverManager.getConnection(url,user,pwd);
			st = con.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
			System.out.println("Seleccionar BD: " + st.executeUpdate(cad0));
			rs =  st.executeQuery(cad1);
			String acabat = "No";
			while (rs.next()) {
				if (rs.getInt(3) == 1){
					acabat = "yes";
				}
				Object[] newRow = {rs.getInt(1), acabat};
				model.addRow(newRow);
			  }
			if(con !=null) con.close();
		}
		catch(ClassNotFoundException e) {
			e.printStackTrace();
		}
		catch(SQLException e) {
			mostraSQLException(e);
		}
		finally{}
	}
	
}

class GuardarPartidaFitxer implements GuardarPartida {	
	public boolean guardar(Partida partida, int loadgameid) {
		return true;
	}
}

class GuardarPartidaBD implements GuardarPartida {	
	public void mostraSQLException(SQLException ex) {
		ex.printStackTrace(System.err);
		System.err.println("SQLState: " + ex.getSQLState());
		System.out.println("Error Code: " + ex.getErrorCode());	
		System.out.println("Message: " + ex.getMessage());	
		Throwable t = ex.getCause();
		while(t != null) {
			System.out.println("Cause: " + t);
			t = t.getCause();
		}
	}
	
	public boolean guardar(Partida partida,int loadgameid) {
		String url = "jdbc:mysql://localhost:3306/";
		String user = "root";
		String pwd ="";
		String cad0 ="use Mastermind";
		int[] randomnum = partida.getRandomnum();
		String numrandom ="";
		for (int i = 0; i < randomnum.length; i++){
			numrandom += randomnum[i];
		}
		int acabat = 0;
		if (partida.getAcabat() == true){
			acabat = 1;
		}
		String cad1 ="insert into partides values ( null, '" + numrandom + "', " + acabat + ")";
		String cad2 ="select id from partides where random_num = " + "'" + numrandom + "'";
		String cad4 ="select id from partides where id = " + loadgameid;
		
		Connection con = null;
		Statement st = null;
		ResultSet rs;
		try {
			Class.forName("com.mysql.jdbc.Driver");
			con = DriverManager.getConnection(url,user,pwd);
			st = con.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
			
			System.out.println("Seleccionar BD: " + st.executeUpdate(cad0));
			rs = st.executeQuery(cad4);
			if (rs.next() == false){
				System.out.println("insertar partida: " + st.executeUpdate(cad1));
				System.out.println("Seleccionar id partida: ");
				rs =  st.executeQuery(cad2);
				int id = 0;
				while (rs.next()) {
					id = rs.getInt(1);
				  }
				ArrayList<Tirada> tirades = partida.getArray();
				for (Iterator it = tirades.iterator(); it.hasNext();){
					Tirada auxiliar = (Tirada)it.next();
					int[] tirada = auxiliar.getPlayernum();
					String numtirada = "";
					for (int i = 0; i < tirada.length; i++){
						numtirada += tirada[i];
					}
					String cad3 = "insert into tirades values (" + id + ",'" + numtirada + "'," + auxiliar.getWellpositioned() + "," + auxiliar.getBadpositioned() + ")";
					System.out.println("Insertar tirada: " + st.executeUpdate(cad3));
				}
			}
			else {
					String cad5 = "update partides set acabat = 1 where id = " + loadgameid;
					st.executeUpdate(cad5);
			}
			
			if(con !=null) con.close();
		}
		catch(ClassNotFoundException e) {
			e.printStackTrace();
		}
		catch(SQLException e) {
			mostraSQLException(e);
		}
		finally{}
		return true;
	}
}

class Singleton {
	private GuardarPartida magatzem;
	private static Singleton INSTANCIA= new Singleton();

	private Singleton() {
		codiGuardar(2);
	}
	
	public static Singleton getInstancia() {
		return INSTANCIA;
	}	
	
	public GuardarPartida getmagatzem() {
		return magatzem;
	}
	
	public void codiGuardar(int codi) {
		switch (codi) {
			case 1: magatzem= new GuardarPartidaFitxer(); break;
			case 2: magatzem= new GuardarPartidaBD(); break;
		}
	}
}

class Joc {
	private static int dificulty;
	private ArrayList<Partida> partides = new ArrayList<Partida>();
	
	public Joc (){
	}
	public void printArray() {
		for (Iterator it = partides.iterator(); it.hasNext();){
			System.out.println(it.next());
		}
	}
	public void addToGames(Partida p1) {
		partides.add(p1);
	}
	public static void setDificulty (int newdificulty) {
		dificulty = newdificulty;
	}
	
	public int getDificulty () {
		return this.dificulty;
	}
	
	public boolean RoundStart (String number, Tirada tirada, Partida partida){
		//System.out.println(partida.toString());
		return tirada.compareTables(partida.getRandomnum());
	}
}

class Mastermind {
		JFrame frame = new JFrame("Mastermind");
		JPanel panell = (JPanel)frame.getContentPane();
		JTextField campNumeros = new JTextField("");
		JTextArea yourInput = new JTextArea("");
		JMenuBar bar = new JMenuBar();
		JMenu arxiu = new JMenu("Menu");
		JMenu estils = new JMenu("Mode");
		JRadioButtonMenuItem dificil = new JRadioButtonMenuItem("Dificil");
		JRadioButtonMenuItem facil = new JRadioButtonMenuItem("Facil");
		ButtonGroup grup = new ButtonGroup();
		JButton newGame = new JButton("Crear partida");
		JButton continueGame= new JButton("Seguir partida");
		JMenuItem saveGame = new JMenuItem("Guardar Partida");
		Object[][] random = {};
		JButton enviar = new JButton("enviar");
		String[] columnNamesEasy = {"Tirada", "BP", "MP", "taulaResultat"};
		String[] columnNamesHard = {"Tirada", "BP", "MP"};
		JTable table = new JTable(null, columnNamesEasy);
		DefaultTableModel model = new DefaultTableModel(random, columnNamesEasy);
		int loadgameid = 0;
		Joc obj1 = new Joc();
		Database database = new Database();
		Partida partida = new Partida(5, 0, 10);
		Tirada tirada = new Tirada();
		
		public Mastermind(boolean aux) {	
		}
		
		public Mastermind () {
			grup.add(dificil);
			grup.add(facil);
			estils.add(dificil);
			estils.add(facil);
			arxiu.add(estils);
			arxiu.add(saveGame);
			JLabel titol = new JLabel("MASTERMIND");
			JLabel yourNumber = new JLabel("YOUR NUMBER:");
			titol.setFont(new Font("Comic Sans MS", Font.BOLD,30));
			yourNumber.setFont(new Font("Comic Sans MS", Font.BOLD,15));
			frame.setSize(600,600);
			panell.setLayout(new GridBagLayout());
			table = new JTable(model);
			JScrollPane scrollPane = new JScrollPane(table);
			table.setFillsViewportHeight(true);
			panell.add(titol, new GridBagConstraints(0,0,0,0,0.0,0.0,GridBagConstraints.NORTH, GridBagConstraints.NONE, new Insets(15,0,0,0),0,0));
			panell.add(scrollPane, new GridBagConstraints(0,0,0,0,10.0,10.0,GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(0,0,0,10),0,0));
			panell.add(newGame, new GridBagConstraints(0,0,0,0,0.0,0.0,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0,0,0,0),0,0));
			panell.add(continueGame, new GridBagConstraints(0,0,0,0,0.0,0.0,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(80,0,0,0),0,0));
			panell.add(campNumeros, new GridBagConstraints(10,0,0,0,0.0,0.0,GridBagConstraints.SOUTH, GridBagConstraints.NONE, new Insets(0,0,10,0),300,0));
			panell.add(enviar, new GridBagConstraints(0,0,0,0,0.0,0.0,GridBagConstraints.SOUTHEAST, GridBagConstraints.NONE, new Insets(0,0,10,10),0,0));
			panell.add(yourNumber, new GridBagConstraints(0,0,0,0,0.0,0.0,GridBagConstraints.SOUTHWEST, GridBagConstraints.NONE, new Insets(0,10,10,0),0,0));
			
			enviar.addActionListener(new StartRound());
			saveGame.addActionListener(new SaveGame());
			continueGame.addActionListener(new LoadGame());
			newGame.addActionListener(new NewGame());

			bar.add(arxiu);
			frame.setJMenuBar(bar);
			frame.setLocationRelativeTo(null);
			frame.setResizable(true);
			frame.setVisible(true);
			
		}
		
		private class StartRound implements ActionListener {
			public void actionPerformed(ActionEvent e) {
				try {
					int aux = Integer.parseInt(campNumeros.getText());// utilitz aquesta linea per comprovar que son numeros que entram, sinos hi haura una Exception
					String number = campNumeros.getText();
					boolean same = false;
					if (number.length() == 5){
						tirada = new Tirada(number);
						same = obj1.RoundStart(number, tirada, partida);
						int badpos = tirada.getBadpositioned();
						int wellpos = tirada.getWellpositioned();
						int[] easytable = tirada.getEndtable();
						String convertedtable = "";
						for (int i = 0; i < easytable.length; i++){
							convertedtable = convertedtable + easytable[i];
						}
						partida.addToArray(tirada);
						if (dificil.isSelected()){
							Object[] newRow = {number, wellpos, badpos, null};
							model.addRow(newRow);
						}
						else{
							Object[] newRow = {number, wellpos, badpos, convertedtable};
							model.addRow(newRow);
						}
						if (same){
							partida.setAcabat(true);
							Singleton.getInstancia().getmagatzem().guardar(partida, loadgameid);
						}
					}
				}
				catch(NumberFormatException b) {
				}
			}
		}
		
		private class SaveGame implements ActionListener {
			public void actionPerformed(ActionEvent e) {
				Singleton.getInstancia().getmagatzem().guardar(partida, loadgameid);
				System.out.println("partida Guardada");
			}
		}
		
		private class LoadPartida implements ActionListener {
			public void actionPerformed(ActionEvent e) {
				loadgameid = Integer.parseInt(yourInput.getText());
				model.setRowCount(0);
				database.fillLoadedGames(partida, tirada, model, loadgameid);
			}
		}
			
		private class LoadGame implements ActionListener {
			Object[][] random2 = {};
			String[] LoadColumnNames = {"id_Partida", "acabat"};
			JTable table2 = new JTable(null, LoadColumnNames);
			DefaultTableModel model2 = new DefaultTableModel(random2, LoadColumnNames);
			public void actionPerformed(ActionEvent e) {
				JFrame Loadframe = new JFrame("Load Game");
				JPanel Loadpanell = (JPanel)Loadframe.getContentPane();
				Loadframe.setSize(600,350);
				Loadpanell.setLayout(new GridBagLayout());
				JButton accept = new JButton("Accept");
				table2 = new JTable(model2);
				JScrollPane scrollPane = new JScrollPane(table2);
				table2.setFillsViewportHeight(true);
				
				Loadpanell.add(scrollPane, new GridBagConstraints(0,0,0,0,0.0,0.0,GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(0,0,100,0),400,300));
				Loadpanell.add(accept, new GridBagConstraints(0,0,0,0,0.0,0.0,GridBagConstraints.SOUTHEAST, GridBagConstraints.NONE, new Insets(0,0,60,0),0,0));
				Loadpanell.add(yourInput, new GridBagConstraints(0,0,0,0,0.0,0.0,GridBagConstraints.SOUTHEAST, GridBagConstraints.NONE, new Insets(0,0,63,100),300,5));
				
				accept.addActionListener(new LoadPartida());
				accept.addActionListener(new ActionListener(){
					public void actionPerformed(ActionEvent e) {
						Loadframe.dispose();
					}
				});
				model2.setRowCount(0);
				database.loadGames(model2);
				Loadframe.setLocationRelativeTo(null);
				Loadframe.setResizable(true);
				Loadframe.setVisible(true);
				
			}
		}
		
		private class NewGame implements ActionListener {
			public void actionPerformed(ActionEvent e) {
				Singleton.getInstancia().getmagatzem().guardar(partida, loadgameid);
				partida = new Partida(5, 0, 10);
				model.setRowCount(0);
				System.out.println("partida creada");
			}
		}
		
		public void StartScreen () {//pantalla inici
			JFrame Startframe = new JFrame("Welcome");
			JPanel Startpanell = (JPanel)Startframe.getContentPane();
			Joc obj = new Joc();
			Startframe.setSize(600,350);
			Startpanell.setLayout(new GridBagLayout());
			JLabel titol = new JLabel("WELCOME TO MASTERMIND!");
			titol.setFont(new Font("Comic Sans MS", Font.BOLD,30));
			JButton continuar = new JButton("Continue");
			
			Startpanell.add(titol, new GridBagConstraints(0,0,0,0,0.0,0.0,GridBagConstraints.NORTH, GridBagConstraints.NONE, new Insets(0,0,100,0),0,0));
			Startpanell.add(continuar, new GridBagConstraints(0,0,0,0,0.0,0.0,GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0,0,0,0),0,0));
			
			continuar.addActionListener(new MainGame());
			
			Startframe.addWindowListener(new BWinListener());
			Startframe.setLocationRelativeTo(null);
			Startframe.setResizable(true);
			Startframe.setVisible(true);
		}
		
		private class MainGame implements ActionListener {//cream partida i carrega la pantalla principal del joc
			public void actionPerformed(ActionEvent e) {
				new Mastermind();
			}
		}
		
		private class BWinListener extends WindowAdapter {
			public void windowClosing(WindowEvent e) {
				System.exit(0);
			}
		}
		public static void main (String args[]) {
			Mastermind aux = new Mastermind(true);
			Database bd = new Database();
			bd.createDatabase();
			aux.StartScreen();
		}
	
}
