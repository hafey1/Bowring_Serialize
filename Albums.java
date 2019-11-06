package main.java.serializeAndDeserialize;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class Albums implements Comparable {
	//this is the state of the class
	// every object must have an album entry
	// would like to compare by rating, but comparisons by album are guaranteed
	private String album;
	private String artist;
	private Integer year;
	private Integer rating;

	public Albums() {}

	//Constructors must have at least an album
	public Albums(String albumPara) {
		album = albumPara;
	}


	public Albums(String albumPara, String artistPara) {
		this.setAlbum(albumPara);
		this.setArtist(artistPara);
	}



	// the method should take in an object and a path
	// the path must specifically be a csv or it needs to return false
	// this function needs an album object, a string relative path name, and a true or false to set append or create a new file
	// we need to take the first two elements of each line and see if they are equal to the string we are trying to add
	private static boolean dupCheckerForSerializing(String albumEntry, String csvFile) throws IOException {

			// take the given string and compare the strings that are split by a comma and compare them all of the lines in the csv
			// if they are equal to anything return false so we do not add it to the file
			String[] albumEntryCheck = albumEntry.split(",");
			String EntryCheckAlbumArtist = albumEntryCheck[0] + "," + albumEntryCheck[1];
			Path csvFilePath = Paths.get(csvFile);
			BufferedReader lineReader = Files.newBufferedReader(csvFilePath);
			String thisLine = "";
			while ((thisLine = lineReader.readLine()) != null) {
				String[] albumArtistCheck = thisLine.split(",");
				String albumArtistJoin = albumArtistCheck[0] + "," + albumArtistCheck[1];
				if (EntryCheckAlbumArtist.equals(albumArtistJoin))
					return false;
			}
			return true;
	}

	public static List<Albums> deserializetoCSV(String pathName) {
		//create the path, the line reader, and the data structure for the resulting objects
		try {
			Path incomingPath = Paths.get(pathName);
			BufferedReader csvLineReader = Files.newBufferedReader(incomingPath);
			List<Albums> deserializedAlbums = new ArrayList<>();

			//write to a string, split up the phrases by commas
			//first is album name, second is artist name
			//third is year released, fourth is personal rating
			String currLine = "";
			while ((currLine = csvLineReader.readLine()) != null) {

				String[] albumInfoSplit = currLine.split(",");

				//every line must have at least one separation
				if (albumInfoSplit.length > 0) {
					//handles if we pass in null values to be parsed, because we don't necessitate year or rating
					Albums deserializeLineToAlbum = new Albums(albumInfoSplit[0], albumInfoSplit[1]);
					if (albumInfoSplit[2].equals("null")) {
						deserializeLineToAlbum.setYear(null);
						if (albumInfoSplit[3] == "null") {
							deserializeLineToAlbum.setRating(null);
						}
					} else if (albumInfoSplit[3] == "null")
						{
							deserializeLineToAlbum.setRating(null);
						}
					else
					{
					deserializeLineToAlbum.setYear(Integer.parseInt(albumInfoSplit[2]));
					deserializeLineToAlbum.setRating(Integer.parseInt(albumInfoSplit[3]));
					}
					deserializedAlbums.add(deserializeLineToAlbum);
				}
			}

			//Print out the album names of the list for preliminary check
			for (Albums e : deserializedAlbums) {
				System.out.println(e.getAlbum());
			}

			csvLineReader.close();

			return deserializedAlbums;
		} catch (IOException e) {
			System.out.println("IO Exception");
			e.printStackTrace();
		}
			return null;
	}

	/**
	 *
	 * @param incToRecords an instance of the class main.java.serializeAndDeserialize.Albums
	 * @param pathName give a relative or absolute path
	 * @param append true is appending to an existing file; false is creating a new file
	 * @throws FileNotFoundException
	 */


	public static void serializetoCSV(Albums incToRecords, String pathName, boolean append) {
		try {//last four characters need to be .csv
			String subPath = pathName.substring(pathName.length() - 4);
			System.out.println(subPath);

			//need to create exception for this, just checks last four characters for .csv
			if (!subPath.equals(".csv")) {
				System.out.println("Error Please enter in a csv file");
				return;
			}


			// this creates the file and either opens a new csv or sets it to append mode
			BufferedWriter serializer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(pathName, append)));
			String csvLine = incToRecords.getAlbum() + "," + incToRecords.getArtist() + "," + incToRecords.getYear() + "," + incToRecords.getRating() + "\n";

			//We do not want to add duplicate entries to the already created files
			if (!dupCheckerForSerializing(csvLine, pathName))
			{System.out.println("Error can not add duplicates");
				return;}

			try {
				serializer.write(csvLine, 0, csvLine.length());
				serializer.close();
			} catch (IOException e) {
				System.out.println("There was an error writing to the file");
			}

		} catch (FileNotFoundException e) {
			System.out.println("The file is not found, please try again");
		} catch (IOException e) {
			System.out.println("Input Output Exception present");
			System.out.println(e.getStackTrace());
		}

	}



	//setter block
	public void setAlbum(String album) {
		this.album = album;
	}
	public void setArtist(String artist) {
		this.artist = artist;
	}
	public void setYear(Integer year) {
		this.year = year;
	}

	public void setRating(Integer rating) {
		this.rating = rating;
	}

	//getter block
	public String getAlbum() {
		return album;
	}
	public String getArtist() {
		return artist;
	}
	public Integer getYear() {
		return year;
	}
	public Integer getRating() {
		return rating;
	}

	//save some lines with string cleaning
	private static String standardize(String unstandardized) {
		String standard = unstandardized.toLowerCase().replace(" ", "");
		return standard;

	}
	@Override
	// -1 means this is less, 0 means equal, 1 means greater
	// Album title should be the first comparison point the second should be if the artists are different
	public int compareTo(Object albumPara) {
		// We want this to be used as an alphabetizer where
		// We want albums to be grouped by artist and then alphabetized
		// if they do not have have the same artist



		return 0;
	}

	@Override
	public boolean equals(Object albumPara) {
		// Equality is determined by if the album and the artist are the same
		//come back to here with a try catch to properly notify with exceptions
		if (!(albumPara instanceof Albums)) {
			System.out.println("Stop this is not an album");
			return false;
		}
		//clean the strings and then compare them
		String normalizedAlbum = standardize(this.getAlbum());
		String comparedAlbum = standardize(((Albums) albumPara).getAlbum());

		String normalizedArtist = standardize(this.getArtist());
		String comparedArtist = standardize(((Albums) albumPara).getArtist());

		// compare the album and the artist names
		if (normalizedAlbum.equals(comparedAlbum) && normalizedArtist.equals(comparedArtist)) {
			System.out.println("These are equal");
			return true;
		}
			System.out.println("These are not equal");
			return false;

	}

}
