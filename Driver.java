package main.java.serializeAndDeserialize;

public class Driver {

	public static void main(String[] args) {



		Albums theShins = new Albums("Wincing the Night Away", "The Shins");
		Albums theShinsAgain = new Albums("Chutes Too Narrow", "The Shins");
		Integer three = 3;
		Albums checkTheShins = theShins;


		Albums fooFighters = new Albums("Foo Fighters", "Foo Fighters");

		theShins.equals(three);

		theShins.equals(fooFighters);

		theShins.equals(checkTheShins);

		theShins.equals(theShinsAgain);

		Albums[] albumArray = new Albums[3];
		albumArray[0] = theShins;
		albumArray[1] = fooFighters;
		albumArray[2] = theShinsAgain;


		for(int i = 0; i < albumArray.length; i++){
			Albums.serializetoCSV(albumArray[i], "dreams.csv", true);
		}

		Albums.deserializetoCSV("dreams.csv");

	}
}
