package com.ikhokha.techcheck;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Main {

	public static void main(String[] args) {
		
		Map<String, Integer> totalResults = new HashMap<>();

		File docPath = new File("docs");
		File[] commentFiles = docPath.listFiles((d, n) -> n.endsWith(".txt"));

		/**
		 * initiating array of threads
		 *
		 * */
		int numberOfThreads = 2;
		Thread[] threads = new Thread[numberOfThreads];


		int loopController = 0;
		for(int iterateThread = 0; iterateThread < numberOfThreads; iterateThread++){

			List<File> filePerThread = new ArrayList<>();

			for (int iterateFiles = loopController; iterateFiles < commentFiles.length; iterateFiles++){

				filePerThread.add(commentFiles[iterateFiles]);
				if(iterateFiles % numberOfThreads -1 == 0){
					loopController = ++iterateFiles;
					iterateFiles = commentFiles.length;
				}
			}

			threads[iterateThread] = new Thread(){
				@Override
				public void run() {}
			};

			processFiles(filePerThread, totalResults);
		}

		startThreads(threads);
		System.out.println("RESULTS\n=======");
		totalResults.forEach((k,v) -> System.out.println(k + " : " + v));
	}
	
	/**
	 * This method processes the files
	 * */
	private static void processFiles(List<File> files, Map<String, Integer> totalResults)
	{
		for (File commentFile : files) {

			CommentAnalyzer commentAnalyzer = new CommentAnalyzer(commentFile);
			Map<String, Integer> fileResults = commentAnalyzer.analyze();
			addReportResults(fileResults, totalResults);

		}
	}

	private static void startThreads(Thread[] threads) throws InterruptedException {
		for (Thread thread: threads ) {
			thread.start();
		}
	}

	/**
	 * This method adds the result counts from a source map to the target map
	 * @param source the source map
	 * @param target the target map
	 */
	private static void addReportResults(Map<String, Integer> source, Map<String, Integer> target) {

		for (Map.Entry<String, Integer> entry : source.entrySet()) {
			if(target.containsKey(entry.getKey())){

				target.put(entry.getKey(), target.get(entry.getKey()) + entry.getValue());

			}
			else{

				target.put(entry.getKey(), entry.getValue());

			}
		}

	}

}
