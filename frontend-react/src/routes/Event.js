import { useEffect, useState } from "react";
import { useSearchParams, useNavigate } from "react-router-dom";
import { EVENTS_URL } from "../constants";


function App() {
  const navigate = useNavigate();
  const [searchParams, setSearchParams] = useSearchParams();
  const [event, setEvent] = useState(null);
  const [name, setName] = useState("");
  const [time, setTime] = useState("");
  const [place, setPlace] = useState("");
  const [info, setInfo] = useState("");

  async function loadEvent(id) {
    if (id) {
      const url = EVENTS_URL + "events/" + id;
      console.log("Fetching event", url);
      const options = {
          method: "GET",
          headers: { "Content-Type": "application/json" }
      }
      fetch(url, options)
          .then(response => {
            if (response.ok) {
              response.json().then(data => {
                setEvent(data);
                setName(data.name);
                setTime(data.time);
                setPlace(data.place);
                setInfo(data.info);
              })
            }
          })
    }
  }

  useEffect(() => {
    console.log("Loading event", searchParams);
    loadEvent(searchParams.get("id"));
  }, []);

  const handleSubmit = e => {
    e.preventDefault();
    const url = EVENTS_URL + ((event) ? "events/" + event.id : "events");
    const options = {
        method: event ? "PUT" : "POST",
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
            if (event) {
              console.log("Updated event" + event.id);
            } else {
              response.json().then(id => {
                console.log("Created event " + id);
                navigate("/");
              });
             }
          } else {
            console.log("Error creating event", response);
          }
        })
        .catch(error => console.log("Failed creating event", error));
  }

  const handleBackNav = e => {
    e.preventDefault();
    navigate("/");
  }

  return (
    <div>
      <h2>{event ? "Ürituse muutmine" : "Ürituse lisamine"}</h2>
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
              <button onClick={handleBackNav}>Tagasi</button> <input type="submit" value={event ? "Muuda" : "Lisa"} />
            </td>
          </tr>
        </table>
      </form>
    </div>
  );
}

export default App;
