package com.github.rnowling.simplejms;

import java.text.SimpleDateFormat;
import java.util.*;

public class CLIDriver
{

	static void dual( String[] args) throws Exception {
		Receiver receiver = new Receiver(args[0], args[1]);
		Sender sender = new Sender(args[0], args[1], args[2]);

		Scanner scanner = new Scanner(System.in);

		while(true)
		{
			String line = scanner.nextLine();

			if(line.equalsIgnoreCase("exit"))
			{
				scanner.close();
				sender.close();
				receiver.close();
				System.exit(0);
			}

			sender.sendMessage(line);
		}
	}


	static void sender( String[] args) throws Exception {
		Sender sender = new Sender(args[0], args[1], args[2]);
		Scanner scanner = new Scanner(System.in);
		while(true)
		{
			String line = scanner.nextLine();

			if(line.equalsIgnoreCase("exit"))
			{
				scanner.close();
				sender.close();
				System.exit(0);
			}
			sender.sendMessage(line);
		}
	}

	static void rec( String[] args) throws Exception {
		System.out.println("Press CTRL+C to quit...");
		Receiver receiver = new Receiver(args[0], args[1]);
		Runtime.getRuntime().addShutdownHook(new Thread() {
			public void run() {
				try {
					Thread.sleep(200);
					System.out.println("Shutting down ...");
					receiver.close();
					//some cleaning up code...

				} catch (Exception e) {
					Thread.currentThread().interrupt();
					e.printStackTrace();
				}
			}
		});
	}

	public static void main(String[] args) throws Exception
	{
		String mode = "d";
		if(args.length < 3)
		{
			System.out.println("Usage: factoryName topicName username [mode=s|r|d]");
			System.exit(1);

		}
		if ( args.length == 4 ){
			mode = args[3];
		}

		switch ( mode ){
			case "d" :
				dual(args);
				break;
			case "s":
				sender(args);
				break;
			case "r":
				rec(args);
				break;
			default:
				System.out.println("Invalid mode arg passed defaulting to dual - d!");
				dual(args);
		}
	}
}
