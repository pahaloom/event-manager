import { useEffect, useState } from "react";
import { Link } from "react-router-dom";
import moment from "moment";
import { EVENTS_URL } from "../constants";


function App() {
  const [pastEvents, setPastEvents] = useState([]);
  const [futureEvents, setFutureEvents] = useState([]);

  async function loadEvents() {
    const url = EVENTS_URL + "events";
    const options = {
        "method": "GET",
        "Content-Type": "application/json"
    }
    fetch(url, options)
        .then(response => {
          if (response.ok) {
            response.json().then((events) => {
              const past = [];
              const future = [];
              const now = new Date();
              events.forEach((e) => {
                const eventDate = moment(e.time, "YYYY-MM-DDThh:mm:ss.SZ").toDate();
                if (now > eventDate) {
                  past.push(e);
                } else {
                  future.push(e);
                }
              });
              setFutureEvents(future);
              setPastEvents(past);
            });
          }
        });
  }

  useEffect(() => {
    loadEvents();
  }, []);

  function EventList({ title, events }) {
    console.log("EventList", title, events);
    const rows = [];
    events.forEach((event) => {
      const eventId = event.id;
      rows.push(
        <tr key={event.id}>
          <td>{event.name}</td>
          <td>{event.time}</td>
          <td>{event.place}</td>
          <td>{event.size}</td>
          <td><Link to={`/participants?id=${eventId}`}>OSAVÕTJAD</Link></td>
        </tr>
      );
    });
    return (
      <table>
        <thead>
          <tr colSpan="5">
            <th>{title}</th>
          </tr>
        </thead>
        <tbody>
          {rows}
        </tbody>
     </table>
    );
  }

  return (
    <div>
      <h2>Home</h2>
      <div class="flex-container">
        <EventList title="Tulevased üritused" events={futureEvents} />
        <EventList title="Toimunud üritused" events={pastEvents} />
      </div>
      <Link to="/event">LISA ÜRITUS</Link>
    </div>
  );
}

export default App;
