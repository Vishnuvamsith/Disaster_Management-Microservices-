import React, { useState, useEffect } from 'react';
import axios from 'axios';
import L from 'leaflet';
import 'leaflet/dist/leaflet.css';

import customMarkerIconImage from '../../src/assets/home/mark.png';

function Popup() {
  const [isOpen, setIsOpen] = useState(true);
  const [isSending, setIsSending] = useState(false);
  const [userLocation, setUserLocation] = useState(null);
  const [nearbySafePlaces, setNearbySafePlaces] = useState([]);
  const [isRequestingHelp, setIsRequestingHelp] = useState(false);
  const [emergencyType, setEmergencyType] = useState('');
  const [mapInitialized, setMapInitialized] = useState(false);

  useEffect(() => {
    const popupTimeout = setTimeout(() => {
      setIsOpen(false);
    }, 10000); // Close popup after 10 seconds

    return () => clearTimeout(popupTimeout);
  }, []);

  useEffect(() => {
    console.log('Initializing map');
    let map;
  
    if (mapInitialized) {
      console.log('Map is being set up');
      if (!window.myMap) {
        map = L.map('map', {
          center: userLocation ? [userLocation.latitude, userLocation.longitude] : [17.46821, 78.373056],
          zoom: 13,
        });
        L.tileLayer('https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png', {
          maxZoom: 19,
          attribution: '© OpenStreetMap'
        }).addTo(map);
        window.myMap = map;
        console.log('Map created');
      } else {
        map = window.myMap;
        console.log('Using existing map');
        map.eachLayer((layer) => {
          if (layer instanceof L.Marker) {
            map.removeLayer(layer);
            console.log('Removed old marker');
          }
        });
      }
  
      nearbySafePlaces.forEach(place => {
        L.marker([place.latitude, place.longitude], {
          icon: new L.Icon({
            iconUrl: customMarkerIconImage,
            iconSize: [25, 41],
            iconAnchor: [12, 41],
            popupAnchor: [1, -34]
          })
        }).addTo(map)
        .bindPopup(`<b>${place.Name}</b>`)
        .openPopup();
        console.log(`Added marker for ${place.Name}`);
      });
    }
  
    return () => {
      if (map) {
        map.off();
        map.remove();
        window.myMap = null;
        console.log('Map cleaned up');
      }
    };
  }, [mapInitialized, nearbySafePlaces, userLocation]);
  

  const handleClose = () => {
    setIsOpen(false);
  };

  const handleEmergencyClick = () => {
    setIsSending(true);
    navigator.geolocation.getCurrentPosition(async (position) => {
      const { latitude, longitude } = position.coords;
      try {
        const response = await axios.post('http://localhost:8085/email/sendcoord', {
          latitude,
          longitude,
          emergencyType,
        });
        console.log('Emergency location sent:', response.data.message);
      } catch (error) {
        console.error('Error sending emergency location:', error);
      } finally {
        setIsSending(false);
      }
    }, (error) => {
      console.error('Geolocation error:', error);
      setIsSending(false);
    });
  };

  const handleHelpRequest = () => {
    setIsRequestingHelp(true);
    setIsSending(true);
    navigator.geolocation.getCurrentPosition(async (position) => {
      const { latitude, longitude } = position.coords;
      console.log(latitude)
      setUserLocation({ latitude, longitude });
      try {
        const response = await axios.get(`http://localhost:8086/alert/nearby-safe-places`, {
          params: { userLat: latitude, userLon: longitude }
        });
        console.log(response.data)
        setNearbySafePlaces(response.data);
        setMapInitialized(true);
      } catch (error) {
        console.error('Error fetching nearby safe places:', error);
      } finally {
        setIsSending(false);
        setIsRequestingHelp(false);
      }
    }, (error) => {
      console.error('Geolocation error:', error);
      setIsRequestingHelp(false);
    });
  };

  if (!isOpen) return null;

  return (
    <div style={{
      display: 'flex',
      justifyContent: 'center',
      alignItems: 'center',
      position: 'fixed',
      top: 0,
      left: 0,
      width: '100%',
      height: '100%',
      backgroundColor: 'rgba(0, 0, 0, 0.5)',
      zIndex: 9999,
    }}>
      <div style={{
        backgroundColor: '#fff',
        padding: '20px',
        boxShadow: '0 4px 8px 0 rgba(0, 0, 0, 0.2)',
        textAlign: 'center',
      }}>
        <span style={{
          position: 'absolute',
          top: '10px',
          right: '10px',
          fontSize: '24px',
          cursor: 'pointer',
        }} onClick={handleClose}>&times;</span>
        <h1>WELCOME TO DISASTER RESILIENCE!</h1>
        <input
          type="text"
          placeholder="Type of Emergency"
          value={emergencyType}
          onChange={(e) => setEmergencyType(e.target.value)}
          style={{ margin: '10px 0' }}
        />
        <button onClick={handleEmergencyClick} disabled={isSending} style={{
          backgroundColor: 'red',
          color: '#fff',
          border: 'none',
          borderRadius: '5px',
          padding: '10px 20px',
          cursor: 'pointer',
        }}>
          {isSending ? 'Sending...' : 'EMERGENCY'}
        </button>
        <button onClick={handleHelpRequest} disabled={isSending} style={{
          backgroundColor: 'blue',
          color: '#fff',
          padding: '10px 20px',
          margin: '10px',
          cursor: 'pointer',
        }}>
          Request Help
        </button>
        {mapInitialized && <div id="map" style={{ height: '400px', width: '100%' }}></div>}
      </div>
    </div>
  );
}

export default Popup;
