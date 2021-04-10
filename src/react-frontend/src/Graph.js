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

  shouldComponentUpdate(nextProps, nextState) {
    if (
      (this.props.coordinates.length === 0 && nextProps.coordinates.length === 0) &&
      (this.state.data.coordinates.length === 0 && nextState.data.coordinates.length === 0)
    ) {
      return false;
    }

    if (
      this.areCoordinatesSame(this.props.coordinates, nextProps.coordinates)
    ) {
      if (this.areCoordinatesSame(this.state.data.coordinates, nextState.data.coordinates)) {
        return false;
      }
    } else {
      console.log('this props: ', this.props);
      console.log('next props: ', nextProps);
      this.setState({
        data: {...this.state.data, coordinates: [...nextProps.coordinates]},
      });
    }

    return true;
  }

  areCoordinatesSame(c1, c2) {
    if (c1.length === 0 && c2.length === 0) {
      return true;
    }
    if (c1.length !== c2.length) {
      return false;
    }
    if (c1[0].longitude !== c2[0].longitude || c1[0].latitude !== c2[0].latitude || c1[0].timeClicked !== c2[0].timeClicked) {
      return false;
    }
    return true;
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
                  key={k.toString() + " " + coordinate.latitude.toString() + " " + coordinate.timeClicked}
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
