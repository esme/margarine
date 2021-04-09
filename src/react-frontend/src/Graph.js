import React, { Component } from 'react';
import L from 'leaflet';
import { MapContainer, TileLayer, Marker, Popup } from 'react-leaflet';
import moment from 'moment';
import "leaflet/dist/leaflet.css";
import icon from 'leaflet/dist/images/marker-icon.png';
import iconShadow from 'leaflet/dist/images/marker-shadow.png';

let DefaultIcon = L.icon({
    iconUrl: icon,
    shadowUrl: iconShadow
});

L.Marker.prototype.options.icon = DefaultIcon;
class Graph extends Component {
  constructor(props) {
    super(props);
    this.state = {
      data: {
        coordinates: props.coordinates,
        minLat: 42.4072-5,
        maxLat: 42.4072,
        minLong: -71.3824-10,
        maxLong: -71.3824
      }
    }
  }

  render() {
    const { data } = this.state;
    var centerLat = (data.minLat + data.maxLat) / 2;
    var distanceLat = data.maxLat - data.minLat;
    var bufferLat = distanceLat * 0.05;
    var centerLong = (data.minLong + data.maxLong) / 2;
    var distanceLong = data.maxLong - data.minLong;
    var bufferLong = distanceLong * 0.15;
    return (
      <>
        <div className="map">
          <MapContainer
            style={{ height: "420px", width: "80%", margin: '0 auto' }}
            zoom={6}
            center={[centerLat, centerLong]}
            bounds={[
              [data.minLat - bufferLat, data.minLong - bufferLong],
              [data.maxLat + bufferLat, data.maxLong + bufferLong]
            ]}
          >
            <TileLayer url="http://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png" />
            {data.coordinates.map((coordinate, k) => {
              return (
                <Marker
                  position={[coordinate.latitude, coordinate.longitude]}
                  key={k}
                >
                  <Popup>
                    {moment(new Date(coordinate.timeClicked)).format('MMM DD YYYY hh:mm:ss')}
                  </Popup>
                </Marker>
              )
            })}
          </MapContainer>
        </div>
      </>
    );
  }
}

export default Graph;
