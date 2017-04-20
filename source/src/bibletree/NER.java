package bibletree;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Properties;
import java.util.Scanner;

import edu.stanford.nlp.ling.CoreAnnotations.NamedEntityTagAnnotation;
import edu.stanford.nlp.ling.CoreAnnotations.SentencesAnnotation;
import edu.stanford.nlp.ling.CoreAnnotations.TextAnnotation;
import edu.stanford.nlp.ling.CoreAnnotations.TokensAnnotation;
import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.pipeline.Annotation;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import edu.stanford.nlp.util.CoreMap;

public class NER {

	public static void main(String[] args) throws IOException {
		// creates a StanfordCoreNLP object, with POS tagging, lemmatization,
		// NER
		Properties props = new Properties();
		props.setProperty("annotators", "tokenize, ssplit, pos, lemma, ner");
		StanfordCoreNLP pipeline = new StanfordCoreNLP(props);

		// read some text in the text variable
		File file = new File("../data/bibletest.txt");
		String text = "";
		Scanner scanner = new Scanner(file);
		text = scanner.useDelimiter("\\Z").next();
		scanner.close();

		// create an empty Annotation just with the given text
		Annotation document = new Annotation(text);

		// run all Annotators on this text
		pipeline.annotate(document);

		// these are all the sentences in this document
		// a CoreMap is essentially a Map that uses class objects as keys and
		// has values with custom types
		List<CoreMap> sentences = document.get(SentencesAnnotation.class);
		List<String> names = new ArrayList<String>();

		for (CoreMap sentence : sentences) {
			// traversing the words in the current sentence
			// a CoreLabel is a CoreMap with additional token-specific methods
			for (CoreLabel token : sentence.get(TokensAnnotation.class)) {
				String ne = token.get(NamedEntityTagAnnotation.class);

				if (ne.startsWith("PERSON")) {
					String word = token.get(TextAnnotation.class);

					boolean startsWithLower = word.substring(0, 1) == word.substring(0, 0).toLowerCase();
					String formattedWord = word.substring(0, 1) + word.substring(1).toLowerCase();

					if (!startsWithLower && !names.contains(formattedWord)) {
						names.add(formattedWord);
					}
				}
			}
		}
		
		Collections.sort(names);
		
		FileWriter fw = new FileWriter("../data/out.txt");
		for (String name : names) {
			fw.write(name + "\r\n");
		}
		fw.close();
	}
}