import Wkt from 'wicket'
import L from 'leaflet'
import 'wicket/wicket-leaflet'

/**
 * Return a Wkt object
 *
 * @param wktValue
 * @return {Wkt} wicket.Wkt object or null if the value is undiscernable
 */
export const getWKTFromString = wktValue => {
  let wkt = new Wkt.Wkt()
  // Attempt to catch malformed wkt string
  try {
    wkt.read(wktValue)
  } catch (e1) {
    try {
      // try sanitization
      wkt.read(wktValue.replace('\n', '').replace('\r', '').replace('\t', ''))
    } catch (e2) {
      if (e2.name === 'WKTError') {
        console.error('Error parsing WKT', wktValue)
        return null
      }
    }
  }

  return wkt
}

/**
 * Display a leaflet map that take in the wkt value from the wkt data attribute
 *
 * @param {string} elemID - The id of the element on the page
 * @param {string} popUpContentID - The id of the content to display in the popup
 * @return {Map} map - the leaflet map object
 */
export const leafletMap = (elemID, popUpContentID) => {
  let wktValue = document.getElementById(elemID).getAttribute('wkt')

  let wkt = getWKTFromString(wktValue)
  if (wkt == null) {
    console.error('Map displaying failed due to wkt extraction')
    return null
  }

  let map = L.map(elemID, { scrollWheelZoom: false }).
    setView({ lon: 0, lat: 0 }, 2)

  // add the OpenStreetMap tiles, need to attribute openstreetmap.org
  L.tileLayer('https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png', {
    maxZoom: 19,
    attribution: '&copy; <a href="https://openstreetmap.org/copyright">OpenStreetMap contributors</a>',
  }).addTo(map)

  // show the scale bar on the lower left corner
  L.control.scale().addTo(map)

  // convert
  let obj = wkt.toObject(map.defaults)

  // Deal with multigeometries
  if (Wkt.isArray(obj)) {
    obj.forEach(i => {
      if (obj.hasOwnProperty(i) && !Wkt.isArray(obj[i])) {
        if (document.getElementById(popUpContentID)) {
          obj[i].bindPopup(document.getElementById(popUpContentID).innerHTML).
            openPopup()
        }
        obj[i].addTo(map)
      }
    })
  } else {
    if (document.getElementById(popUpContentID)) {
      obj.bindPopup(document.getElementById(popUpContentID).innerHTML).openPopup()
    }
    obj.addTo(map)
  }



  // focus on the obj
  if (obj.getBounds !== undefined && typeof obj.getBounds === 'function') {
    // For objects that have defined bounds or a way to get them, eg, POLYGON
    map.fitBounds(obj.getBounds())
  } else if (obj.getLatLng !== undefined && typeof obj.getLatLng ===
    'function') {
    // for obj that has a Latlng, primarily eg, POINT
    map.panTo(obj.getLatLng())
  }

  // (optional) return the map
  return map
}
