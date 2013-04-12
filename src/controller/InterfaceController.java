package controller;





import main.BZEclipse;
import processing.core.*;

import controlP5.*;

public class InterfaceController{

	ControlWindow w;
	ControlP5 controlP5;
	public Slider xlow;
	public Slider ylow;
	public Slider zlow;
	public Slider xhi;
	public Slider yhi;
	public Slider zhi;
	public Textfield tf_dimx;
	public Textfield tf_dimy;
	public Textfield tf_dimz;

	Button button_play;
	Button button_2;
	Button button_setCameraTop;
	Button button_setCameraBottom;
	Button button_setCameraFront;
	Button button_9;
	Button button_10;
	Button button_11;
	Button button_slabs;
	Button button_13;
	Button button_creation3grid;
	Button button_creation3gridrandom;
	Button button_creationTrigWaves;
	Button button_creationRandom;
	Button button_creationImages;
	Button button_applyBoolean;
	public Slider paintopacityslider;
	public Slider paintsizeslider;
	Button button_step;
	public CheckBox airbrush;
	Button button_19;
	Button button_20;
	Button button_21;
	Button button_blend;
	Button button_23;
	Button button_24;
	Button button_25;
	Button button_26;
	Button button_27;
	Button button_28;
	Button button_29;
	Button button_30;
	Button button_32;
	Button button_33;
	Button button_34;
	Button button_35;
	Button button_36;
	Button button_41;
	Button button_42;
	public ControlGroup group_creationButtons,group_simOpts,group_visOpts,group_operations,group_analysis;
	
	Button button_booleanOff;
	
	Button button_diffusion;//Diffusion
	Button button_composite;

	public CheckBox checkbox_surface,checkbox_color_surface,checkbox_trim,checkbox_pick,checkbox_difanalysis,checkbox_offset;
	public RadioButton radio_show_vox,radio_trimA,radio_trimB,radio_trimC;

	Slider surfaceA_slider;
	Slider surfaceB_slider;
	Slider surfaceC_slider;
	
	Slider trimA_slider;
	Slider trimB_slider;
	Slider trimC_slider;
	public CheckBox cb_mode;
	public Slider slider_skip;
	public ColorPicker cpick;
	public boolean enableAirbrush=false;
	public boolean exportNextIteration=false;
	Slider offset_slider;
	
	public DropdownList ddl_boolean_abc1, ddl_boolean_operator,ddl_boolean_abc2,ddl_boolean_surforprop,ddl_reaction;
	public Textlabel debugLabel,booleanLabel,pickerLabel;
	BZEclipse p;
	private Slider slider_avg;
	private Slider slider_speed;
	private Slider slider_speeddecs;
	private Controller<Slider> slider_gs_k;
	private Slider slider_gs_f;
	private Slider slider_gs_b;
	private Slider slider_gs_a;
	public Slider slider_gs_noise;
	private Button button_creategs;
	private Button button_createfill;
	/**
	 * @wbp.parser.entryPoint
	 */
	public InterfaceController(BZEclipse p){
		this.p = p;
		controlP5 = p.controlP5;
		
		setup(p);
	}
	
