VSpace
======
VSpaceis a 3 dimensional drawing tool for Designers. 
Unlike traditional CAD systems that work primarily by representing boundaries (Breps), 
VSpacederives form by the direct representation and manipulation of material properties in space(Preps).
In this Sense this tool allows the designer to treat Form and Material simultaneously in the same design environment.

As a digital environment, P-Rep is based on chemical Reaction Diffussion Phenomena. 
Three “substances” A,B and C, whose concentrations can infinitely vary from zero to one, 
are distributed as a homogeneous mixture in a Voxel Space. Using cellular automata as a method 
for controlling local interactions and following the mathematical laws of reaction-diffusion 
(as described by Alan Turing), a closed system of exchange is set in motion. As substances interact
with each other, gradient fields start to form and eventually three dimensional figurations emerge.

We are currently modeling a more precise control system and working on adding more functionality to the tool.

###External library credits
To make this work, we used several 3rd party libraries, which we commited to /lib/ folder:
- /lib/core.jar :       Processing      http://www.processing.org/
- /lib/peasycam.jar :   PeasyCam        http://mrfeinberg.com/peasycam/
- /lib/controlP5.jar :  ControlP5       http://www.sojamo.de/libraries/controlP5/
- /lib/dxf.jar  :       Processing dxf  http://www.processing.org/reference/libraries/dxf/
