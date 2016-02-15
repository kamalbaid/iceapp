
// class object to store data
function UserInformationData (event, userCount, userName, ip, group, installerName, osName, usedDuration, userDropReason, userID) {
	this.Event = event,
	this.UserCount = userCount,
	this.User = userName,
	this.IP = ip,
	this.Group = group,
	this.Installer = installerName ,
	this.OS =  osName ,
	this.Duration = usedDuration,
	this.Drop = userDropReason ,
	this.UID = userID
}

UserInformationData.prototype.getValue = function() { 
												return this.UserCount;
											}

UserInformationData.prototype.setValue = function(val) { 
												this.UserCount = val;
											}

UserInformationData.prototype.isUsingFlavour = function(name) {
												var using = false;
												if (this.hasOwnProperty(name) && (this[name].search("Allowed")) > 0) {
   													using = true;
   												}
                                                return using;
											}

function addProperties2Object(fromObj, toObj) {
	// add flavour status 
	for (var prop in fromObj) {
		if (fromObj.hasOwnProperty(prop)) {
			toObj[prop] = fromObj[prop];
		}
	}
}


function convertDateToText(date) { 

    var title = date;
    if(date instanceof Date) {
        title = date.getFullYear() + "-";
        title += (date.getMonth() +1 ) + "-";
        title += date.getDate() + " ";
        title += date.getHours() + ":";
        title += date.getMinutes() + ":";
        title += date.getSeconds() + " ";
    }

    return title; 
}

function getUseDuration(loginTime, logoutTime, usedDurationInSeconds) {
	usedDurationInSeconds = (typeof usedDurationInSeconds !== 'undefined') ? usedDurationInSeconds : -1;

	if(usedDurationInSeconds < 0 && logoutTime > 0 && loginTime > 0 ) {
		usedDurationInSeconds = Math.floor((logoutTime - loginTime) / 1000);
	}

	var txt = "In use";
	if(usedDurationInSeconds > -1) {
		var mins = usedDurationInSeconds % 3600;
		txt = Math.floor((usedDurationInSeconds / 3600)) + "h " + Math.floor((mins /60 ))  +"m "+ Math.floor((mins % 60)) + "s";
	}

	return txt
}

