package com.life.sailingplugin;

import java.util.Iterator;
import java.util.TimerTask;

public class TimeTask extends TimerTask {
	
	public TimeTask() {
		
	}

	@Override
	public void run() {
		Iterator<SailingState> iterator = Main.getInstance().getSailingStateMap().values().iterator();	
		while(iterator.hasNext()) {
			SailingState state = iterator.next();
			state.update();
			if(state.getPlayer() == null && state.getFuel() <= 0 && state.getCooltime() <= 0) {
				iterator.remove();
			}
		}
	}
}
