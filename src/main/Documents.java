import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.HashMap;
import java.text.DecimalFormat;


public class Documents {

    //These variable will hold all terms of each document in an array.
    private List<String> termsStopArray = new ArrayList<String>(); //to hold the terms from the stop words text file into this array
    private List<String> termsRateArray= new ArrayList<String>(); //to hold the terms from the word valence text file
    private List<String[]> termsDocsArray = new ArrayList<String[]>(); //to hold all terms from the review file
    private List<String> allTerms = new ArrayList<String>(); //to hold all terms
    private List<Double> numbersRateArray = new ArrayList<Double>(); //to hold number ratings
    private static final DecimalFormat df = new DecimalFormat("###.##"); //to set the number of decimal places after for the negative and positive scoring


//This method reads in the file of the stop words and adds them to the array
    public void readStopWords() {
        try (FileReader file = new FileReader(".../src/stopWords");
             BufferedReader buffer = new BufferedReader(file)) {
            String words;
            while((words = buffer.readLine()) != null){
                termsStopArray.add(words);
            }
        }catch (IOException e) {

        }
    }

//this function adds the words from the speech-recognition review to be tested
    //the badReview text file and goodReview text file were used in this method to test before the speech-recognition portion of the code was added
    public void readReview() {
        try (FileReader fr = new FileReader(".../voice");//Original text file:/Users/nkem/Desktop/RateUs/src/badReview
             BufferedReader br = new BufferedReader(fr)) {
            StringBuilder fullText = new StringBuilder();
            for (String line = null; (line = br.readLine()) != null; fullText.append(line.toLowerCase())) {
                System.out.println(line);
            }

            //code taken from a computergodzilla.blogspot.com which was originally from the tf-idf code
            //This function splits the sentence into separate elements to be added into the array and also removes duplicate terms
            String[] tokenTerms = fullText.toString().replaceAll("[\\W&&[^\\s]]", "").split("\\W+");
            //(?<=\w)ed\b
            for (String term : tokenTerms) {

                if (!allTerms.contains(term)) {  //avoid duplicate entry
                    allTerms.add(term);
                }
            }
            termsDocsArray.add(tokenTerms);
            //System.out.println(allTerms); //test

        }catch (IOException e) {

        }
    }

//This function compares the terms in the array from the review to the terms in the stop array to take away terms that we do not need to test the word valence of
    public boolean isEqual(){
        termsStopArray.retainAll(allTerms);
        allTerms.removeAll(termsStopArray);
        //System.out.println(allTerms); //test
        return true;
    }

//This method uses a HashMap to store the word and valence from a text file
    //With this HashMap, we compare the keys taken out that are also in the allTerms array and pull the value associated with each
    public void calculate() {

        try {
            String file = ".../MapWords.txt";
            BufferedReader reader = new BufferedReader(new FileReader(file));

            HashMap<String, Double> map = new HashMap<>();

            String line;
            while ((line = reader.readLine()) != null) {
                String[] columns = line.split("\\s+");


                String word = columns[0];
                Double rate = Double.parseDouble(columns[1]);

                map.put(word, rate);

                if (allTerms.contains(word)) {
                    termsRateArray.add(word);
                    numbersRateArray.add(rate);
                }
            }
//Once the values-in this case the rating of the word- are pulled, the rates are added based on value of being classified as positive or negative
            double negativeTotal = 0;
            double positiveTotal = 0;

            for (int i = 0; i < numbersRateArray.size(); i++) {
                if (numbersRateArray.get(i) < 5) {
                    negativeTotal = negativeTotal + numbersRateArray.get(i);
                } else {
                    positiveTotal = positiveTotal + (numbersRateArray.get(i) - 5);
                }
            }
            System.out.println("--------------------------------------------------------------------------");
            System.out.println("The negative scoring for this review was " + df.format(negativeTotal));//check
            System.out.println("The positive scoring for this review was " + df.format(positiveTotal)) ;//check
            System.out.println("--------------------------------------------------------------------------");
//The rate totals are then compared to give the overall output of the review
            if (negativeTotal > positiveTotal) {
                System.out.println("This product review overall was NEGATIVE.");
            } else if (positiveTotal > negativeTotal) {
                System.out.println("This product review overall was POSITIVE.");
            } else {
                System.out.println("This product review was NEITHER positive nor negative.");
            }
            System.out.println("--------------------------------------------------------------------------");
            System.out.println("The key terms of this review were " + termsRateArray);


        } catch (IOException e) {

        }
    }

}

