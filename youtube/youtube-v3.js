var request = require('request');
var readline = require('readline');


var JSDOM = require('jsdom').JSDOM;
var virtualConsole = new (require('jsdom')).VirtualConsole();
var decode = require('unescape');
var parseString = require('xml2js').parseString;

var font_regex = /(<\/?font[^>]*>)/ig ;


function getCaption(url, lang, callback) {
    JSDOM.fromURL(url, { runScripts: "dangerously", virtualConsole: virtualConsole }).then(dom => {
      var player = dom.window.ytplayer;
      if(player) {
        // for(var p in dom.window) {
        //   // var ii = JSON.stringify(dom.window[p]).indexOf("Caught in The Act");
        //   // if(ii >= 0) {
        //   //   console.log(p, ii);
        //   // }
        //   if(typeof dom.window[p] === 'function') {
        //     console.log(p);
        //   }
        // }
        // // var oo = dom.getPageData();
        // process.exit(1);

        var data = JSON.parse( player.config.args.player_response );
        if(data.captions) {
          var tracks = data.captions.playerCaptionsTracklistRenderer.captionTracks;
          if(tracks) {
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
                      if(items) {
                        var textItems = [];
                        for(var k = 0; k < items.length; k++) {
                           textItems.push(decode(items[k]._).replace(font_regex, ''));
                        }
                        callback(error, textItems);
                      }
                    });
                  }
                });
                return;
              }
            }
          }
        }
      }

      // no video, no caption
      callback(null, null);
   });
}

if(process.argv.length === 3) {

  const rl = readline.createInterface({
    input: require('fs').createReadStream(process.argv[2]),
    // output: process.stdout
  });

  rl.on('line', function (line) {
    getCaption('https://www.youtube.com/watch?v=' + line, 'en', function(error, captions) {
      if(error) {
         console.log('Error: ' + error);
      } else if (captions) {
        var txt = '';
        for(var i = 0; i < captions.length; i++) {
          //console.log(captions[i]);
          if(txt.length !== 0) {
            txt += ' ';
          }
          txt += captions[i];
        }
        console.log(line + '; ' + txt);
      }
      //else {
      //  console.log('no video or captions');
      //}
    });
  });
} else {
  console.log('Please provide file');
}



