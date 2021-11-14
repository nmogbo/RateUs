public class RateUs  {

    public static void main(String args[]) throws Exception {

        Documents dp = new Documents();
            dp.readReview();
            dp.readStopWords();
            dp.calculate();
            dp.isEqual();
    }

}


