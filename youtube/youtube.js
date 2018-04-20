var request = require('request');

var JSDOM = require('jsdom').JSDOM;
var virtualConsole = new (require('jsdom')).VirtualConsole();
var decode = require('unescape');
var parseString = require('xml2js').parseString;

var font_regex = /(<\/?font[^>]*>)/ig ;


function getCaption(url, lang, callback) {
    JSDOM.fromURL(url, { runScripts: "dangerously", virtualConsole: virtualConsole }).then(dom => {
      var player = dom.window.ytplayer;
      if(player) {
        var data = JSON.parse( dom.window.ytplayer.config.args.player_response );
        if(data.captions) {
          var tracks = data.captions.playerCaptionsTracklistRenderer.captionTracks;
          for(var i = 0; i < tracks.length; i++) {
            // find caption by lang
            if(tracks[i].languageCode === lang) {
              // open caption url
              request(tracks[i].baseUrl, function(error, response, body) {
                if(error) {
                  callback(error);
                } else {
                  parseString(body, function(error, result) {
                    // take only text and remove tags and html codes
                    var items = result.transcript.text;
                    var textItems = [];
                    for(var k = 0; k < items.length; k++) {
                       textItems.push(decode(items[k]._).replace(font_regex, ''));
                    }
                    callback(error, textItems);
                  });
                }
              });
              return;
            }
          }
        }
      }

      // no video, no caption
      callback(null, null);
   });
}

getCaption('https://www.youtube.com/watch?v=plieAqK2a00', 'en', function(error, captions) {
  if(error) {
     console.log('Error: ' + error);
  } else if (captions) {
    var output = '';
   for(var i = 0; i < captions.length; i++) {
     output += captions[i] + ' ';
      //console.log(captions[i]);
    }
    console.log(output);
  } else {
    console.log('no video or caption');
  }
});

