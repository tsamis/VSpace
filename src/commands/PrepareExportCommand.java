package commands;

import main.BZEclipse;

public class PrepareExportCommand implements UndoableCommand {
	
	BZEclipse bz;
	float[] camrotations;
	double camdistance;
	boolean showSurface;
	boolean showVox;
	double id;
	public PrepareExportCommand(BZEclipse bz) {
		this.bz = bz;
		this.id = Math.random();
	}

	@Override
	public void execute() {
	    //First we save state, so we can revert it when export is done
		camrotations = bz.cam.getRotations();
		camdistance = bz.cam.getDistance();
	    showSurface = bz.showSurface;
	    showVox		= bz.showVox;
		
		//Then we actually prepare for export.
		bz.cam.setRotations(0,0,0);
	    bz.cam.setDistance(0);
	    bz.showSurface=true;
	    bz.showVox=false;
	    bz.exportIt=true;
	    bz.addCleanupCommand(this);
	}

	@Override
	public String getName() {
		return "Prepare for export: "+this.id;
	}

	@Override
	public void undo() {
		//We reset the values
		bz.cam.setRotations(camrotations[0], camrotations[1], camrotations[2]);
	    bz.cam.setDistance(camdistance);
	    bz.showSurface=this.showSurface;
	    bz.showVox=this.showVox;
	    bz.exportIt = false;

	}

}
