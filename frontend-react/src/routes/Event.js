import { useState } from "react";
import { EVENTS_URL } from "../constants";


function App() {
  const [name, setName] = useState("");
  const [time, setTime] = useState("");
  const [place, setPlace] = useState("");
  const [info, setInfo] = useState("");

  const handleSubmit = event => {
    event.preventDefault();
    const url = EVENTS_URL + "events";
    const options = {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify({
            "name": name,
            "time": time,
            "place": place,
            "info": info
        })
    };
    fetch(url, options)
        .then(response => {
          if (response.ok) {
            response.json().then(id => {
              console.log("Created event " + id);
            });
          } else {
            console.log("Error creating event", response);
          }
        })
        .catch(error => console.log("Failed creating event", error));
  }

  return (
    <div>
      <h2>Event</h2>
      <form onSubmit={handleSubmit}>
        <table class="form">
          <tr>
            <td>
              <label for="name">Ürituse nimi:</label>
            </td>
            <td>
              <input id="name" name="name" type="text"
                  value={name || ""}
                  onChange={e => setName(e.target.value)} />
            </td>
          </tr>
          <tr>
            <td>
              <label for="time">Toimumisaeg:</label>
            </td>
            <td>
              <input id="time" name="time" type="text"
                 value={time || ""}
                 onChange={e => setTime(e.target.value)} />
            </td>
          </tr>
          <tr>
            <td>
              <label for="place">Koht:</label>
            </td>
            <td>
              <input id="place" name="place" type="text"
                  value={place || ""}
                  onChange={e => setPlace(e.target.value)} />
            </td>
          </tr>
          <tr>
            <td>
              <label for="info">Lisainfo:</label>
            </td>
            <td>
              <textarea id="info" name="info"
                  value={info || ""}
                 onChange={e => setInfo(e.target.value)} />
            </td>
          </tr>
          <tr>
            <td colSpan="2">
              <button>Tagasi</button> <input type="submit" value="Lisa" />
            </td>
          </tr>
        </table>
      </form>
    </div>
  );
}

export default App;
