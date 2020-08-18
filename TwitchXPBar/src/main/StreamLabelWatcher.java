package main;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.nio.file.StandardWatchEventKinds;
import java.nio.file.WatchEvent;
import java.nio.file.WatchKey;
import java.nio.file.WatchService;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

public class StreamLabelWatcher {

	private static final String followersFileName = "total_follower_count.txt";
	private static final String subscribersFileName = "total_subscriber_count.txt";

	private File followersFile;
	private File subscribersFile;

	private String streamLabelsDirectoryPath;

	private  WatchService watchService;

	private XPBarFrame xpBarFrame;

	private boolean running = true;

	private AtomicBoolean valueHasChanged = new AtomicBoolean();
	private Integer followers = Integer.valueOf(0);
	private AtomicInteger newFollowers = new AtomicInteger();
	private Integer subscribers = Integer.valueOf(0);
	private AtomicInteger newSubscribers = new AtomicInteger();

	private Integer score = Integer.valueOf(0);
	
	private Thread xpUpdateThread;
	private Thread watcherThread;
	
	public StreamLabelWatcher(String streamLabelsDirectoryPath) {
		this.streamLabelsDirectoryPath = streamLabelsDirectoryPath;
		followersFile = new File(streamLabelsDirectoryPath + "total_follower_count.txt");
		subscribersFile = new File(streamLabelsDirectoryPath + "total_subscriber_count.txt");
		valueHasChanged.set(false);
		
		Runnable xpUpdateRunnable = new Runnable() {
			@Override
			public void run() {
				xpUpdateLoop();
			}
		};
		
		Runnable watcherRunnable = new Runnable() {
			@Override
			public void run() {
				launchWatcher();
			}
		};
		
		Thread xpUpdateThread = new Thread(xpUpdateRunnable);
		Thread watcherThread = new Thread(watcherRunnable);
		
		xpUpdateThread.start();
		watcherThread.start();
	}

	public void launchWatcher() {
		final Path path = FileSystems.getDefault().getPath(streamLabelsDirectoryPath);

		System.out.println(path);

		try {
			watchService = FileSystems.getDefault().newWatchService();
			path.register(watchService, StandardWatchEventKinds.ENTRY_MODIFY);
			while (running) {
				final WatchKey wk = watchService.take();
				for (WatchEvent<?> event : wk.pollEvents()) {

					//we only register "ENTRY_MODIFY" so the context is always a Path.
					final Path changed = (Path) event.context();
					if (changed.endsWith(followersFileName)) {
						handleFollowersUpdate();
					}
					if (changed.endsWith(subscribersFileName)) {
						handleSubscribersUpdate();
					}
				}
				// reset the key
				boolean valid = wk.reset();
				if (!valid) {
					System.out.println("Key has been unregistered");
				}
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
		}
	}

	public void stop() {
		running = false;
		try {
			watchService.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		try {
			xpUpdateThread.join();
			watcherThread.join();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

	public void registerGui(XPBarFrame xpBarFrame) {
		this.xpBarFrame = xpBarFrame;
		
		//Do initial counts when gui is registered
		handleFollowersUpdate();
		handleSubscribersUpdate();
	}

	private void handleFollowersUpdate() {

		try {
			BufferedReader reader = new BufferedReader (new FileReader(followersFile));
			try {
				String string = reader.readLine();
				newFollowers.set(Integer.parseInt(string));
				handleChange();
			} catch (IOException | NumberFormatException e) {
				// TODO Auto-generated catch block
				//e.printStackTrace();
			}

			reader.close();

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void handleSubscribersUpdate() {
		try {
			BufferedReader reader = new BufferedReader (new FileReader(subscribersFile));
			try {
				newSubscribers.set(Integer.parseInt(reader.readLine()));
				handleChange();
			} catch (IOException | NumberFormatException e) {
				// TODO Auto-generated catch block
				// e.printStackTrace();
			}

			reader.close();

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void handleChange() {
		valueHasChanged.set(true);
	}

	private void xpUpdateLoop() {

		while(running) {

			try {

				while(valueHasChanged.get() == false) {
					Thread.sleep(2000);
				}

				valueHasChanged.set(false);
				
				Integer tempFollowers = newFollowers.get();
				Integer tempSubscribers = newSubscribers.get();
				
				Integer followDiff = tempFollowers - followers;
				Integer subscriberDiff = tempSubscribers - subscribers;
				
				followers = tempFollowers;
				subscribers = tempSubscribers;
				
				score += followDiff + (subscriberDiff * 10);
				
				xpBarFrame.updateXp(score);
				
			}  catch (InterruptedException exception) {

			}
		}

	}
}
