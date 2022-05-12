Thanks for downloading and testing my spline tool! I spent a lot of time on this project, and i hope you like it!

This is still a pretty work in progress project of mine so i will be updating this in the near future.

I have tried to make the descriptions of things relatively intuitive.
If you have any problems, feel free to contact me on discord @ Lurk#0053 or simply tagging me in the Zeepkist Server.

Guide/tutorial:

To make a simple Node(Point without handles, useful for start/end points), simply click in one spot without moving your mouse.

To make a spline Node, Click and drag from the center of your wanted node to where you want the road to continue, this will generate a node and two handles, one on either side of the node.

To move a node/ handle, simply drag the node/handle to where you want it to go, note that handles will mirror their counterpart.

To change a nodes altitude, hold the mousebutton down on top of the node/handle you want to change, and scroll using you mousewheel.

To change a nodes rotation/ make a banked corner, switch to rotation mode using N (when in rotation mode, only nodes should have numbers above them) and change the value like when changing altitude.

when you are satisfied with your track, you can test it ingame. To do this, press R to generate the track file, next make a empty level in Zeepkist by opening the level editor and saving to a new file. Then open the file location of your Zeepkist levels(either by pressing the button in the "load level" interface or by going to \AppData\Roaming\Zeepkist\Levels), and find the level you just made and open the text file, then copy over the contents of the "track.txt" file in the tool folder to the zeepkist level file, press save, and open the file ingame. You should now be seeing the level you have just created.

Note: the current build has no automatic Start/goal or checkpoint placing, so that will have to be done manually.

Keybinds:

Z - Hide lines connecting nodes in the editor, they were mainly there for debug purposes, but might help in understanding your cobwebs.

X - Switch between Solid lines and dotted lines. This has no real impact and is just for preference.

C - Hide Nodes. Used to get a better look at your track, not recommended to build with nodes hidden.

V - Hide Handles. Same as above but with the handles. Makes building really hard, but looks good.

B - Display Altitude/Rotation information on nodes and Handles.

N - Toggle between changing Altitude and Rotation.

M - Reduces track complexity to better performance in the editor, remember to reenable this before pressing R.

A - Insertion Mode, click on a Node, any new Nodes after will be inserted at this Nodes index.
D - Deletion Mode, enables dragging nodes to the little red box in the bottom left to delete them.

Todo:
Deleting Nodes / Adding nodes between existing ones - Check
Improved Visuals
Fine tuning / Zoom features
Faster / Slower scroll using shift/ctrl - Check
Angle copy Features
Grid Snap