function drawC3Chart(userData) {
	console.log("drawc3Chart>>>>" + userData);

    var chartData = [];
    var yData = [];
    
    // contains all the flavours available on the server. used as a set.
    var flavourNamesSet = {};

    for (var i = 0; i < userData.length; i++) {
        var user = userData[i];
        var loginTime = parseInt(user["userConnectionTime"]);
        var logoutTime = parseInt(user["userDropTime"]);

        var usedDuration = getUseDuration(loginTime, logoutTime, parseInt(user["usedDuration"]));

        var status = {};
        var flavourUsage = {};
        var logInFlavourUsage = {};
        var logOutFlavourUsage = {};

        // get flavour used and queried
        var flavourList = user.flavourStatus.entry;

        for (var ii = 0; ii < flavourList.length; ii++) {
            var flavourName = flavourList[ii].key;
            var flavourValues = flavourList[ii].value.item;

            var statVal = parseInt(flavourValues[0]);
            var queried = (statVal & 1 ) == 1;
            var queriedStatus = (statVal & 2 ) == 2;
            var checkout = (statVal & 4 ) == 4;
            var checkoutStatus = (statVal & 8 ) == 8;
            
            var flavStatusTxt = "No Request";
            if(queried && (!queriedStatus)){
            	flavStatusTxt = "Denied";
            }else if(checkout){
            	flavStatusTxt = checkoutStatus ? "Allowed" : "Denied";
            }

            //var flavStatusTxt = "";
            //flavStatusTxt += (statVal & 2) ? "Allowed" : "Denied";
            status[flavourName] = flavStatusTxt;

            var usageIn = parseInt(flavourValues[1]);
            var usageOut = parseInt(flavourValues[2]);
            if(usageIn >= 0 ) {
                logInFlavourUsage[flavourName] = usageIn;
            }

            if(usageOut >= 0 ) {
                logOutFlavourUsage[flavourName] = usageOut;
            }

            // used as set
            flavourNamesSet[flavourName] = true;
        }

       
       if(loginTime != -1){
       		var count = parseInt(user["numUsersIn"]);
        	var inUserData = new UserInformationData("Connected", count, user.userName, user.ipAddress, 
                            user.groupID, user.installerName, user.osName, usedDuration, user.userDropReason, user.userID);

        	addProperties2Object(status, inUserData);

        	chartData.push({
                time: loginTime,
                data: inUserData,
                flavourUsageData: logInFlavourUsage
        	});
    	}

        if(logoutTime != -1) {
        	var count = parseInt(user["numUsersOut"]);
            var outUserData = new UserInformationData("Dropped", count, user.userName, user.ipAddress, 
                            user.groupID, user.installerName, user.osName, usedDuration, user.userDropReason, user.userID);
    
            addProperties2Object(status, outUserData);

            chartData.push({
                time: logoutTime,
                data: outUserData,
                flavourUsageData: logOutFlavourUsage
            });
        }
    }

    chartData.sort( function(a, b) {		// ascending function
		return a.time - b.time;
    });

    console.log("Before Correction " + JSON.stringify(chartData));
    // Correct the data
    var previousUserCount = -1;
    var indexToCorrect = [];
    var previousEvent = "";
    var maxUsers = 0;
    for (var i = chartData.length - 1; i >= 0; i--) {
    	var currentData = chartData[i].data;
    	var currentflavourUsageData = chartData[i].flavourUsageData;
    	if(previousEvent === "Connected"){
    		previousUserCount--;
            // find max 
            Object.keys(currentflavourUsageData).forEach(function(key){
                //console.log(key + " val : "+ currentflavourUsageData[key]);
                previousUserCount = previousUserCount < currentflavourUsageData[key] ? currentflavourUsageData[key] : previousUserCount
            });

            //console.log(previousUserCount);

    		currentData.setValue(previousUserCount);
    	}else if (previousEvent === "Dropped"){
    		previousUserCount++;
    		currentData.setValue(previousUserCount);
		}else{
			previousUserCount = currentData.getValue();
		}

        maxUsers = maxUsers < previousUserCount ? previousUserCount : maxUsers;
    	previousEvent = currentData.Event;
    };

     console.log("After Correction " + JSON.stringify(chartData));
	// correction ended

   	var flavourSeriesData = {};
   	for (var name in flavourNamesSet) {
   		if (flavourNamesSet.hasOwnProperty(name)) {
   			flavourSeriesData[name] = [name];
   		}
   	}

    var xData = ['x'];
    var yData = ['User'];
    
    var previousValue = 0;
    var previousChartData = null;

    var totalUsersConnected = 0;
    var totalUsersDroped = 0;
    var adminRevoked = 0;
    var clientWorkFinished = 0;
    var clientConnectionLost = 0;
    var outputStats = {};

    for (var j = 0; j<chartData.length; j++) {
    	var time = chartData[j].time;
		var currentData = chartData[j].data;
		var currentUserCount = chartData[j].data.getValue();

		xData.push(time);

		var flavoursData = chartData[j].flavourUsageData;

		for (var flavourName in flavourNamesSet) {
			flavourSeriesData[flavourName].push(flavoursData[flavourName]);

			if(currentData.Event === "Connected" && currentData.hasOwnProperty(flavourName)) {
				var status = currentData[flavourName];

				flUsageStat = outputStats[flavourName];
				if(typeof flUsageStat === 'undefined'){
					flUsageStat = [0, 0, 0];   // allowed, denied, not requested
				}
				
				//if(status.search("Queried") > -1){
				//	flUsageStat[0]++;
				//}

				if(status.search("Allowed") > -1) {
					flUsageStat[0]++;
				} else if (status == "Denied"){
					flUsageStat[1]++;
				}else{
					flUsageStat[2]++;
				}

				outputStats[flavourName] = flUsageStat;
			}
		}

		yData.push(chartData[j].data);

		// table stat
		if(currentData.Event === "Connected"){
			totalUsersConnected++;
		}else{
			totalUsersDroped++;
			var reason = currentData.Drop;
			if(reason === 'ADMIN_REVOKED') {
				adminRevoked++;
			} else if(reason === 'CLIENT_WORK_FINISHED') {
				clientWorkFinished++;
			} else {
				clientConnectionLost++;
			}
		}

	}


	//console.log("flavourStats " + JSON.stringify(outputStats, null, 4));
	//console.log("flavourStats " + totalUsersDroped +" "+ totalUsersConnected);
	
    var chartColumnData = [xData, yData];
    for (var flavourName in flavourNamesSet) {
    	chartColumnData.push(flavourSeriesData[flavourName]);
    }

    var chart = c3.generate({
    	bindto: '#chart_div',
    	data: {
        	x: 'x',
        	columns: chartColumnData,
        	onclick: updateToolTipTable
    	},
    	
    	axis: {
        	x: {
        		height: 55,
            	type: 'timeseries',
            	tick: {
                	format: '%e %b %y %H:%M',
                	rotate: 30
            	}

        	},

        	y: {
            	label: {
                	text: 'User Count',
                	position: 'outer-middle',
            	},

                tick:{
                    count:maxUsers+1
                },

                min: 0,
                max: maxUsers

        	},

            max:{
                y : maxUsers
            },
            min:{
                y : 0
            }
    	},

    	zoom: {
    		enabled: true
    	},

    	legend: {
        	position: 'right'
    	},

    	tooltip: {
        	format: {
            	title: convertDateToText,

            	value: function (value, ratio, id, index, orignalData) {
		        	//console.log(" tv : " + value + " td " + ratio + " trd " + id + "index " + index + " orignalData " + orignalData);            
		            if (typeof orignalData === 'object'){
		            	var formattedValue = "<table>";
		            	for (var property in orignalData) {
							if (orignalData.hasOwnProperty(property)) {
		    					// do stuff
		            			formattedValue += "<tr>";
		            			formattedValue += "<td>"+property+"</td>";
		            			formattedValue += "<td>" + orignalData[property] + "</td>";
		            			formattedValue += "</tr>";
							}
						}
						formattedValue += "</table>"; 
		            } else{
						formattedValue = value;
		            }

		            return formattedValue;
            	}
        	}
    	}
	});
	
	outputStats['totalUsersConnected'] = totalUsersConnected;
	outputStats['totalUsersDroped'] = totalUsersDroped;
	outputStats['adminRevoked'] = adminRevoked;
	outputStats['clientWorkFinished'] = clientWorkFinished;
	outputStats['clientConnectionLost'] = clientConnectionLost;
	return outputStats;
}

