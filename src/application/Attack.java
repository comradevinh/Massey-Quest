package application;

//Vinh Nguyen and Maggie Maun
//Our FSE which will be a role-playing game with a developed story; Massey Quest


import javafx.application.Application; //inputting all necessary aspects
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import java.util.*;
import java.io.*; //using serailization to input character data
import java.util.TreeMap;


//note: just add all the objects to an arraylist which will then be serialized, since each element within
//is its own individual object even after serailization

public class Attack implements Serializable {

    private TreeMap<Character,Weapons[]> profileMap = new TreeMap<Character,Weapons[]>(); //player team
    private TreeMap<Character,Weapons[]> enemyProfileMap = new TreeMap<Character,Weapons[]>(); //enemylist

    ArrayList<Character> characters = new ArrayList<Character>();
    ArrayList<Weapons> weapons= new ArrayList<Weapons>();
    ArrayList<Character> enemies = new ArrayList<Character>();

    Weapons weaponsArray[]= new Weapons[5]; //will serve as temprary array when putting weapons into treeset

	public Attack() {// no clue as to what imma do with this yet, but its a constructor
		
	}

	public void save() {

        try { //serializing occurs, will act as saving data, very efficient
            FileOutputStream saveState = new FileOutputStream("/tmp/savestate.ser");
            ObjectOutputStream out = new ObjectOutputStream(saveState);
            out.writeObject(profileMap);
            out.writeObject(enemyProfileMap);
            out.writeObject(weapons);
            out.close(); //closing it
            saveState.close();
            System.out.println("Serialized data is saved in /tmp/savestate.ser");
        } catch (IOException i) {
            i.printStackTrace();
        }

    }

    public void load(){
        try {
            FileInputStream loadState = new FileInputStream("/tmp/savestate.ser");
            ObjectInputStream in = new ObjectInputStream(loadState);
            profileMap = (TreeMap<Character, Weapons[]>) in.readObject();
            enemyProfileMap=(TreeMap<Character, Weapons[]>) in.readObject();
            weapons=(ArrayList<Weapons>) in.readObject();
            in.close();
            loadState.close(); //closing it
        } catch (IOException i) {
            i.printStackTrace();
            return;
        } catch (ClassNotFoundException c) {
            System.out.println("Not found");
            c.printStackTrace();
        }

    }

    public void refreshData(){

	    startingData(); //simply calls upon this in order to create a new save file

    }

    public void startingData(){

        try {
            Scanner inFile = new Scanner(new File("Character Statistics.txt")); //Scanner as input; input is in file
            while (inFile.hasNext()) {
                characters.add(new Character(inFile.nextLine()));
            }
            //inputing all the characters, which will then be put into the treeset
            inFile.close();



        }

        catch(IOException e) {
                System.out.println("Datafile not found");
        }

        try{
			Scanner inFile2 = new Scanner(new File("Weapon Statistics.txt")); //Scanner as input; input is in file
			while (inFile2.hasNext()) {
				weapons.add(new Weapons(inFile2.nextLine()));
			}
			//inputing all the characters, which will then be put into the treeset
			inFile2.close();
		}
		catch(IOException e) {
			System.out.println("Datafile not found");
		}

        try{
            Scanner inFile3 = new Scanner(new File("Enemy Statistics.txt")); //Scanner as input; input is in file
            while (inFile3.hasNext()) {
                enemies.add(new Character(inFile3.nextLine()));
            }
            //inputing all the characters, which will then be put into the treeset
            inFile3.close();
        }
        catch(IOException e) {
            System.out.println("Datafile not found");
        }

        weaponsArray[0]=weapons.get(0); //inputing character weapons into array, putting into treeset
        profileMap.put(characters.get(0),weaponsArray);
        weaponsArray[0]=weapons.get(4);
        profileMap.put(characters.get(1),weaponsArray);
        weaponsArray[0]=weapons.get(8);
        profileMap.put(characters.get(2),weaponsArray);
        weaponsArray[0]=weapons.get(12);
        profileMap.put(characters.get(3),weaponsArray);
        weaponsArray[0]=weapons.get(16);
        profileMap.put(characters.get(4),weaponsArray);

        weaponsArray[0]=weapons.get(1); //inputing enemy weapons into array, putting into treeset
        enemyProfileMap.put(enemies.get(0),weaponsArray);
        weaponsArray[0]=weapons.get(18);
        enemyProfileMap.put(enemies.get(1),weaponsArray);
        weaponsArray[0]=weapons.get(19);
        enemyProfileMap.put(enemies.get(2),weaponsArray);
        weaponsArray[0]=weapons.get(19);
        enemyProfileMap.put(enemies.get(3),weaponsArray);
        weaponsArray[0]=weapons.get(20);
        enemyProfileMap.put(enemies.get(4),weaponsArray);
        weaponsArray[0]=weapons.get(17);
        enemyProfileMap.put(enemies.get(5),weaponsArray);
        weaponsArray[0]=weapons.get(27);
        enemyProfileMap.put(enemies.get(6),weaponsArray);

        save();

        //inputing all the data into the treeset for preparing the beginning of the game

	}

