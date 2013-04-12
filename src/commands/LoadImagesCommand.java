package commands;

import main.BZEclipse;

public class LoadImagesCommand implements Command {

	BZEclipse bz;
	public LoadImagesCommand(BZEclipse bz){
		this.bz = bz;
	}
	@Override
	public void execute() {
		bz.stopSim();
		bz.loadImageFrom();
		bz.loadImageTo();
	}

	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return "LoadImages";
	}

}
