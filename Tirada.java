public class Tirada {
	private int[] playernum = new int[5];
	private int wellpositioned = 0;
	private int badpositioned = 0;
	private int[] endtable = {0,0,0,0,0};
	
	public Tirada() {
	}
	//public Tirada ()
	public Tirada (String player) {  
		for (int i = 0; i < player.length(); i++){
			playernum[i] = (player.charAt(i) - 48);
		}
	}
	
	public boolean compareTables (int[] tofind) {
		boolean same = false;
		int repeats = 0;
		int howmanyinourtable = 0;
		for (int i = 0; i < playernum.length; i++) {
			if (playernum[i] == tofind[i]){
					endtable[i] = 1;
			}
		}
		for (int i = 0; i < playernum.length; i++) {
			if (endtable[i] != 1){
				for (int j = 0; j < tofind.length; j++){
					if (playernum[i] == tofind[j] && endtable[j] != 1){
						repeats = 0;
						howmanyinourtable = 0;
						for (int k = 0; k < tofind.length; k++){
							if (tofind[k] == playernum[i]){
								repeats++;
							}
						}
						for (int l = 0; l < playernum.length; l++){
							if (endtable[l] == 2 && playernum[l] == playernum[i]){
								howmanyinourtable++;
							}
						}
						
						if (howmanyinourtable < repeats) {
							endtable[i] = 2;
							howmanyinourtable++;
						}
					}
				}
			}
		}
		int number = 0;
		for (int i = 0; i < endtable.length; i++){
			if (endtable[i] == 1){
				number++;
				wellpositioned+= 1;
			}
			if (endtable[i] == 2){
				badpositioned += 1;
			}
		}
		if (number == 5) {
			same = true;
		}
		return same;
	}
	
	public void printEndTable() {
		for (int i = 0; i < endtable.length; i++) {
			System.out.print(endtable[i]);
		}
	}
	public String toString() {
		String playernumbers = "";
		for (int i = 0; i < playernum.length; i++){
			playernumbers = playernumbers + playernum[i] + " ";
		}
		return playernumbers;
	}
	
	public int[] getPlayernum () {
		return playernum;
	}
	
	public int[] getEndtable () {
		return endtable;
	}
	
	public int getWellpositioned () {
		return wellpositioned;
	}
	
	public int getBadpositioned () {
		return badpositioned;
	}
	
	public void setPlayernum (int[] newPlayernum) {
		for (int i = 0; i < newPlayernum.length ; i++){
			playernum[i] = newPlayernum[i];
		}
	}
	
	public void setEndtable (int[] newEndtable) {
		for (int i = 0; i <newEndtable.length ; i++){
			endtable[i] = newEndtable[i];
		}
	}
	
	public void setWellpositioned (int newnumber) {
		wellpositioned = newnumber;
	}
	
	public void setBadpositioned (int newnumber) {
		badpositioned = newnumber;
	}
}