	//ATTACK CALCULATIONS

    public int damage(Character cOne, Character cTwo, Weapons wOne, Weapons wTwo){ //will be the method used to calculate damage

        int attackPower=cOne.getStrength()+wOne.getMight(); //will be the attack power of the individual

        if (wOne.getName().equals("Sword")){ //altering attack power, depnding on weapon triangle
            if (wTwo.getName().equals("Lance")){
                attackPower-=1;
            }
            else if(wTwo.getName().equals("Axe")) {
                attackPower+=1;
            }
        }

        else if (wOne.getName().equals("Lance")){ //altering attack power, depnding on weapon triangle
            if (wTwo.getName().equals("Axe")){
                attackPower-=1;
            }
            else if(wTwo.getName().equals("Sword")) {
                attackPower+=1;
            }
        }

        else if (wOne.getName().equals("Axe")){ //altering attack power, depnding on weapon triangle
            if (wTwo.getName().equals("Sword")){
                attackPower-=1;
            }
            else if(wTwo.getName().equals("Lance")) {
                attackPower+=1;
            }
        }

        //checks for critical hit

        boolean critActivate=false; //will turn true if crit is activated, and will do twice the damage

        for (int i=0; i<cOne.getSkills().size();i++){
            if(cOne.getSkills().get(i).equals("Critical")){ //checks if there is the critical skill
                Random critChance= new Random();
                int critical=critChance.nextInt(8);
                if (critical==1) critActivate = true; //multiplies damage by 2, when critical occurs
            }
        }

        //will reduce , if shield skill is present and activated

        for (int i=0; i<cTwo.getSkills().size();i++){
            if(cOne.getSkills().get(i).equals("Critical")){ //checks if there is the critical skill
                Random shieldChance= new Random();
                int shield=shieldChance.nextInt(8);
                if (shield==1) attackPower -= 10; //multiplies damage by 2, when critical occurs
            }
        }

        //actual calculations of damage; attack power subtracted by defence

        if (cOne.getType().equals("")){ //attack power subtracts by magical defence
            if (critActivate==true) attackPower= (attackPower-cTwo.getMDefence())*2;
            else attackPower -=cTwo.getMDefence();
        }
        else{ //attack power subtracts by physical defence
            if (critActivate==true) attackPower= (attackPower-cTwo.getDefence())*2;
            else attackPower -= cTwo.getDefence();
        }


        //checks if damage dealt is less than 0

        if (attackPower<=0){
            return 0; //prevents characters from dealing negative damage
        }
        else return attackPower; //returns the damage done, which will be inputed into different methods

    }

    //Accuracy calculations; determines whether character will hit or not

    public boolean hitMiss(Character cOne, Character cTwo, Weapons wOne, Weapons wTwo){
	    int slowDown=cTwo.getBuild()-wTwo.getWeight();
	    if (slowDown>=0) slowDown=0;
        //determining the hit
        int hitRate= (wOne.getHit()+(cOne.getSkill()*2))-(slowDown*3);

        if (hitRate>100){
            hitRate=100; //ensures hit doesn't go over limit
        }
        if (hitRate<0){
            hitRate=-1; //ensures hit doesn't go under the limit
        }

        Random hitChance= new Random();
        int hit=hitChance.nextInt(100);
        if (hit<=hitRate){ //if it falls within range they will hit
            return true;
        }
        else return false; //if it doesn't fall within range they will not hit

    }

}

class Character{
	
	//declaring all the character stats and variables
	
	private String Name; //determines name of character
	private ArrayList<String> Skills= new ArrayList<String>(); //skills help determine special abilities
	private ArrayList<String> WeaponRanks= new ArrayList<String>(); //determines weapons usable, and weapon skills
	private ArrayList<String> WeaponTypes= new ArrayList<String>(); //determines types of weapons usable
	
	private int HPMAX; //health points
	private int STR; //strength, combines with might of weapon in order to determine attack power
	private int MAG; //magic, combines with might of magic tome in order to determine attack power
	private int SKL; //skill, helps determine hit rate
	private int SPD; //speed, will help determine avoidance
	private int DEF; //defense; attack power subtracted by defense to determine damage done to character
	private int MDEF; //magical defense; magic power subtracted by this to determine damage done to character
	private int BLD; //build; the bigger the build, bigger weapons can be used without speed loss
	
