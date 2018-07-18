import java.util.Random;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
public class Partida {
	private int[] randomnum = new int[5];
	private boolean finished = false;
	private ArrayList<Tirada> tirades = new ArrayList<Tirada>();
	
	public Partida () {
	}	
	
	public Partida (int length, int start, int end) {
		Random rand = new Random();
		for (int i = 0; i < length; i++) {
			randomnum[i] =(rand.nextInt(end - start) + start);
		}
	}
	
	public void setAcabat(boolean newacabat) {
		this.finished = newacabat;
	}
	
	public boolean getAcabat() {
		return this.finished;
	}
	
	public String toString() {
		String table = "";
		for (int i = 0; i < randomnum.length; i++){
			table = table + randomnum[i] + " ";
		}
		return table;
	}
	
	public int[] getRandomnum() {
		return randomnum;
	}
	
	public void setRandomnum(String partida) {
		for (int i = 0; i < randomnum.length; i++){
			randomnum[i] = partida.charAt(i) - 48;
		}
	}
	
	public void addToArray(Tirada p1) {
		tirades.add(p1);
	}
	
	public ArrayList<Tirada> getArray() {
		return tirades;
	}
	
	public void printArray() {
		for (Iterator it = tirades.iterator(); it.hasNext();){
			System.out.println(it.next());
		}
	}
}