function clearUpdateToolTipTable(){
	var tooltip_table = $('#tooltip_table_div').eq(0);
	tooltip_table.html("");
}


function updateToolTipTable(clickedPointData){
	console.log("clickedPointData " + JSON.stringify(clickedPointData));
	var pointIndex = clickedPointData.index;
	var seriesName = clickedPointData.name;
	var data = clickedPointData.orignalData;
    var x_time = clickedPointData.x;

	var targets = this.internal.data.targets;
	//console.log("clickedPointData " + JSON.stringify(targets));

	// list of series
	var tt_table_html = "<table class=\"table table-bordered table-hover\">";
    tt_table_html += "<tr>";
    tt_table_html += "<td class=\"label-red\">Time</td>";
    tt_table_html += "<td>" + convertDateToText(x_time) + "</td>";
    tt_table_html += "</tr>";
	
	for(var i = 0; i< targets.length; i++){
		var currentSeries = targets[i];
		var seriesName = currentSeries.id;
		var pointData = currentSeries.values[pointIndex].orignalData;

		if(typeof pointData === 'object') {
			for (var prop in pointData) {
				if (pointData.hasOwnProperty(prop)) {
					tt_table_html += "<tr>";
					tt_table_html += "<td class=\"label-blue\">" + prop +"</td>";
					tt_table_html += "<td>" + pointData[prop] + "</td>";
					tt_table_html += "</tr>";
				}
			}
		} else {
					tt_table_html += "<tr>";
					tt_table_html += "<td class=\"label-orange\">" + seriesName +"</td>";
					tt_table_html += "<td>" + pointData + "</td>";
					tt_table_html += "</tr>";
		}
	}
	
	//if(!(data instanceof UserInformationData))
	//	return;

	//var tt_table_html = "<table class=\"table table-bordered table-hover\">";

	//for (var prop in data) {
	//	if (data.hasOwnProperty(prop)) {
	//		tt_table_html += "<tr>";
	//		tt_table_html += "<td class=\"label-blue\">" + prop +"</td>";
	//		tt_table_html += "<td>" + data[prop] + "</td>";
	//		tt_table_html += "</tr>";
	//	}
	//}

	tt_table_html += "</table>";

	var tooltip_table = $('#tooltip_table_div').eq(0);
	tooltip_table.html(tt_table_html);
}

