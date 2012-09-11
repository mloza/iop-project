package gui;

import java.util.*;

public class Camera implements FrameObservable {
	List<FrameObserver> observers = new ArrayList<FrameObserver>();
	
	
	@Override
	public void addListener(FrameObserver observer) {
		observers.add(observer);
	}

	@Override
	public void removeListener(FrameObserver observer) {
		observers.remove(observer);
	}

	@Override
	public void notifyListeners() {
		for(FrameObserver observer : observers) {
			observer.update();
		}
	}
}
