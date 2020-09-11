import hljs from 'highlight.js'
import { leafletMap } from './map'
import qrcode from 'qrcode-generator'

// intitialise highlighting
hljs.initHighlightingOnLoad()

// display the leafletMap on the view page where the #map[wkt=*] exists
let mapElement = document.getElementById('map')
if (mapElement) {
  const mapID = 'map';
  const mapDetailID = 'map-detail'
  let map = leafletMap(mapID, mapDetailID)
  if (map === null) {
    document.getElementById(mapID).classList.add("hidden")
    document.getElementById(mapDetailID).classList.remove("hidden")
  }
}

// display qrcode on the view page where the #qrcode[url=*] exists
let qrcodeElement = document.getElementById('qrcode')
if (qrcodeElement) {
  let typeNumber = 0

  let errorCorrectionLevel = 'L'
  let qr = qrcode(typeNumber, errorCorrectionLevel)
  let url = qrcodeElement.getAttribute('url')
  qr.addData(url)
  qr.make()
  document.getElementById('qrcode').innerHTML = qr.createImgTag(3)
}
