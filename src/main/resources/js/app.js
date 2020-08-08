import hljs from 'highlight.js'
import {leafletMap} from './map'

// intitialise highlighting
hljs.initHighlightingOnLoad()

// display the leafletMap on the view page where the #map[wkt=*] exists
leafletMap('map')