package java_Examples;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Collectors;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class Functional_Program_Ex {

	public static void main(String[] args) throws IOException {

		String url = "https://www.flipkart.com/";

		System.out.println("\nUsing for-each loop:\n");
		retriveLinksUsingForEachLoop(url);

		System.out.println("\nUsing Stream:\n");
		retriveLinksUsingStream(url);

		System.out.println("\nUsing Parallel Stream:\n");
		retriveLinksUsingParallelStream(url);

		System.out.println("\nUsing Lambda Expression:\n");
		retriveLinksUsingLambda(url);

	}

	private static void retriveLinksUsingForEachLoop(String url) throws IOException {

		try {
//			10000, This is the timeout value in milliseconds. 
//			The max amount of time to wait for the connection to be established
			Document doc = Jsoup.parse(new URL(url), 10000);
			Elements links = doc.select("a[href]");

			for (Element link : links) {
				System.out.println(link.attr("abs:href"));
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private static void retriveLinksUsingStream(String url) throws IOException {

		try {
			Document doc = Jsoup.parse(new URL(url), 100000);
			
			List<String> links = doc.select("a[href]").stream().map(link -> link.attr("abs:href"))
					.collect(Collectors.toList());

			links.forEach(System.out::println);

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private static void retriveLinksUsingParallelStream(String url) throws IOException {

		try {
			Document doc = Jsoup.parse(new URL(url), 100000);
			List<String> links = doc.select("a[href]").parallelStream().map(link -> link.attr("abs:href"))
					.collect(Collectors.toList());

			links.forEach(System.out::println);

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private static void retriveLinksUsingLambda(String url) throws IOException {

		try {
			Document doc = Jsoup.parse(new URL(url), 100000);
			Elements links = doc.select("a[href]");

			Consumer<Element> printLink = link -> System.out.println(link.attr("abs:href"));
			links.forEach(printLink);

		} catch (IOException e) {
			e.printStackTrace();
		}

	}
}