	/**
	 * @wbp.parser.entryPoint
	 */
	@SuppressWarnings("deprecation")
	private void setup(BZEclipse p){
		int sub1y=285;
		int basey2=185;
		int surfaceconfig_posy = basey2 + 70;
		int surfaceconfig_posx = 20;
		w = controlP5.addControlWindow("controlP5window", p.width+20, 20, 320, p.height);
		w.hideCoordinates();
		w.setBackground(p.color(40));
		w.setTitle("Options");
		w.setUpdateMode(ControlWindow.NORMAL); 
		controlP5.setAutoDraw(false); 

		/*
		 * Misc
		 */
		
		group_analysis = controlP5.addGroup("Misc", 20, 600);
		group_analysis.moveTo(w);
		cpick = controlP5.addColorPicker("Le_Pickeur",15,5+11*13,100,50);
		cpick.moveTo(group_analysis);
		
		checkbox_pick = controlP5.addCheckBox("checkbox_pick_press", 10,5+13);
		checkbox_pick.addItem("Enable Picker", 1);
		checkbox_pick.moveTo(group_analysis);
		
		checkbox_difanalysis = controlP5.addCheckBox("checkbox_difanalysis_press", 10,5+2*13);
		checkbox_difanalysis.addItem("Enable Difusion Analysis", 1);
		checkbox_difanalysis.moveTo(group_analysis);
		
		debugLabel = controlP5.addTextlabel("LabelDebug", "aaaaa",10, 5+3*13);
		debugLabel.moveTo(group_analysis);
		pickerLabel = controlP5.addTextlabel("LabelPicker", "aaaaa",10, 5+4*13);
		pickerLabel.moveTo(group_analysis);
		
		paintopacityslider= controlP5.addSlider("paintopacityslider_changed",0,1,5,5+5*13,100,20);
		paintopacityslider.setLabel("Paint Opacity");
		paintopacityslider.setDecimalPrecision(2);
		paintopacityslider.setValue(0.8f);
		paintopacityslider.moveTo(group_analysis);
		
		paintsizeslider= controlP5.addSlider("paintsizeslider_changed",1,10,5,5+7*13,100,20);
		paintsizeslider.setLabel("Paint Size");
		paintsizeslider.setDecimalPrecision(0);
		paintsizeslider.setValue(1);
		paintsizeslider.moveTo(group_analysis);
		
		airbrush = controlP5.addCheckBox("airbrushcheckbox",5,5+9*13);
		airbrush.addItem("Airbrush", 1);
		airbrush.moveTo(group_analysis);
		
		/*
		 * CAMERA BUTTONS
		 */
		button_setCameraTop=controlP5.addButton("setCameraTop", 4, 0, 0, 20, 20);
		button_setCameraTop.setLabel("TOP");
		button_setCameraTop.moveTo(w);

		button_setCameraBottom=controlP5.addButton("setCameraBottom", 4, 20, 0, 20, 20);
		button_setCameraBottom.setLabel("BOT");
		button_setCameraBottom.moveTo(w);

		button_setCameraFront=controlP5.addButton("setCameraFront", 4, 40, 0, 20, 20);
		button_setCameraFront.setLabel("FR");
		button_setCameraFront.moveTo(w);

		button_9=controlP5.addButton("setCameraBack", 4, 60, 0, 20, 20);
		button_9.setLabel("BK");
		button_9.moveTo(w);

		button_10=controlP5.addButton("setCameraLeft", 4, 80, 0, 20, 20);
		button_10.setLabel("LE");
		button_10.moveTo(w);

		button_11=controlP5.addButton("setCameraRight", 4, 100, 0, 20, 20);
		button_11.setLabel("RI");
		button_11.moveTo(w);
		
		
		
		/*
		 * CREATION CONTROLS
		 */

		group_creationButtons = controlP5.addGroup("Creation", 20, 40);
		group_creationButtons.moveTo(w);
		Slider slider_wDx = controlP5.addSlider("wDxSlider", 2, 100, 125, 0, 20, 100);
		slider_wDx.setDecimalPrecision(0);
		slider_wDx.setValue(p.wDx);
		slider_wDx.setLabel("");
		slider_wDx.moveTo(group_creationButtons);
		
		
		
		Slider slider_wDy = controlP5.addSlider("wDySlider", 2, 100, 175, 0, 20, 100);
		slider_wDy.setDecimalPrecision(0);
		slider_wDy.setValue(p.wDy);
		slider_wDy.setLabel("");
		slider_wDy.moveTo(group_creationButtons);


		Slider slider_wDz = controlP5.addSlider("wDzSlider", 2, 100, 225, 0, 20, 100);
		slider_wDz.setDecimalPrecision(0);
		slider_wDz.setValue(p.wDz);
		slider_wDz.setLabel("");
		slider_wDz.moveTo(group_creationButtons);
		
		//Put them here at 120
		
		tf_dimx = controlP5.addTextfield("dimx_changed",125,105,30,12);
		tf_dimy = controlP5.addTextfield("dimy_changed",175,105,30,12);
		tf_dimz = controlP5.addTextfield("dimz_changed",225,105,30,12);
		
		
		tf_dimx.moveTo(group_creationButtons);
		tf_dimy.moveTo(group_creationButtons);
		tf_dimz.moveTo(group_creationButtons);
		tf_dimx.setText(""+(int)slider_wDx.getValue());
		tf_dimy.setText(""+(int)slider_wDy.getValue());
		tf_dimz.setText(""+(int)slider_wDz.getValue());
		
		tf_dimx.setLabel("dimX");
		tf_dimy.setLabel("dimY");
		tf_dimz.setLabel("dimZ");
		
		button_slabs = controlP5.addButton("button_slabs_pressed", 4, 10, 0, 80, 20);
		button_slabs.setLabel("Slabs");
		button_slabs.moveTo(group_creationButtons);

		button_creation3grid = controlP5.addButton("button_creation3grid_pressed", 4, 10, 30, 80, 20);
		button_creation3grid.setLabel("3x3x3 Grid");
		button_creation3grid.moveTo(group_creationButtons);

		button_creation3gridrandom = controlP5.addButton("button_creation3gridrandom_pressed", 4, 10, 55, 80, 20);
		button_creation3gridrandom.setLabel("Random Grid");
		button_creation3gridrandom.moveTo(group_creationButtons);

		button_creationTrigWaves = controlP5.addButton("button_creationTrigWaves_pressed", 4, 10, 80, 80, 20);
		button_creationTrigWaves.setLabel("Trig Waves");
		button_creationTrigWaves.moveTo(group_creationButtons);

		button_creationRandom = controlP5.addButton("button_creationRandom_pressed", 4, 10, 105, 80, 20);
		button_creationRandom.setLabel("Random");
		button_creationRandom.moveTo(group_creationButtons);
		
		
		button_composite = controlP5.addButton("button_composite_pressed", 4, 10, 130, 80, 20);
		button_composite.setLabel("Composite");
		button_composite.moveTo(group_creationButtons);


		button_creationImages = controlP5.addButton("button_creationImages_pressed", 4, 10, 155, 80, 20);
		button_creationImages.setLabel("Load Images");
		button_creationImages.moveTo(group_creationButtons);
		button_blend = controlP5.addButton("button_blend_pressed", 4,95,155, 60, 20);
		button_blend.setLabel("Blend");
		button_blend.moveTo(group_creationButtons);
		
		button_creategs = controlP5.addButton("button_creategs_pressed", 4,95,130, 60, 20);
		button_creategs.setLabel("RandomGs");
		button_creategs.moveTo(group_creationButtons);
		
		button_createfill = controlP5.addButton("button_createfill_pressed", 4,190,155, 60, 20);
		button_createfill.setLabel("Fill");
		button_createfill.moveTo(group_creationButtons);
		/*
		 * Visualization Controls
		 */
		group_visOpts = controlP5.addGroup("Visualization", 20, 340);
		group_visOpts.moveTo(w);
		
		Textlabel label_surfaceShow = controlP5.addTextlabel("LabelText1", "S", 10, 5);
		label_surfaceShow.moveTo(group_visOpts);
		Textlabel label_materialShow = controlP5.addTextlabel("LabelText2", "M", 10+15*1, 5);
		label_materialShow.moveTo(group_visOpts);
		Textlabel label_surfaceTrim = controlP5.addTextlabel("LabelText3", "T", 10+15*2, 5);
		label_surfaceTrim.moveTo(group_visOpts);
		Textlabel label_A = controlP5.addTextlabel("LabelText4", "A", 10+15*3, 5);
		label_A.moveTo(group_visOpts);
		Textlabel label_B = controlP5.addTextlabel("LabelText5", "B", 10+15*4, 5);
		label_B.moveTo(group_visOpts);
		Textlabel label_C = controlP5.addTextlabel("LabelText6", "C", 10+15*5, 5);
		label_C.moveTo(group_visOpts);
		
		checkbox_surface = controlP5.addCheckBox("surfaces_show", 10,5+13);
		checkbox_surface.addItem("a1",1).setLabel(" ");
		checkbox_surface.addItem("b1",2).setLabel(" ");
		checkbox_surface.addItem("c1",3).setLabel(" ");
		checkbox_surface.moveTo(group_visOpts);
		checkbox_surface.activate(0);
		checkbox_surface.hideLabels();
		checkbox_color_surface = controlP5.addCheckBox("surfaces_color", 10+15,5+13);
		checkbox_color_surface.addItem("a2",4).setLabel(" ");
		checkbox_color_surface.addItem("b2",5).setLabel(" ");
		checkbox_color_surface.addItem("c2",6).setLabel(" ");
		checkbox_color_surface.moveTo(group_visOpts);
		checkbox_color_surface.hideLabels();
		
		radio_show_vox = controlP5.addRadioButton("radio_show_vox", 10+15*3, 5+13*4);	
		radio_show_vox.setItemsPerRow(3);
		radio_show_vox.addItem("a3",6).setLabel("");
		radio_show_vox.addItem("b3",6).setLabel("");
		radio_show_vox.addItem("c3",6).setLabel("");
		radio_show_vox.moveTo(group_visOpts);
		radio_show_vox.hideLabels();
		
		checkbox_trim = controlP5.addCheckBox("surfaces_trim", 10+15*2,5+13);
		checkbox_trim.addItem("aTrim",4).setLabel(" ");
		checkbox_trim.addItem("bTrim",5).setLabel(" ");
		checkbox_trim.addItem("cTrim",6).setLabel(" ");
		checkbox_trim.moveTo(group_visOpts);
		
		checkbox_trim.hideLabels();
		
		surfaceA_slider = controlP5.addSlider("surfaceA_slider_change", 0f, 1f, 10+100, 5+13, 80, 10);
		surfaceA_slider.setLabel("Boundary A");
		surfaceA_slider.setValue(0.5f);
		surfaceA_slider.setValue(1);
		surfaceA_slider.moveTo(group_visOpts);

		surfaceB_slider = controlP5.addSlider("surfaceB_slider_change", 0f, 1f, 10+100, 5+13*2, 80, 10);
		surfaceB_slider.setLabel("Boundary B");
		surfaceB_slider.setValue(0.5f);
		surfaceB_slider.moveTo(group_visOpts);

		surfaceC_slider = controlP5.addSlider("surfaceC_slider_change",  0f, 1f, 10+100, 5+13*3, 80, 10);
		surfaceC_slider.setLabel("Boundary C");
		surfaceC_slider.setValue(0.5f);
		surfaceC_slider.moveTo(group_visOpts);
		
		
		Slider slider_1=controlP5.addSlider("voxThresholdSlider", 0f, 1f, 110, 5+13*4, 80, 10);
		slider_1.setValue(p.voxthreshold);
		slider_1.setDecimalPrecision(3);
		slider_1.setLabel("Property Concentration");
		slider_1.moveTo(group_visOpts);
		
		
		Slider proplow=controlP5.addSlider("voxThreshold2Slider", 0f, 1f, 110, 5+13*5, 80, 10);
		proplow.setValue(p.voxthreshold);
		proplow.setDecimalPrecision(3);
		proplow.setLabel("PropertySubConcentration");
		proplow.setValue(0);
		proplow.moveTo(group_visOpts);
		
		xlow=controlP5.addSlider("xlowslider", 0f, 1f, 15, 5+13*6, 80, 10);
		xlow.setValue(p.voxthreshold);
		xlow.setDecimalPrecision(3);
		xlow.setLabel("X");
		xlow.setValue(0);
		xlow.moveTo(group_visOpts);
		
		ylow=controlP5.addSlider("ylowslider", 0f, 1f, 15, 5+13*7, 80, 10);
		ylow.setValue(p.voxthreshold);
		ylow.setDecimalPrecision(3);
		ylow.setLabel("Y");
		ylow.setValue(0);
		ylow.moveTo(group_visOpts);
		
		zlow=controlP5.addSlider("zlowslider", 0f, 1f, 15, 5+13*8, 80, 10);
		zlow.setValue(p.voxthreshold);
		zlow.setDecimalPrecision(3);
		zlow.setLabel("Z");
		zlow.setValue(0);
		zlow.moveTo(group_visOpts);
		xhi=controlP5.addSlider("xhislider", 0f, 1f, 110, 5+13*6, 80, 10);
		xhi.setValue(p.voxthreshold);
		xhi.setDecimalPrecision(3);
		xhi.setLabel("");
		xhi.setValue(1);
		xhi.moveTo(group_visOpts);
		
		yhi=controlP5.addSlider("yhislider", 0f, 1f, 110, 5+13*7, 80, 10);
		yhi.setValue(p.voxthreshold);
		yhi.setDecimalPrecision(3);
		yhi.setLabel("");
		yhi.setValue(1);
		yhi.moveTo(group_visOpts);
		
		zhi=controlP5.addSlider("zhislider", 0f, 1f, 110, 5+13*8, 80, 10);
		zhi.setValue(p.voxthreshold);
		zhi.setDecimalPrecision(3);
		zhi.setLabel("");
		zhi.setValue(1);
		zhi.moveTo(group_visOpts);
		
		


		
		/*
		 * Playback and simulation controls
		 *
		 */
		
		group_simOpts = controlP5.addGroup("Simulation", 20, 250);
		group_simOpts.moveTo(w);
		
		
		
		slider_avg = controlP5.addSlider("slider_avg_change", 0, 1, 20, 10+11*1, 200, 10);
		slider_avg.setDecimalPrecision(3);
		slider_avg.setValue(0);
		slider_avg.setLabel("AVG<->NEW");
		slider_avg.moveTo(group_simOpts);
		
		slider_speed = controlP5.addSlider("Speed", -100, 100, 20, 10+11*3, 200, 10);
		slider_speed.setDecimalPrecision(0);
		slider_speed.setValue(1);
		slider_speed.moveTo(group_simOpts);

		slider_speeddecs = controlP5.addSlider("SpeedDecimals", -1, 1, 20, 10+11*4, 200, 10);
		slider_speeddecs.setDecimalPrecision(3);
		slider_speeddecs.setValue(0);
		slider_speeddecs.moveTo(group_simOpts);
		
		cb_mode = controlP5.addCheckBox("cb_mode", 20,20+11);	
		cb_mode.addItem("enableavg",0).setLabel("Average before reacting");
		cb_mode.moveTo(group_simOpts);
		
		slider_gs_a = controlP5.addSlider("GrayScottARate", 0, 1, 20, 10+11*1, 200, 10);
		slider_gs_a.setDecimalPrecision(3);
		slider_gs_a.setValue(p.paramDu);
		slider_gs_a.setVisible(false);
		slider_gs_a.moveTo(group_simOpts);
		
		slider_gs_b = controlP5.addSlider("GrayScottBRate", 0, 1, 20, 10+11*2, 200, 10);
		slider_gs_b.setDecimalPrecision(3);
		slider_gs_b.setValue(p.paramDv);
		slider_gs_b.setVisible(false);
		slider_gs_b.moveTo(group_simOpts);
		
		slider_gs_f = controlP5.addSlider("GrayScottF", 0, 1, 20, 10+11*3, 200, 10);
		slider_gs_f.setDecimalPrecision(3);
		slider_gs_f.setValue(p.paramF);
		slider_gs_f.setVisible(false);
		slider_gs_f.moveTo(group_simOpts);
		
		slider_gs_k = controlP5.addSlider("GrayScottK", 0, 1, 20, 10+11*4, 200, 10);
		slider_gs_k.setDecimalPrecision(3);
		slider_gs_k.setValue(p.paramK);
		slider_gs_k.setVisible(false);
		slider_gs_k.moveTo(group_simOpts);

		slider_gs_noise = controlP5.addSlider("NoiseLevel", 0f, 0.2f, 20, 10+11*5, 200, 10);
		slider_gs_noise.setDecimalPrecision(3);
		slider_gs_noise.setValue(0.05f);
		slider_gs_noise.setVisible(false);
		slider_gs_noise.moveTo(group_simOpts);
		
		slider_skip= controlP5.addSlider("frameskip", 1, 100, 20, 10+11*0, 200, 10);
		slider_skip.setDecimalPrecision(0);
		slider_skip.setValue(1);
		slider_skip.moveTo(group_simOpts);
		ddl_reaction = controlP5.addDropdownList("ddl_reaction", 100, 0, 100, 60);
		ddl_reaction.addItem("BZ", 0);
		ddl_reaction.addItem("GS", 1);
		ddl_reaction.setLabel("Reaction ");
		ddl_reaction.setValue(0);
		ddl_reaction.moveTo(group_simOpts);
		/*
		 * OPERATION BUTTONS
		 */
		group_operations = controlP5.addGroup("Operations", 20, 400);
		group_operations.moveTo(w);
		
		button_play=controlP5.addButton("button_play_pressed", 10, 95, 10, 80, 20);
		button_play.setLabel("stop/start calc");
		button_play.moveTo(group_operations);
		
		button_step=controlP5.addButton("button_step_pressed", 10, 180, 10, 80, 20);
		button_step.setLabel("Step");
		button_step.moveTo(group_operations);
		
		
		radio_trimA = controlP5.addRadioButton("radio_trimA", 10+15*3, 5+13*3);	
		radio_trimA.setItemsPerRow(3);
		radio_trimA.addItem("atrima",6).setLabel("");
		radio_trimA.addItem("btrima",6).setLabel("");
		radio_trimA.addItem("ctrima",6).setLabel("");
		radio_trimA.moveTo(group_operations);
		radio_trimA.hideLabels();
		
		radio_trimB = controlP5.addRadioButton("radio_trimB", 10+15*3, 5+13*4);	
		radio_trimB.setItemsPerRow(3);
		radio_trimB.addItem("atrimb",6).setLabel("");
		radio_trimB.addItem("btrimb",6).setLabel("");
		radio_trimB.addItem("ctrimb",6).setLabel("");
		radio_trimB.moveTo(group_operations);
		radio_trimB.hideLabels();
		
		radio_trimC = controlP5.addRadioButton("radio_trimC", 10+15*3, 5+13*5);	
		radio_trimC.setItemsPerRow(3);
		radio_trimC.addItem("atrimc",6).setLabel("");
		radio_trimC.addItem("btrimc",6).setLabel("");
		radio_trimC.addItem("ctrimc",6).setLabel("");
		radio_trimC.moveTo(group_operations);
		radio_trimC.hideLabels();
		
		
		
		
		
		
		trimA_slider = controlP5.addSlider("trimA_slider_change", 0f, 1f, 110, 5+13*5, 80, 10);
		trimA_slider.setLabel("Trim A");
		trimA_slider.setValue(0.5f);
		trimA_slider.moveTo(group_operations);
		
		trimB_slider = controlP5.addSlider("trimB_slider_change", 0f, 1f, 110, 5+13*6, 80, 10);
		trimB_slider.setLabel("Trim B");
		trimB_slider.setValue(0.5f);
		trimB_slider.moveTo(group_operations);
		
		trimC_slider = controlP5.addSlider("trimC_slider_change", 0f, 1f, 110, 5+13*7, 80, 10);
		trimC_slider.setLabel("Trim C");
		trimC_slider.setValue(0.5f);
		trimC_slider.moveTo(group_operations);
		
		offset_slider = controlP5.addSlider("offset_slider_change", -1f, 1f, 110, 10+13*3, 80, 10);
		offset_slider.setLabel("Offset");

		
		
		offset_slider.setValue(0f);
		offset_slider.moveTo(group_operations);
		
		checkbox_offset = controlP5.addCheckBox("checkbox_offset", 10+15*3, 10+13*3);	
		checkbox_offset.setItemsPerRow(3);
		checkbox_offset.addItem("aoffset",6).setLabel("");
		checkbox_offset.addItem("boffset",6).setLabel("");
		checkbox_offset.addItem("coffset",6).setLabel("");
		checkbox_offset.moveTo(group_operations);
		checkbox_offset.hideLabels();
		
		
		button_13=controlP5.addButton("button_export_pressed", 4, 10, 10, 80, 20);
		button_13.setLabel("Export");
		button_13.moveTo(group_operations);

		
		booleanLabel = controlP5.addTextlabel("booleanLabel", "Boolean", 5, 35);

		booleanLabel.moveTo(group_operations);
		
		ddl_boolean_abc1 = controlP5.addDropdownList("ddl_boolean_abc1", 50, 45, 20, 60);
		ddl_boolean_abc1.addItem("a", 0);
		ddl_boolean_abc1.addItem("b", 1);
		ddl_boolean_abc1.addItem("c", 2);
		ddl_boolean_abc1.setLabel(" ");
		ddl_boolean_abc1.moveTo(group_operations);

		ddl_boolean_operator = controlP5.addDropdownList("ddl_boolean_operator", 75, 45, 30, 60);
		ddl_boolean_operator.addItem("OFF", 0);
		
		ddl_boolean_operator.addItem("+", 1);
		ddl_boolean_operator.addItem("-", 2);
		ddl_boolean_operator.addItem("I", 3);
		ddl_boolean_operator.setLabel(" ");
		ddl_boolean_operator.moveTo(group_operations);

		ddl_boolean_abc2 = controlP5.addDropdownList("ddl_boolean_abc2", 110, 45, 20, 60);
		ddl_boolean_abc2.addItem("a", 0);
		ddl_boolean_abc2.addItem("b", 1);
		ddl_boolean_abc2.addItem("c", 2);
		ddl_boolean_abc2.setLabel(" ");
		ddl_boolean_abc2.moveTo(group_operations);

		ddl_boolean_surforprop = controlP5.addDropdownList("ddl_boolean_surfordrop", 135, 45, 35, 60);
		ddl_boolean_surforprop.addItem("Surf", 0);
		ddl_boolean_surforprop.addItem("Prop", 1);
		ddl_boolean_surforprop.addItem("Both", 2);
		ddl_boolean_surforprop.addItem("Special", 3);
		ddl_boolean_surforprop.setLabel(" ");
		ddl_boolean_surforprop.setValue(0);
		ddl_boolean_surforprop.moveTo(group_operations);
		
		button_applyBoolean = controlP5.addButton("button_applyBooleanOperation_pressed", 4,170,35, 30, 10);
		button_applyBoolean.setCaptionLabel("Apply");
		button_applyBoolean.moveTo(group_operations);
		
		button_booleanOff = controlP5.addButton("button_booleanOff_pressed",4,205,35,30,10);
		button_booleanOff.setCaptionLabel("Off");
		button_booleanOff.moveTo(group_operations);
		
		
		
		
	}
	public void update(){
		
		
		float creationButtonsHeight = 180;
		float simoptsButtonsHeight = 20+11*8;
		float operationsButtonsHeight = 63;
		float sliderPosition = 10+13*4;
		float extraTrimSpace = 0;
		if(checkbox_trim.getState(0)){
			trimA_slider.setVisible(true);
			radio_trimA.setVisible(true);
			trimA_slider.setPosition(110, sliderPosition);
			radio_trimA.setPosition(10+15*3, sliderPosition);
			sliderPosition+=13;
			extraTrimSpace+=13;
		}else{
			trimA_slider.setVisible(false);
			radio_trimA.setVisible(false);
		}
		if(ddl_reaction.getValue()==0){//BZ
			show_bz_controls();
			hide_gs_controls();
		}else{
			show_gs_controls();
			hide_bz_controls();
		}
		if(checkbox_trim.getState(1)){
			trimB_slider.setVisible(true);
			radio_trimB.setVisible(true);
			trimB_slider.setPosition(110, sliderPosition);
			radio_trimB.setPosition(10+15*3, sliderPosition);
			sliderPosition+=13;	
			extraTrimSpace+=13;
		}else{
			trimB_slider.setVisible(false);
			radio_trimB.setVisible(false);
		}
		if(checkbox_trim.getState(2)){
			trimC_slider.setVisible(true);
			radio_trimC.setVisible(true);
			trimC_slider.setPosition(110, sliderPosition);
			radio_trimC.setPosition(10+15*3, sliderPosition);
			sliderPosition+=13;
			extraTrimSpace+=13;
		}else{
			trimC_slider.setVisible(false);
			radio_trimC.setVisible(false);
		}

		float visOptsButtonsHeight = 105;
		float simOptsY = group_creationButtons.position().y+group_creationButtons.getHeight()+5;
		if(group_creationButtons.isOpen()){
			simOptsY+=creationButtonsHeight;
		}
		group_simOpts.setPosition(20,simOptsY);
		
		
		float visOptsY = group_simOpts.position().y+group_simOpts.getHeight()+5;
		if(group_simOpts.isOpen()){
			visOptsY+=simoptsButtonsHeight;
		}
		group_visOpts.setPosition(20,visOptsY);
		
		float operationsY = group_visOpts.position().y+group_visOpts.getHeight()+5+13;
		if(group_visOpts.isOpen()){
			operationsY+=visOptsButtonsHeight;
		}
		group_operations.setPosition(20,operationsY);
		
		operationsButtonsHeight += extraTrimSpace;
		
		float analysisY = group_operations.position().y+group_operations.getHeight()+5;
		if(group_operations.isOpen()){
			analysisY += operationsButtonsHeight;
		}
		group_analysis.setPosition(20,analysisY);
		
		
	}
	void hide_bz_controls(){
		slider_avg.setVisible(false);
		slider_speed.setVisible(false);
		slider_speeddecs.setVisible(false);
		cb_mode.setVisible(false);
	}
	void hide_gs_controls(){
		slider_gs_a.setVisible(false);
		slider_gs_b.setVisible(false);
		slider_gs_f.setVisible(false);
		slider_gs_k.setVisible(false);
		slider_gs_noise.setVisible(false);
		
	}
	void show_bz_controls(){
		slider_avg.setVisible(true);
		slider_speed.setVisible(true);
		slider_speeddecs.setVisible(true);
		cb_mode.setVisible(true);
	}
	void show_gs_controls(){
		slider_gs_a.setVisible(true);
		slider_gs_b.setVisible(true);
		slider_gs_f.setVisible(true);
		slider_gs_k.setVisible(true);
		slider_gs_noise.setVisible(true);
		
	}

}
