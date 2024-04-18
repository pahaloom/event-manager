import { useEffect, useState } from "react";
import { EVENTS_URL } from "../constants";
import { Link } from "react-router-dom";


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
              setFutureEvents(events);
              setPastEvents(events);
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
          <td><Link to={`/event?id=${eventId}`}>OSAVÕTJAD</Link></td>
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
    </div>
  );
}

export default App;
