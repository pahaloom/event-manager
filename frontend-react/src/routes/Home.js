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

  const handleRemove = e=> {
    const eventId = e.currentTarget.getAttribute("event-id");
    if (!window.confirm("Oled kindel, et soovid sündmust eemaldada?")) {
      return;
    }
    console.log("Removing event", eventId);
    const url = EVENTS_URL + "events/" + eventId;
    const params = {
      method: "DELETE",
      Headers: { "Content-Type": "application/json" }
    }
    fetch(url, params)
        .then((response) => {
          if (response.ok) {
            loadEvents();
          }
        })
  }

  useEffect(() => {
    loadEvents();
  }, []);

  function EventList({ hasDelete, title, events }) {
    console.log("EventList", title, events);
    const rows = [];
    events.forEach((event) => {
      const eventId = event.id;
      rows.push(
        <tr key={event.id}>
          <td><Link to={`/event?id=${eventId}`} >{event.name}</Link></td>
          <td>{event.time}</td>
          <td>{event.place}</td>
          <td>{event.size}</td>
          <td><Link to={`/participants?id=${eventId}`}>OSAVÕTJAD</Link></td>
          {hasDelete && <td><button onClick={handleRemove} event-id={event.id}><img src="remove.svg" width="20" height="20" alt="remove" /></button></td>}
        </tr>
      );
    });
    return (
      <table>
        <thead>
          <tr>
            <th colSpan={hasDelete ? 6 : 5}>{title}</th>
          </tr>
        </thead>
        <tbody>
          {rows}
        </tbody>
     </table>
    );
  }

  return (
    <div className="content">
      <h2>Home</h2>
      <div class="flex-container">
        <EventList title="Tulevased üritused" hasDelete={true} events={futureEvents} />
        <EventList title="Toimunud üritused" hasDelete={false} events={pastEvents} />
      </div>
      <Link to="/event">LISA ÜRITUS</Link>
    </div>
  );
}

export default App;