	private int WPNTYPES; //number that determines how many types of weapons can be used
	private int SKLNUM; //number of skills a character has
	
	private String Type; //determines whether they're magical or physical fighters
	
	private int EXP; //experience that will determine level ups
	
	private int HP; //will be the variable that is changed
	
	public Character(String characterStat) { //constructor for character class
		
		String[] data = characterStat.split(",");
		
		this.Name=data[0]; //assigning values and initializing variables
		this.HPMAX=Integer.parseInt(data[1]);
		this.STR=Integer.parseInt(data[2]);
		this.MAG=Integer.parseInt(data[3]);
		this.SKL=Integer.parseInt(data[4]);
		this.SPD=Integer.parseInt(data[5]);
		this.DEF=Integer.parseInt(data[6]);
		this.MDEF=Integer.parseInt(data[7]);
		this.BLD=Integer.parseInt(data[8]);
		this.WPNTYPES=Integer.parseInt(data[9]);
		
		for (int i=0;i<WPNTYPES;i++) {
			WeaponRanks.add(data[9+i+1]);//adds the ranks accordingly
		}
		
		for (int i=0;i<WPNTYPES;i++) {
			WeaponTypes.add(data[9+WPNTYPES+i+1]); //adds the types of weapons usable
		}
		
		int tempPos=9+(WPNTYPES*2)+1; //will be used to continue, so that calculations to get positions aren't messy
		
		SKLNUM=Integer.parseInt(data[tempPos]); //the number of skills a character has
		
		for (int i=0;i<SKLNUM;i++) {
			Skills.add(data[tempPos+i+1]); //adds the skills that each character has
		}
		
		this.EXP=Integer.parseInt(data[tempPos+SKLNUM+1]);
		this.Type=data[tempPos+SKLNUM+2];
		
		HP=HPMAX; //assigns the original health of a character to the max health
		
	}
	
	//METHODS TO GET ACCESS TO INFORMATION
	
	public String getName() {
		return Name;
	}
	
	public int getHealth() {
		return HP;
	}
	
	public int getHealthMax() {
		return HPMAX;
	}
	
	public int getStrength() {
		return STR;
	}
	
	public int getMagic() {
		return MAG;
	}
	
	public int getSkill() {
		return SKL;
	}
	
	public int getSpeed() {
		return SPD;
	}
	
	public int getDefence() {
		return DEF;
	}
	
	public int getMDefence() {
		return MDEF;
	}
	
	public int getBuild() {
		return BLD;
	}
	
	public String getType() {
		return Type;
	}
	
	public int getExp() {
		return EXP;
	}

    public ArrayList<String> getSkills(){
        return Skills;
    }
	
	//METHODS TO ALTER INFORMATIONS
	
	public void changeHealth(int number, String gainlost) {
		if (gainlost.equals("up")) {
			HP+=number;
			if (HP>HPMAX) {
				HP=HPMAX; //character caps health without going over
			}
		}
		else {
			HP-=number;
			if (HP<0) {
				HP=0; //character dies
			}
		}
	}
	
	public void upHealthMax() { //will boost the health cap for a character during a level up
		HPMAX+=1;
	}
	
	public void changeExp(int additionalExp) {
		EXP+=additionalExp;
	}
	
}

class Weapons{
	
	private String Name; //determines type of weapon
	private String Type; //determines what item its made out of, e.g. silver or iron, or anima and light
	private String weaponRank; //determines rank of weapon required to wield, from E to A
	private int Value; //will determine its value, for sale and buying cost
	private int Weight; //the weight of the weapon, when subtracted from build, if a negative number comes out, it will decrease speed
	
	private int STR; //will be used to determine might of weapon
    private int HIT; //hit rate of the weapon, assists in accuracy
	
	public Weapons(String weaponStat) { //constructor for weapon class
		
		String[] data= weaponStat.split(",");
		
		this.Name=data[0]; //assigning values and initializing variables
		this.Type=data[1];
		this.weaponRank=data[2];
		this.Value=Integer.parseInt(data[3]);
		this.Weight=Integer.parseInt(data[4]);
		this.STR=Integer.parseInt(data[5]);
		this.HIT=Integer.parseInt(data[6]);
		
	}
	
	//METHODS TO GET ACCESS TO INFORMATION
	
	public String getName() { 
		return Name;
	}
	
	public String getType() {
		return Type;
	}
	
	public String getRank() {
		return weaponRank;
	}
	
	public int getValue() {
		return Value;
	}
	
	public int getWeight() {
		return Weight;
	}
	
	public int getMight() {
		return STR;
	}

	public int getHit() {return HIT;}
	
	//METHODS TO ALTER INFORMATIONS
	
}

