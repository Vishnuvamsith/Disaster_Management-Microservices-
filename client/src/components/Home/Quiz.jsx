import React, { useState, useEffect } from 'react';
import axios from 'axios';
import L from 'leaflet';
import 'leaflet/dist/leaflet.css';
import customMarkerIconImage from '../../assets/home/mark.png';

function QuizSection() {
  const [isSending, setIsSending] = useState(false);
  const [showMapPopup, setShowMapPopup] = useState(false);
  const [userLocation, setUserLocation] = useState(null);
  const [nearbySafePlaces, setNearbySafePlaces] = useState([]);
  const [emergencyType, setEmergencyType] = useState('General Emergency');

  const handleEmergencyClick = () => {
    setIsSending(true);
    navigator.geolocation.getCurrentPosition(async (position) => {
      const { latitude, longitude } = position.coords;
      try {
        await axios.post('http://localhost:8085/email/sendcoord', {
          latitude,
          longitude,
          emergencyType,
        });
        alert('Emergency alert sent successfully');
      } catch (error) {
        console.error('Error sending emergency alert:', error);
        alert('Failed to send emergency alert');
      } finally {
        setIsSending(false);
      }
    }, (error) => {
      console.error('Geolocation error:', error);
      setIsSending(false);
    });
  };

  const handleHelpRequest = () => {
    setIsSending(true);
    navigator.geolocation.getCurrentPosition(async (position) => {
      const { latitude, longitude } = position.coords;
      setUserLocation({ latitude, longitude });
      try {
        const response = await axios.get('http://localhost:8086/alert/nearby-safe-places', {
          params: { userLat: latitude, userLon: longitude }
        });
        console.log(response.data)
        setNearbySafePlaces(response.data);
        setShowMapPopup(true);
      } catch (error) {
        console.error('Error fetching nearby safe places:', error);
        alert('Failed to fetch nearby safe places');
      } finally {
        setIsSending(false);
      }
    }, (error) => {
      console.error('Geolocation error:', error);
      setIsSending(false);
    });
  };

  useEffect(() => {
    let map;

    if (showMapPopup && userLocation) {
      map = L.map('map', {
        center: [userLocation.latitude, userLocation.longitude],
        zoom: 13,
      });

      L.tileLayer('https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png', {
        maxZoom: 19,
        attribution: '© OpenStreetMap'
      }).addTo(map);

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
      });

      return () => map.remove();
    }
  }, [showMapPopup, userLocation, nearbySafePlaces]);

  return (
    <section className="my-36">
      <div className="bg-quiz-bg h-auto sm:bg-cover bg-center bg-no-repeat rounded-xl relative">
        <article className="py-24 md:px-14 px-4 md:w-9/12 md:mx-0 mx-auto md:text-left text-center leading-none">
          <h1 className="font-bold lg:text-[60px] text-[50px] text-white pb-8">
            Are you stuck in a helpless situation?
          </h1>
          <button className="capitalize bg-button-primary hover:bg-button-primary-hover transition-colors px-14 py-3 rounded-sm font-bold text-white mx-6"
                  onClick={handleHelpRequest} disabled={isSending}>
            Help
          </button>
          <button className="capitalize bg-button-primary hover:bg-button-primary-hover transition-colors px-14 py-3 rounded-sm font-bold text-white"
                  onClick={handleEmergencyClick} disabled={isSending}>
            Emergency
          </button>
        </article>
        {showMapPopup && (
          <div className="absolute inset-0 bg-gray-600 bg-opacity-50 flex justify-center items-center">
            <div className="bg-white p-10 rounded-lg shadow-lg relative">
              <button onClick={() => setShowMapPopup(false)} className="absolute top-0 right-0 p-2">X</button>
              <div id="map" style={{ height: '400px', width: '500px' }}></div>
            </div>
          </div>
        )}
      </div>
    </section>
  );
}

export default QuizSection;
