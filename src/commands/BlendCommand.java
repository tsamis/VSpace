package commands;

import main.BZEclipse;
import model.BZGrid.CreationMethod;

public class BlendCommand implements Command {

	BZEclipse bz;
	public BlendCommand(BZEclipse bz){
		this.bz = bz;
	}
	
	@Override
	public void execute() {
		bz.space.fadeYourself(bz.axis);
	}

	@Override
	public String getName() {
		return "Blend Command";
	}

}
