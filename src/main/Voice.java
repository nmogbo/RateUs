import edu.cmu.sphinx.api.Configuration;
import edu.cmu.sphinx.api.SpeechResult;
import edu.cmu.sphinx.api.StreamSpeechRecognizer;
import java.io.*;

/**
 * This code translates a .wav voice file to text
 * Some of this code was gotten from https://cmusphinx.github.io/wiki/tutorialsphinx4/
 */

public class Voice  {
    public static void main(String[] args) throws Exception{
        //Lines 14-20,24-26 was adapted from the aforementioned source
        Configuration configuration = new Configuration();

        configuration.setAcousticModelPath("resource:/edu/cmu/sphinx/models/en-us/en-us");
        configuration.setDictionaryPath("resource:/edu/cmu/sphinx/models/en-us/cmudict-en-us.dict");
        configuration.setLanguageModelPath("resource:/edu/cmu/sphinx/models/en-us/en-us.lm.bin");

        StreamSpeechRecognizer recognizer = new StreamSpeechRecognizer(configuration);
        //InputStream stream = new FileInputStream(new File("/Users/nkem/Downloads/GudReview.wav"));//Olivia
        InputStream stream = new FileInputStream(new File("/Users/nkem/Downloads/Review.wav"));//Marissa

        //Recognizes speech and converts to text
        String answer = "";
        SpeechResult result;
        recognizer.startRecognition(stream);
        while ((result = recognizer.getResult())!= null) {
          answer+=result.getHypothesis()+" ";// adds result of speech to text conversion to String variable
        }
        recognizer.stopRecognition();

        //Writes text to file
        BufferedWriter write = new BufferedWriter(new FileWriter("/Users/nkem/Downloads/voice"));
        write.write(answer);
        write.close();

        System.out.println(answer);
    }
}
