var request = require('request-promise');
var readline = require('readline');

var Promise = require('bluebird');
var JSDOM = require('jsdom').JSDOM;
var virtualConsole = new (require('jsdom')).VirtualConsole();
var decode = require('unescape');
// var parseString = require('xml2js').parseString;
var parseString = Promise.promisify(require('xml2js').parseString);

var font_regex = /(<\/?font[^>]*>)/ig ;

function getTrackLangURL(player, lang) {
  // var player = dom.window.ytplayer;
  if(player) {
    var data = JSON.parse( player.config.args.player_response );
    if(data.captions) {
      var tracks = data.captions.playerCaptionsTracklistRenderer.captionTracks;
      if(tracks) {
        for(var i = 0; i < tracks.length; i++) {
          // find caption by lang
          if(tracks[i].languageCode === lang) {
            return tracks[i].baseUrl;
          }
        }
      }
    }
  }
  return null;
}
var noop = function () {};
function getCaption(id, lang) {
    return JSDOM.fromURL('https://www.youtube.com/watch?v=' + id, {
      referrer: "https://www.youtube.com/",
      runScripts: "dangerously",  // "outside-only",
      //resources: "usable",
      // includeNodeLocations: true,
      virtualConsole: virtualConsole,
      pretendToBeVisual: true,
      beforeParse: (window) => {
        window.spf = function() {
          this.path = noop;
        };
        window.yt = {
          setConfig: noop,
          setMsg: noop
        };
      }
    }).then(dom => {
      var player = dom.window.ytplayer;
      var langUrl = getTrackLangURL(player, lang);
      var title = player ? player.config.args.title : null;
      if(langUrl) {
        return request(langUrl).then(body => {
          return {
            id: id,
            title: title,
            body: body
          };
        });
      }
      return {
        id: id,
        title: title,
      };
    }).then(data => {
      if(data.body) {
        return parseString(data.body).then(trans => {
          data.trans = trans;
          return data;
        });
      }
      return data;
    }).then(data => {
      if(data.trans) {
        var items = data.trans.transcript.text;
        if(items && items.length > 0) {
          var textItem;
          var txt = '';
          for(var k = 0; k < items.length; k++) {
            textItem = decode(items[k]._).replace(font_regex, '');
            if(txt.length !== 0) {
              txt += ' ';
            }
            txt += textItem;
          }
          data.caption = txt.replace(/\n/g, ' ');
        }
      } else {
        data.caption = null;
      }
      return data;
    });
}


if(process.argv.length === 3) {

  const rl = readline.createInterface({
    input: require('fs').createReadStream(process.argv[2]),
  });

  var work = null;

  rl.on('line', function (id) {
    if(work == null) {
      work = getCaption(id, 'en');
    } else {
      work = work.then(() => {
        return getCaption(id, 'en');
      });
    }

    work.then(info => {
      // if no title video is disabled or deleted.
      if(info && info.title) {
        console.log(info.title + ' ; ' + (info.caption || ''));
      }
    }).catch(function (err) {
      console.log(err);
    });
  });

  //work.
} else {
  console.log('Please provide file');
}