function loadStatTable(flavourStats){
	var table1 = $('#table_div1').eq(0);
	var table2 = $('#table_div2').eq(0);

	var totalUsersConnected = flavourStats['totalUsersConnected'];
	var totalUsersDroped = flavourStats['totalUsersDroped'];
	var clientWorkFinished = flavourStats['clientWorkFinished'];
	var adminRevoked = flavourStats['adminRevoked'];
	var clientConnectionLost = flavourStats['clientConnectionLost'];

	var table1HTML = "<table class=\"table table-bordered table-hover\">";

	table1HTML += "<tr class=\"label-green\">";
	table1HTML += "<td class=\"col-md-6\">Total Users Connected</td>";
	table1HTML += "<td class=\"col-md-4\">" + totalUsersConnected + "</td>";
	table1HTML += "</tr>"

	table1HTML += "<tr class=\"label-red\">";
	table1HTML += "<td class=\"col-md-6\">Users Dropped</td>";
	table1HTML += "<td class=\"col-md-4\">" + totalUsersDroped + "</td>";
	table1HTML += "</tr>"

	table1HTML += "<tr class=\"label-green\">";
	table1HTML += "<td class=\"tdd-padding col-md-6\">Normal Shutdown</td>";
	table1HTML += "<td class=\"col-md-4\">" + clientWorkFinished + "</td>";
	table1HTML += "</tr>";
	
	table1HTML += "<tr class=\"label-red\">";
	table1HTML += "<td class=\"tdd-padding col-md-6\">Admin Revoked License Permission</th>";
	table1HTML += "<td class=\"col-md-4\">" + adminRevoked + "</td>";
	table1HTML += "</tr>";

	table1HTML += "<tr class=\"label-red\">";
	table1HTML += "<td class=\"tdd-padding col-md-6\">Connection Lost</td>";
	table1HTML += "<td class=\"col-md-4\">" + clientConnectionLost + "</td>";
	table1HTML += "</tr>";
	table1HTML += "</table>";
	table1HTML += "</td>";

	table1HTML += "</tr>";
	table1HTML += "</table>";

	var text = "<table class=\"table table-bordered  table-hover\">";

	text += "<tr>";
	text += "<th class=\"label-bold\">#</th>";
	//text += "<th>Queried</th>";
	text += "<th>Allowed</th>";
	text += "<th>Denied</th>";
	text += "<th>No Request</th>";		 		
	text += "</tr>";

	for (var flavour in flavourStats) {
	 	if (flavourStats.hasOwnProperty(flavour)) {
	 		var values = flavourStats[flavour];
	 		
	 		if(values instanceof Array){
	 			text += "<tr>";
	 			text += "<th>" + flavour + "</th>";
	 			text += "<td>" + values[0] + "</td>";
	 			text += "<td>" + values[1] + "</td>";
	 			text += "<td>" + values[2] + "</td>";	 		
	 			text += "</tr>"; 
	 		}
	 	}
	}

	text += "</table>";

	//console.log("ddd" + text);
	table1.html(table1HTML);
	table2.html(text);
}
