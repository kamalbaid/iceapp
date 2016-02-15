// determine width of the browser window
var width = 630, height = 460;

if (parseInt(navigator.appVersion)>3) {
 if (navigator.appName=="Netscape") {
  width = window.innerWidth;
  height = window.innerHeight;
 }
 if (navigator.appName.indexOf("Microsoft")!=-1) {
  width = document.body.offsetWidth;
  height = document.body.offsetHeight;
 }
}

// width of each cell in menu
cellWidth = 100;

BLANK_IMAGE = 'images/b.gif';

var STYLE = {
	border:1,			// item's border width, pixels; zero means "none"
	shadow:2,			// item's shadow size, pixels; zero means "none"
	color:{
		border:"#666666",	// color of the item border, if any
		shadow:"#DBD8D1",	// color of the item shadow, if any
		bgON:"#F2F2F2",		// background color for the items
		bgOVER:"#FFFFFF"	// background color for the item which is under mouse right now
	},
	css:{
		ON:"clsCMOn",		// CSS class for items
		OVER:"clsCMOver"	// CSS class  for item which is under mouse
	}
};

var MENU_ITEMS = [
    
    {
      pos:[((width - cellWidth*4) / 2)-50 , 10],    // start point for menu
      itemoff:[0,99],                   // offset for each cell
      leveloff:[21,0], 
      style:STYLE, 
      size:[22,cellWidth+20]
    },
    
    
    {code:"License",
        sub:[
            {"itemoff":[21,0]},
            {code:"Activate", "url":"activate.jsp"},
            {code:"Surrender", "url":"surrender.jsp"},
            {code:"License Manager", "url":"license.jsp"}
        ]
    },
    {code:"Group Manager",
        sub:[
            {itemoff:[21,0]},
            {code:"Groups", "url":"groupPermission.jsp"}
        ]
    },
    
    {code:"Feature", "url":"features.jsp"},
    
    {code:"Users", "url":"users.jsp"},

    {code:"Administrator",
        sub:[
            {itemoff:[21,0]},
            {code:"Change Password", "url":"changePassword.jsp"},
            {code:"Logout", "url":"logout.jsp"}
        ]
    }
];
