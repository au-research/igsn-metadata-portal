(function(){function r(e,n,t){function o(i,f){if(!n[i]){if(!e[i]){var c="function"==typeof require&&require;if(!f&&c)return c(i,!0);if(u)return u(i,!0);var a=new Error("Cannot find module '"+i+"'");throw a.code="MODULE_NOT_FOUND",a}var p=n[i]={exports:{}};e[i][0].call(p.exports,function(r){var n=e[i][1][r];return o(n||r)},p,p.exports,r,e,n,t)}return n[i].exports}for(var u="function"==typeof require&&require,i=0;i<t.length;i++)o(t[i]);return o}return r})()({1:[function(require,module,exports){
"use strict";

var _highlight = _interopRequireDefault(require("highlight.js"));

var _map = require("./map");

var _qrcodeGenerator = _interopRequireDefault(require("qrcode-generator"));

function _interopRequireDefault(obj) { return obj && obj.__esModule ? obj : { "default": obj }; }

// intitialise highlighting
_highlight["default"].initHighlightingOnLoad(); // display the leafletMap on the view page where the #map[wkt=*] exists


var mapElement = document.getElementById('map');

if (mapElement) {
  var mapID = 'map';
  var mapDetailID = 'map-detail';
  var map = (0, _map.leafletMap)(mapID, mapDetailID);

  if (map === null) {
    document.getElementById(mapID).classList.add('hidden');
    document.getElementById(mapDetailID).classList.remove('hidden');
    document.getElementById('location-heading').classList.remove('hidden');
  }
} // display qrcode on the view page where the #qrcode[url=*] exists


var qrcodeElement = document.getElementById('qrcode');

if (qrcodeElement) {
  var typeNumber = 0;
  var errorCorrectionLevel = 'L';
  var qr = (0, _qrcodeGenerator["default"])(typeNumber, errorCorrectionLevel);
  var url = qrcodeElement.getAttribute('url');
  qr.addData(url);
  qr.make();
  document.getElementById('qrcode').innerHTML = qr.createImgTag(3);
}

},{"./map":2,"highlight.js":"highlight.js","qrcode-generator":"qrcode-generator"}],2:[function(require,module,exports){
"use strict";

Object.defineProperty(exports, "__esModule", {
  value: true
});
exports.leafletMap = exports.getWKTFromString = void 0;

var _wicket = _interopRequireDefault(require("wicket"));

var _leaflet = _interopRequireDefault(require("leaflet"));

require("wicket/wicket-leaflet");

function _interopRequireDefault(obj) { return obj && obj.__esModule ? obj : { "default": obj }; }

/**
 * Return a Wkt object
 *
 * @param wktValue
 * @return {Wkt} wicket.Wkt object or null if the value is undiscernable
 */
var getWKTFromString = function getWKTFromString(wktValue) {
  var wkt = new _wicket["default"].Wkt(); // Attempt to catch malformed wkt string

  try {
    wkt.read(wktValue);
  } catch (e1) {
    try {
      // try sanitization
      wkt.read(wktValue.replace('\n', '').replace('\r', '').replace('\t', ''));
    } catch (e2) {
      if (e2.name === 'WKTError') {
        console.error('Error parsing WKT', wktValue);
        return null;
      }
    }
  }

  return wkt;
};
/**
 * Display a leaflet map that take in the wkt value from the wkt data attribute
 *
 * @param {string} elemID - The id of the element on the page
 * @return {Map} map - the leaflet map object
 */


exports.getWKTFromString = getWKTFromString;

var leafletMap = function leafletMap(elemID, popUpContentID) {
  var wktValue = document.getElementById(elemID).getAttribute('wkt');
  var wkt = getWKTFromString(wktValue);

  if (wkt == null) {
    console.error('Map displaying failed due to wkt extraction');
    return null;
  }

  var map = _leaflet["default"].map(elemID, {
    scrollWheelZoom: false
  }).setView({
    lon: 0,
    lat: 0
  }, 2); // add the OpenStreetMap tiles, need to attribute openstreetmap.org


  _leaflet["default"].tileLayer('https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png', {
    maxZoom: 19,
    attribution: '&copy; <a href="https://openstreetmap.org/copyright">OpenStreetMap contributors</a>'
  }).addTo(map); // show the scale bar on the lower left corner


  _leaflet["default"].control.scale().addTo(map); // convert


  var obj = wkt.toObject(map.defaults); // Deal with multigeometries

  if (_wicket["default"].isArray(obj)) {
    obj.forEach(function (i) {
      if (obj.hasOwnProperty(i) && !_wicket["default"].isArray(obj[i])) {
        obj[i].bindPopup(document.getElementById(popUpContentID).innerHTML).openPopup();
        obj[i].addTo(map);
      }
    });
  } else {
    obj.bindPopup(document.getElementById(popUpContentID).innerHTML).openPopup();
    obj.addTo(map);
  } // focus on the obj


  if (obj.getBounds !== undefined && typeof obj.getBounds === 'function') {
    // For objects that have defined bounds or a way to get them, eg, POLYGON
    map.fitBounds(obj.getBounds());
  } else if (obj.getLatLng !== undefined && typeof obj.getLatLng === 'function') {
    // for obj that has a Latlng, primarily eg, POINT
    map.panTo(obj.getLatLng());
  } // (optional) return the map


  return map;
};

exports.leafletMap = leafletMap;

},{"leaflet":"leaflet","wicket":"wicket","wicket/wicket-leaflet":"wicket/wicket-leaflet"}]},{},[1]);

//# sourceMappingURL=maps/bundle.js.map
